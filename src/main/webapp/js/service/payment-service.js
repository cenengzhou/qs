mainApp.service('paymentService', ['$http', '$q',  function($http, $q){
	// Return public API.
    return({
    	getSCPaymentCertList: getSCPaymentCertList,
    	
    });
	
    function getSCPaymentCertList(jobNo, subcontractNo) {
    	var myHeaders = {
    	        "Accept": "application/json",
    	        "Content-Type": "application/json",
    	        //'Authorization': 'Basic ' + Base64.encode("peer" + ":" + "bkend-srv-1234")
    	    };
    	
        var request = $http({
            method: "post",
            //headers: myHeaders,
            url: "service/getSCPaymentCertList.json",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo
            }
    
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    
    // ---
    // PRIVATE METHODS.
    // ---
    // Transform the error response, unwrapping the application dta from
    // the API response payload.
    function handleError( response) {
        // The API response from the server should be returned in a
        // normalized format. However, if the request was not handled by the
        // server (or what not handles properly - ex. server error), then we
        // may have to normalize it on our end, as best we can.
        if (
            ! angular.isObject( response.data ) ||
            ! response.data.message
            ) {
            return( $q.reject( "An unknown error occurred." ) );
        }
        // Otherwise, use expected error message.
        return( $q.reject( response.data.message ) );
    }
    // Transform the successful response, unwrapping the application data
    // from the API response payload.
    function handleSuccess( response ) {
        return( response.data );
    }
}]);




