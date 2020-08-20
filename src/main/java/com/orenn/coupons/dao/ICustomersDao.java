package com.orenn.coupons.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.orenn.coupons.entities.CustomerEntity;

public interface ICustomersDao extends CrudRepository<CustomerEntity, Long>{

	public CustomerEntity findByPhoneNumber(String phone);

	@Query("FROM CustomerEntity WHERE date_of_birth = ?1")
	public List<CustomerEntity> findAllByDateOfBirth(String dateOfBirth);

}
