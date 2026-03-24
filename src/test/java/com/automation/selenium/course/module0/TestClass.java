package com.automation.selenium.course.module0;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class TestClass {
	@Test
	public void simpleTest() {
		System.out.println("Hola QAs");
		
	}


/*
 * 1. Ejecutamos acciones
 * 2. Observamos resultados
 * 3. Validamos si el comportamiento es correcto
 * 
 * */	 
	@Test
	public void simpleSeleniumTest() {
		WebDriver driver = new ChromeDriver();

		driver.manage().window().maximize();
		
		driver.get("https://seleniumjavalocators.vercel.app/pages/login.html");
		System.out.println("Titulo: " + driver.getTitle());
		System.out.println("URL: " + driver.getCurrentUrl());
		
		//System.out.println(driver.getPageSource());
		
		driver.navigate().to("https://seleniumjavalocators.vercel.app/pages/registro.html");
		
		System.out.println("Titulo: " + driver.getTitle());
		System.out.println("URL: " + driver.getCurrentUrl());
		
		driver.navigate().back();
		System.out.println("Hago navigate back");
		
		System.out.println("Titulo: " + driver.getTitle());
		System.out.println("URL: " + driver.getCurrentUrl());
		
		driver.navigate().forward();
		System.out.println("Hago navigate forward");
		
		System.out.println("Titulo: " + driver.getTitle());
		System.out.println("URL: " + driver.getCurrentUrl());
		
		driver.navigate().refresh();
		
		//driver.quit() cierra todas las sesiones, driver.close() cierra la pestaña actual
		driver.quit();
		
	}

}
