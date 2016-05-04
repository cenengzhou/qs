/**
 * koeyyeung
 * Feb 28, 20145:00:25 PM
 */
package com.gammon.qs.client.ui.window;

import java.util.ArrayList;
import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.panel.mainSection.MessageBoardMainPanel;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.MessageBoard;
import com.gammon.qs.shared.GlobalParameter;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;

/**
 * @author koeyyeung
 *
 */
public class AddNewMessageWindow  extends Window{
	private static final String WINDOW_ID = "AddNewMessageBoardWindow_ID";

	private TextField titleTextField;
	private TextArea descriptionTextField;
	private TextField requestorTextField;

	private static final String MESSAGE_TYPE_VALUE = "messageTypeValue";
	private static final String MESSAGE_TYPE_DISPLAY = "messageTypeDisplay"; 
	private ComboBox messageTypeComboBox = new ComboBox();
	private String[][] messageTypes = new String[][] {};
	private Store messageTypeStore = new SimpleStore(new String[] {MESSAGE_TYPE_VALUE, MESSAGE_TYPE_DISPLAY}, messageTypes);


	private static final String IS_DISPLAY_VALUE = "isDisplayValue";
	private static final String IS_DISPLAY_DISPLAY = "isDisplayDisplay"; 
	private ComboBox isDisplayComboBox = new ComboBox();
	private String[][] isDisplays = new String[][] {};
	private Store isDisplaysStore = new SimpleStore(new String[] {IS_DISPLAY_VALUE, IS_DISPLAY_DISPLAY}, isDisplays);

	private DateField deliveryDateField;

	private GlobalSectionController globalSectionController;
	
	public AddNewMessageWindow(GlobalSectionController globalSectionController) {
		super();
		this.globalSectionController = globalSectionController;
		
		setLayout(new RowLayout());
		setTitle("Add New Message");
		setClosable(true);
		setModal(true);
		setHeight(450);
		setWidth(350);
		setId(WINDOW_ID);

		setupUI();
	}

