mainApp.controller('JobInfoCtrl', ['$scope','jobService', 'modalService', '$state', 'GlobalParameter', 'rootscopeService',
                                   function($scope, jobService, modalService, $state, GlobalParameter, rootscopeService) {
	rootscopeService.setSelectedTips('');
	$scope.GlobalParameter = GlobalParameter;
	loadJobInfo();
	
	function loadJobInfo() {
		jobService.getJob($scope.jobNo)
		.then(
				function( data ) {
					$scope.job = data;
					
					if($scope.job.cpfApplicable == "1")
						$scope.job.cpfApplicable = true;
					else
						$scope.job.cpfApplicable = false;
					
					if($scope.job.levyApplicable == "1")
						$scope.job.levyApplicable = true;
					else
						$scope.job.levyApplicable = false;
					
					$scope.job.insuranceCAR = data.insuranceCAR + ' - ' + GlobalParameter.getValueById(GlobalParameter.insuranceStatus, data.insuranceCAR);
					$scope.job.insuranceECI = data.insuranceECI + ' - ' + GlobalParameter.getValueById(GlobalParameter.insuranceStatus, data.insuranceECI);
					$scope.job.insuranceTPL = data.insuranceTPL + ' - ' + GlobalParameter.getValueById(GlobalParameter.insuranceStatus, data.insuranceTPL);
					
				});
		jobService.getJobDates($scope.jobNo)
		.then(
				function( data ) {
					$scope.jobDates = data;
				});
	}


	//Save Function
	$scope.saveJobInfo = function () {
		if (false === $('form[name="levyForm"]').parsley().validate()) {
			event.preventDefault();  
			return;
		}
		if (false === $('form[name="cpfForm"]').parsley().validate()) {
			event.preventDefault();  
			return;
		}
		
		
		if($scope.job.cpfApplicable == true)
			$scope.job.cpfApplicable = "1";
		else{
			$scope.job.cpfApplicable = "0";
			$scope.job.cpfBaseYear = "";
			$scope.job.cpfBasePeriod ="";
			$scope.job.cpfIndexName = "";
		}
		
		if($scope.job.levyApplicable == true)
			$scope.job.levyApplicable = "1";
		else{
			$scope.job.levyApplicable = "0";
			$scope.job.levyCITAPercent = 0;
			$scope.job.levyPCFBPercent = 0;
		}
		
		updateJobInfoAndDates($scope.job, $scope.jobDates)
		
	};
	
	function updateJobInfoAndDates(job, jobDates){
		jobService.updateJobInfoAndDates(job, jobDates)
		.then(
				function( data ) {
					if(data.length>0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
				    	modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Job has been updated successfully.");
				    	$state.reload();
					}
				});
		}


}]);

