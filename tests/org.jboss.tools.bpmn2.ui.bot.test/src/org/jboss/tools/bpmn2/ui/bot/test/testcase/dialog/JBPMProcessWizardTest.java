package org.jboss.tools.bpmn2.ui.bot.test.testcase.dialog;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jface.exception.JFaceLayerException;
import org.jboss.tools.bpmn2.reddeer.dialog.JBPMProcessWizard;
import org.jboss.tools.bpmn2.reddeer.dialog.JBPMProjectWizard;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessRuntimeRequirement.ProcessRuntime;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Verify functionality of the process wizard.
 */
@ProcessRuntime()
public class JBPMProcessWizardTest {

	static ProjectExplorer packageView = new ProjectExplorer();
	static JBPMProjectWizard projectWizardView = new JBPMProjectWizard();
	static JBPMProcessWizard processWizardView = new JBPMProcessWizard();
	
	@BeforeClass
	public static void createProject() {
		projectWizardView.execute("TestProject");
	}
	
	@AfterClass
	public static void deleteProject() {
		packageView.getProject("TestProject").delete(true);
	}
	
	@Test
	public void newProcessTest() {
		processWizardView.execute(new String[] {"TestProject", "src/main/resources"}, "SampleProcess");
		Assert.assertTrue(packageView.getProject("TestProject").containsItem("src/main/resources", "SampleProcess.bpmn"));
	}
	
	/**
	 * ISSUES:
	 * 	1) make sure an empty name may not be added.
	 *  2) should it be legal to create a name without .bpmn suffix?
	 *  
	 * @throws Exception
	 */
	@Test
	public void newProcessFormValidationTest() throws Exception {
		try {
			processWizardView.execute("");
		} catch (JFaceLayerException e) {
			Assert.assertEquals("Button '&Finish' is not enabled", e.getMessage());
		} finally {
			processWizardView.cancel();
		}
	}
	
}
