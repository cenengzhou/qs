mainApp.service('rocService', ['$http', '$q', '$log', 'GlobalHelper',  function($http, $q, $log, GlobalHelper){
	// Return public API.
    return({
		getRocListSummary: getRocListSummary,
		addRoc: addRoc,
		updateRoc: updateRoc,
		saveRocDetails: saveRocDetails,
		getRocClassDescMap: getRocClassDescMap
    });

	function getRocListSummary(jobNo, year, month) {
		var request = $http({
			method: 'GET',
			url: "service/roc/getRocListSummary/" + jobNo + "/" + year + "/" + month
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function getRocClassDescMap() {
		var request = $http({
			method: 'GET',
			url: "service/roc/getRocClassDescMap"
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function addRoc(jobNo, roc) {
		var request = $http({
			method: "post",
			url: "service/roc/addRoc",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo
			},
			data: roc
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function updateRoc(jobNo, roc) {
		var request = $http({
			method: "post",
			url: "service/roc/updateRoc",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo
			},
			data: roc
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function saveRocDetails(jobNo, rocDetails) {
		var request = $http({
			method: "post",
			url: "service/roc/saveRocDetails",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo
			},
			data: rocDetails
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

}]);