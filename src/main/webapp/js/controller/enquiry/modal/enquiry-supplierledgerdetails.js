mainApp.controller('EnquirySupplierLedgerDetailsCtrl', 
			['$scope', '$q', 'modalStatus', 'modalParam', '$uibModalInstance', 'jobcostService', 'masterListService', 'uiGridConstants', 'GlobalHelper', 'GlobalParameter',
    function($scope, $q, modalStatus, modalParam, $uibModalInstance, jobcostService, masterListService, uiGridConstants, GlobalHelper, GlobalParameter){
	$scope.status = modalStatus;
	$scope.parentScope = modalParam;
	$scope.cancel = function () {
		$uibModalInstance.close();
	};
	$scope.GlobalParameter = GlobalParameter;
	$scope.entity = $scope.parentScope.searchEntity;
	$scope.paymentDates = {}
	$scope.loadSupplierLedgerList = function(){
		if($scope.entity){
			loadPaymentStatus();
		} else {
			searchPaymentStatus ()
			.then(function(response){
				if(response.paymentStatus){
					$scope.entity = response.paymentStatus;
					loadPaymentStatus();
				}
			})
		}
	}
	
	$scope.loadSupplierLedgerList();
	
	function loadPaymentStatus(){
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
	function searchPaymentStatus (){
		var deferral = $q.defer();
		var paymentNo = '0000' + $scope.parentScope.searchPaymentCert.paymentCertNo;
		$scope.searchJobNo = $scope.parentScope.jobNo;
		$scope.searchSubcontractNo = $scope.parentScope.subcontractNo;
		$scope.searchInvoiceNo = $scope.parentScope.jobNo + '/' + $scope.parentScope.subcontractNo + '/' + paymentNo.substring(paymentNo.length - 4);
		jobcostService.obtainAPRecordList(
				$scope.searchJobNo, 
				$scope.searchInvoiceNo, 
				$scope.searchSupplierNo, 
				$scope.searchDocumentNo, 
				$scope.searchDocumentType, 
				$scope.searchSubcontractNo, 
				null)
				.then(function(data){
					if(angular.isObject(data)){
						deferral.resolve({
							paymentStatus : data[0]
						});
					}
				});
		return deferral.promise;
	}

}]);