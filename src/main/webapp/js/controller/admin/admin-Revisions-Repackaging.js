mainApp.controller('AdminRevisionsRepackagingCtrl', ['$scope', 'resourceSummaryService', 'modalService', 'GlobalHelper', 'GlobalParameter', 'rootscopeService',
										function($scope, resourceSummaryService, modalService, GlobalHelper, GlobalParameter, rootscopeService){
	$scope.onSubmitResourceSummarySearch = onSubmitResourceSummarySearch;
	$scope.onUpdateResourceSummaryRecord = onUpdateResourceSummaryRecord;
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
			             { field: 'id', width: '60', displayName: "ID", enableCellEdit: false },
			             { field: 'packageNo', width: '100', displayName: "Subcontract No.", enableCellEdit: true, cellEditableCondition : $scope.canEdit },
			             { field: 'resourceDescription', width: '200', displayName: "Description", enableCellEdit: true, cellEditableCondition : $scope.canEdit},
			             { field: 'objectCode', width: '100', displayName: "Object Code", enableCellEdit: true, cellEditableCondition : $scope.canEdit },
			             { field: 'subsidiaryCode', width: '100', displayName: "Subsidiary Code", enableCellEdit: true, cellEditableCondition : $scope.canEdit },
			             { field: 'amountBudget', width: '100', displayName: "Budget", enableCellEdit: true, cellEditableCondition : $scope.canEdit,
			            	 cellFilter: 'number:2',
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.quantity);
			            	 },  	            	 
			             },
			             { field: 'quantity', width: '100', displayName: "Quantity", enableCellEdit: true, cellEditableCondition : $scope.canEdit,
			            	 cellFilter: 'number:4',
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.quantity);
			            	 },  
			             },
			             { field: 'currIVAmount', width: '100', displayName: "Current Amount", enableCellEdit: true, cellEditableCondition : $scope.canEdit,
			            	 cellFilter: 'number:2',
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.currIVAmount);
			            	 },  
			             },
			             { field: 'postedIVAmount', width: '100', displayName: "Posted Amount", enableCellEdit: true, cellEditableCondition : $scope.canEdit,
			            	 cellFilter: 'number:2',
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.postedIVAmount);
			            	 },  
			             },
			             { field: 'rate', width: '100', displayName: "Rate", enableCellEdit: true, cellEditableCondition : $scope.canEdit,
			            	 cellFilter: 'number:2',
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.rate);
			            	 },  
			             },
			             { field: 'unit', width: '100', displayName: "Unit", enableCellEdit: true, cellEditableCondition : $scope.canEdit },
			             { field: 'excludeDefect', width: '100', displayName: "Exclude Defect", enableCellEdit: true, cellEditableCondition : $scope.canEdit,
			            	 editableCellTemplate : 'ui-grid/dropdownEditor',
			            	 editDropdownOptionsArray : GlobalParameter.booleanOptions,
			     			cellFilter : 'dropdownFilter:"booleanOptions"',
			     			editDropdownIdLabel : 'id',
			     			editDropdownValueLabel : 'value',
			             },
			             { field: 'excludeLevy', width: '100', displayName: "Exclude Levy", enableCellEdit: true, cellEditableCondition : $scope.canEdit,
			            	 editableCellTemplate : 'ui-grid/dropdownEditor',
			            	 editDropdownOptionsArray : GlobalParameter.booleanOptions,
				     			cellFilter : 'dropdownFilter:"booleanOptions"',
				     			editDropdownIdLabel : 'id',
				     			editDropdownValueLabel : 'value',
			             },
			             { field: 'forIvRollbackOnly', width: '100', displayName: "For IV Rollback Only", enableCellEdit: true, cellEditableCondition : $scope.canEdit,
			            	 editableCellTemplate : 'ui-grid/dropdownEditor',
			            	 editDropdownOptionsArray : GlobalParameter.booleanOptions,
				     			cellFilter : 'dropdownFilter:"booleanOptions"',
				     			editDropdownIdLabel : 'id',
				     			editDropdownValueLabel : 'value',
			             },
			             { field: 'finalized', width: '100', displayName: "Finalized", enableCellEdit: true, cellEditableCondition : $scope.canEdit },
			             { field: 'resourceType', width: '100', displayName: "Type", enableCellEdit: true, cellEditableCondition : $scope.canEdit },
			             ]
	}
	
	$scope.updateGrid = function(){
		var gridRows = $scope.gridApi.rowEdit.getDirtyRows();
		var dataRows = gridRows.map( function( gridRow ) { return gridRow.entity; });
		if(dataRows.length > 0){
			resourceSummaryService.updateResourceSummariesForAdmin($scope.searchJobNo, dataRows)
			.then(function(data){
				$scope.gridApi.rowEdit.setRowsClean(dataRows);
				if(data == ''){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', 'Resource summary updated');
				} else{
					onSubmitResourceSummarySearch();
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
				}
			})
		} else {
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'No resource modified');
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

	$scope.fieldList = [
			{
				name: 'id',
				display: 'Resource Summary Id',
				order: 1,
				type: 'text',
				readOnly: true
			},
			{
				name: 'jobInfo',
				isObject: true,
				display: 'JobInfo Id',
				order: 2,
				type: 'number',
				readOnly: true
			},
			{
				name: 'systemStatus',
				order: 3,
				readOnly: true,
				type: 'boolean',
				options: $scope.systemStatuOptions
			},
			{
				name: 'packageNo',
				display: 'Subcontract No.',
				order: 4,
				type: 'text'
			},
			{
				name: 'subsidiaryCode',
				order: 5,
				type: 'text'
			},
			{
				name: 'objectCode',
				order: 6,
				type: 'text'
			},
			{
				name: 'resourceDescription',
				order: 7,
				type: 'text'
			},
			{
				name: 'unit',
				order: 8,
				type: 'text'
			},
			{
				name: 'quantity',
				order: 9,
				type: 'number'
			},
			{
				name: 'rate',
				order: 10,
				type: 'number'
			},
			{
				name: 'postedIVAmount',
				display: 'Posted IV Amount',
				order: 11,
				type: 'number'
			},
			{
				name: 'currIVAmount',
				display: 'Current IV Amount',
				order: 12,
				type: 'number'
			},
			{
				name: 'splitForm.id',
				display: 'Split From Id',
				isObject: true,
				order: 13,
				type: 'number',
				readOnly: 'readOnly'
			},
			{
				name: 'mergeTo.id',
				display: 'Merage To Id',
				isObject: true,
				order: 14,
				type: 'number',
				readOnly: 'readOnly'
			},
			{
				name: 'resourceType',
				order: 15,
				type: 'text'
			},
			{
				name: 'excludeLevy',
				order: 16,
				type: 'boolean',
				options: $scope.booleanOptions
			},
			{
				name: 'excludeDefect',
				order: 17,
				type: 'boolean',
				options: $scope.booleanOptions
			},
			{
				name: 'forIvRollbackOnly',
				display: 'For IV Rollback Only',
				order: 18,
				type: 'boolean',
				options: $scope.booleanOptions
			},
			{
				name: 'finalized',
				order: 19,
				type: 'text'
			},
			{
				name: 'amountBudget',
				order: 20,
				type: 'number'
			},
			{
				name: 'cumQuantity',
				display: 'Cumulative Quantity',
				order: 21,
				type: 'number'
			}
	];
	
	checkFieldList();
	
	function onSubmitResourceSummarySearch(){
		resourceSummaryService.obtainResourceSummariesByJobNumberForAdmin(
				$scope.resourceSummarySearch.jobNo)
		.then(function(data){console.log(data);
			$scope.gridOptions.data = data;
			if(data.length > 0) $scope.searchJobNo = data[0].jobInfo.jobNo;
			$scope.resourceSummaryList = data;
			$scope.resourceSummaryList.forEach(function(resourceSummary){
				$scope.fieldList.forEach(function(field){
					if(field.type == 'boolean' && typeof resourceSummary[field.name] == 'boolean'){
						resourceSummary[field.name] = resourceSummary[field.name] ? 'true' : 'false';
					}
				});
			});
		})
	}
	
	function onUpdateResourceSummaryRecord(resourceSummary){
		resourceSummaryService.updateResourceSummariesForAdmin(resourceSummary.jobInfo.jobNo, [resourceSummary])
		.then(function(data){
			if(data == ''){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', 'Resource summary updated');
			}
		})
	}
	
	function checkFieldList(){
		$scope.fieldList.forEach(function(field){
			if(!field.display) field.display = GlobalHelper.camelToNormalString(field.name);
			field.readOnly = field.readOnly ? true : false; 
		});
	}
	
}]);
