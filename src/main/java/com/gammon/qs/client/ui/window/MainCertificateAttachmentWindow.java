/**
 * @author tikywong
 * created on January 18, 2012
 */
package com.gammon.qs.client.ui.window;

import java.util.Date;
import java.util.List;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.renderer.DateRenderer;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.MainCertificateAttachment;
import com.gammon.qs.domain.MainContractCertificate;
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

/**
 * @author tikywong
 * created on January 18, 2012
 */
public class MainCertificateAttachmentWindow extends Window {
	public static final String WINDOW_ID = "mainCertificateAttachmentWindow";
	
	private GlobalSectionController globalSectionController;
	private GlobalMessageTicket globalMessageTicket;
	
	private String jobNumber;
	private Integer mainCertNumber;
	
	private GridPanel gridPanel;
	private RecordDef attachmentRecordDef = new RecordDef(new FieldDef[]{	new StringFieldDef("sequenceNo"),
																			new DateFieldDef("dateAdded"),
																			new StringFieldDef("fileName"),
																			//Invisible record
																			new IntegerFieldDef("documentType")});
	private Store dataStore;
	
	private Panel textPanel;
	private HtmlEditor textEditor;
	
	private Button addTextButton;
	private Button addFileButton;
	private Button saveTextButton;
	private Button deleteButton;
	
	private Window uploadWindow;
	
	public MainCertificateAttachmentWindow(GlobalSectionController globalSectionController, String jobNumber, Integer mainCertNumber){
		super();
		
		this.globalSectionController = globalSectionController;
		this.globalMessageTicket = new GlobalMessageTicket();
		
		this.jobNumber = jobNumber;
		this.mainCertNumber = mainCertNumber;
		
		setupUI();
	}
	
