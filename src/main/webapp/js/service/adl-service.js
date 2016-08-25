mainApp.service('adlService', ['$http', '$q',  function($http, $q){
	// Return public API.
    return({
    	getJobDashboardData:						getJobDashboardData,
    	getMonthlyJobCostList:						getMonthlyJobCostList,
    	getAccountLedgerList:						getAccountLedgerList,
    	getAddressBookListOfSubcontractorAndClient: getAddressBookListOfSubcontractorAndClient
    });
    
    function getJobDashboardData(noJob, type, year){
    	var deferred = $q.defer();
    	$http({
    		method: 'GET',
    		url: 'service/adl/getJobDashboardData',
    		params:{
    			noJob: noJob,
    			type: type,
    			year: year
    		}
    	}).success(function(data) { 
    		deferred.resolve(data);
    	}).error(function(msg, code) {
    		deferred.reject(msg);
    		$log.error(msg, code);
    	});
    	return deferred.promise;
    }
    
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
    
    function getAddressBookListOfSubcontractorAndClient(){
    	var request = $http.get('service/adl/getAddressBookListOfSubcontractorAndClient');
    	return( request.then( handleSuccess, handleError ) );
    }
    
    function handleError( response) {
        if (
            ! angular.isObject( response.data ) ||
            ! response.data.message
            ) {
            return( $q.reject( "An unknown error occurred." ) );
        }
        return( $q.reject( response.data.message ) );
    }
    function handleSuccess( response ) {
        return( response.data );
    }
}]);




