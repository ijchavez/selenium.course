package com.automation.selenium.course.module4.login;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SuccessfulLoginTests extends BaseTest {
    
    /*
     * https://gitlab.com/ijchavez/seleniumjavalocators/-/blob/main/login_user_story.md
     * */
    	
    @Test(description = "Successful User Login Test", priority = 2)
    public void successfulUserLoginTest() {	         
        //username_id,  password, iniciarSesionBtn
        WebElement usernameInput = driver.findElement(By.id("username_id"));
        usernameInput.sendKeys("usuario.demo");
        
        WebElement passwordInput = driver.findElement(By.id("password"));
        passwordInput.sendKeys("Qa1234!");
        
        WebElement loginButton = driver.findElement(By.id("iniciarSesionBtn"));
        loginButton.click();
        
        //loginMessageEyebrow, loginMessageTitle,  loginMessageBody
        
        WebElement loginMessageEyebrow = driver.findElement(By.id("loginMessageEyebrow"));
        WebElement loginMessageTitle = driver.findElement(By.id("loginMessageTitle"));
        WebElement loginMessageBody = driver.findElement(By.id("loginMessageBody"));
        
        Assert.assertEquals(loginMessageEyebrow.getText(), "LOGIN CORRECTO", "El texto del eyebrow no es correcto");
        Assert.assertEquals(loginMessageTitle.getText(), "Inicio de sesion exitoso", "El texto del titulo no es correcto");        
        Assert.assertEquals(
        		loginMessageBody.getText(), 
        		"Bienvenido al laboratorio. El acceso fue concedido correctamente. Seras redirigido a una pagina mock.", 
        		"El texto del body no es correcto"
        		);   

    }	    
	 
}
