package eu.cloudoping.testing.tests;

import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import eu.cloudopting.testing.common.CommonSteps;
import junit.framework.TestCase;

public class TestLogin extends TestCase {
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		baseUrl = CommonSteps.BASE_URL;
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testLogin() throws Exception {
		CommonSteps.login(driver, baseUrl);

		assert (driver.findElement(By.xpath("//div[@id='tab_settings']/form/div/input")).getAttribute("value")
				.equals("Admin"));
		assert (driver.findElement(By.xpath("//div[@id='tab_settings']/form/div[2]/input")).getAttribute("value")
				.equals("Administrator"));
		assert (driver.findElement(By.xpath("//div[@id='tab_settings']/form/div[3]/input")).getAttribute("value")
				.equals("xavier.cases@atos.net"));
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

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
}