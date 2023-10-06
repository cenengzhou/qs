mainApp.service('rocService', ['$http', '$q', '$log', 'GlobalHelper',  function($http, $q, $log, GlobalHelper){
	// Return public API.
    return({
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
		updateRocCutoffAdmin: updateRocCutoffAdmin,
		saveRocDetails: saveRocDetails,
		saveSubdetailList: saveSubdetailList,
		deleteRocListAdmin: deleteRocListAdmin,
		deleteRocDetailListAdmin: deleteRocDetailListAdmin,
		deleteRocSubdetailListAdmin: deleteRocSubdetailListAdmin,
		deleteRoc: deleteRoc,
		getRocClassDescMap: getRocClassDescMap,
		getRocCategoryList: getRocCategoryList,
		getImpactList: getImpactList,
		getStatusList: getStatusList,
		getCutoffPeriod: getCutoffPeriod,
		getRocHistory: getRocHistory,
		getRocDetailHistory: getRocDetailHistory,
		recalculateRoc: recalculateRoc
    });

	function getRocAdmin(jobNo, itemNo) {
		var request = $http({
			method: 'GET',
			url: "service/roc/getRocAdmin/" + jobNo,
			params: {
				itemNo: itemNo
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function getRocDetailListAdmin(jobNo, itemNo, period) {
		var request = $http({
			method: 'GET',
			url: "service/roc/getRocDetailListAdmin/" + jobNo,
			params: {
				itemNo: itemNo,
				period: period
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function getRocSubdetailListAdmin(jobNo, itemNo, period) {
		var request = $http({
			method: 'GET',
			url: "service/roc/getRocSubdetailListAdmin/" + jobNo,
			params: {
				itemNo: itemNo,
				period: period
			}
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

	function getCutoffPeriod() {
		var request = $http({
			method: 'GET',
			url: "service/roc/getCutoffPeriod"
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

	function updateRocAdmin(jobNo, rocList) {
		var request = $http({
			method: "post",
			url: "service/roc/updateRocAdmin",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo
			},
			data: rocList
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

	function updateRocCutoffAdmin(rocCutoff) {
		var request = $http({
			method: "post",
			url: "service/roc/updateRocCutoffAdmin",
			dataType: "application/json;charset=UTF-8",
			data: rocCutoff
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

	function deleteRocListAdmin(jobNo, rocList) {
		var request = $http({
			method: "post",
			url: "service/roc/deleteRocListAdmin",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo
			},
			data: rocList
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

	function deleteRoc(rocId) {
		var request = $http({
			method: "post",
			url: "service/roc/deleteRoc",
			dataType: "application/json;charset=UTF-8",
			params: {},
			data: rocId
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
}]);