mainApp.controller('JobVariationKpiAddModalCtrl', ['$scope','variationKpiService', '$uibModalInstance', '$cookies', 'modalService', '$sce','$state', 'GlobalParameter', 'rootscopeService', '$timeout',
                                   function($scope, variationKpiService, $uibModalInstance, $cookies, modalService, $sce,$state, GlobalParameter, rootscopeService, $timeout) {
	$scope.GlobalParameter = GlobalParameter;

	$scope.jobNo = $cookies.get("jobNo");
	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
	};

	$scope.kpi = {
			jobNo: $scope.jobNo,
			period: moment().format('YYYY-MM'),
			numberIssued:0,
			amountIssued:0.00,
			numberSubmitted:0,
			amountSubmitted: 0.00,
			numberAssessed: 0,
			amountAssessed: 0.00,
			numberApplied: 0,
			amountApplied: 0.00,
			numberCertified: 0,
			amountCertified: 0.00,
			remarks: ""
			};
	

	$scope.fields = [
		{
			type: 'Issued', 
			order: 1, 
			description: 'Issued', 
			numberField: 'numberIssued',
			amountField: 'amountIssued',
			numberAlt: 'Number of Variations issued by the Client \\ Client\'s Rep. (Includes requests for Variations)', 
			amountAlt: 'Value of Variations issued by the Client \\ Client\'s Rep based on GCL anticpated Final Account Submission Value'
		},
		{
			type: 'Submitted', 
			order: 2, 
			description: 'Submitted', 
			numberField: 'numberSubmitted',
			amountField: 'amountSubmitted',
			numberAlt: 'Number of Variations were a GCL have submitted their assessment to the Client \\ Client\'s Rep', 
			amountAlt: 'Value of GCL Variations submissions'
		},
		{
			type: 'Assessed', 
			order: 1, 
			description: 'Assessed', 
			numberField: 'numberAssessed',
			amountField: 'amountAssessed',
			numberAlt: 'Number of Variations were GCL have received the Clients \\ Client\'s Rep assessment', 
			amountAlt: 'Value of Client\'s Rep Variation Assessments'
		},
		{
			type: 'Applied', 
			order: 1, 
			description: 'Applied', 
			numberField: 'numberApplied',
			amountField: 'amountApplied',
			numberAlt: 'Number of Variations were we have included an applied amount within our Application', 
			amountAlt: 'Value of Variations within GCL Interim Application'
		},
		{
			type: 'Certified', 
			order: 1, 
			description: 'Certified', 
			numberField: 'numberCertified',
			amountField: 'amountCertified',
			numberAlt: 'Number of variations were the client has included a certification', 
			amountAlt: 'Value of Variations certified by Client \\ Client\'s Representative'
		},
		
	]
	
	$scope.onAdd = function() {
		if($scope.addForm.$valid){
			var period = moment($scope.kpi.period);
			$scope.kpi.year = period.year();
			$scope.kpi.month = period.month() + 1;
			variationKpiService.saveByJobNo($scope.jobNo, $scope.kpi)
			.then(function(data) {
				if(data) {
					$scope.getPage();
					$scope.cancel();
				}
			}, function(error){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', 'Record for Month Ending ' + period.format('YYYY-MM') + ' already exists');
			});
		}
	}
	
}]);
