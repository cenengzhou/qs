mainApp.controller('JobSelectCtrl', ['$scope', 'colorCode', 'jobService', '$animate',  '$cookies',
                               function($scope, colorCode, jobService, $animate, $cookies) {
	$scope.loading = true;
	$scope.selectedDivision = '';
	$scope.searchquery = '';

	//Clear cookies
	var cookies = $cookies.getAll();
	angular.forEach(cookies, function (v, k) {
		$cookies.remove(k);
	});
	
	loadJobList();
	
    
	function loadJobList() {
		jobService.getJobList()
		.then(
				function( data ) {
					$scope.jobs= data;
				});
	}
	
	
	$scope.updateJobInfo = function (jobNo, jobDescription) {
    	$cookies.put('jobNo', jobNo);
    	$cookies.put('jobDescription', jobDescription);
    }
	
	$scope.removeDefaultAnimation = function (){
        $animate.enabled(false);
    };
    
    
    
    
    
}]);