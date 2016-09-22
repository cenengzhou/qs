mainApp.controller('PaymentDetailsCtrl', ['$scope' , '$stateParams', '$cookies', 'paymentService', 'modalService', 'roundUtil', '$state', 'GlobalParameter',
                                          function($scope, $stateParams, $cookies, paymentService, modalService, roundUtil, $state, GlobalParameter) {
	loadData();
	$scope.disableButtons = true;
	$scope.GlobalParameter = GlobalParameter;
	 $scope.edit = true;
	 $scope.canEdit = function() { 
		 return $scope.edit; 
	};
	  
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
			
			rowEditWaitInterval :-1,

			columnDefs: [
			             { field: 'lineType', enableCellEdit: false},
			             { field: 'billItem', enableCellEdit: false},
			             { field: 'movementAmount',  enableFiltering: false, cellClass: 'text-right blue', cellFilter: 'number:2', 
			            	 cellEditableCondition : $scope.canEdit,
			            	 /* cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
						          if (grid.getCellValue(row,col) != 'GR') {
						            return 'blue';
						          }
						        }*/
			             },
			             { field: 'cumAmount', displayName: "Cumulative Certified Amount", enableFiltering: false, 
			            	 cellClass: 'blue text-right', cellFilter: 'number:2',
			            	 cellEditableCondition : $scope.canEdit},
			             { field: 'postedAmount', displayName: "Posted Certified Amount", enableCellEdit: false,cellClass: 'text-right', cellFilter: 'number:2'},
			             { field: 'description', enableCellEdit: false},
			             { field: 'scSeqNo', displayName: "Sequence No", enableCellEdit: false},
			             { field: 'objectCode', enableCellEdit: false},
			             {field: 'subsidiaryCode', enableCellEdit: false},
			             { field: 'subcontractDetail.id', enableCellEdit: false, visible: false},
			             ]
	};

	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;

		gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			if(rowEntity.lineType == "GR" || rowEntity.lineType == "GP" || rowEntity.lineType == "RT" || rowEntity.lineType == "MR"){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', rowEntity.lineType+" is generated by system.");
			}
		});
		
		gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			if(rowEntity.lineType != "GR" && rowEntity.lineType != "GP" && rowEntity.lineType != "RT" && rowEntity.lineType != "MR"){
				if(colDef.name == "cumAmount"){
					rowEntity.cumAmount  = roundUtil.round(newValue, 2);
					rowEntity.movementAmount = roundUtil.round(rowEntity.cumAmount - rowEntity.postedAmount, 2);
				}
				if(colDef.name == "movementAmount"){
					rowEntity.movementAmount  = roundUtil.round(newValue, 2);
					rowEntity.cumAmount = roundUtil.round(rowEntity.movementAmount + rowEntity.postedAmount, 2);
				}
			}
		});

	}
	
	$scope.update = function(){
		if($scope.payment.paymentStatus == "PND"){
			var gridRows = $scope.gridApi.rowEdit.getDirtyRows();
			var dataRows = gridRows.map( function( gridRow ) { return gridRow.entity; });

			if(dataRows.length==0){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "No record has been modified");
				return;
			}

			updatePaymentDetails($scope.gridOptions.data);
		}
	}
	

	function loadData() {
		if($cookies.get('paymentCertNo') != ""){
			getPaymentCert();
		}else
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please create a payment certificate.");
	}
	
	function getPaymentCert() {
		paymentService.getPaymentCert($scope.jobNo, $scope.subcontractNo, $cookies.get('paymentCertNo'))
		.then(
				function( data ) {
					var payment = data;
					$scope.payment = data;

					if($scope.payment.paymentStatus == "PND")
						$scope.disableButtons = false;

					getPaymentDetailList();
				});
	}

	function getPaymentDetailList() {
		paymentService.getPaymentDetailList($scope.jobNo, $scope.subcontractNo, $cookies.get('paymentCertNo'))
		.then(
				function( data ) {
					if($scope.payment.paymentStatus!="PND")
						$scope.edit = false;
					else
						$scope.edit = true;
					
					angular.forEach(data, function(value, key){
						value.postedAmount = value.cumAmount - value.movementAmount;
						
						/*if(value.lineType == "GR")
							$scope.edit = false;
						else
							$scope.edit = true;*/
					});
					
					$scope.gridOptions.data = data;
					
					$scope.gridApi.grid.refresh();
				});
	}
	


	function updatePaymentDetails(paymentDetails) {
		paymentService.updatePaymentDetails($scope.jobNo, $scope.subcontractNo, $cookies.get('paymentCertNo'), "I", paymentDetails)
		.then(
				function( data ) {
					if(data.length != 0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Payment Details have been updated.");
						$state.reload();
					}
				});
	}


}]);