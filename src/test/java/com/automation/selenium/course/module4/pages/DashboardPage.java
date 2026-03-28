package com.automation.selenium.course.module4.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;


public class DashboardPage {
	private WebDriver driver;
	
	private By profileUser = By.id("profileUser");
	private By loginButton = By.xpath("//a[@href='./login.html']");
	
	public DashboardPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void checkProfileUser(String expectedUsername) {
		Assert.assertEquals(driver.findElement(profileUser).getText(), expectedUsername);
	}
	
	public void checkLoginButtonVisibility() {
		Assert.assertTrue(driver.findElement(loginButton).isDisplayed());
	}
	
}
