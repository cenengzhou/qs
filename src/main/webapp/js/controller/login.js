mainApp.controller('LoginCtrl', ['$rootScope', '$scope', '$http', '$location',
                                 function($rootScope, $scope, $http, $location) {



	/*init();
    function init() {
    	console.log("Login - clearCredentials");
    	 // reset login status
         authenticationService.clearCredentials();
     }*/

	var authenticate = function(credentials, callback) {
		console.log("Login - authenticate");
		var headers = credentials ? {authorization : "Basic "
			+ btoa(credentials.username + ":" + credentials.password)
		} : {};

		$http.get('user', {headers : headers}).success(function(data) {
			if (data.name) {
				$rootScope.authenticated = true;
			} else {
				$rootScope.authenticated = false;
			}
			callback && callback();
		}).error(function() {
			$rootScope.authenticated = false;
			callback && callback();
		});

	}

	//authenticate();
	$scope.credentials = {};
	$scope.login = function() {
		authenticate($scope.credentials, function() {console.log("Login function");
			if ($rootScope.authenticated) {
				//authenticationService.setCredentials($scope.credentials.username, $scope.credentials.password);
				console.log("Authenticated and go to Index page");
				$location.path("/select-job");
				$scope.error = false;
			} else {
				console.log(" Not authenticated and go to Login page");
				$location.path("/login");
				$scope.error = true;
			}
		});
	};
}]);