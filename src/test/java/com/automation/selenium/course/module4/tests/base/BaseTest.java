package com.automation.selenium.course.module4.tests.base;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.automation.selenium.course.module4.pages.DashboardPage;
import com.automation.selenium.course.module4.pages.LandingPage;
import com.automation.selenium.course.module4.pages.LoginPage;

public class BaseTest {
	protected WebDriver driver;
    private static final String pageUrl = "https://seleniumjavalocators.vercel.app";

    private long classStartTime;
    private int executedTests;
    protected LandingPage landingPage;
    protected LoginPage loginPage;
    protected DashboardPage dashboardPage;
    
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
        
        landingPage = new LandingPage(driver);
        
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
