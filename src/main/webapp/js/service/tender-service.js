mainApp.service('tenderService', ['$http', '$q', function($http, $q){
	// Return public API.
	return({
		getTenderDetailList:		getTenderDetailList,
		updateTenderDetails: 		updateTenderDetails

	});

	
	function getTenderDetailList(jobNo, packageNo, vendorNo) {
		var request = $http({
			method: "get",
			url: "service/tender/getTenderDetailList",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				packageNo: packageNo,
				vendorNo: vendorNo
			}
		});
		return( request.then( handleSuccess, handleError ) );
	}
	
	function updateTenderDetails(jobNo, subcontractNo, vendorNo, currencyCode, exchangeRate, taDetails, validate) {
		var request = $http({
			method: "post",
			url: "service/tender/updateTenderDetails",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				subcontractNo: subcontractNo,
				vendorNo: vendorNo,
				currencyCode: currencyCode, 
				exchangeRate: exchangeRate,
				validate: validate
			},
			data: taDetails
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

