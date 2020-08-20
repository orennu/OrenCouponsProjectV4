package com.orenn.coupons.logic;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.orenn.coupons.dao.ICompaniesDao;
import com.orenn.coupons.entities.CompanyEntity;
import com.orenn.coupons.enums.ErrorType;
import com.orenn.coupons.enums.IndustryType;
import com.orenn.coupons.exceptions.ApplicationException;
import com.orenn.coupons.utils.ValidationsUtils;

@Controller
public class CompaniesController {
	
	@Autowired
	private ICompaniesDao companiesDao;
	
	public long addCompany(CompanyEntity company) throws ApplicationException {
		if (!isCompanyAttributesValid(company)) {
			throw new ApplicationException();
		}
		if (isCompanyExistsByName(company.getName())) {
			throw new ApplicationException(ErrorType.ALREADY_EXISTS_ERROR, String.format("company %s, %s", company.getName(), 
					ErrorType.ALREADY_EXISTS_ERROR.getErrorDescription()));
		}
		if (isCompanyExistsByEmail(company.getEmail())) {
			throw new ApplicationException(ErrorType.ALREADY_EXISTS_ERROR, String.format("company email %s, %s", company.getEmail(), 
					ErrorType.ALREADY_EXISTS_ERROR.getErrorDescription()));
		}
		if (isCompanyExistsByPhoneNumber(company.getPhoneNumber())) {
			throw new ApplicationException(ErrorType.ALREADY_EXISTS_ERROR, String.format("company phone number: %s, %s", company.getPhoneNumber(), 
					ErrorType.ALREADY_EXISTS_ERROR.getErrorDescription()));
		}
		
		try {
			return this.companiesDao.save(company).getId();
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.CREATE_ERROR, ErrorType.CREATE_ERROR.getErrorDescription());
		}
	}

	public CompanyEntity getCompanyById(long id) throws ApplicationException {
		if (id < 1) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, String.format("%s, id cannot be less than 1", 
					ErrorType.INVALID_FORMAT_ERROR.getErrorDescription()));
		}

		Optional<CompanyEntity> company;
		try {
			company = this.companiesDao.findById(id);

		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}

		if (!company.isPresent()) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
					String.format("company id %s %s", id, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}

		return company.get();
	}

	public CompanyEntity getCompanyByEmail(String email) throws ApplicationException {
		if (!ValidationsUtils.isValidEmail(email)) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, 
					String.format("%s in email address", ErrorType.INVALID_FORMAT_ERROR.getErrorDescription()));
		}
		
		CompanyEntity company;
		try {
			company = this.companiesDao.getCompanyByEmail(email);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
		
		if (company == null) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
					String.format("company email %s %s", email, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		return company;
	}

	public List<CompanyEntity> getAllCompanies() throws ApplicationException {
		try {
			return (List<CompanyEntity>) this.companiesDao.findAll();
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}

	public long getCompaniesCount() throws ApplicationException {
		try {
			return this.companiesDao.count();
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}

	public List<CompanyEntity> getCompaniesByIndustry(String industryTypeStr) throws ApplicationException {
		if (industryTypeStr == null) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s industry type", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		if (!ValidationsUtils.isIndustryTypeExists(industryTypeStr)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
					String.format("industry type %s %s", industryTypeStr, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		try {
			IndustryType industry = IndustryType.valueOf(industryTypeStr);
			
			return this.companiesDao.findAllByIndustry(industry);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}

	public void updateCompany(CompanyEntity company) throws ApplicationException {
		if (!isCompanyAttributesValid(company)) {
			throw new ApplicationException();
		}
		if (!isCompanyExistsById(company.getId())) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, String.format("company id %s %s" ,company.getId(), 
					ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		try {
			this.companiesDao.save(company);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.UPDATE_ERROR, ErrorType.UPDATE_ERROR.getErrorDescription());
		}
	}

	public void deleteCompanyById(long id) throws ApplicationException {
		if (!isCompanyExistsById(id)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, String.format("company id %s %s" ,id, 
					ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		try {
			this.companiesDao.deleteById(id);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.DELETE_ERROR, ErrorType.DELETE_ERROR.getErrorDescription());
		}
	}
	
	public boolean isCompanyExistsById(CompanyEntity companyEntity) throws ApplicationException {
		if (companyEntity == null) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s company", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		if (companyEntity.getId() < 1) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, String.format("%s, id cannot be less than 1", 
					ErrorType.INVALID_FORMAT_ERROR.getErrorDescription()));
		}
		
		try {
			return this.companiesDao.existsById(companyEntity.getId());
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}

	public boolean isCompanyExistsById(long id) throws ApplicationException {
		if (id < 1) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, String.format("%s, id cannot be less than 1", 
					ErrorType.INVALID_FORMAT_ERROR.getErrorDescription()));
		}
		
		try {
			return this.companiesDao.existsById(id);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}
	
	public boolean isCompanyExistsByPhoneNumber(String phoneNumber) throws ApplicationException {
		if (!ValidationsUtils.isValidPhoneNumber(phoneNumber)) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, 
					String.format("%s, phone number must be 6 - 14 digits", ErrorType.INVALID_FORMAT_ERROR.getErrorDescription()));
		}
		
		try {
			return this.companiesDao.existsByPhoneNumber(phoneNumber);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}

	public boolean isCompanyExistsByEmail(String email) throws ApplicationException {
		if (!ValidationsUtils.isValidEmail(email)) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, 
					String.format("%s in email address", ErrorType.INVALID_FORMAT_ERROR.getErrorDescription()));
		}
		
		try {
			return this.companiesDao.existsByEmail(email);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}

	public boolean isCompanyExistsByName(String name) throws ApplicationException {
		if (!ValidationsUtils.isCompanyNameValid(name)) {
			throw new ApplicationException();
		}
		
		try {
			return this.companiesDao.existsByName(name);
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
	}

	private boolean isCompanyAttributesValid(CompanyEntity company) throws ApplicationException {
		if (company == null) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s company", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		if (!ValidationsUtils.isValidEmail(company.getEmail())) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, 
					String.format("%s in email address", ErrorType.INVALID_FORMAT_ERROR.getErrorDescription()));
		}
		if (!ValidationsUtils.isCompanyNameValid(company.getName())) {
			throw new ApplicationException();
		}
		if (!ValidationsUtils.isValidPhoneNumber(company.getPhoneNumber())) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, 
					String.format("%s, phone number must be 6 - 14 digits", ErrorType.INVALID_FORMAT_ERROR.getErrorDescription()));
		}
		if (!ValidationsUtils.isValidAddress(company.getAddress())) {
			throw new ApplicationException(ErrorType.INVALID_CHARS_ERROR, String.format("%s in address", ErrorType.INVALID_CHARS_ERROR.getErrorDescription()));
		}
		if (!ValidationsUtils.isValidCompanyType(company.getIndustry())) {
			throw new ApplicationException();
		}
		
		return true;
	}

}
