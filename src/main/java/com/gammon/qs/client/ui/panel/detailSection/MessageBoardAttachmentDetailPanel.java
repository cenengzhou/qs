package com.gammon.qs.client.ui.panel.detailSection;

import java.util.ArrayList;
import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.panel.mainSection.MessageBoardMainPanel;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.client.ui.window.MessageBoardAttachmentWindow;
import com.gammon.qs.domain.MessageBoardAttachment;
import com.gammon.qs.shared.GlobalParameter;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.Connection;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Form;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.CheckboxListenerAdapter;
import com.gwtext.client.widgets.form.event.FormListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.CheckboxColumnConfig;
import com.gwtext.client.widgets.grid.CheckboxSelectionModel;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;
import com.gwtext.client.widgets.layout.TableLayoutData;

/**
 * koeyyeung
 * Feb 7, 2014 11:57:07 AM
 */
public class MessageBoardAttachmentDetailPanel extends EditorGridPanel{
	private static final String ATTACHMENT_IMAGE = GlobalParameter.BASED_URL+"images/";
	
	private Store dataStore;
	
	//Record names
	private static final String MSG_BOARD_ATTACHMENT_ID_RECORD_NAME = "messageBoardAttachmentIDRecordName";
	private static final String SEQUENCE_NO_RECORD_NAME = "sequenceNoRecordName";
	private static final String FILENAME_RECORD_NAME = "fileNameRecordName";
	private static final String DOC_TYPE_RECORD_NAME = "docTypeRecordName";
	
	//records
	private final RecordDef recordDef = new RecordDef(
			new FieldDef[] {	
					new StringFieldDef(MSG_BOARD_ATTACHMENT_ID_RECORD_NAME), 
					new StringFieldDef(SEQUENCE_NO_RECORD_NAME),
					new StringFieldDef(FILENAME_RECORD_NAME),
					new StringFieldDef(DOC_TYPE_RECORD_NAME)
			});
	
	private CheckboxSelectionModel selectionModel;
	
	//private GlobalMessageTicket globalMessageTicket;
	private GlobalSectionController globalSectionController;
	private String detailSectionPanel_ID;
	private List<MessageBoardAttachment> attachmentList;
	private String messageID;
	
	public MessageBoardAttachmentDetailPanel(GlobalSectionController globalSectionController, String messageID) {
		super();
		this.globalSectionController = globalSectionController;
		this.detailSectionPanel_ID = globalSectionController.getDetailSectionController().getMainPanel().getId();
		//this.globalMessageTicket = new GlobalMessageTicket();
		this.messageID = messageID;
	
		setupGrid();
		search();
	}

