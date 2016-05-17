mainApp.service('ivService', ['$http', function($http){
	// Return public API.
    return({
        getIVList: getIVList,
        addResource: addResource
        
    });
	
    function getIVList() {
        var request = $http({
            method: "get",
            url: "data/iv.json"
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function addResource() {
        var request = $http({
            method: "get",
            url: "data/iv.json",
            params: {
                action: "add"
            },
            data: {
                resource: resource
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    
    // ---
    // PRIVATE METHODS.
    // ---
    // Transform the error response, unwrapping the application dta from
    // the API response payload.
    function handleError( response ) {
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

