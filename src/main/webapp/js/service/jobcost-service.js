mainApp.service('jobcostService', ['$http', '$q', 'GlobalHelper',  function($http, $q, GlobalHelper){
	// Return public API.
    return({
    	getPORecordList:			getPORecordList,
    	getARRecordList:			getARRecordList,
    	obtainAPRecordList:			obtainAPRecordList,
    	getAPPaymentHistories:		getAPPaymentHistories,
    	createAccountMasterByGroup: createAccountMasterByGroup
    });
 
    function getPORecordList(jobNumber, orderNumber, orderType, supplierNumber){
    	var request = $http({
    		method: 'POST',
    		url: 'service/jobcost/getPORecordList',
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
    		url: 'service/jobcost/getARRecordList',
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
    		url: 'service/jobcost/obtainAPRecordList',
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
    		url: 'service/jobcost/getAPPaymentHistories',
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
    		url: 'service/jobcost/createAccountMasterByGroup',
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
    
}]);




