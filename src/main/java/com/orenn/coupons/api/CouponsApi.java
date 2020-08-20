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

import com.orenn.coupons.entities.CouponEntity;
import com.orenn.coupons.exceptions.ApplicationException;
import com.orenn.coupons.logic.CouponsController;

@RestController
@RequestMapping("/coupons")
public class CouponsApi {
	
	@Autowired
	private CouponsController couponsController;
	
	@PostMapping
	public long addCoupon(@RequestBody CouponEntity coupon) throws ApplicationException {
		return this.couponsController.addCoupon(coupon);
	}
	
	@GetMapping("/{id}")
	public CouponEntity getCouponById(@PathVariable("id") long id) throws ApplicationException {
		return this.couponsController.getCouponById(id);
	}
	
	@GetMapping
	public List<CouponEntity> getAllCoupons() throws ApplicationException {
		return this.couponsController.getAllCoupons();
	}
	
	@GetMapping("/count")
	public long getCouponsCount() throws ApplicationException {
		return this.couponsController.getCouponsCount();
	}
	
	@GetMapping(value = "/search", params = "companyId")
	public List<CouponEntity> getCouponsByCompany(@RequestParam("companyId") long companyId) throws ApplicationException {
		return this.couponsController.getCouponsByCompanyId(companyId);
	}
	
	@GetMapping(value = "/search", params = "category")
	public List<CouponEntity> getCouponsByCategory(@RequestParam("category") String category) throws ApplicationException {
		return this.couponsController.getCouponsByCategory(category);
	}
	
	@GetMapping(value = "/search", params = "price")
	public List<CouponEntity> getCouponsByPriceOrLower(@RequestParam("price") float price) throws ApplicationException {
		return this.couponsController.getCouponsByPriceOrLower(price);
	}
	
	@PutMapping
	public void updateCoupon(@RequestBody CouponEntity coupon) throws ApplicationException {
		this.couponsController.updateCoupon(coupon);
	}
	
	//for testing only
	@PutMapping(value = "{id}/purchase", params = {"quantity", "cancel"})
	public void updateCouponQuantityById(@PathVariable("id") long id, @RequestParam("quantity") int quantity, @RequestParam("cancel") boolean cancel) throws ApplicationException {
		this.couponsController.updateCouponQuantityById(id, quantity, cancel);
	}
	
	@DeleteMapping("/{id}")
	public void deleteCouponById(@PathVariable("id") long id) throws ApplicationException {
		this.couponsController.deleteCouponById(id);
	}
	
	//for testing only
	@DeleteMapping("/expired")
	public void deleteExpiredCoupons() throws ApplicationException {
		this.couponsController.deleteExpiredCoupons();
	}
}
