package tests.forms.change;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeoutException;

import org.junit.Test;

import tests.BaseTest;

public class H_CreateChange extends BaseTest {

	protected String ChangeNumber = null;
	
	public H_CreateChange(String environment) {
		super(environment);
	}
	
	public void testProcess() throws TimeoutException {
		//No Super.testProcess() because it extends the BaseTest Class
		//Code here should do the manual process a user would do
		serviceNow.ChooseApplicationAndModule("Change", "Create New");
		//TODO - Create a Class to Interact with Wizards.
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
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
//		serviceNow.searchFor(this.ChangeNumber);
//		if(serviceNow.getFieldValueByLabel("Number").equalsIgnoreCase(this.ChangeNumber)) {
//			assertTrue("The Change ["+this.ChangeNumber+"] was created successfully",true);
//		}
//		else {
			fail("The Change ["+this.ChangeNumber+"] was NOT created successfully");
//		}
	}
}
