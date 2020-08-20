package com.orenn.coupons.dao;

import java.sql.Timestamp;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.orenn.coupons.entities.CouponEntity;
import com.orenn.coupons.enums.CouponCategory;

public interface ICouponsDao extends CrudRepository<CouponEntity, Long>{

	public List<CouponEntity> findAllByCategory(CouponCategory category);

	public List<CouponEntity> findAllByCompanyId(long companyId);

	public boolean existsByTitle(String title);

	@Modifying
	@Query("FROM CouponEntity WHERE price <= ?1")
	public List<CouponEntity> findAllByPrice(float price);
	
	@Modifying
	@Transactional
	@Query("UPDATE CouponEntity SET quantity = quantity + ?2 WHERE id = ?1")
	public void updateCouponQuantityById(long id, int quantity);
	
	@Modifying
	@Transactional
	@Query("UPDATE CouponEntity SET quantity = 0 WHERE id = ?1")
	// We don't really delete coupons to not loose reference to old customers' purchases
	public void deleteById(long id);
	
	@Modifying
	@Transactional
	@Query("UPDATE CouponEntity SET quantity = 0 WHERE expiration_date < ?1")
	public void deleteExpiredCoupons(Timestamp sqlDateTime);

}
