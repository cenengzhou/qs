mainApp.controller('EnquiryCustomerLedgerDetailsCtrl', ['$scope', 'modalStatus', 'modalParam', '$uibModalInstance', 'mainCertService', 'uiGridConstants', 'GlobalHelper', 'GlobalParameter',
                                            function($scope, modalStatus, modalParam, $uibModalInstance, mainCertService, uiGridConstants, GlobalHelper, GlobalParameter){
	$scope.status = modalStatus;
	$scope.parentScope = modalParam;
	$scope.cancel = function () {
		$uibModalInstance.close();
	};
	$scope.GlobalParameter = GlobalParameter;
	$scope.entity = $scope.parentScope.searchEntity;
	$scope.receiveDates = {}
	$scope.loadCustomerLedgerList = function(){
		
		mainCertService.getMainCertReceiveDateAndAmount($scope.entity.company, $scope.entity.documentNumber)
		.then(function(data){
			if(angular.isArray(data)){
				$scope.receiveDates = data;
			};
		});
	}
	
	$scope.loadCustomerLedgerList();		
	
}]);