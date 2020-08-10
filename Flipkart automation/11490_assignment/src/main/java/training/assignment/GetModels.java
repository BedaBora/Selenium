package training.assignment;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetModels {

	public static void main(String[] args) throws InterruptedException, SQLException {
		
		//Model pojo store
		ArrayList<ModelClass> mobilesAvailable=new ArrayList<>();
		
		//Login Credentials  
		String loginID="7637856997";	      //Enter username
		String loginPassword="Password@123";	  //Enter password
		
		//Regex for selecting Asus models
		String mobileBrand="Asus";
		String regex="("+mobileBrand+"\\s.*?.*)\\s.*?(\\d*?\\.?\\d{1})(\\d.*)\\s.?Ratings\\s?\\&\\s(\\d.*)\\s.?Reviews";
		
		//Website to visit
		String url="https://www.flipkart.com/";
		String workPath=System.getProperty("user.dir");
		
		String category="Electronics";
		String expectedUrl="https://www.flipkart.com/mobiles/pr?sid=tyy%2C4io&p%5B%5D=facets.brand%255B%255D%3DAsus&otracker=nmenu_sub_Electronics_0_Asus";
		
		System.setProperty("webdriver.gecko.driver",workPath+"\\src\\main\\resources\\drivers\\gecko\\geckodriver.exe");
		
		//Run firefox in private window
		
//		DesiredCapabilities caps = new DesiredCapabilities();           
//        FirefoxOptions options = new FirefoxOptions();					
//        options.addArguments("-private");								
//        caps.setCapability("moz:firefoxOptions",options);
        
        WebDriver driver =new FirefoxDriver();
		driver.manage().window().maximize();                 //Open in full screen mode
		driver.get(url);
		driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		
		//Login to Flipkart
		driver.findElement(By.xpath("//input[@class='_2zrpKA _1dBPDZ']")).sendKeys(loginID);
		driver.findElement(By.xpath("//input[@class='_2zrpKA _3v41xv _1dBPDZ']")).sendKeys(loginPassword);
		
		driver.findElement(By.xpath("//button[@class='_2AkmmA _1LctnI _7UHT_c']")).click();
		
		//Close login button
//		WebDriverWait wait= new WebDriverWait(driver, 20);
//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='_3zdbog _3Ed3Ub']"))).click();
		

		Thread.sleep(1000);
		
		//Display Electronics dropdown
		Actions action= new Actions(driver);
		WebElement dropdown= driver.findElement(By.xpath("//span[contains(text(),'"+category+"')]"));
		action.moveToElement(dropdown).perform();
		System.out.println("Selected Menu");
		
		///Open ASUS
		Thread.sleep(1000);	
		driver.findElement(By.linkText(mobileBrand)).click();
		
		if(driver.getCurrentUrl().contentEquals(expectedUrl)) {
			System.out.println("Opened link successfully");
		}
		
		Thread.sleep(2000);

		
		//Find total number of pagination
		int noOfPages= driver.findElements(By.xpath("//a[@class='_2Xp0TH']")).size();
		WebElement nextPage=driver.findElement(By.xpath("//a[@class='_3fVaIS']//span[contains(text(),'Next')]"));
		
		Pattern checkRegex= Pattern.compile(regex);
		System.out.println((noOfPages+1)+" pages available.");
		
		for(int currPage=0; currPage <=noOfPages; currPage++) {
			driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
			
			//Get text content of page
			String pageTexts=driver.findElement(By.xpath("//div[@class='t-0M7P _2doH3V']")).getText();
//			System.out.println(pageTexts);

			//Regex for mobile detail
			Matcher regexMatcher= checkRegex.matcher(pageTexts);
			while(regexMatcher.find()) {
				ModelClass mobiles=new ModelClass();
				if(regexMatcher.group().length()!=0) {
					mobiles.setModelName(regexMatcher.group(1)); 
					mobiles.setRating(Float.parseFloat(regexMatcher.group(2)));	//Model Name
					mobiles.setRatingCount(Integer.parseInt(regexMatcher.group(3).replace(",","")));			//Total Ratings
					mobiles.setReviewsCount(Integer.parseInt(regexMatcher.group(4).replace(",","")));		//Total Reviews
//					for(int i=1; i<=regexMatcher.groupCount(); i++)
//						System.out.println(regexMatcher.group(i).trim());
//					System.out.println(regexMatcher.group(1));
//					System.out.println(regexMatcher.group(2));
//					System.out.println(regexMatcher.group(3).replace(",","").trim());
//					System.out.println(regexMatcher.group(4).replace(",","").trim());
					mobilesAvailable.add(mobiles);
				}
//				System.out.println("Object: "+i+mobiles.toString());
			}		
			
			Thread.sleep(1000);
			try {
				nextPage.click();
			}catch(Exception ex){
				break;
			}
		}
		System.out.println("-------------------------------------------------------------------------------------------------");

		System.out.println(mobilesAvailable.size()+" entries extracted.");
		
		//Logging out
		WebElement logOutElement=driver.findElement(By.cssSelector("div._3ybBIU div._1tz-RS div._3pNZKl:nth-child(2) div._1jA3uo:nth-child(3) > div.dHGf8H"));
		action.moveToElement(logOutElement).perform();
		driver.findElement(By.xpath("//div[@class='_3ybBIU']//li[10]//a[1]")).click();
		
		Thread.sleep(2000);
		driver.quit();
		
		
		//--------------------------------------------Sending data to database---------------------------------------//
		Connection dbCon= null;
		PreparedStatement pstmt= null;
		String userName="root";
		String password="root";
		
		String query="INSERT INTO mobiles VALUES(?,?,?,?)";
		
		int batchSize=50;
		int count=0;
		String databaseUrl="jdbc:mysql://localhost:3306/project";
		//Register to server
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			dbCon=DriverManager.getConnection(databaseUrl, userName, password);
			System.out.println("Connection established");
			dbCon.setAutoCommit(false);
			pstmt= dbCon.prepareStatement(query);
			
			for(ModelClass mobile_data: mobilesAvailable) {
				pstmt.setString(1, mobile_data.getModelName());
				pstmt.setFloat(2, mobile_data.getRating());
				pstmt.setInt(3, mobile_data.getReviewsCount());
				pstmt.setInt(4, mobile_data.getRatingCount());
				count++;
				pstmt.addBatch();
				
				if(count%batchSize==0) {
					pstmt.executeBatch();
				}
			}
			
			pstmt.executeBatch();
			dbCon.commit();
			System.out.println("Data sent to databse");
		}
		catch(SQLException ex) {
			ex.printStackTrace();
			try {
				dbCon.rollback();
			}catch(SQLException sqex){
				sqex.printStackTrace();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}finally {
			System.out.println("Closing connection...");
			dbCon.close();
			pstmt.close();
		}	
	}		
}
