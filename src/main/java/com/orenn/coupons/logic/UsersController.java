package com.orenn.coupons.logic;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.orenn.coupons.beans.PostLoginData;
import com.orenn.coupons.beans.SuccessfulLoginData;
import com.orenn.coupons.beans.UserLoginData;
import com.orenn.coupons.dao.IUsersDao;
import com.orenn.coupons.entities.UserEntity;
import com.orenn.coupons.enums.ErrorType;
import com.orenn.coupons.enums.UserType;
import com.orenn.coupons.exceptions.ApplicationException;
import com.orenn.coupons.utils.StringUtils;
import com.orenn.coupons.utils.ValidationsUtils;

@Controller
public class UsersController {
	
	@Autowired
	private IUsersDao usersDao;
	@Autowired
	private CompaniesController companiesController;
	@Autowired
	private CacheController cacheController;

	public SuccessfulLoginData login(UserLoginData userLoginData) throws ApplicationException {
		if (!isLoginDataValid(userLoginData)) {
			throw new ApplicationException();
		}
		
		try {
			PostLoginData postLoginData = new PostLoginData();
			String hashedPassword = hashPassword(userLoginData.getPassword());
			UserEntity user = this.usersDao.findByUserNameAndPassword(userLoginData.getUserName(), hashedPassword);
			if (user == null) {
				throw new ApplicationException();
			}
			postLoginData.setId(user.getId());
			postLoginData.setType(user.getType());
			if (user.getType().equals(UserType.COMPANY)) {
				postLoginData.setCompanyId(user.getCompany().getId());
			}
			
			String token = StringUtils.generateToken();
			cacheController.put(token, postLoginData);
			
			return new SuccessfulLoginData(token, postLoginData.getType());
			
		} catch (ApplicationException e) {
			throw new ApplicationException(ErrorType.LOGIN_FAILED, ErrorType.LOGIN_FAILED.getErrorDescription());
			
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
		
	}

	public long addUser(UserEntity user) throws ApplicationException {
		if (!isUserAttributesValid(user)) {
			throw new ApplicationException();
		}
		if (isUserExistsByUserName(user.getUserName())) {
			throw new ApplicationException(ErrorType.ALREADY_EXISTS_ERROR, String.format("username %s, %s", user.getUserName(), 
					ErrorType.ALREADY_EXISTS_ERROR.getErrorDescription()));
		}
		if (isUserExistsByEmail(user.getEmail())) {
			throw new ApplicationException(ErrorType.ALREADY_EXISTS_ERROR, String.format("email %s, %s", user.getEmail(), 
					ErrorType.ALREADY_EXISTS_ERROR.getErrorDescription()));
		}
		try {
			user.setPassword(hashPassword(user.getPassword()));
			
			return this.usersDao.save(user).getId();
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.CREATE_ERROR, ErrorType.CREATE_ERROR.getErrorDescription());
		}
	}

