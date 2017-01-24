mainApp.service('unitService', ['$http', '$q', 'GlobalHelper',  function($http, $q, GlobalHelper){
	// Return public API.
    return({
    	getAllWorkScopes:		getAllWorkScopes,
    	getUnitOfMeasurementList: getUnitOfMeasurementList,
    	getAppraisalPerformanceGroupMap: getAppraisalPerformanceGroupMap,
    	getSCStatusCodeMap: getSCStatusCodeMap
    });
   
    function getAllWorkScopes(){
    	var request = $http.get('service/unit/getAllWorkScopes');
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getUnitOfMeasurementList(){
    	var request = $http.get('service/unit/getUnitOfMeasurementList');
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function getAppraisalPerformanceGroupMap(){
    	var request = $http.get('service/unit/getAppraisalPerformanceGroupMap');
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function getSCStatusCodeMap(){
    	var request = $http.get('service/unit/getSCStatusCodeMap');
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

}]);




