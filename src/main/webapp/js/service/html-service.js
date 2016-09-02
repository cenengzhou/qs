mainApp.service('htmlService', ['$http', '$q', 'GlobalHelper', function($http, $q, GlobalHelper){
	// Return public API.
    return({
    	makeHTMLStringForSCPaymentCert:		makeHTMLStringForSCPaymentCert,
    	makeHTMLStringForAddendumApproval:	makeHTMLStringForAddendumApproval,
    	makeHTMLStringForTenderAnalysis:	makeHTMLStringForTenderAnalysis
    });
   
    function makeHTMLStringForSCPaymentCert(jobNumber, subcontractNumber, paymentNo, htmlVersion){
    	var request = $http({
    		method: 'POST',
    		url: 'service/html/makeHTMLStringForSCPaymentCert',
    		params:{
    			jobNumber: jobNumber,
    			subcontractNumber: subcontractNumber,
    			paymentNo: paymentNo,
    			htmlVersion: htmlVersion
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function makeHTMLStringForAddendumApproval(jobNumber, subcontractNumber, addendumNo, htmlVersion){
    	var request = $http({
    		method: 'POST',
    		url: 'service/html/makeHTMLStringForAddendumApproval',
    		params:{
    			jobNumber: jobNumber,
    			subcontractNumber: subcontractNumber,
    			addendumNo: addendumNo,
    			htmlVersion: htmlVersion
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function makeHTMLStringForTenderAnalysis(noJob, noSubcontract, htmlVersion){
    	var request = $http({
    		method: 'POST',
    		url: 'service/html/makeHTMLStringForTenderAnalysis',
    		params:{
    			noJob: noJob,
    			noSubcontract: noSubcontract,
    			htmlVersion: htmlVersion
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
//        modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', response.data.message );
////        return( $q.reject( response.data.message ) );
//    }
//    // Transform the successful response, unwrapping the application data
//    // from the API response payload.
//    function handleSuccess( response ) {
//        return( response.data );
//    }
}]);




