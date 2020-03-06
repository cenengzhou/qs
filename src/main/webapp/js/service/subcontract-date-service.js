mainApp.service('subcontractDateService', ['$http', '$q', 'GlobalHelper',  function($http, $q, GlobalHelper){
	return({
		getScDateList: 		getScDateList
	});

    function getScDateList(jobNo, packageNo, reload) {
    	var deferred = $q.defer();
    	if(reload || !GlobalHelper.scdates || GlobalHelper.scdates.length == 0) {
    		GlobalHelper.scdates = [];
	        var request = $http({
	            method: "get",
	            url: "service/subcontractDate/getScDateList/" + jobNo + "/" + packageNo
	        }).then(function(response) {
	        	GlobalHelper.scdates = response.data;
	        	deferred.resolve(response.data);
	        });
    	} else {
    		deferred.resolve(GlobalHelper.scdates);
    	}
    	return deferred.promise;
    }
}]);
    