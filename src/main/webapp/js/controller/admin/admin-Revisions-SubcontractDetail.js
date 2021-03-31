mainApp.controller('AdminRevisionsSubcontractDetailCtrl', ['$scope', 'modalService', 'GlobalHelper', 'GlobalParameter', 'subcontractService', 'rootscopeService',
										function($scope, modalService, GlobalHelper, GlobalParameter, subcontractService, rootscopeService) {

	$scope.onSubmitSubcontractDetailSearch = onSubmitSubcontractDetailSearch;
	
	rootscopeService.gettingUser()
	.then(function (response){
		$scope.isEditable =  GlobalHelper.containRole('ROLE_QS_QS_ADM', response.user.UserRoles);
	});
	$scope.canEdit = function(){
		return $scope.isEditable;
	}
	
	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			enableFullRowSelection: false,
			multiSelect: true,
			showGridFooter : true,
			showColumnFooter: true,
			enableCellEditOnFocus : true,
			allowCellFocus: false,
			exporterMenuPdf: false,
			enableCellSelection: false,
			rowEditWaitInterval :-1,
			columnDefs: [
	             { field: 'id', width: '120', displayName: "ID", enableCellEdit: false },
	             { field: 'jobNo', width: '120', displayName: "Job Number", enableCellEdit: false },
	             { field: 'subcontract.packageNo', width: '120', displayName: "Subcontract", enableCellEdit: false },
	             { field: 'sequenceNo', width: '120', displayName: "Sequence Number", enableCellEdit: false },
	             { field: 'description', width: '240', displayName: "Description", enableCellEdit: $scope.canEdit },
	             { field: 'remark', width: '360', displayName: "Remark", enableCellEdit: $scope.canEdit },
	             { field: 'objectCode', width: '120', displayName: "Object Code", enableCellEdit: $scope.canEdit },
	             { field: 'subsidiaryCode', width: '120', displayName: "Subsidiary Code", enableCellEdit: $scope.canEdit },
	             { field: 'billItem', width: '120', displayName: "Bill Item", enableCellEdit: $scope.canEdit },
	             { field: 'unit', width: '120', displayName: "Unit", enableCellEdit: $scope.canEdit },
	             { field: 'lineType', width: '120', displayName: "Line Type", enableCellEdit: $scope.canEdit },
	             { field: 'approved', width: '120', displayName: "Approved", enableCellEdit: $scope.canEdit },
	             { field: 'resourceNo', width: '120', displayName: "Resource Number", enableCellEdit: $scope.canEdit },
	             { field: 'scRate', width: '120', displayName: "Subcontract Rate", enableCellEdit: $scope.canEdit,
	            	 cellFilter: 'number:4',
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 return GlobalHelper.numberClass(row.entity.scRate);
	            	 }
	             },
	             { field: 'quantity', width: '120', displayName: "Quantity", enableCellEdit: $scope.canEdit,
	            	 cellFilter: 'number:4',
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 return GlobalHelper.numberClass(row.entity.quantity);
	            	 }
	             },
	             { field: 'amountCumulativeCert', width: '120', displayName: "Cum Cert Amount", enableCellEdit: $scope.canEdit,
	            	 cellFilter: 'number:4',
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 return GlobalHelper.numberClass(row.entity.amountCumulativeCert);
	            	 }	            
	             },
	             { field: 'cumCertifiedQuantity', width: '120', displayName: "Cum Cert Qty", enableCellEdit: $scope.canEdit,
	            	 cellFilter: 'number:4',
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 return GlobalHelper.numberClass(row.entity.cumCertifiedQuantity);
	            	 }	                        	 
	             },
	             
	             { field: 'amountCumulativeWD', width: '120', displayName: "Cum WD Amount", enableCellEdit: $scope.canEdit,
	            	 cellEditableCondition: checkEditableForCumWD,
	            	 cellFilter: 'number:4',
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 return GlobalHelper.numberClass(row.entity.amountCumulativeWD);
	            	 }	            
	             },
	             { field: 'cumWorkDoneQuantity', width: '120', displayName: "Cum WD Qty", enableCellEdit: $scope.canEdit,
	            	 cellEditableCondition: checkEditableForCumWD,
	            	 cellFilter: 'number:4',
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 return GlobalHelper.numberClass(row.entity.cumWorkDoneQuantity);
	            	 }	                        	 
	             },

	             
	             { field: 'amountPostedCert', width: '120', displayName: "Posted Cert Amount", enableCellEdit: $scope.canEdit,
	            	 cellFilter: 'number:4',
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 return GlobalHelper.numberClass(row.entity.amountPostedCert);
	            	 }	                        	 
	             },
	             { field: 'postedCertifiedQuantity', width: '120', displayName: "Posted Cert Qty", enableCellEdit: $scope.canEdit,
	            	 cellFilter: 'number:4',
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 return GlobalHelper.numberClass(row.entity.postedCertifiedQuantity);
	            	 }	                        	 
	             },
	             { field: 'amountSubcontractNew', width: '120', displayName: "New Subcontract Amount", enableCellEdit: $scope.canEdit,
	            	 cellFilter: 'number:4',
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 return GlobalHelper.numberClass(row.entity.amountSubcontractNew);
	            	 }	                        	 
	             },
	             { field: 'newQuantity', width: '120', displayName: "New Qty", enableCellEdit: $scope.canEdit,
	            	 cellFilter: 'number:4',
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 return GlobalHelper.numberClass(row.entity.newQuantity);
	            	 }	                        	            	 
	             },
	             { field: 'originalQuantity', width: '120', displayName: "Original Qty", enableCellEdit: $scope.canEdit,
	            	 cellFilter: 'number:4',
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 return GlobalHelper.numberClass(row.entity.originalQuantity);
	            	 }
	             },
	             { field: 'toBeApprovedRate', width: '120', displayName: "To Be Approved Rate", enableCellEdit: $scope.canEdit,
	            	 cellFilter: 'number:4',
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 return GlobalHelper.numberClass(row.entity.toBeApprovedRate);
	            	 }
	             },
	             { field: 'amountSubcontract', width: '120', displayName: "Subcontract Amount", enableCellEdit: $scope.canEdit,
	            	 cellFilter: 'number:4',
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 return GlobalHelper.numberClass(row.entity.amountSubcontract);
	            	 }
	             },
	             { field: 'amountBudget', width: '120', displayName: "Budget Amount", enableCellEdit: $scope.canEdit,
	            	 cellFilter: 'number:4',
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 return GlobalHelper.numberClass(row.entity.amountBudget);
	            	 }
	             },
	             { field: 'lastModifiedDate', width: '120', displayName: "Last Modify Date", enableCellEdit: false },
	             { field: 'lastModifiedUser', width: '120', displayName: "Last Modify User", enableCellEdit: false },
	             { field: 'createdDate', width: '120', displayName: "Date Create", enableCellEdit: false },
	             { field: 'createdUser', width: '120', displayName: "User Create", enableCellEdit: false },
	             { field: 'systemStatus', width: '120', displayName: "System Status", enableCellEdit: $scope.canEdit,
					editableCellTemplate : 'ui-grid/dropdownEditor',
					editDropdownOptionsArray : GlobalParameter.systemStatuOptions,
					cellFilter : 'dropdownFilter:"systemStatuOptions"',
					editDropdownIdLabel : 'id',
					editDropdownValueLabel : 'value'
	             }
			]
	}
	
	function checkEditableForCumWD(scope){
		var allowEditLineTypeArray = [
									'B1', 'BD', 'BQ', //BQ
									'OA', //OA
									'BS', 'CF', 'D1', 'D2', 'HD', 'L1', 'L2', 'OA', 'V1', 'V2', 'V3' // VO
								];
		if(allowEditLineTypeArray.indexOf(scope.row.entity.lineType) >= 0){
			return true;
		} else {
			return false;
		}
	}
	
	$scope.updateGrid = function(){
		var gridRows = $scope.gridApi.rowEdit.getDirtyRows();
		var dataRows = gridRows.map( function( gridRow ) { return gridRow.entity; });
		if(dataRows.length > 0){
			subcontractService.updateSubcontractDetailListAdmin(dataRows)
			.then(function(data){
				$scope.gridApi.rowEdit.setRowsClean(dataRows);
				if(data == ''){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', 'Subcontract Detail updated');
				} 
			})
		} else {
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'No Subcontract Detail modified');
		}
	}
	
	$scope.gridOptions.onRegisterApi= function(gridApi){
		$scope.gridApi = gridApi;
		gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			if(newValue != oldValue){
				$scope.gridApi.rowEdit.setRowsDirty( [rowEntity]);
			}
		});
	}
	
	function onSubmitSubcontractDetailSearch(){
		subcontractService.getSCDetails($scope.subcontractDetailSearch.jobNo, $scope.subcontractDetailSearch.subcontractNo)
		.then(function(data){
			$scope.gridOptions.data = data;
			$scope.subcontractDetailList = data;
		})
	}
	
	function onUpdateSubcontractDetailRecord(resourceSummary){
		resourceSummaryService.updateResourceSummariesForAdmin(resourceSummary.jobInfo.jobNo, [resourceSummary])
		.then(function(data){
			if(data == ''){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', 'Resource summary updated');
			}
		})
	}
	
}]);
