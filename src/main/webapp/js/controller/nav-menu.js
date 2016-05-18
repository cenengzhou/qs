mainApp.controller('NavMenuCtrl', ['$http', '$scope', '$location', function($http, $scope, $location) {
	$scope.activeMenu = '';
    $scope.activeJobSideMenu = 'JobDashboard';
    $scope.activeSubcontractSideMenu = 'SubcontractDashboard';
    $scope.activeIVSideMenu = 'IVUpdate';
      
    
    $scope.logout = function() {
    	console.log("Call logout function");
    	$http.post('logout', {}).success(function() {
    		
    		
    	}).error(function(data) {
    		//$rootScope.authenticated = false;
    	});
    }
}]);