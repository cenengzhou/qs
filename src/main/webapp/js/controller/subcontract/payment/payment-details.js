mainApp.controller('PaymentDetailsCtrl', ['$scope' , '$http', '$stateParams', '$cookieStore', 'paymentService', 
                                          function($scope , $http, $stateParams, $cookieStore, paymentService) {
	$scope.disableButtons = true;
	loadData();

	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			//enableFullRowSelection: true,
			multiSelect: true,
			enableCellEditOnFocus : true,
			showGridFooter : false,
			//showColumnFooter : true,
			exporterMenuPdf: false,

			columnDefs: [
			             { field: 'lineType', enableCellEdit: false},
			             { field: 'billItem', enableCellEdit: false},
			             { field: 'movementAmount', cellClass: "grid-theme-blue"},
			             { field: 'cumAmount', displayName: "Cumulative Certified Amount", cellClass: "grid-theme-blue"},
			             { field: 'postedAmount', displayName: "Posted Certified Amount", enableCellEdit: false},
			             { field: 'description', enableCellEdit: false},
			             { field: 'scSeqNo', displayName: "Sequence No", enableCellEdit: false},
			             { field: 'objectCode', enableCellEdit: false},
			             {field: 'subsidiaryCode', enableCellEdit: false}
			             ]
	};

	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;

		gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {

			//Alert to show what info about the edit is available
			//alert('Column: ' + colDef.name + ' feedbackRate: ' + rowEntity.feedbackRate);
			//rowEntity.feedbackRateHK = rowEntity.feedbackRate * $scope.exchangeRate;
		});

	}
	
	function loadData() {
		getPaymentCert();
		loadPaymentDetails();
	}
	
	function getPaymentCert() {
		if($cookieStore.get('paymentCertNo') != ""){
			paymentService.getPaymentCert($scope.jobNo, $scope.subcontractNo, $cookieStore.get('paymentCertNo'))
			.then(
					function( data ) {
						$scope.payment = data;
						
						if($scope.payment.paymentStatus == "PND")
							$scope.disableButtons = false;
						
					});
		}
	}
	
	function loadPaymentDetails() {
		if($cookieStore.get('paymentCertNo') != ""){
		paymentService.getPaymentDetailList($scope.jobNo, $scope.subcontractNo, $cookieStore.get('paymentCertNo'))
		.then(
				function( data ) {
					$scope.gridOptions.data = data;
					$scope.gridApi.grid.refresh();
				});
		}
	}

}]);