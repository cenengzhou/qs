mainApp.service('storageService', ['$http', '$q', 'GlobalHelper', '$rootScope', 'jobService', 
	function($http, $q, GlobalHelper, $rootScope, jobService){
	// Return public API.
    return({
    	gettingJobList:		gettingJobList,
    });
    
    var cacheKeyArray = {};
    function gettingJobList(){
    	var deferral = $q.defer();
    	if(!$rootScope.jobs){
    		obtainCacheKey("JOB_LIST")
    		.then(function(response){
    			cacheKeyArray["JOB_LIST"] = response.cacheKey;
				getItem("JOB_LIST")
				.then(function(response){
					$rootScope.jobs = response.value
				}, function(error){
    				jobService.getJobList()
            		.then(function(data){
            			$rootScope.jobs = data;
            			deferral.resolve({
            				jobs : data
            			});
            			setItem("JOB_LIST", data);
            		})
				})
    		})
    	} else {
    		deferral.resolve({
    			jobs : $rootScope.jobs
    		});
    	}
    	return deferral.promise;
    }
    
    function obtainCacheKey(itemType){
    	if(!cacheKeyArray) cacheKeyArray = [];
    	var deferral = $q.defer();
    	if(!cacheKeyArray[itemType]){
    		$http.post('service/properties/obtainCacheKey', JSON.stringify(itemType))
    		.then(function(response){
    			deferral.resolve({
    				cacheKey : response.data
    			});
    		})
    	} else {
    		deferral.resolve({
    			cacheKey: cacheKeyArray[itemType]
    		});
    	}
    	return deferral.promise;
    }

    function getItem(key){
    	var deferral = $q.defer();
    	obtainCacheKey(key)
    	.then(function(response){
    		var item = localStorage.getItem(key);
    		if(item){
    			try{
	    			var jsonString = CryptoJS.AES.decrypt(item, response.cacheKey).toString(CryptoJS.enc.Utf8);
		    		deferral.resolve({
		    			value : JSON.parse(jsonString)
		    		});
    			} catch(e) {
    				console.error(e);
    				removeItem(key);
    				deferral.reject(key + ' item decrypt error:' + e);
    			}
    		} else {
    			removeItem(key);
    			deferral.reject(key + ' item not found');
    		}
    	});
    	return deferral.promise;
    }
    
    function setItem(key, value){
    	obtainCacheKey(key)
    	.then(function(response){
    		try{
    			localStorage.setItem(key, CryptoJS.AES.encrypt(JSON.stringify(value), response.cacheKey));
    		} catch(e) {
    			console.error(key + ' item encrypt error:' + e);
    		}
    	})
    }
    
    function removeItem(key){
    	localStorage.removeItem(key);
    }
//    1. 	!$rootScope.jobs
//    1.1 		get jobs from localStorage
//    1.1.1 		decrypt jobs from localStorage
//    1.1.1.1			assign $rootScope.jobs
//    1.2.1 		get jobs from server
//    1.2.1.1			assign $rootScope.jobs
//    1.2.1.2 			encrypt and save to localStorage
//    
//    
    
//    jobsJSON = JSON.stringify(data);
//	localStorage.jobs = CryptoJS.AES.encrypt(jobsJSON, "a5ed1f63b63f8ff9f9d98c29d7457974");
//	bytes = CryptoJS.AES.decrypt(localStorage.jobs, "a5ed1f63b63f8ff9f9d98c29d7457974");
//	try{
//		jobs = JSON.parse(bytes.toString(CryptoJS.enc.Utf8));
//	} catch(e){
//		console.log(e);
//	}

}]);




