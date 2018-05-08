package com.zolee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zolee.domain.Employee;
import com.zolee.repository.EmployeeRepository;

@Service
public class EmployeeService {

	private EmployeeRepository employeeRepository;
	
	@Autowired
	public void setEmployeeRepository(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	public List<Employee> findAll(){
		return employeeRepository.findAll();
	}

	public Employee findByUserName(String userName) {
		return employeeRepository.findByUserName(userName);
	}
	
	public Employee findByRealName(String realName) {
		return employeeRepository.findByRealName(realName);
	}
	
	public void save(Employee employee) {
		employeeRepository.save(employee);
	}

	public void delete(long id) {
		employeeRepository.delete(id);
	}

}
