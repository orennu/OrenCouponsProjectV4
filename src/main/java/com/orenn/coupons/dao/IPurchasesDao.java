package com.orenn.coupons.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.orenn.coupons.beans.PurchaseData;
import com.orenn.coupons.entities.PurchaseEntity;

public interface IPurchasesDao extends CrudRepository<PurchaseEntity, Long>{
	
	@Query("FROM PurchaseEntity WHERE purchase_date = ?1")
	public List<PurchaseEntity> findAllByPurchaseDate(String purchaseDate);
	
	@Query("SELECT COUNT(id) FROM PurchaseEntity WHERE purchase_date = ?1")
	public long getCountByPurchaseDate(String purchaseDate);

	@Query("SELECT new com.orenn.coupons.beans.PurchaseData(p.purchaseDate, p.quantity, c.title, c.price, c.category, c.imageUuid) FROM PurchaseEntity p JOIN CouponEntity c ON p.couponId = c.id WHERE customer_id = ?1")
	public List<PurchaseData> findAllByCustomerId(long customerId);
	
	@Query("SELECT COUNT(id) FROM PurchaseEntity WHERE customer_id = ?1")
	public long getCountByCustomerId(long customerId);

	public List<PurchaseEntity> findAllByCouponId(long couponId);
	
	@Query("SELECT COUNT(id) FROM PurchaseEntity WHERE coupon_id = ?1")
	public long getCountByCouponId(long couponId);
	
	@Query("SELECT CASE WHEN COUNT(p.id) > 0 THEN true ELSE false END FROM PurchaseEntity p WHERE customer_id = ?1 AND coupon_id = ?2")
	public boolean existsByCustomerIdAndCouponId(long customerId, long couponId);
	
	@Query("SELECT new com.orenn.coupons.beans.PurchaseData(p.id, p.purchaseDate, p.quantity, cp.title, cp.price, cp.category, cs.firstName, cs.lastName, cp.imageUuid) FROM PurchaseEntity p JOIN CouponEntity cp ON p.couponId = cp.id JOIN CompanyEntity cm ON cp.company = cm.id JOIN CustomerEntity cs ON p.customer = cs.id WHERE cm.id = ?1")
	public List<PurchaseData> findAllByComapnyId(long comapnyId);
	
	@Modifying
	@Transactional
	@Query("UPDATE PurchaseEntity SET quantity = quantity + ?3 WHERE customer_id = ?1 AND coupon_id = ?2")
	public void updatePurchaseQuantityByCustomerAndCouponId(long customerId, long couponId, int quantity);

}
