mainApp.service('subcontractorService', ['$http', '$q', 'GlobalHelper',  function($http, $q, GlobalHelper){
	// Return public API.
    return({
    	obtainSubcontractorWrappers:		obtainSubcontractorWrappers,
    	obtainClientWrappers:	obtainClientWrappers
    });
   
    function obtainSubcontractorWrappers(workScope, subcontractor){
    	var request = $http({
    		url: 'service/subcontractor/obtainSubcontractorWrappers',
    		method: 'POST',
    		params: {
    			workScope: workScope,
    			subcontractor: subcontractor
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function obtainClientWrappers(client){
    	var request = $http({
    		url: 'service/subcontractor/obtainClientWrappers',
    		method: 'POST',
    		params: {
    			client: client
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

}]);




