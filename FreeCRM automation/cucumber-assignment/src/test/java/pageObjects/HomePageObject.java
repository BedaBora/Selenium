package pageObjects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class HomePageObject {

	protected WebDriver homepage_driver;
	
	public HomePageObject(WebDriver homepage_driver) {
		this.homepage_driver= homepage_driver;
		PageFactory.initElements(homepage_driver, this);
	}
	
	@FindBy(xpath= "//div[@id='main-nav']")
	@CacheLookup
	WebElement menu;
	
	@FindBy(xpath= "//div[@id='main-nav']//a[4]")
	@CacheLookup
	WebElement tab;
	
	@FindBy(xpath="//div[@class='ui basic button floating item dropdown']")
	@CacheLookup
	WebElement dropDown;
	
	@FindBy(xpath="//button[contains(text(),'Show Filters')]")
	@CacheLookup
	WebElement filterBtn;
	
	
	//Check if menu is available
	public void CheckMenuVisibility() {
		Assert.assertEquals(menu.isDisplayed(), true);
	}
	
	//Check if  Company tab is visible
	public void CheckCompanyTab() {
		Assert.assertEquals(tab.isDisplayed(), true);
	}
	
	//Open company tab
	public void OpenCompaniesTab() {
		tab.click();
	}
	
	//Access the dropdown menu for settings
	public void openDropDown() {
		int count=0;
		dropDown.click();
		String options= dropDown.getText();
		System.out.println(options);
		String regex= "(\\S.*)";
		Pattern pat=Pattern.compile(regex);
		Matcher regexMatch= pat.matcher(options);
		while(regexMatch.find()) {
			count++;
		}
		System.out.println(count);
		Assert.assertEquals(count, 5);
	}
	
	//Filter data
	public void filter(String search) throws InterruptedException {
		filterBtn.click();
		
		//Select name to filter
		homepage_driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[2]/form[1]/div[2]/div[1]/div[1]/div[1]/div[1]/i[1]")).click();
		homepage_driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[2]/form[1]/div[2]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]")).click();
		
		//Choose contains
		homepage_driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[2]/form[1]/div[2]/div[1]/div[1]/div[2]/div[1]/i[1]")).click();
		homepage_driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[2]/form[1]/div[2]/div[1]/div[1]/div[2]/div[1]/div[2]/div[4]")).click();
		
		//Enter value MAX
		
		homepage_driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[2]/form[1]/div[2]/div[1]/div[1]/div[3]/div[1]/input[1]")).sendKeys(search);
		homepage_driver.findElement(By.xpath("//body//button[5]")).click();
		Thread.sleep(4000);
		
	}
}
