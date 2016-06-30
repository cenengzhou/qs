mainApp.service('repackagingService', ['$http', '$q', function($http, $q){
	// Return public API.
    return({
    	getLatestRepackaging:		getLatestRepackaging,
    	getRepackagingListByJobNo: 	getRepackagingListByJobNo,
    	
    	addRepackaging:			addRepackaging,
    	updateRepackaging:				updateRepackaging,
    	deleteRepackaging:			deleteRepackaging,
    	generateSnapshot:				generateSnapshot
        
    });
	
    function getLatestRepackaging(jobNo) {
        var request = $http({
            method: "get",
            url: "service/repackaging/getLatestRepackaging",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function getRepackagingListByJobNo(jobNo) {
        var request = $http({
            method: "get",
            url: "service/repackaging/getRepackagingListByJobNo",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function addRepackaging(jobNo) {
        var request = $http({
            method: "post",
            url: "service/repackaging/addRepackaging",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function updateRepackaging(repackaging) {
        var request = $http({
            method: "post",
            url: "service/repackaging/updateRepackaging",
            dataType: "application/json;charset=UTF-8",
            data : repackaging
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function generateSnapshot(id, jobNo) {
        var request = $http({
            method: "post",
            url: "service/repackaging/generateSnapshot",
            dataType: "application/json;charset=UTF-8",
            params: {
            	id: id,
                jobNo: jobNo
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function deleteRepackaging(id) {
        var request = $http({
            method: "delete",
            url: "service/repackaging/deleteRepackaging",
            dataType: "application/json;charset=UTF-8",
            params: {
                id: id
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

