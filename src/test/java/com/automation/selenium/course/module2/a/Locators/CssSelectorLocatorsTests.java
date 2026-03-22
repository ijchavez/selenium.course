package com.automation.selenium.course.module2.a.Locators;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class CssSelectorLocatorsTests {
	/*******************************************************
	*  CSS Selector                                        *
	* "ElementoHTML[propiedad='Valor']"                    *
	* # -> id                                              * 
	* . -> clase                                           *
	* [class*='primary'] -> contiene                       *
	* Tagname  : Tagname del nodo.                         *
	* Attribute: Nombre del atributo del nodo.             *
	* Value    : Valor del atributo.                       *
	*******************************************************/
	
	@Test
	public void locatorsTest() {
		//Setup
		WebDriver driver = new ChromeDriver();
		//------------------
		
		//Steps 
		//------------------
		driver.manage().window().maximize();
		
		driver.get("https://seleniumjavalocators.vercel.app/pages/actions.html");
		
		WebElement dropZoneOne = driver.findElement(By.cssSelector("div[title='Zona de destino 1']"));
		System.out.println(dropZoneOne.getText());
		
		List<WebElement> dragElementList = driver.findElements(By.cssSelector(".drag-item"));
		for(final WebElement dragEl : dragElementList) {
			System.out.println(">>> " + dragEl.getText());
			
		}
		
		WebElement doubleClickButton = driver.findElement(By.cssSelector("#dclick"));
		System.out.println(doubleClickButton.getText());
		
		WebElement rightClickButton = driver.findElement(By.cssSelector("button[data-testid*='right-click-']"));
		System.out.println(rightClickButton.getText());
		
		//TearDown
		
		driver.quit();
		//------------
		
	}
}
