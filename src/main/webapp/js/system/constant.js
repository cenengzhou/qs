mainApp.constant("colorCode", {
    red: "#FF5252",
    lightRed:"#FF7E7E",
    blue: "#00c0ef",
    lightBlue: "#6ED2EB",
    green: "#00a65a",
    lightGreen:"#33cc33",
    yellow: "#f39c12",
    lightYellow:"#FFBD51",
    grey: "#d2d6de",
    lightGrey: "#DFE2E9",
    purple: "#605ca8",
    lightPurple: "#9996C6",
    pink: "#f94877",
    lightPink: "#f71752",
    primary:"#2184DA",
   	lime:"#65C56F",
   	white:"#fff"
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
	paymentTerms:[
		{id:"QS0", value: "QS0 - Manual Input Due Date"},
		{id:"QS1", value: "QS1 - Pay when Paid + 7 days"},
		{id:"QS2", value: "QS2 - Pay when Paid + 14 days"},
		{id:"QS3", value: "QS3 - Pay when IPA Received + 56 days"},
		{id:"QS4", value: "QS4 - Pay when Invoice Received + 28 days"},
		{id:"QS5", value: "QS5 - Pay when Invoice Received + 30 days"},
		{id:"QS6", value: "QS6 - Pay when Invoice Received + 45 days"},
		{id:"QS7", value: "QS7 - Pay when Invoice Received + 60 days"}
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
	subcontractStatus: [
		{id:100, value:'100 - SC Created'},
		{id:160, value:'160 - TA Analysis Ready'},
		{id:330, value:'330 - Award Request Submitted'},
		{id:550, value:'550 - SC Awarded'}
	],
	packageStatus: [
		{id:100, value:'100 - SC Created'},
		{id:990, value:'990'}
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
	paymentStatus: [
		{id:'N', value:'N - Not Submitted'},
		{id:'D', value:'D - Payment Requistion'},
		{id:'I', value:'I - Interim Payment'},
		{id:'F', value:'F - Final Payment'}
	],
	splitTerminateStatus: [
		{id:0, value:'0 - Not Submitted'},
		{id:1, value:'1 - Split SC Submitted'},
		{id:2, value:'2 - Terminate SC Submitted'},
		{id:3, value:'3 - Split Approvaed'},
		{id:4, value:'4 - Terminate Approved'},
		{id:5, value:'5 - Split Rejected'},
		{id:6, value:'6 - Terminate Rejected'},
	], 
	paymentCurrency: [
		{id:'AUD', value:'AUD - Austrialian Dollar'},
		{id:'CAD', value:'CAD - Canadian Dollar'},
		{id:'CHF', value:'CHF - Swiss Franc'},
		{id:'CNY', value:'CNY - Chinese Renminbi'},
		{id:'DEM', value:'DEM - Deutsche Marks'},
		{id:'EUR', value:'EUR - Euro'},
		{id:'GBP', value:'GBP - Pound Sterling'},
		{id:'HKD', value:'HKD - Hong Kong Dollar'},
		{id:'IDR', value:'IDR - Indonesia Rupiah'},
		{id:'JPY', value:'JPY - Japanese Yen'},
		{id:'KRW', value:'KRW - South Korean Won'},
		{id:'MOP', value:'MOP - Macau Pataca'},
		{id:'MYR', value:'MYR - Malaysian Ringgit'},
		{id:'NOK', value:'NOK - Norwegian Kroner'},
		{id:'NZD', value:'NZD - New Zealand Dollar'},
		{id:'PHP', value:'PHP - Philippine Peso'},
		{id:'SEK', value:'SEK - Swedish Kroner'},
		{id:'SGD', value:'SGD - Singapore Dollar'},
		{id:'THB', value:'THB - Thai Baht'},
		{id:'TWD', value:'TWD - Taiwan Dollar'},
		{id:'USD', value:'USD - U.S. Dollar'},
		{id:'VND', value:'VND - Vietnam Dong'},
		{id:'ZAR', value:'ZAR - South African Rand'}		
	],
	submittedAddendum: [
		{id:' ', value:'Not submitted'},
		{id:1, value: 'Submitted'}
	],
	directPayment: [
		{id:'N', value:'Normal Payment'},
		{id:'Y', value:'Direct Payment'}
	],
	finQS0Review: [
	    {id:'N', value:'No'},
	    {id:'Y', value:'Yes'}
	],
	paymentStatus: [
		{id:'PND', value:'PND - Pending'},
		{id:'SBM', value:'SBM - Submitted'},
		{id:'UFR', value:'UFR - Under Review by Finance'},
		{id:'PCS', value:'PCS - AP Not Created'},
		{id:'APR', value:'APR - AP Created'}
	],
	intermFinalPayment: [
		{id:'I', value:'Interim'},
		{id:'F', value:'Final'}
	],
	certificateStatus: [
		{id:'100', value:'100 - Certificate Created'},
		{id:'120', value:'120 - IPA sent'},
		{id:'150', value:'150 - Certificate Confirmed'},
		{id:'200', value:'200 - Certificate Waiting for Approval'},
		{id:'300', value:'300 - Certificate Posted to Finance\'s AR'}
	]
});

mainApp.constant('GlobalMessage', {
	subcontractorHoldMessage:
		"The subcontractor is under finance hold status. <br/>Please contact Finance Dept at 2516-8911 ext 7799 for details.",
	paymentHoldMessage:
		"Please contact Finance Dept -Ms Cindy Deng at 2516-8911 ext 7789 or Ms Kathy Chen at 2516-8911 ext 7798.<br/> For SGP vendors, please contact Mr Nix Goh at 6722-3654.",
});
