package tests.forms.change;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeoutException;

import org.junit.Test;

import tests.forms.incident.H_CreateIncident;

public class H_CreateNormalChangeFromIncident extends H_CreateIncident {
	
	protected String ChangeNumber = null;

	public H_CreateNormalChangeFromIncident(String environment) {
		super(environment);
	}

	public void testProcess() throws TimeoutException {
		super.testProcess();
		serviceNow.searchFor(this.IncNumber);
		serviceNow.clickContextMenuItem("Create Normal Change");
		
		this.ChangeNumber = serviceNow.getFieldValueByLabel("Number");

	}

	@Test
	public void validate() {
		try {
			testProcess();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

		//Run Validation to make sure things happened correctly
		boolean infoMessageCorrect = false;
		boolean changeTypeIsCorrect = false;
		boolean changeOnIncidentForm = false;
		if (serviceNow.getInfoMessageContaining("Change "+this.ChangeNumber+" created") != null) {
			infoMessageCorrect = true;
		}
		if(serviceNow.getFieldValueByLabel("Type").equalsIgnoreCase("Normal")) {
			changeTypeIsCorrect = true;
		}
		serviceNow.searchFor(this.IncNumber);
		serviceNow.clickTabByLabel("Related Records");
		if (serviceNow.getFieldValueByLabel("Change Request").equalsIgnoreCase(this.ChangeNumber)) {
			changeOnIncidentForm = true;
		}
		if(infoMessageCorrect && changeTypeIsCorrect && changeOnIncidentForm) {
			assertTrue("The Change ["+this.ChangeNumber+"] was created successfully",true);
		}
		else {
			fail("The Change ["+this.ChangeNumber+"] was NOT created successfully");
		}
	}


}
