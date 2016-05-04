package com.gammon.qs.client.ui.window.windowSection;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.WindowListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.repository.PackageRepositoryRemote;
import com.gammon.qs.client.repository.PackageRepositoryRemoteAsync;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemote;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.panel.StatusButtonPanel;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SystemConstant;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;

public class CreateSystemConstantWindow extends Window {
	
	public static final int WINDOW_WIDTH = 850;
	public static final int WINDOW_HEIGHT = 220;
	
	public static final int MAX_STATUS_LENGTH = 60;
	
	public static final int CREATE_BUTTON_WIDTH = 90;
	public static final int CLOSE_BUTTON_WIDTH = 90;
	
	public static final int SEARCH_COMPONENT_WIDTH = 190;
	
	/* these regexen are used to validate input for each of the fields */
	public static final String NUMBERS_REGEX = "^[0-9]+$";
	
	public static final String CREATE_SYSTEM_CONSTANT_FORM_PANEL_ID = "CreateSystemConstantFormPanel";
	
	private GlobalSectionController globalSectionController;
	
	private PackageRepositoryRemoteAsync packageRepository;
	
	private Panel formPanel;
	private StatusButtonPanel buttonPanel;
	
	private TextField scMaxRetentionPercentField = new NumberField();
	private TextField scInterimRetentionPercentField = new NumberField();
	private TextField scMOSRetentionPercentField = new NumberField();

	
	//private ArrayList<Field> inputFields;
	private Field[] inputFields = new Field[8];
	
	private ComboBox systemCodeComboBox;
	private ComboBox companyComboBox;
	private ComboBox scPaymentTermComboBox;
	private ComboBox retentionTypeComboBox;
	private ComboBox finQS0ReviewComboBox;
	
	private Label	systemCodeLabel 					= new Label("System Code :");
	private Label	companyLabel 						= new Label("Company :");
	private Label	scPaymentTermLabel 					= new Label("SC Payment Term :");
	private Label	scMaxRetentionPercentLabel 			= new Label("SC Max Retention % :");
	private Label	scInterimRetentionPercentLabel 		= new Label("SC Interim Retention % :");
	private Label	scMOSRetentionPercentLabel 			= new Label("SC MOS Retention % :");
	private Label	retentionTypeLabel 					= new Label("Retention Type :");
	private Label	finQS0ReviewLabel 					= new Label("Reviewed by Finance :");
	private ArrayList<Label> LabelFields;

	
	private ArrayList<String>					systemCodeOptionList					= new ArrayList<String>();
	private ArrayList<String>					companyCodeOptionList					= new ArrayList<String>();
	
	private Button createButton;
	private Button closeButton;
	
	private Boolean canWrite;

	private int loadingStatus = 0;
	private final int MAXLOADINGSTATUS = 2;
	
	public CreateSystemConstantWindow(GlobalSectionController globalSectionControllerArgument) {
		this.globalSectionController = globalSectionControllerArgument;
		
		packageRepository = (PackageRepositoryRemoteAsync) GWT.create(PackageRepositoryRemote.class);
		((ServiceDefTarget)packageRepository).setServiceEntryPoint(GlobalParameter.PACKAGE_REPOSITORY_URL);
		
		buildWindow();
		
		getPermissions();
	}
	
	private void buildWindow() {
		this.setModal(true);
		this.setLayout(new RowLayout());
		this.setClosable(false);
		this.setWidth(WINDOW_WIDTH);
		this.setHeight(WINDOW_HEIGHT);
		this.setTitle("Add new System Constant");
		constructForm();
		
		constructButtonPanel();		
		
		this.add(formPanel);
		this.add(buttonPanel);
		
		this.addListener( new WindowListenerAdapter() {
			public void onResize(Window source, int width, int height) {
				buttonPanel.resize();
			}	
		});
	}
	
