package getBackEndData;
import java.sql.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class FreeCRM {

	public static void main(String[] args) throws InterruptedException, SQLException {
		
		String path= System.getProperty("user.dir");
		System.setProperty("webdriver.gecko.driver", path+"/drivers/geckodriver/geckodriver.exe");
		
		WebDriver driver= new FirefoxDriver();
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.MINUTES);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.MINUTES);
		String url="https://freecrm.co.in/";
		driver.manage().window().maximize();
		driver.get(url);
		Thread.sleep(3000);
		
	    WebDriverWait wait = new WebDriverWait(driver, 15);
	    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Log In')]")));
	    
		driver.findElement(By.xpath("//span[contains(text(),'Log In')]")).click();
		Thread.sleep(5000);
		
		//----------------------------USER LOGIN---------------------------//
		WebElement emailField= driver.findElement(By.name("email"));
		WebElement password= driver.findElement(By.name("password"));
		WebElement loginBtn= driver.findElement(By.xpath("//*[@id=\"ui\"]/div/div/form/div/div[3]"));
		
		emailField.sendKeys("numinus1@gmail.com");
		password.sendKeys("HighRadius");
		loginBtn.click();
		
		//--------------------NAVIGATE TO COMPANIES----------------------//
		Thread.sleep(3000);
		driver.findElement(By.xpath("//span[contains(text(),'Companies')]")).click();
		Thread.sleep(2000);
		
		JavascriptExecutor js= (JavascriptExecutor)driver;
		
		WebElement newBtn= driver.findElement(By.xpath("//button[contains(text(),'New')]"));	
		//------------------------CLICK NEW BUTTON------------------------//
//		js.executeScript("arguments[0].click()", newBtn);
		Thread.sleep(2000);

//		WebElement name= driver.findElement(By.xpath("//div[@class='ui right corner labeled input']//input[@name='name']"));
//		WebElement description= driver.findElement(By.xpath("//textarea[@name='description']"));
		int count=0;
		int pages=0;
		
		
		
		//--------------------------------Connecting to database----------------------------------//
		Connection dbCon=null;
		Statement stmt= null;
		String database="jdbc:mysql://localhost:3306/project";
		String userName="root";
		String pass="root";
		String query="SELECT * FROM mobiles;";
		
		//-------------------------------------Getting data from database----------------------------------//
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			dbCon= DriverManager.getConnection(database, userName, pass);
			
			stmt=dbCon.createStatement();
			ResultSet rs=stmt.executeQuery(query);
					
			while(rs.next()) {
							
				//----------------------------------INSERT DATA INTO THE TABLE----------------------------//
				driver.findElement(By.xpath("//div[@class='ui right corner labeled input']//input[@name='name']")).sendKeys(rs.getString(1));
				String desc="rating: "+rs.getFloat(2)+", total reviews: "+rs.getInt(3)+", total ratings: "+rs.getInt(4);
				driver.findElement(By.xpath("//textarea[@name='description']")).sendKeys(desc);
				
				System.out.println(rs.getString(1));
				System.out.println(desc);
				Thread.sleep(2000);
				
				driver.findElement(By.xpath("//button[@class='ui linkedin button']")).click(); //Save
				Thread.sleep(3000);
				
				driver.findElement(By.xpath("//span[contains(text(),'Companies')]")).click();	//Go to companies
				
				
				Thread.sleep(1000);
				js.executeScript("arguments[0].click()", driver.findElement(By.xpath("//button[contains(text(),'New')]")));
				Thread.sleep(1000);
				count++;
				if(count%10==0) {
					pages++;
				}
			}
		}catch(SQLException sqexp) {
			sqexp.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			dbCon.close();
			stmt.close();
		}
		
		//------------------------------------------COUNT NUMBER OF RECORDS-------------------------------------------------//
		int records=0;
		
		String regex="(Asus\\s.*?.*)";
		Pattern checkRegex= Pattern.compile(regex);
		
		Actions ation= new Actions(driver);
		WebElement nxtBtn= driver.findElement(By.xpath("//i[@class='right chevron icon']"));
		int i=0;
		while(i<20) {
			Thread.sleep(2000);
			String texts= driver.findElement(By.xpath("//table[@class='ui celled sortable striped table custom-grid table-scroll']")).getText();		
			Matcher regexMatcher= checkRegex.matcher(texts);
			while(regexMatcher.find()) {
				records++;
			}
			i++;
			try {
				nxtBtn.click();
			}
			catch(NoSuchElementException ex){
				ex.printStackTrace();
			}
		}
		
		System.out.println(i);
		System.out.println(records);
		
		Thread.sleep(2000);	
		//-----------------------------------------LOG OUT---------------------------------//
		driver.findElement(By.xpath("//div[@class='ui basic button floating item dropdown']")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//body//div[@class='ui fluid container']//div[@class='ui fluid container']//a[5]")).click();
		driver.quit();
		
		
	}
}
