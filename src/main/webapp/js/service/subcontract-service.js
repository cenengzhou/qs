mainApp.service('subcontractService', ['$http', 'Base64', '$q',  function($http, Base64, $q){
	// Return public API.
    return({
    	getSubcontract: 									getSubcontract,
    	getSubcontractList: 								getSubcontractList,
    	getWorkScope: 										getWorkScope,
    	getSCDetails:										getSCDetails,
    	getSubcontractDetailForWD:							getSubcontractDetailForWD,
    	getSubcontractDetailsDashboardData:					getSubcontractDetailsDashboardData,
    	getSubcontractDashboardData:						getSubcontractDashboardData,
    	getAwardedSubcontractNos:							getAwardedSubcontractNos,
    	getUnawardedSubcontractNosUnderPaymentRequisition: 	getUnawardedSubcontractNosUnderPaymentRequisition,
    	
    	upateSubcontract: 									upateSubcontract,
    	upateSubcontractDates: 								upateSubcontractDates,
    	updateWDandIV:										updateWDandIV,
    	updateWDandIVByPercent:								updateWDandIVByPercent,
    	submitAwardApproval:								submitAwardApproval,
    	recalculateResourceSummaryIV:						recalculateResourceSummaryIV,
    	
    	runProvisionPostingManually:						runProvisionPostingManually,
    	generateSCPackageSnapshotManually: 					generateSCPackageSnapshotManually,
    	updateF58001FromSCPackageManually: 					updateF58001FromSCPackageManually,
    	searchSystemConstants:								searchSystemConstants,
    	updateMultipleSystemConstants:						updateMultipleSystemConstants,
    	inactivateSystemConstant:							inactivateSystemConstant,
    	createSystemConstant: 								createSystemConstant,
    	updateSubcontractAdmin:								updateSubcontractAdmin,

    	getSubcontractSnapshotList:							getSubcontractSnapshotList,
    	getProvisionPostingHistList:						getProvisionPostingHistList,
    	getSCDetailList:									getSCDetailList,
    	getSCDetailForAddendumUpdate:						getSCDetailForAddendumUpdate
    });
	
    function getSubcontractList(jobNo, awardedOnly) {
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
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function getSCDetails(jobNo, subcontractNo) {
        var request = $http({
            method: "get",
            url: "service/subcontract/getSCDetails",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function getSubcontractDetailForWD(jobNo, subcontractNo) {
        var request = $http({
            method: "get",
            url: "service/subcontract/getSubcontractDetailForWD",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function getSubcontractDashboardData(jobNo, subcontractNo, year) {
        var request = $http({
            method: "get",
            url: "service/subcontract/getSubcontractDashboardData",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo, 
            	year: year
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function getSubcontractDetailsDashboardData(jobNo, subcontractNo) {
        var request = $http({
            method: "get",
            url: "service/subcontract/getSubcontractDetailsDashboardData",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    
    function getAwardedSubcontractNos(jobNo) {
        var request = $http({
            method: "get",
            url: "service/subcontract/getAwardedSubcontractNos",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function getUnawardedSubcontractNosUnderPaymentRequisition(jobNo) {
        var request = $http({
            method: "get",
            url: "service/subcontract/getUnawardedSubcontractNosUnderPaymentRequisition",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function getWorkScope(workScopeCode) {
        var request = $http({
            method: "get",
            url: "service/subcontract/getWorkScope",
            dataType: "application/json;charset=UTF-8",
            params: {
            	workScopeCode: workScopeCode
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    
    function upateSubcontract(jobNo, subcontract) {
        var request = $http({
            method: "post",
            url: "service/subcontract/upateSubcontract",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo
            },
            data: subcontract
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function upateSubcontractDates(jobNo, subcontract) {
        var request = $http({
            method: "post",
            url: "service/subcontract/upateSubcontractDates",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo
            },
            data: subcontract
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function updateWDandIV(jobNo, subcontractNo, scDetail) {
        var request = $http({
            method: "post",
            url: "service/subcontract/updateWDandIV",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo,
                subcontractNo: subcontractNo
            },
            data: scDetail
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function updateWDandIVByPercent(jobNo, subcontractNo, percent) {
        var request = $http({
            method: "post",
            url: "service/subcontract/updateWDandIVByPercent",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo,
                subcontractNo: subcontractNo,
                percent: percent
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function recalculateResourceSummaryIV(jobNo, subcontractNo, recalculateFinalizedPackage) {
        var request = $http({
            method: "post",
            url: "service/subcontract/recalculateResourceSummaryIV",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo,
                subcontractNo: subcontractNo,
                recalculateFinalizedPackage: recalculateFinalizedPackage
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function submitAwardApproval(jobNo, subcontractNo) {
        var request = $http({
            method: "post",
            url: "service/subcontract/submitAwardApproval",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo,
                subcontractNo: subcontractNo
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function runProvisionPostingManually(jobNumber, glDate){
    	var request = $http({
    		method: 'post',
    		url: 'service/subcontract/runProvisionPostingManually',
    		dataType: "application/json;charset=UTF-8",
    		params: {
    			jobNumber: jobNumber,
    			glDate: glDate
    		}
    	});
    	return( request.then( handleSuccess, handleError ) );
    }
    
    function generateSCPackageSnapshotManually(){
    	var request = $http.post("service/subcontract/generateSCPackageSnapshotManually");
    	return( request.then( handleSuccess, handleError ) );
    }
    
    function updateF58001FromSCPackageManually(){
    	var request = $http.post("service/subcontract/updateF58001FromSCPackageManually");
    	return( request.then( handleSuccess, handleError ) );
    }

	function searchSystemConstants(){
    	var request = $http.post("service/subcontract/searchSystemConstants");
    	return( request.then(handleSuccess, handleError));
    }
    
	function updateMultipleSystemConstants(systemConstants){
    	var request = $http.post("service/subcontract/updateMultipleSystemConstants", systemConstants);
    	return( request.then(handleSuccess, handleError));
    }
    
	function inactivateSystemConstant(systemConstants){
    	var request = $http.post('service/subcontract/inactivateSystemConstant', systemConstants);
    	return( request.then(handleSuccess, handleError));
    }
    
	function createSystemConstant(newRecord){
    	var request = $http.post('service/subcontract/createSystemConstant', newRecord);
    	return( request.then(handleSuccess, handleError));
    }
    
    function updateSubcontractAdmin(subcontract){
       	var request = $http.post('service/subcontract/updateSubcontractAdmin', subcontract);
    	return( request.then(handleSuccess, handleError));
    }
    
    function getSubcontractSnapshotList(noJob, year, month, awardedOnly, showJobInfo){
    	var request = $http({
    		method: 'get',
    		url: 'service/subcontract/getSubcontractSnapshotList',
    		params: {
    			noJob: noJob,
    			year: year,
    			month: month,
    			awardedOnly: awardedOnly,
    			showJobInfo: showJobInfo
    		}
    	});
    	return( request.then( handleSuccess, handleError ) );
    }
    
    function getProvisionPostingHistList(jobNo, subcontractNo, year, month){
    	var request = $http({
    		method: 'get',
    		url: 'service/subcontract/getProvisionPostingHistList',
    		params: {
    			jobNo: jobNo,
    			subcontractNo: subcontractNo,
    			year: year,
    			month: month
    		}
    	});
    	return( request.then( handleSuccess, handleError ) );
    }
    
    function getSCDetailList(jobNo) {
        var request = $http({
            method: "get",
            url: "service/subcontract/getSCDetailList",
            params: {
            	jobNo: jobNo
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    

    function getSCDetailForAddendumUpdate(jobNo, subcontractNo) {
        var request = $http({
            method: "get",
            url: "service/subcontract/getSCDetailForAddendumUpdate",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo
            }
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




