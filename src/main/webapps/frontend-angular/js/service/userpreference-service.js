mainApp.service('userpreferenceService', ['$http', '$q', 'GlobalHelper', '$rootScope',
								function($http, $q, GlobalHelper, $rootScope){
	// Return public API.
    return({
    	gettingUserPreference:						gettingUserPreference,
    	settingDefaultJobNo:						settingDefaultJobNo,
    	getNotificationReadStatusByCurrentUser: 	getNotificationReadStatusByCurrentUser,
    	insertNotificationReadStatusByCurrentUser: insertNotificationReadStatusByCurrentUser,
    	updateNotificationReadStatusByCurrentUser: updateNotificationReadStatusByCurrentUser,
    	updateAnnouncentSetting: 						updateAnnouncentSetting,
    	gettingGridPreference:		gettingGridPreference,
    	savingGridPreference:		savingGridPreference,
    	clearGridPreference:		clearGridPreference,
    });
   
    function obtainUserPreferenceByCurrentUser(){
    	var request = $http.post('service/userPreference/obtainUserPreferenceByCurrentUser');
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

	function gettingUserPreference(reload){
		var deferral = $q.defer();
		if(reload || !$rootScope.userPreference){
			obtainUserPreferenceByCurrentUser()
			.then(function(data){
				$rootScope.userPreference = data;
				deferral.resolve({
					userPreference : $rootScope.userPreference
				});
			});
		} else {
			deferral.resolve({
				userPreference : $rootScope.userPreference
			});
		}
		return deferral.promise;
	}
	
	function savingGridPreference(gridName, state){
		var deferral = $q.defer();
		gettingUserPreference()
		.then(function(response){
			$rootScope.gridPreference[$rootScope.userPreference['GRIDPREFIX'] + gridName] = state;
			var data = {};
			data[($rootScope.userPreference['GRIDPREFIX'] + gridName)] = JSON.stringify($rootScope.gridPreference[$rootScope.userPreference['GRIDPREFIX'] + gridName]);
	    	var request = $http({
	    		method: 'post',
	    		url: 'service/userPreference/saveGridPreference',
	    		data: data
	    	});
	    	return( request.then( deferral.resolve, deferral.reject ) );
		});
		return deferral.promise;
	}
	
	function clearGridPreference(gridName){
		var deferral = $q.defer();
		var request = $http({
    		method: 'post',
    		url: 'service/userPreference/clearGridPreference',
    		params:{gridName: gridName}
    	}).then(function(response){
    		delete $rootScope.gridPreference[$rootScope.userPreference['GRIDPREFIX'] + gridName];
    		return deferral.resolve();
    	}, function(error){
    		return deferral.reject();
    	});
		return deferral.promise;
	}
	
	function gettingGridPreference(reload){
		var deferral = $q.defer();
		if(!$rootScope.gridPreference){
			gettingUserPreference(reload)
			.then(function(response){
				$rootScope.gridPreference = {};
				angular.forEach(response.userPreference, function(preference, key){
					if(key.indexOf(response.userPreference['GRIDPREFIX']) >= 0){
//						console.log('Key:' + key + ' pref:' + preference);
						$rootScope.gridPreference[key] = JSON.parse(preference);
					};
				});
				deferral.resolve({
					gridPreference : $rootScope.gridPreference,
					gridPrefix : $rootScope.userPreference['GRIDPREFIX']
				});
			});
		} else {
			deferral.resolve({
				gridPreference : $rootScope.gridPreference,
				gridPrefix : $rootScope.userPreference['GRIDPREFIX']
			});
		}
		return deferral.promise;
	}
	
	function settingDefaultJobNo(defaultJobNo){
		var request = $http.post('service/userPreference/setDefaultJobNo', JSON.stringify(defaultJobNo));
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
	
	function getNotificationReadStatusByCurrentUser(){
		var request = $http.get('service/userPreference/getNotificationReadStatusByCurrentUser');
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
	
	function insertNotificationReadStatusByCurrentUser(){
		var request = $http.post('service/userPreference/insertNotificationReadStatusByCurrentUser');
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function updateNotificationReadStatusByCurrentUser(status){
		var request = $http.post('service/userPreference/updateNotificationReadStatusByCurrentUser', JSON.stringify(status));
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	
	function updateAnnouncentSetting(setting){
		var request = $http.post('service/userPreference/updateAnnouncentSetting', JSON.stringify(setting));
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
}]);




