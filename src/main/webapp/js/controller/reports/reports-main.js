
mainApp.controller('ReportMainCtrl', ['$scope' , '$http', 'modalService', 'blockUI', '$window', '$cookies', 'GlobalParameter', 'GlobalHelper', 'modalService', '$sce', '$log', 'jobService', 'adlService', 'rootscopeService',
                                function($scope , $http, modalService, blockUI, $window, $cookies, GlobalParameter, GlobalHelper, modalService, $sce, $log, jobService, adlService, rootscopeService) {
	rootscopeService.setSelectedTips('');
	$scope.GlobalParameter = GlobalParameter;
	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");
	$scope.printPaymentCertDueDateType = true;
	$scope.printPaymentCertJobNumber = $scope.jobNo;
	$scope.reportPerRow = 3;
	$scope.repeatRange = repeatRange;
	$scope.onDownloadReport = onDownloadReport;
	$scope.today = moment().format(GlobalParameter.MOMENT_DATE);
	$scope.month = moment().format('MM');
	$scope.year = moment().format('YYYY');
	$scope.lastMonth = moment().month(moment().month() -1 ).format('YYYY-MM');
	$scope.companies = {};
	$scope.divisions = {};
	var commonParameters = ['company', 'division', 'jobNumber', 
       	                'subcontractNumber', 'subcontractorNumber', 'subcontractorNature', 
    	                'paymentType', 'workScope', 'clientNo', 'includeJobCompletionDate', 
    	                'splitTerminateStatus', 'month', 'year'
    	                ];
	var paymentCertParameters = ['dueDateType', 'dueDate', 'company', 'jobNumber'];
	var VALIDATE_COMPANY = '.{5,5}';
	var VALIDATE_COMPANY_MESSAGE = 'Please leave blank or enter 5 characters';
	var VALIDATE_DUEDATE = GlobalParameter.MOMENT_DATE_FORMAT;
	var VALIDATE_DUEDATE_MESSAGE = 'Please select date from the date picker';
	var VALIDATE_JOBNO = '.{1,5}';
	var VALIDATE_JOBNO_MESSAGE = 'Please leave blank or enter 5 characters';
	var VALIDATE_DIVISION = '.{3,3}';
	var VALIDATE_DIVISION_MESSAGE = 'Please leave blank or enter 3 characters';
	var reports = [
					{
						   id: 0,
						   name: 'paymentCertificateReport',
						   comingSoon: 'NO',
						   reportUrls: [
						                	{
					 	            	   type: 'xls',
					 	            	   url:'paymentCertificateEnquiryExcelExport',
					 	            	   parameters: paymentCertParameters
						                	}, 
						                	{
					 	            	   type: 'pdf',
					 	            	   url: 'printPaymentCertificateReportPdf',
					 	            	   parameters: paymentCertParameters
						                	}
						               ],
						   searchFields: [
						                 {field: 'company', value:'', inputType: 'company', mdMenuClass: 'company-code-and-name', minLength:0, maxLength:5, validateMessage: VALIDATE_COMPANY_MESSAGE,
						                  autoCompleteList: $scope.companies, querySearch: querySearch, selectedItemChange: selectedItemChange, searchTextChange: searchTextChange},
						                 {field: 'jobNumber', value:'', inputType: 'text', validatePattern: VALIDATE_JOBNO, validateMessage:VALIDATE_JOBNO_MESSAGE, label: 'Job No', defaultValue: $scope.jobNo},
						                 {field: 'dueDateType', inputType: 'dueDateType'},
						                 {field: 'dueDate', inputType: 'dueDate', validatePattern: VALIDATE_DUEDATE, validateMessage: VALIDATE_DUEDATE_MESSAGE}
					    ],
					    requiredFields:['dueDate'],
					    selectiveFields:['company', 'jobNumber']
					},
					{
						   id: 1,
						   name: 'subcontractReport',
						   comingSoon: 'NO',
						   reportUrls: [
					 	               {
					 	            	   type: 'xls',
					 	            	   url: 'financeSubcontractListDownload',
					 	            	   parameters: commonParameters
					 	               }, 
					 	               {
					 	            	   type: 'pdf',
					 	            	   url: 'subcontractReportExport',
					 	            	   parameters: commonParameters
						            	   }
						   ],
						   searchFields: [
						                 {field: 'company', value:'', inputType: 'company', mdMenuClass: 'company-code-and-name', minLength:0, maxLength:5, validateMessage: VALIDATE_COMPANY_MESSAGE,
						                  autoCompleteList: $scope.companies, querySearch: querySearch, selectedItemChange: selectedItemChange, searchTextChange: searchTextChange},
						                 {field: 'division', value:'', inputType: 'division', 
					             	  autoCompleteList: $scope.divisions, querySearch: querySearch, selectedItemChange: selectedItemChange, searchTextChange: searchTextChange},
						                 {field: 'jobNumber', inputType: 'text', validatePattern: VALIDATE_JOBNO, validateMessage:VALIDATE_JOBNO_MESSAGE, label: 'Job No', defaultValue: $scope.jobNo},
						                 {field: 'subcontractNumber', inputType: 'text', label: 'Subcontract No'},
						                 {field: 'subcontractorNumber', inputType: 'text', label: 'Subcontractor No'},
					  ],
					  selectiveFields:['company', 'jobNumber', 'division', 'subcontractorNumber']
					},
					{
						   id: 2,
						   name: 'subcontractLiabilityReport',
						   comingSoon: 'NO',
						   reportUrls: [
					 	               {
					 	            	   type: 'xls',
					 	            	   url: 'subcontractLiabilityExcelExport',
					 	            	   parameters: commonParameters
					 	               }, 
					 	               {
					 	            	   type: 'pdf',
					 	            	   url: 'subcontractLiabilityReportExport',
					 	            	   parameters:commonParameters
						            	   }
					    ],
						   searchFields: [
						                 {field: 'company', value:'', inputType: 'company', mdMenuClass: 'company-code-and-name', minLength:0, maxLength:5, validateMessage: VALIDATE_COMPANY_MESSAGE,
						                  autoCompleteList: $scope.companies, querySearch: querySearch, selectedItemChange: selectedItemChange, searchTextChange: searchTextChange},
						                 {field: 'division', value:'', inputType: 'division', 
					             	  autoCompleteList: $scope.divisions, querySearch: querySearch, selectedItemChange: selectedItemChange, searchTextChange: searchTextChange},
						                 {field: 'jobNumber', inputType: 'text', validatePattern: VALIDATE_JOBNO, validateMessage:VALIDATE_JOBNO_MESSAGE, label: 'Job No', defaultValue: $scope.jobNo},
						                 {field: 'subcontractNumber', inputType: 'text', label: 'Subcontract No'},
						                 {field: 'subcontractorNumber', inputType: 'text', label: 'Subcontractor No'},
						                 {field: 'periods', inputType: 'periods'}
					      ],
					      selectiveFields:['company', 'jobNumber', 'division', 'subcontractorNumber']
					},
					{
						   id: 3,
						   name: 'subcontractorAnalysisReport',
						   comingSoon: 'NO',
						   reportUrls: [
					 	               {
					 	            	   type: 'xls',
					 	            	   url: 'subcontractorAnalysisExcelExport',
					 	            	   parameters: commonParameters
					 	               }, 
					 	               {
					 	            	   type: 'pdf',
					 	            	   url: 'subcontractorAnalysisReportExport',
					 	            	   parameters: commonParameters
						            	   }
					 	               ],
						   searchFields: [
						                 {field: 'company', value:'', inputType: 'company', mdMenuClass: 'company-code-and-name', minLength:0, maxLength:5, validateMessage: VALIDATE_COMPANY_MESSAGE,
						                  autoCompleteList: $scope.companies, querySearch: querySearch, selectedItemChange: selectedItemChange, searchTextChange: searchTextChange},
						                 {field: 'division', value:'', inputType: 'division', 
					             	  autoCompleteList: $scope.divisions, querySearch: querySearch, selectedItemChange: selectedItemChange, searchTextChange: searchTextChange},
						                 {field: 'jobNumber', inputType: 'text', validatePattern: VALIDATE_JOBNO, validateMessage:VALIDATE_JOBNO_MESSAGE, label: 'Job No', defaultValue: $scope.jobNo},
						                 {field: 'subcontractNumber', inputType: 'text', label: 'Subcontract No'},
						                 {field: 'subcontractorNumber', inputType: 'text', label: 'Subcontractor No'},
						                 {field: 'periods', inputType: 'periods'}
					  ],
					  selectiveFields:['company', 'jobNumber', 'division', 'subcontractorNumber']
					},
					{
						   id: 4,
						   name: 'monthlyContractExpenditureReport',
						   comingSoon: 'YES',
						   searchFields: [
					 	                 {field: 'company', value:'', inputType: 'company'},
					 	                 {field: 'division', value:'', inputType: 'division'},
					 	                 {field: 'jobNo', inputType: 'text'},
					  ]
					},
					{
						   id: 5,
						   name: 'contractFinancialPerformanceReport',
						   comingSoon: 'YES',
						   searchFields: [
					 	                 {field: 'company', value:'', inputType: 'company'},
					 	                 {field: 'division', value:'', inputType: 'division'},
					 	                 {field: 'jobNumber', inputType: 'text', label: 'Job No'},
					 	                 ]
					},
					{
						   id: 6,
						   name: 'jobCostReport',
						   comingSoon: 'YES',
						   searchFields: [
					 	                 {field: 'company', value:'', inputType: 'company'},
					 	                 {field: 'division', value:'', inputType: 'division'},
					 	                 {field: 'jobNumber', inputType: 'text', label: 'Job No'},
					 	                 ]
					},
					{
						   id: 7,
						   name: 'cashFlowReport',
						   comingSoon: 'YES',
						   searchFields: [
					 	                 {field: 'company', value:'', inputType: 'company'},
					 	                 {field: 'division', value:'', inputType: 'division'},
					 	                 {field: 'jobNumber', inputType: 'text', label: 'Job No'},
					 	                 ]
					}
	];
	
	function onDownloadReport(report, selectedReportUrl){
		if(!validateFields(report, selectedReportUrl)) return;
 		var url = getUrlWithParameters(selectedReportUrl.url, selectedReportUrl.parameters, selectedReportUrl.searchValues)
 		var wnd = window.open(url, '_blank', 'width=400,height=240,left=0,top=0,location=no,menubar=no,titlebar=no,toolbar=no',true);
// 		console.log(url);
	}
	
	function validateFields(report, selectedReportUrl){
		var result = true;
		var msg = '';
		selectedReportUrl.searchValues = [];
		selectedReportUrl.parameters.forEach(function(parameter){
			selectedReportUrl.searchValues[parameter] = '';
 		})
 		report.searchFields.forEach(function(searchField){
 			selectedReportUrl.searchValues[searchField.field] = searchField.value != null ? searchField.value : '';
 			if(searchField.field === 'periods') {
 				if(searchField.value){
 					selectedReportUrl.searchValues['month'] = '';
 					selectedReportUrl.searchValues['year'] = '';
 				} else {
 					var d = searchField.monthEndHistory.split('-');
 					selectedReportUrl.searchValues['year'] = d[0]
 					selectedReportUrl.searchValues['month'] = d[1];
 				}
 			}
 			if(searchField.selectedItem && searchField.field === 'company') {
 				selectedReportUrl.searchValues['company'] = searchField.selectedItem.value;
 			}
 			
 			if(searchField.selectedItem && searchField.field === 'division') {
 				selectedReportUrl.searchValues['division'] = searchField.selectedItem.value;
 			}
 		})
		if(report.requiredFields){
			report.requiredFields.forEach(function(required){
				if(!selectedReportUrl.searchValues[required]) {
					msg += GlobalHelper.camelToNormalString(required) + ' is required<br>';
					result = false;
				}
			})
		}
		if(report.selectiveFields){
			var selectiveResult = false;
			report.selectiveFields.forEach(function(selective){
				if(selectedReportUrl.searchValues[selective]) selectiveResult = true;
			})
			if(!selectiveResult) {
				var selectiveMessage = '';
				report.selectiveFields.forEach(function(text){selectiveMessage += GlobalHelper.camelToNormalString(text).replace('Number', 'No.') + ', ';});
				msg += 'Please enter one of below field:<br>' + selectiveMessage.substring(0, selectiveMessage.length-2);
				result = false;
			}
		}
		
		if(msg) modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', msg);
		return result;
	}
	
	function getUrlWithParameters(link, parameters, values){
		var url = 'service/report/' + link + '?';
		parameters.forEach(function(parameter){
			if(parameter === 'dueDate' && values['dueDate']) values['dueDate'] = moment(values['dueDate']).format('DD/MM/YYYY');
			if(parameter === 'jobNumber' && values['jobNumber'] === '*') values['jobNumber'] = '';
			url += parameter + '=' + escape(values[parameter]) + '&';
			if(parameter === 'division' && values['division'] === 'E&M') values['division'] = 'EM';
		});
		return url.substring(0, url.length-1);
	}
	
	function repeatRange(n,c){
		var r = n%c;
		var t = r > 0 ? n/c+1 : n/c;
		var a = [];
		for (var i = 0; i< t; i++){
			a.push(i);
		}
		return a;
	}
	
	function querySearch (query, items) {
      var results = query ? items.filter( createFilterFor(query) ) : items;
      return results;
    }

    function searchTextChange(text) {
//      $log.info('Text changed to ' + text);
    }

    function selectedItemChange(item) {
//      $log.info('Item changed to ' + JSON.stringify(item));
    }
	
	function loadAllCompany() {
		rootscopeService.gettingCompanies()
		.then(function(response){
			$scope.companies = response.companies;
		});
		loadAllDivision()
    }

	function loadAllDivision() {
		rootscopeService.gettingDivisions()
		.then(function(response){
			$scope.divisions = response.divisions; 
  			processReport();
	      });
	    }

    function createFilterFor(query) {
      var lowercaseQuery = angular.lowercase(query);

      return function filterFn(item) {
        return (item.value.indexOf(lowercaseQuery) === 0);
      };

    }
    
	function processReport(){
		rootscopeService.gettingUser()
		.then(function(response){
			$scope.jobAll = GlobalHelper.containRole('JOB_ALL', response.user.authorities);
			if(!$scope.jobAll){
				VALIDATE_JOBNO = '.{5,5}';
			} else {
				VALIDATE_JOBNO = '.{1,5}';
			}
			reports.forEach(function(report){
				if(!report.title) report.title = GlobalHelper.camelToNormalString(report.name);
				if(report.searchFields) report.searchFields.forEach(function(searchField){
					if(!searchField.label) searchField.label = GlobalHelper.camelToNormalString(searchField.field)
					if(searchField.field === 'company') searchField.autoCompleteList = $scope.companies;
					if(searchField.field === 'division') searchField.autoCompleteList = $scope.divisions;
					if(searchField.field === 'jobNumber') searchField.validatePattern = VALIDATE_JOBNO;
				})
				
			});
		});
		reports.sort(function(a,b){return (a.id > b.id) ? 1 : ((b.id > a.id) ? -1 : 0);});
		$scope.reports = reports;
	}	
	
	loadAllCompany();
	
}]);