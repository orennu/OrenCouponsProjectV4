package com.orenn.coupons.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orenn.coupons.beans.SuccessfulLoginData;
import com.orenn.coupons.beans.UserLoginData;
import com.orenn.coupons.entities.UserEntity;
import com.orenn.coupons.exceptions.ApplicationException;
import com.orenn.coupons.logic.UsersController;

@RestController
@RequestMapping("/users")
public class UsersApi {
	
	@Autowired
	private UsersController usersController;
	
	@PostMapping("/login")
	public SuccessfulLoginData login(@RequestBody UserLoginData userLoginData) throws ApplicationException {
		return this.usersController.login(userLoginData);
	}
	
	@PostMapping("/register")
	public long addUser(@RequestBody UserEntity user) throws ApplicationException {
		return this.usersController.addUser(user);
	}
	
	@GetMapping("/{id}")
	public UserEntity getUserById(@PathVariable("id") long id) throws ApplicationException {
		return this.usersController.getUserById(id);
	}
	
	@GetMapping
	public List<UserEntity> getAllUsers() throws ApplicationException {
		return this.usersController.getAllUsers();
	}
	
	@GetMapping("/count")
	public long getUsersCount() throws ApplicationException {
		return this.usersController.getUsersCount();
	}
	
	@GetMapping(value = "/search", params = "type")
	public List<UserEntity> getUsersByType(@RequestParam("type") String userTypeStr) throws ApplicationException {
		return this.usersController.getUsersByType(userTypeStr);
	}

	@GetMapping(value = "/search", params = "companyId")
	public List<UserEntity> getUsersByCompanyId(@RequestParam("companyId") Long companyId) throws ApplicationException {
		return this.usersController.getUsersByCompanyId(companyId);
	}
	
	@GetMapping(value = "/search", params = "userName")
	public UserEntity getUserByUserName(@RequestParam("userName") String userName) throws ApplicationException {
		return this.usersController.getUserByUserName(userName);
	}
	
	@PutMapping
	public void updateUser(@RequestBody UserEntity user) throws ApplicationException {
		this.usersController.updateUser(user);
	}
	
	@PutMapping(value = "/{id}")
	public void lockUser(@PathVariable("id") long id, @RequestParam("lock") boolean toLock) throws ApplicationException {
		this.usersController.lockUser(id, toLock);
	}
	
	@DeleteMapping("/{id}")
	public void deleteUserById(@PathVariable("id") long id) throws ApplicationException {
		this.usersController.deleteUserById(id);
	}
	
}
