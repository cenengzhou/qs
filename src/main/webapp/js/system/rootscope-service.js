mainApp.service('rootscopeService', ['$http', '$q', '$window', 'GlobalHelper', '$rootScope', 'jobService', 'jdeService', 'adlService', 'SessionHelper',
	function($http, $q, $window, GlobalHelper, $rootScope, jobService, jdeService, adlService, SessionHelper){
	// Return public API.
    return({
    	gettingJob:			gettingJob,
    	gettingJobList:		gettingJobList,
    	gettingCompanies:	gettingCompanies,
    	gettingDivisions:	gettingDivisions,
    	gettingUser:		gettingUser,
    	gettingSessionId:	gettingSessionId,
    	gettingProperties:	gettingProperties,
    	gettingWorkScopes:	gettingWorkScopes,
    	gettingAddressBookListOfSubcontractor:	gettingAddressBookListOfSubcontractor,
    	gettingAddressBookListOfClient:	gettingAddressBookListOfClient,
    	gettingAddressBookListOfSubcontractorAndClient: gettingAddressBookListOfSubcontractorAndClient,
    	getPreviousStatus:	getPreviousStatus,
    	setPreviousStatus:	setPreviousStatus,
    	getRoutedToDefaultJob:	getRoutedToDefaultJob,
    	setRoutedToDefaultJob:	setRoutedToDefaultJob,
    	getSelectedTips:	getSelectedTips,
    	setSelectedTips:	setSelectedTips,
    	configHiddenMenu:	configHiddenMenu,
    	setEnv:				setEnv,
    	getShowAdminMenu:	getShowAdminMenu,
    	checkMaintenance:	checkMaintenance,
    });
    
    function gettingJob(jobNo){
    	var deferral = $q.defer();
    	if(!$rootScope.jobDetails) $rootScope.jobDetails = {};
    	if($rootScope.jobDetails[jobNo] && $rootScope.jobDetails[jobNo].jobNumber == jobNo){
    		deferral.resolve({
    			job: $rootScope.jobDetails[jobNo]
    		});
    	} else{
    		jobService.getJob(jobNo)
    		.then(function(data){
    			$rootScope.jobDetails[jobNo] = data;
    			deferral.resolve({
        			job: $rootScope.jobDetails[jobNo]
        		})
    		})
    	}
    	return deferral.promise;
    }
    
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
        			console.log("obtain job list from server | type:" + jobListType + " length:" + data.length)
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
  	  	          value: division,
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
				checkMaintenance();
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
			$http.post('service/system/getProperties')
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
			adlService.getAllWorkScopes()
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
	
	function gettingAddressBookListOfClient(){
		var deferral = $q.defer();
		if(!$rootScope.addressBookListOfClient){
			gettingAddressBookListOfSubcontractorAndClient()
			.then(function(response){
				$rootScope.addressBookListOfClient = getAddressBookList(response.addressBookListOfSubcontractorAndClient, 'C  ');
				deferral.resolve({
					addressBookListOfClient : $rootScope.addressBookListOfClient
				});
			});
		} else {
			deferral.resolve({
				addressBookListOfClient : $rootScope.addressBookListOfClient
			});
		}
		return deferral.promise;		
	}
	
	function gettingAddressBookListOfSubcontractor(searchSubcontractor, searchWorkScopes){
		var deferral = $q.defer();
		gettingAddressBookListOfSubcontractorAndClient()
		.then(function(response){
			var addressBookListOfSubcontractor = getAddressBookList(response.addressBookListOfSubcontractorAndClient, 'V  ');
			var filteredAddressBookListOfSubcontractor = addressBookListOfSubcontractor.filter(function(item){
				var subcon = true;
				var ws = false;
				if(searchSubcontractor) {
					subcon = item.addressBookName.toLowerCase().indexOf(searchSubcontractor.toLowerCase()) > -1 || item.addressBookNumber == searchSubcontractor;
				}
				if(searchWorkScopes) {
					if(item.subcontractorWorkscopes && item.subcontractorWorkscopes.length > 0){
						var workscopes = item.subcontractorWorkscopes;
						workscopes.forEach(function(workscope){
							if(!ws && workscope.codeWorkscope.trim() == searchWorkScopes){
								ws = true;
							}
						})
					}
						
//					workscope = item.subcontractorWorkscopes && item.subcontractorWorkscopes.indexOf(searchWorkScopes) > -1;
				} else {
					ws = true;
				}
				
				return subcon && ws;
			});
			
			deferral.resolve({
				addressBookListOfSubcontractor : filteredAddressBookListOfSubcontractor
			});
		});
		return deferral.promise;
	}
	
	function getAddressBookList(array, addressBookTypeCode) {
		return array.filter(function(item){
			return item.addressBookTypeCode === addressBookTypeCode;
		});
	}
	
	function gettingAddressBookListOfSubcontractorAndClient(){
		var deferral = $q.defer();
		if(!$rootScope.addressBookListOfSubcontractorAndClient){
			adlService.getAddressBookListOfSubcontractorAndClient()
			.then(function(data){
				$rootScope.addressBookListOfSubcontractorAndClient = data;
				deferral.resolve({
					addressBookListOfSubcontractorAndClient : $rootScope.addressBookListOfSubcontractorAndClient
				});
			});
		} else {
			deferral.resolve({
				addressBookListOfSubcontractorAndClient : $rootScope.addressBookListOfSubcontractorAndClient
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
		$rootScope.showQSAdmin = GlobalHelper.containRole('ROLE_QS_QS_ADM', $rootScope.user.UserRoles)
								||GlobalHelper.containRole('ROLE_PCMS_QS_ADMIN', $rootScope.user.UserRoles);
		$rootScope.showIMSAdmin = GlobalHelper.containRole('ROLE_QS_IMS_ADM', $rootScope.user.UserRoles)
								||GlobalHelper.containRole('ROLE_PCMS_IMS_ADMIN', $rootScope.user.UserRoles);
		$rootScope.showIMSEnquiry = GlobalHelper.containRole('ROLE_QS_IMS_ENQ', $rootScope.user.UserRoles)
								||GlobalHelper.containRole('ROLE_PCMS_IMS_ENQ', $rootScope.user.UserRoles);
		$rootScope.showEditParentJob = GlobalHelper.containRole('ROLE_QS_QS_ADM', $rootScope.user.UserRoles);
		if($rootScope.showQSAdmin || $rootScope.showIMSAdmin || $rootScope.showIMSEnquiry){
			$rootScope.showAdminMenu = true;
		} else {
			$rootScope.showAdminMenu = GlobalHelper.containRole('ROLE_QS_IMS_ENQ', $rootScope.user.UserRoles)
									||GlobalHelper.containRole('ROLE_PCMS_IMS_ENQ', $rootScope.user.UserRoles);
		}
	}
	
	function checkMaintenance(){
		gettingUser()
		.then(function(response){
			if(GlobalHelper.containRole('ROLE_MAINTENANCE', response.user.UserRoles)){
				$window.location = '503.html';
			}
		});
	}
	
	function setEnv(){
		var env = [];
		env['DEV'] = ['localhost', 'erpwls11', 'gambpm11'];
		env['UAT'] = ['erpwls12', 'gambpm12'];
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
    	if(!$rootScope.cacheKeyArray) $rootScope.cacheKeyArray = [];
    	var deferral = $q.defer();
    	if(!$rootScope.cacheKeyArray[itemType]){
    		$http.post('service/system/obtainCacheKey', JSON.stringify(itemType))
    		.then(function(response){
    			$rootScope.cacheKeyArray[itemType] = response.data;
    			deferral.resolve({
    				cacheKey : $rootScope.cacheKeyArray[itemType]
    			});
    		})
    	} else {
    		deferral.resolve({
    			cacheKey: $rootScope.cacheKeyArray[itemType]
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
    				console.warn('Fail to decrypt ' + key + ' with ' + response.cacheKey + ' | ' + e);
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
    			console.warn(key + ' item encrypt fail:' + e);
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




