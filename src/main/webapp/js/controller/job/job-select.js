mainApp.controller('JobSelectCtrl', ['$scope', 'colorCode', 'jobService', '$animate',  '$cookies', 'modalService', '$rootScope',
                               function($scope, colorCode, jobService, $animate, $cookies, modalService, $rootScope) {
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