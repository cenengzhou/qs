mainApp.controller('NavMenuCtrl', ['$scope','$http','$rootScope', '$location',  function($scope, $http, $rootScope, $location) {
	$scope.activeMenu = '';
    $scope.activeJobSideMenu = 'JobDashboard';
    $scope.activeSubcontractSideMenu = 'SubcontractDashboard';
    $scope.activeIVSideMenu = 'IVUpdate';


    $scope.logout = function() {
    	console.log("Call logout function");
    	$http.post('logout', {}).success(function() {
    		$rootScope.authenticated = false;
    		console.log("$rootScope.authenticated: "+$rootScope.authenticated);
    		$location.path("/logout");
    		
    	}).error(function(data) {
    		$rootScope.authenticated = false;
    	});
    }
}]);