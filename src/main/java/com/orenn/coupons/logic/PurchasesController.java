package com.orenn.coupons.logic;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.orenn.coupons.dao.IPurchasesDao;
import com.orenn.coupons.entities.PurchaseEntity;
import com.orenn.coupons.enums.ErrorType;
import com.orenn.coupons.exceptions.ApplicationException;

@Controller
public class PurchasesController {
	
	@Autowired
	private IPurchasesDao purchasesDao;
	
	@Autowired
	private CustomersController customersController;
	
	@Autowired
	private CouponsController couponsController;
	
	@Transactional
	public long addPurchase(PurchaseEntity purchase) throws ApplicationException {
		if (!isPurchaseAttributesValid(purchase)) {
			throw new ApplicationException();
		}
		if (isPurchaseExistsByCustomerAndCouponId(purchase.getCustomer().getId(), purchase.getCouponId())) {
			throw new ApplicationException(ErrorType.ALREADY_EXISTS_ERROR, String.format("purchase %s", ErrorType.ALREADY_EXISTS_ERROR.getErrorDescription()));
		}
		
		try {
			this.couponsController.updateCouponQuantityById(purchase.getCouponId(), purchase.getQuantity(), false);
			return this.purchasesDao.save(purchase).getId();
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.CREATE_ERROR, ErrorType.CREATE_ERROR.getErrorDescription());
		}
	}

	public PurchaseEntity getPurchaseById(long id) throws ApplicationException {
		if (!isPurchaseExistsById(id)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
					String.format("purchase id %s %s", id, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		Optional<PurchaseEntity> purchase;
		try {
			purchase = this.purchasesDao.findById(id);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
		
		if (!purchase.isPresent()) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
					String.format("purchase id %s %s", id, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		return purchase.get();
	}

	public List<PurchaseEntity> getAllPurchases() throws ApplicationException {
		try {
			return (List<PurchaseEntity>) this.purchasesDao.findAll();
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}

	public long getPurchasesCount() throws ApplicationException {
		try {
			return this.purchasesDao.count();
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}

	public List<PurchaseEntity> getPurchasesByDate(String purchaseDate) throws ApplicationException {
		if (purchaseDate == null) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s purchase date", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		
		try {
			return this.purchasesDao.findAllByPurchaseDate(purchaseDate);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}

	public long getPurchasesCountByDate(String purchaseDate) throws ApplicationException {
		if (purchaseDate == null) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s purchase date", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		
		try {
			return this.purchasesDao.getCountByPurchaseDate(purchaseDate);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}
	
	public List<PurchaseEntity> getPurchasesByCustomerId(long customerId) throws ApplicationException {
		if (!this.customersController.isCustomerExistsById(customerId)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR,
					String.format("company id %s %s", customerId, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		try {
			return this.purchasesDao.findAllByCustomerId(customerId);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}
	
	public long getPurchasesCountByCustomerId(long customerId) throws ApplicationException {
		if (!this.customersController.isCustomerExistsById(customerId)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR,
					String.format("company id %s %s", customerId, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		try {
			return this.purchasesDao.getCountByCustomerId(customerId);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}
	
	public List<PurchaseEntity> getPurchasesByCouponId(long couponId) throws ApplicationException {
		if (!this.couponsController.isCouponExistsById(couponId)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR,
					String.format("coupon id %s %s", couponId, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		try {
			return this.purchasesDao.findAllByCouponId(couponId);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}
	
	public long getPurchasesCountByCouponId(long couponId) throws ApplicationException {
		if (!this.couponsController.isCouponExistsById(couponId)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR,
					String.format("coupon id %s %s", couponId, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		try {
			return this.purchasesDao.getCountByCouponId(couponId);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}
	
	public void deletePurchaseById(long id) throws ApplicationException {
		if (!isPurchaseExistsById(id)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
					String.format("purchase id %s %s", id, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		try {
			this.purchasesDao.deleteById(id);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.DELETE_ERROR, ErrorType.DELETE_ERROR.getErrorDescription());
		}
	}
	
	private boolean isPurchaseExistsById(long id) throws ApplicationException {
		if (id < 1) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, String.format("%s, id cannot be less than 1", 
					ErrorType.INVALID_FORMAT_ERROR.getErrorDescription()));
		}
		
		try {
			return this.purchasesDao.existsById(id);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}
	
	private boolean isPurchaseExistsByCustomerAndCouponId(long customerId, long couponId) throws ApplicationException {
		if (customerId < 1 || couponId < 1) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, String.format("%s, id cannot be less than 1", 
					ErrorType.INVALID_FORMAT_ERROR.getErrorDescription()));
		}
		
		try {
			return this.purchasesDao.existsByCustomerIdAndCouponId(customerId, couponId);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}
	
	private boolean isPurchaseAttributesValid(PurchaseEntity purchase) throws ApplicationException {
		if (purchase == null) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s purchase", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		if (!this.couponsController.isCouponExistsById(purchase.getCouponId())) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
					String.format("coupon id %s %s", purchase.getCouponId(), ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		if (!this.customersController.isCustomerExistsById(purchase.getCustomer().getId())) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
					String.format("customer id %s %s", purchase.getCustomer().getId(), ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		if (purchase.getQuantity() <= 0) {
			throw new ApplicationException(ErrorType.INVALID_QUANTITY_ERROR, 
										String.format("%s, must be greater than 0", ErrorType.INVALID_QUANTITY_ERROR.getErrorDescription()));
		}
		if (purchase.getQuantity() > this.couponsController.getCouponQuantityById(purchase.getCouponId())) {
			throw new ApplicationException(ErrorType.INVALID_QUANTITY_ERROR, 
										String.format("%s cannot be greater than quantity of coupons", ErrorType.INVALID_QUANTITY_ERROR.getErrorDescription()));
		}
		
		return true;
	}

}
