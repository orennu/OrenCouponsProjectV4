package com.orenn.coupons.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orenn.coupons.entities.CompanyEntity;
import com.orenn.coupons.exceptions.ApplicationException;
import com.orenn.coupons.logic.CompaniesController;

@RestController
@RequestMapping("/companies")
public class CompaniesApi {
	
	@Autowired
	private CompaniesController companiesController;
	
	@PostMapping
	public long addCompany(@RequestBody CompanyEntity company) throws ApplicationException {
		return this.companiesController.addCompany(company);
	}
	
	@GetMapping("/{id}")
	public CompanyEntity getCompanyById(@PathVariable("id") long id) throws ApplicationException {
		return this.companiesController.getCompanyById(id);
	}
	
	@GetMapping(value = "/search", params = "email")
	public CompanyEntity getCompanyByEmail(@RequestParam("email") String email) throws ApplicationException {
		return this.companiesController.getCompanyByEmail(email);
	}
	
	@GetMapping
	public List<CompanyEntity> getAllCompanies() throws ApplicationException {
		return this.companiesController.getAllCompanies();
	}
	
	@GetMapping("/count")
	public long getCompaniesCount() throws ApplicationException {
		return this.companiesController.getCompaniesCount();
	}
	
	@GetMapping(value = "/search", params = "type")
	public List<CompanyEntity> getCompaniesByIndustryType(@RequestParam("type") String industryTypeStr) throws ApplicationException {
		return this.companiesController.getCompaniesByIndustry(industryTypeStr);
	}
	
	@PutMapping
	public void updateCompany(@RequestBody CompanyEntity company) throws ApplicationException {
		this.companiesController.updateCompany(company);
	}
	
	@DeleteMapping("/{id}")
	public void deleteCompanyById(@PathVariable("id") long id) throws ApplicationException {
		this.companiesController.deleteCompanyById(id);
	}
}
