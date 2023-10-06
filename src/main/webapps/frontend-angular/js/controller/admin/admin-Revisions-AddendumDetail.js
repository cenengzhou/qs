mainApp.controller('AdminRevisionsAddendumDetailCtrl', ['$scope', 'modalService', 'GlobalHelper', 'GlobalParameter', 'addendumService', 'rootscopeService',
										function($scope, modalService, GlobalHelper, GlobalParameter, addendumService, rootscopeService) {
	$scope.onSubmitAddendumDetailSearch = onSubmitAddendumDetailSearch;

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
	             { field: 'description', width: '240', displayName: "Description", enableCellEdit: $scope.canEdit },
	             { field: 'remarks', width: '360', displayName: "Remark", enableCellEdit: $scope.canEdit },
	             { field: 'codeObject', width: '120', displayName: "Object Code", enableCellEdit: $scope.canEdit },
	             { field: 'codeSubsidiary', width: '120', displayName: "Subsidiary Code", enableCellEdit: $scope.canEdit },
	             { field: 'codeObjectForDaywork', width: '120', displayName: "Object Code for day work", enableCellEdit: $scope.canEdit },
	             { field: 'bpi', width: '120', displayName: "bpi", enableCellEdit: $scope.canEdit },
	             { field: 'unit', width: '120', displayName: "Unit", enableCellEdit: $scope.canEdit },
	             { field: 'typeVo', width: '120', displayName: "typeVo", enableCellEdit: $scope.canEdit },
	             { field: 'idResourceSummary', width: '120', displayName: "Resource summary", enableCellEdit: $scope.canEdit },
	             { field: 'idHeaderRef', width: '120', displayName: "Header Reference", enableCellEdit: $scope.canEdit },
	             { field: 'noSubcontractChargedRef', width: '120', displayName: "Subcontract charged ref", enableCellEdit: $scope.canEdit },
	             { field: 'rateAddendum', width: '120', displayName: "Addendum Rate", enableCellEdit: $scope.canEdit,
	            	 cellFilter: 'number:4',
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 return GlobalHelper.numberClass(row.entity.rateAddendum);
	            	 }
	             },
	             { field: 'rateBudget', width: '120', displayName: "Budget Rate", enableCellEdit: $scope.canEdit,
	            	 cellFilter: 'number:4',
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 return GlobalHelper.numberClass(row.entity.rateBudget);
	            	 }
	             },
	             { field: 'quantity', width: '120', displayName: "Quantity", enableCellEdit: $scope.canEdit,
	            	 cellFilter: 'number:4',
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 return GlobalHelper.numberClass(row.entity.quantity);
	            	 }
	             },
	             { field: 'amtAddendum', width: '120', displayName: "Addendum Amount", enableCellEdit: $scope.canEdit,
	            	 cellFilter: 'number:4',
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 return GlobalHelper.numberClass(row.entity.amtAddendum);
	            	 }
	             },
	             { field: 'amtBudget', width: '120', displayName: "Budget Amount", enableCellEdit: $scope.canEdit,
	            	 cellFilter: 'number:4',
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 return GlobalHelper.numberClass(row.entity.amtBudget);
	            	 }
	             },
	             { field: 'dateLastModified', width: '120', displayName: "Last Modify Date", enableCellEdit: false },
	             { field: 'usernameLastModified', width: '120', displayName: "Last Modify User", enableCellEdit: false },
	             { field: 'dateCreated', width: '120', displayName: "Date Create", enableCellEdit: false },
	             { field: 'usernameCreated', width: '120', displayName: "User Create", enableCellEdit: false },
	             { field: 'typeRecoverable', width: '120', displayName: "Recoverable", enableCellEdit: $scope.canEdit,
									editableCellTemplate : 'ui-grid/dropdownEditor',
									editDropdownOptionsArray : GlobalParameter.recoverableOptions,
									cellFilter : 'dropdownFilter:"recoverableOptions"',
									editDropdownIdLabel : 'id',
									editDropdownValueLabel : 'value'
	             }
			]
	}
	
	$scope.updateGrid = function(){
		var gridRows = $scope.gridApi.rowEdit.getDirtyRows();
		var dataRows = gridRows.map( function( gridRow ) { return gridRow.entity; });
		if(dataRows.length > 0){
			addendumService.updateAddendumDetailListAdmin(dataRows)
			.then(function(data){
				$scope.gridApi.rowEdit.setRowsClean(dataRows);
				if(data == ''){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', 'Addendum Detail updated');
				} else {
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'Addendum Detail update fail');
				}
			});
		} else {
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'No Addendum Detail modified');
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
	
	function onSubmitAddendumDetailSearch(){
		addendumService.getAllAddendumDetails($scope.addendumDetailSearch.jobNo, $scope.addendumDetailSearch.subcontractNo, $scope.addendumDetailSearch.addendumNo)
		.then(function(data){
			$scope.gridOptions.data = data;
			$scope.addendumDetailList = data;
		})
	}
	
}]);