	public void setupUI(){
		Panel contentPanel = new Panel();
		contentPanel.setPaddings(0);
		contentPanel.setFrame(true);
		contentPanel.setLayout(new TableLayout(2));

		//Message Type
		Label messageTypeLabel = new Label("Message Type");
		messageTypeLabel.setCls("table-cell");
		contentPanel.add(messageTypeLabel);

		messageTypeComboBox.setCtCls("table-cell");
		messageTypeComboBox.setForceSelection(true);
		messageTypeComboBox.setMinChars(1);
		messageTypeComboBox.setValueField(MESSAGE_TYPE_VALUE);
		messageTypeComboBox.setDisplayField(MESSAGE_TYPE_DISPLAY);
		messageTypeComboBox.setMode(ComboBox.LOCAL);
		messageTypeComboBox.setTriggerAction(ComboBox.ALL);
		messageTypeComboBox.setValue(MessageBoard.ANNOUNCEMENT);
		messageTypeComboBox.setTypeAhead(true);
		messageTypeComboBox.setSelectOnFocus(true);
		messageTypeComboBox.setWidth(200);

		messageTypes = new String[][]{
				{"","ALL"},
				{MessageBoard.ANNOUNCEMENT, MessageBoard.ANNOUNCEMENT},
				{MessageBoard.PROMOTION,MessageBoard.PROMOTION},
				{MessageBoard.ENHANCEMENT, MessageBoard.ENHANCEMENT}
		};
		messageTypeStore = new SimpleStore(new String[]{MESSAGE_TYPE_VALUE,MESSAGE_TYPE_DISPLAY},messageTypes);
		messageTypeComboBox.setStore(messageTypeStore);
		contentPanel.add(messageTypeComboBox);			


		//Title
		Label titleLabel = new Label("Title");
		titleLabel.setCls("table-cell");

		titleTextField = new TextField();
		titleTextField.setCtCls("table-cell");
		titleTextField.setWidth(200);

		contentPanel.add(titleLabel);
		contentPanel.add(titleTextField);

		//Description
		Label descriptionLabel = new Label("Description");
		descriptionLabel.setCls("table-cell");

		descriptionTextField = new TextArea();
		descriptionTextField.setCtCls("table-cell");
		descriptionTextField.setWidth(200);
		descriptionTextField.setHeight(200);

		contentPanel.add(descriptionLabel);
		contentPanel.add(descriptionTextField);

		//Requestor
		Label requestorLabel = new Label("Requestor");
		requestorLabel.setCls("table-cell");

		requestorTextField = new TextField();
		requestorTextField.setCtCls("table-cell");
		requestorTextField.setWidth(200);

		contentPanel.add(requestorLabel);
		contentPanel.add(requestorTextField);


		//Delivery Date
		Label deliveryDateLabel = new Label("Delivery Date");
		deliveryDateLabel.setCls("table-cell");	

		deliveryDateField = new DateField("Delivery Date", "deliveryDateSearch", 200);
		deliveryDateField.setFormat(GlobalParameter.DATEFIELD_DATEFORMAT);
		deliveryDateField.setCtCls("table-cell");
		deliveryDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		contentPanel.add(deliveryDateLabel);
		contentPanel.add(deliveryDateField);


		//Is Display
		Label isDisplayLabel = new Label("Is Display");
		isDisplayLabel.setCls("table-cell");
		contentPanel.add(isDisplayLabel);

		isDisplayComboBox.setCtCls("table-cell");
		isDisplayComboBox.setForceSelection(true);
		isDisplayComboBox.setMinChars(1);
		isDisplayComboBox.setValueField(IS_DISPLAY_VALUE);
		isDisplayComboBox.setDisplayField(IS_DISPLAY_DISPLAY);
		isDisplayComboBox.setMode(ComboBox.LOCAL);
		isDisplayComboBox.setTriggerAction(ComboBox.ALL);
		isDisplayComboBox.setTypeAhead(true);
		isDisplayComboBox.setSelectOnFocus(true);
		isDisplayComboBox.setWidth(200);

		isDisplays = new String[][]{
				{"Y", "Yes"},
				{"N", "No"}
		};
		isDisplaysStore = new SimpleStore(new String[]{IS_DISPLAY_VALUE,IS_DISPLAY_DISPLAY},isDisplays);
		isDisplayComboBox.setStore(isDisplaysStore);
		contentPanel.add(isDisplayComboBox);

		Button addButton = new Button("Add");
		addButton.setIconCls("save-button-icon");
		addButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				add();
			}
		});
		
		Button closeButton = new Button("Close");
		closeButton.setIconCls("cancel-icon");
		closeButton.setVisible(true);
		closeButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				AddNewMessageWindow.this.close();
			}
		});
		
		addButton(addButton);
		addButton(closeButton);
		
		add(contentPanel);
	}

	private void add() {
		List<MessageBoard> messageList = new ArrayList<MessageBoard>();

		MessageBoard message = new MessageBoard();
		message.setTitle(titleTextField.getValueAsString());
		message.setDescription(descriptionTextField.getValueAsString());
		message.setRequestor(requestorTextField.getValueAsString());
		message.setDeliveryDate(deliveryDateField.getValue());
		message.setMessageType(messageTypeComboBox.getValueAsString());
		message.setIsDisplay(isDisplayComboBox.getValueAsString());

		messageList.add(message);

		UIUtil.maskPanelById(WINDOW_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMessageBoardRepository().updateMessages(messageList, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(WINDOW_ID);
			}
			public void onSuccess(Boolean updated) {
				if(updated){
					AddNewMessageWindow.this.close();
					MessageBox.alert("Message Board has been updated successfully.");
					((MessageBoardMainPanel)globalSectionController.getMainSectionController().getContentPanel()).refreshAll();
				}
				UIUtil.unmaskPanelById(WINDOW_ID);
			}
		});
	}
}
