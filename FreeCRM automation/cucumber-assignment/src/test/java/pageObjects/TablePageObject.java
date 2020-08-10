package pageObjects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TablePageObject {
	protected WebDriver table_driver;
	
	public TablePageObject(WebDriver table_driver) {
		this.table_driver= table_driver;
		PageFactory.initElements(table_driver, this);
	}
	
	@FindBy(xpath="//i[@class='right chevron icon']")
	@CacheLookup
	WebElement nxtBtn;
	
	@FindBy(xpath="/html[1]/body[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[2]/table[1]/tfoot[1]/tr[1]/th[2]/div[2]/a[2]")
	@CacheLookup
	WebElement firstPage;
	
	//Count the number of entries
	public int entriesCount() throws InterruptedException{
		int count=0;
		String regex="(Asus\\s.*?.*)";
		Pattern checkRegex= Pattern.compile(regex);
		int i=0;
		while(i<20) {
			Thread.sleep(2000);
			String texts= table_driver.findElement(By.xpath("//table[@class='ui celled sortable striped table custom-grid table-scroll']")).getText();		
			Matcher regexMatcher= checkRegex.matcher(texts);
			while(regexMatcher.find()) {
				count++;
			}
			i++;
			try {
				nxtBtn.click();
			}
			catch(NoSuchElementException ex){
				ex.printStackTrace();
			}
		}
		
		return count;
	}
	
	//Delete top 10 entries
	public void deleteTopEntries(int topN) {
		firstPage.click();
		int i=1;
		while(i<=topN) {
			table_driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[2]/table[1]/tbody[1]/tr["+i+"]/td[1]/div[1]/label[1]")).click();
			i++;
		}
		table_driver.findElement(By.xpath("//div[@name='action']")).click();
		Actions action=new Actions(table_driver);
		action.moveToElement(table_driver.findElement(By.xpath("//div[@class='visible menu transition']//div[@class='item']"))).click().build().perform();
		table_driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[2]/table[1]/tfoot[1]/tr[1]/th[2]/span[1]/div[2]")).click();
//		table_driver.findElement(By.xpath("/html[1]/body[1]/div[3]/div[1]/div[3]/button[1]")).click(); //Cancel button
		table_driver.findElement(By.xpath("/html[1]/body[1]/div[3]/div[1]/div[3]/button[2]")).click(); //Delete button
	}
	
	//Edit last 5 entries
	public void editLastEntries(int lastN) throws InterruptedException {
		int i=1;
		int currData=9;
		while(i<=lastN) {
			Thread.sleep(2000);
			table_driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[2]/table[1]/tbody[1]/tr["+currData+"]/td[4]/a[2]/button[1]")).click();
			i++;
			currData--;
			Thread.sleep(2000);
			WebElement name= table_driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[2]/form[1]/div[1]/div[1]/div[1]/div[1]/div[1]/input[1]"));
			name.clear();
			name.sendKeys("Iphone");
//			table_driver.findElement(By.xpath("//button[contains(text(),'Cancel')]")).click(); //Click on cancel button			
			table_driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[2]/div[1]/button[2]")).click(); //Save button
			table_driver.navigate().back();
			table_driver.navigate().back();
		}
	}
}
