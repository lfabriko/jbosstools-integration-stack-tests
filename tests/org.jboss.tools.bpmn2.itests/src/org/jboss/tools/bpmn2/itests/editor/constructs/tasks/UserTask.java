package org.jboss.tools.bpmn2.itests.editor.constructs.tasks;


import org.eclipse.swtbot.swt.finder.SWTBot;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.constructs.AbstractTask;
import org.jboss.tools.bpmn2.itests.editor.properties.variables.IParameter;


/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class UserTask extends AbstractTask {

	public UserTask(String name) {
		super(name, ConstructType.USER_TASK);
	}
	
	public void setTaskName(String name) {
		properties.selectTab("User Task");
		new LabeledText("Task Name").setText(name);
	}
	
	public void setPriority(int priority) {
		properties.selectTab("User Task");
		new LabeledText("Priority").setText(String.valueOf(priority));
	}
	
	public void setComment(String comment) {
		properties.selectTab("User Task");
		new LabeledText("Comment").setText(comment);
	}
	
	public void setGroupId(String id) {
		properties.selectTab("User Task");
		new LabeledText("Group Id").setText(id);
	}
	
	public void setSkippable(boolean skippable) {
		properties.selectTab("User Task");
		CheckBox box = new CheckBox("Skippable");
		if ((box.isChecked() && !skippable) || (!box.isChecked() && skippable)) {
			box.click();
		}
		
	}
	
	public void setContent(String content) {
		properties.selectTab("User Task");
		new LabeledText("Content").setText(content);
	}
	
	public void setLocale(String locale) {
		properties.selectTab("User Task");
		new LabeledText("Locale").setText(locale);
	}
	
	public void addActor(String script, String language) {
		properties.selectTab("User Task");
		
		SWTBot viewBot = Bot.get().activeShell().bot();
		viewBot.toolbarButtonWithTooltip("Add").click();
		
		new DefaultCombo("Script Language").setSelection(language);
		new LabeledText("Script").setText(script);
		
		viewBot.toolbarButtonWithTooltip("Close").click();
	}
	
	public void removeActor(String name) {
		properties.selectTab("User Task");
		
		SWTBot viewBot = Bot.get().activeShell().bot();
		viewBot.table().select(name);
		viewBot.toolbarButtonWithTooltip("Remove").click();
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractTask#setIsForCompensation(boolean)
	 */
	@Override
	public void setIsForCompensation(boolean b) {
		super.setIsForCompensation(b);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractTask#setOnEntryScript(java.lang.String, java.lang.String)
	 */
	@Override
	public void setOnEntryScript(String language, String script) {
		super.setOnEntryScript(language, script);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractTask#setOnExistScript(java.lang.String, java.lang.String)
	 */
	@Override
	public void setOnExistScript(String language, String script) {
		super.setOnExistScript(language, script);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractTask#addInputParameter(org.jboss.tools.bpmn2.itests.editor.properties.variables.IParameter)
	 */
	@Override
	public void addParameterMapping(IParameter parameter) {
		super.addParameterMapping(parameter);
	}

}
