package com.orenn.coupons.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.orenn.coupons.enums.CouponCategory;
import com.orenn.coupons.enums.ErrorType;
import com.orenn.coupons.enums.IndustryType;
import com.orenn.coupons.enums.UserType;
import com.orenn.coupons.exceptions.ApplicationException;

public class ValidationsUtils {
	
	public static boolean isValidPhoneNumber(String phoneNumber) throws ApplicationException {
		if (isNull(phoneNumber)) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s phone number", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		
		String phoneRegex = "^\\+?(?:[0-9] ?){6,14}[0-9]$";
		Pattern phonePattern = Pattern.compile(phoneRegex);
		Matcher matcher = phonePattern.matcher(phoneNumber);
		
		return matcher.matches();
	}
	
	public static boolean isValidEmail(String email) throws ApplicationException {
		if (isNull(email)) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s email", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		
		String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
		Pattern emailPattern = Pattern.compile(emailRegex);
		Matcher matcher = emailPattern.matcher(email);
		
		return matcher.matches();
	}
	
	public static boolean isValidUrl(String url) throws ApplicationException {
		if (isNull(url)) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s url", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		
		String urlRegex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		Pattern urlPattern = Pattern.compile(urlRegex);
		Matcher matcher = urlPattern.matcher(url);
		
		return matcher.matches();
	}
	
	public static boolean isValidUuid(String uuid) throws ApplicationException {
		if (isNull(uuid)) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s uuid", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		
		String uuidRegex = "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}";
		Pattern uuidPattern = Pattern.compile(uuidRegex);
		Matcher matcher = uuidPattern.matcher(uuid);
		
		return matcher.matches();
	}
	
	public static boolean isValidAddress(String address) throws ApplicationException {
		if (address.isEmpty()) {
			return true;
		}
		if (isNull(address)) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s address", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		if (!isValidLength(address, 2, 255)) {
			throw new ApplicationException(ErrorType.INVALID_LENGTH_ERROR, 
					String.format("%s must be between 2 - 255", ErrorType.INVALID_LENGTH_ERROR.getErrorDescription()));
		}
		if (address.trim().length() < address.length()) {
			throw new ApplicationException(ErrorType.INVALID_WHITESPACE_ERROR, 
					String.format("address %s", ErrorType.INVALID_WHITESPACE_ERROR.getErrorDescription()));
		}
		
		String addressRegex = "[a-zA-Z0-9 .,]+";
		Pattern addressPattern = Pattern.compile(addressRegex);
		Matcher matcher = addressPattern.matcher(address);
		
		return matcher.matches();
	}
	
	public static boolean isValidLength(String str, int min, int max) {
		if (str.length() >= min && str.length() <= max) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isValidUserName(String userName) throws ApplicationException {
		if (isNull(userName)) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s username", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		
		String userNameRegex = "^[a-z][a-z0-9_]{2,20}$";
		Pattern userNamePattern = Pattern.compile(userNameRegex);
		Matcher matcher = userNamePattern.matcher(userName);
		
		return matcher.matches();
	}
	
	public static boolean isValidPassword(String password) throws ApplicationException {
		if (isNull(password)) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s password", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		
		String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{10,16}$";
		Pattern passwordPattern = Pattern.compile(passwordRegex);
		Matcher matcher = passwordPattern.matcher(password);
		
		return matcher.matches();
	}
	
	public static boolean isValidUserType(UserType userType) throws ApplicationException {
		if (isNull(userType)) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s user type", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		if (!isUserTypeExists(userType.name())) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
					String.format("user type %s %s", userType.name(), ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		return true;
	}
	
	public static boolean isUserTypeExists(String userTypeStr) {
		for (UserType userType : UserType.values()) {
			if (userType.name().equals(userTypeStr)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isValidName(String name) throws ApplicationException {
		String nameRegex = "^[\\p{L}.'-]+$";
		if (!isMatchingPattern(nameRegex, name)) {
			throw new ApplicationException(ErrorType.INVALID_CHARS_ERROR, 
					String.format("%s, name must contain only valid name characters", ErrorType.INVALID_CHARS_ERROR.getErrorDescription()));
		}
		if (name.trim().length() < name.length()) {
			throw new ApplicationException(ErrorType.INVALID_WHITESPACE_ERROR, 
					String.format("invalid name, %s", ErrorType.INVALID_WHITESPACE_ERROR.getErrorDescription()));
		}
		
		return true;
	}
	
	public static boolean isCompanyNameValid(String companyName) throws ApplicationException {
		if (companyName == null) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s company name", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		if (!isValidLength(companyName, 2, 20)) {
			throw new ApplicationException(ErrorType.INVALID_LENGTH_ERROR, 
					String.format("%s, company name must be 2 - 20 characters", ErrorType.INVALID_LENGTH_ERROR.getErrorDescription()));
		}
		if (companyName.trim().length() < companyName.length()) {
			throw new ApplicationException(ErrorType.INVALID_WHITESPACE_ERROR, 
					String.format("company name %s", ErrorType.INVALID_WHITESPACE_ERROR.getErrorDescription()));
		}
		String nameRegex = "[a-zA-Z0-9 ]+";
		
		if (!isMatchingPattern(nameRegex, companyName)) {
			throw new ApplicationException(ErrorType.INVALID_CHARS_ERROR, 
					String.format("%s, company name must consist of letters and digits", ErrorType.INVALID_CHARS_ERROR.getErrorDescription()));
		}
		
		return true;
	}
	
	public static boolean isValidCompanyType(IndustryType industry) throws ApplicationException {
		if (isNull(industry)) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s industry type", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		if (!isIndustryTypeExists(industry.name())) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
					String.format("industry type %s %s", industry.name(), ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		return true;
	}
	
	public static boolean isIndustryTypeExists(String industryTypeStr) {
		for (IndustryType industryType : IndustryType.values()) {
			if (industryType.name().equals(industryTypeStr)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isValidCouponCategory(CouponCategory category) throws ApplicationException {
		if (isNull(category)) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s coupon category", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		if (!isIndustryTypeExists(category.name())) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
					String.format("coupon category %s %s", category.name(), ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		return true;
	}

	public static boolean isCategoryExists(String categoryStr) {
		for (CouponCategory category : CouponCategory.values()) {
			if (category.name().equals(categoryStr)) {
				return true;
			}
		}
		
		return false;
	}

	public static boolean isValidDescription(String description, String item) throws ApplicationException {
		if (description.trim().length() < description.length()) {
			throw new ApplicationException(ErrorType.INVALID_WHITESPACE_ERROR, 
					String.format("%s %s" , item, ErrorType.INVALID_WHITESPACE_ERROR.getErrorDescription()));
		}
		String regex = "[a-zA-Z0-9 .,/+]+";
		if (!isMatchingPattern(regex, description)) {
			return false;
		}
		
		return true;
		
	}
	
	public static boolean isMatchingPattern(String regex, String str) {
		Pattern namePattern = Pattern.compile(regex);
		Matcher matcher = namePattern.matcher(str);
		
		if (matcher.matches()) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isNull(Object object) {
		if (object == null) {
			return true;
		}
		
		return false;
	}

}