	private void constructForm() {
		formPanel = new Panel();
		formPanel.setLayout(new TableLayout(4));
		formPanel.setFrame(true);
		formPanel.setPaddings(2);
		formPanel.setId(CREATE_SYSTEM_CONSTANT_FORM_PANEL_ID);
		
		systemCodeComboBox = FieldFactory.createComboBox();
		companyComboBox = FieldFactory.createComboBox();
		scPaymentTermComboBox = FieldFactory.createComboBox();
		retentionTypeComboBox = FieldFactory.createComboBox();
		finQS0ReviewComboBox = FieldFactory.createComboBox();

		LabelFields = new ArrayList<Label>();
		LabelFields.add(systemCodeLabel);
		LabelFields.add(companyLabel);
		LabelFields.add(scPaymentTermLabel);
		LabelFields.add(scMaxRetentionPercentLabel);
		LabelFields.add(scInterimRetentionPercentLabel);
		LabelFields.add(scMOSRetentionPercentLabel);
		LabelFields.add(retentionTypeLabel);
		LabelFields.add(finQS0ReviewLabel);

		inputFields[0] = systemCodeComboBox;
		inputFields[1] = companyComboBox;
		inputFields[2] = scPaymentTermComboBox;
		inputFields[3] = scMaxRetentionPercentField;
		inputFields[4] = scInterimRetentionPercentField;
		inputFields[5] = scMOSRetentionPercentField;
		inputFields[6] = retentionTypeComboBox;
		inputFields[7] = finQS0ReviewComboBox;
		
		for(int i = 0; i < 8 ; i++){
			Label tempLabel = LabelFields.get(i);
			//Field tempField = inputFields.get(i);
			Field tempField = inputFields[i];
			formPanel.add(tempLabel);
			tempLabel.setWidth(SEARCH_COMPONENT_WIDTH);
			tempLabel.setCtCls("table-cell");
			
			formPanel.add(tempField);
			tempField.setWidth(SEARCH_COMPONENT_WIDTH);
			tempField.setCtCls("table-cell");
			
		}
		
		@SuppressWarnings("unused")
		String[][] valuePair;

		/* set option to system code type combobox */
		queryForComboBoxOptions(systemCodeOptionList, SystemConstant.SEARCHING_FIELD_SYSTEM_CODE);

		/* set option to compay combobox */
		queryForComboBoxOptions(companyCodeOptionList, SystemConstant.SEARCHING_FIELD_COMPANY_CODE);

		/* set option to payment term combobox */
		setStaticComboBoxOption(scPaymentTermComboBox, stringArrAdapter(GlobalParameter.getPaymentTerms(false)));
		
		/* set option to retention type combobox */
		valuePair = new String [][]{
				new String[]{SCPackage.RETENTION_LUMPSUM,SCPackage.RETENTION_LUMPSUM},
				new String[]{SCPackage.RETENTION_ORIGINAL,SCPackage.RETENTION_ORIGINAL},
				new String[]{SCPackage.RETENTION_REVISED,SCPackage.RETENTION_REVISED}
		};
		setStaticComboBoxOption(retentionTypeComboBox, GlobalParameter.getRetentionType(false));
		
		/* set option to reviewed by finance combobox */
		valuePair = new String [][]{
				new String[]{SystemConstant.FINQS0REVIEW_Y,SystemConstant.FINQS0REVIEW_Y},
				new String[]{SystemConstant.FINQS0REVIEW_N,SystemConstant.FINQS0REVIEW_N}
		};
		setStaticComboBoxOption(finQS0ReviewComboBox,GlobalParameter.getReviewedByFinance(false));
	}
	
