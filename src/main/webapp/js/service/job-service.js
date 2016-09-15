mainApp.service('jobService', ['$http', '$q', 'GlobalHelper',  function($http, $q, GlobalHelper){
	// Return public API.
    return({
    	getJob: 		getJob,
    	getJobList: 	getJobList,
    	getJobDates:	getJobDates,
    	updateJobInfo: 	updateJobInfo,
    	updateJobDates:	updateJobDates,
    	updateJobInfoAndDates: updateJobInfoAndDates,
    	getJobDetailList: 	getJobDetailList
    });
	
    function getJob(jobNo) {
        var request = $http({
            method: "get",
            url: "service/job/getJob",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getJobList() {
        var request = $http({
            method: "get",
            url: "service/job/getJobList",
            dataType: "application/json;charset=UTF-8"
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getJobDates(jobNo) {
        var request = $http({
            method: "get",
            url: "service/job/getJobDates",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function updateJobDates(jobDates) {
        var request = $http({
            method: "post",
            url: "service/job/updateJobDates",
            dataType: "application/json;charset=UTF-8",
            data: jobDates
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function updateJobInfo(job) {
        var request = $http({
            method: "post",
            url: "service/job/updateJobInfo",
            dataType: "application/json;charset=UTF-8",
            data: job
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function updateJobInfoAndDates(job, jobDates) {
        var request = $http({
            method: "post",
            url: "service/job/updateJobInfoAndDates",
            data: {job: job, jobDates: jobDates}
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    
    function getJobDetailList() {
        var request = $http({
            method: "POST",
            url: "service/job/getJobDetailList",
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
}]);



