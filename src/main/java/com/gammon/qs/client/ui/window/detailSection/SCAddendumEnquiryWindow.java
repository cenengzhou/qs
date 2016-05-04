package com.gammon.qs.client.ui.window.detailSection;

import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.PackageRepositoryRemoteAsync;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.AddendumEnquirySilverYellowToBeApprovedRateRenderer;
import com.gammon.qs.client.ui.renderer.AddendumEnquiryYellowAquaAmountRenderer;
import com.gammon.qs.client.ui.renderer.AddendumEnquiryYellowAquaQuantityRenderer;
import com.gammon.qs.client.ui.renderer.AddendumEnquiryYellowRateRenderer;
import com.gammon.qs.client.ui.renderer.AmountRenderer;
import com.gammon.qs.client.ui.renderer.QuantityRenderer;
import com.gammon.qs.client.ui.renderer.RateRenderer;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.SCDetails;
import com.gammon.qs.domain.SCDetailsBQ;
import com.gammon.qs.domain.SCDetailsCC;
import com.gammon.qs.domain.SCDetailsOA;
import com.gammon.qs.domain.SCDetailsVO;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.addendumApproval.AddendumApprovalResponseWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.TextAlign;
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
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.event.EditorGridListenerAdapter;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;

public class SCAddendumEnquiryWindow extends Window {

	private static final String RESULT_PANEL_ID ="updateResultBQPanel"; 

	//UI
	private GlobalSectionController globalSectionController;
	private Panel mainPanel;
	private Panel leftTopPanel;
	private Panel rightTopPanel;
	private Panel topPanel;
	private Panel middlePanel;
	private Panel rightMiddlePanel;
	private Panel leftMiddlePanel;
	private Panel resultPanel;
	private EditorGridPanel resultEditorGridPanel;

	private Label origSCSumTextLabel;
	private Label subcontractNumberTextLabel;
	private Label jobNumberTextLabel;
	private Label SCSumTextLabel1;
	private Label SCSumTextLabel2;
	private Label ApprovedVOTextLabel1;
	private Label ApprovedVOTextLabel2;
	private Label revisedSCSumTextLabel1;
	private Label revisedSCSumTextLabel2;
	private Double revisedSCSumValue;

	private String sequenceNumber ="";
	private String billItem ="";
	private String resourceNumber ="";
	private String subsidiaryCode ="";
	private String objectCode ="";

	private Label jobDescriptionLabel;

	private CheckBox bqOnlyCheckBox;
	private Label bqOnlyLabel;

	private Button submitAddendumApprovalWindowButton;
	private Button openSCAttachmentButton;
	private Button closeWindowButton;

	//data store
	private Store dataStore;

	//Record[] data 
	private Record[] data=null;
	private Record[] newData=null; 
	//Remote service
	private PackageRepositoryRemoteAsync packageRepository;

	private Integer recordSize = 0;
	//private String subcontractDescription = "";
	Double dToBeApprovedSCSum = 0.00000;
	Double dToBeApprovedRevisedSCSum = 0.00000;
	Double dToBeApproved_ApprovedVO = 0.00000;

	AmountRenderer amountRenderer;
	QuantityRenderer quantityRenderer;
	RateRenderer rateRenderer;

	// used to check access rights
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private List<String> accessRightsList;
	private final RecordDef addendumEnquiryRecordDef = new RecordDef(
			new FieldDef[] {
					new StringFieldDef("lineType"),
					new StringFieldDef("billItem"),
					new StringFieldDef("originalQuantity"),
					new StringFieldDef("bqQuantity"),
					new StringFieldDef("toBeApprovedQuantity"),
					new StringFieldDef("ecaRate"),
					new StringFieldDef("scRate"),
					new StringFieldDef("toBeApprovedRate"),
					new StringFieldDef("totalAmount"), //current total amount
					new StringFieldDef("toBeApprovedAmount"),
					new StringFieldDef("bqBrief"), //description
					new StringFieldDef("uom"),
					new StringFieldDef("billNo"), //[c] Bill Number
					new StringFieldDef("sortSeqNo"), //sequence number
					new StringFieldDef("objectCode"),
					new StringFieldDef("subsidiaryCode"),
					new StringFieldDef("resourceNo"),
					new StringFieldDef("bgRate"), //[c] BG Rate
					new StringFieldDef("bgAmount"), //[c] BG Amount
					new StringFieldDef("prevWorkDoneQty"),
					new StringFieldDef("thisWorkDone"), //[c] This Work Done
					new StringFieldDef("cumWorkDoneQty"),
					new StringFieldDef("cumLiabilityAmount"), //[c] Cum Liability Amount
					new StringFieldDef("prevCertifiedQty"),
					new StringFieldDef("thisCert"), //[c] This Cert
					new StringFieldDef("cumCertifiedQuanity"),
					new StringFieldDef("balanceType"), //%
					new StringFieldDef("cumCertAmount"), //Cum Cert. Amount
					new StringFieldDef("contraChargeSCNo"), //Corr SC#
					new StringFieldDef("remark"),
					new StringFieldDef("altObjectType"),
					new StringFieldDef("provisionAmount"), //provisionAmount 
					new StringFieldDef("approved"),
					new StringFieldDef("sourceType")
			}
	);

