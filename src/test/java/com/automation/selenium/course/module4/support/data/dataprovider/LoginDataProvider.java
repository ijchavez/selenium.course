package com.automation.selenium.course.module4.support.data.dataprovider;

import org.testng.annotations.DataProvider;

import com.automation.selenium.course.module3.utilities.Utilities;

public class LoginDataProvider {
	
	@DataProvider(name="invalidUserData")
	public Object[][] invalidUserData(){
		return new Object[][]{
			{"usuario.demo", "lock123!!"},
			{"usuario.demo.02", "lock123!!"},
			{"usuario.demo.03", "off2251!!"},
			{"usuario.inhabilitado.02", "Qa1234!!"},
			{"usuario.bloqueado.02", "Qa1234!!"},
			{"usuario.bloqueado.03", "Off1234!"},
			{"usuario.demo.-1", "lock123!!"},
			{"usuario.demo.99", "lock123!!"},
			{"usuario.demo.04", "off2251!!"},
			{"usuario.inhabilitado.096", "Qa1234!!"},
			{"usuario.bloqueado.05", "Qa1234!!"},
			{"usuario.bloqueado.08", "Off1234!"}
		};
	}
	
	@DataProvider(name = "suspendedUserData")
	public Object[][] suspendedUserData() throws Exception{
		return Utilities.readFromExcelFile("C:\\Viejo D\\selenium-java-course\\selenium.course\\src\\test\\resources\\usuariosInhabilitados.xlsx", "Sheet1");
	}
}
