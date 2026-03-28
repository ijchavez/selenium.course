package com.automation.selenium.course.module4.tests.login;

import org.testng.annotations.Test;

import com.automation.selenium.course.module4.tests.base.BaseTest;

public class SuccessfulLoginTests extends BaseTest {
    
    /*
     * https://gitlab.com/ijchavez/seleniumjavalocators/-/blob/main/login_user_story.md
     * */
    private static final String USERNAME = 	"usuario.demo";
	
    @Test(description = "Successful User Login Test")
    public void successfulUserLoginTest() {
    	loginPage = landingPage.doClickOnLoginLink();
    	
        loginPage.doFillUsernameInput(USERNAME);
        loginPage.doFillPasswordInput("Qa1234!");
        
        dashboardPage = loginPage.doClickOnLoginButtonExpectSuccess();
        
        loginPage.checkSuccessfulLoginMessageEyebrow();
        loginPage.checkSuccessfulLoginMessageTitle();
        loginPage.checkSuccessfulLoginMessageEyebrow();
        
        dashboardPage.checkProfileUser(USERNAME);
        dashboardPage.checkLoginButtonVisibility();
        

    }	    
	 
}
