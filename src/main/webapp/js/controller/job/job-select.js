mainApp.controller('JobSelectCtrl', ['$scope', '$rootScope', 'colorCode', 'jobService', '$animate',  '$cookies',
                               function($scope, $rootScope, colorCode, jobService, $animate, $cookies) {
	$scope.loading = true;
	

	$scope.searchquery = '';
	
	//$scope.searchJob = function () {
		loadJobList();
	//};
	
	
    
	function loadJobList() {
		jobService.getJobList()
		.then(
				function( data ) {
					$scope.jobs= data;
				});
	}
	
	$scope.removeDefaultAnimation = function (){
        $animate.enabled(false);
    };
    
    
    
    
    
}]);