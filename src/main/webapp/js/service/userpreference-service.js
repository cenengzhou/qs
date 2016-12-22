mainApp.service('userpreferenceService', ['$http', '$q', 'GlobalHelper', '$rootScope',
								function($http, $q, GlobalHelper, $rootScope){
	// Return public API.
    return({
    	gettingUserPreference:						gettingUserPreference,
    	settingDefaultJobNo:						settingDefaultJobNo,
    	getNotificationReadStatusByCurrentUser: 	getNotificationReadStatusByCurrentUser,
    	insertNotificationReadStatusByCurrentUser: insertNotificationReadStatusByCurrentUser,
    	updateNotificationReadStatusByCurrentUser: updateNotificationReadStatusByCurrentUser
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
				})
			});
		} else {
			deferral.resolve({
				userPreference : $rootScope.userPreference
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

	
}]);




