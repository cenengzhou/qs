mainApp.service('jobcostService', ['$http', '$q', 'GlobalHelper',  function($http, $q, GlobalHelper){
	// Return public API.
    return({
    	getPORecordList:		getPORecordList,
    	getARRecordList:		getARRecordList,
    	obtainAPRecordList:	obtainAPRecordList,
    	getAPPaymentHistories:	getAPPaymentHistories
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

//    // ---
//    // PRIVATE METHODS.
//    // ---
//    // Transform the error response, unwrapping the application dta from
//    // the API response payload.
//    function handleError( response) {
//        // The API response from the server should be returned in a
//        // normalized format. However, if the request was not handled by the
//        // server (or what not handles properly - ex. server error), then we
//        // may have to normalize it on our end, as best we can.
//        if (
//            ! angular.isObject( response.data ) ||
//            ! response.data.message
//            ) {
//            return( $q.reject( "An unknown error occurred." ) );
//        }
//        // Otherwise, use expected error message.
//    	modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', response.data.message ); 
////        return( $q.reject( response.data.message ) );
//    }
//    // Transform the successful response, unwrapping the application data
//    // from the API response payload.
//    function handleSuccess( response ) {
//        return( response.data );
//    }
}]);




