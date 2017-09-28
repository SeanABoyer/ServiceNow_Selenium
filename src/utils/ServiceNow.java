package utils;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByTagName;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ServiceNow {
	private WebDriver _driver;
	private WebDriverWait _wait;
	private boolean _formFocused = false;
	private Actions _action;

	public ServiceNow(WebDriver driver) {
		_driver = driver;
		_wait = new WebDriverWait(_driver, 10);
		_action = new Actions(_driver);
	}
	
	//================================================================================
	// Getters
	//================================================================================
	/**
	 * @category Get Field ID TODO
	 */
	private WebElement getFieldByID(String fieldID) {
		this.focusForm(true);
		WebElement field = _driver.findElement(By.id(fieldID));
		return field;
		
	}
	/**
	 * @category Get Field Label
	 */
	private WebElement getFieldByLabel(String fieldLabel) {
		this.focusForm(true);
		WebElement field = null;
		List<WebElement> allLabels = _driver.findElements(By.className("label-text"));
		for (int i = 0; i < allLabels.size(); i++) {
			if (allLabels.get(i).getText().trim().equalsIgnoreCase(fieldLabel)) {
				field = allLabels.get(i).findElement(By.xpath("../../.."));
				break;
			}
		}
		return field;
	}
	/**
	 * @category Get Field Value ID TODO
	 */
	private String getFieldValueByID(String fieldID) {
		WebElement field = getFieldByID(fieldID);
		return field.findElement(By.tagName("input")).getAttribute("value");

	}
	/**
	 * @category Get Field Value label
	 */
	public String getFieldValueByLabel(String fieldLabel) {
		this.focusForm(true);
		String value = null;
		WebElement field = getFieldByLabel(fieldLabel);
		switch(this.getFieldType(field)) {
		case "choice":
			value = _driver.findElement(By.id(field.getAttribute("id").split("element.")[1])).findElement(By.cssSelector("[selected='SELECTED']")).getText();
			break;
		default:
			value = field.findElement(By.tagName("input")).getAttribute("value");;
		}
		return value;
	}
	/**
	 * @category Get Tab ID TODO
	 */
	private WebElement getTabByID(String tabID) {
		return null;
		
	}
	/**
	 * @category Get Tab Label TODO
	 */
	public WebElement getTabByLabel(String tabLabel) {
		return null;
		
	}
	/**
	 * @category Get Button ID TODO
	 */
	private WebElement getButtonByID(String buttonID) {
		return null;
		
	}
	/**
	 * @category Get Button Label TODO
	 */
	private WebElement getButtonByLabel(String buttonLabel) {
		return null;
		
	}
	//================================================================================
	// Setters
	//================================================================================
	/**
	 * @category Set Element
	 */
	private void typeInElement(WebElement element, String content) {
		_action.moveToElement(element);
		switch (this.getFieldType(element)) {
		case "choice":
			List<WebElement> options = element.findElements(By.tagName("option"));
			for (int i = 0; i < options.size(); i++) {
				if (options.get(i).getText().trim().equalsIgnoreCase(content)) {
					element.click();
					options.get(i).click();
					break;
				}
			}
			break;
		default:
			_action.click();
			if(!this.getFieldValueByID(element.getAttribute("id")).equalsIgnoreCase("")) {
				//TODO - Find a faster way to do this.
				_action.sendKeys(Keys.END);
				for (int i = 0; i <this.getFieldValueByID(element.getAttribute("id")).length(); i ++) {
					_action.sendKeys(Keys.BACK_SPACE);
				}

			}
			_action.sendKeys(content);
			_action.sendKeys(Keys.TAB);
		_action.perform();
		}

	}
	
	/**
	 * @category Set Field ID TODO
	 * 
	 * TODO - refactor this method to not be redundant.
	 */
	private void populateFieldByID(WebElement field, String content) {
		this.focusForm(true);
		WebElement element = _wait.until(ExpectedConditions.presenceOfElementLocated(By.id(field.getAttribute("id"))));
		String fieldType = getFieldType(field);
		this.typeInElement(element, content);
		this.focusForm(false);
	}

	/**
	 * @category Set Field Label
	 */
	public void populateFieldByLabel(String fieldLabel, String content) {
		this.focusForm(true);
		WebElement field = getFieldByLabel(fieldLabel);
		this.typeInElement(field, content);
		this.focusForm(false);
	}
	//================================================================================
	// Clickers
	//================================================================================
	/**
	 * @category Click Tab Label
	 */
	public void clickTabByLabel(String tabLabel) {
		this.focusForm(true);
		List<WebElement> tabs = _driver.findElements(By.className("tabs2_tab"));
		for (int i = 0; i < tabs.size(); i++) {
			if (tabs.get(i).getText().endsWith(tabLabel)) {
				tabs.get(i).click();
				break;
			}
		}
	}
	/**
	 * @category Click Button ID
	 */
	private void clickButtonByID(String buttonID) {
		this.focusForm(true);
		WebElement button = _wait.until(ExpectedConditions.presenceOfElementLocated(By.id(buttonID)));
		button.click();
		try {
			Alert alert = _driver.switchTo().alert();
			alert.accept();
		} catch (Exception ex) {
			// WHo Cares.. we were just trying to get rid of the alert
		}
	}

	/**
	 * @category Click Button Label
	 */
	public void clickButtonByLabel(String buttonlabel) {
		this.focusForm(true);
		WebElement button = null;
		List<WebElement> buttonList = _wait
				.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("button")));
		for (int i = 0; i < buttonList.size(); i++) {
			if (buttonList.get(i).getText().trim().equalsIgnoreCase(buttonlabel)) {
				button = buttonList.get(i);
				break;
			}
		}
		this.clickButtonByID(button.getAttribute("id"));
	}
	/**
	 * @category Click Menu ID
	 */
	public void clickContextMenuItem(String contextMenuID, String contextMenuItemText) {
		_action.moveToElement(_driver.findElement(By.id(contextMenuID)));
		_action.contextClick(_driver.findElement(By.id(contextMenuID))).build().perform();
		List<WebElement> contextItemList = _driver.findElements(By.className("context_item"));
		for (int i = 0; i < contextItemList.size(); i++) {
			if (contextItemList.get(i).getText().trim().equalsIgnoreCase(contextMenuItemText)) {
				contextItemList.get(i).click();
				break;
			}
		}
		try {
			Alert alert = _driver.switchTo().alert();
			alert.accept();
		} catch (Exception ex) {
			// WHo Cares.. we were just trying to get rid of the alert
		}
	}
	//================================================================================
	// Other
	//================================================================================
	private String getFieldType(WebElement element) {
		this.focusForm(true);
		String type = element.findElement(By.id(element.getAttribute("id").replaceFirst("element", "label"))).getAttribute("type");
		return type;
	}
	
	private boolean mandatoryFieldsPopulated() {
		this.focusForm(true);
		List<WebElement> manadatoryFields = _driver.findElements(By.className("is-required"));
		if (manadatoryFields.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * Changes focus of the page so that Selenium knows to interact with the
	 * navigation area or The Iframe that the form lives inside.
	 * 
	 * @param bool
	 *            True = Focus the Form False = Focus the whole page
	 */
	private void focusForm(boolean bool) {
		if (_formFocused == bool) {
			return;
		} else {
			_formFocused = bool;
		}
		if (_formFocused) {
			WebDriverWait wait = new WebDriverWait(_driver, 10);
			WebElement form = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("gsft_main")));
			_driver.switchTo().frame(form);
		} else {
			_driver.switchTo().defaultContent();
		}
	}

	/*
	 * PUBLIC CLASSES
	 */




	/**
	 * @category TODO
	 */
	public void populateAllFields() {
		// Find all fields that are possible to type in and fill those fields with
		// generic data
		// Reference
		// Choice
		// String
		// ETC
	}


	

	/**
	 * Pauses code until all mandatory fields have been populated on the form
	 * 
	 * @param timeoutDurationSecs
	 *            - an int in seconds for how long you would like to wait before
	 *            timeout.
	 */
	public void waitUntilMandatoryReferenceFieldsPopulate(int timeoutDurationSecs) {
		// TODO - Get this working as intended
		// StartTIme = time.now
		while (!mandatoryFieldsPopulated()) {
			// if (StartTime + duration > CurrenTime)
			// Throw Error
		}
	}

	/**
	 * Searches for the Applicaiton Name, Clicks the module under the application.
	 * 
	 * @param applicationName
	 *            - The name of the Application
	 * @param moduleName
	 *            - The name of the module
	 */
	public void ChooseApplicationAndModule(String applicationName, String moduleName) {
		this.focusForm(false);
		WebElement filterNavigator = _wait.until(ExpectedConditions.elementToBeClickable(By.id("filter")));
		filterNavigator.clear();
		filterNavigator.sendKeys(applicationName);
		WebElement applicationList = _wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("concourse_application_tree")));
		List<WebElement> applications = applicationList
				.findElements(By.xpath("//li[@ng-repeat='application in applications']"));
		for (int i = 0; i < applications.size(); i++) {
			if (applications.get(i).findElement(By.tagName("a")).getText().trim().equalsIgnoreCase(applicationName)) {
				// Found Application
				WebElement application = applications.get(i);
				WebElement moduleList = application.findElement(By.tagName("ul"));
				List<WebElement> modules = moduleList.findElements(
						By.xpath("//li[@ng-repeat='appModule in ::application.modules track by appModule.id']"));
				for (int j = 0; j < modules.size(); j++) {
					if (modules.get(j).getText().trim().equalsIgnoreCase(moduleName)) {
						// Found Module
						modules.get(j).click();
						_wait.until(ExpectedConditions
								.refreshed(ExpectedConditions.presenceOfElementLocated(By.name("gsft_main"))));
						return;
					}
				}
				// throw new Exception("Module Not Found");
			}
		}
		// throw new Exception("Application Not Found");
	}

	/**
	 * If the Service Now environment is using Basic Auth, it this method will login
	 * for the user. Called in the BaseTest class.
	 * 
	 * @param userName
	 *            - The username as a string.
	 * @param Password
	 *            - The password as a string.
	 */
	public void login(String userName, String Password) {
		focusForm(true);
		WebElement usernameElement = _wait.until(ExpectedConditions.presenceOfElementLocated(By.id("user_name")));
		WebElement passwordElement = _wait.until(ExpectedConditions.presenceOfElementLocated(By.id("user_password")));
		usernameElement.sendKeys(userName);
		passwordElement.sendKeys(Password);
		passwordElement.sendKeys(Keys.ENTER);
		focusForm(false);
	}

	/**
	 * Types into the search bar in the top right and searches.
	 * 
	 * @param content
	 *            - The value to type into the search bar.
	 */
	public void searchFor(String content) {
		this.focusForm(false);
		WebElement mGTypeArea = _wait.until(ExpectedConditions.presenceOfElementLocated(By.name("sysparm_search")));
		if (!mGTypeArea.getClass().toString().contains("focus")) {
			WebElement magnifyingGlass = _wait
					.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[action='textsearch.do']")));
			magnifyingGlass.click();
		}
		if(!mGTypeArea.getAttribute("value").equalsIgnoreCase("")) {
			mGTypeArea.clear();
		}
		mGTypeArea.sendKeys(content);
		mGTypeArea.sendKeys(Keys.ENTER);
		//this.typeInElement(mGTypeArea, content);

	}


}
