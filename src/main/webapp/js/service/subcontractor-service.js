mainApp.service('subcontractorService', ['$http', '$q', 'GlobalHelper',  function($http, $q, GlobalHelper){
	// Return public API.
    return({
    	obtainSubcontractorWrappers:		obtainSubcontractorWrappers,
    	obtainClientWrappers:	obtainClientWrappers
    });
   
    function obtainSubcontractorWrappers(workScope, subcontractor){
    	var request = $http({
    		url: 'service/subcontractor/obtainSubcontractorWrappers',
    		method: 'POST',
    		params: {
    			workScope: workScope,
    			subcontractor: subcontractor
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function obtainClientWrappers(client){
    	var request = $http({
    		url: 'service/subcontractor/obtainClientWrappers',
    		method: 'POST',
    		params: {
    			client: client
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
//        return( $q.reject( response.data.message ) );
//    }
//    // Transform the successful response, unwrapping the application data
//    // from the API response payload.
//    function handleSuccess( response ) {
//        return( response.data );
//    }
}]);




