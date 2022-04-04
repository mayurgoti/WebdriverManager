package seleniumPractice;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BrokenLinkCheck {

	public static void main(String[] args) {
		
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		options.setHeadless(true);
		WebDriver driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(2000));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2000));
		
		driver.get("https://www.amazon.com");
		
		List<WebElement> links = driver.findElements(By.tagName("a"));
		System.out.println("No of link are: " + links.size());
		List<String> urlList = new ArrayList<String>();
		for(WebElement e: links) {
			String url = e.getAttribute("href");
			urlList.add(url);
		}
		long stTime = System.currentTimeMillis();
		urlList.parallelStream().forEach(e -> checkBrokenLink(e));
		long endTime = System.currentTimeMillis();
		System.out.println("Total time taken: " + (endTime-stTime));
		driver.quit();
	}
	
	public static void checkBrokenLink(String linkUrl) {
		try {
			URL url = new URL(linkUrl);
			HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
			httpUrlConnection.setConnectTimeout(5000);
			httpUrlConnection.connect();		
			if(httpUrlConnection.getResponseCode() >= 400) {
				System.out.println(linkUrl + "----->" + httpUrlConnection.getResponseMessage() + "is a broken link");
			}else {
				System.out.println(linkUrl + "---->" + httpUrlConnection.getResponseMessage());
			}		
		}catch(Exception e) {
			
		}
	}
	

}
