package utils;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
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
	/*
	 * PRIVATE CLASSES
	 */
	
	//TODO - Create a method to clear out the field completly before typing into it.
	
	private String getFieldType(WebElement element) {
		//TODO:: Fix so that it works with only the given element, not the parents
		this.focusForm(true);
		String type;
		for (int i = 0;i < 10; i++) {
			WebElement parentElement = element.findElement(By.xpath(".."));
			if(parentElement.getAttribute("class").contains("form-group")) {
				type = parentElement.findElement(By.cssSelector("[data-type='label']")).getAttribute("type");
				return type;
			}
			element = parentElement;
		}
		return "error";
	}	
	/**
	 * Finds all fields marked mandatory and checks if they have populated.
	 * 
	 * @return
	 * 		True = All mandatory fields have data in them.
	 * 		False = A mandatory field still needs to be filled.
	 */
	private boolean mandatoryFieldsPopulated() {
		this.focusForm(true);
		List<WebElement> manadatoryFields = _driver.findElements(By.className("is-required"));
		if (manadatoryFields.isEmpty()) {
			return true;
		}
		return false;
	}	
	/**
	 * Changes focus of the page so that Selenium knows to interact with the navigation area or The Iframe that the form lives inside.
	 * 
	 * @param bool
	 * 		True = Focus the Form
	 * 		False = Focus the whole page
	 */
	private void focusForm(boolean bool) {
		if (_formFocused == bool) {
			return;
		}
		else {
			_formFocused = bool;
		}
		if (_formFocused) {
			WebDriverWait wait = new WebDriverWait(_driver, 10);
			WebElement form = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("gsft_main")));
			_driver.switchTo().frame(form);
		}
		else {
			_driver.switchTo().defaultContent();
		}
	}
	/**
	 * Finds the field with the given label and gets the element.
	 * 
	 * @param fieldLabel - the label of the field.
	 * @return - A web element with the given field label
	 */
	private WebElement getFieldByLabel(String fieldLabel) {
		this.focusForm(true);
		WebElement label = null;
		List<WebElement> allLabels = _driver.findElements(By.className("label-text"));
		for (int i = 0; i < allLabels.size(); i++) {
			if(allLabels.get(i).getText().trim().equalsIgnoreCase(fieldLabel)){
				label = allLabels.get(i).findElement(By.xpath("../../.."));
				break;
			}
		}
		return label;
	}
	
	/*
	 * PUBLIC CLASSES
	 */
	public void clickTab(String tabName) {
		this.focusForm(true);
		List<WebElement> tabs = _driver.findElements(By.className("tabs2_tab"));
		for (int i = 0; i <tabs.size(); i++) {
			if (tabs.get(i).getText().trim().equalsIgnoreCase(tabName)) {
				tabs.get(i).click();
			}
		}
		this.focusForm(false);
	}
	/**
	 * Clicks the button with the given ID.
	 * 
	 * @param buttonID - The ID of the button
	 */
	public void clickButton(String buttonID) {
		this.focusForm(true);
		WebElement button = _wait.until(ExpectedConditions.presenceOfElementLocated(By.id(buttonID)));
		button.click();
		try {
			Alert alert = _driver.switchTo().alert();
			alert.accept();
		}
		catch (Exception ex) {
			//WHo Cares.. we were just trying to get rid of the alert
		}
		this.focusForm(false);
	}
	/**
	 * Clicks the button with the given name.
	 * 
	 * @param buttonName - The name on the button
	 */
	public void clickButtonByLabel(String buttonName) {
		this.focusForm(true);
		WebElement button = null;
		List<WebElement> buttonList = _wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("button")));
		for (int i = 0; i < buttonList.size(); i++) {
			if(buttonList.get(i).getText().trim().equalsIgnoreCase(buttonName)){
				button = buttonList.get(i);
				break;
			}
		}
		this.clickButton(button.getAttribute("id"));
		this.focusForm(false);
	}
	public void clickContextMenuItem(String contextMenuID, String contextMenuItemText) {
		_action.moveToElement(_driver.findElement(By.id(contextMenuID)));
		_action.contextClick(_driver.findElement(By.id(contextMenuID))).build().perform();
		List<WebElement> contextItemList = _driver.findElements(By.className("context_item"));
		for (int i = 0; i <contextItemList.size(); i++) {
			if(contextItemList.get(i).getText().trim().equalsIgnoreCase(contextMenuItemText)) {
				contextItemList.get(i).click();
				break;
			}
		}
		try {
			Alert alert = _driver.switchTo().alert();
			alert.accept();
		}
		catch (Exception ex) {
			//WHo Cares.. we were just trying to get rid of the alert
		}
	}

	//TODO: Get this working
	public void populateAllFields() {
		//Find all fields that are possible to type in and fill those fields with generic data
		//Reference
		//Choice
		//String
		//ETC
	}
	/**
	 * Find the field given and populates it with test data, (Figures out Type of field).
	 * 
	 * @param fieldID - The ID of the field.
	 * @throws Exception - 
	 */
	public void populateField(String fieldID) throws Exception {
		this.focusForm(true);
		WebElement element = _wait.until(ExpectedConditions.presenceOfElementLocated(By.id(fieldID)));
		switch(getFieldType(element)) {
		case "error":
			//TODO: Throw a better exception. (Maybe just write an error and do not fill in the field).
			throw new Exception("Unknown Field Type, Can not populate");
		case "string":
			populateFieldById(fieldID,"Test Data");
		}
		element.sendKeys(Keys.TAB);
		this.focusForm(false);
	}
	/**
	 * Finds the field given and types into it.
	 * 
	 * @param fieldID - The ID of the field.
	 * @param content - The value to type into the field.
	 */
	public void populateFieldById(String fieldID,String content) {
		this.focusForm(true);
		WebElement element = _wait.until(ExpectedConditions.presenceOfElementLocated(By.id(fieldID)));
		_action.moveToElement(element);
		_action.click();
		_action.sendKeys(content);
		_action.sendKeys(Keys.TAB);
		_action.build().perform();
		this.focusForm(false);
	}
	/**
	 * Finds the field given and types into it.
	 * 
	 * @param fieldLabel - The label of the field.
	 * @param content - The value to type into the field.
	 */
	public void populateFieldByLabel(String fieldLabel, String content) {
		this.focusForm(true);
		WebElement label = getFieldByLabel(fieldLabel);
		//WebElement element = label.findElement(By.xpath("../../.."));
		this.populateFieldById(label.getAttribute("id"),content);
		this.focusForm(false);
	}

	/**
	 * Pauses code until all mandatory fields have been populated on the form
	 * @param timeoutDurationSecs - an int in seconds for how long you would like to wait before timeout.
	 */
	public void waitUntilMandatoryReferenceFieldsPopulate(int timeoutDurationSecs) {
		//TODO - Get this working as intended
		//StartTIme = time.now
		while (!mandatoryFieldsPopulated()) {
			//if (StartTime + duration > CurrenTime)
			//Throw Error
		}
	}	
	/**
	 * Searches for the Applicaiton Name, Clicks the module under the application.
	 * 
	 * @param applicationName - The name of the Application
	 * @param moduleName - The name of the module
	 */
	public void ChooseApplicationAndModule(String applicationName, String moduleName){
		this.focusForm(false);
		WebElement filterNavigator = _wait.until(ExpectedConditions.elementToBeClickable(By.id("filter")));
		filterNavigator.clear();
		filterNavigator.sendKeys(applicationName);
		WebElement applicationList =_wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("concourse_application_tree")));
		List<WebElement> applications = applicationList.findElements(By.xpath("//li[@ng-repeat='application in applications']"));
		for (int i = 0; i < applications.size(); i++) {
			if (applications.get(i).findElement(By.tagName("a")).getText().trim().equalsIgnoreCase(applicationName)) {
				//Found Application
				WebElement application = applications.get(i);
				WebElement moduleList = application.findElement(By.tagName("ul"));
				List<WebElement> modules = moduleList.findElements(By.xpath("//li[@ng-repeat='appModule in ::application.modules track by appModule.id']"));
				for (int j = 0; j < modules.size(); j++) {
					if(modules.get(j).getText().trim().equalsIgnoreCase(moduleName)) {
						//Found Module
						modules.get(j).click();
						_wait.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(By.name("gsft_main"))));
						return;
					}
				}
				//throw new Exception("Module Not Found");
			}
		}
		//throw new Exception("Application Not Found");
	}
	/**
	 * If the Service Now environment is using Basic Auth, it this method will login for the user. Called in the BaseTest class.
	 * @param userName - The username as a string.
	 * @param Password - The password as a string.
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
	 * @param content - The value to type into the search bar.
	 */
	public void searchFor(String content) {
		this.focusForm(false);
		WebElement mGTypeArea = _wait.until(ExpectedConditions.presenceOfElementLocated(By.name("sysparm_search")));
		if(!mGTypeArea.getClass().toString().contains("focus")) {
			WebElement magnifyingGlass = _wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[action='textsearch.do']")));
			magnifyingGlass.click();
		}
		mGTypeArea.sendKeys(content);
		mGTypeArea.sendKeys(Keys.ENTER);
	}
	/**
	 * Obtains the value of a field given the label.
	 *
	 * @param fieldLabel - The value of the label.
	 * @return 			 - The value of the field.
	 */
	public String getFieldValueByLabel(String fieldLabel) {
		this.focusForm(true);
		WebElement field = getFieldByLabel(fieldLabel);
		return field.findElement(By.tagName("input")).getAttribute("value").trim();
	}
}
