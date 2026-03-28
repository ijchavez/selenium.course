package com.automation.selenium.course.module4.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class LoginPage extends BasePage{
	private By usernameInput = By.id("username_id");
	private By passwordInput = By.id("password");
	private By LoginButton = By.id("iniciarSesionBtn");
	private By loginMessageEyebrow = By.id("loginMessageEyebrow");
	private By loginMessageTitle = By.id("loginMessageTitle");
	private By loginMessageBody = By.id("loginMessageBody");
	
	private static final String SUCCESS_EYEBROW_MESSAGE = "LOGIN CORRECTO";
	private static final String SUCCESS_LOGIN_TITLE = "Inicio de sesion exitoso";
	private static final String SUCCESS_LOGIN_MESSAGE_BODY = "Bienvenido al laboratorio. El acceso fue concedido correctamente. Seras redirigido a una pagina mock.";
	private static final String BLOCKED_EYEBROW_MESSAGE = "ACCESO RESTRINGIDO";
	private static final String BLOCKED_LOGIN_TITLE = "Usuario bloqueado";
	private static final String BLOCKED_LOGIN_MESSAGE_BODY = "La cuenta esta bloqueada temporalmente. Debes solicitar un desbloqueo antes de continuar.";
	private static final String INVALID_EYEBROW_MESSAGE = "CREDENCIALES INVALIDAS";
	private static final String INVALID_LOGIN_TITLE = "Usuario o contrasena incorrecto";
	private static final String INVALID_LOGIN_MESSAGE_BODY = "La combinacion ingresada no es valida. Revisa la seccion de credenciales de prueba e intenta nuevamente.";
	private static final String SUSPENDED_EYEBROW_MESSAGE = "CUENTA INHABILITADA";
	private static final String SUSPENDED_LOGIN_TITLE = "Usuario inhabilitado";
	private static final String SUSPENDED_LOGIN_MESSAGE_BODY = "La cuenta no tiene acceso habilitado. Necesitas reactivacion para iniciar sesion.";
	
	
	public LoginPage(WebDriver driver) {
		super(driver);
	}
	
	public void doFillUsernameInput(String username) {
		driver.findElement(usernameInput).sendKeys(username);
		
	}
	
	public void doFillPasswordInput(String password) {
		driver.findElement(passwordInput).sendKeys(password);
		
	}
	
	public void doClickOnLoginButton() {
		driver.findElement(LoginButton).click();
	}
	
	public DashboardPage doClickOnLoginButtonExpectSuccess() {
		driver.findElement(LoginButton).click();
		
		return new DashboardPage(driver);
	}
	
	public void checkSuccessfulLoginMessageEyebrow() {
		Assert.assertEquals(
				driver.findElement(loginMessageEyebrow).getText(), 
				SUCCESS_EYEBROW_MESSAGE, 
				"El texto del eyebrow no es correcto");
	}
	
	public void checkSuccessfulLoginMessageTitle() {
		Assert.assertEquals(
				driver.findElement(loginMessageTitle).getText(), 
				SUCCESS_LOGIN_TITLE, 
				"El texto del titulo no es correcto");
	}
		
	public void checkSuccessfulLoginMessageBody() {
		Assert.assertEquals(
				driver.findElement(loginMessageBody).getText(), 
				SUCCESS_LOGIN_MESSAGE_BODY, 
				"El texto del body no es correcto");
	}
	
	public void checkBlockedLoginMessageEyebrow() {
		Assert.assertEquals(
				driver.findElement(loginMessageEyebrow).getText(), 
				BLOCKED_EYEBROW_MESSAGE, 
				"El texto del eyebrow no es correcto");
	}
	
	public void checkBlockedLoginMessageTitle() {
		Assert.assertEquals(
				driver.findElement(loginMessageTitle).getText(), 
				BLOCKED_LOGIN_TITLE, 
				"El texto del titulo no es correcto");
	}
		
	public void checkBlockedLoginMessageBody() {
		Assert.assertEquals(
				driver.findElement(loginMessageBody).getText(), 
				BLOCKED_LOGIN_MESSAGE_BODY, 
				"El texto del body no es correcto");
	}
	
	public void checkInvalidLoginMessageEyebrow() {
		Assert.assertEquals(
				driver.findElement(loginMessageEyebrow).getText(), 
				INVALID_EYEBROW_MESSAGE, 
				"El texto del eyebrow no es correcto");
	}
	
	public void checkInvalidLoginMessageTitle() {
		Assert.assertEquals(
				driver.findElement(loginMessageTitle).getText(), 
				INVALID_LOGIN_TITLE, 
				"El texto del titulo no es correcto");
	}
		
	public void checkInvalidLoginMessageBody() {
		Assert.assertEquals(
				driver.findElement(loginMessageBody).getText(), 
				INVALID_LOGIN_MESSAGE_BODY, 
				"El texto del body no es correcto");
	}

	public void checkSuspendedLoginMessageEyebrow() {
		Assert.assertEquals(
				driver.findElement(loginMessageEyebrow).getText(), 
				SUSPENDED_EYEBROW_MESSAGE, 
				"El texto del eyebrow no es correcto");
	}
	
	public void checkSuspendedLoginMessageTitle() {
		Assert.assertEquals(
				driver.findElement(loginMessageTitle).getText(), 
				SUSPENDED_LOGIN_TITLE, 
				"El texto del titulo no es correcto");
	}
		
	public void checkSuspendedLoginMessageBody() {
		Assert.assertEquals(
				driver.findElement(loginMessageBody).getText(), 
				SUSPENDED_LOGIN_MESSAGE_BODY, 
				"El texto del body no es correcto");
	}
	
}
