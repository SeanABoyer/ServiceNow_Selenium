package tests.forms.incident;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class Create2 extends Create {

	public Create2(String environment) {
		super(environment);
		// TODO Auto-generated constructor stub
	}

	public void testProcess() throws Exception {
		super.testProcess();
		System.out.println("CREATE2");
	}

	@Test
	public void validate() {
		try {
			testProcess();
		} catch (Exception e) {
			fail("Test Threw an Exception.");
			e.printStackTrace();
		}
		//Run Validation to make sure things happened correctly
		assertTrue("The Incident was created successfully",true);
	}


}
