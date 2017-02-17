package eu.cloudopting.testing.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CommonSteps {
	
	public static String BASE_URL = "http://dev.cloudopting.org/";
	public static String FIREFOX_DRIVER_PROPERTY_NAME = "webdriver.gecko.driver";
	public static String FIREFOX_DRIVER_PATH = "src/test/resources/selenium_standalone_binaries/osx/marionette/64bit/geckodriver";
	public static String CHROME_DRIVER_PROPERTY_NAME = "webdriver.chrome.driver";
	public static String CHROME_DRIVER_PATH = "src/test/resources/selenium_standalone_binaries/osx/googlechrome/64bit/chromedriver";
	
	public static void login(WebDriver driver, String baseUrl) throws InterruptedException {
		System.out.println("Common.login");

		driver.get(baseUrl);
		Thread.sleep(5000);
		driver.findElement(By.xpath("//a[@ui-sref='login']")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("Administr@t0r");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(5000);
		

	}
	
	public static void logout(WebDriver driver, String baseUrl) {
		System.out.println("Common.logout");
		
		driver.get(baseUrl + "/");
	    driver.findElement(By.linkText("Logout")).click();
	}

}
