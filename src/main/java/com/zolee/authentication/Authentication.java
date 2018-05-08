package com.zolee.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zolee.domain.Employee;
import com.zolee.service.EmployeeService;

@Service
public class Authentication {

	private EmployeeService employeeService;

	@Autowired
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	private PasswordHashing passwordHashing;
	
	@Autowired
	public void setPasswordHashing(PasswordHashing passwordHashing) {
		this.passwordHashing = passwordHashing;
	}

	public boolean authenticate(String name, String password) {
		Employee employee = employeeService.findByUserName(name);
		if(employee!=null && employee.getUserName().equals(name) && employee.getPassword().equals(passwordHashing.generateHashCode(password)))
			return true;
		return false;
	}
}
