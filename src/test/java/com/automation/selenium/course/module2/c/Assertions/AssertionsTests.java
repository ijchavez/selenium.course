package com.automation.selenium.course.module2.c.Assertions;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AssertionsTests {
    private WebDriver driver;
    private static final String pageUrl = "https://seleniumjavalocators.vercel.app/pages/checkbox-radio-toggle.html";
    

	    @Test
	    public void assertEqNotEqTest() {
	        driver = new ChromeDriver();
	        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	        driver.manage().window().maximize();
	        driver.get(pageUrl);
	        
	        Assert.assertEquals(driver.getCurrentUrl(), pageUrl);
	        Assert.assertEquals(driver.getTitle(), "Checkbox + Radio + Toggle | QA Automation");
	        Assert.assertNotEquals(driver.getTitle(), "QA Automation Lab | Selenium Java Locators");
	        
            WebElement h1TitleElement = driver.findElement(By.id("controlTitle"));
            Assert.assertEquals(h1TitleElement.getText(), "Checkbox + Radio + Toggle", "El titulo no es correcto");
            
	        driver.quit();
	    }
	    
	    @Test
	    public void assertTrueTest() {
	        driver = new ChromeDriver();
	        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	        driver.manage().window().maximize();
	        driver.get(pageUrl);
	        
	        WebElement cbEventsCheckboxElement = driver.findElement(By.id("cb_events"));       
	        cbEventsCheckboxElement.click();
	        
	        Assert.assertTrue(cbEventsCheckboxElement.isSelected(), "Se esperaba true");
	        
	        driver.quit();
	    }
	    
	    @Test
	    public void assertFalseTest() {
	        driver = new ChromeDriver();
	        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	        driver.manage().window().maximize();
	        driver.get(pageUrl);
	        
	        WebElement cbEventsCheckboxElement = driver.findElement(By.id("cb_events"));       
	        cbEventsCheckboxElement.click();
	        
	        WebElement cbNewsCheckboxElement = driver.findElement(By.id("cb_news"));       
	        cbNewsCheckboxElement.click();
	        
	        WebElement cbUpdatesElement = driver.findElement(By.id("cb_updates"));
	        Assert.assertFalse(cbUpdatesElement.isSelected(), "Se esperaba false");
	        
	        driver.quit();
	    }
	    
		/**
		 * a.El usuario va a hacer click en los checkboxes:
		 * - newsletter
		 * - updates
		 * 
		 * b.El usuario va a elegir el plan Pro
		 * - Validar que el plan pro está seleccionado
		 * - Validar que los otros planes no estan seleccionados
		 * 
		 * c.El usuario hace click en el Toggle
		 *  - Validar que el texto debajo del Toggle sea: Estado toggle: ON 
		 * 
		 * d.El usuario al hacer click en el botón Revisar Seleccion
		 * - Validar que el texto sea: Checkboxes: news, updates | Radio plan: pro | Toggle: ON
		 *	
		 */
	    @Test
	    public void assertFullTest() {
	        driver = new ChromeDriver();
	        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	        driver.manage().window().maximize();
	        driver.get(pageUrl);
	        
	        
	        //a
	        WebElement cbNewsCheckboxElement = driver.findElement(By.id("cb_news"));       
	        cbNewsCheckboxElement.click();
	        
	        WebElement cbUpdatesElement = driver.findElement(By.id("cb_updates"));
	        cbUpdatesElement.click();
	        
	        //b
	        
	        WebElement proPlanRb = driver.findElement(By.id("plan_pro"));
	        proPlanRb.click();
	        
	        Assert.assertTrue(proPlanRb.isSelected(), "El radiobutton Plan Pro no está seleccionado");
	        
	        WebElement basicPlanRb = driver.findElement(By.id("plan_basic"));
	        Assert.assertFalse(basicPlanRb.isSelected(), "El radiobutton Basic Plan está seleccionado");
	        
	        WebElement plusPlanRb = driver.findElement(By.id("plan_plus"));
	        Assert.assertFalse(plusPlanRb.isSelected(), "El radiobutton Plus Pro está seleccionado");
	        
	        //c 
	        WebElement darkModeToggle = driver.findElement(By.cssSelector("input[data-testid='toggle-darkmode']"));
	        darkModeToggle.click();
	       
	        WebElement darkModeToggleMessage = driver.findElement(By.id("toggleStatus"));
	        String darkModeToggleMessageText = darkModeToggleMessage.getText();
	        
	        Assert.assertEquals(darkModeToggleMessageText, "Estado toggle: ON", "El mensaje no es correcto luego de clickear el toggle");
	        Assert.assertNotEquals(darkModeToggleMessageText, "Estado toggle: OFF");	        
	        
	        //d reviewSelection className 
	        
	        WebElement reviewSelectionButton = driver.findElement(By.id("reviewSelection"));
	        reviewSelectionButton.click();
	        
	        WebElement resultBoxMessage = driver.findElement(By.className("result-box"));
	        Assert.assertEquals(resultBoxMessage.getText(), "Checkboxes: news, updates | Radio plan: pro | Toggle: ON");
	        
	        driver.quit();
	    }

}
