package com.gammon.qs.client.ui.window;

import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.AbstractAttachment;
import com.gammon.qs.domain.SCAttachment;
import com.gammon.qs.shared.GlobalParameter;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.HtmlEditor;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.RowSelectionModel;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;

public class ShowAttachmentWindow extends Window {

	private static final String RESULT_PANEL_ID ="attachmentPanel"; 
	private static final String MAINPANEL_ID ="attachmentContentTextArea"; 

	//UI
	private GlobalSectionController globalSectionController;
	private GlobalMessageTicket globalMessageTicket;
	private Panel mainPanel;
	private Panel searchPanel;
	private Panel resultPanel;
	private GridPanel resultGridPanel;

	private TextField subcontractNumberTextField;
	private TextField jobNumberTextField;
	private TextField vendorNumberTextField;
	private HtmlEditor contentTextEditor = new HtmlEditor();
	private TextArea contentTextEditorTextBox = new TextArea();
	private TextField addtionalInfoTextField = new TextField();

	//data store
	private Store dataStore;

	//variables
	private Integer recordSize = 0;
	private String nameObject = null;
	private Integer sequenceNumber = null;
	private String documentType = null;
	@SuppressWarnings("unused")
	private String fileName = null;
	private String textKey = null;
	private String currentUsingWindow = null;

	private Button addTextButton;
	private Button addAttachmentButton;
	private Button saveTextButton;
	private Button delButton;
	
	private Integer maxSequenceNumber ;
	
	private final RecordDef scAttachmentRecordDef = new RecordDef(
			new FieldDef[] {
					new StringFieldDef("sequenceNo"),
					new StringFieldDef("documentType"),
					new StringFieldDef("fileName")
			}
	);
	

