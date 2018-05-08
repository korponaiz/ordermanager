package com.zolee.uicontrol;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.zolee.domain.Orders;
import com.zolee.service.EmployeeService;
import com.zolee.service.OrdersService;

@SpringView(name = EditOrdersPage.NAME)
public class EditOrdersPage extends AdminPagePattern {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "EditOrders";

	private OrdersService ordersService;
	
	@Autowired	
	public void setOrdersService(OrdersService ordersService) {
		this.ordersService = ordersService;
	}

	private EmployeeService employeeService;
	
	@Autowired
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	private VerticalLayout mainLayout;
	private HorizontalLayout inputLayout;
	private HorizontalLayout button2Layout;
	private Label titleLabel;
	private Label inputLabel;
	private TextField orderNameField;
	private TextField orderQuantityField;
	private TextField orderDescriptionField;
	private TextField orderDescription2Field;
	private Button saveButton;
	private Button closeButton;
	private Button updateButton;
	private Button deleteButton;
	private Button allOrdersButton;
	private Button openOrdersButton;
	private Button closeOrdersButton;
	private Grid<Orders> grid;
	private long selectedOrderId;
	private List<Orders> openOrdersList;

	public EditOrdersPage() {
		
		mainLayout = new VerticalLayout();
		openOrdersList = new ArrayList<>();
		inputLayout = new HorizontalLayout();
		button2Layout = new HorizontalLayout();
		titleLabel = new Label("Megrendelések szerkesztése");
		inputLabel = new Label(" ");
		orderNameField = new TextField("Megnevezés:");
		orderQuantityField = new TextField("Mennyiség:");
		orderDescriptionField = new TextField("Mihez kell:");
		orderDescription2Field = new TextField("Megjegyzés:");
		saveButton = new Button("Ment");
		closeButton = new Button("Lezár");
		updateButton = new Button("Átír");
		deleteButton = new Button("Töröl");
		allOrdersButton = new Button("Mind");
		openOrdersButton = new Button("Nyitott");
		closeOrdersButton = new Button("Zárt");
		grid = new Grid<>(Orders.class);
		inputLayout.setSpacing(false);
		button2Layout.setSpacing(false);

		addComponent(mainLayout);
		mainLayout.addComponents(titleLabel, button2Layout, inputLayout, grid);
		button2Layout.addComponents(saveButton, closeButton, updateButton, deleteButton, allOrdersButton, openOrdersButton, closeOrdersButton);
		inputLayout.addComponents(inputLabel, orderNameField, orderQuantityField, orderDescriptionField, orderDescription2Field);
		
		grid.setSizeUndefined();
		grid.setWidth("1120px");
		grid.getColumn("ordername").setCaption("Megnevezés").setWidth(320);
		grid.getColumn("orderdate").setCaption("Dátum").setWidth(120);
		grid.getColumn("orderquantity").setCaption("Mennyiség").setWidth(100);
		grid.getColumn("orderdescription").setCaption("Mihez kell").setWidth(200);
		grid.getColumn("orderdescription2").setCaption("Megjegyzés").setWidth(200);
		grid.getColumn("orderstate").setCaption("Állapot").setWidth(80);
		grid.getColumn("employee").setCaption("Ki").setWidth(100);
		
		inputLabel.setWidth("120px");
		orderNameField.setWidth("160px");
		orderQuantityField.setWidth("80px");
		orderDescriptionField.setWidth("160px");
		orderDescription2Field.setWidth("320px");
		saveButton.setWidth("120px");
		closeButton.setWidth("120px");
		updateButton.setWidth("120px");
		deleteButton.setWidth("120px");
		allOrdersButton.setWidth("120px");
		openOrdersButton.setWidth("120px");
		closeOrdersButton.setWidth("120px");
		
		grid.asSingleSelect().addValueChangeListener( e -> {
			gridSelectEvent(e);
		});

		orderQuantityField.addValueChangeListener( e -> orderQuantityFieldEvent());
		
		saveButton.addClickListener( e -> save());
		updateButton.addClickListener( e -> update());
		closeButton.addClickListener( e -> closeOrder());
		deleteButton.addClickListener( e -> delete());
		allOrdersButton.addClickListener( e -> upDateGrid(""));
		openOrdersButton.addClickListener( e -> upDateGrid("open"));
		closeOrdersButton.addClickListener( e -> upDateGrid("close"));

	}
	
	@PostConstruct
	private void init() {
		openOrdersList = ordersService.findByOrderstate("open");
		grid.setItems(openOrdersList);
		upDateGrid("");
	}

	private void upDateGrid(String state) {
		List<Orders> resultList;
		if(state.equals("")) {
			resultList = ordersService.findAll();
		}else {
			resultList = ordersService.findByOrderstate(state);
		}
		if(resultList!=null) {
			grid.setItems(resultList);
			grid.setColumns("orderdate", "ordername", "orderquantity", "orderdescription", "orderdescription2", "orderstate", "employee");
		}
	}

	private void gridSelectEvent(ValueChangeEvent<Orders> e) {
		if(e.getValue()!=null) {
			orderNameField.setValue(e.getValue().getOrdername());
			orderQuantityField.setValue(""+e.getValue().getOrderquantity());
			orderDescriptionField.setValue(e.getValue().getOrderdescription());
			orderDescription2Field.setValue(e.getValue().getOrderdescription2());
			selectedOrderId = e.getValue().getId();
		}
	}

	private void orderQuantityFieldEvent() {
		if(orderQuantityField.getValue().length()!=0) {
			if(!checkNumberFormat()) {
				Notification.show("Only number");
			}
		}
	}
	
	private void save() {
		if(checkEmptyField()) {
			Notification.show("Üres mező!");
		}else if(checkNumberFormat()) {
			ordersService.save(new Orders(LocalDate.now(), orderNameField.getValue(), Integer.parseInt(orderQuantityField.getValue()),
					"open", orderDescriptionField.getValue(), orderDescription2Field.getValue(),
					(employeeService.findByUserName(VaadinSession.getCurrent().getAttribute("employeename").toString()))));
			upDateGrid("open");
		}else {
			Notification.show("Hibás adatok!");
		}
	}
	
	private void update() {
		if(checkEmptyField()) {
			Notification.show("Üres mező!");
		}
		else if(selectedOrderId!=0) {
			ordersService.save(new Orders(selectedOrderId, LocalDate.now(), orderNameField.getValue(), Integer.parseInt(orderQuantityField.getValue()),
					"open", orderDescriptionField.getValue(), orderDescription2Field.getValue(),
					(employeeService.findByUserName(VaadinSession.getCurrent().getAttribute("employeename").toString()))));
			upDateGrid("open");
		}
	}
	
	private void closeOrder() {
		if(selectedOrderId!=0) {
			ordersService.save(new Orders(selectedOrderId, LocalDate.now(), orderNameField.getValue(), Integer.parseInt(orderQuantityField.getValue()),
				"close", orderDescriptionField.getValue(), orderDescription2Field.getValue(),
				(employeeService.findByUserName(VaadinSession.getCurrent().getAttribute("employeename").toString()))));
			upDateGrid("close");
		}
	}
	
	private void delete() {
		if(selectedOrderId!=0) {
			ordersService.delete(selectedOrderId);
			upDateGrid("open");
		}
	}

	private boolean checkEmptyField() {
		if(orderNameField.getValue().equals("") || orderQuantityField.getValue().equals("")) {
			return true;
		}
		return false;
	}

	private boolean checkNumberFormat() {
		try{
			Integer.parseInt(orderQuantityField.getValue());
			}
		catch(NumberFormatException e){
			return false;
		}
		return true;
	}

}
