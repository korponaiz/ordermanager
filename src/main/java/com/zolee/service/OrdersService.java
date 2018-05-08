package com.zolee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zolee.domain.Orders;
import com.zolee.repository.OrdersRepository;

@Service
public class OrdersService {

	private OrdersRepository ordersRepository;

	@Autowired
	public void setOrdersRepository(OrdersRepository ordersRepository) {
		this.ordersRepository = ordersRepository;
	}
	
	public List<Orders> findAll(){
		return ordersRepository.findAll();
	}
	
	public List<Orders> findByOrderstate(String orderstate){
		return ordersRepository.findByOrderstate(orderstate);
	}

	public void save(Orders orders) {
		ordersRepository.save(orders);
	}
	
	public void delete(long id) {
		ordersRepository.delete(id);
	}
	
}
