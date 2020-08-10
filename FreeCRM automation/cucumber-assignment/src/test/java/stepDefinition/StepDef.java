package stepDefinition;

import org.testng.AssertJUnit;
import io.cucumber.java.After;
import io.cucumber.java.en.*;
import pageObjects.HomePageObject;
import pageObjects.LoginPageObject;
import pageObjects.TablePageObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;

import io.cucumber.datatable.DataTable;

public class StepDef {
	
	public WebDriver driver;
	public LoginPageObject loginPage;
	public HomePageObject homePage;
	public TablePageObject tablePage;
	
						//LOGIN PAGE OBJECT//
	//--------------------LOGIN TO FREECRM--------------------//
	@Given("User in the homepage")
	public void user_in_the_homepage() throws InterruptedException {
		String path= System.getProperty("user.dir");
		System.setProperty("webdriver.gecko.driver", path+"/src/test/resources/driver/geckodriver/geckodriver.exe");
		
		driver = new FirefoxDriver();
		
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.MINUTES);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.MINUTES);
		loginPage= new LoginPageObject(driver);
		String url="https://freecrm.co.in/";
		driver.manage().window().maximize();
		driver.navigate().to(url);
		Thread.sleep(3000);
		driver.findElement(By.xpath("//span[contains(text(),'Log In')]")).click();
		Thread.sleep(5000);
	}

	@And("The login page is displayed")
	public void the_login_page_is_displayed() {
		AssertJUnit.assertEquals(driver.getTitle(), "Cogmento CRM");
	}


	@When("User enters valid username and password")
	public void user_enters_valid(DataTable dataTable) throws InterruptedException {
		List<List<String>> credential= dataTable.asLists();
		String email= credential.get(0).get(0);
		String password= credential.get(0).get(1);
		
		loginPage.setCredentials(email, password);
		Thread.sleep(2000);
	}

	@And("Click on login button")
	public void click_on_login_button() {
		loginPage.clickLoginButton();
	}

	@Then("User is logged into the website")
	public void user_is_logged_into_the_website(){
		AssertJUnit.assertEquals(driver.findElement(By.xpath("//*[@id=\"top-header-menu\"]/div[1]")).isDisplayed(), true);
	}
	
								//HOME PAGE OBJECT//
	//---------------------------CHECK ON COMPANY TAB-----------------------------------//

	@When("Menu is displayed")
	public void menu_is_displayed() throws InterruptedException {
		homePage= new HomePageObject(driver);
		Thread.sleep(3000);
		homePage.CheckMenuVisibility();
	}

	@Then("Company tab is present")
	public void company_tab_is_present() throws InterruptedException {
		homePage.CheckCompanyTab();
		Thread.sleep(2000);
		homePage.OpenCompaniesTab();
	}

									//TABLE PAGE OBJECT//
	//-------------------------------CHECK NO OF RECORDS--------------------------------//
	@And("Count the number of entries in each page")
	public void check_entries() throws InterruptedException {
		tablePage= new TablePageObject(driver);
		int entries= tablePage.entriesCount();
		Assert.assertEquals(entries, 199);
		Thread.sleep(2000);
	}
	
	//------------------------------EDIT LAST 5 RECORDS----------------------------------//
	@And("Edit last {int} records")
	public void edit_records(int range) throws InterruptedException {
		tablePage.editLastEntries(range);
	}
	
	
	//------------------------------DELETE TOP 10 RECORDS--------------------------------//
	@And("Select top {int} rows")
	public void select_entries(int limit) throws InterruptedException {
		Thread.sleep(2000);
		tablePage.deleteTopEntries(limit);
	}
	
	//---------------------------------FILTER OPTION---------------------------------------//
	@Then("Check {string} filter option")
	public void check_filter(String filter) throws InterruptedException {
		homePage.filter(filter);
	}
	
	//--------------------------------------LOGOUT----------------------------------------//
	@After
	public void tearDown() throws InterruptedException {
		Thread.sleep(2000);
//		driver.findElement(By.xpath("//div[@class='ui basic button floating item dropdown']")).click();
		homePage.openDropDown();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//body//div[@class='ui fluid container']//div[@class='ui fluid container']//a[5]")).click();
		driver.quit();
	}
	
}
