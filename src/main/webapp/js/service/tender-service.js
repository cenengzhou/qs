mainApp.service('tenderService', ['$http', '$q', 'GlobalHelper', function($http, $q, GlobalHelper){
	// Return public API.
	return({
		getTenderDetailList:		getTenderDetailList,
		getTender:					getTender,
		getRecommendedTender:		getRecommendedTender,
		getTenderList:				getTenderList,
		getTenderComparisonList:	getTenderComparisonList,
		createTender:				createTender,
		updateTenderDetails: 		updateTenderDetails,
		updateRecommendedTender:	updateRecommendedTender,
		deleteTender:				deleteTender

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
	
	function createTender(jobNo, subcontractNo, subcontractorNo) {
		var request = $http({
			method: "post",
			url: "service/tender/createTender",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				subcontractNo: subcontractNo,
				subcontractorNo: subcontractorNo
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



//	// ---
//	// PRIVATE METHODS.
//	// ---
//	// Transform the error response, unwrapping the application dta from
//	// the API response payload.
//	function handleError( response ) {
//		// The API response from the server should be returned in a
//		// normalized format. However, if the request was not handled by the
//		// server (or what not handles properly - ex. server error), then we
//		// may have to normalize it on our end, as best we can.
//		if (
//				! angular.isObject( response.data ) ||
//				! response.data.message
//		) {
//			return( $q.reject( "An unknown error occurred." ) );
//		}
//		// Otherwise, use expected error message.
//		return( $q.reject( response.data.message ) );
//	}
//	// Transform the successful response, unwrapping the application data
//	// from the API response payload.
//	function handleSuccess( response ) {
//		return( response.data );
//	}
}]);

