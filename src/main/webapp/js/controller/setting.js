mainApp.controller('AppCtrl', ['$http', '$scope', '$location', '$window', function($http, $scope, $location, $window) {
	   
    $scope.$on('$includeContentLoaded', function() {
        App.initComponent();
    });
    $scope.$on('$viewContentLoaded', function() {
        App.initComponent();
    });
    $scope.$on('$stateChangeStart', function() {
        // reset layout setting
        /*$rootScope.setting.layout.pageSidebarMinified = false;
        $rootScope.setting.layout.pageFixedFooter = false;
        $rootScope.setting.layout.pageRightSidebar = true;
        $rootScope.setting.layout.pageTwoSidebar = false;
        $rootScope.setting.layout.pageTopMenu = false;
        $rootScope.setting.layout.pageBoxedLayout = false;
        $rootScope.setting.layout.pageWithoutSidebar = false;
        $rootScope.setting.layout.pageContentFullHeight = false;
        $rootScope.setting.layout.pageContentWithoutPadding = false;
        $rootScope.setting.layout.paceTop = false;*/
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
    	$window.location.href = 'login.htm';
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