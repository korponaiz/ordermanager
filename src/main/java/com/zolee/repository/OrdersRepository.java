package com.zolee.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.zolee.domain.Orders;

public interface OrdersRepository extends CrudRepository<Orders, Long> {
	
	List<Orders> findAll();
	
	List<Orders> findByOrderstate(String orderstate);

}
