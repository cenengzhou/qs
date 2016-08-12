mainApp.service('addendumService', ['$http', '$q',  function($http, $q){
	// Return public API.
    return({
    	getLatestAddendum: 					getLatestAddendum,
    	getAddendum:						getAddendum,
    	getAddendumList:					getAddendumList,
    	getTotalApprovedAddendumAmount: 	getTotalApprovedAddendumAmount,
    	getAddendumDetailHeader:			getAddendumDetailHeader,
    	getAddendumDetailsByHeaderRef:		getAddendumDetailsByHeaderRef,
    	getAllAddendumDetails:				getAllAddendumDetails,
    	getAddendumDetailsWithoutHeaderRef:	getAddendumDetailsWithoutHeaderRef,
    	createAddendum:						createAddendum,
    	updateAddendum:						updateAddendum,
    	updateAddendumDetailHeader:			updateAddendumDetailHeader,
    	deleteAddendumDetailHeader:			deleteAddendumDetailHeader,
    	addAddendumDetail:				addAddendumDetail,
    	addAddendumFromResourceSummaries: 	addAddendumFromResourceSummaries,
    	deleteAddendumDetail:				deleteAddendumDetail
    });
	
    function getLatestAddendum(jobNo, subcontractNo) {
        var request = $http({
            method: "get",
            url: "service/addendum/getLatestAddendum",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    

    function getAddendum(jobNo, subcontractNo, addendumNo) {
        var request = $http({
            method: "get",
            url: "service/addendum/getAddendum",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo,
            	addendumNo: addendumNo
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function getAddendumList(jobNo, subcontractNo) {
        var request = $http({
            method: "get",
            url: "service/addendum/getAddendumList",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function getTotalApprovedAddendumAmount(jobNo, subcontractNo) {
        var request = $http({
            method: "get",
            url: "service/addendum/getTotalApprovedAddendumAmount",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function getAddendumDetailHeader(addendumDetailHeaderRef) {
        var request = $http({
            method: "get",
            url: "service/addendum/getAddendumDetailHeader",
            dataType: "application/json;charset=UTF-8",
            params: {
            	addendumDetailHeaderRef: addendumDetailHeaderRef
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function getAddendumDetailsByHeaderRef(addendumDetailHeaderRef) {
        var request = $http({
            method: "get",
            url: "service/addendum/getAddendumDetailsByHeaderRef",
            dataType: "application/json;charset=UTF-8",
            params: {
            	addendumDetailHeaderRef: addendumDetailHeaderRef
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function getAllAddendumDetails(jobNo, subcontractNo, addendumNo) {
        var request = $http({
            method: "get",
            url: "service/addendum/getAllAddendumDetails",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo,
            	addendumNo: addendumNo
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function getAddendumDetailsWithoutHeaderRef(jobNo, subcontractNo, addendumNo) {
        var request = $http({
            method: "get",
            url: "service/addendum/getAddendumDetailsWithoutHeaderRef",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo,
            	addendumNo: addendumNo
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    
    function createAddendum(addendum) {
        var request = $http({
            method: "post",
            url: "service/addendum/createAddendum",
            dataType: "application/json;charset=UTF-8",
            data: addendum
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    
    function updateAddendum(addendum) {
        var request = $http({
            method: "post",
            url: "service/addendum/updateAddendum",
            dataType: "application/json;charset=UTF-8",
            data: addendum
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function updateAddendumDetailHeader(jobNo, subcontractNo, addendumNo, addendumDetailHeaderRef, description) {
        var request = $http({
            method: "post",
            url: "service/addendum/updateAddendumDetailHeader",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo,
            	addendumNo: addendumNo,
            	addendumDetailHeaderRef: addendumDetailHeaderRef,
            	description: description
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function deleteAddendumDetailHeader(addendumDetailHeaderRef) {
        var request = $http({
            method: "post",
            url: "service/addendum/deleteAddendumDetailHeader",
            dataType: "application/json;charset=UTF-8",
            params: {
            	addendumDetailHeaderRef: addendumDetailHeaderRef
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    
    function addAddendumDetail(jobNo, subcontractNo, addendumNo, addendumDetail) {
        var request = $http({
            method: "post",
            url: "service/addendum/addAddendumDetail",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo,
            	addendumNo: addendumNo
            },
            data: addendumDetail
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function addAddendumFromResourceSummaries(jobNo, subcontractNo, addendumNo, addendumDetailHeaderRef, resourceSummaryList) {
        var request = $http({
            method: "post",
            url: "service/addendum/addAddendumFromResourceSummaries",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo,
            	addendumNo: addendumNo,
            	addendumDetailHeaderRef: addendumDetailHeaderRef
            },
            data: resourceSummaryList
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function deleteAddendumDetail(jobNo, subcontractNo, addendumDetailList) {
        var request = $http({
            method: "post",
            url: "service/addendum/deleteAddendumDetail",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo
            },
            data: addendumDetailList
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




