package com.automation.selenium.course.module3.login;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTests {
    private WebDriver driver;
    private static final String pageUrl = "https://seleniumjavalocators.vercel.app/pages/login.html";

    /*
     * https://gitlab.com/ijchavez/seleniumjavalocators/-/blob/main/login_user_story.md
     * */

    private long classStartTime;
    private int executedTests;
    
    @BeforeClass
    public void beforeClass() {
    	//Se ejecuta una sola vez, antes de correr los tests de esa clase en particular
    	classStartTime = System.currentTimeMillis();
    	executedTests = 0;
    	
    	System.out.println("Inicio de la clase LoginTest");
    	
    }
    
	@BeforeMethod
	public void setUp() {
    	System.out.println("Inicio de setUp");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        driver.get(pageUrl);
	}
    	
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
    
    @Test(description = "Blocked User Login Test", priority = 1)
    public void blockedUserLoginTest() {	        
        //username_id,  password, iniciarSesionBtn
        WebElement usernameInput = driver.findElement(By.id("username_id"));
        usernameInput.sendKeys("usuario.bloqueado");
        
        WebElement passwordInput = driver.findElement(By.id("password"));
        passwordInput.sendKeys("Lock1234!");
        
        WebElement loginButton = driver.findElement(By.id("iniciarSesionBtn"));
        loginButton.click();
        
        //loginMessageEyebrow, loginMessageTitle,  loginMessageBody
        
        WebElement loginMessageEyebrow = driver.findElement(By.id("loginMessageEyebrow"));
        WebElement loginMessageTitle = driver.findElement(By.id("loginMessageTitle"));
        WebElement loginMessageBody = driver.findElement(By.id("loginMessageBody"));
        
        Assert.assertEquals(loginMessageEyebrow.getText(), "ACCESO RESTRINGIDO", "El texto del eyebrow no es correcto");
        Assert.assertEquals(loginMessageTitle.getText(), "Usuario bloqueado", "El texto del titulo no es correcto");        
        Assert.assertEquals(
        		loginMessageBody.getText(), 
        		"La cuenta esta bloqueada temporalmente. Debes solicitar un desbloqueo antes de continuar.", 
        		"El texto del body no es correcto"
        		);   

    }

   
    @Test(description = "Invalid User Login Test",
    	  dependsOnMethods = "sampleLoginTest",	
    	  dataProvider = "invalidUserData", 
    	  dataProviderClass = com.automation.selenium.course.module3.dataprovider.LoginDataProvider.class)
    public void invalidUserLoginTest(String username, String password) {	        
        //username_id,  password, iniciarSesionBtn
        WebElement usernameInput = driver.findElement(By.id("username_id"));
        usernameInput.sendKeys(username);
        
        WebElement passwordInput = driver.findElement(By.id("password"));
        passwordInput.sendKeys(password);
        
        WebElement loginButton = driver.findElement(By.id("iniciarSesionBtn"));
        loginButton.click();
        
        //loginMessageEyebrow, loginMessageTitle,  loginMessageBody
        
        WebElement loginMessageEyebrow = driver.findElement(By.id("loginMessageEyebrow"));
        WebElement loginMessageTitle = driver.findElement(By.id("loginMessageTitle"));
        WebElement loginMessageBody = driver.findElement(By.id("loginMessageBody"));
        
        Assert.assertEquals(loginMessageEyebrow.getText(), "CREDENCIALES INVALIDAS", "El texto del eyebrow no es correcto");
        Assert.assertEquals(loginMessageTitle.getText(), "Usuario o contrasena incorrecto", "El texto del titulo no es correcto");        
        Assert.assertEquals(
        		loginMessageBody.getText(), 
        		"La combinacion ingresada no es valida. Revisa la seccion de credenciales de prueba e intenta nuevamente.", 
        		"El texto del body no es correcto"
        		);   

    }

    
    /*
     * 
		usuario					| password |
		usuario.inhabilitado    | Off1234! |
		usuario.inhabilitado.01 | Off1234! |
		usuario.inhabilitado.02 | Off1234! |
		usuario.inhabilitado.03 | Off1234! |
     * */
  
    
	 @Test(description = "Suspended User Login Test", 
		   dataProvider = "suspendedUserData", 
		   dataProviderClass = com.automation.selenium.course.module3.dataprovider.LoginDataProvider.class)
	 public void suspendedUserLoginTest(String username, String password) {	        
	     //username_id,  password, iniciarSesionBtn
	     WebElement usernameInput = driver.findElement(By.id("username_id"));
	     usernameInput.sendKeys(username);
	     
	     WebElement passwordInput = driver.findElement(By.id("password"));
	     passwordInput.sendKeys(password);
	     
	     WebElement loginButton = driver.findElement(By.id("iniciarSesionBtn"));
	     loginButton.click();
	     
	     //loginMessageEyebrow, loginMessageTitle,  loginMessageBody
	     
	     WebElement loginMessageEyebrow = driver.findElement(By.id("loginMessageEyebrow"));
	     WebElement loginMessageTitle = driver.findElement(By.id("loginMessageTitle"));
	     WebElement loginMessageBody = driver.findElement(By.id("loginMessageBody"));
	     
	     Assert.assertEquals(loginMessageEyebrow.getText(), "CUENTA INHABILITADA", "El texto del eyebrow no es correcto");
	     Assert.assertEquals(loginMessageTitle.getText(), "Usuario inhabilitado", "El texto del titulo no es correcto");        
	     Assert.assertEquals(
	     		loginMessageBody.getText(), 
	     		"La cuenta no tiene acceso habilitado. Necesitas reactivacion para iniciar sesion.", 
	     		"El texto del body no es correcto"
	     		);   
	
	 }
 
	 @Test
	 public void sampleLoginTest() {
		 System.out.println("Ejecución de Sample Login Test");
		 //Assert.assertTrue(false);
	 }
	     @AfterMethod
    public void tearDown() {
    	System.out.println("Fin de setUp");
    	executedTests++;
    	
        driver.quit();
    }
    
    @AfterClass
    public void afterClass() {
    	long totalTime = System.currentTimeMillis() - classStartTime;
    	
    	//Se ejecuta una sola vez, después de correr los tests de esa clase en particular
    	System.out.println("Fin  de la clase LoginTest");
    	System.out.println("Tests ejecutados: ".concat(String.valueOf(executedTests)));
    	System.out.println("Tiempo total: ".concat(String.valueOf(totalTime).concat("ms")));
    	
    }

}
