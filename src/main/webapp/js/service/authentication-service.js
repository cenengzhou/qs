mainApp.factory('authenticationService', ['$http', '$cookieStore', '$rootScope', 'Base64',
                                          function($http, $cookieStore, $rootScope, Base64){
	//Method 1
	 /*return({
		 setCredentials: setCredentials,
		 clearCredentials: clearCredentials
	        
	    });

	
	function setCredentials(username, password) {
		var authdata = Base64.encode(username + ':' + password);

		$rootScope.globals = {
				currentUser: {
					username: username,
					authdata: authdata
				}
		};

		$http.defaults.headers.common['Authorization'] = 'Basic ' + authdata; 
		$cookieStore.put('globals', $rootScope.globals);
	}

	function clearCredentials() {
		$rootScope.globals = {};
		$cookieStore.remove('globals');
		delete $http.defaults.headers.common.Authorization;
	}*/
	
	
	//Method2
	// initialize to whatever is in the cookie, if anything
	/*$http.defaults.headers.common['Authorization'] = 'Basic ' + $cookieStore.get('authdata');

	return {
		setCredentials: function (username, password) {
			var encoded = Base64.encode(username + ':' + password);
			$http.defaults.headers.common.Authorization = 'Basic ' + encoded;
			$cookieStore.put('authdata', encoded);
		},
		clearCredentials: function () {
			document.execCommand("ClearAuthenticationCache");
			$cookieStore.remove('authdata');
			$http.defaults.headers.common.Authorization = 'Basic ';
		}
	};*/
	
}]);