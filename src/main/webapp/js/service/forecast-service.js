mainApp.service('forecastService', ['$http', '$q', '$log', 'GlobalHelper',  function($http, $q, $log, GlobalHelper){
	// Return public API.
    return({
    	getByTypeDesc:		getByTypeDesc,
    	getForecastByJobNo: getForecastByJobNo,
    	getCriticalProgrammeRFList: getCriticalProgrammeRFList,
    	addCriticalProgram: addCriticalProgram,
    	saveByJobNo: 	saveByJobNo,
    	saveForecastByJobNo: saveForecastByJobNo,
    	deleteByJobNo:	deleteByJobNo
    });
	

   
  //Asyn Call
    function getByTypeDesc(jobNo, year, month, type, desc) {
    	var deferred = $q.defer();
    	$http({
    		method: 'GET',
    		url: "service/forecast/getByTypeDesc/" + jobNo + "/" + year + "/" + month+ "/" + type+ "/" + desc
    	}).success(function(data) { 
    		deferred.resolve(data);
    	}).error(function(msg, code) {
    		deferred.reject(msg);
    		$log.error(msg, code);
    	});
    	return deferred.promise;
    }
    
    
    function getForecastByJobNo(jobNo, year, month) {
		var request = $http({
			method: 'GET',
    		url: "service/forecast/getForecastByJobNo/" + jobNo + "/" + year + "/" + month
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    
    function getCriticalProgrammeRFList(jobNo, year, month) {
		var request = $http({
			method: "get",
			url: "service/forecast/getCriticalProgrammeRFList/" + jobNo + "/" + year + "/" + month
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
  
    
    function saveByJobNo(jobNo, forecast) {
		var request = $http({
			method: "post",
			url: "service/forecast/" + jobNo,
			data: forecast
		});
		return request;
    }
    
    function saveForecastByJobNo(jobNo, forecast) {
		var request = $http({
			method: "post",
			url: "service/forecast/saveForecastByJobNo/" + jobNo,
			data: forecast
		});
		return request;
    }
    
    
    function addCriticalProgram(jobNo, forecast) {
		var request = $http({
			method: "post",
			url: "service/forecast/addCriticalProgram/" + jobNo,
			data: forecast
		});
		return request;
    }
    
    function deleteByJobNo(jobNo, id) {
		var request = $http({
			method: "delete",
			url: "service/forecast/" + jobNo + "/" + id
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
}]);