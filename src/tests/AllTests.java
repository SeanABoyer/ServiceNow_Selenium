package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import tests.forms.change.H_CreateEmergencyChangeFromIncident;
import tests.forms.change.H_CreateNormalChangeFromIncident;
import tests.forms.incident.H_CreateIncident;
import tests.forms.incident.H_CreateIncidentWithSave;
import tests.forms.incident.H_IncidentToClosed;
import tests.forms.incident.H_IncidentToWorkInProgress;
import tests.forms.knowledge.H_CreateKnowledge;
import tests.forms.problem.H_CreateProblem;
import tests.forms.problem.H_CreateProblemFromIncident;

@RunWith(Suite.class)
@Suite.SuiteClasses({
//	H_CreateChange.class,
	H_CreateEmergencyChangeFromIncident.class,
	H_CreateNormalChangeFromIncident.class,
	
	H_CreateIncident.class,
	H_CreateIncidentWithSave.class,
	H_IncidentToWorkInProgress.class,
	H_IncidentToClosed.class,
	
	H_CreateKnowledge.class,
	
	H_CreateProblem.class,
	H_CreateProblemFromIncident.class
})
public class AllTests {

}
