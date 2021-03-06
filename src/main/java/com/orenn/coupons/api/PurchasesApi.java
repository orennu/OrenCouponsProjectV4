package com.orenn.coupons.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orenn.coupons.beans.PurchaseData;
import com.orenn.coupons.entities.PurchaseEntity;
import com.orenn.coupons.exceptions.ApplicationException;
import com.orenn.coupons.logic.PurchasesController;

@RestController
@RequestMapping("/purchases")
public class PurchasesApi {
	
	@Autowired
	private PurchasesController purchasesController;
	
	@PostMapping
	public void addPurchase(@RequestBody PurchaseEntity[] purchaseArray) throws ApplicationException {
		this.purchasesController.addPurchase(purchaseArray);
	}
	
	@GetMapping("/{id}")
	public PurchaseEntity getPurchaseById(@PathVariable("id") long id) throws ApplicationException {
		return this.purchasesController.getPurchaseById(id);
	}
	
	@GetMapping
	public List<PurchaseEntity> getAllPurchases() throws ApplicationException {
		return this.purchasesController.getAllPurchases();
	}
	
	@GetMapping("/count")
	public long getPurchasesCount() throws ApplicationException {
		return this.purchasesController.getPurchasesCount();
	}
	
	@GetMapping(value = "/search", params = "purchaseDate")
	public List<PurchaseEntity> getPurchasesByDate(@RequestParam("purchaseDate") String purchaseDate) throws ApplicationException {
		return this.purchasesController.getPurchasesByDate(purchaseDate);
	}
	
	@GetMapping(value = "/count/search", params = "purchaseDate")
	public long getPurchasesCountByDate(@RequestParam("purchaseDate") String purchaseDate) throws ApplicationException {
		return this.purchasesController.getPurchasesCountByDate(purchaseDate);
	}
	
	@GetMapping(value = "/search", params = "customerId")
	public List<PurchaseData> getPurchasesByCustomerId(@RequestParam("customerId") long customerId) throws ApplicationException {
		return this.purchasesController.getPurchasesByCustomerId(customerId);
	}
	
	@GetMapping(value = "/count/search", params = "customerId")
	public long getPurchasesCountByCustomerId(@RequestParam("customerId") long customerId) throws ApplicationException {
		return this.purchasesController.getPurchasesCountByCustomerId(customerId);
	}
	
	@GetMapping(value = "/search", params = "couponId")
	public List<PurchaseEntity> getPurchasesByCouponId(@RequestParam("couponId") long couponId) throws ApplicationException {
		return this.purchasesController.getPurchasesByCouponId(couponId);
	}
	
	@GetMapping(value = "/count/search", params = "couponId")
	public long getPurchasesCountByCouponId(@RequestParam("couponId") long couponId) throws ApplicationException {
		return this.purchasesController.getPurchasesCountByCouponId(couponId);
	}
	
	@GetMapping(value = "/search", params = "companyId")
	public List<PurchaseData> getPurchasesByCompanyId(@RequestParam("companyId") long companyId) throws ApplicationException {
		return this.purchasesController.getPurchasesByCompanyId(companyId);
	}
	
	@DeleteMapping("/{id}")
	public void deletePurchaseById(@PathVariable("id") long id) throws ApplicationException {
		this.purchasesController.deletePurchaseById(id);
	}

}
