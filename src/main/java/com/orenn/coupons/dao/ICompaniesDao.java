package com.orenn.coupons.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.orenn.coupons.entities.CompanyEntity;
import com.orenn.coupons.enums.IndustryType;

public interface ICompaniesDao extends CrudRepository<CompanyEntity, Long> {

	public CompanyEntity getCompanyByEmail(String email);

	public List<CompanyEntity> findAllByIndustry(IndustryType industry);

	public boolean existsByPhoneNumber(String phoneNumber);

	public boolean existsByEmail(String email);

	public boolean existsByName(String name);

}
