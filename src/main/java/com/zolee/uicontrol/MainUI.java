package com.zolee.uicontrol;

import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.Page.UriFragmentChangedEvent;
import com.vaadin.server.Page.UriFragmentChangedListener;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

@SuppressWarnings("deprecation")
@Theme("valo")
@SpringUI
public class MainUI extends UI {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	SpringViewProvider springViewProvider;

	@Override
	protected void init(VaadinRequest request) {
		
		Navigator navigator = new Navigator(this, this);
		navigator.addProvider(springViewProvider);
		
		getNavigator().addView(LoginPage.NAME, LoginPage.class);
		getNavigator().setErrorView(LoginPage.class);

		Page.getCurrent().addUriFragmentChangedListener(new UriFragmentChangedListener() {
			
			@Override
			public void uriFragmentChanged(UriFragmentChangedEvent event) {
				router(event.getUriFragment());
			}
		});
		
		router("");
	}

	private void router(String route) {
		Notification.show(route);
		if(getSession().getAttribute("employeename") != null && getSession().getAttribute("employeeright").equals("admin")){
			getNavigator().addView(EditEmployeesPage.NAME, EditEmployeesPage.class);
			getNavigator().addView(EditOrdersPage.NAME, EditOrdersPage.class);
			getNavigator().addView(SettingsPage.NAME, SettingsPage.class);
			if(route.equals("!EditEmployees")) {
				getNavigator().navigateTo(EditEmployeesPage.NAME);
			}else if(route.equals("!PasswordChange")){
				getNavigator().navigateTo(SettingsPage.NAME);
			}else {
				getNavigator().navigateTo(EditOrdersPage.NAME);
			}
		}else if(getSession().getAttribute("employeename") != null && getSession().getAttribute("employeeright").equals("user")) {
			getNavigator().addView(EditOrdersPage.NAME, EditOrdersPage.class);
			getNavigator().addView(SettingsPage.NAME, SettingsPage.class);
			if(route.equals("!PasswordChange")) {
				getNavigator().navigateTo(SettingsPage.NAME);
			}else {
				getNavigator().navigateTo(EditOrdersPage.NAME);
			}
		}else{
			getNavigator().navigateTo(LoginPage.NAME);
		}
		
	}
	
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = MainUI.class)
	public static class Servlet extends VaadinServlet {
	}

}
