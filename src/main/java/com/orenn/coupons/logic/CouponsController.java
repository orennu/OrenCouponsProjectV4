package com.orenn.coupons.logic;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.orenn.coupons.dao.ICouponsDao;
import com.orenn.coupons.entities.CouponEntity;
import com.orenn.coupons.enums.CouponCategory;
import com.orenn.coupons.enums.ErrorType;
import com.orenn.coupons.exceptions.ApplicationException;
import com.orenn.coupons.utils.DateUtils;
import com.orenn.coupons.utils.ValidationsUtils;

@Controller
public class CouponsController {
	
	@Autowired
	private ICouponsDao couponsDao;
	
	@Autowired
	private CompaniesController companiesController;

	public long addCoupon(CouponEntity coupon) throws ApplicationException {
		if (!isCouponAttributesValid(coupon)) {
			throw new ApplicationException();
		}
		if (isCouponExistsByCompanyId(coupon.getCompany().getId()) && isCouponExistsByTitle(coupon.getTitle())) {
			throw new ApplicationException(ErrorType.ALREADY_EXISTS_ERROR, 
					String.format("coupon %s", ErrorType.ALREADY_EXISTS_ERROR.getErrorDescription()));
		}
		
		try {
			return this.couponsDao.save(coupon).getId();
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.CREATE_ERROR, ErrorType.CREATE_ERROR.getErrorDescription());
		}
	}
	
	public CouponEntity getCouponById(long id) throws ApplicationException {
		if (id < 1) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, String.format("%s, id cannot be less than 1", 
					ErrorType.INVALID_FORMAT_ERROR.getErrorDescription()));
		}
		
		Optional<CouponEntity> coupon;
		try {
			coupon = this.couponsDao.findById(id);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
		
		if (!coupon.isPresent()) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
					String.format("coupon id %s %s", id, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		return coupon.get();
	}

	public List<CouponEntity> getAllCoupons() throws ApplicationException {
		try {
			return (List<CouponEntity>) this.couponsDao.findAll();
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}

	public long getCouponsCount() throws ApplicationException {
		try {
			return this.couponsDao.count();
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}
	
	public List<CouponEntity> getCouponsByCompanyId(long companyId) throws ApplicationException {
		if (!this.companiesController.isCompanyExistsById(companyId)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR,
										String.format("company id %s %s", companyId, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		try {
			return this.couponsDao.findAllByCompanyId(companyId);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}

	public List<CouponEntity> getCouponsByCategory(String categoryStr) throws ApplicationException {
		if (categoryStr == null) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s coupon category", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		if (!ValidationsUtils.isCategoryExists(categoryStr)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
					String.format("coupon category %s %s", categoryStr, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		try {
			CouponCategory category = CouponCategory.valueOf(categoryStr);
			
			return this.couponsDao.findAllByCategory(category);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}

	public List<CouponEntity> getCouponsByPriceOrLower(float price) throws ApplicationException {
		if (price <= 0) {
			throw new ApplicationException(ErrorType.INVALID_PRICE_ERROR, ErrorType.INVALID_PRICE_ERROR.getErrorDescription());
		}
		
		try {
			return this.couponsDao.findAllByPrice(price);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}
	
	public int getCouponQuantityById(long id) throws ApplicationException {
		CouponEntity coupon = this.getCouponById(id);
		return coupon.getQuantity();
	}

	public void updateCoupon(CouponEntity coupon) throws ApplicationException {
		if (!isCouponAttributesValid(coupon)) {
			throw new ApplicationException();
		}
		if (!isCouponExistsById(coupon.getId())) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, String.format("coupon id %s %s" ,coupon.getId(), 
					ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		try {
			this.couponsDao.save(coupon);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.UPDATE_ERROR, ErrorType.UPDATE_ERROR.getErrorDescription());
		}
	}
	
	@Transactional
	public void updateCouponQuantityById(long id, int quantity, boolean cancel) throws ApplicationException {
		if (id < 1) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, String.format("%s, id cannot be less than 1", 
					ErrorType.INVALID_FORMAT_ERROR.getErrorDescription()));
		}
		if (!isCouponExistsById(id)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, String.format("coupon id %s %s" ,id, 
					ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		if (quantity <= 0) {
			throw new ApplicationException(ErrorType.INVALID_QUANTITY_ERROR, 
										String.format("%s, cannot be 0 or less", ErrorType.INVALID_QUANTITY_ERROR.getErrorDescription()));
		}
		
		if (cancel) {
			try {
				this.couponsDao.updateCouponQuantityById(id, quantity);
			} catch (Exception e) {
				throw new ApplicationException(e, ErrorType.UPDATE_ERROR, ErrorType.UPDATE_ERROR.getErrorDescription());
			}
		}
		else {
			int quantityInStock = (int) this.getCouponById(id).getQuantity();
			if (quantity > quantityInStock) {
				throw new ApplicationException(ErrorType.EXCEEDED_ERROR, "quantity requested exceeded available coupons");
			}

			try {
				this.couponsDao.updateCouponQuantityById(id, -quantity);
			} catch (Exception e) {
				throw new ApplicationException(e, ErrorType.UPDATE_ERROR, ErrorType.UPDATE_ERROR.getErrorDescription());
			}
			
		}
	}

	public void deleteCouponById(long id) throws ApplicationException {
		if (!isCouponExistsById(id)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, String.format("coupon id %s %s" ,id, 
					ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		try {
			this.couponsDao.deleteById(id);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.DELETE_ERROR, ErrorType.DELETE_ERROR.getErrorDescription());
		}
	}

	public void deleteExpiredCoupons() throws ApplicationException {
		try {
			this.couponsDao.deleteExpiredCoupons(DateUtils.getSqlDateTime(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorType.DELETE_ERROR, ErrorType.DELETE_ERROR.getErrorDescription());
		}
	}
	
	public boolean isCouponExistsById(long id) throws ApplicationException {
		if (id < 1) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, String.format("%s, id cannot be less than 1", 
					ErrorType.INVALID_FORMAT_ERROR.getErrorDescription()));
		}
		
		try {
			return this.couponsDao.existsById(id);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}
	
	private boolean isCouponExistsByTitle(String title) throws ApplicationException {
		try {
			return this.couponsDao.existsByTitle(title);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}
	
	private boolean isCouponExistsByCompanyId(long companyId) throws ApplicationException {
		try {
			return this.couponsDao.existsById(companyId);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
		
	}
	
	private boolean isCouponAttributesValid(CouponEntity coupon) throws ApplicationException {
		if (coupon == null) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s coupon", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		if (!ValidationsUtils.isValidLength(coupon.getTitle(), 10, 50)) {
			throw new ApplicationException(ErrorType.INVALID_LENGTH_ERROR,  
										String.format("%s, title must be between 10 - 50 characters", 
												ErrorType.INVALID_LENGTH_ERROR.getErrorDescription()));
		}
		if (!ValidationsUtils.isValidLength(coupon.getDescription(), 10, 255)) {
			throw new ApplicationException(ErrorType.INVALID_LENGTH_ERROR,  
										String.format("%s, description must be between 10 - 255 characters", 
												ErrorType.INVALID_LENGTH_ERROR.getErrorDescription()));
		}
		if (!ValidationsUtils.isValidDescription(coupon.getTitle(), "Title")) {
			throw new ApplicationException(ErrorType.INVALID_CHARS_ERROR, 
										String.format("%s in title", ErrorType.INVALID_CHARS_ERROR.getErrorDescription()));
		}
		if (!ValidationsUtils.isValidDescription(coupon.getDescription(), "Description")) {
			throw new ApplicationException(ErrorType.INVALID_CHARS_ERROR, 
										String.format("%s in description", ErrorType.INVALID_CHARS_ERROR.getErrorDescription()));
		}
		if (!this.companiesController.isCompanyExistsById(coupon.getCompany())) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
										String.format("company id %s %s ", 
												coupon.getCompany().getId(), ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		if (coupon.getPrice() <= 0) {
			throw new ApplicationException(ErrorType.INVALID_PRICE_ERROR, ErrorType.INVALID_PRICE_ERROR.getErrorDescription());
		}
		if (!ValidationsUtils.isValidUrl(coupon.getImage())) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, 
										String.format("%s, image must use URL format", ErrorType.INVALID_FORMAT_ERROR.getErrorDescription()));
		}
		if (coupon.getQuantity() <= 0) {
			throw new ApplicationException(ErrorType.INVALID_QUANTITY_ERROR, 
										String.format("%s, cannot be 0 or less", ErrorType.INVALID_QUANTITY_ERROR.getErrorDescription()));
		}
		if (coupon.getStartDate().before(new Date())) {
			throw new ApplicationException(ErrorType.INVALID_DATE_ERROR,  
										String.format("%s, start date cannot be set before current time", 
												ErrorType.INVALID_DATE_ERROR.getErrorDescription()));
		}
		if (coupon.getExpirationDate().before(new Date())) {
			throw new ApplicationException(ErrorType.INVALID_DATE_ERROR, ErrorType.INVALID_DATE_ERROR.getErrorDescription() 
										+ ", expiration date cannot be set before current time");
		}
		if (coupon.getStartDate().after(coupon.getExpirationDate())) {
			throw new ApplicationException(ErrorType.INVALID_DATE_ERROR,  
										String.format("%s, start date cannot be after expiration date", 
												ErrorType.INVALID_DATE_ERROR.getErrorDescription()));
		}
		if (!ValidationsUtils.isValidCouponCategory(coupon.getCategory())) {
			throw new ApplicationException();
		}
		
		return true;
	}

}

