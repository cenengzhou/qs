mainApp.controller('PaymentDetailsCtrl', ['$scope' , '$http', '$stateParams', '$cookieStore', 'paymentService', 'modalService', 'roundUtil', 
                                          function($scope , $http, $stateParams, $cookieStore, paymentService, modalService, roundUtil) {
	loadData();
	$scope.disableButtons = true;

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
			             { field: 'movementAmount', enableCellEdit: false},
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
			if(rowEntity.lineType == "GR" || rowEntity.lineType == "GP" || rowEntity.lineType == "RT" || rowEntity.lineType == "MR"){
				rowEntity.cumAmount  = roundUtil.round(newValue, 2);
				rowEntity.movementAmount = roundUtil.round(rowEntity.cumAmount - rowEntity.postedAmount, 2);
			}
			if(colDef.name == "cumAmount"){
				rowEntity.cumAmount  = roundUtil.round(newValue, 2);
				rowEntity.movementAmount = roundUtil.round(rowEntity.cumAmount - rowEntity.postedAmount, 2);
			}
		});

	}
	

	function loadData() {
		if($cookieStore.get('paymentCertNo') != ""){
			getPaymentCert();
			loadPaymentDetails();
		}else
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please create a payment certificate.");
	}
	
	function getPaymentCert() {
		paymentService.getPaymentCert($scope.jobNo, $scope.subcontractNo, $cookieStore.get('paymentCertNo'))
		.then(
				function( data ) {
					var payment = data;
					$scope.payment = data;

					if($scope.payment.paymentStatus == "PND")
						$scope.disableButtons = false;

				});
	}

	function loadPaymentDetails() {
		paymentService.getPaymentDetailList($scope.jobNo, $scope.subcontractNo, $cookieStore.get('paymentCertNo'))
		.then(
				function( data ) {
					angular.forEach(data, function(value, key){
						value.postedAmount = value.movementAmount+value.cumAmount;
					});
					
					$scope.gridOptions.data = data;
					
					$scope.gridApi.grid.refresh();
				});
	}

}]);