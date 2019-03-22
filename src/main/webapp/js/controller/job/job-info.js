mainApp.controller('JobInfoCtrl', ['$scope','jobService', 'modalService', '$sce','$state', 'GlobalParameter', 'rootscopeService', 'subcontractService', '$timeout',
                                   function($scope, jobService, modalService, $sce,$state, GlobalParameter, rootscopeService, subcontractService, $timeout) {
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
					
					if($scope.job.innovationApplicable == "1")
						$scope.job.innovationApplicable = true;
					else
						$scope.job.innovationApplicable = false;
					
					$scope.insuranceCARDesc = data.insuranceCAR + ' - ' + GlobalParameter.getValueById(GlobalParameter.insuranceStatus, data.insuranceCAR);
					$scope.insuranceECIDesc = data.insuranceECI + ' - ' + GlobalParameter.getValueById(GlobalParameter.insuranceStatus, data.insuranceECI);
					$scope.insuranceTPLDesc = data.insuranceTPL + ' - ' + GlobalParameter.getValueById(GlobalParameter.insuranceStatus, data.insuranceTPL);
					$scope.job.projectFullDescription = $scope.job.projectFullDescription.split("\n").join("<br>")
					subcontractService.getParentSubcontractList($scope.jobNo)
					.then(function(data){
						$scope.parentSubcontractList = data;
					});
				});
		jobService.getJobDates($scope.jobNo)
		.then(
				function( data ) {
					$scope.jobDates = data;
				});
	}
	
	$scope.trustAsHtml = function(string) {
	    return $sce.trustAsHtml(string);
	};
	
	$scope.divHeight = {'jobDesc': {'preview': false, 'minHeight': 90, 'offsetHeight': 0}};

	$scope.changeDivHeight = function(id){
		var div = $('#'+id)[0];
		var item = $scope.divHeight[id];
		if(item && !item.offsetHeight) {
			item.offsetHeight = div.offsetHeight
		}
		
		var needCollapse = item.offsetHeight > item.minHeight;
		
		if(item.preview) {
			if(needCollapse) item.preview = false;
			$(div).animate({'height' : item.offsetHeight}, 500);		
		} else {	
			if(needCollapse) item.preview = true;
			var minHeight = item.offsetHeight > item.minHeight ? item.minHeight : item.offsetHeight;
			$(div).animate({'height' : minHeight}, 500);
		}
	}
	
	$timeout(function(){
		$scope.changeDivHeight('jobDesc');
	}, 500);
	
	$scope.syncEotAndRevisedDate = function(item){
		if($scope.loaded)
		$timeout(function(){
			switch (item){
			case 'revised':
				if($scope.jobDates.revisedCompletionDate && $scope.jobDates.plannedEndDate != null) {
					$scope.job.eotAwarded = moment($scope.jobDates.revisedCompletionDate, 'YYYY-MM-DD').diff($scope.jobDates.plannedEndDate, 'd');
				} else {
					$scope.job.eotAwarded = 0;
				}
				break;
			case 'eot':
				if($scope.jobDates.plannedEndDate) {
					$scope.jobDates.revisedCompletionDate = moment($scope.jobDates.plannedEndDate, 'YYYY-MM-DD').add($scope.job.eotAwarded, 'd');
				} else {
					$scope.jobDates.revisedCompletionDate = null;
				}
				break;
			case 'complete':
				if($scope.jobDates.revisedCompletionDate){
					$scope.jobDates.revisedCompletionDate = moment($scope.jobDates.plannedEndDate, 'YYYY-MM-DD').subtract($scope.job.eotAwarded,'d');
				} else {
					$scope.job.eotAwarded = 0;
				}
				break;
			}
		}, 300);
		else $scope.loaded = true;
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
		
		if($scope.job.innovationApplicable == true)
			$scope.job.innovationApplicable = "1";
		else{
			$scope.job.innovationApplicable = "0";
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

