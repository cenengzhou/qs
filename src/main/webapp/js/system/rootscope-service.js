mainApp.service('rootscopeService', ['$http', '$q', '$window', 'GlobalHelper', '$rootScope', 'jobService', 'unitService', 'adlService', 'SessionHelper',
	function($http, $q, $window, GlobalHelper, $rootScope, jobService, unitService, adlService, SessionHelper){
	// Return public API.
    return({
    	gettingJobList:		gettingJobList,
    	gettingCompanies:	gettingCompanies,
    	gettingDivisions:	gettingDivisions,
    	gettingUser:		gettingUser,
    	gettingSessionId:	gettingSessionId,
    	gettingProperties:	gettingProperties,
    	gettingWorkScopes:	gettingWorkScopes,
    	
    	getPreviousStatus:	getPreviousStatus,
    	setPreviousStatus:	setPreviousStatus,
    	getRoutedToDefaultJob:	getRoutedToDefaultJob,
    	setRoutedToDefaultJob:	setRoutedToDefaultJob,
    	getSelectedTips:	getSelectedTips,
    	setSelectedTips:	setSelectedTips,
    	configHiddenMenu:	configHiddenMenu,
    	setEnv:				setEnv,
    	getShowAdminMenu:	getShowAdminMenu,
    });
    
    var cacheKeyArray = {};
//  1. 		!$rootScope.jobs
//  1.1			obtainCacheKey  
//  1.2 		get jobs from localStorage
//  1.2.1 			decrypt jobs with cacheKey from localStorage
//  1.2.1.1				fail: go 1.3.1
//  1.2.1.2				success: assign $rootScope.jobs
//  1.3.1 		get jobs from server
//  1.3.1.1			assign $rootScope.jobs
//  1.3.1.2 			encrypt and save to localStorage
    function gettingJobList(jobListType){
    	var deferral = $q.defer();
    	if(!$rootScope[jobListType]){
    		var isCompletedJob = jobListType === 'COMPLETED_JOB_LIST' ? true : false;
			getItem(jobListType)
			.then(function(response){
				$rootScope[jobListType] = response.value;
				deferral.resolve({
    				jobs : $rootScope[jobListType]
    			});
			}, function(error){
				jobService.getJobList(isCompletedJob)
        		.then(function(data){
        			$rootScope[jobListType] = data;
        			deferral.resolve({
        				jobs : $rootScope[jobListType]
        			});
        			setItem(jobListType, data);
        			console.log("job list type:" + jobListType + " length:" + data.length)
        		});
			});
    	} else {
    		deferral.resolve({
    			jobs : $rootScope[jobListType]
    		});
    	}
    	return deferral.promise;
    }
    
    function gettingCompanies(){
    	var deferral = $q.defer();
    	if(!$rootScope.companies){
    		adlService.obtainCompanyCodeAndName()
    		.then(function(data){
    			$rootScope.companies = data.map( function (company) {
			        return {
			          value: company.companyCode.toLowerCase(),
			          display: company.companyCode + ' - ' + company.companyName
			        };
    			});
				deferral.resolve({
					companies: $rootScope.companies
				});
    		});
    	} else {
    		deferral.resolve({
    			companies: $rootScope.companies
    		});
    	}
    	return deferral.promise;
    }
    
    function gettingDivisions(){
    	var deferral = $q.defer();
    	if(!$rootScope.divisions){
    		jobService.obtainAllJobDivision()
  	      	.then(function(data){
  	    	  $rootScope.divisions = data.map( function (division) {
  	  	        return {
  	  	          value: division.toLowerCase(),
  	  	          display: division
  	  	        };
  	    	  });
  	    	  deferral.resolve({
  	    		  divisions: $rootScope.divisions
    		  });
    	  });
    	} else {
    		deferral.resolve({
    			divisions: $rootScope.divisions
    		})
    	}
    	return deferral.promise;
    }
    
	function gettingUser(){
		var deferral = $q.defer();
		if(!$rootScope.user){
			$http.get('service/security/getCurrentUser')
			.then(function(response){
				$rootScope.user = response.data;
				configHiddenMenu();
				deferral.resolve({
					user : $rootScope.user
				})
			})
		} else {
			deferral.resolve({
				user: $rootScope.user
			})
		}
		return deferral.promise;
	}
	
	function gettingSessionId(){
		var deferral = $q.defer();
		if(!$rootScope.sessionId){
			SessionHelper.getCurrentSessionId()
			.then(function(data){
			    $rootScope.sessionId = data;
			    deferral.resolve({
			    	sessionId : $rootScope.sessionId
			    });
			});
		} else {
			deferral.resolve({
				sessionId : $rootScope.sessionId
			});
		}
		return deferral.promise;
	}

	function gettingProperties(){
		var deferral = $q.defer();
		if(!$rootScope.properties){
			$http.post('service/properties/getProperties')
			.then(function(response){
	    		$rootScope.properties = response.data;
	    		deferral.resolve({
	    			properties : $rootScope.properties
	    		});
	    	});
		} else {
    		deferral.resolve({
    			properties : $rootScope.properties
    		});
		}
		return deferral.promise;
	}
	
	function gettingWorkScopes(){
		var deferral = $q.defer();
		if(!$rootScope.workScopes){
			unitService.getAllWorkScopes()
			.then(function(data){
				$rootScope.workScopes = data;
				deferral.resolve({
					workScopes : $rootScope.workScopes
				});
			})
		} else {
			deferral.resolve({
				workScopes: $rootScope.workScopes
			});
		}
		return deferral.promise;
	}
	
	function getPreviousStatus(){
		return $rootScope.previousStatus;
	}
	
	function setPreviousStatus(previousStatus){
		$rootScope.previousStatus = previousStatus;
	}

	function getRoutedToDefaultJob(){
		return $rootScope.routedToDefaultJob;
	}
	
	function setRoutedToDefaultJob(routed){
		$rootScope.routedToDefaultJob = routed;
	}
	
	function getSelectedTips(){
		return $rootScope.selectedTips;
	}
	
	function setSelectedTips(tips) {
		$rootScope.selectedTips = tips;
	}

	function configHiddenMenu(){
		$rootScope.showQSAdmin = GlobalHelper.containRole('ROLE_PCMS_QS_ADMIN', $rootScope.user.UserRoles);
		$rootScope.showIMSAdmin = GlobalHelper.containRole('ROLE_PCMS_IMS_ADMIN', $rootScope.user.UserRoles);
		if($rootScope.showQSAdmin || $rootScope.showIMSAdmin){
			$rootScope.showAdminMenu = true;
		} else {
			$rootScope.showAdminMenu = GlobalHelper.containRole('ROLE_PCMS_IMS_ENQ', $rootScope.user.UserRoles);
		}
	}
	
	function setEnv(){
		var env = [];
		env['DEV'] = ['localhost', 'erpwls11'];
		env['UAT'] = ['erpwls12'];
		env['PRO'] = ['erpwls01', 'erpwls02'];
		$rootScope.env = 'PRO';
		for(var key in env){
			env[key].forEach(function(e){
				if($window.location.hostname.indexOf(e) >= 0){
					$rootScope.env = key;
					//console.log('Env:'+e + ' ' + $rootScope.env);
				}
			})
		}
		cleanupLocalStore();
	}
	
	function getShowAdminMenu(){
		return $rootScope.showAdminMenu;
	}
	
    //====================================================
    function obtainCacheKey(itemType){
    	if(!cacheKeyArray) cacheKeyArray = [];
    	var deferral = $q.defer();
    	if(!cacheKeyArray[itemType]){
    		$http.post('service/properties/obtainCacheKey', JSON.stringify(itemType))
    		.then(function(response){
    			cacheKeyArray[itemType] = response.data;
    			deferral.resolve({
    				cacheKey : cacheKeyArray[itemType]
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

    function cleanupLocalStore(){
    	var validCacheItem = ['ONGOING_JOB_LIST', 'COMPLETED_JOB_LIST'];
    	for(var i = 0; i < localStorage.length; i++){
    		var key = localStorage.key(i);
    		if(validCacheItem.indexOf(key) < 0 && key.indexOf('TourShow') < 0){
    			removeItem(key);
    		}
    	}
    }    
}]);