	private void setupUI(){
		this.setId(WINDOW_ID);
		this.setTitle("Attachment for Main Certificate");
		this.setPaddings(5);
		this.setWidth(1024);
		this.setHeight(650);
		this.setClosable(false);
		
		setupTopToolbar();
		setupGridPanel();
		setupTextPanel();
		
		Button closeButton = new Button("Close");
		closeButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				globalSectionController.closeCurrentWindow();
			}
		});
		addButton(closeButton);
	}
	
	private void setupTopToolbar(){
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
				saveTextAttachment();
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
		
		setTopToolbar(new Button[]{addTextButton, addFileButton, saveTextButton, deleteButton});
	}
	
	private void setupGridPanel() {
		gridPanel = new GridPanel();
		
		boolean singleSelect = true;
		gridPanel.setSelectionModel(new RowSelectionModel(singleSelect));
		gridPanel.setHeight(250);
		gridPanel.setWidth(1024);
		
		//columns
		ColumnConfig sequenceNoColConfig = new ColumnConfig("Seq. No", "sequenceNo", 50 , false);
		ColumnConfig dateAddedColConfig = new ColumnConfig("Date Added", "dateAdded", 100, false);
		ColumnConfig fileNameColConfig = new ColumnConfig("File Name", "fileName", 300 , false);
		
		dateAddedColConfig.setRenderer(new DateRenderer());
		
		ColumnConfig[] columns = new ColumnConfig[]{sequenceNoColConfig,
													dateAddedColConfig,
													fileNameColConfig};
		gridPanel.setColumnModel(new ColumnModel(columns));
		
		//records
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(attachmentRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		gridPanel.setStore(dataStore);
		
		//grid view
		GridView view = new GridView();
		view.setAutoFill(false);
		view.setForceFit(false);
		gridPanel.setView(view);
		
		//listener
		gridPanel.addGridRowListener(new GridRowListenerAdapter(){
			public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
				globalMessageTicket.refresh();
				rowSelected(false);
			}

			public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e) {
				globalMessageTicket.refresh();
				rowSelected(true);
			}			
		});
		
		add(gridPanel);
		
		populateGridPanel();
	}
	
	private void setupTextPanel() {
		textPanel = new Panel();
		
		textPanel.setHeight(350);
		textPanel.setWidth(1024);
		textPanel.setLayout(new FitLayout());
		
		textEditor = new HtmlEditor();
		textEditor.setHeight(350);
		textEditor.setWidth(1024);
		textEditor.setValue("No Content");
		textEditor.setReadOnly(true);
		textEditor.setDisabled(true);
		textPanel.add(textEditor);
		
		add(textPanel);
	}
	
	private void rowSelected(boolean doubleClicked){
		Record record = gridPanel.getSelectionModel().getSelected();
		
		int documentType = record.getAsInteger("documentType");
		String sequenceNo = record.getAsString("sequenceNo");
		
		if(MainCertificateAttachment.TEXT.intValue()==documentType)
			getTextAttachment(sequenceNo);
		else{
			textEditor.setValue("Double click to download the attachment");
			textEditor.setDisabled(true);
			textEditor.setReadOnly(true);
			
			saveTextButton.disable();
			
			if(doubleClicked)
				com.google.gwt.user.client.Window.open(GlobalParameter.MAINCERTIFICATE_ATTACHMENT_DOWNLOAD_URL+"?jobNumber="+jobNumber+"&mainCertNumber="+mainCertNumber.toString()+"&sequenceNo="+sequenceNo, "_blank", "");
		}
	}
	
	private void populateGridPanel(){
		dataStore.removeAll();
		
		saveTextButton.disable();
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMainContractCertificateRepository().getMainContractCert(jobNumber, mainCertNumber, new AsyncCallback<MainContractCertificate>() {
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());		
			}

			public void onSuccess(MainContractCertificate mainCert) {
				if(mainCert!=null){
					UIUtil.maskMainPanel();
					try {
						globalSectionController.getMainCertificateAttachmentRepository().obtainMainCertificateAttachment(mainCert, new AsyncCallback<List<MainCertificateAttachment>>(){

							@Override
							public void onFailure(Throwable e) {
								UIUtil.unmaskMainPanel();
								UIUtil.throwException(e);
							}

							@Override
							public void onSuccess(List<MainCertificateAttachment> result) {
								//populate the grid
								for(MainCertificateAttachment attachment:result){
									Record record = attachmentRecordDef.createRecord(new Object[]{
											attachment.getSequenceNo(),
											attachment.getCreatedDate(),
											attachment.getFileName(),
											attachment.getDocumentType()}); 
									
									dataStore.add(record);
								}
								UIUtil.unmaskMainPanel();
							}
						});
					} catch (DatabaseOperationException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		
	}

	private void getTextAttachment(String sequenceNo){
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getAttachmentRepository().getMainCertTextAttachment(jobNumber, mainCertNumber, Integer.parseInt(sequenceNo), new AsyncCallback<String>(){
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());					
			}

			public void onSuccess(String text) {
				if(text!=null)
					textEditor.setValue(text);
				else
					textEditor.setValue("");
				
				textEditor.setDisabled(false);
				textEditor.setReadOnly(false);
				textEditor.focus();
			
				saveTextButton.enable();
			}
		});
	}
	
	private void addTextAttachment() {
		Record latestRecord = null;
		
		if(dataStore.getCount()!=0)
			latestRecord = dataStore.getAt(dataStore.getCount()-1);
		
		final Integer sNo = latestRecord==null? 1:(latestRecord.getAsInteger("sequenceNo")+1);
		MessageBox.prompt("Attachment Name", "Please input a name for this attachment", new MessageBox.PromptCallback(){
			
			public void execute(String btnID, final String fileName) {
				if(btnID.equals("ok")){
					SessionTimeoutCheck.renewSessionTimer();
					globalSectionController.getAttachmentRepository().addMainCertTextAttachment(jobNumber, mainCertNumber, sNo, fileName, new AsyncCallback<Boolean>() {
						public void onFailure(Throwable e) {
							UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
						}

						public void onSuccess(Boolean added) {
							addNewRecord(sNo, fileName, MainCertificateAttachment.TEXT);
						}
					});
				}
			}	
		});
	}
	
	private void addNewRecord(Integer sequenceNo, String fileName, Integer documentType) {
		Record record = attachmentRecordDef.createRecord(new Object[]{sequenceNo, new Date(), fileName, documentType});
		dataStore.add(record);
		gridPanel.getView().focusRow(dataStore.getCount()-1);
		gridPanel.getSelectionModel().selectLastRow();
		
		if(MainCertificateAttachment.TEXT.intValue()==documentType)
			rowSelected(false);
	}
	
	private void addFileAttachment() {
		Record latestRecord = null;
		
		if(dataStore.getCount()!=0)
			latestRecord = dataStore.getAt(dataStore.getCount()-1);
		
		final Integer sNo = latestRecord==null? 1:(latestRecord.getAsInteger("sequenceNo")+1);
		
		uploadWindow = new Window();
		uploadWindow.setLayout(new FitLayout());
		uploadWindow.setWidth(250);
		uploadWindow.setTitle("Upload attachment for Main Certificate");
		
		final FormPanel uploadPanel = new FormPanel();
		uploadPanel.setHeight(25);
		uploadPanel.setPaddings(0, 2, 2, 0);
		uploadPanel.setFileUpload(true);
		
		final TextField fileTextField = new TextField("File", "bytes");
		fileTextField.setHideLabel(true);
		fileTextField.setInputType("file");
		fileTextField.setAllowBlank(false);
		uploadPanel.add(fileTextField);
		
		final Hidden jobNumberField = new Hidden("jobNumber", jobNumber);
		uploadPanel.add(jobNumberField);
		final Hidden mainCertNumberField = new Hidden("mainCertNumber", mainCertNumber.toString());
		uploadPanel.add(mainCertNumberField);
		final Hidden sequenceNoField = new Hidden("sequenceNo", sNo.toString());
		uploadPanel.add(sequenceNoField);
		
		uploadPanel.addFormListener(new FormListenerAdapter(){
			public void onActionComplete(Form form, int httpStatus, String responseText) {
				JSONValue jsonValue = JSONParser.parse(responseText);
				JSONObject jsonObj = jsonValue.isObject();
				
				if (jsonObj.get("success").isBoolean().booleanValue()) {
					addNewRecord(sNo, jsonObj.get("fileName").isString().stringValue(), MainCertificateAttachment.FILE);
					uploadWindow.close();
				}
				else
					MessageBox.alert("Upload Failed");
			}

			public void onActionFailed(Form form, int httpStatus, String responseText) {
				MessageBox.alert("Upload Failed");
			}
		});
		
		//Buttons
		Button uploadButton = new Button("Upload");
		uploadButton.setCls("table-cell");
		uploadButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				uploadPanel.getForm().submit(GlobalParameter.MAINCERTIFICATE_ATTACHMENT_UPLOAD_URL, null, Connection.POST, "Uploading...", false);				
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
	
	private void saveTextAttachment() {
		UIUtil.maskPanelById(WINDOW_ID, "Saving text...", true);
		
		Record record = gridPanel.getSelectionModel().getSelected();
		if(record==null || record.getAsInteger("documentType")!=MainCertificateAttachment.TEXT.intValue())
			return;
		
		Integer sequenceNo = record.getAsInteger("sequenceNo");
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getAttachmentRepository().saveMainCertTextAttachment(jobNumber, mainCertNumber, sequenceNo, textEditor.getValueAsString(), new AsyncCallback<Boolean>() {
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(WINDOW_ID);
				UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
			}
			public void onSuccess(Boolean saved) {
				UIUtil.unmaskPanelById(WINDOW_ID);
				if(saved)
					MessageBox.alert("Saved");
				else
					MessageBox.alert("Failed: Text could not be saved.");
			}
		});
	}
	
	private void deleteAttachment() {
		final Record record = gridPanel.getSelectionModel().getSelected();
		if(record==null)
			return;
		
		MessageBox.confirm("Delete Attachment", "Are you sure you want to delete this attachment?", new MessageBox.ConfirmCallback(){
			public void execute(String btnID) {
				if(btnID.equals("yes")){
					UIUtil.maskPanelById(WINDOW_ID, "Deleting attachment...", true);
					
					String sequenceNo = record.getAsString("sequenceNo");
					SessionTimeoutCheck.renewSessionTimer();
					globalSectionController.getAttachmentRepository().deleteMainCertAttachment(jobNumber, mainCertNumber, Integer.parseInt(sequenceNo), new AsyncCallback<Boolean>() {
						public void onFailure(Throwable e) {
							UIUtil.unmaskPanelById(WINDOW_ID);
							UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
						}

						public void onSuccess(Boolean deleted) {
							UIUtil.unmaskPanelById(WINDOW_ID);
							if(deleted){
								textEditor.setValue("No Content");
								textEditor.setReadOnly(true);
								textEditor.setDisabled(true);
								
								MessageBox.alert("Attachment is Deleted.");
								dataStore.remove(record);
							}
							else
								MessageBox.alert("Attachment could not be deleted");
						}
					});
				}
				
			}
			
		});
		
	}
}
