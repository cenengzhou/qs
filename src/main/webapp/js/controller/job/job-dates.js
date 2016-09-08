mainApp.controller('JobDatesCtrl', ['$scope','jobService', 'modalService', '$state', 'GlobalParameter',
                                   function($scope, jobService, modalService, $state, GlobalParameter) {

	loadJobDates();
	$scope.GlobalParameter = GlobalParameter;
	function loadJobDates() {
		jobService.getJob($scope.jobNo)
		.then(
				function( data ) {
					$scope.job = data;
				});
		
		jobService.getJobDates($scope.jobNo)
		.then(
				function( data ) {
					console.log(data);
					$scope.jobDates = data;
				});
	}


	$scope.saveDates = function () {
		console.log($scope.job);
		updateJobInfo($scope.job);
	};
	
	function updateJobInfo(job){
		jobService.updateJobInfo(job)
		.then(
				function( data ) {
					if(data.length>0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						updateJobDates($scope.jobDates)
					}
				});
		}
	
	function updateJobDates(jobDates){
		jobService.updateJobDates(jobDates)
		.then(
				function( data ) {
					if(data.length>0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
				    	modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Job Dates has been updated successfully.");
				    	$state.reload();
					}
				});
		}

}]);

