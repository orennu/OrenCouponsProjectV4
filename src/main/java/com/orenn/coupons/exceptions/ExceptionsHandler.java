package com.orenn.coupons.exceptions;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.orenn.coupons.beans.ErrorBean;
import com.orenn.coupons.enums.ErrorType;

@RestControllerAdvice
public class ExceptionsHandler {
	
	@ExceptionHandler
	@ResponseBody
	public ErrorBean setResponse(Throwable throwable, HttpServletResponse httpResponse) {
		
		if (throwable instanceof ApplicationException) {
			ApplicationException applicationException = (ApplicationException) throwable;
			
			ErrorType errorType = applicationException.getErrorType();
			int errorCode = errorType.geterrorCode();
			String errorName = errorType.getErrorName();
			String errorDescription = applicationException.getMessage();
			
			if (errorType.isPrintStackTrace()) {
				applicationException.printStackTrace();
			}
			
			httpResponse.setStatus(errorCode);
			ErrorBean errorBean = new ErrorBean(errorCode, errorName, errorDescription);
			
			return errorBean;
		}
		
		httpResponse.setStatus(500);
		String errorDescription = throwable.getMessage();
		ErrorBean errorBean = new ErrorBean(500, "Internal server error", errorDescription);
		throwable.printStackTrace();
		
		return errorBean;
	}
}
