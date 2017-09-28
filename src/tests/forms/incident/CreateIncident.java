package tests.forms.incident;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeoutException;

import org.junit.Test;

import tests.BaseTest;

public class CreateIncident extends BaseTest{

	protected String IncNumber = null;
	
	public CreateIncident(String environment) {
		super(environment);
	}
	
	public void testProcess() throws TimeoutException {
		//No Super.testProcess() because it extends the BaseTest Class
		//Code here should do the manual process a user would do
		serviceNow.ChooseApplicationAndModule("Incident", "Create New");
		//serviceNow.populateFieldByLabel("Caller","Abel Tuter");
		serviceNow.populateFieldByLabel("Short description","Test Data");
		serviceNow.waitUntilMandatoryReferenceFieldsPopulate(10);
		
		this.IncNumber = serviceNow.getFieldValueByLabel("Number");
		serviceNow.clickButtonByLabel("SUBMIT");
		
	}

	@Test
	public void validate() {
		try {
			testProcess();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		//Run Validation to make sure things happened correctly
		serviceNow.searchFor(this.IncNumber);
		if(serviceNow.getFieldValueByLabel("Number").equalsIgnoreCase(this.IncNumber)) {
			assertTrue("The Incident ["+this.IncNumber+"] was created successfully",true);
		}
		else {
			fail("The Incident ["+this.IncNumber+"] was NOT created successfully");
		}

	}

}
