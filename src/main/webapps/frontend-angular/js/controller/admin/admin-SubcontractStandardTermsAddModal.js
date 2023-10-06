mainApp.controller('AdminSubcontractStandardTermsAddModalCtrl', 
		['$scope', '$uibModalInstance', '$http', 'modalStatus', 'modalParam', 'modalService', 'systemService', 'jobService', '$state',
		function($scope, $uibModalInstance, $http, modalStatus, modalParam, modalService, systemService, jobService, $state) {
	
	$scope.dataList = modalParam;
	$scope.newRecord = {};
	$scope.formOfSubcontractList = ['Major', 'Minor', 'Consultancy Agreement', 'Internal Trading'];
	$scope.companyList = ['00000'];
	$scope.scPaymentTermList = ['QS0', 'QS1', 'QS2', 'QS3', 'QS4', 'QS5', 'QS6', 'QS7', 'QS8'];
	$scope.retentionTypeList = ['Lump Sum Amount Retention', 'Percentage - Original SC Sum', 'Percentage - Revised SC Sum', 'No Retention'];
	getCompanyList();

	//$scope.finQS0ReviewList = ['N', 'Y'];
	/*angular.forEach($scope.dataList, function(data){
		uniquePush($scope.formOfSubcontractList, data.systemCode);
		uniquePush($scope.companyList, data.company);
		uniquePush($scope.scPaymentTermList, data.scPaymentTerm);
		uniquePush($scope.retentionTypeList, data.retentionType);
		//uniquePush($scope.finQS0ReviewList, data.finQS0Review);
	});*/
	
	
	
	$scope.cancel = function () {
		$uibModalInstance.close();
		$state.reload();
	};
	
	$scope.onSubmit = function(){		
		systemService.createSystemConstant($scope.newRecord)
		.then(function(data){
			var newObj = {};
			angular.copy($scope.newRecord, newObj);
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Create succeeded");
			$scope.dataList.push(newObj);
		}, function(data){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "A Subcontract Standard Terms with the same company and form of subcontract already exists." );
		});

	};
	
	function getCompanyList(){
		jobService.obtainAllJobCompany()
			.then(function(data){
				angular.forEach(data, function(company){
					uniquePush($scope.companyList, company);
				});
		});
	}
	
	function uniquePush(list, item){
		if(list.indexOf(item) == -1){
			list.push(item);
		}
	};
}]);
