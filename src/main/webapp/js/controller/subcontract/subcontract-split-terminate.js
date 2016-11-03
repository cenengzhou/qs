mainApp.controller('SubcontractSplitTerminateCtrl', ['$scope' , 'subcontractService', 'modalService', '$uibModal', 'confirmService', 'roundUtil', '$state', '$stateParams', 'GlobalParameter', 'paymentService',
                                            function($scope, subcontractService, modalService, $uibModal, confirmService, roundUtil, $state, $stateParams, GlobalParameter, paymentService) {
	$scope.remeasuredSubcontractSumAfterSplit = 0;
	$scope.approvedVOAmountAfterSplit = 0;
	
	
	$scope.action = $stateParams.action;
	
	if($scope.action == 'Split')
		$scope.canEdit = true;
	else
		$scope.canEdit = false;
	
	
	getSubcontract();
	getSCDetails();
	getSubcontractDetailTotalNewAmount();
	
	
	$scope.gridOptions = {
			enableSorting: true,
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableColumnMoving: false,
			//enableRowSelection: true,
			//enableFullRowSelection: true,
			//multiSelect: false,
			showGridFooter : false,
			showColumnFooter : false,
			//fastWatch : true,

			enableCellEditOnFocus : true,
			exporterMenuPdf: false,
			rowEditWaitInterval :-1,
			
	
			columnDefs: [{ field: 'id', width: 50, enableCellEdit: false, visible: false},
			             { field: 'lineType', width: 50, enableCellEdit: false},
			             { field: 'billItem', width: 100, enableCellEdit: false},
			             { field: 'description', width: 100, enableCellEdit: false},
     	 				{field: 'costRate', width: 80, enableCellEdit: false, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:4'},
         	 			{ field: 'scRate', displayName: "SC Rate", width: 80, enableCellEdit: false, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:4'},
 	 					{ field: 'quantity', width: 100, enableCellEdit: false, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:4'},
 	 					{field: 'amountBudget', displayName: "Budget Amount", width: 150, enableCellEdit: false, enableFiltering: false,
 	 						cellClass: 'text-right', cellFilter: 'number:2'}, 
 						{ field: 'amountSubcontract', displayName: "SC Amount", width: 150, enableCellEdit: false, enableFiltering: false,
 							cellClass: 'text-right', cellFilter: 'number:2'},
			            { field: 'newQuantity', displayName: "Budget Quantity", width: 150, enableFiltering: false, cellEditableCondition : $scope.canEdit, 
			            	 cellClass: function(){
			            		 if($scope.action == 'Split')
			            			return 'text-right blue'
			            		else
			            			return 'text-right'
			            	 }, 
			            	cellFilter: 'number:4'
			            },
			            { field: 'amountSubcontractNew', displayName: "New SC Amount", width: 150, enableFiltering: false, cellEditableCondition : $scope.canEdit,
		            			cellClass: function(){
				            		 if($scope.action == 'Split')
				            			return 'text-right blue'
				            		else
				            			return 'text-right'
				            	 }, 
			            		cellFilter: 'number:2'
			            },
			            { field: 'amountBudgetNew', displayName: "New Budget Amount", width: 150, enableFiltering: false, enableCellEdit: false,
            		 		cellClass: 'text-right', cellFilter: 'number:2'},
        		 		{ field: 'cumWorkDoneQuantity', displayName: "Cum. WD Quantity", width: 150, enableCellEdit: false, enableFiltering: false, 
            		 		cellClass: 'text-right', cellFilter: 'number:4'},
            		 	{ field: 'amountCumulativeWD', displayName: "Cum WD Amount", width: 150, enableCellEdit: false, enableFiltering: false, 
		            		 		cellClass: 'text-right', cellFilter: 'number:2'},
			             { field: 'amountCumulativeCert', displayName: "Cum Certified Amount", width: 150, enableCellEdit: false, enableFiltering: false,
	            		 			cellClass: 'text-right', cellFilter: 'number:2'},
	            			
			             {field: 'objectCode', width: 100, enableCellEdit: false},
			             {field: 'subsidiaryCode', width: 100, enableCellEdit: false},
			             
			             {field: 'unit', width: 60, enableCellEdit: false, enableFiltering: false},
			             {field: 'sequenceNo', width: 100, visible: false, enableCellEdit: false, enableFiltering: false},

			             {field: 'approved', width: 100, visible: false, enableCellEdit: false, enableFiltering: false},
			             {field: 'remark', width: 100, visible: false, enableCellEdit: false, enableFiltering: false}
			             ]

			
	};
	
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;
		
		
		gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			if(colDef.name == 'newQuantity'){
				if(newValue.length ==0){
					rowEntity.newQuantity = oldValue;
					return;
				}
				else{
					var newQuantityOldValue = oldValue;
					var amountSubcontractNewOldValue = rowEntity.amountSubcontractNew;

					rowEntity.newQuantity = roundUtil.round(newValue, 4);
					rowEntity.amountSubcontractNew = roundUtil.round(rowEntity.newQuantity * rowEntity.scRate, 2);
					rowEntity.amountBudgetNew = roundUtil.round(rowEntity.newQuantity * rowEntity.costRate, 2);
				}
			}else if(colDef.name == 'amountSubcontractNew'){
				if(newValue.length ==0){
					rowEntity.newQuantity = oldValue;
					return;
				}
				else{
					var newQuantityOldValue = rowEntity.newQuantity;
					var amountSubcontractNewOldValue = oldValue;

					rowEntity.amountSubcontractNew = roundUtil.round(newValue, 2);
					rowEntity.newQuantity = roundUtil.round(rowEntity.amountSubcontractNew / rowEntity.scRate, 4);
					rowEntity.amountBudgetNew = roundUtil.round(rowEntity.newQuantity * rowEntity.costRate, 2);
				}
			}
			
			
			if(rowEntity.amountSubcontractNew <  rowEntity.amountCumulativeWD || rowEntity.amountSubcontractNew < rowEntity.amountCumulativeCert){
				rowEntity.newQuantity = newQuantityOldValue;
				rowEntity.amountSubcontractNew = amountSubcontractNewOldValue;
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "New subcontract amount should not be smaller than cumulative work done / certified amount ");
				return;
			}else if (rowEntity.amountSubcontractNew > rowEntity.amountSubcontract){
				rowEntity.newQuantity = newQuantityOldValue;
				rowEntity.amountSubcontractNew = amountSubcontractNewOldValue;
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "New subcontract amount should not be larger than original subcontract amount. ");
				return;
			}
			
			if(rowEntity.lineType == 'BQ'){
				$scope.remeasuredSubcontractSumAfterSplit = $scope.remeasuredSubcontractSumAfterSplit - amountSubcontractNewOldValue + rowEntity.amountSubcontractNew;
			}else{
				$scope.approvedVOAmountAfterSplit = $scope.approvedVOAmountAfterSplit - amountSubcontractNewOldValue + rowEntity.amountSubcontractNew;
			}

		});

	}
	
	$scope.save = function() {
		$scope.disableButtons = true;
		var gridRows = $scope.gridApi.rowEdit.getDirtyRows();
		var dataRows = gridRows.map( function( gridRow ) { return gridRow.entity; });
		
		if(dataRows.length==0){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "No record has been modified");
			$scope.disableButtons = false;
			return;
		}
		
		
		updateSCDetailsNewQuantity(dataRows, false);
	}
	
	$scope.submitApproval = function (){
		$scope.disableButtons = true;
		
		if($scope.action == 'Split'){
			var gridRows = $scope.gridApi.rowEdit.getDirtyRows();
			var dataRows = gridRows.map( function( gridRow ) { return gridRow.entity; });
		}
		else{
			var dataRows = $scope.gridOptions.data;
		}

		var modalOptions = {
				bodyText: "Are you sure you want to submit "+$scope.action+" Subcontract Approval?"
		};

		confirmService.showModal({}, modalOptions).then(function (result) {
			if(result == "Yes"){
				if(dataRows.length!=0){
					updateSCDetailsNewQuantity(dataRows, true);
				}else{
					if($scope.action == 'Split')
						submitSplitTerminateSC('S');
					else
						submitSplitTerminateSC('T');
				}
			}else
				$scope.disableButtons = false;
			
		});
		
	}
	
	$scope.openAttachment = function(){
//		modalService.open('lg', 'view/subcontract/attachment/attachment-sc-file.html', 'AttachmentSCFileCtrl', 'Success', $scope);
		$scope.nameObject = GlobalParameter['AbstractAttachment'].SCPackageNameObject;
		$scope.uibModalInstance = $uibModal.open({
			animation: true,
			templateUrl: "view/subcontract/attachment/attachment-sc-file.html",
			controller: 'AttachmentSCFileCtrl',
			size: 'lg',
			backdrop: 'static',
			scope: $scope
		});
	}
	
	function getSCDetails() {
		subcontractService.getSubcontractDetailsWithBudget($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					$scope.gridOptions.data = data;
					if($scope.action == 'Terminate'){
						angular.forEach(data, function(value, key){
							value.newQuantity = value.cumWorkDoneQuantity;
							value.amountBudgetNew = roundUtil.round(value.newQuantity * value.costRate, 2);
							value.amountSubcontractNew = roundUtil.round(value.newQuantity * value.scRate, 2);

							if(value.lineType == 'BQ'){
								$scope.remeasuredSubcontractSumAfterSplit += value.amountSubcontractNew;
							}else{
								$scope.approvedVOAmountAfterSplit += value.amountSubcontractNew;
							}
							
						});
					}
				});
	}
	
	
	function getSubcontractDetailTotalNewAmount(){
		subcontractService.getSubcontractDetailTotalNewAmount($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					if(data.length > 0){
						if($scope.action == 'Split'){
							$scope.remeasuredSubcontractSumAfterSplit = data[0];
							$scope.approvedVOAmountAfterSplit = data[1];
						}
					}
				});
	}
	
	function updateSCDetailsNewQuantity(scDetailsToUpdate, submitApproval){
		subcontractService.updateSCDetailsNewQuantity(scDetailsToUpdate)
		.then(
				function( data ) {
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
						$scope.disableButtons = false;
					}else{
						if(submitApproval){
							if($scope.action == 'Split')
								submitSplitTerminateSC('S');
							else
								submitSplitTerminateSC('T');
						}else{
							modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Subcontract details have been updated.");
							$state.reload();
						}
					}
				});
	}
	
	
	function submitSplitTerminateSC(splitTerminateAction){
		//S: Split
		//T: Terminate
		subcontractService.submitSplitTerminateSC($scope.jobNo, $scope.subcontractNo, splitTerminateAction)
		.then(
				function( data ) {
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
						$scope.disableButtons = false;
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Subcontract Split Approval has been submitted.");
						$state.reload();
					}
				});
	}
	
	function getSubcontract(){
		subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					$scope.remeasuredSubcontractSum = data.remeasuredSubcontractSum;
					$scope.approvedVOAmount = data.approvedVOAmount;
					$scope.splitTerminateStatus = data.splitTerminateStatus;
					
					if(data.scStatus < 500 || data.paymentStatus == 'F' || data.splitTerminateStatus ==1  || data.splitTerminateStatus ==2 || data.splitTerminateStatus ==4|| data.submittedAddendum ==1)
						$scope.disableButtons = true;
					else
						$scope.disableButtons = false;
					
					getLatestPaymentCert();
				});
	}
	
	
	function getLatestPaymentCert() {
		paymentService.getLatestPaymentCert($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					if(data !=null && data.length >0){
						if(data.paymentStatus != "PND" && data.paymentStatus != "APR")
							$scope.disableButtons = true;
						
					}
				});
	}
	
}]);