package utils;

import java.util.List;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ServiceNow {
	protected WebDriver _driver;
	protected WebDriverWait _wait;
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
	 * @category Get Message
	 */
	private List<WebElement> getInfoMessages() {
		this.focusForm(true);
		List<WebElement> infoMessages = _driver.findElements(By.className("outputmsg_info"));
		return infoMessages;
	}
	
	/**
	 * @category Get Message
	 */
	public WebElement getInfoMessageContaining(String substring) {
		List<WebElement> infoMessages = getInfoMessages();
		WebElement messageElement = null;
		for (int i = 0; i < infoMessages.size(); i++) {
			if(infoMessages.get(i).findElement(By.className("outputmsg_text")).getText().trim().contains(substring)){
				messageElement = infoMessages.get(i);
			}
		}
		return messageElement;		
	}
	/**
	 * @category Get Message
	 */
	private List<WebElement> getErrorMessages() {
		this.focusForm(true);
		List<WebElement> infoMessages = _driver.findElements(By.className("outputmsg_info"));
		return infoMessages;
	}
	
	/**
	 * @category Get Message
	 */
	public WebElement getErrorMessageContaining(String substring) {
		List<WebElement> errorMessages = getErrorMessages();
		WebElement messageElement = null;
		for (int i = 0; i < errorMessages.size(); i++) {
			if(errorMessages.get(i).findElement(By.className("outputmsg_text")).getText().trim().contains(substring)){
				messageElement = errorMessages.get(i);
			}
		}
		return messageElement;		
	}
	/**
	 * @category Get Field ID
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
		if(field == null) {
			throw new NotFoundException("Field with the label of ["+fieldLabel+"] was not found.");
		}
		return field;
	}
	private String getFieldValueByElement(WebElement element) {
		String value = null;
		switch(this.getFieldType(element)) {
		case "choice":
			value = _driver.findElement(By.id(element.getAttribute("id").split("element.")[1])).findElement(By.cssSelector("[selected='SELECTED']")).getText();
			break;
		case "reference":
			value = element.findElement(By.id("sys_display."+element.getAttribute("id").split("element.")[1])).getAttribute("value");
			break;
		default:
			value = element.findElement(By.tagName("input")).getAttribute("value");
		}
		return value;
	}
	/**
	 * @category Get Field Value ID 
	 */
	private String getFieldValueByID(String fieldID) {
		WebElement field = getFieldByID(fieldID);
		return getFieldValueByElement(field);

	}
	/**
	 * @category Get Field Value label
	 */
	public String getFieldValueByLabel(String fieldLabel) {
		this.focusForm(true);
		WebElement field = getFieldByLabel(fieldLabel);
		return getFieldValueByElement(field);
	}

	/**
	 * @category Get Tab Label
	 */
	private WebElement getTabByLabel(String tabLabel) {
		WebElement tab = null;
		List<WebElement> tabs = _driver.findElements(By.className("tabs2_tab"));
		for (int i = 0; i < tabs.size(); i++) {
			if (tabs.get(i).getText().endsWith(tabLabel)) {
				tab = tabs.get(i);
				break;
			}
		}
		return tab;
		
	}
	/**
	 * @category Get Button ID 
	 */
	private WebElement getButtonByID(String buttonID) {
		WebElement button = _wait.until(ExpectedConditions.presenceOfElementLocated(By.id(buttonID)));
		return button;
		
	}
	/**
	 * @category Get Button Label 
	 */
	private WebElement getButtonByLabel(String buttonLabel) {
		WebElement button = null;
		List<WebElement> buttonList = _wait
				.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("button")));
		for (int i = 0; i < buttonList.size(); i++) {
			if (buttonList.get(i).getText().trim().equalsIgnoreCase(buttonLabel)) {
				button = buttonList.get(i);
				break;
			}
		}
		return button;
		
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
	 * @category Set Field ID 
	 * The fieldID must be the element.TABLENAME.FIELDNAME format
	 */
	public void populateFieldByID(String fieldID, String content) {
		this.focusForm(true);
		WebElement element = _wait.until(ExpectedConditions.presenceOfElementLocated(By.id(fieldID)));
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
		WebElement tab = getTabByLabel(tabLabel);
		tab.click();
	}
	/**
	 * @category Click Button ID
	 */
	public void clickButtonByID(String buttonID) {
		this.focusForm(true);
		WebElement button = this.getButtonByID(buttonID);
		button.click();
	}

	/**
	 * @category Click Button Label
	 */
	public void clickButtonByLabel(String buttonLabel) {
		this.focusForm(true);
		WebElement button = this.getButtonByLabel(buttonLabel);
		this.clickButtonByID(button.getAttribute("id"));
	}
	/**
	 * @category Click Menu ID
	 */
	public void clickContextMenuItem(String menuItem) {
		this.focusForm(true);
		WebElement headerBar = _wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("container-fluid")));
		_action.moveToElement(headerBar);
		_action.contextClick().build().perform();
		List<WebElement> contextItemList = _driver.findElements(By.className("context_item"));
		for (int i = 0; i < contextItemList.size(); i++) {
			if (contextItemList.get(i).getText().trim().equalsIgnoreCase(menuItem)) {
				contextItemList.get(i).click();
				break;
			}
		}
	}
	//================================================================================
	// Other
	//================================================================================
	private boolean isFieldReadOnly(WebElement element) {
		//TODO - 
		return false;
	}
	private String getFieldType(WebElement element) {
		this.focusForm(true);
		String type = element.findElement(By.id(element.getAttribute("id").replaceFirst("element", "label"))).getAttribute("type");
		try {
			if(type.equalsIgnoreCase("Choice") && element.findElement(By.tagName("select")).getAttribute("disabled").equalsIgnoreCase("true")) {
				type = "String";
			}
		}catch (Exception ex) {
			//TODO - find a way to check if the field is readonly.
		}
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
	 * @throws TimeoutException 
	 */
	public void waitUntilMandatoryReferenceFieldsPopulate(int timeoutDurationSecs) throws TimeoutException {
		long StartTime = System.currentTimeMillis();
		while (!mandatoryFieldsPopulated()) {
			if (StartTime + timeoutDurationSecs*1000 <= System.currentTimeMillis()) {
				throw new TimeoutException("Some mandatory fields are still empty.");
			}
		}
	}

	/**
	 * Searches for the Application Name, Clicks the module under the application.
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
						return;
					}
				}
				throw new NotFoundException("Module by the name of ["+moduleName+"] was not found.");
			}
		}
		throw new NotFoundException("Application by the name of ["+applicationName+"] was not found.");
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
	}


}
