package tests.forms.problem;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeoutException;

import org.junit.Test;

import tests.BaseTest;

public class H_CreateProblem extends BaseTest{

	protected String PrbNumber = null;
	
	public H_CreateProblem(String environment) {
		super(environment);
	}
	
	public void testProcess() throws TimeoutException {
		//No Super.testProcess() because it extends the BaseTest Class
		//Code here should do the manual process a user would do
		serviceNow.ChooseApplicationAndModule("Problem", "Create New");
		//The above method seems to be not working to click the Create New. Need to look into this more. - TODO
		this.PrbNumber = serviceNow.getFieldValueByLabel("Number");
		serviceNow.clickButtonByLabel("SUBMIT");
		
	}
	
	/**
	 * An Example of creating a problem and validating it has been created.
	 */
	@Test
	public void test() {
		try {
			testProcess();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		//Run Validation to make sure things happened correctly
		serviceNow.searchFor(this.PrbNumber);
		if(serviceNow.getFieldValueByLabel("Number").equalsIgnoreCase(this.PrbNumber)) {
			assertTrue("The Incident ["+this.PrbNumber+"] was created successfully",true);
		}
		else {
			fail("The Incident ["+this.PrbNumber+"] was NOT created successfully");
		}
	}

}
