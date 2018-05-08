package com.zolee.uicontrol;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.zolee.authentication.Authentication;
import com.zolee.domain.Orders;
import com.zolee.service.EmployeeService;
import com.zolee.service.OrdersService;

@SpringView(name = LoginPage.NAME)
public class LoginPage extends VerticalLayout implements View{

	private static final long serialVersionUID = 1L;
	public static final String NAME = "";

	private Authentication authenticationService;
	
	@Autowired
	public void setAuthenticationService(Authentication authenticationService) {
		this.authenticationService = authenticationService;
	}
	
	private EmployeeService employeeService;
	
	@Autowired
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	private OrdersService ordersService;
	
	@Autowired
	public void setOrdersService(OrdersService ordersService) {
		this.ordersService = ordersService;
	}

	private HorizontalLayout titleLayout;
	private Label titleLabel;
	private Label emptyLabel;
	private Label userNameLabel;
	private Label passwordLabel;
	private TextField userNameField;
	private PasswordField passwordField;
	private Panel panel;
	private FormLayout content;
	private Button okButton;
	private Grid<Orders> grid;

	public LoginPage() {

		titleLayout = new HorizontalLayout();
		panel = new Panel("Rendelés nyilvántartó");
		content = new FormLayout();
		titleLabel = new Label("Rendelés nyilvántartó");
		emptyLabel = new Label();
		userNameLabel = new Label("Név:");
		passwordLabel = new Label("Jelszó:");
		userNameField = new TextField();
		passwordField = new PasswordField();
		okButton = new Button("OK");
		grid = new Grid<>(Orders.class);

		grid.setWidth("1120px");
		grid.getColumn("ordername").setCaption("Megnevezés").setWidth(320);
		grid.getColumn("orderdate").setCaption("Dátum").setWidth(120);
		grid.getColumn("orderquantity").setCaption("Mennyiség").setWidth(100);
		grid.getColumn("orderdescription").setCaption("Mihez kell").setWidth(200);
		grid.getColumn("orderdescription2").setCaption("Megjegyzés").setWidth(200);
		grid.getColumn("orderstate").setCaption("Állapot").setWidth(80);
		grid.getColumn("employee").setCaption("Ki").setWidth(100);

		userNameField.setWidth("160px");
		passwordField.setWidth("160px");
		okButton.setWidth("120px");
		emptyLabel.setWidth("140px");

		addComponents(titleLayout, grid);
		titleLayout.addComponents(titleLabel, emptyLabel, userNameLabel, userNameField, passwordLabel, passwordField, okButton);
		panel.setContent(content);

		okButton.addClickListener( e -> authenticate());
	}
	
	@PostConstruct
	private void init() {
		List<Orders> openOrdersList;
		openOrdersList = ordersService.findByOrderstate("open");
		if(openOrdersList!=null) {
			grid.setItems(openOrdersList);
			grid.setColumns("orderdate", "ordername", "orderquantity", "orderdescription", "orderdescription2", "orderstate", "employee");
		}
	}
	
	private void authenticate() {
		if(authenticationService.authenticate(userNameField.getValue(), passwordField.getValue()) && employeeService.findByUserName(userNameField.getValue()).getRight().equals("admin")){
			VaadinSession.getCurrent().setAttribute("employeename", userNameField.getValue());
			VaadinSession.getCurrent().setAttribute("employeeright", "admin");
			getUI().getNavigator().addView(EditEmployeesPage.NAME, EditEmployeesPage.class);
			getUI().getNavigator().addView(EditOrdersPage.NAME, EditOrdersPage.class);
			getUI().getNavigator().addView(SettingsPage.NAME, SettingsPage.class);
			Page.getCurrent().setUriFragment("!"+EditOrdersPage.NAME);
		}else if(authenticationService.authenticate(userNameField.getValue(), passwordField.getValue()) && employeeService.findByUserName(userNameField.getValue()).getRight().equals("user")) {
			VaadinSession.getCurrent().setAttribute("employeename", userNameField.getValue());
			VaadinSession.getCurrent().setAttribute("employeeright", "user");
			getUI().getNavigator().addView(EditOrdersPage.NAME, EditOrdersPage.class);
			getUI().getNavigator().addView(SettingsPage.NAME, SettingsPage.class);
			Page.getCurrent().setUriFragment("!"+EditOrdersPage.NAME);
		}else{
			Notification.show("Invalid credentials", Notification.Type.ERROR_MESSAGE);
		}
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
	}
}
