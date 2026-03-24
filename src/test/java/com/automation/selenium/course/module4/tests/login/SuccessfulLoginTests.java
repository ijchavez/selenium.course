package com.automation.selenium.course.module4.tests.login;

import org.testng.annotations.Test;

import com.automation.selenium.course.module4.tests.base.BaseTest;

public class SuccessfulLoginTests extends BaseTest {
    
    /*
     * https://gitlab.com/ijchavez/seleniumjavalocators/-/blob/main/login_user_story.md
     * */
    	
    @Test(description = "Successful User Login Test")
    public void successfulUserLoginTest() {	         
        loginPage.doFillUsernameInput("usuario.demo");
        loginPage.doFillPasswordInput("Qa1234!");
        
        loginPage.doClickOnLoginButton();
        
        loginPage.checkSuccessfulLoginMessageEyebrow();
        loginPage.checkSuccessfulLoginMessageTitle();
        loginPage.checkSuccessfulLoginMessageEyebrow();

    }	    
	 
}
