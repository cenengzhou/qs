mainApp.controller('AdminSubcontractStandardTermsMaintenanceCtrl', function(
		$scope, $rootScope, $http, modalService ) {
	$http.post("service/subcontract/SearchSystemConstants").then(
			function(response) {
				$scope.gridOptions.data = response.data;
			});

	$scope.gridOptions = {
		enableFiltering : true,
		enableColumnResizing : true,
		enableGridMenu : true,
		enableRowSelection : true,
		enableSelectAll : true,
		enableFullRowSelection : false,
		multiSelect : true,
		showGridFooter : true,
		enableCellEditOnFocus : false,
		allowCellFocus : false,
		enableCellSelection : false,
		enablePaginationControls : true,
		rowEditWaitInterval : -1,
		paginationPageSizes : [ 25, 50, 100, 150, 200 ],
		paginationPageSize : 25,
		columnDefs : [ {
			field : 'systemCode',
			displayName : "System Code",
			enableCellEdit : false
		}, {
			field : 'company',
			displayName : "Company",
			enableCellEdit : false
		}, {
			field : 'scPaymentTerm',
			displayName : "SC Payment Term",
			editableCellTemplate : 'ui-grid/dropdownEditor',
			cellClass : 'text-primary',
			editDropdownOptionsArray : [ {
				id : 'QS0',
				value : 'QS0 - Manual Input Due Date'
			}, {
				id : 'QS1',
				value : 'QS1 - Pay when Paid + 7 days'
			}, {
				id : 'QS2',
				value : 'QS2 - Pay when paid + 14 days'
			}, {
				id : 'QS3',
				value : 'QS3 - Pay when IPA Received + 56 days'
			}, {
				id : 'QS4',
				value : 'QS4 - Pay when Invoice Received + 28 days'
			}, {
				id : 'QS5',
				value : 'QS5 - Pay when Invoice Received + 30 days'
			}, {
				id : 'QS6',
				value : 'QS6 - Pay when Invoice Received + 45 days'
			}, {
				id : 'QS7',
				value : 'QS7 - Pay when Invoice Received + 60 days'
			} ],
			editDropdownIdLabel : 'id',
			editDropdownValueLabel : 'value',
			enableCellEdit : true
		}, {
			field : 'scMaxRetentionPercent',
			displayName : "SC Max Retention %",
			cellClass : 'text-primary text-right',
			cellFilter : 'number:2',
			type : 'number',
			enableCellEdit : true
		}, {
			field : 'scInterimRetentionPercent',
			displayName : "SC Interim Retention %",
			cellClass : 'text-primary text-right',
			type : 'number',
			cellFilter : 'number:2',
			enableCellEdit : true
		}, {
			field : 'scMOSRetentionPercent',
			displayName : "SC MOS Retention %",
			cellClass : 'text-primary text-right',
			cellFilter : 'number:2',
			type : 'number',
			enableCellEdit : true
		}, {
			field : 'retentionType',
			displayName : "Retention Type",
			editableCellTemplate : 'ui-grid/dropdownEditor',
			cellClass : 'text-primary',
			editDropdownOptionsArray : [ {
				id : 'Lump Sum Amount Retention',
				value : 'Lump Sum Amount Retention'
			}, {
				id : 'Percentage - Original SC Sum',
				value : 'Percentage - Original SC Sum'
			}, {
				id : 'Percentage - Revised SC Sum',
				value : 'Percentage - Revised SC Sum'
			} ],
			editDropdownIdLabel : 'id',
			editDropdownValueLabel : 'value',
			enableCellEdit : true
		}, {
			field : 'finQS0Review',
			displayName : "Reviewed by Finance",
			editableCellTemplate : 'ui-grid/dropdownEditor',
			cellClass : 'text-primary',
			editDropdownOptionsArray : [ {
				id : 'N',
				value : 'No'
			}, {
				id : 'Y',
				value : 'Yes'
			} ],
			enableCellEdit : true
		} ]
	};
	
	$scope.afterCellEdit = function(rowEntity, colDef) {
		$scope.gridApi.rowEdit.setRowsDirty( [rowEntity]);
		$scope.gridDirtyRows = $scope.gridApi.rowEdit.getDirtyRows($scope.gridApi);
		console.log($scope.gridDirtyRows);
	};

	$scope.rowSelectionChanged = function(row) {
		 $scope.selectedRows = $scope.gridApi.selection.getSelectedRows();
	};
	$scope.rowSelectionChangedBatch = function(rows) {
		var msg = 'rows changed ' + rows.length;
		console.log(msg);
	};

	$scope.onUpdate = function() {
		var dataRows = $scope.gridDirtyRows.map(function(gridRow) {
			return gridRow.entity;
		});
		$http.post('service/subcontract/UpdateMultipleSystemConstants', dataRows)
		.then(function(response){
			$scope.gridApi.rowEdit.setRowsClean(dataRows);
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Updated " + dataRows.length + " records");;
		}, function(response){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Status:" + response.statusText );
		});
		
	};

	$scope.onDelete = function() {
		$scope.selectedRows = $scope.gridApi.selection.getSelectedRows();
		$http.post('service/subcontract/InactivateSystemConstant', $scope.selectedRows)
		.then(function(response){
			angular.forEach($scope.selectedRows, function (data, index) {
			    $scope.gridOptions.data.splice($scope.gridOptions.data.lastIndexOf(data), 1);
			  });
			$scope.selectedRows = [];
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Deleted " + $scope.selectedRows.length + " records");;
		}, function(response){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Status:" + response.statusText );
		});
	};
	
	$scope.gridOptions.onRegisterApi = function(gridApi) {
		$scope.gridApi = gridApi;
		$scope.gridApi.edit.on.afterCellEdit($scope, $scope.afterCellEdit);
		$scope.gridApi.selection.on.rowSelectionChanged($scope, $scope.rowSelectionChanged);
		$scope.gridApi.selection.on.rowSelectionChangedBatch($scope, $scope.rowSelectionChangedBatch);
	};

});
