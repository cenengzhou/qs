mainApp.service('adlService', ['$http', '$q',  function($http, $q){
	// Return public API.
    return({
    	getMonthlyJobCostList:		getMonthlyJobCostList,
    	getAccountLedgerList:	getAccountLedgerList
    });
   
    function getMonthlyJobCostList(noJob, noSubcontract, year, month){
    	var request = $http({
    		method: 'GET',
    		url: 'service/adl/getMonthlyJobCostList',
    		params:{
    			noJob: noJob,
    			noSubcontract: noSubcontract,
    			year: year,
    			month: month
    		}
    	});
    	return( request.then( handleSuccess, handleError ) );
    }
    
    function getAccountLedgerList(noJob, typeLedger, yearStart, yearEnd, monthStart, monthEnd, typeDocument, noSubcontract, codeObject, codeSubsidiary){
    	var request = $http({
    		method: 'GET',
    		url: 'service/adl/getAccountLedgerList',
    		params:{
    			noJob: noJob,
    			typeLedger: typeLedger,
    			yearStart: yearStart,
    			yearEnd: yearEnd,
    			monthStart: monthStart,
    			monthEnd: monthEnd,
    			typeDocument: typeDocument,
    			noSubcontract: noSubcontract,
    			codeObject: codeObject,
    			codeSubsidiary: codeSubsidiary
    		}
    	});
    	return( request.then( handleSuccess, handleError ) );
    }
    

    // ---
    // PRIVATE METHODS.
    // ---
    // Transform the error response, unwrapping the application dta from
    // the API response payload.
    function handleError( response) {
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




