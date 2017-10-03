package tests.forms.problem;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeoutException;

import org.junit.Test;

import tests.forms.incident.H_CreateIncident;

public class H_CreateProblemFromIncident extends H_CreateIncident {
	
	protected String PrbNumber = null;
	
	public H_CreateProblemFromIncident(String environment) {
		super(environment);
	}

	public void testProcess() throws TimeoutException {
		super.testProcess();
		serviceNow.searchFor(this.IncNumber);
		serviceNow.clickContextMenuItem("Create Problem");
		
		this.PrbNumber = serviceNow.getFieldValueByLabel("Number");
	}

	@Test
	public void validate() {
		try {
			testProcess();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		//Run Validation to make sure things happened correctly
		//Check if PRB Exists
		boolean prbExists = false;
		boolean IncLinkedtoPrb = false;
		serviceNow.searchFor(this.PrbNumber);
		if(serviceNow.getFieldValueByLabel("Number").equalsIgnoreCase(this.PrbNumber)) {
			prbExists = true;
		}
		//Check if INC is linked to PRB from PRB form
		serviceNow.searchFor(this.IncNumber);
		serviceNow.clickTabByLabel("Related Records");
		if(serviceNow.getFieldValueByLabel("Problem").equalsIgnoreCase(this.PrbNumber)) {
			IncLinkedtoPrb = true;
		}
		
		if (prbExists && IncLinkedtoPrb) {
			assertTrue("The Problem ["+this.PrbNumber+"] was created successfully from ["+this.IncNumber+"].",true);
		}
		else {
			fail("A Problem was not created successfully from ["+this.IncNumber+"].");
		}
	}


}
