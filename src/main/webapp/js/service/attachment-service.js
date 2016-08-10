mainApp.service('attachmentService', ['$http', '$q',  function($http, $q){
	// Return public API.
    return({
    	getRepackagingAttachments:		getRepackagingAttachments
    });
   
    function getRepackagingAttachments(repackagingEntryID){
    	var request = $http({
    		method: 'POST',
    		url: 'service/attachment/getRepackagingAttachments',
    		params:{
    			repackagingEntryID: repackagingEntryID
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




