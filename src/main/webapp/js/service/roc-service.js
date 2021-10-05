mainApp.service('rocService', ['$http', '$q', '$log', 'GlobalHelper',  function($http, $q, $log, GlobalHelper){
	// Return public API.
    return({
		getCutoffDate: getCutoffDate,
		getRocAdmin: getRocAdmin,
		getRocDetailListAdmin: getRocDetailListAdmin,
		getRocSubdetailListAdmin: getRocSubdetailListAdmin,
		getRocListSummary: getRocListSummary,
		getRocSubdetailList: getRocSubdetailList,
		addRoc: addRoc,
		updateRoc: updateRoc,
		updateRocAdmin: updateRocAdmin,
		updateRocDetailListAdmin: updateRocDetailListAdmin,
		updateRocSubdetailListAdmin: updateRocSubdetailListAdmin,
		saveRocDetails: saveRocDetails,
		saveSubdetailList: saveSubdetailList,
		deleteRocAdmin: deleteRocAdmin,
		deleteRocDetailListAdmin: deleteRocDetailListAdmin,
		deleteRocSubdetailListAdmin: deleteRocSubdetailListAdmin,
		getRocClassDescMap: getRocClassDescMap,
		getRocCategoryList: getRocCategoryList,
		getImpactList: getImpactList,
		getStatusList: getStatusList,
		getRocHistory: getRocHistory,
		getRocDetailHistory: getRocDetailHistory,
		recalculateRoc: recalculateRoc
    });

	function getCutoffDate() {
		var request = $http({
			method: 'GET',
			url: "service/roc/getCutoffDate"
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function getRocAdmin(jobNo, rocCategory, description) {
		var request = $http({
			method: 'GET',
			url: "service/roc/getRocAdmin/" + jobNo + "/" + rocCategory + "/" + description
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function getRocDetailListAdmin(jobNo, rocCategory, description) {
		var request = $http({
			method: 'GET',
			url: "service/roc/getRocDetailListAdmin/" + jobNo + "/" + rocCategory + "/" + description
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function getRocSubdetailListAdmin(jobNo, rocCategory, description) {
		var request = $http({
			method: 'GET',
			url: "service/roc/getRocSubdetailListAdmin/" + jobNo + "/" + rocCategory + "/" + description
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function getRocListSummary(jobNo, year, month) {
		var request = $http({
			method: 'GET',
			url: "service/roc/getRocListSummary/" + jobNo + "/" + year + "/" + month
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function getRocSubdetailList(jobNo, rocId, year, month) {
		var request = $http({
			method: 'GET',
			url: "service/roc/getRocSubdetailList/" + jobNo + "/" + rocId + "/" + year+ "/" + month
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

	function getRocCategoryList() {
		var request = $http({
			method: 'GET',
			url: "service/roc/getRocCategoryList"
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function getImpactList() {
		var request = $http({
			method: 'GET',
			url: "service/roc/getImpactList"
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function getStatusList() {
		var request = $http({
			method: 'GET',
			url: "service/roc/getStatusList"
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function getRocHistory(rocId) {
		var request = $http({
			method: 'GET',
			url: "service/roc/getRocHistory/" + rocId
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function getRocDetailHistory(rocDetailId) {
		var request = $http({
			method: 'GET',
			url: "service/roc/getRocDetailHistory/" + rocDetailId
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

	function updateRocAdmin(jobNo, roc) {
		var request = $http({
			method: "post",
			url: "service/roc/updateRocAdmin",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo
			},
			data: roc
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function updateRocDetailListAdmin(jobNo, rocDetailList) {
		var request = $http({
			method: "post",
			url: "service/roc/updateRocDetailListAdmin",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo
			},
			data: rocDetailList
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function updateRocSubdetailListAdmin(jobNo, rocSubdetailList) {
		var request = $http({
			method: "post",
			url: "service/roc/updateRocSubdetailListAdmin",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo
			},
			data: rocSubdetailList
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function recalculateRoc(jobNo) {
		var request = $http({
			method: "post",
			url: "service/roc/recalculateRoc",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function saveRocDetails(jobNo, rocDetails, year, month) {
		var request = $http({
			method: "post",
			url: "service/roc/saveRocDetails",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				year: year,
				month: month
			},
			data: rocDetails
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function saveSubdetailList(jobNo, rocId, rocSubdetails, year, month) {
		var request = $http({
			method: "post",
			url: "service/roc/saveSubdetailList",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				rocId: rocId,
				year: year,
				month: month
			},
			data: rocSubdetails
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function deleteRocAdmin(jobNo, roc) {
		var request = $http({
			method: "post",
			url: "service/roc/deleteRocAdmin",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo
			},
			data: roc
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function deleteRocDetailListAdmin(jobNo, rocDetailList) {
		var request = $http({
			method: "post",
			url: "service/roc/deleteRocDetailListAdmin",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo
			},
			data: rocDetailList
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function deleteRocSubdetailListAdmin(jobNo, rocSubdetailList) {
		var request = $http({
			method: "post",
			url: "service/roc/deleteRocSubdetailListAdmin",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo
			},
			data: rocSubdetailList
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
}]);