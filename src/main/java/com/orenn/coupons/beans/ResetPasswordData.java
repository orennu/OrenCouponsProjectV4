package com.orenn.coupons.beans;

public class ResetPasswordData {
	
	private String code;
	private String email;
	private String password;

	public ResetPasswordData(String code, String email, String password) {
		this.code = code;
		this.email = email;
		this.password = password;
	}
	
	public ResetPasswordData(String code, String email) {
		this.code = code;
		this.email = email;
	}
	
	public ResetPasswordData() {
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
