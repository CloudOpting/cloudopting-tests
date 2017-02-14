package eu.cloudoping.testing.tests;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
		System.out.println("createUser");
		driver.get(baseUrl);
		CommonSteps.login(driver, baseUrl);
		
		Thread.sleep(15000);
		System.out.println("testServiceManagement");
		driver.get(baseUrl + "/publish");
	    
	    driver.findElement(By.id("serviceName")).clear();
	    driver.findElement(By.id("serviceName")).sendKeys("ServiceName");
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
	    
//	    WebElement descriptionDiv = driver.findElement(By.xpath("(//div[@class='ta-scroll-window ng-scope ta-text ta-editor form-control')[2]"));
//	    System.out.println(descriptionDiv.getText());
//	    descriptionDiv.click();
//	    descriptionDiv.sendKeys("Description");
	    
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
	    
	    WebElement thanksElement = driver.findElement(By.xpath("//div[@class='thanks']//h5"));
	    assert(thanksElement.getText().equals("Your service has been published"));
	    
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
