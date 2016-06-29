mainApp.service('repackagingService', ['$http', '$q', function($http, $q){
	// Return public API.
    return({
    	getResourceSummaries: 			getResourceSummaries,
    	getLatestRepackagingEntry:		getLatestRepackagingEntry,
    	getRepackagingEntriesByJobNo: 	getRepackagingEntriesByJobNo,
    	
    	addResourceSummary:				addResourceSummary,
    	addRepackagingEntry:			addRepackagingEntry,
    	updateRepackagingEntry:			updateRepackagingEntry,
    	deleteRepackagingEntry:			deleteRepackagingEntry,
    	generateSnapshot:				generateSnapshot
        
    });
	
    function getResourceSummaries(jobNo, packageNo, objectCode) {
        var request = $http({
            method: "get",
            url: "service/repackaging/repackaging/getResourceSummaries",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	packageNo: packageNo,
            	objectCode: objectCode
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }

    function getLatestRepackagingEntry(jobNo) {
        var request = $http({
            method: "get",
            url: "service/repackaging/repackaging/getLatestRepackagingEntry",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function getRepackagingEntriesByJobNo(jobNo) {
        var request = $http({
            method: "get",
            url: "service/repackaging/repackaging/getRepackagingEntriesByJobNo",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function addRepackagingEntry(jobNo) {
        var request = $http({
            method: "post",
            url: "service/repackaging/addRepackagingEntry",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function addResourceSummary(jobNo, repackagingEntryId, resourceSummary) {
        var request = $http({
            method: "post",
            url: "service/repackaging/addResourceSummary",
           // dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	repackagingEntryId: repackagingEntryId
            },
            data: resourceSummary
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function updateRepackagingEntry(repackaging) {
        var request = $http({
            method: "post",
            url: "service/repackaging/updateRepackagingEntry",
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
    
    function deleteRepackagingEntry(id) {
        var request = $http({
            method: "delete",
            url: "service/repackaging/deleteRepackagingEntry",
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

