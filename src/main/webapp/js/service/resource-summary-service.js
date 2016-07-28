mainApp.service('resourceSummaryService', ['$http', '$q', function($http, $q){
	// Return public API.
	return({
		getResourceSummaries: 				getResourceSummaries,
		getResourceSummariesBySC:			getResourceSummariesBySC,
		getResourceSummariesByAccountCode: 	getResourceSummariesByAccountCode,
		getResourceSummariesByLineType:		getResourceSummariesByLineType,
		addResourceSummary:					addResourceSummary,
		updateResourceSummaries:			updateResourceSummaries,
		deleteResources:					deleteResources,
		splitOrMergeResources:				splitOrMergeResources

	});

	function getResourceSummaries(jobNo, subcontractNo, objectCode) {
		var request = $http({
			method: "get",
			url: "service/resourceSummary/getResourceSummaries",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				subcontractNo: subcontractNo,
				objectCode: objectCode
			}
		});
		return( request.then( handleSuccess, handleError ) );
	}
	
	function getResourceSummariesBySC(jobNo, subcontractNo, objectCode) {
		var request = $http({
			method: "get",
			url: "service/resourceSummary/getResourceSummariesBySC",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				subcontractNo: subcontractNo
			}
		});
		return( request.then( handleSuccess, handleError ) );
	}

	function getResourceSummariesByAccountCode(jobNo, subcontractNo, objectCode, subsidiaryCode) {
		var request = $http({
			method: "get",
			url: "service/resourceSummary/getResourceSummariesByAccountCode",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				subcontractNo: subcontractNo,
				objectCode: objectCode,
				subsidiaryCode: subsidiaryCode
			}
		});
		return( request.then( handleSuccess, handleError ) );
	}
	
	
	function getResourceSummariesByLineType(jobNo, subcontractNo, objectCode, subsidiaryCode, lineType, resourceNo) {
		var request = $http({
			method: "get",
			url: "service/resourceSummary/getResourceSummariesByLineType",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				subcontractNo: subcontractNo,
				objectCode: objectCode,
				subsidiaryCode: subsidiaryCode,
				lineType: lineType,
				resourceNo: resourceNo
			}
		});
		return( request.then( handleSuccess, handleError ) );
	}
	
	
	function addResourceSummary(jobNo, repackagingEntryId, resourceSummary) {
		var request = $http({
			method: "post",
			url: "service/resourceSummary/addResourceSummary",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				repackagingEntryId: repackagingEntryId
			},
			data: resourceSummary
		});
		return( request.then( handleSuccess, handleError ) );
	}
	
	function updateResourceSummaries(jobNo, resourceSummaryList) {
		var request = $http({
			method: "post",
			url: "service/resourceSummary/updateResourceSummaries",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo
			},
			data: resourceSummaryList
		});
		return( request.then( handleSuccess, handleError ) );
	}


	function deleteResources(resourceSummaryList) {
		var request = $http({
			method: "post",
			url: "service/resourceSummary/deleteResources",
			dataType: "application/json;charset=UTF-8",
			data: resourceSummaryList
		});
		return( request.then( handleSuccess, handleError ) );
	}

	 function splitOrMergeResources(repackagingEntryId, resourceSummarySplitMergeWrapper) {
	        var request = $http({
	            method: "post",
	            url: "service/resourceSummary/splitOrMergeResources",
	            dataType: "application/json;charset=UTF-8",
	            params: {
	            	repackagingEntryId: repackagingEntryId
	            },
	            data : resourceSummarySplitMergeWrapper
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

