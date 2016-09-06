mainApp.service('tenderVarianceService', ['$http', '$q', 'GlobalHelper', function($http, $q, GlobalHelper){
	// Return public API.
	return({
		getTenderVarianceList: 	getTenderVarianceList,
		createTenderVariance:	createTenderVariance

	});

	
	function getTenderVarianceList(jobNo, subcontractNo, subcontractorNo) {
		var request = $http({
			method: "get",
			url: "service/tenderVariance/getTenderVarianceList",
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
			url: "service/tenderVariance/createTenderVariance",
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

