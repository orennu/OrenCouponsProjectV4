package com.orenn.coupons;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.orenn.coupons.dao.IUsersDao;
import com.orenn.coupons.entities.UserEntity;
import com.orenn.coupons.enums.UserType;
import com.orenn.coupons.logic.UsersController;

@Component
public class DBInit {
	
	@Autowired
	private IUsersDao usersDao;
	
	@Autowired
	private UsersController userController;
	
	@PostConstruct
	public void init() {
		
		UserEntity superadmin = new UserEntity();
		superadmin.setUserName("superadmin");
		superadmin.setPassword(userController.hashPassword("Test12345!!"));
		superadmin.setEmail("superadmin@localhost.local");
		superadmin.setType(UserType.ADMIN);
		
		if (!usersDao.existsByEmail(superadmin.getEmail())) {
			this.usersDao.save(superadmin);
		}
		
	}

}
