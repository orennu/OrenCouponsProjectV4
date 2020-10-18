package com.orenn.coupons.beans;

import com.orenn.coupons.enums.UserType;

public class SuccessfulLoginData {
	
	private long id;
	private String token;
	private UserType type;
	
	public SuccessfulLoginData(long id, String token, UserType type) {
		this.id = id;
		this.token = token;
		this.type = type;
	}

	public long getId() {
		return id;
	}
	
	public String getToken() {
		return token;
	}

	public UserType getType() {
		return type;
	}
	
}
