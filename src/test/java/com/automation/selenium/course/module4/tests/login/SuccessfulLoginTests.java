package com.automation.selenium.course.module4.tests.login;

import org.testng.annotations.Test;

import com.automation.selenium.course.module4.base.BaseTest;
import com.automation.selenium.course.module4.pages.DashboardPage;
import com.automation.selenium.course.module4.pages.LandingPage;
import com.automation.selenium.course.module4.pages.LoginPage;

public class SuccessfulLoginTests extends BaseTest {
    
    /*
     * https://gitlab.com/ijchavez/seleniumjavalocators/-/blob/main/login_user_story.md
     * */
    private static final String USERNAME = 	"usuario.demo";
	
    @Test(description = "Successful User Login Test")
    public void successfulUserLoginTest() {
    	LandingPage landingPage = new LandingPage(driver);
    	LoginPage loginPage = landingPage.doClickOnLoginLink();
    	
        loginPage.doFillUsernameInput(USERNAME);
        loginPage.doFillPasswordInput("Qa1234!");
        
        DashboardPage dashboardPage = loginPage.doClickOnLoginButtonExpectSuccess();
        
        loginPage.checkSuccessfulLoginMessageEyebrow();
        loginPage.checkSuccessfulLoginMessageTitle();
        loginPage.checkSuccessfulLoginMessageEyebrow();
        
        dashboardPage.checkProfileUser(USERNAME);
        dashboardPage.checkLoginButtonVisibility();
        

    }	    
	 
}
