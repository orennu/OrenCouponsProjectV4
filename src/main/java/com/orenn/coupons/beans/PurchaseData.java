package com.orenn.coupons.beans;

import java.util.Date;

import com.orenn.coupons.enums.CouponCategory;

public class PurchaseData {
	
	private long purchaseId;
	private Date purchaseDate;
	private int purchaseQuantity;
	private String couponTitle;
	private float couponPrice;
	private CouponCategory couponCategory;
	private String customerFirstName;
	private String customerLastName;
	private String couponImageUuid;
	
	public PurchaseData(long purchaseId, Date purchaseDate, int purchaseQuantity, String couponTitle, float couponPrice,
			CouponCategory couponCategory, String customerFirstName, String customerLastName, String couponImageUuid) {
		this.purchaseId = purchaseId;
		this.purchaseDate = purchaseDate;
		this.purchaseQuantity = purchaseQuantity;
		this.couponTitle = couponTitle;
		this.couponPrice = couponPrice;
		this.couponCategory = couponCategory;
		this.customerFirstName = customerFirstName;
		this.customerLastName = customerLastName;
		this.couponImageUuid = couponImageUuid;
	}
	
	public PurchaseData(Date purchaseDate, int purchaseQuantity, String couponTitle, float couponPrice,
			CouponCategory couponCategory, String couponImageUuid) {
		this.purchaseDate = purchaseDate;
		this.purchaseQuantity = purchaseQuantity;
		this.couponTitle = couponTitle;
		this.couponPrice = couponPrice;
		this.couponCategory = couponCategory;
		this.couponImageUuid = couponImageUuid;
	}



	public PurchaseData() {
	}

	public long getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(long purchaseId) {
		this.purchaseId = purchaseId;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public int getPurchaseQuantity() {
		return purchaseQuantity;
	}

	public void setPurchaseQuantity(int purchaseQuantity) {
		this.purchaseQuantity = purchaseQuantity;
	}

	public String getCouponTitle() {
		return couponTitle;
	}

	public void setCouponTitle(String couponTitle) {
		this.couponTitle = couponTitle;
	}

	public float getCouponPrice() {
		return couponPrice;
	}

	public void setCouponPrice(float couponPrice) {
		this.couponPrice = couponPrice;
	}

	public CouponCategory getCouponCategory() {
		return couponCategory;
	}

	public void setCouponCategory(CouponCategory couponCategory) {
		this.couponCategory = couponCategory;
	}

	public String getCustomerFirstName() {
		return customerFirstName;
	}

	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}

	public String getCustomerLastName() {
		return customerLastName;
	}

	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}
	
	public String getCouponImageUuid() {
		return couponImageUuid;
	}

	public void setCouponImageUuid(String couponImageUuid) {
		this.couponImageUuid = couponImageUuid;
	}

	@Override
	public String toString() {
		return "PurchaseData [purchaseId=" + purchaseId + ", purchaseDate=" + purchaseDate + ", purchaseQuantity="
				+ purchaseQuantity + ", couponTitle=" + couponTitle + ", couponPrice=" + couponPrice
				+ ", couponCategory=" + couponCategory + ", customerFirstName=" + customerFirstName
				+ ", customerLastName=" + customerLastName + ", couponImageUuid=" + couponImageUuid + "]";
	}

}
