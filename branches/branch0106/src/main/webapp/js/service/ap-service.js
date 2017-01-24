mainApp.service('apService', ['$http', 'GlobalHelper', 
					function($http, GlobalHelper){
	// Return public API.
    return({
    	completeAwardApproval:		completeAwardApproval,
    	completePaymentApproval:	completePaymentApproval,
    	completeSplitTerminateApproval: completeSplitTerminateApproval,
    	completeAddendumApproval: completeAddendumApproval,
    	completeMainCertApproval: completeMainCertApproval
    });
   
    function completeAwardApproval(params){
    	var request = $http.get('service/ap/completeAwardApproval/' + params['jobNumber'] + '/' + params['packageNo'] + '/' + params['approvedOrRejected']);
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function completePaymentApproval(params){
    	var request = $http.get('service/ap/completePaymentApproval/' + params['jobNumber'] + '/' + params['packageNo'] + '/' + params['approvedOrRejected']);
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function completeSplitTerminateApproval(params){
    	var request = $http.get('service/ap/completeSplitTerminateApproval/' + params['jobNumber'] + '/' + params['packageNo'] + '/' + params['approvedOrRejected'] + '/' + params['splitOrTerminate']);
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function completeAddendumApproval(params){
    	var request = $http.get('service/ap/completeAddendumApproval/' + params['jobNumber'] + '/' + params['packageNo'] + '/' + params['username'] + '/' + params['approvedOrRejected']);
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function completeMainCertApproval(params){
    	var request = $http.get('service/ap/completeMainCertApproval/' + params['jobNumber'] + '/' + params['mainCertNo'] + '/' + params['approvedOrRejected']);
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

}]);




