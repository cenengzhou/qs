mainApp.service('transitService', ['$http', '$q',  function($http, $q){
	// Return public API.
    return({
    	obtainTransitCodeMatcheList:	obtainTransitCodeMatcheList,
    	obtainTransitUomMatcheList:		obtainTransitUomMatcheList,
    	transitUpload:					transitUpload
    });
   
    function obtainTransitCodeMatcheList(){
    	var request = $http.post("service/transit/obtainTransitCodeMatcheList");
    	return( request.then( handleSuccess, handleError ) );
    }

    function obtainTransitUomMatcheList(){
    	var request = $http.post("service/transit/obtainTransitUomMatcheList");
    	return( request.then( handleSuccess, handleError ) );
    }

    function transitUpload(formData){
    	var request = $http({
			method : 'POST',
			url : 'gammonqs/transitUpload.smvc',
			data : formData,
			headers : {
				'Content-Type' : undefined
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



