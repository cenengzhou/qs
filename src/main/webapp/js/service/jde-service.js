mainApp.service('jdeService', ['$http', '$q', 'GlobalHelper',  function($http, $q, GlobalHelper){
	// Return public API.
    return({
    	getPORecordList:			getPORecordList,
    	getARRecordList:			getARRecordList,
    	obtainAPRecordList:			obtainAPRecordList,
    	getAPPaymentHistories:		getAPPaymentHistories,
    	createAccountMasterByGroup: createAccountMasterByGroup,
    	
		getSubcontractor:				getSubcontractor,
		getSubcontractorList:			getSubcontractorList,
		searchObjectList:				searchObjectList,
		searchSubsidiaryList:			searchSubsidiaryList,
		validateAndCreateAccountCode:	validateAndCreateAccountCode,
		getSubcontractorWorkScope:		getSubcontractorWorkScope,
		searchVendorAddressDetails:		searchVendorAddressDetails,
		
		postBudget:		postBudget,
		getAccountBalanceByDateRangeList:	getAccountBalanceByDateRangeList,
		getAccountLedgerListByAccountCodeList:	getAccountLedgerListByAccountCodeList,
    	
    	getAllWorkScopes:		getAllWorkScopes,
    	getUnitOfMeasurementList: getUnitOfMeasurementList,
    	getAppraisalPerformanceGroupMap: getAppraisalPerformanceGroupMap,
    	getSCStatusCodeMap: getSCStatusCodeMap,
    });
 
    function getPORecordList(jobNumber, orderNumber, orderType, supplierNumber){
    	var request = $http({
    		method: 'POST',
    		url: 'service/jde/getPORecordList',
    		params:{
    			jobNumber: jobNumber,
    			orderNumber: orderNumber,
    			orderType: orderType,
    			supplierNumber: supplierNumber
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getARRecordList(jobNumber, reference, customerNumber, documentNumber, documentType){
    	var request = $http({
    		method: 'POST',
    		url: 'service/jde/getARRecordList',
    		params:{
    			jobNumber: jobNumber,
    			reference: reference,
    			customerNumber: customerNumber,
    			documentNumber: documentNumber,
    			documentType: documentType
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function obtainAPRecordList(jobNumber, invoiceNumber, supplierNumber, documentNumber, documentType, subledger, subledgerType){
    	var request = $http({
    		method: 'POST',
    		url: 'service/jde/obtainAPRecordList',
    		params:{
    			jobNumber: jobNumber,
    			invoiceNumber: invoiceNumber,
    			supplierNumber: supplierNumber,
    			documentNumber: documentNumber,
    			documentType: documentType,
    			subledger: subledger,
    			subledgerType: subledgerType
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function getAPPaymentHistories(company, documentType, supplierNumber, documentNumber){
    	var request = $http({
    		method: 'POST',
    		url: 'service/jde/getAPPaymentHistories',
    		params:{
    			company: company,
    			documentType: documentType,
    			supplierNumber: supplierNumber,
    			documentNumber: documentNumber
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    
    function createAccountMasterByGroup(jobNo, resourceCheck, resourceSummaryCheck, scDetailCheck, forecastCheck){
    	var request = $http({
    		method: 'POST',
    		url: 'service/jde/createAccountMasterByGroup',
    		params:{
    			jobNo: jobNo,
    			resourceCheck: resourceCheck,
    			resourceSummaryCheck: resourceSummaryCheck,
    			scDetailCheck: scDetailCheck,
    			forecastCheck: forecastCheck
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
	function getSubcontractor(subcontractorNo) {
		var request = $http({
			method: "get",
			url: "service/jde/getSubcontractor",
			dataType: "application/json;charset=UTF-8",
			params: {
				subcontractorNo: subcontractorNo
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
	
	
	function getSubcontractorList(searchStr) {
		var request = $http({
			method: "get",
			url: "service/jde/getSubcontractorList",
			dataType: "application/json;charset=UTF-8",
			params: {
				searchStr: searchStr
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
	

	function searchObjectList(searchStr) {
		var request = $http({
			method: "POST",
			url: "service/jde/searchObjectList",
			params: {
				searchStr: searchStr
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
	
	function searchSubsidiaryList(searchStr) {
		var request = $http({
			method: "POST",
			url: "service/jde/searchSubsidiaryList",
			params: {
				searchStr: searchStr
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
	
	function validateAndCreateAccountCode(jobNo, objectCode, subsidiaryCode) {
		var request = $http({
			method: "POST",
			url: "service/jde/validateAndCreateAccountCode",
			params: {
				jobNo: jobNo,
				objectCode: objectCode,
				subsidiaryCode: subsidiaryCode
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
	
	function getSubcontractorWorkScope(vendorNo) {
		var request = $http({
			method: "POST",
			url: "service/jde/getSubcontractorWorkScope",
			params: {
				vendorNo: vendorNo
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
	
	function searchVendorAddressDetails(vendorNo) {
		var request = $http({
			method: "POST",
			url: "service/jde/searchVendorAddressDetails",
			params: {
				vendorNo: vendorNo
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
	
    function postBudget(jobNo){
    	var request = $http({
    		method: 'POST',
    		url: 'service/jde/postBudget',
    		params:{
    			jobNumber: jobNo
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getAccountBalanceByDateRangeList(jobNumber, subLedger, subLedgerType, totalFlag, postFlag, fromDate, thruDate, year, period){
    	var request = $http({
    		method: 'get',
    		url: 'service/jde/getAccountBalanceByDateRangeList',
    		params:{
    			jobNumber: jobNumber,
    			subLedger: subLedger,
    			subLedgerType: subLedgerType,
    			totalFlag: totalFlag,
    			postFlag: postFlag,
    			fromDate: fromDate,
    			thruDate: thruDate,
    			year: year,
    			period: period
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getAccountLedgerListByAccountCodeList(accountCode, postFlag, ledgerType, fromDate, thruDate, subLedgerType, subLedger){
    	var request = $http({
    		method: 'get',
    		url: 'service/jde/getAccountLedgerListByAccountCodeList',
    		params:{
    			accountCode: accountCode,
    			postFlag: postFlag,
    			ledgerType: ledgerType,
    			fromDate: fromDate,
    			thruDate: thruDate,
    			subLedgerType: subLedgerType,
    			subLedger: subLedger
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getAllWorkScopes(){
    	var request = $http.get('service/jde/getAllWorkScopes');
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getUnitOfMeasurementList(){
    	var request = $http.get('service/jde/getUnitOfMeasurementList');
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function getAppraisalPerformanceGroupMap(){
    	var request = $http.get('service/jde/getAppraisalPerformanceGroupMap');
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function getSCStatusCodeMap(){
    	var request = $http.get('service/jde/getSCStatusCodeMap');
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

}]);




