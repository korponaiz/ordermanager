package com.zolee.uicontrol;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.zolee.authentication.PasswordHashing;
import com.zolee.domain.Employee;
import com.zolee.service.EmployeeService;

@SpringView(name = EditEmployeesPage.NAME)
public class EditEmployeesPage extends AdminPagePattern{

	private static final long serialVersionUID = 1L;
	public static final String NAME = "EditEmployees";

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

	private VerticalLayout mainLayout;
	private HorizontalLayout inputLayout;
	private HorizontalLayout editButtonLayout;
	private Label titleLabel;
	private TextField userNameField;
	private TextField realNameField;
	private PasswordField passwordField;
	private NativeSelect<String> rightSelect;
	private Button saveButton;
	private Button updateButton;
	private Button deleteButton;
	private Grid<Employee> grid;
	private long selectedEmployeId;

	public EditEmployeesPage() {
		
		mainLayout = new VerticalLayout();
		inputLayout = new HorizontalLayout();
		editButtonLayout = new HorizontalLayout();
		titleLabel = new Label("Felhasználók szerkesztése");
		saveButton = new Button("Ment");
		updateButton = new Button("Átír");
		deleteButton = new Button("Töröl");
		userNameField = new TextField("User Name:");
		realNameField = new TextField("Real Name:");
		passwordField = new PasswordField("Password:");
		rightSelect = new NativeSelect<>("User right");
		rightSelect.setItems("admin", "user");
		rightSelect.setSelectedItem("user");
		grid = new Grid<>(Employee.class);
		grid.setColumns("userName", "realName", "right");
		inputLayout.setSpacing(false);
		editButtonLayout.setSpacing(false);

		grid.setWidth("660px");
		grid.getColumn("userName").setCaption("Felhasználó név").setWidth(220);
		grid.getColumn("realName").setCaption("Teljes név").setWidth(220);
		grid.getColumn("right").setCaption("Felhasználó jog").setWidth(220);
		userNameField.setWidth("220px");
		realNameField.setWidth("220px");
		passwordField.setWidth("220px");
		rightSelect.setWidth("220px");
		saveButton.setWidth("220px");
		updateButton.setWidth("220px");
		deleteButton.setWidth("220px");

		addComponent(mainLayout);
		mainLayout.addComponents(titleLabel, inputLayout, editButtonLayout, grid);
		editButtonLayout.addComponents(saveButton, updateButton, deleteButton);
		inputLayout.addComponents(userNameField, realNameField, rightSelect, passwordField);

		grid.asSingleSelect().addValueChangeListener( e -> {
			gridSelectEvent(e);
		});
		saveButton.addClickListener( e -> save());
		deleteButton.addClickListener( e -> delete());
		updateButton.addClickListener( e -> update());
	}

	private void gridSelectEvent(ValueChangeEvent<Employee> e) {
		if(e.getValue()!=null) {
			userNameField.setValue(e.getValue().getUserName());
			realNameField.setValue(e.getValue().getRealName());
			rightSelect.setValue(e.getValue().getRight());
			passwordField.setValue(e.getValue().getPassword());
			selectedEmployeId = e.getValue().getId();
		}
	}
	
	@PostConstruct
	private void init() {
		upDateGrid();
	}
	
	private void upDateGrid() {
		grid.setItems(employeeService.findAll());
	}
	
	private void save() {
		if(checkEmptyField()) {
			Notification.show("Empty field!");
		}else if(employeeService.findByUserName(userNameField.getValue())!=null) {
			Notification.show("Existing user!");
		}else {
			employeeService.save(new Employee(userNameField.getValue(), realNameField.getValue(),
					passwordHashing.generateHashCode(passwordField.getValue()),
					rightSelect.getValue()));
		}
		upDateGrid();
	}
	
	private void update() {
		if(checkEmptyField()) {
			Notification.show("Empty field!");
		}else {
			employeeService.save(new Employee(selectedEmployeId, userNameField.getValue(), realNameField.getValue(),
					passwordHashing.generateHashCode(passwordField.getValue()),
					rightSelect.getValue()));
		}
		upDateGrid();
	}

	private void delete() {
		if(selectedEmployeId!=0) {
			employeeService.delete(selectedEmployeId);
		}
		upDateGrid();
	}

	private boolean checkEmptyField() {
		if(userNameField.getValue().equals("") || realNameField.getValue().equals("") || rightSelect.getValue()==null ||
				passwordField.getValue().equals("")) {
			return true;
		}
		return false;
	}
}
