package com.gammon.qs.client.ui.window;

import java.util.Date;
import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.renderer.DateRenderer;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.RepackagingAttachment;
import com.gammon.qs.shared.GlobalParameter;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.Connection;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Form;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.HtmlEditor;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FormListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.RowSelectionModel;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;

public class RepackagingAttachmentWindow extends Window {
	public static final String WINDOW_ID = "repackagingAttachmentWindow";
	
	private GlobalSectionController globalSectionController;
	private GlobalMessageTicket globalMessageTicket;
	private Long repackagingEntryID;
	
	private GridPanel attachmentGridPanel;
	private HtmlEditor textEditor;
	private Window uploadWindow;
	
	private Button addTextButton;
	private Button addFileButton;
	private Button saveTextButton;
	private Button deleteButton;
	
	private Store dataStore;
	private RecordDef attachmentRecordDef = new RecordDef(
			new FieldDef[]{
					new DateFieldDef("dateAdded"),
					new StringFieldDef("sequenceNo"),
					new StringFieldDef("fileName"),
					new IntegerFieldDef("documentType")
			});
	
	public RepackagingAttachmentWindow(GlobalSectionController globalSecController, Long repacakagingEntryID, Integer repackagingVersion){
		super();
		this.globalSectionController = globalSecController;
		this.repackagingEntryID = repacakagingEntryID;
		globalMessageTicket = new GlobalMessageTicket();

		this.setId(WINDOW_ID);
		this.setTitle("Attachments for Repackaging Version " + repackagingVersion.toString());
		this.setPaddings(5);
		this.setWidth(1024);
//		this.setHeight(650);
		this.setClosable(false);
		
		attachmentGridPanel = new GridPanel();
		attachmentGridPanel.setSelectionModel(new RowSelectionModel(true));
		attachmentGridPanel.setHeight(250);
		ColumnConfig sequenceNoColConfig = new ColumnConfig("Seq. No", "sequenceNo", 50 , false);
		ColumnConfig dateAddedColConfig = new ColumnConfig("Date Added", "dateAdded", 100, false);
		dateAddedColConfig.setRenderer(new DateRenderer());
		ColumnConfig fileNameColConfig = new ColumnConfig("File Name", "fileName", 300 , false);
		ColumnConfig[] columns = new ColumnConfig[]{
				sequenceNoColConfig,
				dateAddedColConfig,
				fileNameColConfig
		};
		attachmentGridPanel.setColumnModel(new ColumnModel(columns));
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(attachmentRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		attachmentGridPanel.setStore(dataStore);		
		GridView view = new GridView();
		view.setAutoFill(false);
		view.setForceFit(false);
		attachmentGridPanel.setView(view);
		
		Panel textPanel = new Panel();
		textPanel.setHeight(250);
		textPanel.setLayout(new FitLayout());
		textEditor = new HtmlEditor();
		textPanel.add(textEditor);
		
		this.add(attachmentGridPanel);
		this.add(textPanel);
		
		attachmentGridPanel.addGridRowListener(new GridRowListenerAdapter(){
			public void onRowClick(GridPanel grid, int rowIndex, EventObject e){
				globalMessageTicket.refresh();
				rowSelected(false);
			}
			public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e){
				rowSelected(true);
			}
		});
		
		addTextButton = new Button("Add Text");
		addTextButton.setTooltip("Add Text", "Create a new text attachment");
		addTextButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				globalMessageTicket.refresh();
				addTextAttachment();
			}
		});
		addFileButton = new Button("Add File");
		addFileButton.setTooltip("Add File", "Import a file as an attachment");
		addFileButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				globalMessageTicket.refresh();
				addFileAttachment();
			}
		});
		saveTextButton = new Button("Save Text");
		saveTextButton.setTooltip("Save Text", "Save changes made to the selected text attachment");
		saveTextButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				globalMessageTicket.refresh();
				saveText();
			}
		});
		deleteButton = new Button("Delete");
		deleteButton.setTooltip("Delete", "Delete the selected file or text attachment");
		deleteButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				globalMessageTicket.refresh();
				deleteAttachment();
			}
		});
		this.setTopToolbar(new Button[]{addTextButton, addFileButton, saveTextButton, deleteButton});
		
		Button closeButton = new Button("Close");
		closeButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				globalSectionController.closeCurrentWindow();
			}
		});
		this.addButton(closeButton);
	}
	
	public void rowSelected(boolean dbl){
		Record record = attachmentGridPanel.getSelectionModel().getSelected();
		int docType = record.getAsInteger("documentType");
		String sequenceNo = record.getAsString("sequenceNo");
		if(RepackagingAttachment.TEXT.intValue() == docType){
			getAttachmentText(Integer.valueOf(sequenceNo));
		}
		else{
			textEditor.setValue("Please double-click on the row to download the attachment");
			textEditor.disable();
			saveTextButton.disable();
			if(dbl){ //Double-clicked - download file
				com.google.gwt.user.client.Window.open(GlobalParameter.REPACKAGING_ATTACHMENT_DOWNLOAD_URL+"?repackagingEntryID="+repackagingEntryID.toString()+"&sequenceNo="+sequenceNo, "_blank", "");
			}
		}
	}
	
	public void getAttachmentText(Integer sequenceNo){
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getAttachmentRepository().getRepackagingTextAttachment(repackagingEntryID, sequenceNo, new AsyncCallback<String>(){
			public void onSuccess(String text) {
				if(text != null)
					textEditor.setValue(text);
				else
					textEditor.setValue("");
				saveTextButton.enable();
				textEditor.enable();
				textEditor.focus();
			}
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());	
			}
		});
	}
	
	public void addTextAttachment(){
		int seqNo = 1;
		if(dataStore.getCount() != 0){
			Record record = dataStore.getAt(dataStore.getCount() - 1);
			seqNo = record.getAsInteger("sequenceNo") + 1;
		}
		final Integer sequenceNo = Integer.valueOf(seqNo);
		MessageBox.prompt("Attachment Name", "Please input a name for this attachment", new MessageBox.PromptCallback(){
			public void execute(String btnID, final String text) {
				if(btnID.equals("ok")){
					SessionTimeoutCheck.renewSessionTimer();
					globalSectionController.getAttachmentRepository().addRepackagingTextAttachment(repackagingEntryID, sequenceNo, text, new AsyncCallback<Integer>(){
						public void onSuccess(Integer seq) {
							addNewRecord(seq, text, RepackagingAttachment.TEXT);
						}
						public void onFailure(Throwable e) {
//							UIUtil.alert(e.getMessage());
							UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
						}
					});
				}
			}			
		});
	}
	
	public void addFileAttachment(){
		int seqNo = 1;
		if(dataStore.getCount() != 0){
			Record record = dataStore.getAt(dataStore.getCount() - 1);
			seqNo = record.getAsInteger("sequenceNo") + 1;
		}
		final Integer sequenceNo = Integer.valueOf(seqNo);
		uploadWindow = new Window();
		uploadWindow.setLayout(new FitLayout());
		uploadWindow.setWidth(250);
		uploadWindow.setTitle("Import Vendor Feedback from Excel");
		
		final FormPanel uploadPanel = new FormPanel();
		uploadPanel.setHeight(25);
		uploadPanel.setPaddings(0, 2, 2, 0);
		uploadPanel.setFileUpload(true);
		final TextField fileTextField = new TextField("File", "bytes");
		fileTextField.setHideLabel(true);
		fileTextField.setInputType("file");
		fileTextField.setAllowBlank(false);
		uploadPanel.add(fileTextField);
		final Hidden repackagingEntryIDField = new Hidden("repackagingEntryID", repackagingEntryID.toString());
		uploadPanel.add(repackagingEntryIDField);
		final Hidden sequenceNoField = new Hidden("sequenceNo", sequenceNo.toString());
		uploadPanel.add(sequenceNoField);
		uploadPanel.addFormListener(new FormListenerAdapter() {
			public void onActionComplete(Form form, int httpStatus, String responseText) {
				JSONValue jsonValue = JSONParser.parse(responseText);
				JSONObject jsonObj = jsonValue.isObject();
				if (jsonObj.get("success").isBoolean().booleanValue()) {
					addNewRecord(sequenceNo, jsonObj.get("fileName").isString().stringValue(), RepackagingAttachment.FILE);
					uploadWindow.close();
				}
				else
					MessageBox.alert("Upload Failed");
			}
			
			public void onActionFailed(Form form, int httpStatus, String responseText) {
				MessageBox.alert("Upload Failed");
			}
		});
		Button uploadButton = new Button("Upload");
		uploadButton.setCls("table-cell");
		uploadButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				uploadPanel.getForm().submit(GlobalParameter.REPACKAGING_ATTACHMENT_UPLOAD_URL, null, Connection.POST, "Uploading...", false);				
			}});
		Button closeButton = new Button("Close");
		closeButton.setCls("table-cell");
		closeButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				uploadWindow.close();
			}});
		uploadWindow.setButtons(new Button[]{uploadButton, closeButton});
		uploadWindow.add(uploadPanel);
		uploadWindow.show();
	}
	
	public void addNewRecord(Integer sequenceNo, String fileName, Integer docType){
		Record record = attachmentRecordDef.createRecord(new Object[]{new Date(), sequenceNo, fileName, docType});
		dataStore.add(record);
		attachmentGridPanel.getView().focusRow(dataStore.getCount()-1);
		attachmentGridPanel.getSelectionModel().selectLastRow();
		rowSelected(false);
	}
	
	public void saveText(){
		UIUtil.maskPanelById(WINDOW_ID, "Saving text...", true);
		Record record = attachmentGridPanel.getSelectionModel().getSelected();
		if(record == null || record.getAsInteger("documentType") == RepackagingAttachment.FILE.intValue())
			return;
		String sequenceNo = record.getAsString("sequenceNo");
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getAttachmentRepository().saveRepackagingTextAttachment(repackagingEntryID, Integer.valueOf(sequenceNo), textEditor.getValueAsString(), new AsyncCallback<Boolean>(){
			public void onSuccess(Boolean success) {
				UIUtil.unmaskPanelById(WINDOW_ID);
				if(success)
					MessageBox.alert("Saved");
				else
					MessageBox.alert("Text could not be saved");
			}
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
			}			
		});
	}
	
	public void deleteAttachment(){
		final Record record = attachmentGridPanel.getSelectionModel().getSelected();
		if(record == null)
			return;
		MessageBox.confirm("Delete Attachment", "Are you sure you want to delete this attachment?", new MessageBox.ConfirmCallback(){
			public void execute(String btnID) {
				if(btnID.equals("yes")){
					UIUtil.maskPanelById(WINDOW_ID, "Deleting attachment...", true);
					String sequenceNo = record.getAsString("sequenceNo");
					SessionTimeoutCheck.renewSessionTimer();
					globalSectionController.getAttachmentRepository().deleteRepackagingAttachment(repackagingEntryID, Integer.valueOf(sequenceNo), new AsyncCallback<Boolean>(){
						public void onSuccess(Boolean success) {
							UIUtil.unmaskPanelById(WINDOW_ID);
							if(success){
								MessageBox.alert("Deleted");
								dataStore.remove(record);
							}
							else
								MessageBox.alert("Attachment could not be deleted");
						}
						public void onFailure(Throwable e) {
							UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());							
						}
					});
				}
			}			
		});
	}

	public void populateGrid(List<RepackagingAttachment> attachments){
		dataStore.removeAll();
		saveTextButton.disable();
		textEditor.disable();
		if(attachments == null || attachments.size() == 0)
			return;
		for(RepackagingAttachment attachment : attachments){
			Record record = attachmentRecordDef.createRecord(new Object[]{
					attachment.getCreatedDate(),
					attachment.getSequenceNo(),
					attachment.getFileName(),
					attachment.getDocumentType()
			});
			dataStore.add(record);
		}
	}
}
