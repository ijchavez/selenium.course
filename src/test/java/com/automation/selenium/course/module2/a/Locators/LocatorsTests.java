package com.automation.selenium.course.module2.a.Locators;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class LocatorsTests {
	/*
	 * - ID -> Ojo los IDs dinámicos
	 * - Name 
	 * - Atributos funcionales, data-testid
	 * - cssSelector
	 * - Xpath
	 * - Indices
	 * 
	 * */
	
	@Test
	public void locatorsTest() {
		//Setup
		WebDriver driver = new ChromeDriver();
		//------------------
		
		//Steps 
		//------------------
		driver.manage().window().maximize();
		
		driver.get("https://seleniumjavalocators.vercel.app/pages/locators-challenge.html");
		
		WebElement stateChangerButton = driver.findElement(By.name("stateChanger"));
		System.out.println(stateChangerButton.getText());
		
		System.out.println("==================");
		
		WebElement hintsElement = driver.findElement(By.className("hints"));
		System.out.println(hintsElement.getText());	
		
		System.out.println("==================");
		
		// <a> Soy Un Link</a> 
		
		
		WebElement linkElementByLinkText = driver.findElement(By.linkText("Link de prueba (no navega)"));
		System.out.println(linkElementByLinkText.getText());	
		
		System.out.println("==================");
		
		WebElement linkElementByPartialLinkText = driver.findElement(By.partialLinkText("Link de "));
		System.out.println(linkElementByPartialLinkText.getText());	
		
		System.out.println("==================");
		
		//TearDown
		
		driver.quit();
		//------------
		
	}
}
