package org.jboss.tools.switchyard.ui.bot.test;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.test.RedDeerTest;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.switchyard.reddeer.binding.BindingWizard;
import org.jboss.tools.switchyard.reddeer.binding.SOAPBindingPage;
import org.jboss.tools.switchyard.reddeer.component.Component;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.condition.ConsoleHasChanged;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.wizard.ImportFileWizard;
import org.jboss.tools.switchyard.reddeer.wizard.PromoteServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.SwitchYardProjectWizard;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerDeployment;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.Server;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.State;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.Type;
import org.jboss.tools.switchyard.ui.bot.test.suite.SwitchyardSuite;
import org.jboss.tools.switchyard.ui.bot.test.util.BackupClient;
import org.jboss.tools.switchyard.ui.bot.test.util.SoapClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Creation test from existing BPEL process
 * 
 * @author apodhrad
 * 
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@Server(type = Type.ALL, state = State.RUNNING)
@RunWith(SwitchyardSuite.class)
public class BottomUpBPELTest extends RedDeerTest {

	public static final String PROJECT = "bpel_project";
	public static final String WSDL = "http://localhost:8080/bpel_project/SayHelloService?wsdl";

	@Before
	@After
	public void closeSwitchyardFile() {
		try {
			new SwitchYardEditor().saveAndClose();
		} catch (Exception ex) {
			// it is ok, we just try to close switchyard.xml if it is open
		}
	}

	@Test
	public void bottomUpBPELtest() throws Exception {
		String version = SwitchyardSuite.getLibraryVersion();
		new SwitchYardProjectWizard(PROJECT, version).impl("BPEL").binding("SOAP").create();
		new ProjectExplorer().getProject(PROJECT).getProjectItem("src/main/resources").select();
		new ImportFileWizard().importFile("resources/bpel", "SayHello.bpel");
		new ImportFileWizard().importFile("resources/wsdl", "SayHelloArtifacts.wsdl");

		// There is no way how to create deployment descriptor, only manually
		new ImportFileWizard().importFile("resources/bpel", "deploy.xml");

		new SwitchYardEditor().addComponent("Component");
		new Component("Component").select();
		new SwitchYardEditor().activateTool("Process (BPEL)");
		new Component("Component").click();

		new PushButton("Browse...").click();
		new DefaultText(0).setText("SayHello.bpel");
		new PushButton("OK").click();
		new PushButton("Finish").click();

		new Component("Component").contextButton("Service").click();
		new PushButton("Browse...").click();
		new DefaultText(0).setText("SayHelloArtifacts.wsdl");
		new PushButton("OK").click();
		new PushButton("Finish").click();

		PromoteServiceWizard wizard = new Service("SayHello").promoteService();
		wizard.activate().setServiceName("SayHelloService").finish();

		new Service("SayHelloService").addBinding("SOAP");
		BindingWizard<SOAPBindingPage> soapWizard = BindingWizard.createSOAPBindingWizard();
		soapWizard.getBindingPage().setContextPath(PROJECT);
		soapWizard.finish();

		new SwitchYardEditor().save();

		/* Test SOAP Response */
		new ServerDeployment().deployProject(PROJECT);
		new ServerDeployment().fullPublish(PROJECT);
		try {
			SoapClient.testResponses(WSDL, "SayHello");
		} catch (Exception e) {
			BackupClient.backupDeployment(PROJECT);
			throw e;
		}

		new WaitWhile(new ConsoleHasChanged());
	}
}
