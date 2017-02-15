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

public class TestUserManagement extends TestCase{
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
	public void testUserManagement() throws Exception {
		System.out.println("createUser");
		driver.get(baseUrl);
		CommonSteps.login(driver, baseUrl);

		createUser();

		driver.get(baseUrl + "user_manager");
		boolean created = userExists();

		assertTrue("The user is not created", created);		
		
		deleteUser();
		Thread.sleep(15000);
		boolean found = userExists();
		
		assertFalse("The user is still present", found);
		
	}
	
	@Test
	public void testSearchUser() throws InterruptedException {
		driver.get(baseUrl);
		CommonSteps.login(driver, baseUrl);
		
		driver.get(baseUrl + "user_manager");
		
		WebElement searchField = driver.findElement(By.xpath("//input[@type='text']"));
		searchField.sendKeys("profesia");
		
		WebElement searchButton = driver.findElement(By.cssSelector(".btn-search"));
		
		searchButton.click();
		
		WebElement userRow = findUserRow("profesia", "Profesia Profesia", "pro@profesia.it", "Profesia");
		
		assertNotNull("user not found", userRow);
	}
	
	private void deleteUser() throws InterruptedException {
		System.out.println("deleteUser");
		driver.get(baseUrl + "user_manager");
		Thread.sleep(5000);
		
		WebElement tr = findUserRow("test", "Test User", "test.user@profesia.it", "Profesia");

		if (tr == null) {
			fail("User not found!");
		}
		System.out.println("User found");
		List<WebElement> findElements = tr.findElements(By.tagName("td"));
		for (WebElement el: findElements) {
			System.out.println(el.getText());
		}
		//WebElement delButton = tr.findElements(By.xpath("//button[@class='btn btn-icon actionsTooltip ng-binding']")).get(1);
		WebElement delButton = tr.findElements(By.tagName("button")).get(1);
		System.out.println("Del button found: " + delButton.getText());
		delButton.click();
		System.out.println("Del button clicked");
		Thread.sleep(15000);
		Alert alert = driver.switchTo().alert();
		System.out.println("Alert: " + alert.getText());
		alert.accept(); //click ok to the confirmation
		System.out.println("Confirmation clicked");
	}
		
	

	private WebElement findUserRow(String alias, String name, String email, String organization ) throws InterruptedException {
		driver.get(baseUrl + "user_manager");
		Thread.sleep(5000);
		String numberCounter = driver.findElement(By.xpath("//span[@class='pagination-number ng-binding']")).getText();
		System.out.println("numberCounter " + numberCounter);
		int numberOfPages = Integer.parseInt(numberCounter.split("/")[1]);
		System.out.println("Pages: " + numberOfPages);
		WebElement userTR = null;
		for (int i = 0; i < numberOfPages; i++) {
			System.out.println("Current page: " + i);
			List<WebElement> trElements = driver
					.findElements(By.xpath("//table[@class='table table-hover']//tbody//tr"));
			for (WebElement trElement : trElements) {
				List<WebElement> tdElements = trElement.findElements(By.xpath("./td"));
				if (tdElements.get(0).getText().equals(alias) 
						&& tdElements.get(1).getText().equals(name)
						&& tdElements.get(2).getText().equals(email)
						&& tdElements.get(3).getText().equals(organization)) {
					userTR = trElement;
				}
			}
			WebElement button = driver.findElement(By.xpath("//div[@class='input-group text-center']//button[2]"));
			if (button.isEnabled()) {
				button.click();
			}
		
		}
		return userTR;
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

	private boolean userExists() throws InterruptedException {
		WebElement tr = findUserRow("test", "Test User", "test.user@profesia.it", "Profesia");
		System.out.println("<tr>: " + tr);
		return (tr != null);
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
