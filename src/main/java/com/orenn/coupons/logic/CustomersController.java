package com.orenn.coupons.logic;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.orenn.coupons.dao.ICustomersDao;
import com.orenn.coupons.entities.CustomerEntity;
import com.orenn.coupons.enums.ErrorType;
import com.orenn.coupons.enums.UserType;
import com.orenn.coupons.exceptions.ApplicationException;
import com.orenn.coupons.utils.DateUtils;
import com.orenn.coupons.utils.ValidationsUtils;

@Controller
public class CustomersController {
	
	// validations
	
	@Autowired
	private ICustomersDao customersDao;

	@Autowired
	private UsersController usersController;
	
	@Transactional
	public long addCustomer(CustomerEntity customer) throws ApplicationException {
		if (!isCustomerAttributesValid(customer)) {
			throw new ApplicationException();
		}
		if (this.customersDao.existsById(customer.getId())) {
			throw new ApplicationException(ErrorType.ALREADY_EXISTS_ERROR, 
					String.format("customer with id %s %s", 
							customer.getId(), ErrorType.ALREADY_EXISTS_ERROR.getErrorDescription()));
		}
		if (this.customersDao.existsByPhoneNumber(customer.getPhoneNumber())) {
			throw new ApplicationException(ErrorType.ALREADY_EXISTS_ERROR,
					String.format("customer with phone number %s %s", 
							customer.getPhoneNumber(), ErrorType.ALREADY_EXISTS_ERROR.getErrorDescription()));
		}
		
		long id = this.usersController.addUser(customer.getUser());
		customer.setId(id);
		
		try {
			return this.customersDao.save(customer).getId();
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.CREATE_ERROR, ErrorType.CREATE_ERROR.getErrorDescription());
		}
	}

	public CustomerEntity getCustomerById(long id) throws ApplicationException {
		if (id < 1) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, String.format("%s, id cannot be less than 1", 
					ErrorType.INVALID_FORMAT_ERROR.getErrorDescription()));
		}
		
		Optional<CustomerEntity> customer;
		try {
			customer = this.customersDao.findById(id);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
		
		if (!customer.isPresent()) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
					String.format("customer id %s %s", id, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		return customer.get();
	}

	public CustomerEntity getCustomerByPhoneNumber(String phone) throws ApplicationException {
		if (!ValidationsUtils.isValidPhoneNumber(phone)) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, 
										String.format("%s, phone number must be 6 - 14 digits", ErrorType.INVALID_FORMAT_ERROR.getErrorDescription()));
		}
		
		CustomerEntity customer;
		try {
			customer = this.customersDao.findByPhoneNumber(phone);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
		
		if (customer == null) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
					String.format("customer phone %s %s", phone, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		return customer;
	}

	public List<CustomerEntity> getAllCustomers() throws ApplicationException {
		try {
			return (List<CustomerEntity>) this.customersDao.findAll();
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}

	public long getCustomersCount() throws ApplicationException {
		try {
			return this.customersDao.count();
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}
	
	public List<CustomerEntity> getCustomersByDateOfBirth(String dateOfBirth) throws ApplicationException {
		if (dateOfBirth == null) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s date of birth", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		
		try {
			return this.customersDao.findAllByDateOfBirth(dateOfBirth);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}
	
	@Transactional
	public void updateCustomer(CustomerEntity customer) throws ApplicationException {
		if (!isCustomerAttributesValid(customer)) {
			throw new ApplicationException();
		}
		if (!isCustomerExistsById(customer.getId())) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, String.format("customer id %s %s" ,customer.getId(), 
					ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		try {
			this.usersController.updateUser(customer.getUser());
			this.customersDao.save(customer);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorType.UPDATE_ERROR, ErrorType.UPDATE_ERROR.getErrorDescription());
		}
	}

	public void deleteCustomerById(long id) throws ApplicationException {
		if (!isCustomerExistsById(id)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
					String.format("customer id %s %s", id, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		try {
			this.customersDao.deleteById(id);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.DELETE_ERROR, ErrorType.DELETE_ERROR.getErrorDescription());
		}
	}
	
	public boolean isCustomerExistsById(long id) throws ApplicationException {
		if (id < 1) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, String.format("%s, id cannot be less than 1", 
					ErrorType.INVALID_FORMAT_ERROR.getErrorDescription()));
		}
		
		try {
			return this.customersDao.existsById(id);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}

	private boolean isCustomerAttributesValid(CustomerEntity customer) throws ApplicationException {
		if (customer == null) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s customer", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		if (customer.getUser().getType() != UserType.CUSTOMER) {
			throw new ApplicationException(ErrorType.FORBIDDEN_TYPE, String.format("%s for user of type customer", ErrorType.FORBIDDEN_TYPE));
		}
		if (!ValidationsUtils.isValidLength(customer.getFullName(), 3, 30)) {
			throw new ApplicationException(ErrorType.INVALID_LENGTH_ERROR, 
										String.format("%s, customer name must be between 3-30 characters", ErrorType.INVALID_LENGTH_ERROR.getErrorDescription()));
		}
		if (!ValidationsUtils.isValidName(customer.getFirstName())) {
			throw new ApplicationException();
		}
		if (!ValidationsUtils.isValidName(customer.getLastName())) {
			throw new ApplicationException();
		}
		if (!ValidationsUtils.isValidPhoneNumber(customer.getPhoneNumber())) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, 
										String.format("%s, phone number must be 6 - 14 digits", ErrorType.INVALID_FORMAT_ERROR.getErrorDescription()));
		}
		if (!ValidationsUtils.isValidAddress(customer.getAddress())) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, 
										String.format("%s in address", ErrorType.INVALID_FORMAT_ERROR.getErrorDescription()));
		}
		if (customer.getDateOfBirth() == null) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s date of birth", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		if (customer.getDateOfBirth().after(new Date())) {
			throw new ApplicationException(ErrorType.INVALID_DATE_ERROR, 
										String.format("%s, birth date cannot be in the future", ErrorType.INVALID_DATE_ERROR.getErrorDescription()));
		}
		if (DateUtils.calculateAge(customer.getDateOfBirth()) < 18) {
			throw new ApplicationException(ErrorType.INVALID_DATE_ERROR, 
										String.format("%s, customer must be 18 years old or older", ErrorType.INVALID_DATE_ERROR.getErrorDescription()));
		}
		
		return true;
	}
}

