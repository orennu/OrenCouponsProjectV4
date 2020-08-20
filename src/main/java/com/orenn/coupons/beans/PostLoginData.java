package com.orenn.coupons.beans;

import com.orenn.coupons.enums.UserType;

public class PostLoginData {
	
	private long id;
	private Long companyId;
	private UserType type;
	
	public PostLoginData(long id, Long companyId, UserType type) {
		this(id, type);
		this.companyId = companyId;
	}
	
	public PostLoginData(long id, UserType type) {
		this.id = id;
		this.companyId = null;
		this.type = type;
	}
	
	public PostLoginData() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

}
