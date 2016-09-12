mainApp.controller('EnquirySupplierLedgerDetailsCtrl', 
			['$scope', 'modalStatus', 'modalParam', '$uibModalInstance', 'jobcostService', 'masterListService', 'uiGridConstants', 'GlobalHelper', 'GlobalParameter',
    function($scope, modalStatus, modalParam, $uibModalInstance, jobcostService, masterListService, uiGridConstants, GlobalHelper, GlobalParameter){
	$scope.status = modalStatus;
	$scope.parentScope = modalParam;
	$scope.cancel = function () {
		$uibModalInstance.close();
	};
	$scope.GlobalParameter = GlobalParameter;
	$scope.entity = $scope.parentScope.searchEntity;
	$scope.paymentDates = {}
	$scope.loadSupplierLedgerList = function(){
		jobcostService.getAPPaymentHistories(
				$scope.entity.company, 
				$scope.entity.documentType, 
				$scope.entity.supplierNumber, 
				$scope.entity.documentNumber)
		.then(function(data){
			if(angular.isArray(data)){
				$scope.paymentDates = data;
			};
		});
		masterListService.getSubcontractorList($scope.entity.supplierNumber)
		.then(function(data){
			if(angular.isArray(data)){
				$scope.supplierName = data[0].vendorName;
			}
		})
	}
	
	$scope.loadSupplierLedgerList();

}]);