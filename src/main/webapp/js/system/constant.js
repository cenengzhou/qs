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

mainApp.constant("paymentTermsDescription", {
	"QS0": "Manual Input Due Date",
	"QS1": "Pay when Paid + 7 days",
	"QS2": "Pay when Paid + 14 days",
	"QS3": "Pay when IPA Received + 56 days",
	"QS4": "Pay when Invoice Received + 28 days",
	"QS5": "Pay when Invoice Received + 30 days",
	"QS6": "Pay when Invoice Received + 45 days",
	"QS7": "Pay when Invoice Received + 60 days"
});

mainApp.constant("subcontractRetentionTerms", {
	"RETENTION_LUMPSUM": "Lump Sum Amount Retention",
	"RETENTION_ORIGINAL": "Percentage - Original SC Sum",
	"RETENTION_REVISED": "Percentage - Revised SC Sum"
});

mainApp.constant('GlobalParameter', {
	paymentTerms:{
		"QS0": "QS0 - Manual Input Due Date",
		"QS1": "QS1 - Pay when Paid + 7 days",
		"QS2": "QS2 - Pay when Paid + 14 days",
		"QS3": "QS3 - Pay when IPA Received + 56 days",
		"QS4": "QS4 - Pay when Invoice Received + 28 days",
		"QS5": "QS5 - Pay when Invoice Received + 30 days",
		"QS6": "QS6 - Pay when Invoice Received + 45 days",
		"QS7": "QS7 - Pay when Invoice Received + 60 days"
	},
	subcontractTerm:{
		'Lump Sum':'Lump Sum',
		'Re-measurement':'Re-measurement'
	},
	packageType:{
		'M':'M',
		'S':'S'
	},
	cpfCalculation: {
		'Not Subject to CPF':'Not Subject to CPF',
		'Subject to CPF':'Subject to CPF'
	},
	formOfSubcontract: {
		'Consultancy Agreement': 'Consultancy Agreement',
		'Internal trading':'Internal trading',
		'Major':'Major',
		'Minor':'Minor'
	},
	subcontractStatus: {
		100:'100 - SC Created',
		160:'160 - TA Analysis Ready',
		330:'330 - Award Request Submitted',
		550:'550 - SC Awarded'
	},
	packageStatus: {
		100:'100 - SC Created',
		990:'990'
	},
	paymentInformation:{
		'Interim Payment Schedule (Subcontract)':'Interim Payment Schedule (Subcontract)',
		'Interim Payment Schedule (Main Contract)':'Interim Payment Schedule (Main Contract)'
	},
	subcontractorNature: {
		'DSC':'DSC',
		'DS':'DS',
		'NSC':'NSC',
		'NDS':'NDS',
		'NDSC':'NDSC'
	},
	retentionTerms: {
		'Percentage - Original SC Sum':'Percentage - Original SC Sum',
		'Percentage - Revised SC Sum':'Percentage - Revised SC Sum',
		'Lump Sum Amount Retention':'Lump Sum Amount Retention'
	},
	paymentStatus: {
		'N':'N - Not Submitted',
		'D':'D - Payment Requistion',
		'I':'I - Interim Payment',
		'F':'F - Final Payment'
	},
	splitTerminateStatus: {
		0:'0 - Not Submitted',
		1:'1 - Split SC Submitted',
		2:'2 - Terminate SC Submitted',
		3:'3 - Split Approvaed',
		4:'4 - Terminate Approved',
		5:'5 - Split Rejected',
		6:'6 - Terminate Rejected',
	}, 
	paymentCurrency: {
		'AUD':'AUD - Austrialian Dollar',
		'CAD':'CAD - Canadian Dollar',
		'CHF':'CHF - Swiss Franc',
		'CNY':'CNY - Chinese Renminbi',
		'DEM':'DEM - Deutsche Marks',
		'EUR':'EUR - Euro',
		'GBP':'GBP - Pound Sterling',
		'HKD':'HKD - Hong Kong Dollar',
		'IDR':'IDR - Indonesia Rupiah',
		'JPY':'JPY - Japanese Yen',
		'KRW':'KRW - South Korean Won',
		'MOP':'MOP - Macau Pataca',
		'MYR':'MYR - Malaysian Ringgit',
		'NOK':'NOK - Norwegian Kroner',
		'NZD':'NZD - New Zealand Dollar',
		'PHP':'PHP - Philippine Peso',
		'SEK':'SEK - Swedish Kroner',
		'SGD':'SGD - Singapore Dollar',
		'THB':'THB - Thai Baht',
		'TWD':'TWD - Taiwan Dollar',
		'USD':'USD - U.S. Dollar',
		'VND':'VND - Vietnam Dong',
		'ZAR':'ZAR - South African Rand'		
	},
	submittedAddendum: {
		' ': 'Not submitted',
		1: 'Submitted'
	},
	directPayment: {
		'N': 'Normal Payment',
		'Y': 'Direct Payment'
	},
	paymentStatus: {
		'PND': 'PND - Pending',
		'SBM': 'SBM - Submitted',
		'UFR': 'UFR - Under Review by Finance',
		'PCS': 'PCS - AP Not Created',
		'APR': 'APR - AP Created'
	},
	intermFinalPayment: {
		'I': 'Interim',
		'F': 'Final'
	},
	certificateStatus: {
		100: '100 - Certificate Created',
		120: '120 - IPA sent',
		150: '150 - Certificate Confirmed',
		200: '200 - Certificate Waiting for Approval',
		300: '300 - Certificate Posted to Finance\'s AR'
	}

});