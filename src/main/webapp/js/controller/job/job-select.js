mainApp.controller('JobSelectCtrl', ['$scope', 'colorCode', 'jobService', '$animate',  '$cookies',
                               function($scope, colorCode, jobService, $animate, $cookies) {
	$scope.loading = true;
	$scope.selectedDivision = '';

	$scope.searchquery = '';
	
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