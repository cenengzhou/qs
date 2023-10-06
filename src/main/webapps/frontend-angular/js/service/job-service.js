mainApp.service('jobService', ['$http', '$q', 'GlobalHelper',  function($http, $q, GlobalHelper){
	// Return public API.
    return({
    	getJob: 		getJob,
    	getCompanyName: getCompanyName,
    	getJobList: 	getJobList,
    	getJobDates:	getJobDates,
    	updateJobInfo: 	updateJobInfo,
    	updateJobDates:	updateJobDates,
    	updateJobInfoAndDates: updateJobInfoAndDates,
    	updateParentCompanyGuarantee: updateParentCompanyGuarantee,
    	getJobDetailList: 	getJobDetailList,
    	obtainAllJobCompany: obtainAllJobCompany,
    	obtainAllJobDivision: obtainAllJobDivision,
        canAdminJob: canAdminJob
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
    
    function getCompanyName(jobNo) {
        var request = $http({
            method: "get",
            url: "service/job/getCompanyName",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getJobList(isCompletedJob) {
        var request = $http({
            method: "post",
            url: "service/job/getJobList",
            dataType: "application/json;charset=UTF-8",
            data:    	isCompletedJob
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

    function updateParentCompanyGuarantee(job) {
        var request = $http({
            method: "post",
            url: "service/job/updateParentCompanyGuarantee",
            data: job
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
 
    function obtainAllJobCompany() {
        var request = $http.get("service/job/obtainAllJobCompany");
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function obtainAllJobDivision() {
        var request = $http.get("service/job/obtainAllJobDivision");
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function canAdminJob(jobNo, adminJobNo) {
        var request = $http({
            method: "get",
            url: "service/job/canAdminJob",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
                adminJobNo: adminJobNo
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
}]);