	private void constructButtonPanel() {
		buttonPanel = new StatusButtonPanel(MAX_STATUS_LENGTH);
		buttonPanel.setBorder(false);
		buttonPanel.setFrame(true);
		buttonPanel.setHeight(40);
		buttonPanel.setPaddings(4);
		
		/* add create button */
		createButton = new Button("Add");
		createButton.addClass("right-align-button");
		createButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, com.gwtext.client.core.EventObject e){
				doCreate();
			}
		});
		buttonPanel.addButton(createButton, CREATE_BUTTON_WIDTH, false);
		
		/* add close button */
		closeButton = new Button("Close");
		closeButton.addClass("right-align-button");
		closeButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, com.gwtext.client.core.EventObject e){
				globalSectionController.closePromptWindow();
			}
		});
		buttonPanel.addButton(closeButton, CLOSE_BUTTON_WIDTH, true);
	}
	
	private void doCreate() {

		for(Field f : inputFields){
			/* check for any blank input fields */
			if(f instanceof ComboBox){
				ComboBox combobox = (ComboBox)f;
				if(combobox.getRawValue().trim().length() <= 0){
					MessageBox.alert("Create failed","One or more combo box is empty");
					return;
				}
			}
			else if(f instanceof TextField){
				TextField textField = (TextField)f;
				if(textField.getText().trim().length() <= 0){
					MessageBox.alert("Create failed","One or more text fields is empty");
					return;
				}
			}
		}
		
		if( !systemCodeComboBox.getRawValue().matches(NUMBERS_REGEX) ){
			MessageBox.alert("Create failed","System Code is not valid!");
			return;
		}
		if( !companyComboBox.getRawValue().matches(NUMBERS_REGEX) ){
			MessageBox.alert("Create failed","Company Code is not valid!");
			return;
		}

		
		UIUtil.maskPanelById(CREATE_SYSTEM_CONSTANT_FORM_PANEL_ID, "Creating...", false);
		createButton.disable();
		
		SystemConstant request = new SystemConstant();
		request.setSystemCode(systemCodeComboBox.getRawValue().trim());
		request.setCompany(companyComboBox.getRawValue().trim());
		request.setScPaymentTerm(scPaymentTermComboBox.getRawValue().trim());
		request.setScMaxRetentionPercent(Double.parseDouble(scMaxRetentionPercentField.getText().trim()));
		request.setScInterimRetentionPercent(Double.parseDouble(scInterimRetentionPercentField.getText().trim()));
		request.setScMOSRetentionPercent(Double.parseDouble(scMOSRetentionPercentField.getText().trim()));
		request.setRetentionType(retentionTypeComboBox.getRawValue().trim());
		request.setFinQS0Review(finQS0ReviewComboBox.getRawValue().trim());
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.createSystemConstant(request, globalSectionController.getUser().getUsername(), new AsyncCallback<Boolean>() {
			
			public void onFailure(Throwable e) {
				MessageBox.alert("Create failed","Error occurred when creating system constant.\r\nError message: "+e.getMessage());
				UIUtil.unmaskPanelById(CREATE_SYSTEM_CONSTANT_FORM_PANEL_ID);
				createButton.enable();
			}
			
			public void onSuccess(Boolean ret) {
				MessageBox.alert("INFO","Create succeeded");
				UIUtil.unmaskPanelById(CREATE_SYSTEM_CONSTANT_FORM_PANEL_ID);
				createButton.enable();
			}
			
		});
	}
	
	private void getPermissions() {
		UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository = (UserAccessRightsRepositoryRemoteAsync) GWT.create(UserAccessRightsRepositoryRemote.class);
		((ServiceDefTarget)userAccessRightsRepository).setServiceEntryPoint(GlobalParameter.USER_ACCESS_RIGHTS_REPOSITORY_URL);
		SessionTimeoutCheck.renewSessionTimer();
		userAccessRightsRepository.getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010512_OBJECT_SUBSIDIARY_RULE_WINDOW, new AsyncCallback<List<String>>(){
			public void onSuccess(List<String> accessRightsReturned) {
					if(accessRightsReturned.contains("WRITE")) {
						setCanWrite(true);
					} else {
						setCanWrite(false);
					}
			}
			
			public void onFailure(Throwable e) {
				MessageBox.alert("Error","An error occured while retrieving permissions from the server");
				setCanWrite(false);
			}
		});
	}
	
	private void setCanWrite(boolean value){
		this.canWrite = value;
		
		if(canWrite) {
			buttonPanel.showButton(createButton);
		}
	}
	
	private void setStaticComboBoxOption(ComboBox combobox, String[][] valuePair){
		final String valueField = "VALUE";
		final String displayField = "DISPLAY";
		String[] header = new String[]{valueField,displayField};	
		Store store = new SimpleStore(header, valuePair);

		if(combobox != null){
			combobox.setStore(store);
			combobox.setDisplayField(displayField);
			combobox.setValueField(valueField);
			combobox.setForceSelection(true);
			combobox.setTypeAhead(true);
		}
	}

	private void setDynamicComboBoxOption(ArrayList<String> comboBoxData){
		final String valueField = "VALUE";
		final String displayField = "DISPLAY";

		String[] header = new String[]{valueField,displayField};

		RecordDef comboBoxRecordDef = new RecordDef(new FieldDef[]{new StringFieldDef(valueField),
				   new StringFieldDef(displayField)});
		String[][] ValuePair = new String[comboBoxData.size()][];
		Store store = new SimpleStore(header, ValuePair);
		
		int index = 0;
		for(String str : comboBoxData){
			ValuePair[index] = new String[]{str,str};
			store.add(comboBoxRecordDef.createRecord(ValuePair[index]));
			index++;
		}		
		store.commitChanges();

		ComboBox tempCombobox = null;
		
		if(comboBoxData == systemCodeOptionList)
			tempCombobox = this.systemCodeComboBox;
		else if(comboBoxData == companyCodeOptionList)
			tempCombobox = this.companyComboBox;
		
		if(tempCombobox != null){
			tempCombobox.setStore(store);
			tempCombobox.setDisplayField(displayField);
			tempCombobox.setValueField(valueField);	
		}
	}
	
	private void queryForComboBoxOptions(final ArrayList<String> comboBoxData,
			final String searchingField) {
		UIUtil.maskPanelById(CREATE_SYSTEM_CONSTANT_FORM_PANEL_ID, "Initializing...", true);
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.obtainSystemConstantSearchOption(searchingField,
				new AsyncCallback<ArrayList<String>>() {
					public void onSuccess(ArrayList<String> result) {
						loadingStatus++;
						if(loadingStatus == MAXLOADINGSTATUS){
							UIUtil.unmaskPanelById(CREATE_SYSTEM_CONSTANT_FORM_PANEL_ID);
						}
						try{
							comboBoxData.removeAll(comboBoxData);
							String[][] ValuePair = new String[result.size()][];
							int index = 0;
							if(result != null){
								for(String str : result){
									ValuePair[index] = new String[2];
									if(str==null)
										continue;
									ValuePair[index][0] = str;
									ValuePair[index][1] = str;
									comboBoxData.add(str);
									index++;
								}
							}
							setDynamicComboBoxOption(comboBoxData);
						}
						catch(Exception ex){
							//UIUtil.alert(ex.getMessage());
							MessageBox.alert("Error", "An error occurred when retriving searching criteria options."
														+ "\r\nPlease refresh the page to retrived again."
														+ "\r\nIf still not working, please seek for help from helpdesk"
														+ "\r\nReceived call : true"
														+ "\r\nError Message : "+ex.getMessage());
						}

					}

					public void onFailure(Throwable e) {
						if (e instanceof com.google.gwt.user.client.rpc.InvocationException) {
							MessageBox.alert("Error", "An error occurred when retriving searching criteria options."
									+ "\r\nPlease refresh the page to retrived again."
									+ "\r\nIf still not working, please seek for help from helpdesk"
									+ "\r\nReceived call : false"
									+ "\r\nError Message : "+e.getMessage());
						}
						UIUtil.unmaskPanelById(CREATE_SYSTEM_CONSTANT_FORM_PANEL_ID);
					}

				});	
	}
	
	private String[][] stringArrAdapter(String[][] source){
		String[][] strArrSource = source;
		String[][] strArr = new String[strArrSource.length][];
		for(int i = 0 ; i < strArr.length ; i++){
			strArr[i] = new String[]{strArrSource[i][0],strArrSource[i][0]};
		}
		return strArr;

	}

}
