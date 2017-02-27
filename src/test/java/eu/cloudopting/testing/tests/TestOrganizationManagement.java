package eu.cloudopting.testing.tests;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import eu.cloudopting.testing.common.CommonSteps;
import junit.framework.TestCase;

public class TestOrganizationManagement extends TestCase {
	private WebDriver driver;
	private String baseUrl;
	private StringBuffer verificationErrors = new StringBuffer();
	
	@Before
	public void setUp() throws Exception {
		System.setProperty(CommonSteps.FIREFOX_DRIVER_PROPERTY_NAME, CommonSteps.FIREFOX_DRIVER_PATH);
		
		driver = new FirefoxDriver();
		
		baseUrl = CommonSteps.BASE_URL;
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}
//	
//	@Test
//	public void testOrganizationManagement() throws InterruptedException {
//		CommonSteps.login(driver, baseUrl);
//		System.out.println("testCreateOrganization");
//		
//		createOrganization();
//		
//		WebElement organizationRow = findOrganizationRow("TestOrganization");
//		
//		assertNotNull("The organization is not in the list", organizationRow);
//		
//		System.out.println("Cells: " + organizationRow.findElements(By.tagName("td")).size());
//		
//		WebElement delButton = organizationRow.findElements(By.tagName("button")).get(1);
//	    System.out.println(delButton.getText());
//	    
//	    delButton.click();
//	    Thread.sleep(5000);
//	    Alert alert = driver.switchTo().alert();
//		System.out.println("Alert: " + alert.getText());
//		Thread.sleep(5000);
//		alert.accept(); //click ok to the confirmation
//		System.out.println("Confirmation clicked");
//		
//		WebElement afterDelOrganizationRow = findOrganizationRow("TestOrganization");
//	    
//	    assertNull("After delete the organization is still in the service list", afterDelOrganizationRow);
//	}
//	
	/**
	 * Test Organization search
	 * Input the name of an organization and check that the matching record is in the shown list
	 * @throws InterruptedException
	 */
	@Test
	public void testSearchOrganization() throws InterruptedException {
		CommonSteps.login(driver, baseUrl);
		System.out.println("testSearchOrganization");
		driver.get(baseUrl + "/org_manager");
		WebElement searchField = driver.findElement(By.xpath("//input[@type='text']"));
		searchField.sendKeys("Profesia");
		WebElement searchButton = driver.findElement(By.cssSelector(".btn-search"));
		searchButton.click();
		Thread.sleep(5000);
		WebElement table = driver.findElement(By.cssSelector(".table-hover"));
		
		boolean found = false;
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		for (WebElement row: rows) {
			System.out.println("Rows: " + rows.size());
			Thread.sleep(5000);
			List<WebElement> cells = row.findElements(By.tagName("td"));
			System.out.println("Cells: " + cells.size());
			if (cells.size() == 0) {
				continue;
			}
			if (cells.get(0).getText().equals("Profesia")) {
				found = true;
				break;
			}
		}
		
		assertTrue("Organization not found", found);
		
	}
	
	private WebElement findOrganizationRow(String organizationName) throws InterruptedException {
		driver.get(baseUrl + "/org_manager");
		Thread.sleep(5000);
		String numberCounter = driver.findElement(By.xpath("//span[@class='pagination-number ng-binding']")).getText();
		System.out.println("numberCounter " + numberCounter);
		int numberOfPages = Integer.parseInt(numberCounter.split("/")[1]);
		System.out.println("Pages: " + numberOfPages);
		WebElement serviceTR = null;
		for (int i = 0; i < numberOfPages; i++) {
			System.out.println("Current page: " + i);
			Thread.sleep(5000);
			List<WebElement> trElements = driver
					.findElements(By.xpath("//table[@class='table table-hover']//tbody//tr"));
			for (WebElement trElement : trElements) {
				List<WebElement> tdElements = trElement.findElements(By.xpath("./td"));

				if (tdElements.get(0).getText().equals(organizationName)) {
					System.out.println("FOUND");
					serviceTR = trElement;
					return serviceTR;
				}
			}
			WebElement button = driver.findElement(By.xpath("//div[@class='input-group text-center']//button[2]"));
			if (button.isEnabled()) {
				Thread.sleep(5000);
				button.click();
			}
		
		}
		return null;
	}
	
	private void createOrganization() throws InterruptedException {
		driver.get(baseUrl + "/org_detail_manager");
		
		driver.findElement(By.xpath("//input[@type='text']")).clear();
	    driver.findElement(By.xpath("//input[@type='text']")).sendKeys("TestOrganization");
	    driver.findElement(By.xpath("(//input[@type='text'])[2]")).clear();
	    driver.findElement(By.xpath("(//input[@type='text'])[2]")).sendKeys("TestOrganization");
	    driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
	    driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys("abc");
	    new Select(driver.findElement(By.xpath("//body[@id='page-top']/div[2]/section/div/div/div/div/div[2]/form/div[4]/select"))).selectByVisibleText("Service Provider");
	    driver.findElement(By.cssSelector("option[value=\"0\"]")).click();
	    driver.findElement(By.xpath("//body[@id='page-top']/div[2]/section/div/div/div/div/div[2]/form/div[5]/select")).click();
	    new Select(driver.findElement(By.xpath("//body[@id='page-top']/div[2]/section/div/div/div/div/div[2]/form/div[5]/select"))).selectByVisibleText("Pending");
	    driver.findElement(By.xpath("(//option[@value='0'])[2]")).click();
	    
		Thread.sleep(5000);

	    
	    driver.findElement(By.xpath("//button[@type='submit'][2]")).click();
	    
	    Thread.sleep(5000);
	}
	
	
	@After
	public void tearDown() throws Exception {
		CommonSteps.logout(driver, baseUrl);
		
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
}
