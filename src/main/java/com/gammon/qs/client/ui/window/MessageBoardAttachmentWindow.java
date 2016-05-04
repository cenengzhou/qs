package com.gammon.qs.client.ui.window;

import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.domain.MessageBoardAttachment;
import com.gammon.qs.shared.GlobalParameter;
import com.google.gwt.user.client.ui.Image;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.layout.AbsoluteLayout;
import com.gwtext.client.widgets.layout.AbsoluteLayoutData;
import com.gwtext.client.widgets.layout.RowLayout;

/**
 * koeyyeung
 * Feb 5, 2014 5:33:59 PM
 */
public class MessageBoardAttachmentWindow extends Window{
	private static final String ATTACHMENT_PATH = GlobalParameter.IMAGE_DOWNLOAD_URL;
	private Panel basePanel;
	
	private ToolbarButton firstButton;
	private ToolbarButton previousButton;
	private ToolbarButton nextButton;
	private ToolbarButton lastButton;
	private NumberField currentPageField;
	private ToolbarTextItem totalPageText;
	
	private int currentImageIndex;
	private List<MessageBoardAttachment> attachmentList;
	
	private Image image;
	
	@SuppressWarnings("unused")
	private GlobalSectionController globalSectionController;

	public MessageBoardAttachmentWindow(GlobalSectionController globalSectionController,List<MessageBoardAttachment> attachmentList, int currentImageIndex) {
		super();
		this.globalSectionController = globalSectionController;
		this.currentImageIndex = currentImageIndex;
		this.attachmentList = attachmentList;
		
		setupUI();
	}
	
	private void setupUI() {
		setTitle("Attachment");
		setLayout(new RowLayout());
		setBorder(false);
		setHeight(600);
		setWidth(1000);
		setClosable(true);
		setModal(true);
		
		setupImagePanel();
	}

	private void setupToolbar(){
		Toolbar toolbar = new Toolbar();

		firstButton = new ToolbarButton();
		previousButton = new ToolbarButton();
		nextButton = new ToolbarButton();
		lastButton = new ToolbarButton();
		
		//First Button
		firstButton.setIconCls("first-page-button");
		firstButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				currentImageIndex = 0;
				//disable button
				firstButton.setDisabled(true);
				firstButton.setIconCls("first-page-disabled-button");
				previousButton.setDisabled(true);
				previousButton.setIconCls("prev-page-disabled-button");
				
				//enable button
				nextButton.enable();
				nextButton.setIconCls("next-page-button");
				lastButton.enable();
				lastButton.setIconCls("last-page-button");
				
				reloadPanel();
			}
		});
		
		//Previous Button
		previousButton.setIconCls("prev-page-button");
		previousButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				currentImageIndex -= 1;
				
				if(currentImageIndex == 0){
					//disable button
					firstButton.disable();
					firstButton.setIconCls("first-page-disabled-button");
					previousButton.disable();
					previousButton.setIconCls("prev-page-disabled-button");
				}
				
				//enable button
				nextButton.enable();
				nextButton.setIconCls("next-page-button");
				lastButton.enable();
				lastButton.setIconCls("last-page-button");
				
				reloadPanel();
			}
		});
	
		//Next Button
		nextButton.setIconCls("next-page-button");
		nextButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				currentImageIndex += 1;
				if(currentImageIndex == attachmentList.size()-1){
					//disable button
					nextButton.disable();
					nextButton.setIconCls("next-page-disabled-button");
					lastButton.disable();
					lastButton.setIconCls("last-page-disabled-button");
				}
				
				//enable button
				firstButton.enable();
				firstButton.setIconCls("first-page-button");
				previousButton.enable();
				previousButton.setIconCls("prev-page-button");
				
				reloadPanel();
			}
		});

		//Last Button
		lastButton.setIconCls("last-page-button");
		lastButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				currentImageIndex = attachmentList.size()-1;
				
				//disable button
				nextButton.disable();
				nextButton.setIconCls("next-page-disabled-button");
				lastButton.disable();
				lastButton.setIconCls("last-page-disabled-button");
				
				//enable button
				firstButton.enable();
				firstButton.setIconCls("first-page-button");
				previousButton.enable();
				previousButton.setIconCls("prev-page-button");
				
				reloadPanel();
			}
		});
		
		ToolbarButton closeButton = new ToolbarButton("Close");
		closeButton.setIconCls("cancel-icon");
		closeButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				MessageBoardAttachmentWindow.this.close();
			}
		});
		
		currentPageField = new NumberField();
		currentPageField.setAllowDecimals(false);
		currentPageField.setWidth(30);
		
		currentPageField.addListener(new FieldListenerAdapter(){
			public void onSpecialKey(Field field, EventObject e){
				if(e.getKey() == EventObject.ENTER)
					if(!"".equals(currentPageField.getValueAsString()) && currentPageField.getValueAsString()!="0"){
						currentImageIndex = Integer.valueOf(currentPageField.getValueAsString())-1;
						reloadPanel();
					}
			}
		});
		
		totalPageText = new ToolbarTextItem(String.valueOf(attachmentList.size()));
		

		if(attachmentList.size()==1){
			//disable button
			firstButton.disable();
			firstButton.setIconCls("first-page-disabled-button");
			previousButton.disable();
			previousButton.setIconCls("prev-page-disabled-button");

			nextButton.disable();
			nextButton.setIconCls("next-page-disabled-button");
			lastButton.disable();
			lastButton.setIconCls("last-page-disabled-button");
		}
		else if(currentImageIndex==0){//first image
			firstButton.disable();
			firstButton.setIconCls("first-page-disabled-button");
			previousButton.disable();
			previousButton.setIconCls("prev-page-disabled-button");
		}
		else if(currentImageIndex==attachmentList.size()-1){//last image
			nextButton.disable();
			nextButton.setIconCls("next-page-disabled-button");
			lastButton.disable();
			lastButton.setIconCls("last-page-disabled-button");
		}

		
		toolbar.addButton(firstButton);
		toolbar.addSeparator();
		toolbar.addButton(previousButton);
		toolbar.addSeparator();
		
		toolbar.addItem(new ToolbarTextItem("Page"));
		toolbar.addField(currentPageField);
		toolbar.addItem(new ToolbarTextItem(" of "));
		toolbar.addItem(totalPageText);
		
		toolbar.addSeparator();
		toolbar.addButton(nextButton);
		toolbar.addSeparator();
		toolbar.addButton(lastButton);
		toolbar.addSeparator();
		toolbar.addFill();
		toolbar.addSeparator();
		toolbar.addButton(closeButton);
		toolbar.addSeparator();

		
		basePanel.setBottomToolbar(toolbar);
	}
	
	private void setupImagePanel(){
		basePanel = new Panel();
		//basePanel.setSize(500, 500);
		basePanel.setAutoScroll(true);
		basePanel.setBorder(false);
		basePanel.setLayout(new AbsoluteLayout());
		
		setupToolbar();
		
		add(basePanel);
		
		populateImage();
	}
	
	
	private void populateImage(){
		currentPageField.setValue(currentImageIndex+1);
		
		image = new Image(ATTACHMENT_PATH+"?imageID="+attachmentList.get(currentImageIndex).getId()+"&filename="+attachmentList.get(currentImageIndex).getFilename());
		image.setHeight("100%");
		image.setWidth("100%");
		
		basePanel.add(image, new AbsoluteLayoutData(0, 0));
		basePanel.doLayout();	
	}
	
	private void reloadPanel(){
		basePanel.remove(image);
		populateImage();
	}
	
}
