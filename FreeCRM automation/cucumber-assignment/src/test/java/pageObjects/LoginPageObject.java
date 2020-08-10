package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.PageFactory;

public class LoginPageObject {
	protected WebDriver login_driver;
	
	public LoginPageObject(WebDriver login_driver) {
		this.login_driver= login_driver;
		PageFactory.initElements(login_driver, this);
	}
	
	@FindBy(name= "email")
	@CacheLookup
	WebElement emailField;
	
	@FindBy(name= "password")
	@CacheLookup
	WebElement passwordField;
	
	@FindBy(xpath ="//*[@id=\"ui\"]/div/div/form/div/div[3]")
	@CacheLookup
	WebElement loginBtn;
	
	//Send login credentials
	public void setCredentials(String email, String password) {
		emailField.clear();
		emailField.sendKeys(email);
		
		passwordField.clear();
		passwordField.sendKeys(password);
	}
	
	//Click on login button
	public void clickLoginButton() {
		loginBtn.click();
	}
}

