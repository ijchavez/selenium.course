package com.automation.selenium.course.module2.b.BooleanDriverFunctions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class BooleanDriverFunctions {
	/**
	 * Clase de apoyo para practicar tres metodos muy usados de Selenium WebDriver.
	 *
	 * <p><b>isSelected()</b>:
	 * se utiliza para verificar si un elemento seleccionable esta marcado o no.
	 * Normalmente se usa con checkboxes, radio buttons y algunos toggles.
	 * Devuelve <code>true</code> si el elemento esta seleccionado y <code>false</code> si no lo esta.
	 *
	 * <p><b>isEnabled()</b>:
	 * se utiliza para verificar si un elemento esta habilitado para interactuar.
	 * Normalmente se aplica sobre botones, inputs, selects u otros controles de formulario.
	 * Devuelve <code>true</code> si el elemento puede usarse y <code>false</code> si esta deshabilitado.
	 *
	 * <p><b>isDisplayed()</b>:
	 * se utiliza para verificar si un elemento es visible en la interfaz.
	 * Sirve para confirmar si un texto, boton, cartel, loader o cualquier otro componente
	 * realmente aparece en pantalla para el usuario.
	 * Devuelve <code>true</code> si el elemento esta visible y <code>false</code> si no se muestra.
	 *
	 * <p>Estos metodos son utiles para validar estados basicos de la UI durante una automatizacion.
	 */
	@Test
	public void isDisplayedTest() {
		//Setup
		WebDriver driver = new ChromeDriver();
		//------------------
		
		//Steps 
		//------------------
		driver.manage().window().maximize();
		
		driver.get("https://seleniumjavalocators.vercel.app/pages/waits.html");
		
		WebElement startVisibilityWaitButton = driver.findElement(By.id("startVisibilityWait"));
		startVisibilityWaitButton.click();
		
		WebElement delayedElementMessage = driver.findElement(By.id("delayedElement"));
		System.out.println("Estado del mensaje antes de la espera: " + delayedElementMessage.isDisplayed());
		
		try {
			Thread.sleep(5000);
			System.out.println("Esperando...");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Estado del mensaje después de la espera: " + delayedElementMessage.isDisplayed());
		//TearDown
		
		driver.quit();
		//------------
		
	}
	
	@Test
	public void isEnabledTest() {
		//Setup
		WebDriver driver = new ChromeDriver();
		//------------------
		
		//Steps 
		//------------------
		driver.manage().window().maximize();
		
		driver.get("https://seleniumjavalocators.vercel.app/pages/disabled-readonly.html");
		
		WebElement startEnableFlowButton = driver.findElement(By.id("startEnableFlow"));
		startEnableFlowButton.click();
		
		WebElement submitDisabledButton = driver.findElement(By.id("submitDisabled"));
				
		
		System.out.println("Estado del botón antes de la espera: " + submitDisabledButton.isEnabled());
		
		try {
			Thread.sleep(5000);
			System.out.println("Esperando...");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Estado del botón después de la espera: " + submitDisabledButton.isEnabled());
		//TearDown
		
		driver.quit();
		//------------
		
	}
	
	@Test
	public void isSelectedTest() {
		//Setup
		WebDriver driver = new ChromeDriver();
		//------------------
		
		//Steps 
		//------------------
		driver.manage().window().maximize();
		
		driver.get("https://seleniumjavalocators.vercel.app/pages/checkbox-radio-toggle.html");
		
		WebElement cbNewsCheckbox = driver.findElement(By.id("cb_news"));
		
		
		System.out.println("Estado del botón antes del click: " + cbNewsCheckbox.isSelected());
		
		try {
			Thread.sleep(1500);
			System.out.println("Esperando...");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("hacemos click");
		cbNewsCheckbox.click();
		
		try {
			Thread.sleep(1500);
			System.out.println("Esperando...");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Estado del botón después del click: " + cbNewsCheckbox.isSelected());
		//TearDown
		
		driver.quit();
		//------------
		
	}
	
	
}
