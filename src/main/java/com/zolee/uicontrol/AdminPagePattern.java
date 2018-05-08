package com.zolee.uicontrol;

import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class AdminPagePattern extends HorizontalLayout implements View{

	private static final long serialVersionUID = 1L;

	private VerticalLayout buttonLayout;
//	VerticalLayout mainLayout;
	private Label currentUser;
	private Button editEmployeePageButton; 
	private Button editOrderPageButton; 
	private Button settingsButton;
	private Button logoutButton;

	public AdminPagePattern() {

		buttonLayout = new VerticalLayout();
//		mainLayout = new VerticalLayout();
		currentUser = new Label("" );
		editEmployeePageButton = new Button("Felhasználók");
		editOrderPageButton = new Button("Rendelések");
		settingsButton = new Button("Beállítások");
		logoutButton = new Button("Kijelentkezés");

		editEmployeePageButton.setEnabled(false);
		if(VaadinSession.getCurrent().getAttribute("employeeright").equals("admin")) {
			editEmployeePageButton.setEnabled(true);
		}
		
		currentUser.setValue("Felhasználó: " + VaadinSession.getCurrent().getAttribute("employeename").toString());

		editEmployeePageButton.setWidth("150px");
		editOrderPageButton.setWidth("150px");
		settingsButton.setWidth("150px");
		logoutButton.setWidth("150px");

		setSpacing(false);
		addComponent(buttonLayout/*, mainLayout*/);
		buttonLayout.addComponents(currentUser, logoutButton, editOrderPageButton, settingsButton, editEmployeePageButton);
		currentUser.setValue("Felhasználó: " + VaadinSession.getCurrent().getAttribute("employeename").toString());
		editEmployeePageButton.addClickListener( e -> Page.getCurrent().setUriFragment("!"+EditEmployeesPage.NAME));
		editOrderPageButton.addClickListener( e -> Page.getCurrent().setUriFragment("!" + EditOrdersPage.NAME));
		settingsButton.addClickListener( e -> Page.getCurrent().setUriFragment("!"+SettingsPage.NAME));
		logoutButton.addClickListener( e -> logout());

	}

	private void logout() {
		if(VaadinSession.getCurrent().getAttribute("employeeright").equals("admin")) {
			getUI().getNavigator().removeView(EditOrdersPage.NAME);
			getUI().getNavigator().removeView(EditEmployeesPage.NAME);
			getUI().getNavigator().removeView(SettingsPage.NAME);
			VaadinSession.getCurrent().setAttribute("employeename", null);
			Page.getCurrent().setUriFragment("");
		}else {
			getUI().getNavigator().removeView(EditOrdersPage.NAME);
			getUI().getNavigator().removeView(SettingsPage.NAME);
			VaadinSession.getCurrent().setAttribute("employeename", null);
			Page.getCurrent().setUriFragment("");
		}
	}

}
