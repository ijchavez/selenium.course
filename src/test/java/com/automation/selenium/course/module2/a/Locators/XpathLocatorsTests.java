package com.automation.selenium.course.module2.a.Locators;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class XpathLocatorsTests {
	/*******************************************************
	*  XPATH                                               *
	* "//ElementoHTML[@propiedad='Valor']"                 *
	* "//ElementoHTML[contains(text(),'Texto a buscar')]"  *
	* "//ElementHTML[contains(@atributo,'Parte del Valor')]*
	* "//ElementoHTML[text()='Texto a Buscar']             *
	* //       : Posicion sobre el nodo actual.            *
	* Tagname  : Tagname del nodo.                         *
	* @        : Atributo a elegir.                        *
	* Attribute: Nombre del atributo del nodo.             *
	* Value    : Valor del atributo.                       *
	* https://www.guru99.com/xpath-selenium.html           *
	*******************************************************/
	
	@Test
	public void locatorsTest() {
		//Setup
		WebDriver driver = new ChromeDriver();
		//------------------
		
		//Steps 
		//------------------
		driver.manage().window().maximize();
		
		driver.get("https://seleniumjavalocators.vercel.app/pages/locators-challenge.html");
		
		//ElementoHTML[@propiedad='Valor']
		WebElement noValidationsTitle = driver.findElement(By.xpath("//p[@data-testid='challenge-result']"));
		System.out.println(noValidationsTitle.getText());
		
		
		//ElementoHTML[contains(text(),'Texto a buscar')]
		WebElement nestedStructureZoneTitle = driver.findElement(By.xpath("//h2[contains(text(),'Zona')]"));
		System.out.println(nestedStructureZoneTitle.getText());
		
		//ElementHTML[contains(@atributo,'Parte del Valor')
		WebElement h1Title = driver.findElement(By.xpath("//h1[contains(@data-testid,'locators-')]"));
		System.out.println(h1Title.getText());
		
		//ElementoHTML[text()='Texto a Buscar'] 
		WebElement linkElement = driver.findElement(By.xpath("//a[text()='Link de prueba (no navega)']"));
		System.out.println(linkElement.getText());
		
		WebElement userIdHashtagCodeElement = driver.findElement(By.xpath("//li[@data-hint='id']//code"));
		System.out.println(userIdHashtagCodeElement.getText());
		
		//TearDown
		
		driver.quit();
		//------------
		
	}
}
