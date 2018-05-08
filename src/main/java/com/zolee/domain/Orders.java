package com.zolee.domain;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Orders {

	@GeneratedValue
	@Id
	private Long id;
	private LocalDate orderdate;
	private String ordername;
	private int orderquantity;
	private String orderstate; 
	private String orderdescription;
	private String orderdescription2;
	@ManyToOne
	private Employee employee;

	public Orders() {
	}

	public Orders(LocalDate orderdate, String ordername, int orderquantity, String orderstate, String orderdescription,
			String orderdescription2, Employee employee) {
		this.ordername = ordername;
		this.orderdate = orderdate;
		this.orderquantity = orderquantity;
		this.orderstate = orderstate;
		this.orderdescription = orderdescription;
		this.orderdescription2 = orderdescription2;
		this.employee = employee;
	}

	public Orders(Long id, LocalDate orderdate, String ordername, int orderquantity, String orderstate,
			String orderdescription, String orderdescription2, Employee employee) {
		this.id = id;
		this.ordername = ordername;
		this.orderdate = orderdate;
		this.orderquantity = orderquantity;
		this.orderstate = orderstate;
		this.orderdescription = orderdescription;
		this.orderdescription2 = orderdescription2;
		this.employee = employee;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrdername() {
		return ordername;
	}

	public void setOrdername(String ordername) {
		this.ordername = ordername;
	}

	public int getOrderquantity() {
		return orderquantity;
	}

	public void setOrderquantity(int orderquantity) {
		this.orderquantity = orderquantity;
	}

	public String getOrderdescription() {
		return orderdescription;
	}

	public void setOrderdescription(String orderdescription) {
		this.orderdescription = orderdescription;
	}

	public String getOrderdescription2() {
		return orderdescription2;
	}

	public void setOrderdescription2(String orderdescription2) {
		this.orderdescription2 = orderdescription2;
	}

	public LocalDate getOrderdate() {
		return orderdate;
	}

	public void setOrderdate(LocalDate orderdate) {
		this.orderdate = orderdate;
	}

	public String getOrderstate() {
		return orderstate;
	}

	public void setOrderstate(String orderstate) {
		this.orderstate = orderstate;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
}
