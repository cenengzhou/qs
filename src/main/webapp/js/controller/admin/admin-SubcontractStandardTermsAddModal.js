mainApp.controller('AdminSubcontractStandardTermsAddModalCtrl', 
		function($scope, $rootScope, $uibModalInstance, $http, modalStatus, modalParam, modalService) {
	
	$scope.dataList = modalParam;
	$scope.newRecord = {};
	$scope.systemCodeList = ['59', '60', '61'];
	$scope.companyList = [];
	$scope.scPaymentTermList = ['QS0', 'QS1', 'QS2', 'QS3', 'QS4', 'QS5', 'QS6', 'QS7'];
	$scope.retentionTypeList = ['Lump Sum Amount Retention', 'Percentage - Original SC Sum', 'Percentage - Revised SC Sum'];
	$scope.finQS0ReviewList = ['N', 'Y'];
	angular.forEach($scope.dataList, function(data){
		uniquePush($scope.systemCodeList, data.systemCode);
		uniquePush($scope.companyList, data.company);
		uniquePush($scope.scPaymentTermList, data.scPaymentTerm);
		uniquePush($scope.retentionTypeList, data.retentionType);
		uniquePush($scope.finQS0ReviewList, data.finQS0Review);
	});
	
	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
	};
	
	$scope.onSubmit = function(){		
		$http.post('service/subcontract/CreateSystemConstant', $scope.newRecord)
		.then(function(response){
			var newObj = {};
			angular.copy($scope.newRecord, newObj);
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Create succeeded");
			$scope.dataList.push(newObj);
		}, function(response){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "A SystemConstant with the same systemCode and company already exists" );
		});

	};
	
	function uniquePush(list, item){
		if(list.indexOf(item) == -1){
			list.push(item);
		}
	};
});
