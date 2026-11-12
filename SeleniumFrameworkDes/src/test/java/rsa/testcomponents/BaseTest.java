package rsa.testcomponents;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import io.github.bonigarcia.wdm.WebDriverManager;
import rsa.pageobjects.LandingPage;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

public class BaseTest {
	public WebDriver driver;
	public LandingPage landingPage;
	
	public WebDriver initializeDriver() throws IOException {
		Properties prop = new Properties();
		FileInputStream fis=new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\java\\rsa\\resources\\GlobalData.properties");
		prop.load(fis);	
		String browserName=prop.getProperty("browser");
		if (browserName.equalsIgnoreCase("Chrome")) {
//			WebDriverManager.chromedriver().setup();
			driver= new ChromeDriver();
		}else if (browserName.equalsIgnoreCase("Firefox")){
			//firefox
		}else if (browserName.equalsIgnoreCase("edge")){
			System.setProperty("webdriver.edge.driver", browserName);
//			driver=new EdgeDriver();
		}
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		driver.manage().window().maximize();
		return driver;
	}
	    public List<HashMap<String, String>> getJsonDataToMap(String filePath) throws IOException {
			String jsonContent=FileUtils.readFileToString(new File(filePath),
					StandardCharsets.UTF_8);
			ObjectMapper mapper=new ObjectMapper();
			List<HashMap<String,String>> data=mapper.readValue(jsonContent,new TypeReference<List<HashMap<String,String>>>(){});
			// Fill in ${TEST_EMAIL} / ${TEST_PASSWORD} from the environment
			data.forEach(row -> row.replaceAll((key, value) -> TestCredentials.resolve(value)));
			return data;
		}
	    
	    public String getScreenshot(String testCaseName, WebDriver driver) throws IOException {
			File src=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			File file=new File(System.getProperty("user.dir")+"//reports//"+testCaseName+".png");
			FileUtils.copyFile(src, file);
			return (System.getProperty("user.dir")+"//reports//"+testCaseName+ ".png");
		}

	@BeforeMethod(alwaysRun=true)
	public LandingPage launchApplication() throws IOException {
		driver=initializeDriver();
	    landingPage=new LandingPage(driver);
		landingPage.goTo();
		return landingPage;
	}
	@AfterMethod(alwaysRun=true)
	public void tearDown() {
		// quit() closes the browser and stops chromedriver; close() only closes the window
 		driver.quit();
	}
}
