mainApp.controller('AdminSubcontractStandardTermsMaintenanceCtrl', 
		['$scope', '$rootScope', '$http', 'modalService', 'blockUI', 'subcontractService', 'GlobalParameter',
		 function($scope, $rootScope, $http, modalService, blockUI, subcontractService, GlobalParameter ) {
	$scope.blockStandardTerms = blockUI.instances.get('blockStandardTerms');
	$scope.loadData = function(){
//		$scope.blockStandardTerms.start('Loading...');
		subcontractService.searchSystemConstants().then(
			function(data) {
//				$scope.blockStandardTerms.stop();
				$scope.gridOptions.data = data;
			});
	};
	$scope.loadData();

	$scope.gridOptions = {
		enableFiltering : true,
		enableCellEditOnFocus : true,
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
		enableCellEditOnFocus : true,
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
			displayName : 'SC Payment Term',
			editableCellTemplate : 'ui-grid/dropdownEditor',
			cellFilter: 'dropdownFilter:"paymentTerms":true',
			cellClass : 'text-primary',
			editDropdownOptionsArray : GlobalParameter.getIdPlusValue(GlobalParameter.paymentTerms),
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
			editDropdownOptionsArray : GlobalParameter.retentionTerms,
			cellFilter : 'dropdownFilter:"retentionTerms"',
			editDropdownIdLabel : 'id',
			editDropdownValueLabel : 'value',
			enableCellEdit : true
		}, {
			field : 'finQS0Review',
			displayName : "Reviewed by Finance",
			editableCellTemplate : 'ui-grid/dropdownEditor',
			cellClass : 'text-primary',
			editDropdownOptionsArray : GlobalParameter.finQS0Review,
			cellFilter : 'dropdownFilter:"finQS0Review"',
			enableCellEdit : true
		} ]
	};
	
	$scope.afterCellEdit = function(rowEntity, colDef, newValue, oldValue) {
		if(newValue !== oldValue){
			$scope.gridApi.rowEdit.setRowsDirty( [rowEntity]);
			$scope.gridDirtyRows = $scope.gridApi.rowEdit.getDirtyRows($scope.gridApi);
		}
	};

	$scope.rowSelectionChanged = function(row) {
		 $scope.selectedRows = $scope.gridApi.selection.getSelectedRows();
	};
	$scope.rowSelectionChangedBatch = function(rows) {
		$scope.selectedRows = $scope.gridApi.selection.getSelectedRows();
	};

	$scope.onUpdate = function() {
		var dataRows = $scope.gridDirtyRows.map(function(gridRow) {
			return gridRow.entity;
		});
		subcontractService.updateMultipleSystemConstants(dataRows)
		.then(function(data){
			$scope.gridApi.rowEdit.setRowsClean(dataRows);
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Updated " + dataRows.length + " records");;
		}, function(data){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data );
		});
		
	};

	$scope.onDelete = function() {
		$scope.selectedRows = $scope.gridApi.selection.getSelectedRows();
		subcontractService.inactivateSystemConstant($scope.selectedRows)
		.then(function(data){
			angular.forEach($scope.selectedRows, function (data, index) {
			    $scope.gridOptions.data.splice($scope.gridOptions.data.lastIndexOf(data), 1);
			  });
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Deleted " + $scope.selectedRows.length + " records");;
			$scope.selectedRows = [];
		}, function(data){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data );
		});
	};
	
	$scope.onAdd = function(){
		modalService.open('md', 'view/admin/modal/admin-SubcontractStandardTermsAddModal.html', 'AdminSubcontractStandardTermsAddModalCtrl', '', $scope.gridOptions.data);
	};
	
	$scope.gridOptions.onRegisterApi = function(gridApi) {
		$scope.gridApi = gridApi;
		$scope.gridApi.edit.on.afterCellEdit($scope, $scope.afterCellEdit);
		$scope.gridApi.selection.on.rowSelectionChanged($scope, $scope.rowSelectionChanged);
		$scope.gridApi.selection.on.rowSelectionChangedBatch($scope, $scope.rowSelectionChangedBatch);
	};

}]);
