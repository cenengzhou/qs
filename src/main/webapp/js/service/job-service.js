mainApp.service('jobService', ['$http', function($http){
	// Return public API.
    return({
    	getJob: getJob,
    	getJobList: getJobList,
    	obtainJobInfo: obtainJobInfo,
    	obtainJobDashboardData: obtainJobDashboardData,
    });
	
    function getJob(jobNo) {
        var request = $http({
            method: "get",
            //headers: myHeaders,
            url: "service/job/getJob",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function getJobList() {
        var request = $http({
            method: "get",
            //headers: myHeaders,
            url: "service/job/getJobList",
            dataType: "application/json;charset=UTF-8"
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function obtainJobInfo() {
        var request = $http({
            method: "get",
            url: "data/job.json"
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function obtainJobDashboardData() {
        var request = $http({
            method: "get",
            url: "data/job-dashboard.json"
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



