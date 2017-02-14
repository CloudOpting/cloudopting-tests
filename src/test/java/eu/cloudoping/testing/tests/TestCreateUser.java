package eu.cloudoping.testing.tests;

import static org.junit.Assert.fail;
import java.util.List;
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
import org.openqa.selenium.support.ui.Select;

import eu.cloudopting.testing.common.CommonSteps;

public class TestCreateUser {
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
	public void testCreateUser() throws Exception {
		driver.get(baseUrl);
		CommonSteps.login(driver, baseUrl);

		createUser();

		driver.get(baseUrl + "user_manager");
		assert (userHasBeenCreated());
	}



	private void createUser() throws InterruptedException {
		driver.findElement(By.linkText("Menu")).click();
		driver.findElement(By.linkText("User Manager")).click();
		Thread.sleep(5000);
		// FILL FORM
		driver.findElement(By.xpath("(//button[@type='button'])[2]")).click();
		driver.findElement(By.name("alias")).click();
		driver.findElement(By.name("alias")).clear();
		driver.findElement(By.name("alias")).sendKeys("test");
		driver.findElement(By.name("firstName")).clear();
		driver.findElement(By.name("firstName")).sendKeys("Test");
		driver.findElement(By.name("lastName")).clear();
		driver.findElement(By.name("lastName")).sendKeys("User");
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("test.user@profesia.it");
		driver.findElement(By.xpath("//input[@type='password']")).click();
		driver.findElement(By.xpath("//input[@type='password']")).clear();
		driver.findElement(By.xpath("//input[@type='password']")).sendKeys("test_user");
		driver.findElement(By.xpath("//body[@id='page-top']/div[2]/section/div/div/div/div/div[2]/form/div[6]/select"))
				.click();
		new Select(driver.findElement(
				By.xpath("//body[@id='page-top']/div[2]/section/div/div/div/div/div[2]/form/div[6]/select")))
						.selectByVisibleText("Italian");
		driver.findElement(By.cssSelector("option[value=\"1\"]")).click();
		driver.findElement(By.xpath("//input[@type='checkbox']")).click();
		driver.findElement(By.xpath("(//input[@type='checkbox'])[3]")).click();
		new Select(driver.findElement(
				By.xpath("//body[@id='page-top']/div[2]/section/div/div/div/div/div[2]/form/div[9]/select")))
						.selectByVisibleText("Profesia");
		driver.findElement(By.cssSelector("option[value=\"1\"]")).click();
		// SUBMIT
		driver.findElement(By.xpath("(//button[@type='submit'])[2]")).click();
		Thread.sleep(10000);
	}

	private boolean userHasBeenCreated() throws InterruptedException {
		driver.get(baseUrl + "user_manager");
		Thread.sleep(5000);
		String numberCounter = driver.findElement(By.xpath("//span[@class='pagination-number ng-binding']")).getText();
		System.out.println("numberCounter " + numberCounter);
		int numberOfPages = Integer.parseInt(numberCounter.split("/")[1]);
		System.out.println("Pages: " + numberOfPages);
		for (int i = 0; i < numberOfPages; i++) {
			System.out.println("Current page: " + i);
			List<WebElement> trElements = driver
					.findElements(By.xpath("//table[@class='table table-hover']//tbody//tr"));
			for (WebElement trElement : trElements) {
				List<WebElement> tdElements = trElement.findElements(By.xpath("./td"));
				if (tdElements.get(0).getText().equals("test") && tdElements.get(1).getText().equals("Test User")
						&& tdElements.get(2).getText().equals("test.user@profesia.it")
						&& tdElements.get(3).getText().equals("Profesia")) {
					System.out.println("User found at page number " + (i + 1));
					return true;
				}
			}
			driver.findElement(By.xpath("//div[@class='input-group text-center']//button[2]")).click();
		}
		System.out.println("User not found");
		return false;
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
