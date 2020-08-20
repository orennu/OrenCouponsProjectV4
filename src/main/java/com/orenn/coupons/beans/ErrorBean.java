package com.orenn.coupons.beans;

public class ErrorBean {
	
	private int errorCode;
	private String errorName;
	private String errorDescription;
	
	public ErrorBean(int errorCode, String errorName, String errorDescription) {
		this.errorCode = errorCode;
		this.errorName = errorName;
		this.errorDescription = errorDescription;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorName() {
		return errorName;
	}

	public void setErrorName(String errorName) {
		this.errorName = errorName;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	
}
