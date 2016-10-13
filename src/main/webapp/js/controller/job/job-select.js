mainApp.controller('JobSelectCtrl', ['$scope', '$state', 'colorCode', 'jobService', '$animate',  '$cookies', 'modalService', '$rootScope', 'storageService', 'userpreferenceService',
                               function($scope, $state, colorCode, jobService, $animate, $cookies, modalService, $rootScope, storageService, userpreferenceService) {
	$rootScope.selectedTips = '';
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
		if(!$rootScope.routedToDefaultJob){
			userpreferenceService.gettingUserPreference()
			.then(function(response){
				if(response.userPreference && response.userPreference.DEFAULT_JOB_NO){
					$scope.updateJobInfo(response.userPreference.DEFAULT_JOB_NO, response.userPreference.DEFAULT_JOB_DESCRIPTION);
					$rootScope.routedToDefaultJob = true;
					$state.go('job.dashboard');
				} else {
					getJobList();
				}
			})
			return;
		}
		getJobList();
	}
	
	function getJobList(){
		storageService.gettingJobList()
		.then(function( response ) {
			$scope.jobs= response.jobs;
		});
	}
	
	$scope.updateJobInfo = function (jobNo, jobDescription) {
    	$cookies.put('jobNo', jobNo);
    	$cookies.put('jobDescription', jobDescription);
    }
	
	$scope.createJob = function (){
		modalService.open('md', 'view/transit/modal/transit-header-modal.html', 'TransitHeaderModalCtrl');
	}
	
	$scope.transitEnquiry = function(){
		modalService.open('lg', 'view/transit/modal/transit-enquiry-modal.html', 'TransitEnquiryCtrl');
	}
	
	$scope.removeDefaultAnimation = function (){
        $animate.enabled(false);
    };
    
    
    
    
    
}]);