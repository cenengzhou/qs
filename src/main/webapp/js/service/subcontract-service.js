mainApp.service('subcontractService', ['$http', 'Base64', '$q',  function($http, Base64, $q){
	// Return public API.
    return({
    	getSubcontract: 	getSubcontract,
    	getSubcontractList: getSubcontractList,
    	addSubcontract: 	addSubcontract,
    	getWorkScope: 		getWorkScope,
    	obtainVendorInfo: 	obtainVendorInfo
    	
    });
	
    function getSubcontractList(jobNo) {
    	var myHeaders = {
    	        "Accept": "application/json",
    	        "Content-Type": "application/json",
    	        //'Authorization': 'Basic ' + Base64.encode("peer" + ":" + "bkend-srv-1234")
    	    };
    	
        var request = $http({
            method: "get",
            //headers: myHeaders,
            url: "service/subcontract/getSubcontractList",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo
            }
    	/*var request = $http({
            method: "get",
            url: "/gammonqs/subcontractReportExport.rpt",
            params: {
            	jobNumber: "13362",
            	subcontractNumber:"",
            	subcontractorNumber:"",
            	subcontractorNature:"",
            	paymentType:"",
            	workScope:"",
            	clientNo:"",
            	includeJobCompletionDate:"",
            	splitTerminateStatus:"",
            	month:"",
            	year:""
            },
            responseType: 'arraybuffer'
            */
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function getSubcontract(jobNo, subcontractNo) {
        var request = $http({
            method: "get",
            url: "service/subcontract/getSubcontract",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function getWorkScope(workScopeCode) {
        var request = $http({
            method: "get",
            url: "service/subcontract/getWorkScope",
            params: {
            	workScopeCode: workScopeCode
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function addSubcontract(jobNo, subcontract) {
        var request = $http({
            method: "post",
            url: "service/subcontract/addSubcontract",
            params: {
                jobNo: jobNo
            },
            data: subcontract
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function obtainVendorInfo() {
        var request = $http({
            method: "get",
            url: "data/vendor-info.json"
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