	public ShowAttachmentWindow(final GlobalSectionController globalSectionController, final String storePlace, String subcontractNumber, final String currentUsingWindow){
		super();
		this.currentUsingWindow = currentUsingWindow;
		this.globalSectionController=globalSectionController;
		globalMessageTicket = new GlobalMessageTicket();

		if (storePlace.equals("Attachment_Subcontract"))
			nameObject = AbstractAttachment.SCPackageNameObject;
		else if (storePlace.equals("Attachment_SCDetail"))
			nameObject = AbstractAttachment.SCDetailsNameObject;
		else if (storePlace.equals("Attachment_Payment"))
			nameObject = AbstractAttachment.SCPaymentNameObject;
		else if (storePlace.equals("Attachment_Vendor"))
			nameObject = AbstractAttachment.VendorNameObject;
		
		
		this.setTitle("Attachment");
		this.setPaddings(5);
		this.setWidth(1024);
		this.setHeight(650);
		this.setClosable(false);
		this.setLayout(new FitLayout());

		this.mainPanel = new Panel();
		this.mainPanel.setLayout(new RowLayout());
		mainPanel.setId(MAINPANEL_ID);

		//search Panel
		this.searchPanel = new Panel();
		this.searchPanel.setPaddings(2);
		this.searchPanel.setFrame(true);
		this.searchPanel.setHeight("200");
		TableLayout searchPanelLayout = new TableLayout(6);		
		this.searchPanel.setLayout(searchPanelLayout);
		this.contentTextEditor.enable();
		contentTextEditor.setReadOnly(false);
		contentTextEditorTextBox.enable();

		addTextButton = new Button("Add Text");
		addTextButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				globalMessageTicket.refresh();
				Record curRecord = null;
				maxSequenceNumber = 0;
				if (dataStore!=null && dataStore.getCount()!=0) {
					curRecord = dataStore.getAt(dataStore.getCount()-1);
					documentType = (curRecord.getAsObject("documentType")!=null? curRecord.getAsString("documentType"):"");
					maxSequenceNumber = (curRecord.getAsObject("sequenceNo")!=null? curRecord.getAsInteger("sequenceNo"):0);
				} 

				UIUtil.maskPanelById(MAINPANEL_ID, "Loading...", true);
				
				addText((maxSequenceNumber+1), " ", "Text Attachment Added");
			};
		});
		addTextButton.setCls("table-cell");

		addAttachmentButton = new Button("Add File");
		addAttachmentButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				globalMessageTicket.refresh();
				Record curRecord = null;
				maxSequenceNumber = 0;
				if (dataStore!=null && dataStore.getCount()!=0) {
					curRecord = dataStore.getAt(dataStore.getCount()-1);
					documentType = (curRecord.getAsObject("documentType")!=null? curRecord.getAsString("documentType"):"");
					maxSequenceNumber = (curRecord.getAsObject("sequenceNo")!=null? curRecord.getAsInteger("sequenceNo"):0);
				}
				promptAddNewAttachmentWindow((maxSequenceNumber+1)+"");
				//Scroll to bottom in grid
				resultGridPanel.getView().focusRow(resultGridPanel.getStore().getCount() - 1);
				resultGridPanel.getSelectionModel().selectLastRow();
			};
		});
		
		addAttachmentButton.setCls("table-cell");

		saveTextButton = new Button("Save");
		saveTextButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				globalMessageTicket.refresh();
				if (SCAttachment.VendorNameObject.equals(nameObject))
					saveText(sequenceNumber, contentTextEditorTextBox.getText());
				else
					saveText(sequenceNumber, contentTextEditor.getValueAsString());
			};
		});
		saveTextButton.setCls("table-cell");

		delButton = new Button("Delete");
		delButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				globalMessageTicket.refresh();
				UIUtil.maskPanelById(MAINPANEL_ID, "Loading...", true);
				//if(documentType.equals("Text")) {
					delAttachment();
				//} else {
					//delAttachment(globalSectionController.getJob().getJobNumber() , new Integer(subcontractNumberTextField.getText().trim().trim()), new Integer(sequenceNumber.trim()));
				//}
			};

		});
		
		delButton.setCls("table-cell");

		ToolTip addTextToolTip = new ToolTip();
		addTextToolTip.setTitle("Add Text");
		addTextToolTip.setHtml("Add Text as an Attachment");
		addTextToolTip.setDismissDelay(15000);
		addTextToolTip.setWidth(200);
		addTextToolTip.setTrackMouse(true);
		addTextToolTip.applyTo(addTextButton);

		ToolTip addAttachmentToolTip = new ToolTip();
		addAttachmentToolTip.setTitle("Add File");
		addAttachmentToolTip.setHtml("Upload a file as an Attachment");
		addAttachmentToolTip.setDismissDelay(15000);
		addAttachmentToolTip.setWidth(200);
		addAttachmentToolTip.setTrackMouse(true);
		addAttachmentToolTip.applyTo(addAttachmentButton);

		ToolTip saveTextToolTip = new ToolTip();
		saveTextToolTip.setTitle("Save Text");
		saveTextToolTip.setHtml("Save Text in a Text Attachment");
		saveTextToolTip.setDismissDelay(15000);
		saveTextToolTip.setWidth(200);
		saveTextToolTip.setTrackMouse(true);
		saveTextToolTip.applyTo(saveTextButton);

		ToolTip delToolTip = new ToolTip();
		delToolTip.setTitle("Delete Text/File");
		delToolTip.setHtml("Delete a Text/File Attachment");
		delToolTip.setDismissDelay(15000);
		delToolTip.setWidth(200);
		delToolTip.setTrackMouse(true);
		delToolTip.applyTo(delButton);
		
		this.setTopToolbar(
				new Button[]{	
						addTextButton,
						addAttachmentButton,
						saveTextButton,
						delButton
				});


		if (storePlace.equals("Attachment_Vendor")){
			Label vendorNoLabel = new Label("Vendor Number");
			vendorNoLabel.setCls("table-cell");
			this.searchPanel.add(vendorNoLabel);
			this.vendorNumberTextField = new TextField("Vendor Number", "vendorNumber", 150);
			this.vendorNumberTextField.setCtCls("table-cell");
			this.vendorNumberTextField.setValue(subcontractNumber);
			this.vendorNumberTextField.disable();
			this.searchPanel.add(vendorNumberTextField);		
			
		}else{
			Label jobNumberLabel = new Label("Job Number");
			jobNumberLabel.setCls("table-cell");
			this.searchPanel.add(jobNumberLabel);
			this.jobNumberTextField =new TextField("Job Number","jobNumber",150);
			this.jobNumberTextField.setCtCls("table-cell");
			this.jobNumberTextField.setValue(globalSectionController.getJob().getJobNumber());
			this.jobNumberTextField.disable();
			this.searchPanel.add(jobNumberTextField);
	
			Label bqDescriptionLabel = new Label("Subcontract Number");
			bqDescriptionLabel.setCls("table-cell");
			this.searchPanel.add(bqDescriptionLabel);
			this.subcontractNumberTextField = new TextField("Subcontract Number", "subcontractNumber", 150);
			this.subcontractNumberTextField.setCtCls("table-cell");
			this.subcontractNumberTextField.setValue(subcontractNumber);
			this.subcontractNumberTextField.disable();
			this.searchPanel.add(subcontractNumberTextField);		
		}
		Label additionalInfo = null;
		if(storePlace.equals("Attachment_Payment")){
			this.addtionalInfoTextField = new TextField("Payment Cert Number", "certNo", 150);
			additionalInfo = new Label("Payment Cert Number");
		}else if (storePlace.equals("Attachment_SCDetail")){ 
			this.addtionalInfoTextField = new TextField("SCDetails Sequence Number", "scDetailSequenceNo", 150);
			additionalInfo = new Label("SCDetails Sequence Number");
		}
		if(additionalInfo != null){
			additionalInfo.setCls("table-cell");
			this.addtionalInfoTextField.setCls("table-cell");
			this.addtionalInfoTextField.disable();
			this.searchPanel.add(additionalInfo);
			this.searchPanel.add(addtionalInfoTextField);
		}

		this.mainPanel.add(searchPanel);

		//Grid Panel 
		resultPanel = new Panel();
		resultPanel.setId(RESULT_PANEL_ID);
		this.resultPanel.setHeight(180);
		this.resultPanel.setBorder(true);
		this.resultPanel.setFrame(true);
		this.resultPanel.setPaddings(5);
		this.resultPanel.setAutoScroll(true);

		resultPanel.setLayout(new FitLayout());
		resultGridPanel =  new GridPanel();
		resultGridPanel.setSelectionModel(new RowSelectionModel());
		
		ColumnConfig sequenceNoColumn = new ColumnConfig("Seq. No", "sequenceNo", 130 , true);
		//ColumnConfig documentTypeColumn = new ColumnConfig("Document Type", "documentType", 130 , true);
		ColumnConfig fileNameColumn = new ColumnConfig("File Name", "fileName", 300 , true);

		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(scAttachmentRecordDef);
		this.dataStore = new Store(proxy, reader);
		this.dataStore.load();
		resultGridPanel.setStore(this.dataStore);


		ColumnConfig[] columns = new ColumnConfig[] {
				sequenceNoColumn,
				//documentTypeColumn,
				fileNameColumn
		};

		resultGridPanel.setColumnModel(new ColumnModel(columns));
		
		
		GridView view = new GridView();
		view.setAutoFill(false);
		view.setForceFit(false);
		resultGridPanel.setView(view);
		contentTextEditor.setWidth(1024);
		contentTextEditor.setHeight(320);
		contentTextEditorTextBox.setWidth("1024");
		contentTextEditorTextBox.setHeight("320");
		
		resultGridPanel.addGridRowListener(new GridRowListenerAdapter(){

			public void onRowClick(GridPanel grid, int rowIndex, EventObject e)
			{	
				globalMessageTicket.refresh();
				UIUtil.maskPanelById(RESULT_PANEL_ID, "Loading", true);
				Record curRecord = dataStore.getAt(rowIndex);
				sequenceNumber = (curRecord.getAsObject("sequenceNo")!=null? curRecord.getAsInteger("sequenceNo") : Integer.valueOf(0));
				documentType = (curRecord.getAsObject("documentType")!=null? curRecord.getAsString("documentType"):"");
				loadRowAttachmentContent(documentType, sequenceNumber);
			}
			public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e) {
				globalMessageTicket.refresh();
				Record curRecord = dataStore.getAt(rowIndex);
				sequenceNumber = (curRecord.getAsObject("sequenceNo")!=null? curRecord.getAsInteger("sequenceNo") : Integer.valueOf(0));
				fileName = (curRecord.getAsObject("fileName")!=null? curRecord.getAsString("fileName"):"");
				String docType = (curRecord.getAsObject("documentType")!=null? curRecord.getAsString("documentType"):"");
				if (!docType.trim().equals("Text")) {
					com.google.gwt.user.client.Window.open(GlobalParameter.DOWNLOAD_SC_ATTACHMENT_URL+"?nameObject="+nameObject+"&textKey="+textKey+"&sequenceNo="+sequenceNumber.toString(), "_blank", "");			
				}
			}
		});

		resultPanel.add(resultGridPanel);

		this.mainPanel.add(resultPanel);
		
		if (SCAttachment.VendorNameObject.equals(nameObject))
			mainPanel.add(contentTextEditorTextBox);
		else
			mainPanel.add(contentTextEditor);

		this.add(this.mainPanel);

		Button closeWindowButton = new Button("Close");
		closeWindowButton.addListener(new ButtonListenerAdapter(){ 
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				if (currentUsingWindow.equals("p")||currentUsingWindow.equals("a")||currentUsingWindow.equals("v"))
					globalSectionController.closePromptWindow();
				else
					globalSectionController.closeCurrentWindow();				
			};
		});		

		this.addButton(closeWindowButton);
	}
	
	/**
	 * 
	 * @author tikywong
	 * Jun 23, 2011 9:37:46 AM
	 * 
	 * nameObject:
	 * - AbstractAttachment (SCPackageNameObject, SCPaymentNameObject, SCDetailsNameObject, TenderAnalysisNameObject, VendorNameObject)
	 * 
	 * parent window:
	 * a - calls addendumEnquiryAddFileWindow - For SCAddendumEnquiryWindow
	 * v - calls addendumEnquiryAddFileWindow (no subcontract number)- For AddressBookDetailsWindow
	 * p - calls promptWindow - For AddNewAttachmentWindow
	 * c - calls promptWindow - For PaymentDetailGridPanel, SCDetailDetailPanel, PackageEditorGridPanel
	 */
	public void promptAddNewAttachmentWindow(String sequenceNumber) {
		if (currentUsingWindow.equals("a"))
			globalSectionController.showAddNewAttachmentWindow(this, nameObject, textKey, subcontractNumberTextField.getText().trim().trim(), sequenceNumber, "a");
		else if (currentUsingWindow.equals("p")||currentUsingWindow.equals("c"))
			globalSectionController.showAddNewAttachmentWindow(this, nameObject, textKey, subcontractNumberTextField.getText().trim().trim(), sequenceNumber, "p");
		else if (currentUsingWindow.equals("v"))
			globalSectionController.showAddNewAttachmentWindow(this, nameObject, textKey, "", sequenceNumber, "v");
	};

	public void loadAttachmentList(String textKey){
		this.textKey = textKey;
		String splittedTextKey[] = textKey.split("\\|");
		this.addtionalInfoTextField.setValue(splittedTextKey[2]);
		UIUtil.maskPanelById(RESULT_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getAttachmentRepository().getAttachmentList(nameObject, textKey, new AsyncCallback<List<? extends AbstractAttachment>>(){
			public void onSuccess(List<? extends AbstractAttachment> result) {
				populateGrid(result);
				UIUtil.unmaskPanelById(RESULT_PANEL_ID);
				
				//Scroll to bottom in grid
				if (result.size()>0){
					resultGridPanel.getView().focusRow(resultGridPanel.getStore().getCount() - 1);
					resultGridPanel.getSelectionModel().selectLastRow();
				}
				Record curRecord = null;
				maxSequenceNumber = 0;
				if (dataStore.getCount()!=0) {
					curRecord = dataStore.getAt(dataStore.getCount()-1);
					documentType = (curRecord.getAsObject("documentType")!=null? curRecord.getAsString("documentType"):"");
					maxSequenceNumber = (curRecord.getAsObject("sequenceNo")!=null? curRecord.getAsInteger("sequenceNo"):0);
				} 
				if(result.size()>0)
					loadRowAttachmentContent(documentType, maxSequenceNumber);
				sequenceNumber = maxSequenceNumber;
			}

			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
				UIUtil.unmaskPanelById(RESULT_PANEL_ID);
			}
		});
	}

	private void saveText(Integer sequenceNo, String textContent){
		if (documentType.trim().equals("Text")){
			UIUtil.maskPanelById(MAINPANEL_ID, "Loading...", true);	 	
			SessionTimeoutCheck.renewSessionTimer();
			globalSectionController.getAttachmentRepository().uploadTextAttachment(nameObject, textKey, sequenceNo, textContent, globalSectionController.getUser().getUsername(),  new AsyncCallback<Boolean>(){
				public void onSuccess(Boolean result) {
					MessageBox.alert("Saved Success.");
					UIUtil.unmaskPanelById(MAINPANEL_ID);
				}

				public void onFailure(Throwable e) {
					UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
					UIUtil.unmaskPanelById(MAINPANEL_ID);
				}
			});

		} 
	}	

	private void addText(Integer sequenceNo, String textContent, final String alertMessage){
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getAttachmentRepository().uploadTextAttachment(nameObject, textKey, sequenceNo, textContent, globalSectionController.getUser().getUsername(),  new AsyncCallback<Boolean>(){
			public void onSuccess(Boolean result) {
				loadAttachmentList(textKey);
				MessageBox.alert(alertMessage);
				UIUtil.unmaskPanelById(MAINPANEL_ID);
			}

			public void onFailure(Throwable e) {				
				UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
				UIUtil.unmaskPanelById(MAINPANEL_ID);
			}

		});	


	}	

	private void delAttachment(){
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getAttachmentRepository().deleteAttachment(nameObject, textKey, sequenceNumber, new AsyncCallback<Boolean>(){
			public void onSuccess(Boolean result) {
				if (result){
					MessageBox.alert("Attachment Deleted.");
				} else {
					MessageBox.alert("Delete Failed");
				}
				if (contentTextEditor!=null)
					contentTextEditor.setValue("");
				loadAttachmentList(textKey);

				UIUtil.unmaskPanelById(MAINPANEL_ID);
			}

			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
				UIUtil.unmaskPanelById(RESULT_PANEL_ID);
			}
		});
	}	

	public void populateGrid(List<? extends AbstractAttachment> Attachment)	{
		if (Attachment != null && Attachment.size()>0){
			this.dataStore.removeAll();	
			this.recordSize = Attachment.size();
			Record[] data = new Record[this.recordSize];

			for(int i=0; i < Attachment.size();  i++ ){
				try{
					data[i] = this.scAttachmentRecordDef.createRecord(
							new Object[]{
									Attachment.get(i).getSequenceNo(),
									(Attachment.get(i).getDocumentType()==0? "Text":"File"),
									Attachment.get(i).getFileName()
							});
				}catch (Exception e){				
					UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
				}
			}
			this.dataStore.add(data);
			delButton.enable();
		}
		else{
			if (dataStore!=null)
				dataStore.removeAll();

			//contentTextEditor.disable();
			//contentTextEditor.setReadOnly(true);
			contentTextEditor.setDisabled(true);
			contentTextEditorTextBox.disable();
			saveTextButton.disable();
			delButton.disable();
		}
	}
	
	public void loadRowAttachmentContent(String documentType, Integer sequenceNumber){
		if(documentType.equals("Text")) {
			saveTextButton.enable();
//			contentTextEditor.enable();
			//contentTextEditor.setReadOnly(false);
			contentTextEditor.setDisabled(false);
			SessionTimeoutCheck.renewSessionTimer();
			globalSectionController.getAttachmentRepository().getTextAttachmentContent(nameObject, textKey, sequenceNumber, new AsyncCallback<String>(){
				public void onSuccess(String result) {
					if("".equals(result))
						MessageBox.alert("Download Error");
					else{
						if (SCAttachment.VendorNameObject.equals(nameObject)){
							contentTextEditorTextBox.enable();
							contentTextEditorTextBox.setValue(result);
						}else{
//							contentTextEditor.enable();
							//contentTextEditor.setReadOnly(false);
							contentTextEditor.setDisabled(false);
							contentTextEditor.setValue(result);
						}
					}
					UIUtil.unmaskPanelById(RESULT_PANEL_ID);
				}

				public void onFailure(Throwable e) {
					UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
					UIUtil.unmaskPanelById(RESULT_PANEL_ID);
				}
			});

		} else {
			saveTextButton.disable();
			UIUtil.unmaskPanelById(RESULT_PANEL_ID);
			if (SCAttachment.VendorNameObject.equals(nameObject)){
				contentTextEditorTextBox.setValue("\n\nPlease double click a row to download an attachment");
				contentTextEditorTextBox.disable();				
			}else{
				contentTextEditor.setValue("\n\nPlease double click a row to download an attachment");
				//contentTextEditor.disable();
				//contentTextEditor.setReadOnly(true);
				contentTextEditor.setDisabled(true); 
			}
		}
	}
}
