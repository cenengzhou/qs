mainApp.controller('JobVariationKpiAddModalCtrl', ['$scope','variationKpiService', '$uibModalInstance', '$cookies', 'modalService', '$sce','$state', 'GlobalParameter', 'rootscopeService', '$timeout',
                                   function($scope, variationKpiService, $uibModalInstance, $cookies, modalService, $sce,$state, GlobalParameter, rootscopeService, $timeout) {
	$scope.GlobalParameter = GlobalParameter;

	$scope.jobNo = $cookies.get("jobNo");
	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
	};

	$scope.showMonthColumn = false;
	$scope.kpiList = [{
			jobNo: $scope.jobNo,
			period: moment().format('YYYY-MM'),
			numberIssued:0,
			amountIssued:0,
			numberSubmitted:0,
			amountSubmitted: 0,
			numberAssessed: 0,
			amountAssessed: 0,
			numberApplied: 0,
			amountApplied: 0,
			numberCertified: 0,
			amountCertified: 0,
			numberAgreed: 0,
			amountAgreed: 0,
			remarks: "",
			eojSecured: 0,
			eojUnsecured: 0,
			eojTotal: 0
			}];
	

	$scope.onAdd = function() {
		var period = moment($scope.kpiList[0].period);
		$scope.kpiList[0].year = period.year();
		$scope.kpiList[0].month = period.month() + 1;
		variationKpiService.saveByJobNo($scope.jobNo, $scope.kpiList[0])
		.then(function(data) {
			if(data) {
				$scope.getPage($scope.kpiList[0].year);
				$scope.cancel();
			}
		}, function(error){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', 'Record for Month Ending ' + period.format('YYYY-MM') + ' already exists');
		});
	}
	
}]);