	private String jobNumber;
	private String jobDescription;
	private Integer subcontractNumber;
	private Store store;
	public SCAddendumEnquiryWindow(GlobalSectionController globalSectionController, String jobNumber, String jobDescription, Integer subcontractNumber, Store store, boolean showButton) {
		super();

		this.globalSectionController =globalSectionController;
		this.jobNumber = jobNumber;
		this.jobDescription = jobDescription;
		this.subcontractNumber = subcontractNumber;
		this.store = store;
		
		userAccessRightsRepository = globalSectionController.getUserAccessRightsRepository();
		packageRepository = globalSectionController.getPackageRepository();
		
		setupUI(showButton);
	}
	
	private void setupUI(boolean showButton){
		setTitle("SC Addendum Enquiry");
		setPaddings(5);
		setWidth(1024);
		setHeight(670);
		setClosable(false);
		setLayout(new FitLayout());

		//Renderer
		amountRenderer = new AmountRenderer(globalSectionController.getUser());
		quantityRenderer = new QuantityRenderer(globalSectionController.getUser());
		rateRenderer = new RateRenderer(globalSectionController.getUser());
		
		setupSearchPanel();
		setupGridPanel();
		setupBottomButton();
		
		if(showButton){
			addButton(openSCAttachmentButton);
			addButton(submitAddendumApprovalWindowButton);
		}
		addButton(closeWindowButton);
	}
	
