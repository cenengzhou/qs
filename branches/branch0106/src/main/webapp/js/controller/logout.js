mainApp.controller('LogoutCtrl', ['$http', '$cookies',
                                  function($http, $cookies) {
	$http.get('logout');
	
	//Clear cookies when logout
	var cookies = $cookies.getAll();
	angular.forEach(cookies, function (v, k) {
		$cookies.remove(k);
	});

}]);