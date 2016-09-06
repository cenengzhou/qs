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
    
}]);




