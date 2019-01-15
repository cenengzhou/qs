mainApp.service('personnelService', ['$http', '$q', '$log', 'GlobalHelper',  function($http, $q, $log, GlobalHelper){
	// Return public API.
    return({
    	save:				save,
    	saveList:			saveList,
    	deletePersonnel:	deletePersonnel,
    	clearDraft:			clearDraft,
    	submitForApproval:	submitForApproval,
    	cancelApproval:		cancelApproval,
    	returnDecisionMap: 	returnDecisionMap,
    	returnDecisionMapHtml: 	returnDecisionMapHtml,
    	getActivePersonnel:	getActivePersonnel,
    	getAllPersonnelMap:	getAllPersonnelMap
    });
	
    function deletePersonnel(id) {
		var request = $http({
			method: "DELETE",
			url: "service/personnel/" + id,
//			data: personnel
//			params: {
//				paymentCertId: paymentCertId
//			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );    	
    }
    
    function save(jobNo, personnel) {
		var request = $http({
			method: "post",
			url: "service/personnel/save/" + jobNo,
			data: personnel
//			params: {
//				paymentCertId: paymentCertId
//			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
    
    function saveList(jobNo, personnelList) {
		var request = $http({
			method: "post",
			url: "service/personnel/saveList/" + jobNo,
			data: personnelList
//			params: {
//				paymentCertId: paymentCertId
//			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

    function clearDraft(jobNo) {
		var request = $http({
			method: "post",
			url: "service/personnel/clearDraft",
//			data: personnel
			params: {
				jobNo: jobNo
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
    
    function submitForApproval(jobNo) {
		var request = $http({
			method: "post",
			url: "service/personnel/submitForApproval/" + jobNo,
//			data: personnel
//			params: {
//				jobNo: jobNo
//			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
    
    function cancelApproval(refNo) {
		var request = $http({
			method: "post",
			url: "service/personnel/cancelApproval/" + refNo,
//			data: personnel
//			params: {
//				jobNo: jobNo
//			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
    
    function returnDecisionMap(refNo) {
		var request = $http({
			method: "get",
			url: "service/personnel/returnDecisionMap/" + refNo,
//			data: personnel
//			params: {
//				jobNo: jobNo
//			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    	
    }
    
    function returnDecisionMapHtml(refNo) {
		var request = $http({
			method: "get",
			url: "service/personnel/returnDecisionMap.html",
//			data: personnel
			params: {
				refNo: refNo
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    	
    }
    
    function getActivePersonnel(jobNo) {
		var request = $http({
			method: "get",
			url: "service/personnel/getActivePersonnel/" + jobNo,
//			data: personnel
//			params: {
//				jobNo: jobNo
//			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

    function getAllPersonnelMap() {
		var request = $http({
			method: "get",
			url: "service/personnel/getAllPersonnelMap",
//			data: personnel
//			params: {
//				jobNo: jobNo
//			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}


}]);