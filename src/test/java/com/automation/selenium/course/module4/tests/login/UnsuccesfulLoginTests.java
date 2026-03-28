package com.automation.selenium.course.module4.tests.login;

import org.testng.annotations.Test;

import com.automation.selenium.course.module4.tests.base.BaseTest;

public class UnsuccesfulLoginTests extends BaseTest {
    /*
     * https://gitlab.com/ijchavez/seleniumjavalocators/-/blob/main/login_user_story.md
     * */
   
    @Test(description = "Blocked User Login Test", priority = 1)
    public void blockedUserLoginTest() {	
    	loginPage = landingPage.doClickOnLoginLink();
    	
        loginPage.doFillUsernameInput("usuario.bloqueado");
        loginPage.doFillPasswordInput("Lock1234!");
        
        loginPage.doClickOnLoginButton();
        
        loginPage.checkBlockedLoginMessageEyebrow();
        loginPage.checkBlockedLoginMessageTitle();
        loginPage.checkBlockedLoginMessageBody();

    }
   
    @Test(description = "Invalid User Login Test",
    	  dependsOnMethods = "sampleLoginTest",	
    	  dataProvider = "invalidUserData", 
    	  dataProviderClass = com.automation.selenium.course.module3.dataprovider.LoginDataProvider.class)
    public void invalidUserLoginTest(String username, String password) {
    	loginPage = landingPage.doClickOnLoginLink();
    	
        loginPage.doFillUsernameInput(username);
        loginPage.doFillPasswordInput(password);
        
        loginPage.doClickOnLoginButton();
        
        loginPage.checkInvalidLoginMessageEyebrow();
        loginPage.checkInvalidLoginMessageTitle();
        loginPage.checkInvalidLoginMessageBody();
        
    }
    
	 @Test(description = "Suspended User Login Test", 
		   dataProvider = "suspendedUserData", 
		   dataProviderClass = com.automation.selenium.course.module3.dataprovider.LoginDataProvider.class)
	 public void suspendedUserLoginTest(String username, String password) {
	   	loginPage = landingPage.doClickOnLoginLink();
	    	
        loginPage.doFillUsernameInput(username);
        loginPage.doFillPasswordInput(password);
        
        loginPage.doClickOnLoginButton();
	     
        loginPage.checkSuspendedLoginMessageEyebrow();
        loginPage.checkSuspendedLoginMessageTitle();
        loginPage.checkSuspendedLoginMessageBody();
	 }
 
	 @Test
	 public void sampleLoginTest() {
		 System.out.println("Ejecución de Sample Login Test");
		 //Assert.assertTrue(false);
	 }
}
