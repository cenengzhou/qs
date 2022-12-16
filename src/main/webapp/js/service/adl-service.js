mainApp.service('adlService', ['$http', '$q', '$log', 'GlobalHelper', 
						function($http, $q, $log, GlobalHelper){
	// Return public API.
    return({
    	getJobDashboardData:						getJobDashboardData,
    	getMonthlyJobCostList:						getMonthlyJobCostList,
    	getAccountLedgerList:						getAccountLedgerList,
    	getAccountLedgerListByGlDate:				getAccountLedgerListByGlDate,
			getAddressBook: getAddressBook,
			obtainSubcontractor:	obtainSubcontractor,
    	getAddressBookListOfSubcontractorAndClient: getAddressBookListOfSubcontractorAndClient,
    	obtainCompanyCodeAndName:					obtainCompanyCodeAndName,
    	getMonthlyJobCostListByPeroidRange:			getMonthlyJobCostListByPeroidRange,
    	getAllWorkScopes:							getAllWorkScopes,
    });
    
  //Asyn Call
    function getJobDashboardData(noJob, year){
    	var deferred = $q.defer();
    	$http({
    		method: 'GET',
    		url: 'service/adl/getJobDashboardData',
    		params:{
    			noJob: noJob,
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
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
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
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getAccountLedgerListByGlDate(searchObjectMap){
    	var request = $http({
    		method: 'POST',
    		url: 'service/adl/getAccountLedgerListByGlDate',
    		data: searchObjectMap
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getAddressBook(addressBookNo){
    	var request = $http({
    		method: 'GET',
    		url: 'service/adl/getAddressBook',
    		params:{
    			addressBookNo: addressBookNo
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
							}
							
		function obtainSubcontractor(addressBookNo){
    	var request = $http({
    		method: 'GET',
    		url: 'service/adl/obtainSubcontractor',
    		params:{
    			addressBookNo: addressBookNo
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getAddressBookListOfSubcontractorAndClient(addressBookParam, addressBookTypeCode){
    	var request = $http({
    		method: 'GET',
    		url: 'service/adl/getAddressBookListOfSubcontractorAndClient',
    		params:{
    			addressBookParam: addressBookParam,
    			addressBookTypeCode: addressBookTypeCode
    			
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function obtainCompanyCodeAndName(){
    	var request = $http.post('service/adl/obtainCompanyCodeAndName');
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getMonthlyJobCostListByPeroidRange(noJob, noSubcontract, fromYear, fromMonth, toYear, toMonth){
    	var request = $http({
    		method: 'GET',
    		url: 'service/adl/getMonthlyJobCostListByPeroidRange',
    		params:{
    			noJob: noJob,
    			noSubcontract: noSubcontract,
    			fromYear: fromYear,
    			fromMonth: fromMonth,
    			toYear: toYear,
    			toMonth: toMonth
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function getAllWorkScopes(){
    	var request = $http.get('service/adl/getAllWorkScopes');
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
}]);
