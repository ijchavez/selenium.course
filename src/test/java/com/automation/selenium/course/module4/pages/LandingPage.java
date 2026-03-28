package com.automation.selenium.course.module4.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class LandingPage {
	private WebDriver driver;
	
	private By loginLink = By.cssSelector("a[href='./pages/login.html']");
	private By registrationLink = By.cssSelector("a[href='./pages/registro.html']");
	
	public LandingPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public LoginPage doClickOnLoginLink() {
		driver.findElement(loginLink).click();
		
		return new LoginPage(driver);
	}
	
	public RegistrationPage doClickOnRegistrationLink() {
		driver.findElement(registrationLink).click();
		
		return new RegistrationPage(driver);
	}
	
}