	private void setupSearchPanel(){
		mainPanel = new Panel();
		mainPanel.setLayout(new RowLayout());
		mainPanel.setFrame(true);

		topPanel = new Panel();
		topPanel.setHeight(80);
		topPanel.setLayout(new HorizontalLayout(2));

		middlePanel = new Panel();
		middlePanel.setHeight(180);
		middlePanel.setLayout(new HorizontalLayout(2));

		leftTopPanel = new Panel();
		leftTopPanel.setPaddings(5);
		leftTopPanel.setWidth(612);
		leftTopPanel.setHeight(50);
		TableLayout leftTopPanelLayout = new TableLayout(3);		
		leftTopPanel.setLayout(leftTopPanelLayout);

		rightTopPanel = new Panel();
		rightTopPanel.setPaddings(5);
		rightTopPanel.setWidth(372);
		rightTopPanel.setHeight(50);
		TableLayout rightTopPanelLayout = new TableLayout(2);		
		rightTopPanel.setLayout(rightTopPanelLayout);
		rightTopPanel.setFrame(true);

		leftMiddlePanel = new Panel();
		leftMiddlePanel.setPaddings(5);
		leftMiddlePanel.setWidth(612);
		leftMiddlePanel.setHeight(150);
		TableLayout leftMiddlePanelLayout = new TableLayout(4);		
		leftMiddlePanel.setLayout(leftMiddlePanelLayout);

		rightMiddlePanel = new Panel();
		rightMiddlePanel.setTitle("Grid Legend");
		rightMiddlePanel.setPaddings(5);
		rightMiddlePanel.setWidth(372); 
		rightMiddlePanel.setHeight(150);
		rightMiddlePanel.setFrame(true);
		TableLayout rightMiddlePanelLayout = new TableLayout(2);		
		rightMiddlePanel.setLayout(rightMiddlePanelLayout);

		Label jobNumberLabel = new Label("Job Number");
		jobNumberLabel.setCls("table-cell");
		leftTopPanel.add(jobNumberLabel);

		jobNumberTextLabel =new Label();
		jobNumberTextLabel.setCtCls("gwt-TextBox-readonly-shape2");
		jobNumberTextLabel.setText(jobNumber);
		jobNumberTextLabel.setWidth(150);
		leftTopPanel.add(jobNumberTextLabel);

		jobDescriptionLabel = new Label();
		jobDescriptionLabel.setCtCls("table-cell");
		jobDescriptionLabel.setText(jobDescription);
		leftTopPanel.add(jobDescriptionLabel);

		Label subcontractNumberLabel = new Label("Subcontract Number");
		subcontractNumberLabel.setCls("table-cell");
		leftTopPanel.add(subcontractNumberLabel);
		subcontractNumberTextLabel = new Label();
		subcontractNumberTextLabel.setCtCls("gwt-TextBox-readonly-shape2");
		subcontractNumberTextLabel.setText(subcontractNumber.toString());
		leftTopPanel.add(subcontractNumberTextLabel);

		Label subcontractDescriptionLabel = new Label();
		subcontractDescriptionLabel.setCls("table-cell");
		subcontractDescriptionLabel.setText(store.getRecordAt(1).getAsObject("value").toString());
		leftTopPanel.add(subcontractDescriptionLabel);


		topPanel.add(leftTopPanel);


		bqOnlyCheckBox = new CheckBox();

		rightTopPanel.add(bqOnlyCheckBox);

		bqOnlyLabel = new Label();
		bqOnlyLabel.setCtCls("table-cell");
		bqOnlyLabel.setText("Display changed BQ and to be approved VO Only");
		rightTopPanel.add(bqOnlyLabel);
		rightTopPanel.add(bqOnlyCheckBox);

		bqOnlyCheckBox.addClickListener(new ClickListener(){
			public void onClick(Widget arg0) {
				bqVoChecked();
			}
		});

		topPanel.add(rightTopPanel);

		Label origSCSumLabel = new Label("Original Subcontract Sum");
		origSCSumLabel.setCls("table-cell");
		leftMiddlePanel.add(origSCSumLabel);

		origSCSumTextLabel=new Label();//
		origSCSumTextLabel.setCtCls("gwt-TextBox-readonly-shape2");
		origSCSumTextLabel.setWidth(140);
		origSCSumTextLabel.setAutoWidth(false);
		origSCSumTextLabel.setText(amountRenderer.render(store.getRecordAt(6).getAsObject("value").toString()));
		leftMiddlePanel.add(origSCSumTextLabel);

		Label balnkLabel1 = new Label("  ");
		balnkLabel1.setWidth(5);
		balnkLabel1.setCls("table-cell");
		leftMiddlePanel.add(balnkLabel1);
		Label balnkLabel2 = new Label("                       ");
		balnkLabel2.setWidth(140);
		balnkLabel2.setCls("table-cell");
		leftMiddlePanel.add(balnkLabel2);
		Label balnkLabel3 = new Label("                       ");
		balnkLabel3.setWidth(140);
		balnkLabel3.setCls("table-cell");
		leftMiddlePanel.add(balnkLabel3);

		Label prevCumSumApprovedLabel = new Label("Previous Cum Sum Approved");

		prevCumSumApprovedLabel.setCls("gwt-Label-right-singleUnderline");
		leftMiddlePanel.add(prevCumSumApprovedLabel);

		Label balnkLabel4 = new Label("  ");
		balnkLabel4.setWidth(5);
		balnkLabel4.setCls("table-cell");
		leftMiddlePanel.add(balnkLabel4);

		Label RevCumSumToBeApprovedLabel = new Label("Revised Cum Sum To Be Approved");
		RevCumSumToBeApprovedLabel.setCls("gwt-Label-right-singleUnderline");
		leftMiddlePanel.add(RevCumSumToBeApprovedLabel);

		Label SCSumLabel = new Label("Subcontract Sum");
		SCSumLabel.setCls("table-cell");
		leftMiddlePanel.add(SCSumLabel);

		SCSumTextLabel1 = new Label();
		SCSumTextLabel1.setCtCls("gwt-TextBox-readonly-shape2");
		SCSumTextLabel1.setText(amountRenderer.render(store.getRecordAt(7).getAsObject("value").toString()));
		leftMiddlePanel.add(SCSumTextLabel1);

		Label balnkLabel5 = new Label("  ");
		balnkLabel5.setWidth(5);
		balnkLabel5.setCls("table-cell");
		leftMiddlePanel.add(balnkLabel5);

		SCSumTextLabel2 = new Label();
		SCSumTextLabel2.setCtCls("gwt-TextBox-readonly-shape2");
		SCSumTextLabel2.setText("");
		leftMiddlePanel.add(SCSumTextLabel2);

		Label ApprovedVOLabel = new Label("Approved VO");
		ApprovedVOLabel.setCls("table-cell");
		leftMiddlePanel.add(ApprovedVOLabel);

		ApprovedVOTextLabel1 = new Label();
		ApprovedVOTextLabel1.setCtCls("gwt-Label-right-singleUnderline");
		ApprovedVOTextLabel1.setText(amountRenderer.render(store.getRecordAt(8).getAsObject("value").toString()));
		leftMiddlePanel.add(ApprovedVOTextLabel1);

		Label balnkLabel6 = new Label("  ");
		balnkLabel6.setWidth(5);
		balnkLabel6.setCls("table-cell");
		leftMiddlePanel.add(balnkLabel6);


		ApprovedVOTextLabel2 = new Label();
		ApprovedVOTextLabel2.setCtCls("gwt-Label-right-singleUnderline");
		ApprovedVOTextLabel2.setText("");
		leftMiddlePanel.add(ApprovedVOTextLabel2);		



		Label revisedSCSumLabel = new Label("Revised Subcontract Sum");
		revisedSCSumLabel.setCls("table-cell");
		leftMiddlePanel.add(revisedSCSumLabel);

		revisedSCSumTextLabel1 = new Label();
		revisedSCSumTextLabel1.setCtCls("gwt-Label-right-doubleUnderline");
		revisedSCSumTextLabel1.setText(amountRenderer.render(store.getRecordAt(9).getAsObject("value").toString()));
		leftMiddlePanel.add(revisedSCSumTextLabel1);

		Label balnkLabel7 = new Label("  ");
		balnkLabel7.setWidth(5);
		balnkLabel7.setCls("table-cell");
		leftMiddlePanel.add(balnkLabel7);

		revisedSCSumTextLabel2 = new Label();
		revisedSCSumTextLabel2.setCtCls("gwt-Label-right-doubleUnderline");
		revisedSCSumTextLabel2.setText("");
		leftMiddlePanel.add(revisedSCSumTextLabel2);

		middlePanel.add(leftMiddlePanel);

		TextField textFieldLegendColor1 = new TextField();
		textFieldLegendColor1.setStyle("background-color:" + "33FFFF" + ";background-image:none;");   
		textFieldLegendColor1.setWidth(20);
		rightMiddlePanel.add(textFieldLegendColor1);

		Label gridlegendLabel1 = new Label("Changed BQ");
		gridlegendLabel1.setCls("table-cell");
		rightMiddlePanel.add(gridlegendLabel1);

		TextField textFieldLegendColor2 = new TextField();
		textFieldLegendColor2.setStyle("background-color:" + "FFFF66" + ";background-image:none;");   
		textFieldLegendColor2.setWidth(20);
		rightMiddlePanel.add(textFieldLegendColor2);

		Label gridlegendLabel2 = new Label("To be approved VO");
		gridlegendLabel2.setCls("table-cell");
		rightMiddlePanel.add(gridlegendLabel2);

		TextField textFieldLegendColor3 = new TextField();
		textFieldLegendColor3.setStyle("background-color:" + "CCCCCC" + ";background-image:none;");   
		textFieldLegendColor3.setWidth(20);
		rightMiddlePanel.add(textFieldLegendColor3);

		Label gridlegendLabel3 = new Label("Not Applicable");
		gridlegendLabel3.setCls("table-cell");
		rightMiddlePanel.add(gridlegendLabel3);

		middlePanel.add(rightMiddlePanel);


		mainPanel.add(topPanel);
		mainPanel.add(middlePanel);
	}
	
