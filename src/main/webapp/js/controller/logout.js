mainApp.controller('LogoutCtrl', ['$http', '$location',
                                 function($http, $location) {
	$http.post('logout');
}]);