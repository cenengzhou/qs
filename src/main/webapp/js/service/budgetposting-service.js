mainApp.service('budgetpostingService', ['$http', '$q', 'GlobalHelper', function($http, $q, GlobalHelper){
	// Return public API.
    return({
    	postBudget:		postBudget
    });
   
    function postBudget(jobNo){
    	var request = $http({
    		method: 'POST',
    		url: 'service/budgetposting/postBudget',
    		params:{
    			jobNumber: jobNo
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

}]);




