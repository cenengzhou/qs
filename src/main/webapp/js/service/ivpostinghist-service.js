mainApp.service('ivpostinghistService', ['$http', '$q', 'GlobalHelper', function($http, $q, GlobalHelper){
	// Return public API.
    return({
    	obtainIVPostingHistoryList:		obtainIVPostingHistoryList
    });
   
    function obtainIVPostingHistoryList(jobNumber, fromDate, toDate){
    	var request = $http({
    		method: 'POST',
    		url: 'service/ivpostinghist/obtainIVPostingHistoryList',
    		params:{
    			jobNumber: jobNumber,
    			fromDate: fromDate,
    			toDate: toDate
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
}]);