	private void setupToolbar(){
		Toolbar adminToolbar = new Toolbar();

		ToolbarButton uploadButton = new ToolbarButton("Upload");
		uploadButton.setIconCls("upload-icon");
		uploadButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				showImportWindow();
			}
		});

		ToolbarButton deleteButton = new ToolbarButton("Delete");
		deleteButton.setIconCls("remove-button-icon");
		deleteButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				beforeDelete();
			}
		});

		ToolbarButton updateButton = new ToolbarButton("Save");
		updateButton.setIconCls("save-button-icon");
		updateButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				updateRecords();
			}
		});

		adminToolbar.addButton(uploadButton);
		adminToolbar.addSeparator();
		adminToolbar.addButton(deleteButton);
		adminToolbar.addSeparator();
		adminToolbar.addButton(updateButton);
		adminToolbar.addSeparator();

		setTopToolbar(adminToolbar);
	}
	
	private void setupGrid() {
		setBorder(false);
		setFrame(false);
		setPaddings(0);
		setAutoScroll(true);
		setView(new CustomizedGridView());
		
		setupToolbar();
		
		selectionModel = new CheckboxSelectionModel();
		
		//Column headers	
		CheckboxColumnConfig checkBoxColConfig = new CheckboxColumnConfig(selectionModel);
		
		ColumnConfig sequenceNoColConfig = new ColumnConfig("Sequence No.",SEQUENCE_NO_RECORD_NAME,100,true);
		Field seqNoField = FieldFactory.createPositiveNumberField(0);
		sequenceNoColConfig.setEditor(new GridEditor(seqNoField));
		
		ColumnConfig fileNameColConfig = new ColumnConfig("File Name", FILENAME_RECORD_NAME, 250, true);
		ColumnConfig docTypeColConfig = new ColumnConfig("Document Type", DOC_TYPE_RECORD_NAME, 100,true);
		docTypeColConfig.setEditor(new GridEditor(new TextField()));
		
		ColumnConfig attachmentColConfig = new ColumnConfig("Attachment", MSG_BOARD_ATTACHMENT_ID_RECORD_NAME ,100,true);
		attachmentColConfig.setRenderer(new Renderer() {
			public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
				if(MessageBoardAttachment.IMAGE_DOC_TYPE.equals(record.getAsString(DOC_TYPE_RECORD_NAME)))
					return "<img src=\""+ATTACHMENT_IMAGE+"attachment.gif"+ "\"/>";
				else
					return "";
			}  
		});  
		
		BaseColumnConfig[] columns = new BaseColumnConfig[] {
			checkBoxColConfig,
			sequenceNoColConfig,
			fileNameColConfig,
			docTypeColConfig,
			attachmentColConfig
		}; 
		setColumnModel(new ColumnModel(columns));

		setSelectionModel(selectionModel);
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(recordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		setStore(dataStore);
		
		addGridCellListener(new GridCellListenerAdapter(){
			public void onCellClick(GridPanel grid, int rowIndex, int colindex, EventObject e) {
				if(colindex==4)	//Attachment Column
					showImageWindow(attachmentList, rowIndex);
			}
		});
	}
	
	private void search(){
		UIUtil.maskPanelById(detailSectionPanel_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMessageBoardAttachmentRepository().obtainAttachmentListByID(Long.valueOf(messageID), new AsyncCallback<List<MessageBoardAttachment>>() {
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(detailSectionPanel_ID);
			}
			public void onSuccess(List<MessageBoardAttachment> attachmentList) {
				populateGrid(attachmentList);
				UIUtil.unmaskPanelById(detailSectionPanel_ID);
			}
		});
	}
	
	private void populateGrid(List<MessageBoardAttachment> attachmentList){
		dataStore.removeAll();
		
		if(attachmentList == null || attachmentList.size() == 0)
			return;
		
		for(MessageBoardAttachment attachment: attachmentList){
			Record record = recordDef.createRecord(new Object[]{
				attachment.getId(),
				attachment.getSequenceNo(),
				attachment.getFilename(),
				attachment.getDocType()
			});
			dataStore.add(record);
		}
		
		this.attachmentList = attachmentList;
	}

	public void showImageWindow(List<MessageBoardAttachment> attachmentList, int currentImageIndex) {
		Window window = new MessageBoardAttachmentWindow(globalSectionController, attachmentList, currentImageIndex);
		window.show();
	}

	private void updateRecords() {
		Record[] recordList = dataStore.getModifiedRecords();

		List<MessageBoardAttachment> attachmentList = new ArrayList<MessageBoardAttachment>();

		for (Record record : recordList) {
			MessageBoardAttachment attachment = attachmentRecord(record);
			if(attachment==null) 
				return;
			attachmentList.add(attachment);
		}
		if (attachmentList.size() == 0) {
			MessageBox.alert("No data has been changed.");
			return;
		}

		UIUtil.maskPanelById(detailSectionPanel_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMessageBoardAttachmentRepository().updateAttachments(attachmentList, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(detailSectionPanel_ID);
			}
			public void onSuccess(Boolean updated) {
				if(updated){
					MessageBox.alert("Attachments have been updated successfully.");
					refresh();
				}
				else{
					MessageBox.alert("No record has been changed.");
					refresh();
				}
				UIUtil.unmaskPanelById(detailSectionPanel_ID);
			}
		});
	}

	private MessageBoardAttachment attachmentRecord(Record record) {
		MessageBoardAttachment attachment = new MessageBoardAttachment();

		attachment.setId(Long.valueOf(record.getAsInteger(MSG_BOARD_ATTACHMENT_ID_RECORD_NAME)));
		attachment.setSequenceNo(Integer.valueOf(record.getAsString(SEQUENCE_NO_RECORD_NAME)));
		attachment.setDocType(record.getAsString(DOC_TYPE_RECORD_NAME));

		return attachment;
	}
	
	private void beforeDelete(){
		if(selectionModel.getSelections().length == 0){
			MessageBox.alert("Please select a row to delete.");
			return;
		}
		MessageBox.confirm("Message Board", "The selected rows will be deleted. Confirm? ",
				new MessageBox.ConfirmCallback() {
			public void execute(String btnID) {
				if (btnID.equals("yes")){
					deleteRecord(selectionModel.getSelections());
				}
			}
		});
	}
	
	private void deleteRecord(Record[] records){
		List<Long> attachmentIDList = new ArrayList<Long>();
		for (Record record: records)
			attachmentIDList.add((long) record.getAsInteger(MSG_BOARD_ATTACHMENT_ID_RECORD_NAME));
		if (attachmentIDList.size() == 0)
			return;

		UIUtil.maskPanelById(detailSectionPanel_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMessageBoardAttachmentRepository().deleteAttachments(attachmentIDList, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(detailSectionPanel_ID);
			}
			public void onSuccess(Boolean deleted) {
				if(deleted){
					MessageBox.alert("Record has been deleted successfully.");
					refresh();
				}
				else{
					MessageBox.alert("No record has been changed.");
				}
				UIUtil.unmaskPanelById(detailSectionPanel_ID);
			}
		});
	}

	private void showImportWindow(){
		final Window window = new Window();
		window.setLayout(new RowLayout());
		window.setTitle("Upload Attachment");
		window.setClosable(false);
		window.setModal(true);
		window.setHeight(130);
		window.setWidth(400);
		
		final FormPanel uploadPanel = new FormPanel();
		uploadPanel.setHeight(120);
		uploadPanel.setPaddings(2,2,2,2);
		uploadPanel.setFileUpload(true);
		uploadPanel.setLayout(new TableLayout(3));

		Label fileLabel = new Label("File");
		fileLabel.setCls("table-cell");
		uploadPanel.add(fileLabel);
		
		final TextField fileTextField = new TextField("File", "file");
		fileTextField.setHideLabel(true);
		fileTextField.setWidth(350);
		fileTextField.setInputType("file");
		uploadPanel.add(fileTextField);

		final Hidden messageBoardIDHiddenField = new Hidden("messageBoardID", messageID);
		uploadPanel.add(messageBoardIDHiddenField);
		
		Panel emptyPanel0 = new Panel();
		emptyPanel0.setBorder(false);
		emptyPanel0.setHeight(10);
		uploadPanel.add(emptyPanel0, new TableLayoutData(3));
		
		final Hidden docTypeHiddenField = new Hidden("docType", MessageBoardAttachment.IMAGE_DOC_TYPE);
		uploadPanel.add(docTypeHiddenField);
		
		Checkbox imageCheckBox = new Checkbox("Image: .jpg, .png, .gif, .ico, .bmp");
		imageCheckBox.setChecked(true);
		imageCheckBox.setCtCls("table-cell");
		uploadPanel.add(imageCheckBox);
		imageCheckBox.addListener(new CheckboxListenerAdapter(){
			public void onCheck(Checkbox field, boolean checked) {
				if(checked)
					docTypeHiddenField.setValue(MessageBoardAttachment.IMAGE_DOC_TYPE);
				else
					docTypeHiddenField.setValue("");
			}
		});
		
		uploadPanel.addFormListener(new FormListenerAdapter() {
			public void onActionComplete(Form form, int httpStatus, String responseText) {
				uploadLocationsResponseCallback(responseText);
			}

			public void onActionFailed(Form form, int httpStatus, String responseText) {
				uploadLocationsResponseCallback(responseText);
			}
		});
		
		
		
		final Button importButton = new Button("Import");
		importButton.setIconCls("upload-icon");
		importButton.setVisible(true);
		importButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				if(fileTextField.getValueAsString().length()>0){
					uploadPanel.getForm().submit(GlobalParameter.IMAGE_UPLOAD_URL, null, Connection.POST, "Importing...", false);
				}else {
					MessageBox.alert("Please select a file to upload.");
				}
			}
		});

		final Button closeButton = new Button("Close");
		closeButton.setIconCls("cancel-icon");
		closeButton.setVisible(true);
		closeButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				window.close();
			}
		});

		
		window.add(uploadPanel);
		window.addButton(importButton);
		window.addButton(closeButton);
		window.show();
	}
	
	private void uploadLocationsResponseCallback(String responseText) {
		JSONValue jsonValue = JSONParser.parse(responseText);
		JSONObject jsonObj = jsonValue.isObject();
		
		if (jsonObj.get("success").isBoolean().booleanValue()) {
			MessageBox.alert("Attachment is uploaded successfully.");
			refresh();
		} else {
			String error = jsonObj.get("error").isString().toString();

			MessageBox.alert("Upload Failed: " + error);
		}
	}
	
	private void refresh(){
		dataStore.removeAll();
		search();
		((MessageBoardMainPanel)globalSectionController.getMainSectionController().getContentPanel()).refresh();
	}
	
}
