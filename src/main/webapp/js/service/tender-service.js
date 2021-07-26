mainApp.service('tenderService', ['$http', '$q', 'GlobalHelper', function($http, $q, GlobalHelper){
	// Return public API.
	return({
		getTenderDetailList:		getTenderDetailList,
		getTender:					getTender,
		getRecommendedTender:		getRecommendedTender,
		getTenderList:				getTenderList,
		getTenderComparisonList:	getTenderComparisonList,
		getUneditableTADetailIDs:	getUneditableTADetailIDs,
		createTender:				createTender,
		updateTenderDetails: 		updateTenderDetails,
		updateRecommendedTender:	updateRecommendedTender,
		deleteTender:				deleteTender,
		getTenderVarianceList: 	getTenderVarianceList,
		createTenderVariance:	createTenderVariance,
		updateTenderAdmin:	updateTenderAdmin,
		updateTenderDetailListAdmin:  updateTenderDetailListAdmin
	});


	function getTenderDetailList(jobNo, subcontractNo, subcontractorNo) {
		var request = $http({
			method: "get",
			url: "service/tender/getTenderDetailList",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				subcontractNo: subcontractNo,
				subcontractorNo: subcontractorNo
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function getTender(jobNo, subcontractNo, subcontractorNo) {
		var request = $http({
			method: "get",
			url: "service/tender/getTender",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				subcontractNo: subcontractNo,
				subcontractorNo: subcontractorNo
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function updateTenderAdmin(tender) {
		var request = $http.post('service/tender/updateTenderAdmin', tender);
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function updateTenderDetailListAdmin(tenderDetailList) {
		var request = $http.post('service/tender/updateTenderDetailListAdmin', tenderDetailList);
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function getRecommendedTender(jobNo, subcontractNo) {
		var request = $http({
			method: "get",
			url: "service/tender/getRecommendedTender",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				subcontractNo: subcontractNo
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function getTenderList(jobNo, subcontractNo) {
		var request = $http({
			method: "get",
			url: "service/tender/getTenderList",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				subcontractNo: subcontractNo
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function getTenderComparisonList(jobNo, subcontractNo) {
		var request = $http({
			method: "get",
			url: "service/tender/getTenderComparisonList",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				subcontractNo: subcontractNo
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}


	function getUneditableTADetailIDs(jobNo, subcontractNo, tenderNo) {
		var request = $http({
			method: "get",
			url: "service/tender/getUneditableTADetailIDs",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				subcontractNo: subcontractNo,
				tenderNo: tenderNo
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function createTender(jobNo, subcontractNo, subcontractorNo, subcontractorName) {
		var request = $http({
			method: "post",
			url: "service/tender/createTender",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				subcontractNo: subcontractNo,
				subcontractorNo: subcontractorNo,
				subcontractorName: subcontractorName

			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function updateRecommendedTender(jobNo, subcontractNo, subcontractorNo) {
		var request = $http({
			method: "post",
			url: "service/tender/updateRecommendedTender",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				subcontractNo: subcontractNo,
				subcontractorNo: subcontractorNo
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function updateTenderDetails(jobNo, subcontractNo, subcontractorNo, currencyCode, exchangeRate, remarks, statusChangeExecutionOfSC, taDetails, validate) {
		var request = $http({
			method: "post",
			url: "service/tender/updateTenderDetails",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				subcontractNo: subcontractNo,
				subcontractorNo: subcontractorNo,
				currencyCode: currencyCode,
				exchangeRate: exchangeRate,
				remarks: remarks,
				statusChangeExecutionOfSC: statusChangeExecutionOfSC,
				validate: validate
			},
			data: taDetails
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}


	function deleteTender(jobNo, subcontractNo, subcontractorNo) {
		var request = $http({
			method: "post",
			url: "service/tender/deleteTender",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				subcontractNo: subcontractNo,
				subcontractorNo: subcontractorNo
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function getTenderVarianceList(jobNo, subcontractNo, subcontractorNo) {
		var request = $http({
			method: "get",
			url: "service/tender/getTenderVarianceList",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				subcontractNo: subcontractNo,
				subcontractorNo: subcontractorNo
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function createTenderVariance(jobNo, subcontractNo, subcontractorNo, tenderVarianceList) {
		var request = $http({
			method: "post",
			url: "service/tender/createTenderVariance",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				subcontractNo: subcontractNo,
				subcontractorNo: subcontractorNo
			},
			data: tenderVarianceList
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

}]);

