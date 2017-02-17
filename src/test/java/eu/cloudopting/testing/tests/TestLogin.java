package eu.cloudopting.testing.tests;

import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import eu.cloudopting.testing.common.CommonSteps;
import junit.framework.TestCase;

public class TestLogin extends TestCase {
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
	
	/**
	 * Test the login process.
	 * Login with the admin credentials and check in the account page that the data is correct
	 * @throws Exception
	 */
	@Test
	public void testLogin() throws Exception {
		System.err.println("testLogin");
		CommonSteps.login(driver, baseUrl);
		
		driver.findElement(By.xpath("//a[contains(text(),'Account')]")).click();
		Thread.sleep(5000);
		
		assertEquals("The username does not match", "Admin", driver.findElement(By.xpath("//div[@id='tab_settings']/form/div/input")).getAttribute("value"));
		assertEquals("The name does not match", "Administrator", driver.findElement(By.xpath("//div[@id='tab_settings']/form/div[2]/input")).getAttribute("value"));
		assertEquals("The email does not match", "xavier.cases@atos.net", driver.findElement(By.xpath("//div[@id='tab_settings']/form/div[3]/input")).getAttribute("value"));
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