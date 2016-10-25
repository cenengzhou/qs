mainApp.service('resourceSummaryService', ['$http', '$q', 'GlobalHelper', function($http, $q, GlobalHelper){
	// Return public API.
	return({
		getResourceSummaries: 					getResourceSummaries,
		getResourceSummariesBySC:				getResourceSummariesBySC,
		getResourceSummariesByAccountCode: 		getResourceSummariesByAccountCode,
		getResourceSummariesByLineType:			getResourceSummariesByLineType,
		getResourceSummariesForAddendum:		getResourceSummariesForAddendum,
		getResourceSummariesGroupByObjectCode: 	getResourceSummariesGroupByObjectCode,
		addResourceSummary:						addResourceSummary,
		updateResourceSummaries:				updateResourceSummaries,
		deleteResources:						deleteResources,
		splitOrMergeResources:					splitOrMergeResources,
		updateIVAmount:							updateIVAmount,
		postIVAmounts:							postIVAmounts,
		generateResourceSummaries:				generateResourceSummaries,
		updateIVForSubcontract:					updateIVForSubcontract
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
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
	
	function getResourceSummariesForAddendum(jobNo) {
		var request = $http({
			method: "get",
			url: "service/resourceSummary/getResourceSummariesForAddendum",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
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
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
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
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
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
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
	
	function getResourceSummariesGroupByObjectCode(jobNo) {
		var request = $http({
			method: "get",
			url: "service/resourceSummary/getResourceSummariesGroupByObjectCode",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}


	function addResourceSummary(jobNo, repackagingId, resourceSummary) {
		var request = $http({
			method: "post",
			url: "service/resourceSummary/addResourceSummary",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				repackagingId: repackagingId
			},
			data: resourceSummary
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
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
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}


	function deleteResources(resourceSummaryList) {
		var request = $http({
			method: "post",
			url: "service/resourceSummary/deleteResources",
			dataType: "application/json;charset=UTF-8",
			data: resourceSummaryList
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function splitOrMergeResources(jobNo, resourceSummarySplitMergeWrapper) {
		var request = $http({
			method: "post",
			url: "service/resourceSummary/splitOrMergeResources",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo
			},
			data : resourceSummarySplitMergeWrapper
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function updateIVAmount(resourceSummaryList) {
		var request = $http({
			method: "post",
			url: "service/resourceSummary/updateIVAmount",
			dataType: "application/json;charset=UTF-8",
			data : resourceSummaryList
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function postIVAmounts(jobNo, finalized) {
		var request = $http({
			method: "post",
			url: "service/resourceSummary/postIVAmounts",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				finalized: finalized
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	 function generateResourceSummaries( jobNo) {
	        var request = $http({
	            method: "post",
	            url: "service/resourceSummary/generateResourceSummaries",
	            dataType: "application/json;charset=UTF-8",
	            params: {
	                jobNo: jobNo
	            }
	        });
	        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	    }
	 
	 function updateIVForSubcontract(resourceSummaryList) {
			var request = $http({
				method: "post",
				url: "service/resourceSummary/updateIVForSubcontract",
				dataType: "application/json;charset=UTF-8",
				data: resourceSummaryList
			});
			return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
		}
	 
}]);

