mainApp.controller('AppCtrl', ['$http', '$scope', '$location', '$window', 'SessionHelper',
                     function($http, $scope, $location, $window, SessionHelper) {

    $scope.$on('$includeContentLoaded', function(event) {
        App.initComponent();
    });
    $scope.$on('$viewContentLoaded', function(event, viewConfig) {
        App.initComponent();
    });
    $scope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams, options) {
    	SessionHelper.validateSession()
    	.then(function(data){
    		if(!data){
    			if(toState.name != 'logout'){
    				$window.location.href = 'login.htm?ValidateCurrentSessionFailed';
    			}
    		}
    	},  function(data){
				$window.location.href = 'login.htm?ValidateCurrentSessionError';
		});
    	App.initComponent();
        //App.scrollTop();
        $('.pace .pace-progress').addClass('hide');
        $('.pace').removeClass('pace-inactive');
    });
    $scope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
        Pace.restart();
        App.initPageLoad();
        //App.initSidebarSelection();
    });
    $scope.$on('$stateNotFound', function(event, unfoundState, fromState, fromParams) {
        Pace.stop();
    });
    $scope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParams, error) {
    	$window.location.href = 'login.htm?StateChangeError';
        Pace.stop();
    });
    
    
    //Logout
    $scope.logout = function() {
//    	console.log("Call logout function");
    	$location.path("/logout");
    	
    }
  
}]);