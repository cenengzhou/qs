mainApp.service('quartzService', ['$http', '$q', 'GlobalHelper',  function($http, $q, GlobalHelper){
	// Return public API.
    return({
    	getAllTriggers:				getAllTriggers,
    	updateQrtzTriggerList:		updateQrtzTriggerList
    });
   
    function getAllTriggers(){
    	var request = $http.post('service/quartz/getAllTriggers');
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function updateQrtzTriggerList(triggers){
    	var request = $http.post('service/quartz/updateQrtzTriggerList', triggers);
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
}]);




