package com.automation.selenium.course.module1;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SeleniumTests {
	/*
	 * 1. Ejecutamos acciones
	 * 2. Observamos resultados
	 * 3. Validamos si el comportamiento es correcto
	 * 
	 * Setup
	 * 
	 * Steps
	 *  - Localizar Elemento
	 *  - Interactuar
	 * Assertions
	 *  - Localizar un Elemento
	 *  - Validar comportamiento
	 *  
	 * TearDown
	 * */	 
		@Test
		public void simpleSeleniumLoginTest() {
			//Setup
			WebDriver driver = new ChromeDriver();
			//------------------
			
			//Steps 
			//------------------
			driver.manage().window().maximize();
			
			driver.get("https://seleniumjavalocators.neocities.org/pages/login");
			System.out.println("Titulo: " + driver.getTitle());
			System.out.println("URL: " + driver.getCurrentUrl());
			
			//Localizamos Elemento
			WebElement userInput = driver.findElement(By.id("username_id"));
			//Interactuamos
			userInput.sendKeys("fabiangomez");
					
			
			WebElement passwordInput = driver.findElement(By.id("password"));
			passwordInput.sendKeys("banco123");
						
			WebElement loginButton = driver.findElement(By.id("iniciarSesionBtn"));
			loginButton.click();
						
			//Assertions
			
			//------------------
			WebElement loginMessage = driver.findElement(By.id("mensajeInicioSesion"));
			System.out.println(loginMessage.getText());
			System.out.println(loginMessage.getDomAttribute("data-testid"));
			System.out.println(loginMessage.getDomAttribute("data-qa"));	
			
			
			Assert.assertEquals(loginMessage.getText(), "Intentaste iniciar sesion con usuario 'fabiangomez' y contrasena 'banco123'.");
			Assert.assertEquals(loginMessage.getDomAttribute("data-testid"), "login-message");
			Assert.assertEquals(loginMessage.getDomAttribute("data-qa"), "login-feedback");
			//TearDown
			
			driver.quit();
			//------------
			
		}
		
}
