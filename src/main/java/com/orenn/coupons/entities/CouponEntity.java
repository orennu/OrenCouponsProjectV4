package com.orenn.coupons.entities;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import javax.annotation.PostConstruct;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.orenn.coupons.enums.CouponCategory;
import com.orenn.coupons.utils.DailyTask;

@Entity
@Table(name = "coupons")
public class CouponEntity {
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;
	
	@Column(name = "title", nullable = false)
	private String title;
	
	@Column(name = "description", nullable = false)
	private String description;
	
	@Column(name = "price", nullable = false)
	private float price;
	
	@Column(name = "category", nullable = false)
	@Enumerated(EnumType.STRING)
	private CouponCategory category;
	
	@Column(name = "image", nullable = false)
	private String image;
	
	@Column(name = "quantity", nullable = false)
	private int quantity;
	
	@Column(name = "start_date", nullable = false)
	private Date startDate;
	
	@Column(name = "expiration_date", nullable = false)
	private Date expirationDate;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id", nullable = true, unique = false)
	private CompanyEntity company;
	
	public CouponEntity() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public CouponCategory getCategory() {
		return category;
	}

	public void setCategory(CouponCategory category) {
		this.category = category;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	public CompanyEntity getCompany() {
		return company;
	}
	
	public void setCompany(CompanyEntity company) {
		this.company = company;
	}
	
	@Override
	public String toString() {
		return "CouponEntity [id=" + id + ", title=" + title + ", description=" + description + ", price=" + price
				+ ", category=" + category + ", image=" + image + ", quantity=" + quantity + ", startDate=" + startDate
				+ ", expirationDate=" + expirationDate + ", company=" + company + "]";
	}

	@PostConstruct
	private void cleanupExpiredCoupons() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 1);
		calendar.set(Calendar.MINUTE, 30);
		calendar.set(Calendar.SECOND, 0);
		
		DailyTask dailyTask = new DailyTask();
		Timer timer = new Timer();
		timer.schedule(dailyTask, calendar.getTime());
	}

}