	private void setupGridPanel(){
		//Grid Panel 
		resultPanel = new Panel();
		resultPanel.setId(RESULT_PANEL_ID);
		resultPanel.setBorder(true);
		resultPanel.setPaddings(5);
		resultPanel.setAutoScroll(true);

		resultPanel.setLayout(new FitLayout());
		resultEditorGridPanel =  new EditorGridPanel();

		ColumnConfig lineTypeColumn = new ColumnConfig("Line Type", "lineType", 130 , true);
		ColumnConfig billItemColumn = new ColumnConfig("BQ Item", "billItem", 130 , true);

		ColumnConfig originalQuantityColumn = new ColumnConfig("Original Qty", "originalQuantity", 130 , true);
		originalQuantityColumn.setRenderer(quantityRenderer);
		originalQuantityColumn.setAlign(TextAlign.RIGHT);
		originalQuantityColumn.setHidden(true);

		ColumnConfig bqQuantityColumn = new ColumnConfig("Approved Qty", "bqQuantity", 130 , true);
		bqQuantityColumn.setAlign(TextAlign.RIGHT);
		bqQuantityColumn.setRenderer(new AddendumEnquiryYellowAquaQuantityRenderer(globalSectionController.getUser()));

		ColumnConfig toBeApprovedQuantityColumn = new ColumnConfig("To Be Approved Qty", "toBeApprovedQuantity", 130 , true);
		toBeApprovedQuantityColumn.setAlign(TextAlign.RIGHT);
		toBeApprovedQuantityColumn.setRenderer(new AddendumEnquiryYellowAquaQuantityRenderer(globalSectionController.getUser()));

		ColumnConfig ecaRateColumn = new ColumnConfig("Cost Rate", "ecaRate", 130 , true);
		ecaRateColumn.setRenderer(rateRenderer);
		ecaRateColumn.setAlign(TextAlign.RIGHT);
		ecaRateColumn.setHidden(true);

		ColumnConfig scRateColumn = new ColumnConfig("Approved Rate", "scRate", 130 , true);
		scRateColumn.setAlign(TextAlign.RIGHT);
		scRateColumn.setRenderer(new AddendumEnquiryYellowRateRenderer(globalSectionController.getUser()));

		ColumnConfig toBeApprovedRateColumn = new ColumnConfig("To Be Approved Rate", "toBeApprovedRate", 130 , true);
		toBeApprovedRateColumn.setAlign(TextAlign.RIGHT);
		toBeApprovedRateColumn.setRenderer(new AddendumEnquirySilverYellowToBeApprovedRateRenderer(globalSectionController.getUser()));

		ColumnConfig totalAmountColumn = new ColumnConfig("Approved Amount", "totalAmount", 130 , true);
		totalAmountColumn.setAlign(TextAlign.RIGHT);
		totalAmountColumn.setRenderer(new AddendumEnquiryYellowAquaAmountRenderer(globalSectionController.getUser()));

		ColumnConfig toBeApprovedAmountColumn = new ColumnConfig("To Be Approved Total Amount", "toBeApprovedAmount", 130 , true);
		toBeApprovedAmountColumn.setAlign(TextAlign.RIGHT);
		toBeApprovedAmountColumn.setRenderer(new AddendumEnquiryYellowAquaAmountRenderer(globalSectionController.getUser()));

		ColumnConfig bqBriefColumn = new ColumnConfig("Description", "bqBrief", 250 , true);
		ColumnConfig uomColumn = new ColumnConfig("UM", "uom", 130 , true);

		ColumnConfig billNoColumn = new ColumnConfig("Bill Number", "billNo", 130 , true);
		billNoColumn.setHidden(true);

		ColumnConfig sortSeqNoColumn = new ColumnConfig("Sequence Number", "sortSeqNo", 130 , true);
		sortSeqNoColumn.setAlign(TextAlign.RIGHT);
		sortSeqNoColumn.setHidden(true);

		ColumnConfig objectCodeColumn = new ColumnConfig("Object", "objectCode", 130 , true);
		ColumnConfig subsidiaryCodeColumn = new ColumnConfig("Subsidiary", "subsidiaryCode", 130 , true);
		ColumnConfig resourceNoColumn = new ColumnConfig("Resource Number", "resourceNo", 130 , true);
		resourceNoColumn.setAlign(TextAlign.RIGHT);
		resourceNoColumn.setHidden(true);

		ColumnConfig bgRateColumn = new ColumnConfig("BG Rate", "bgRate", 130 , true);
		bgRateColumn.setRenderer(rateRenderer);
		bgRateColumn.setAlign(TextAlign.RIGHT);
		bgRateColumn.setHidden(true);

		ColumnConfig bgAmountColumn = new ColumnConfig("BG Amount", "bgAmount", 130 , true);
		bgAmountColumn.setRenderer(amountRenderer);
		bgAmountColumn.setAlign(TextAlign.RIGHT);
		bgAmountColumn.setHidden(true);

		ColumnConfig prevWorkDoneQtyColumn = new ColumnConfig("Prev. Work Done", "prevWorkDoneQty", 130 , true);
		prevWorkDoneQtyColumn.setRenderer(quantityRenderer);
		prevWorkDoneQtyColumn.setAlign(TextAlign.RIGHT);
		prevWorkDoneQtyColumn.setHidden(true);

		ColumnConfig thisWorkDoneColumn = new ColumnConfig("This Work Done", "thisWorkDone", 130 , true);
		thisWorkDoneColumn.setRenderer(quantityRenderer);
		thisWorkDoneColumn.setAlign(TextAlign.RIGHT);
		thisWorkDoneColumn.setHidden(true);


		ColumnConfig cumWorkDoneQtyColumn = new ColumnConfig("Cum. Work Done", "cumWorkDoneQty", 130 , true);
		cumWorkDoneQtyColumn.setRenderer(quantityRenderer);
		cumWorkDoneQtyColumn.setAlign(TextAlign.RIGHT);
		cumWorkDoneQtyColumn.setHidden(true);

		ColumnConfig cumLiabilityAmountColumn = new ColumnConfig("Cum. Liability Amount", "cumLiabilityAmount", 130 , true);
		cumLiabilityAmountColumn.setRenderer(amountRenderer);
		cumLiabilityAmountColumn.setAlign(TextAlign.RIGHT);	
		cumLiabilityAmountColumn.setHidden(true);

		ColumnConfig prevCertifiedQtyColumn = new ColumnConfig("prev. Cert.", "prevCertifiedQty", 130 , true);
		prevCertifiedQtyColumn.setRenderer(quantityRenderer);
		prevCertifiedQtyColumn.setAlign(TextAlign.RIGHT);
		prevCertifiedQtyColumn.setHidden(true);

		ColumnConfig thisCertColumn = new ColumnConfig("This Cert.", "thisCert", 130 , true);
		thisCertColumn.setRenderer(quantityRenderer);
		thisCertColumn.setAlign(TextAlign.RIGHT);
		thisCertColumn.setHidden(true);

		ColumnConfig cumCertifiedQuanityColumn = new ColumnConfig("Cum. Cert.", "cumCertifiedQuanity", 130 , true);
		cumCertifiedQuanityColumn.setRenderer(quantityRenderer);
		cumCertifiedQuanityColumn.setAlign(TextAlign.RIGHT);
		cumCertifiedQuanityColumn.setHidden(true);

		ColumnConfig balanceTypeColumn = new ColumnConfig("%", "balanceType", 130 , true);
		balanceTypeColumn.setHidden(true);

		ColumnConfig cumCertAmountColumn = new ColumnConfig("Cum. Cert. Amount", "cumCertAmount", 130 , true);
		cumCertAmountColumn.setRenderer(amountRenderer);
		cumCertAmountColumn.setAlign(TextAlign.RIGHT);	
		cumCertAmountColumn.setHidden(true);

		ColumnConfig contraChargeSCNoColumn = new ColumnConfig("Corr SC #", "contraChargeSCNo", 130 , true);
		contraChargeSCNoColumn.setHidden(true);
		ColumnConfig remarkColumn = new ColumnConfig("Remark", "remark", 130 , true);
		remarkColumn.setHidden(true);
		ColumnConfig altObjectTypeColumn = new ColumnConfig("Alt Object Type", "altObjectType", 130 , true);
		altObjectTypeColumn.setHidden(true);

		ColumnConfig provisionAmountColumn = new ColumnConfig("Provision Amount", "provisionAmount", 130 , true);
		provisionAmountColumn.setRenderer(amountRenderer);
		provisionAmountColumn.setAlign(TextAlign.RIGHT);		
		provisionAmountColumn.setHidden(true);

		ColumnConfig approvedColumn = new ColumnConfig("Approved", "approved", 130 , true);
		ColumnConfig sourceTypeColumn = new ColumnConfig("Source Type", "sourceType", 130 , true);
		sourceTypeColumn.setHidden(true);

		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(addendumEnquiryRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		resultEditorGridPanel.setStore(dataStore);

		ColumnConfig[] columns = new ColumnConfig[] {

				lineTypeColumn,
				billItemColumn,
				originalQuantityColumn,
				bqQuantityColumn,
				toBeApprovedQuantityColumn,
				ecaRateColumn,
				scRateColumn,
				toBeApprovedRateColumn,
				totalAmountColumn,
				toBeApprovedAmountColumn,
				bqBriefColumn,
				uomColumn,
				billNoColumn,
				sortSeqNoColumn,
				objectCodeColumn,
				subsidiaryCodeColumn,
				resourceNoColumn,
				bgRateColumn,
				bgAmountColumn,
				prevWorkDoneQtyColumn,
				thisWorkDoneColumn,
				cumWorkDoneQtyColumn,
				cumLiabilityAmountColumn,
				prevCertifiedQtyColumn,
				thisCertColumn,
				cumCertifiedQuanityColumn,
				balanceTypeColumn,
				cumCertAmountColumn,
				contraChargeSCNoColumn,
				remarkColumn,
				altObjectTypeColumn,
				provisionAmountColumn,
				approvedColumn//,
				//sourceTypeColumn
		};
		resultEditorGridPanel.setColumnModel(new ColumnModel(columns));

		resultEditorGridPanel.addEditorGridListener(new EditorGridListenerAdapter(){

			public boolean doBeforeEdit(GridPanel grid, Record record, String field, Object value, int rowIndex,int colIndex)
			{				
				String jobNumber = record.getAsString("jobNumber");

				if(jobNumber!=null && !"".equals(jobNumber.trim()))
					return true;

				return false;
			}

		});

		resultEditorGridPanel.addGridRowListener(new GridRowListenerAdapter(){

			public void onRowClick(GridPanel grid, int rowIndex, EventObject e)
			{	
				Record curRecord = dataStore.getAt(rowIndex);
				sequenceNumber =(curRecord.getAsObject("sortSeqNo")!=null? curRecord.getAsString("sortSeqNo").trim():"0");
				billItem =(curRecord.getAsObject("billItem")!=null? curRecord.getAsString("billItem").trim():"0");
				resourceNumber =(curRecord.getAsObject("resourceNo")!=null? curRecord.getAsString("resourceNo").trim():"0");
				subsidiaryCode =(curRecord.getAsObject("subsidiaryCode")!=null? curRecord.getAsString("subsidiaryCode").trim():"0");
				objectCode =(curRecord.getAsObject("objectCode")!=null? curRecord.getAsString("objectCode").trim():"0");
			}
		});

		GridView view = new CustomizedGridView();
		view.setAutoFill(false);
		view.setForceFit(false);
		resultEditorGridPanel.setView(view);

		resultPanel.add(resultEditorGridPanel);

		mainPanel.add(resultPanel);

		add(mainPanel);
	}
	
	private void setupBottomButton(){
		closeWindowButton = new Button("Close");
		closeWindowButton.addListener(new ButtonListenerAdapter(){ 
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				globalSectionController.closeCurrentWindow();				
			};
		});		


		submitAddendumApprovalWindowButton = new Button("Addendum Approval");
		//Check status of SubmittedAddendum to enable the submitAddendumApprovalWindowButton
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getPackageRepository().getSCPackage(jobNumber, subcontractNumber.toString(), new AsyncCallback<SCPackage>() {
			public void onFailure(Throwable e) {
				UIUtil.alert(e.getMessage());
			}
			public void onSuccess(SCPackage scPackage) {
				if(!"1".equals(scPackage.getSubmittedAddendum())){
					submitAddendumApprovalWindowButton.setDisabled(false);
				}else{
					submitAddendumApprovalWindowButton.setDisabled(true);
				}

			}
		});

		submitAddendumApprovalWindowButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				//disable the submitAddendumApprovalWindowButton to avoid double-clicking
				submitAddendumApprovalWindowButton.setDisabled(true);

				UIUtil.maskPanelById(SCAddendumEnquiryWindow.this.getId(),"Submitting...",true);
				String paymentRequest = globalSectionController.getPackageEditorFormPanel().getPaymentRequestStatus().trim();
				String splitTerminateStatus = globalSectionController.getPackageEditorFormPanel().getSplitTerminateStatus().trim();
				if (paymentRequest.equals("SBM")||paymentRequest.equals("PCS")||paymentRequest.equals("UFR")){
					MessageBox.alert("Addendum Request cannot be submitted.<br> Payment request was submitted");
				}
				else if(splitTerminateStatus.equals("2") || splitTerminateStatus.equals("4")){
					MessageBox.alert("Addendum Request cannot be submitted.<br> Package was terminated.");
				}
				// Addendum Request should not be submitted when split sc request was submitted.
				else if (splitTerminateStatus.equals("1")){
					MessageBox.alert("Addendum Request cannot be submitted.<br> Split Subcontract approval request was submitted.");
				}else{
					SessionTimeoutCheck.renewSessionTimer();
					packageRepository.submitAddendumApproval(jobNumberTextLabel.getText(), Integer.parseInt(subcontractNumberTextLabel.getText()), revisedSCSumValue-Double.parseDouble(origSCSumTextLabel.getText().replace(",","")),globalSectionController.getUser().getUsername().toUpperCase() , new AsyncCallback<AddendumApprovalResponseWrapper>(){
						public void onFailure(Throwable e){
							UIUtil.unmaskPanelById(SCAddendumEnquiryWindow.this.getId());
							UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
						}

						public void onSuccess(AddendumApprovalResponseWrapper result) {
							UIUtil.unmaskPanelById(SCAddendumEnquiryWindow.this.getId());
							if (result.getIsFinished()){
								MessageBox.alert("Addendum Approval request was submitted!\nPlease check status in SC Approval list");
							} else {		
								MessageBox.alert("Submit addendum approval failed.\n"+result.getErrorMsg());
								submitAddendumApprovalWindowButton.setDisabled(false);
							}
						}
					});
				}

			}
		});

		openSCAttachmentButton = new Button();
		openSCAttachmentButton.setText("Attachment");
		openSCAttachmentButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e)
			{
				if("".equals(sequenceNumber.trim()))
					MessageBox.alert("No SCDetail is seleted");
				else
					globalSectionController.showAttachmentInAddendumEnquiry("Attachment_SCDetail", jobNumberTextLabel.getText().trim(), subcontractNumberTextLabel.getText().trim(),sequenceNumber.trim(),billItem.substring(0, billItem.indexOf("/")), resourceNumber, subsidiaryCode, objectCode);
			}
		});

		ToolTip showSCAttachmentTip = new ToolTip();
		showSCAttachmentTip.setTitle("Attachment");
		showSCAttachmentTip.setHtml("Show Attachment Window");
		showSCAttachmentTip.setCtCls("toolbar-button");
		showSCAttachmentTip.setDismissDelay(15000);
		showSCAttachmentTip.setWidth(300);
		showSCAttachmentTip.setTrackMouse(true);
		showSCAttachmentTip.applyTo(openSCAttachmentButton);

		submitAddendumApprovalWindowButton.setVisible(false);

		// Check for access rights - then add toolbar buttons accordingly
		SessionTimeoutCheck.renewSessionTimer();
		userAccessRightsRepository.getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010209_SCADDENDUM_ENQUIRYWINDOW, new AsyncCallback<List<String>>(){
			public void onSuccess(List<String> accessRightsReturned) {
				try{
					accessRightsList = accessRightsReturned;
					securitySetup();
				}catch(Exception e){
					UIUtil.alert(e);
				}
			}

			public void onFailure(Throwable e) {
				UIUtil.alert(e.getMessage());
			}
		});
	}

	public void searchField(String jobNumber, String scNumber){
		UIUtil.maskPanelById(RESULT_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.getAddendumEnquiry(jobNumber, scNumber, new AsyncCallback<List<SCDetails>>(){

			public void onSuccess(List<SCDetails> result) {
				populateGrid(result);
				UIUtil.unmaskPanelById(RESULT_PANEL_ID);
			}

			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
				UIUtil.unmaskPanelById(RESULT_PANEL_ID);
			}	
		});
	}

	public void populateGrid(List<SCDetails> addendumEnquiryList) {
		dataStore.removeAll();	
		recordSize = addendumEnquiryList.size();
		data = new Record[recordSize];


		dToBeApproved_ApprovedVO=0.0;
		dToBeApprovedSCSum=0.0;
		for(int i=0; i < addendumEnquiryList.size();  i++ )
		{
			SCDetails curaddendumEnquiryList = addendumEnquiryList.get(i);
			if (curaddendumEnquiryList!=null)
				try{

					Double tobeapproveQty =0.0;
					Double costRate =0.0;
					Double tobeapproveRate =0.0;
					Double totalAmount =0.0;
					Double toBeApprovedtotalAmount =0.0;
					Double postedWorkDoneQuantity=0.0;
					Double cumWorkDoneQuantity=0.0;
					String ccSCNo ="";
					String altObjectAcc ="";
					if (curaddendumEnquiryList instanceof SCDetailsVO) {

						tobeapproveQty = ((SCDetailsVO) curaddendumEnquiryList).getToBeApprovedQuantity();

						tobeapproveRate = ((SCDetailsVO)curaddendumEnquiryList).getToBeApprovedRate();
						toBeApprovedtotalAmount= ((SCDetailsVO)curaddendumEnquiryList).getToBeApprovedAmount();
						totalAmount = ((SCDetailsVO)curaddendumEnquiryList).getTotalAmount();
						ccSCNo = ((SCDetailsVO)curaddendumEnquiryList).getContraChargeSCNo();
						dToBeApproved_ApprovedVO = dToBeApproved_ApprovedVO+toBeApprovedtotalAmount;
					}else if (curaddendumEnquiryList instanceof SCDetailsBQ) {

						tobeapproveQty = ((SCDetailsBQ)curaddendumEnquiryList).getToBeApprovedQuantity(); 
						costRate = ((SCDetailsBQ)curaddendumEnquiryList).getCostRate();
						totalAmount = ((SCDetailsBQ)curaddendumEnquiryList).getTotalAmount();
						tobeapproveRate = ((SCDetailsBQ)curaddendumEnquiryList).getScRate();
						toBeApprovedtotalAmount= ((SCDetailsBQ)curaddendumEnquiryList).getToBeApprovedAmount();
						dToBeApprovedSCSum = dToBeApprovedSCSum+ toBeApprovedtotalAmount;
					}else if (curaddendumEnquiryList instanceof SCDetailsOA) {
						postedWorkDoneQuantity=((SCDetailsOA)curaddendumEnquiryList).getPostedCertifiedQuantity();
						cumWorkDoneQuantity=((SCDetailsOA )curaddendumEnquiryList).getCumWorkDoneQuantity();
					}else if (curaddendumEnquiryList instanceof SCDetailsCC) {
						ccSCNo = ((SCDetailsCC)curaddendumEnquiryList).getContraChargeSCNo();
						altObjectAcc = ((SCDetailsCC)curaddendumEnquiryList).getAltObjectCode();
					}
					if (costRate ==null){
						costRate=0.00;
					}
					data[i] = addendumEnquiryRecordDef.createRecord(
							new Object[]{
									curaddendumEnquiryList.getLineType(),
									curaddendumEnquiryList.getBillItem(),
									curaddendumEnquiryList.getOriginalQuantity(),
									curaddendumEnquiryList.getQuantity(),
									tobeapproveQty,
									costRate,
									curaddendumEnquiryList.getScRate(),
									tobeapproveRate,
									totalAmount,
									toBeApprovedtotalAmount,
									curaddendumEnquiryList.getDescription(),
									curaddendumEnquiryList.getUnit(),
									curaddendumEnquiryList.getBillItem().substring(0, curaddendumEnquiryList.getBillItem().indexOf("/")),//[c] Bill Number
									curaddendumEnquiryList.getSequenceNo(),
									curaddendumEnquiryList.getObjectCode(),
									curaddendumEnquiryList.getSubsidiaryCode(),
									curaddendumEnquiryList.getResourceNo(),
									(costRate-curaddendumEnquiryList.getScRate()),//[c] BG Rate
									(costRate-curaddendumEnquiryList.getScRate())*curaddendumEnquiryList.getQuantity(),//[c] BG Amount
									postedWorkDoneQuantity,
									(cumWorkDoneQuantity-postedWorkDoneQuantity),//[c] This Work Done,
									cumWorkDoneQuantity,
									(cumWorkDoneQuantity*curaddendumEnquiryList.getScRate()),//[c] Cum Liability Amount,
									curaddendumEnquiryList.getPostedCertifiedQuantity(),
									(curaddendumEnquiryList.getCumCertifiedQuantity()-curaddendumEnquiryList.getPostedCertifiedQuantity()),//[c] This Cert,
									curaddendumEnquiryList.getCumCertifiedQuantity(),
									curaddendumEnquiryList.getBalanceType(),
									(curaddendumEnquiryList.getCumCertifiedQuantity()*curaddendumEnquiryList.getScRate()),//[c] Cum Cert. Amount,
									ccSCNo,
									curaddendumEnquiryList.getRemark(),
									altObjectAcc,
									curaddendumEnquiryList.getScRate()*(curaddendumEnquiryList.getCumCertifiedQuantity()-curaddendumEnquiryList.getPostedCertifiedQuantity()),//[c] provisionAmount,
									curaddendumEnquiryList.getApproved(),
									""
							}
					);

				}catch (Exception e)
				{				
					UIUtil.alert(e);
				}

		}

		SCSumTextLabel2.setText(amountRenderer.render(dToBeApprovedSCSum.toString()));
		ApprovedVOTextLabel2.setText(amountRenderer.render(dToBeApproved_ApprovedVO.toString()));
		revisedSCSumTextLabel2.setText(amountRenderer.render((dToBeApprovedSCSum+dToBeApproved_ApprovedVO)+""));
		revisedSCSumValue = dToBeApprovedSCSum+dToBeApproved_ApprovedVO;
		//dataStore.add(data);
		bqOnlyCheckBox.setChecked(true);
		bqVoChecked();
	}

	public void bqVoChecked(){
		// keep all the data that are highlighted yellow // aqua
		if (bqOnlyCheckBox.isChecked() && newData==null){
			Record[] copyData = new Record[data.length];
			for (int i =0; i < data.length; i++){
				copyData[i] = data[i];
			}
			int dataNumberRemoved = 0;
			for (int i =0; i < copyData.length; i++){
				AddendumEnquiryYellowAquaAmountRenderer yellowAquaRender = new AddendumEnquiryYellowAquaAmountRenderer(globalSectionController.getUser());
				if (yellowAquaRender.check(copyData[i],"").contains("FFFF00")){

				}
				else if(yellowAquaRender.check(copyData[i],"").contains("00FFFF")){

				}
				else{
					copyData[i]=null;
					dataNumberRemoved ++;
				}
			}
			newData = new Record[recordSize-dataNumberRemoved];
			int count=0;
			for(int i = 0; i< copyData.length;i++){
				if (copyData[i] != null){
					newData[count] = copyData[i];
					count++;
				}
			}
			dataStore.removeAll();
			dataStore.add(newData);
		}
		else if(bqOnlyCheckBox.isChecked() && newData!=null){
			dataStore.removeAll();
			dataStore.add(newData);
		}
		//display all
		else{
			dataStore.removeAll();
			dataStore.add(data);
		}

		if(newData.length==0){
			openSCAttachmentButton.setDisabled(true);
			
			String originalRevisedSCSum = store.getRecordAt(9).getAsObject("value").toString();
			
			if(Double.valueOf(originalRevisedSCSum!=""?originalRevisedSCSum:"0.0").doubleValue()== revisedSCSumValue.doubleValue())
				submitAddendumApprovalWindowButton.setDisabled(true);
		}
	}
	private void securitySetup(){
		if(accessRightsList.contains("WRITE")){
			submitAddendumApprovalWindowButton.setVisible(true);
		}
	}
}