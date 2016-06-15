mainApp.controller('AppCtrl', ['$http', '$scope', '$location', function($http, $scope, $location) {
	   
    $scope.$on('$includeContentLoaded', function() {
        App.initComponent();
    });
    $scope.$on('$viewContentLoaded', function() {
        App.initComponent();
    });
    $scope.$on('$stateChangeStart', function() {
    	App.initComponent();
        App.scrollTop();
        $('.pace .pace-progress').addClass('hide');
        $('.pace').removeClass('pace-inactive');
    });
    $scope.$on('$stateChangeSuccess', function() {
        Pace.restart();
        App.initPageLoad();
        //App.initSidebarSelection();
    });
    $scope.$on('$stateNotFound', function() {
        Pace.stop();
    });
    $scope.$on('$stateChangeError', function() {
        Pace.stop();
    });
    
    
    //Logout
    $scope.logout = function() {
    	console.log("Call logout function");
    	$http.post('logout', {}).success(function() {
    		$location.path("/logout");
    		
    	}).error(function(data) {
    		//$rootScope.authenticated = false;
    	});
    }
  
}]);