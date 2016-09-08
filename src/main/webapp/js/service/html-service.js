mainApp.service('htmlService', ['$http', '$q', 'GlobalHelper', function($http, $q, GlobalHelper){
	// Return public API.
    return({
    	makeHTMLStringForSCPaymentCert:		makeHTMLStringForSCPaymentCert,
    	makeHTMLStringForAddendumApproval:	makeHTMLStringForAddendumApproval,
    	makeHTMLStringForTenderAnalysis:	makeHTMLStringForTenderAnalysis
    });
   
    function makeHTMLStringForSCPaymentCert(requestObj){
    	var request = $http({
    		method: 'POST',
    		url: 'service/html/makeHTMLStrForPaymentCert',
    		data: requestObj
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function makeHTMLStringForAddendumApproval(requestObj){
    	var request = $http({
    		method: 'POST',
    		url: 'service/html/makeHTMLStrForAddendum',
    		data: requestObj
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function makeHTMLStringForTenderAnalysis(requestObj){
    	var request = $http({
    		method: 'POST',
    		url: 'service/html/makeHTMLStrForAward',
    		data: requestObj
		});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
}]);




