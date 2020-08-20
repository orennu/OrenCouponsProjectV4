package com.orenn.coupons.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.orenn.coupons.entities.PurchaseEntity;

public interface IPurchasesDao extends CrudRepository<PurchaseEntity, Long>{
	
	@Query("FROM PurchaseEntity WHERE purchase_date = ?1")
	public List<PurchaseEntity> findAllByPurchaseDate(String purchaseDate);
	
	@Query("SELECT COUNT(id) FROM PurchaseEntity WHERE purchase_date = ?1")
	public long getCountByPurchaseDate(String purchaseDate);

	public List<PurchaseEntity> findAllByCustomerId(long customerId);
	
	@Query("SELECT COUNT(id) FROM PurchaseEntity WHERE customer_id = ?1")
	public long getCountByCustomerId(long customerId);

	public List<PurchaseEntity> findAllByCouponId(long couponId);
	
	@Query("SELECT COUNT(id) FROM PurchaseEntity WHERE coupon_id = ?1")
	public long getCountByCouponId(long couponId);
	
	@Query("SELECT CASE WHEN COUNT(p.id) > 0 THEN true ELSE false END FROM PurchaseEntity p WHERE customer_id = ?1 AND coupon_id = ?2")
	public boolean existsByCustomerIdAndCouponId(long customerId, long couponId);

}
