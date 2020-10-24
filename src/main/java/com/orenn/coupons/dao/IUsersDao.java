package com.orenn.coupons.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.orenn.coupons.entities.UserEntity;
import com.orenn.coupons.enums.UserType;

public interface IUsersDao extends CrudRepository<UserEntity, Long>{

	public UserEntity findByEmail(String email);

	public UserEntity findByUserName(String userName);

	public UserEntity findByUserNameAndPassword(String userName, String hashedPassword);

	public boolean existsByUserName(String userName);
	
	public boolean existsByEmail(String email);
	
	public List<UserEntity> findAllByCompanyId(long companyId);

	public List<UserEntity> findAllByType(UserType userType);
	
	@Modifying
	@Transactional
	@Query("UPDATE UserEntity SET lock_user = ?2 WHERE id = ?1")
	public void lock(long id, boolean toLock);

	@Modifying
	@Transactional
	@Query("UPDATE UserEntity SET password = ?2 WHERE email = ?1")
	public void updatePassword(String email, String password);


}
