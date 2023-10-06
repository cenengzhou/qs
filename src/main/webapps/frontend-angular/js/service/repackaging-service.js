mainApp.service('repackagingService', ['$http', '$q', 'GlobalHelper', function($http, $q, GlobalHelper){
	// Return public API.
    return({
    	getRepackagingEntry:				getRepackagingEntry,
    	getRepackaging:						getRepackaging,
    	getLatestRepackaging:				getLatestRepackaging,
    	getRepackagingListByJobNo: 			getRepackagingListByJobNo,
    	getRepackagingDetails:				getRepackagingDetails, 
    	
    	addRepackaging:						addRepackaging,
    	updateRepackaging:					updateRepackaging,
    	deleteRepackaging:					deleteRepackaging,
    	
    	generateSnapshot:					generateSnapshot,
    	confirmAndPostRepackaingDetails: 	confirmAndPostRepackaingDetails,
    	sendEmailToReviewer:				sendEmailToReviewer,
    	getReviewerList:					getReviewerList,
    	getReviewerListFromGSF:				getReviewerListFromGSF,
    });
	
    function getRepackagingEntry(repackagingID) {
        var request = $http({
            method: "get",
            url: "service/repackaging/getRepackagingEntry",
            dataType: "application/json;charset=UTF-8",
            params: {
            	repackagingID: repackagingID
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getRepackaging(jobNo, version) {
        var request = $http({
            method: "get",
            url: "service/repackaging/getRepackaging",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	version: version
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getLatestRepackaging(jobNo) {
        var request = $http({
            method: "get",
            url: "service/repackaging/getLatestRepackaging",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
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
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getRepackagingDetails(repackagingID, changesOnly) {
        var request = $http({
            method: "get",
            url: "service/repackaging/getRepackagingDetails",
            dataType: "application/json;charset=UTF-8",
            params: {
            	repackagingID: repackagingID,
            	changesOnly: changesOnly
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
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
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function updateRepackaging(repackaging) {
        var request = $http({
            method: "post",
            url: "service/repackaging/updateRepackaging",
            dataType: "application/json;charset=UTF-8",
            data : repackaging
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
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
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
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
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    
    function confirmAndPostRepackaingDetails(repackagingID) {
        var request = $http({
            method: "post",
            url: "service/repackaging/confirmAndPostRepackaingDetails",
            dataType: "application/json;charset=UTF-8",
            params: {
            	repackagingID: repackagingID
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function sendEmailToReviewer(mailData) {
        var request = $http({
            method: "post",
            url: "service/repackaging/sendEmailToReviewer",
            data: mailData
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getReviewerList() {
        var request = $http({
            method: "get",
            url: "service/repackaging/getReviewerList",
            dataType: "application/json;charset=UTF-8"
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
  
    function getReviewerListFromGSF(jobNo) {
        var request = $http({
            method: "post",
            params: {
            	jobNo:jobNo
        	},
            url: "service/repackaging/getReviewerListFromGSF",
            dataType: "application/json;charset=UTF-8"
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

}]);

