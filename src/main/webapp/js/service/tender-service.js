mainApp.service('tenderService', ['$http', '$q', function($http, $q){
	// Return public API.
	return({
		getTenderDetailList:		getTenderDetailList,
		getTender:					getTender,
		getTenderList:				getTenderList,
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
		return( request.then( handleSuccess, handleError ) );
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
		return( request.then( handleSuccess, handleError ) );
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
		return( request.then( handleSuccess, handleError ) );
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
		return( request.then( handleSuccess, handleError ) );
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
		return( request.then( handleSuccess, handleError ) );
	}
	
	function updateTenderDetails(jobNo, subcontractNo, subcontractorNo, currencyCode, exchangeRate, taDetails, validate) {
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
				validate: validate
			},
			data: taDetails
		});
		return( request.then( handleSuccess, handleError ) );
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
		return( request.then( handleSuccess, handleError ) );
	}



	// ---
	// PRIVATE METHODS.
	// ---
	// Transform the error response, unwrapping the application dta from
	// the API response payload.
	function handleError( response ) {
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

