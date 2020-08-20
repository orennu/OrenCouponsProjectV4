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

import com.orenn.coupons.entities.CustomerEntity;
import com.orenn.coupons.exceptions.ApplicationException;
import com.orenn.coupons.logic.CustomersController;

@RestController
@RequestMapping("/customers")
public class CustomersApi {
	
	@Autowired
	private CustomersController customersController;
	
	@PostMapping("/register")
	public long addCustomer(@RequestBody CustomerEntity customer) throws ApplicationException {
		return this.customersController.addCustomer(customer);
	}
	
	@GetMapping("/{id}")
	public CustomerEntity getCustomerById(@PathVariable("id") long id) throws ApplicationException {
		return this.customersController.getCustomerById(id);
	}
	
	@GetMapping(value = "/search", params = "phoneNumber")
	public CustomerEntity getCustomerByPhoneNumber(@RequestParam String phoneNumber) throws ApplicationException {
		return this.customersController.getCustomerByPhoneNumber(phoneNumber);
	}
	
	@GetMapping
	public List<CustomerEntity> getAllCustomers() throws ApplicationException {
		return this.customersController.getAllCustomers();
	}
	
	@GetMapping("/count")
	public long getCustomersCount() throws ApplicationException {
		return this.customersController.getCustomersCount();
	}
	
	@GetMapping(value = "/search", params = "dateOfBirth")
	public List<CustomerEntity> getCustomersByDateOfBirth(@RequestParam String dateOfBirth) throws ApplicationException {
		return this.customersController.getCustomersByDateOfBirth(dateOfBirth);
	}
	
	@PutMapping
	public void updateCustomer(@RequestBody CustomerEntity customer) throws ApplicationException {
		this.customersController.updateCustomer(customer);
	}
	
	@DeleteMapping("/{id}")
	public void deleteCustomerById(@PathVariable("id") long id) throws ApplicationException {
		this.customersController.deleteCustomerById(id);
	}
}
