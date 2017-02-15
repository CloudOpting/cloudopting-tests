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
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import eu.cloudopting.testing.common.CommonSteps;
import junit.framework.TestCase;

public class TestServiceManagement extends TestCase {
	private WebDriver driver;
	private String baseUrl;
	private StringBuffer verificationErrors = new StringBuffer();
	
	@Before
	public void setUp() throws Exception {
		System.setProperty("webdriver.gecko.driver", "src/test/resources/selenium_standalone_binaries/osx/marionette/64bit/geckodriver");

		driver = new FirefoxDriver();
		baseUrl = CommonSteps.BASE_URL;
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}
	
	@Test
	public void testServiceManagement() throws Exception{
		System.out.println("testServiceManagement");
		driver.get(baseUrl);
		CommonSteps.login(driver, baseUrl);
		
		fillForm();
		
		WebElement thanksElement = driver.findElement(By.xpath("//div[@class='thanks']//h5"));
		assertEquals("The service has not been published", "your service has been published", thanksElement.getText().toLowerCase());
		
		WebElement serviceRow = findServiceRow("TestServiceName");
		
	    assertNotNull("The service is not in the service list", serviceRow);
	    
	    WebElement delButton = serviceRow.findElements(By.tagName("button")).get(3);
	    System.out.println(delButton.getText());
	    
	    delButton.click();
	    Alert alert = driver.switchTo().alert();
		System.out.println("Alert: " + alert.getText());
		alert.accept(); //click ok to the confirmation
		System.out.println("Confirmation clicked");
		
		WebElement afterDelServiceRow = findServiceRow("TestServiceName");
	    
	    assertNull("After delete the service is still in the service list", afterDelServiceRow);
	}
	
	@Test
	public void testSearch() throws InterruptedException {
		CommonSteps.login(driver, baseUrl);
		System.out.println("testSearch");
		driver.get(baseUrl + "/list");
		WebElement searchField = driver.findElement(By.xpath("//input[@type='text']"));
		searchField.sendKeys("CKAN");
		WebElement searchButton = driver.findElement(By.cssSelector(".btn-search"));
		searchButton.click();
		
		WebElement serviceRow = findServiceRow("CKAN");
		
		assertNotNull("Service not found", serviceRow);
	}
	
	@Test
	public void testInstanceList() throws InterruptedException {
		CommonSteps.login(driver, baseUrl);
		System.out.println("testSearch");
		driver.get(baseUrl + "/list");
		
		WebElement serviceRow = findServiceRow("CKAN");
		
		WebElement listButton = serviceRow.findElements(By.tagName("button")).get(2);
	    System.out.println(listButton.getText());
	    
	    listButton.click();
	    Thread.sleep(5000);
	    WebElement table = driver.findElement(By.cssSelector(".table-hover"));
	    List<WebElement> rows = table.findElements(By.tagName("tr"));
	    System.out.println("Rows: " + rows.size());
	    for (WebElement row : rows) {
	    	Thread.sleep(5000);
	    	List<WebElement> cells = row.findElements(By.tagName("td"));
	    	if (cells.size() == 0) { //skip header
	    		continue;
	    	}
	    	 System.out.println("Cells: " + cells.size());
	    	assertEquals("Name of the service not found", "CKAN", cells.get(0).getText());
	    }
	}
	
	private void fillForm() throws InterruptedException {
		Thread.sleep(15000);
		System.out.println("testServiceManagement");
		driver.get(baseUrl + "/publish");
	    
	    driver.findElement(By.id("serviceName")).clear();
	    driver.findElement(By.id("serviceName")).sendKeys("TestServiceName");
	    driver.findElement(By.id("toscaName")).clear();
	    driver.findElement(By.id("toscaName")).sendKeys("ServiceName");
	    driver.findElement(By.id("servicePrice")).clear();
	    driver.findElement(By.id("servicePrice")).sendKeys("100");
	    driver.findElement(By.id("platformPrice")).clear();
	    driver.findElement(By.id("platformPrice")).sendKeys("100");
	    new Select(driver.findElement(By.id("sizeId"))).selectByVisibleText("Medium");
	    driver.findElement(By.cssSelector("option[value=\"3\"]")).click();
	    driver.findElement(By.id("termsAndConditions")).clear();
	    driver.findElement(By.id("termsAndConditions")).sendKeys("link");
	    driver.findElement(By.id("shortDescription")).clear();
	    driver.findElement(By.id("shortDescription")).sendKeys("description");
	    
	    driver.findElement(By.id("applicationSubscriberMail")).clear();
	    driver.findElement(By.id("applicationSubscriberMail")).sendKeys("template");
	    driver.findElement(By.id("applicationSpMail")).clear();
	    driver.findElement(By.id("applicationSpMail")).sendKeys("template");
	    
	    Thread.sleep(5000);
	    
	    WebElement saveButton = driver.findElement(By.xpath("//*[contains(text(), 'Save')]"));
	    System.out.println(saveButton.toString());
	    if (saveButton.isEnabled()) {
	    	System.out.println("click");
	    	saveButton.click();
	    }
	    Thread.sleep(5000);
	    //driver.findElement(By.xpath("(//button[@type='button'])[30]")).click();
	    WebElement nextButton = driver.findElement(By.xpath("//*[contains(text(), 'Next')]"));
	    System.out.println(nextButton.toString());
	    if (nextButton.isEnabled()) {
	    	System.out.println("click2");
	    	nextButton.click();
	    }
	    Thread.sleep(5000);
	    //driver.findElement(By.xpath("(//button[@type='button'])[32]")).click();
	    
	    WebElement skipButton = driver.findElement(By.xpath("//*[contains(text(), 'Skip')]"));
	    System.out.println(skipButton.toString());
	    if (skipButton.isEnabled()) {
	    	System.out.println("click3");
	    	skipButton.click();
	    }
	    Thread.sleep(5000);
	    WebElement finishButton = driver.findElement(By.xpath("//*[contains(text(), 'Finish')]"));
	    System.out.println(finishButton.toString());
	    if (finishButton.isEnabled()) {
	    	System.out.println("click4");
	    	finishButton.click();
	    }
	    Thread.sleep(5000);
	}
	
	private WebElement findServiceRow(String servicename) throws InterruptedException {
		driver.get(baseUrl + "list");
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
//				for(WebElement td : tdElements) {
//					System.out.println(td.getText());
//				}
//				tdElements = trElement.findElements(By.xpath("./td"));
				if (tdElements.get(0).getText().equals(servicename)) {
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
