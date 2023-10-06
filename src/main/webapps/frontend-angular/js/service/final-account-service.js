mainApp.service('finalAccountService', ['$http', '$q', '$log', 'GlobalHelper',  function($http, $q, $log, GlobalHelper){
	// Return public API.
    return({
		getFinalAccount: getFinalAccount,
		createFinalAccount: createFinalAccount,
		getFinalAccountAdmin: getFinalAccountAdmin,
		updateFinalAccountAdmin: updateFinalAccountAdmin,
		deleteFinalAccountAdmin: deleteFinalAccountAdmin
    });

	function getFinalAccount(jobNo, addendumNo, addendumId) {
		var request = $http({
			method: 'GET',
			url: "service/finalAccount/getFinalAccount/" + jobNo + "/" + addendumNo + "/" + addendumId
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function createFinalAccount(jobNo, addendumNo, addendumId, finalAccount) {
		var request = $http({
			method: "post",
			url: "service/finalAccount/createFinalAccount",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				addendumNo: addendumNo,
				addendumId: addendumId
			},
			data: finalAccount
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function getFinalAccountAdmin(jobNo, subcontractNo, addendumNo) {
		var request = $http({
			method: 'GET',
			url: "service/finalAccount/getFinalAccountAdmin/" + jobNo + "/" + subcontractNo + "/" + addendumNo
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function updateFinalAccountAdmin(jobNo, subcontractNo, addendumNo, finalAccount) {
		var request = $http({
			method: "post",
			url: "service/finalAccount/updateFinalAccountAdmin",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				subcontractNo: subcontractNo,
				addendumNo: addendumNo
			},
			data: finalAccount
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function deleteFinalAccountAdmin(jobNo, subcontractNo, addendumNo, finalAccount) {
		var request = $http({
			method: "post",
			url: "service/finalAccount/deleteFinalAccountAdmin",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				subcontractNo: subcontractNo,
				addendumNo: addendumNo
			},
			data: finalAccount
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

}]);