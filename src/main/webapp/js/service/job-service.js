mainApp.service('jobService', ['$http', function($http){
	// Return public API.
    return({
    	obtainJobInfo: obtainJobInfo,
    	obtainJobDashboardData: obtainJobDashboardData,
    });
	
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




/*mainApp.service('jobService', ['$http', function($http){
    var job = [{
        'jobNo': '13362',
        'jobDescription': 'Pak Shek Kok, Tai Po, TPTL201'
    }];
    
	//simply returns the contacts list
    this.jobList = function () {
        return job;
    }
	
	 this.jobNumber= function(){
	        return "13362";
	    };    
}]);*/



