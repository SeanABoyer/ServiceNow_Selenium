package tests.forms.incident;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class IncidentToClosed extends IncidentToWorkInProgress {
	

	public IncidentToClosed(String environment) {
		super(environment);
		// TODO Auto-generated constructor stub
	}

	public void testProcess() throws Exception {
		super.testProcess();
		serviceNow.searchFor(this.IncNumber);
		serviceNow.populateFieldByLabel("State", "Closed");
		serviceNow.populateFieldByLabel("Short description", "Moved incident to Closed");
		serviceNow.clickButtonByLabel("Submit");

	}

	@Test
	public void validate() {
		try {
			testProcess();
		} catch (Exception e) {
			fail(e.getMessage());
		}
		//Run Validation to make sure things happened correctly
		serviceNow.searchFor(this.IncNumber);
		if(serviceNow.getFieldValueByLabel("Short description").equalsIgnoreCase("Moved incident to Closed") &&
			serviceNow.getFieldValueByLabel("State").equalsIgnoreCase("Closed")) {
			assertTrue("The Incident ["+this.IncNumber+"] was updated successfully",true);
		}
		else {
			fail("The Incident ["+this.IncNumber+"] was NOT updated successfully");
		}
	}


}