	public UserEntity getUserById(long id) throws ApplicationException {
		if (id < 1) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, String.format("%s, id cannot be less than 1", 
					ErrorType.INVALID_FORMAT_ERROR.getErrorDescription()));
		}
		
		Optional<UserEntity> user;
		try {
			user = this.usersDao.findById(id);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
		
		if (!user.isPresent()) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
					String.format("user id %s %s", id, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		return user.get();
	}

	public List<UserEntity> getAllUsers() throws ApplicationException {
		try {
			return (List<UserEntity>) this.usersDao.findAll();
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}
	
	public long getUsersCount() throws ApplicationException {
		try {
			return this.usersDao.count();
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}
	
	public List<UserEntity> getUsersByCompanyId(long companyId) throws ApplicationException {
		if (!this.companiesController.isCompanyExistsById(companyId)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR,
										String.format("company id %s %s", companyId, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		try {
			return this.usersDao.findAllByCompanyId(companyId);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}
	
	public List<UserEntity> getUsersByType(String userTypeStr) throws ApplicationException {
		if (userTypeStr == null) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s user type", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		if (!ValidationsUtils.isUserTypeExists(userTypeStr)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
					String.format("user type %s %s", userTypeStr, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		try {
			UserType userType = UserType.valueOf(userTypeStr);
			
			return this.usersDao.findAllByType(userType);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}

	public UserEntity getUserByUserName(String userName) throws ApplicationException {
		if (!ValidationsUtils.isValidUserName(userName)) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, 
										String.format("%s, %s", ErrorType.INVALID_FORMAT_ERROR.getErrorDescription(), userName));
		}
		
		UserEntity user;
		try {
			user = this.usersDao.findByUserName(userName);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
		
		if (user == null) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
					String.format("username %s %s", userName, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		return user;
	}

	public void updateUser(UserEntity user) throws ApplicationException {
		if (!isUserAttributesValid(user)) {
			throw new ApplicationException();
		}
		if (!isUserExistsById(user.getId())) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, String.format("user id %s %s" ,user.getId(), 
					ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		try {
			user.setPassword(hashPassword(user.getPassword()));
			this.usersDao.save(user);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.UPDATE_ERROR, ErrorType.UPDATE_ERROR.getErrorDescription());
		}
	}
	
	public void lockUser(long id, boolean toLock) throws ApplicationException {
		if (!isUserExistsById(id)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, String.format("user id %s %s" ,id ,ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		try {
			this.usersDao.lock(id, toLock);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.UPDATE_ERROR, ErrorType.UPDATE_ERROR.getErrorDescription());
		}
	}

	public void deleteUserById(long id) throws ApplicationException {
		if (!isUserExistsById(id)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
					String.format("user id %s %s", id, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		try {
			this.usersDao.deleteById(id);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.DELETE_ERROR, ErrorType.DELETE_ERROR.getErrorDescription());
		}
	}
	
	public boolean isUserExistsById(long id) throws ApplicationException {
		if (id < 1) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, String.format("%s, id cannot be less than 1, %s", 
					ErrorType.INVALID_FORMAT_ERROR.getErrorDescription(), id));
		}
		
		try {
			return this.usersDao.existsById(id);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}

	public boolean isUserExistsByEmail(String email) throws ApplicationException {
		if (!ValidationsUtils.isValidEmail(email)) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, 
										String.format("%s, %s", ErrorType.INVALID_FORMAT_ERROR.getErrorDescription(), email));
		}
		
		try {
			return this.usersDao.existsByEmail(email);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}
	
	public boolean isUserExistsByUserName(String userName) throws ApplicationException {
		if (!ValidationsUtils.isValidUserName(userName)) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, 
										String.format("%s, %s", ErrorType.INVALID_FORMAT_ERROR.getErrorDescription(), userName));
		}
		
		try {
			return this.usersDao.existsByUserName(userName);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}
	
	private boolean isUserAttributesValid(UserEntity user) throws ApplicationException {
		if (user == null) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s user", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		if (!ValidationsUtils.isValidUserName(user.getUserName())) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, 
										String.format("%s, %s", ErrorType.INVALID_FORMAT_ERROR.getErrorDescription(), user.getUserName()));
		}
		if (!ValidationsUtils.isValidPassword(user.getPassword())) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, 
										String.format("%s, %s", ErrorType.INVALID_FORMAT_ERROR.getErrorDescription(), user.getPassword()));
		}
		if (!ValidationsUtils.isValidEmail(user.getEmail())) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, 
										String.format("%s, %s", ErrorType.INVALID_FORMAT_ERROR.getErrorDescription(), user.getEmail()));
		}
		if (!ValidationsUtils.isValidUserType(user.getType())) {
			throw new ApplicationException();
		}
		if (user.getType().equals(UserType.COMPANY)) {
			if (!companiesController.isCompanyExistsById(user.getCompany())) {
				throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
						String.format("company id %s %s", user.getCompany().getId(), ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
			}
		}
		
		return true;
	}
	
	private boolean isLoginDataValid(UserLoginData userLoginData) throws ApplicationException {
		if (userLoginData == null) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s user login data", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		if (!ValidationsUtils.isValidUserName(userLoginData.getUserName())) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, 
										String.format("%s, %s", ErrorType.INVALID_FORMAT_ERROR.getErrorDescription(), userLoginData.getUserName()));
		}
		if (!ValidationsUtils.isValidPassword(userLoginData.getPassword())) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, 
										String.format("%s, %s", ErrorType.INVALID_FORMAT_ERROR.getErrorDescription(), userLoginData.getPassword()));
		}
		
		return true;
	}
	
	public String hashPassword(String password) {
		String hashedPassword = StringUtils.generateHashedPassword(password);
		return hashedPassword;
	}
	
}

