mainApp.constant("colorCode", {
    red: "#F04B46",
    lightRed:"#F36F6B",
    blue: "#2184DA",
    lightBlue: "#4D9CE1",
    green: "#17B6A4",
    lightGreen:"#98ded7",
    yellow: "#fcaf41",
    lightYellow:"#FCBF67",
    grey: "#aab3ba",
    lightGrey: "#BBC2C7",
    purple: "#9b59b6",
    lightPurple: "#cfafdc",
    pink: "#f94877",
    lightPink: "#f71752",
    primary:"#2184DA",
   	lime:"#65C56F",
   	lightLime: "#83D08B",
   	info:"#38AFD3",
   	lightInfo:"#5FBFDB",	
   	white:"#fff",
   	black: "#3C454D",
   	
});
//#ffaaaa - Pink


mainApp.constant("subcontractRetentionTerms", {
	"RETENTION_LUMPSUM": "Lump Sum Amount Retention",
	"RETENTION_ORIGINAL": "Percentage - Original SC Sum",
	"RETENTION_REVISED": "Percentage - Revised SC Sum"
});

mainApp.constant('GlobalParameter', {
	getValueById: function  (arr, id) {
	    for (var i = 0; i < arr.length; i++) {
	        if (arr[i].id === id) {
	            return arr[i].value;
	        }
	    }
	},
	getObjectById: function  (arr, id) {
	    for (var i = 0; i < arr.length; i++) {
	        if (arr[i].id === id) {
	            return arr[i];
	        }
	    }
	},
	getIdPlusValue: function  (arr) {
		var result = [];
		arr.forEach(function(obj){
			var newObj = {};
			newObj['id'] = obj.id;
			newObj['value'] = obj.id + ' - ' + obj.value;
			result.push(newObj);
		});
		return result;
	},
	getAttributeArray: function (objArray, attribute){
		var result = [];
		objArray.forEach(function(obj){
			result.push(obj[attribute]);
		})
		return result;
	},

	DATE_FORMAT: 'dd/MM/yyyy',
	DATETIME_FORMAT: ' dd/MM/yyyy hh:mm',
	MOMENT_DATE_FORMAT: 'YYYY-MM-DD',
	MOMENT_DATE_REGEX: '\\d{4}-\\d{1,2}-\\d{1,2}',
	MOMENT_DATETIME_FORMAT: 'YYYY-MM-DD HH:mm',
	// Info Tips content
	//to show id - value => ng-options="v.id as (v.id + ' - ' + v.value) for v in GlobalParameter.paymentTerms"
	addendumTypes:[
	      		{id:"V1", value: "External VO - No Budget" },
	      		{id:"V2", value: "Internal VO - No Budget" },
	      		{id:"L1", value: "Claims vs GSL"},
	      		{id:"L2", value: "Claims vs other Subcontract"},
	      		{id:"D1", value: "Day Work for GCL"},
	      		{id:"D2", value: "Day Work for other Subcontract"},
	      		{id:"CF", value: "CPF"},
	      		{id:"AP", value: "Advanced Payment"},
	      		{id:"C1", value: "Contra Charges by GSL"},
	      		{id:"C2", value: "Contra Charges by other SC"},
	      		{id:"MS", value: "Material On Site"},
	      		{id:"OA", value: "Other Adjustment"},
	      		{id:"RA", value: "Retention Adjustment"},
	      		{id:"RR", value: "Release Retention"}
  	],
	paymentTerms:[
				{id:"QS0", value: "Manual Input Due Date"},
				{id:"QS1", value: "Pay when Paid + 7 days"},
				{id:"QS2", value: "Pay when Paid + 14 days"},
				{id:"QS3", value: "Pay when IPA Received + 56 days"},
				{id:"QS4", value: "Pay when Invoice Received + 28 days"},
				{id:"QS5", value: "Pay when Invoice Received + 30 days"},
				{id:"QS6", value: "Pay when Invoice Received + 45 days"},
				{id:"QS7", value: "Pay when Invoice Received + 60 days"}
	],
	paymentStatus: [
        		{id:'PND', value:'Pending', color:'#007D00'},
        		{id:'SBM', value:'Submitted', color:'#E68550'},
        		{id:'UFR', value:'Under Finance Review', color:'#E68550'},
        		{id:'PCS', value:'Waiting For Posting', color:'#E68550'},
        		{id:'APR', value:'Posted To Finance', color:'#707070'}
	],
	paymentStatusCode: [
                {id:'A', value:'Approved for Payment'},
                {id:'P', value:'Paid in Full'},
                {id:'H', value:'Held/Pending Approval'},
                {id:'#', value:'Check being Written'},
                {id:'%', value:'Withholding Applies'}
    ],
    approvalType: [
               {id:'SP', value:'Interim Payment', details:'For interim payment of awarded subcontract'},
               {id:'SF', value:'Final Account', details:'For final account of awarded subcontract'},
               {id:'NP', value:'Payment Requisition for subcontract not yet awarded', details:'For non-awarded subcontract'},
               {id:'SM', value:'Variation to SC Award (Cumulative amount)', details:'For addendum'},
               {id:'SL', value:'SC Addendum (> 25% of Original SC Sum or HKD$250,000)', details:'For addendum with amount >25% of Original SC Sum or >HKD$250,000'},
               {id:'VA', value:'Split Subcontract', details:'For split subcontract'},
               {id:'VB', value:'Terminate Subcontract', details:'For interim payment of awarded subcontract'},
               {id:'FR', value:'Under Finance Review', details:'For finance review with Payment Term \'QS0\''},
               {id:'AW', value:'SC Award', details:'For awarded subcontract'},
               {id:'ST', value:'SC Award over budget (Over-budget amount)', details:'For awarded subcontract with over-budget amount'},
               {id:'V5', value:'Varied SC Award', details:'For awarded subcontract with non-standard term payment'},
               {id:'V6', value:'Varied SC Award and over-budget', details:'For awarded subcontract with non-standard term payment and over-budget amount'},
               {id:'RM', value:'Negative Main Contract Certificate', details:'For Main Contract Certificate with negative net certificate amount'}
    ],
	mainContractCertificateStatus: [
        		{id:'100', value:'Certificate Created'},
        		{id:'120', value:'IPA sent'},
        		{id:'150', value:'Certificate(IPC) Confirmed'},
        		{id:'200', value:'Certificate(IPC) Waiting for special approval'},
        		{id:'300', value:'Certificate(IPC) Posted to Finance\'s Account Receivable(AR) Ledger'},
        		{id:'400', value:'Certified Amount Received Status \'400\' was used for historical Main Contract Certificates which were created in JDE QS System.'}
	],
	subcontractStatus: [
        		{id:100, value:'Newly Created Subcontract'},
        		{id:160, value:'Tender Analysis Ready'},
        		{id:330, value:'Subcontract Award request Submitted'},
        		{id:340, value:'Subcontract Award request Rejected'},
        		{id:500, value:'Awarded Subcontract'}
	],
	repackagingStatus: [
                {id:100, value:'Unlocked'},
                {id:200, value:'Updated'},
                {id:300, value:'Snapshot Generated'},
                {id:900, value:'Locked'},
    ],
	// End Info Tips Content
    awardStatus: [
                  {id:' ', value:'Not Awarded'},
                  {id:'AWD', value:'Awarded Subcontract'},
                  {id:'RCM', value:'Approving or Rejected Subcontract'}
    ],
	subcontractTerm:[
		{id:'Lump Sum', value:'Lump Sum'},
		{id:'Re-measurement', value:'Re-measurement'}
	],
	packageType:[
		{id:'M', value:'M'},
		{id:'S', value:'S'}
	],
	cpfCalculation: [
		{id:'Not Subject to CPF', value:'Not Subject to CPF'},
		{id:'Subject to CPF', value:'Subject to CPF'}
	],
	formOfSubcontract: [
		{id:'Consultancy Agreement', value:'Consultancy Agreement'},
		{id:'Internal trading', value:'Internal trading'},
		{id: 'Major', value:'Major'},
		{id:'Minor', value:'Minor'}
	],
	packageStatus: [
		{id:'100', value:'SC Created'},
		{id:'990', value:''}
	],
	paymentInformation:[
		{id:'Interim Payment Schedule (Subcontract)', value:'Interim Payment Schedule (Subcontract)'},
		{id:'Interim Payment Schedule (Main Contract)', value:'Interim Payment Schedule (Main Contract)'}
	],
	subcontractorNature: [
		{id:'DSC', value:'DSC'},
		{id:'DS', value:'DS'},
		{id:'NSC', value:'NSC'},
		{id:'NDS', value:'NDS'},
		{id:'NDSC', value:'NDSC'}
	],
	retentionTerms: [
		{id:'Percentage - Original SC Sum', value:'Percentage - Original SC Sum'},
		{id:'Percentage - Revised SC Sum', value:'Percentage - Revised SC Sum'},
		{id:'Lump Sum Amount Retention', value:'Lump Sum Amount Retention'}
	],
	subcontract_paymentStatus: [
		{id:'N', value:'Not Submitted'},
		{id:'D', value:'Payment Requisition'},
		{id:'I', value:'Interim Payment'},
		{id:'F', value:'Final Payment'}
	],
	splitTerminateStatus: [
		{id:'0', value:'Not Submitted'},
		{id:'1', value:'Split Subcontract Submitted'},
		{id:'2', value:'Terminate Subcontract Submitted'},
		{id:'3', value:'Split Subcontract Approved'},
		{id:'4', value:'Terminate Subcontract Approved'},
		{id:'5', value:'Split Subcontract Rejected'},
		{id:'6', value:'Terminate Subcontract Rejected'},
	], 
	paymentCurrency: [
		{id:'AUD', value:'Austrialian Dollar'},
		{id:'CAD', value:'Canadian Dollar'},
		{id:'CHF', value:'Swiss Franc'},
		{id:'CNY', value:'Chinese Renminbi'},
		{id:'DEM', value:'Deutsche Marks'},
		{id:'EUR', value:'Euro'},
		{id:'GBP', value:'Pound Sterling'},
		{id:'HKD', value:'Hong Kong Dollar'},
		{id:'IDR', value:'Indonesia Rupiah'},
		{id:'JPY', value:'Japanese Yen'},
		{id:'KRW', value:'South Korean Won'},
		{id:'MOP', value:'Macau Pataca'},
		{id:'MYR', value:'Malaysian Ringgit'},
		{id:'NOK', value:'Norwegian Kroner'},
		{id:'NZD', value:'New Zealand Dollar'},
		{id:'PHP', value:'Philippine Peso'},
		{id:'SEK', value:'Swedish Kroner'},
		{id:'SGD', value:'Singapore Dollar'},
		{id:'THB', value:'Thai Baht'},
		{id:'TWD', value:'Taiwan Dollar'},
		{id:'USD', value:'U.S. Dollar'},
		{id:'VND', value:'Vietnam Dong'},
		{id:'ZAR', value:'South African Rand'}		
	],
	submittedAddendum: [
		{id:' ', value:'Not submitted'},
		{id:'1', value: 'Submitted'}
	],
	directPayment: [
		{id:'N', value:'Normal Payment'},
		{id:'Y', value:'Direct Payment'}
	],
	finQS0Review: [
	    {id:'N', value:'No'},
	    {id:'Y', value:'Yes'}
	],
	intermFinalPayment: [
		{id:'I', value:'Interim'},
		{id:'F', value:'Final'}
	],
	booleanOptions:[
		{id: 'true', value: 'True'},
		{id: 'false', value: 'False'}
	],
	zeroOneOptions:[
		{id: true, value: 'True'},
		{id: false, value: 'False'}
	],
	approvedOrRejectedOptions:[
		{id: 'Approved', value: 'A'},
		{id: 'Rejected', value: 'R'}
	],
	splitOrTerminateOptions:[
		{id: 'Split', value: 'S'},
		{id: 'Terminate', value: 'T'}
	],
	month: [
       {id:'01', value:'January'},
       {id:'02', value:'February'},
       {id:'03', value:'March'},
       {id:'04', value:'April'},
       {id:'05', value:'May'},
       {id:'06', value:'June'},
       {id:'07', value:'July'},
       {id:'08', value:'August'},
       {id:'09', value:'September'},
       {id:'10', value:'October'},
       {id:'11', value:'November'},
       {id:'12', value:'December'}
    ],
    subcontractorVenderType: [
       {id:1, value:'Supplier'},
       {id:2, value:'Subcontractor'},
       {id:3, value:'Both (Supplier & Subcontractor)'}
    ],
    subcontractorVendorStatus: [
       {id:'1', value:'Performance being observed'},
       {id:'2', value:'Suspended'},
       {id:'3', value:'Blacklisted'},
       {id:'4', value:'Obsolete'},
       {id:'5', value:'On HSE League Table'},
       {id:'6', value:'Observed & On HSE League'},
       {id:'7', value:'Suspended & On HSE League'}
	],
	subcontractorApproval: [
	   {id:'N', value:'No'},
	   {id:'Y', value:'Yes'},
	   {id:'', value:'-'}
	],
	subcontractorHoldPayment: [
	   {id:'N', value:'No'},
	   {id:'Y', value:'Yes'},
	   {id:'', value:'-'}
	],
	subcontractorFinancialAlertStatus: function(status){
		if(status !== null && status.length > 0){
			return 'Yes';
		}
		return 'No';
	},
	AbstractAttachment:{
	                    'SCPaymentNameObject' : 'GT58012',
	                   'SCPackageNameObject' :'GT58010',
	                    'SCDetailsNameObject' : 'GT58011',
//						'TenderAnalysisNameObject' : 'GT58023'; //Never used
	                    'VendorNameObject' : 'GT58024',
	                    'MainCertNameObject' : 'GT59026'
	},
	PurchaseOrderType:[
		{id:'GH', value:'Plant Blanket Sundry Order'},
		{id:'P3', value:'Payment Requisition'},
		{id:'GB', value:'Blanket Purchase Order'},
		{id:'GE', value:'Plant Sundry Release Order'},
		{id:'GL', value:'Release Purchase Order'},
		{id:'GM', value:'Purchase Order of Fixed Asset'},
		{id:'GP', value:'Purchase Order'},
		{id:'GX', value:'Order exceed or w/o budget'},
		{id:'GY', value:'BPO exceed or w/o budget'},
		{id:'OB', value:'Blanket Purchase Order'},
		{id:'OF', value:'PO of Fixed Asset'},
		{id:'OH', value:'Blanket Sundry Order'},
		{id:'OL', value:'Release Purchase Order'},
		{id:'ON', value:'Sundry Release Order'},
		{id:'OP', value:'Purchase Order'},
		{id:'OS', value:'Purchase Order of Fixed Asset'},
		{id:'OX', value:'Order exceed or w/o budget'},
		{id:'OY', value:'BPO exceed or w/o budget'},
		{id:'O2', value:'Supplier Catalog Order'},
		{id:'OR', value:'Purchase Requisition'},
		{id:'GR', value:'Purchase Requisition - CSD'},
		{id:'P4', value:'Reimbursement of Expense'}
	],
	PerformanceAppraisalStatus: [
		{id:'A', value:'Approved'},
		{id:'E', value:'Evaluated'},
		{id:'N', value:'Not Evaluated'},
		{id:'P', value:'Pending for approval'},
		{id:'V', value:'Verified for approval'}
	],
	ApprovalStatus:[
	                {id:'A', value:'Approved'},
	                {id:'N', value:'Not Apprvoved'}
    ],
	imageServerAddress : 'http://gammon.gamska.com/PeopleDirectory_Picture/',
	tinyMceMaxCharLength: 2000,
});

mainApp.constant('GlobalMessage', {
	subcontractorHoldMessage:
		"The subcontractor is under finance hold status. <br/>Please contact Finance Dept at 2516-8911 ext 7799 for details.",
	paymentHoldMessage:
		"Please contact Finance Dept -Ms Cindy Deng at 2516-8911 ext 7789 or Ms Kathy Chen at 2516-8911 ext 7798.<br/> For SGP vendors, please contact Mr Nix Goh at 6722-3654.",
	deleteAttachment:
		"Are you sure to delete attachments",
	maxCharLimitReached:
		"You've reached the max. limit of text length!",
});
