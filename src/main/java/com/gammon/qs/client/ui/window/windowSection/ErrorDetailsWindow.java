package com.gammon.qs.client.ui.window.windowSection;

import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

/**
 * @author matthewatc
 *
 * Window to display a list of short messages to the user.
 */
public class ErrorDetailsWindow extends Window {
	
	private GlobalSectionController globalSectionController;
	
	private Panel msgPanel;
	
	private Button closeButton;
	
	/**
	 * @param globalSectionControllerArgument the GlobalSectionController
	 * @param titleText text to display in the window's title bar
	 * @param headerText text to display, in bold, at the top of the list of messages
	 * @param messages a list of short messages to display
	 */
	public ErrorDetailsWindow(GlobalSectionController globalSectionControllerArgument, String titleText, String headerText, List<String> messages) {
		this.globalSectionController = globalSectionControllerArgument;
		
		this.setLayout(new FitLayout());
		this.setClosable(false);
		this.setTitle(titleText);
		
		this.setWidth(610);
		
		buildMessageList(headerText, messages);
		
		closeButton = new Button("Close");
		closeButton.addClass("right-align-button");
		closeButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, com.gwtext.client.core.EventObject e){
				globalSectionController.closePromptWindow();
			}
		});
		
		this.add(msgPanel);
		this.addButton(closeButton);
	}
	


	private void buildMessageList(String headerText, List<String> messages) {
		msgPanel = new Panel();
		msgPanel.setLayout(new VerticalLayout());
		msgPanel.setAutoScroll(true);
		msgPanel.setAutoHeight(true);
		
		Label headerLabel = new Label(headerText);
		headerLabel.addClass("left-align");
		headerLabel.addClass("text-bold");
		msgPanel.add(headerLabel);
		
		for(String msg : messages) {
			if(msg != null) {
				Label msgLabel = new Label(msg.trim());
				msgLabel.addClass("left-align");
				msgPanel.add(msgLabel);
			}
		}
		
	}
}
