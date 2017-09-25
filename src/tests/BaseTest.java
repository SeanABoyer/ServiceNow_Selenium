package tests;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import utils.ServiceNow;

@RunWith(Parameterized.class)
public class BaseTest {
	//TODO: Look into being able to run with multiple browsers
	protected static WebDriver _driver;
	private String _environment;
	protected ServiceNow serviceNow = new ServiceNow(_driver);
	
	public BaseTest(String environment) {
		this._environment = environment;
		
	}
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		if(utils.Settings.getBrowser() == "Chrome") {
			System.setProperty("webdriver.chrome.driver", "src/chromedriver.exe");
			_driver = new ChromeDriver();
		}
		else if(utils.Settings.getBrowser().equals("Firefox")) {
			System.setProperty("webdriver.gecko.driver", "src/geckodriver.exe");
			_driver = new FirefoxDriver();
		}
		_driver.manage().window().maximize();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		_driver.quit();
	}

	@Before
	public void setUp() {
		_driver.get("https://"+_environment+".service-now.com/");
		if (utils.Settings.getLogonType() == "BASIC_AUTH") {
			serviceNow.login(utils.Settings.getUserName(),utils.Settings.getPassWord());
		}
		
	}

	@After
	public void tearDown() throws Exception {
		if (utils.Settings.getLogonType().equals("BASIC_AUTH")) {
			//Do Logout Stuff
		}
	}
	
	@Parameters(name = "{index}: {0}")
	public static Object[] data(){
		ArrayList<String> enviromentNames = utils.Settings.getEnviromentNames();
		Object[] enviromentList = new Object[enviromentNames.size()];
		for (int i = 0; i < enviromentNames.size(); i++) {
			enviromentList[i] = enviromentNames.get(i);
		}
		return enviromentList;
	}

}
