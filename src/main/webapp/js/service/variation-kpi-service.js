mainApp.service('variationKpiService', ['$http', '$q', '$log', 'GlobalHelper',  function($http, $q, $log, GlobalHelper){
	// Return public API.
    return({
    	findByJobNo:	findByJobNo,
    	saveByJobNo: 	saveByJobNo,
    	saveListByJobNo: saveListByJobNo,
    	deleteByJobNo:	deleteByJobNo,
    	getByPage:		getByPage
    });
	
    function findByJobNo(jobNo) {
		var request = $http({
			method: "get",
			url: "service/variationKpi/" + jobNo
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function getByPage(page, size, direction, property,
    		noJob, 
			year, 
			month, 
			numberIssued, 
			amountIssued, 
			numberSubmitted, 
			amountSubmitted, 
			numberAssessed, 
			amountAssessed, 
			numberApplied, 
			amountApplied, 
			numberCertified, 
			amountCertified, 
			remarks
		) {
		var request = $http({
			method: "get",
			url: "service/variationKpi/" + page + "/" + size,
			params: {
				direction: direction,
				property: property,
				noJob: noJob, 
				year: year, 
				month: month, 
				numberIssued: numberIssued, 
				amountIssued: amountIssued, 
				numberSubmitted: numberSubmitted, 
				amountSubmitted: amountSubmitted, 
				numberAssessed: numberAssessed, 
				amountAssessed: amountAssessed, 
				numberApplied: numberApplied, 
				amountApplied: amountApplied, 
				numberCertified: numberCertified, 
				amountCertified: amountCertified, 
				remarks: remarks
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function saveByJobNo(jobNo, kpi) {
		var request = $http({
			method: "post",
			url: "service/variationKpi/" + jobNo,
			data: kpi
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function saveListByJobNo(jobNo, kpiList) {
		var request = $http({
			method: "post",
			url: "service/variationKpi/saveList/" + jobNo,
			data: kpiList
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function deleteByJobNo(jobNo, id) {
		var request = $http({
			method: "delete",
			url: "service/variationKpi/" + jobNo + "/" + id
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
}]);