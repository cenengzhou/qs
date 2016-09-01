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

//     // ---
//    // PRIVATE METHODS.
//    // ---
//    // Transform the error response, unwrapping the application dta from
//    // the API response payload.
//    function handleError( response) {
//        // The API response from the server should be returned in a
//        // normalized format. However, if the request was not handled by the
//        // server (or what not handles properly - ex. server error), then we
//        // may have to normalize it on our end, as best we can.
//        if (
//            ! angular.isObject( response.data ) ||
//            ! response.data.message
//            ) {
//            return( $q.reject( "An unknown error occurred." ) );
//        }
//        // Otherwise, use expected error message.
//        return( $q.reject( response.data.message ) );
//    }
//    // Transform the successful response, unwrapping the application data
//    // from the API response payload.
//    function handleSuccess( response ) {
//        return( response.data );
//    }
}]);




