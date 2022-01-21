mainApp.controller('JobSelectCtrl', ['$scope', '$state', '$timeout', 'colorCode', 'jobService', '$animate',  '$cookies', 'modalService', 'rootscopeService', 'userpreferenceService', 'blockUI',
                               function($scope, $state, $timeout, colorCode, jobService, $animate, $cookies, modalService, rootscopeService, userpreferenceService, blockUI) {
	rootscopeService.setSelectedTips('');
	$scope.loading = true;
	$scope.selectedDivision = '';
	$scope.searchquery = '';
	$scope.showCompleted = true;
	//Clear cookies
	var cookies = $cookies.getAll();
	angular.forEach(cookies, function (v, k) {
		$cookies.remove(k);
	});
	
	$scope.hasRole = rootscopeService.hasRole;
	loadJobList();
	
	function loadJobList() {
		if(!rootscopeService.getPreviousStatus() && !rootscopeService.getRoutedToDefaultJob()){
			userpreferenceService.gettingUserPreference()
			.then(function(response){
				if(response.userPreference && response.userPreference.DEFAULT_JOB_NO){
					$scope.updateJobInfo(response.userPreference.DEFAULT_JOB_NO, response.userPreference.DEFAULT_JOB_DESCRIPTION);
					rootscopeService.setRoutedToDefaultJob(true);
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
		blockUI.start('Loading...');
		$timeout(function(){
		rootscopeService.gettingJobList('ONGOING_JOB_LIST')
		.then(function( response ) {
			$scope.jobs = response.jobs;
			blockUI.stop();
			if($scope.showCompleted){
				blockUI.start('Loading...');
				$timeout(function(){
				rootscopeService.gettingJobList('COMPLETED_JOB_LIST')
				.then(function( response ) {
					$scope.jobs = $scope.jobs.concat(response.jobs);
					$scope.jobs.sort(function(a,b){return a.jobNo - b.jobNo;});
					blockUI.stop();
					$scope.jobListOffset = document.getElementById('job-list').offsetTop;
				});
				}, 150);
			}
		});
		}, 150);
	}
	
	$scope.updateJobInfo = function (jobNo, jobDescription) {
    	$cookies.put('jobNo', jobNo);
    	$cookies.put('jobDescription', jobDescription);
    	rootscopeService.defaultRoute();
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
    
    $scope.updateJobList = function(){
    	getJobList();
    }
}]);