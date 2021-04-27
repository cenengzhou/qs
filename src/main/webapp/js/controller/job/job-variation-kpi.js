mainApp.controller('JobVariationKpiCtrl', ['$scope','variationKpiService', '$uibModal', '$cookies', 'modalService', '$sce','$state', 'GlobalParameter', 'rootscopeService', '$timeout',
                                   function($scope, variationKpiService, $uibModal, $cookies, modalService, $sce,$state, GlobalParameter, rootscopeService, $timeout) {
	$scope.GlobalParameter = GlobalParameter;

	$scope.jobNo = $cookies.get("jobNo");
	$scope.gridDirtyRows = [];
	$scope.gridSelectedRows = [];
	$scope.isSelected = 0;
	$scope.isDirty = 0;
	$scope.showMonthColumn = true;
	$scope.modified = false;
	var paginationOptions = {
		    pageNumber: 1,
		    pageSize: 10,
		    sort: null
		  };
	$scope.period = moment().format('YYYY-MM');
	$scope.kpiList = [];
	
	$scope.fields = [
		{type: 'job', description: 'Project', field: 'noJob', order: 1},
		{type: 'month', description: 'Month Ending', field: 'month', order: 2},
		{type: 'number', description: 'Number of Issued', field: 'numberIssued', order: 3, alt: 'Number of Variations issued by the Client \\ Client\'s Rep. (Includes requests for Variations)'},
		{type: 'amount', description: 'Amount of Issued', field: 'amountIssued', order: 4, alt: 'Value of Variations issued by the Client \\ Client\'s Rep based on GCL anticpated Final Account Submission Value'},
		{type: 'number', description: 'Number of Submitted', field: 'numberSubmitted', order: 5, alt: 'Number of Variations were a GCL have submitted their assessment to the Client \\ Client\'s Rep'},
		{type: 'amount', description: 'Amount of Submitted', field: 'amountSubmitted', order: 6, alt: 'Value of GCL Variations submissions'},
		{type: 'number', description: 'Number of Assessed', field: 'numberAssessed', order: 7, alt: 'Number of Variations were GCL have received the Clients \\ Client\'s Rep assessment'},
		{type: 'amount', description: 'Amount of Assessed', field: 'amountAssessed', order: 8, alt: 'Value of Client\'s Rep Variation Assessments'},
		{type: 'number', description: 'Number of Applied', field: 'numberApplied', order: 9, alt: 'Number of Variations were we have included an applied amount within our Application'},
		{type: 'amount', description: 'Amount of Applied', field: 'amountApplied', order: 10, alt: 'Value of Variations within GCL Interim Application'},
		{type: 'number', description: 'Number of Certified', field: 'numberCertified', order: 11, alt: 'Number of variations were the client has included a certification'},
		{type: 'amount', description: 'Amount of Certified', field: 'amountCertified', order: 12, alt: 'Value of Variations certified by Client \\ Client\'s Representative'},
		{type: 'amount', description: 'Secured EOJ', field: 'eojSecured', order: 13, alt: 'alt (TBC)'},
		{type: 'amount', description: 'Unsecured EOJ', field: 'eojUnsecured', order: 14, alt: 'alt (TBC)'},
		{type: 'amount', description: 'Total EOJ', field: 'eojTotal', order: 15, alt: 'alt (TBC)'},
		{type: 'text', description: 'Remarks', field: 'remarks', order: 16, alt: 'alt (TBC)'}
	];
	
	$scope.items = [
		{
			type: 'Total', 
			order: 1, 
			description: 'Total',
			numberField: 'numberIssued',
			amountField: 'amountIssued',
			numberAlt: 'Number of Variations issued by the Client \\ Client\'s Rep. (Includes requests for Variations)', 
			amountAlt: 'Value of Variations issued by the Client \\ Client\'s Rep based on GCL anticpated Final Account Submission Value',
			alt: 'Total of all Variations inclusive of Requests for Variations (Budget to be included in “Amount” if pricing is not completed'
		},
		{
			type: 'Submitted', 
			order: 2, 
			description: 'Submitted', 
			numberField: 'numberSubmitted',
			amountField: 'amountSubmitted',
			numberAlt: 'Number of Variations were a GCL have submitted their assessment to the Client \\ Client\'s Rep', 
			amountAlt: 'Value of GCL Variations submissions',
			alt: 'Variations submitted by Gammon to the Client / Client\'s representative'
		},
		{
			type: 'Assessed', 
			order: 3, 
			description: 'Assessed', 
			numberField: 'numberAssessed',
			amountField: 'amountAssessed',
			numberAlt: 'Number of Variations were GCL have received the Clients \\ Client\'s Rep assessment', 
			amountAlt: 'Value of Client\'s Rep Variation Assessments',
			alt: 'Assessments of submitted Variations received from the Client / Client\'s representative'
		},
		{
			type: 'Agreed',
			order: 4,
			description: 'Agreed',
			numberField: 'numberAgreed',
			amountField: 'amountAgreed',
			alt: 'Variations which have been agreed in writing with the Client / Client\'s representative'
		},
		{
			type: 'Applied', 
			order: 5, 
			description: 'Applied', 
			numberField: 'numberApplied',
			amountField: 'amountApplied',
			numberAlt: 'Number of Variations were we have included an applied amount within our Application', 
			amountAlt: 'Value of Variations within GCL Interim Application',
			alt: 'Variations which Gammon is including in its Interim Application for Payment'
		},
		{
			type: 'Certified', 
			order: 6, 
			description: 'Certified', 
			numberField: 'numberCertified',
			amountField: 'amountCertified',
			numberAlt: 'Number of variations were the client has included a certification', 
			amountAlt: 'Value of Variations certified by Client \\ Client\'s Representative',
			alt: 'Variations which have been certified by the Client / Client Rep in its Interim Payment Certificate'
		},
		{
			type: 'Secured',
			order: 7,
			description: 'Secured EOJ',
			amountField: 'eojSecured',
			amountAlt: 'amountAlt (TBC)',
		},
		{
			type: 'Unsecured',
			order: 8,
			description: 'Unsecured EOJ',
			amountField: 'eojUnsecured',
			amountAlt: ' amountAlt (TBC)',
		},
		{
			type: 'Total_EOJ',
			order: 9,
			description: 'Total EOJ',
			amountField: 'eojTotal',
			amountAlt: 'amountAlt (TBC)',
		},
		{
			type: 'Remark',
			order: 10,
			description: 'Remarks',
			remarkField: 'remarks',
			remarkAlt: 'remarkAlt (TBC)',
		}
	];
	

	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: false,
			multiSelect: true,
			enableCellEditOnFocus : true,
			showColumnFooter : false,
			showGridFooter : false,
			exporterMenuPdf: false,    
			enableSorting: true,
			rowEditWaitInterval :-1,
		    paginationPageSizes: [10, 25, 50],
		    paginationPageSize: 10,
		    paginationCurrentPage: 1,
		    useExternalPagination: true,
		    useExternalSorting: true,
		    useExternalFiltering: true,
			columnDefs: [
						 { field: 'id', enableCellEdit: false, visible: false},
						 { field: $scope.fields[0].field, width:100, displayName: "Project", enableCellEdit: false, enableFiltering: false},
						 { field: 'year', width:100, displayName: "Year", enableCellEdit: true, type: 'number'},
						 { field: 'month', width:100, displayName: "Month", enableCellEdit: true, type: 'number'},
						 { field: $scope.fields[2].field, width:150, displayName: $scope.fields[2].description, enableCellEdit: true, type: 'number'},
						 { field: $scope.fields[3].field, width:150, displayName: $scope.fields[3].description, enableCellEdit: true, type: 'number'},
						 { field: $scope.fields[4].field, width:150, displayName: $scope.fields[4].description, enableCellEdit: true, type: 'number'},
						 { field: $scope.fields[5].field, width:150, displayName: $scope.fields[5].description, enableCellEdit: true, type: 'number'},
						 { field: $scope.fields[6].field, width:150, displayName: $scope.fields[6].description, enableCellEdit: true, type: 'number'},
						 { field: $scope.fields[7].field, width:150, displayName: $scope.fields[7].description, enableCellEdit: true, type: 'number'},
						 { field: $scope.fields[8].field, width:150, displayName: $scope.fields[8].description, enableCellEdit: true, type: 'number'},
						 { field: $scope.fields[9].field, width:150, displayName: $scope.fields[9].description, enableCellEdit: true, type: 'number'},
						 { field: $scope.fields[10].field, width:150, displayName: $scope.fields[10].description, enableCellEdit: true, type: 'number'},
						 { field: $scope.fields[11].field, width:150, displayName: $scope.fields[11].description, enableCellEdit: true, type: 'number'},
						 { field: $scope.fields[12].field, width:150, displayName: $scope.fields[12].description, enableCellEdit: true, type: 'number'},
						 { field: $scope.fields[13].field, width:150, displayName: $scope.fields[13].description, enableCellEdit: true, type: 'number'},
						 { field: $scope.fields[14].field, width:150, displayName: $scope.fields[14].description, enableCellEdit: true, type: 'number'},
						 { field: $scope.fields[15].field, width:150, displayName: $scope.fields[15].description, enableCellEdit: true, type: 'text'},
//						 { field: 'remarks', width:250, displayName: 'Remark', enableCellEdit: true}
			           ]
	};
	
	$scope.getPage = function(year) {
		if(!year) year = moment().year();
		variationKpiService.getByJobNoYear($scope.jobNo, year)
		.then(function(data) {
			$scope.period = moment().year(year).format('YYYY');
			$scope.kpiList = data;
		});
	    //getVariationKpiList(paginationOptions.pageNumber, paginationOptions.pageSize, paginationOptions.direction, paginationOptions.property);
	}; $scope.getPage();
		
	function loadData() {
		getVariationKpiList(paginationOptions.pageNumber, paginationOptions.pageSize);
	}
	
	$scope.getMonthShortName = function(m) {
		var shortName = moment().month(m-1).format('MMM');
		return shortName;
	}
	
	function getVariationKpiList(page, size, direction, property){
		page--;
		var noJob = $scope.jobNo ;
		var year = $scope.gridApi ? $scope.gridApi.grid.columns[3].filters[0].term : "";
		var month = $scope.gridApi ? $scope.gridApi.grid.columns[4].filters[0].term : ""; 
		var numberIssued = $scope.gridApi ? $scope.gridApi.grid.columns[5].filters[0].term : "";
		var amountIssued = $scope.gridApi ? $scope.gridApi.grid.columns[6].filters[0].term : ""; 
		var numberSubmitted = $scope.gridApi ? $scope.gridApi.grid.columns[7].filters[0].term : ""; 
		var amountSubmitted = $scope.gridApi ? $scope.gridApi.grid.columns[8].filters[0].term : "";
		var numberAssessed = $scope.gridApi ? $scope.gridApi.grid.columns[9].filters[0].term : "";
		var amountAssessed = $scope.gridApi ? $scope.gridApi.grid.columns[10].filters[0].term : "";
		var numberApplied = $scope.gridApi ? $scope.gridApi.grid.columns[11].filters[0].term : "";
		var amountApplied = $scope.gridApi ? $scope.gridApi.grid.columns[12].filters[0].term : "";
		var numberCertified = $scope.gridApi ? $scope.gridApi.grid.columns[13].filters[0].term : ""; 
		var amountCertified = $scope.gridApi ? $scope.gridApi.grid.columns[14].filters[0].term : "";
		//var remarks = '';$scope.gridApi ? $scope.gridApi.grid.columns[15].filters[0].term : "";
		var eojSecured = $scope.gridApi ? $scope.gridApi.grid.columns[15].filters[0].term : "";
		var eojUnsecured = $scope.gridApi ? $scope.gridApi.grid.columns[16].filters[0].term : "";
		var eojTotal = $scope.gridApi ? $scope.gridApi.grid.columns[17].filters[0].term : "";
		var remarks = $scope.gridApi ? $scope.gridApi.grid.columns[18].filters[0].term : "";
		variationKpiService.getByPage(page, size, direction, property,
				noJob, 
				year, 
				month, 
				numberIssued, 
				amountIssued, 
				numberSubmitted, 
				amountSubmitted, 
				numberAssessed, 
				amountAssessed, 
				numberApplied, 
				amountApplied, 
				numberCertified, 
				amountCertified, 
				remarks,
				eojSecured,
				eojUnsecured,
				eojTotal
		)
		.then(function(data){
			$scope.gridSelectedRows = [];
			$scope.gridDirtyRows = [];
			$scope.gridOptions.data = data.content;
			$scope.gridOptions.totalItems = data.totalElements;
			$scope.totalItems = data.totalElements;
			if($scope.gridOptions > 50) {
				$scope.gridOptions.paginationPageSizes = [10, 25, 50, data.totalElements];
			} else {
				$scope.gridOptions.paginationPageSizes = [10, 25, 50];
			}
			$scope.gridApi.selection.clearSelectedRows();
		});
	}
	$scope.onAdd = function() {
		$scope.uibModalInstance = $uibModal.open({
			animation: true,
			templateUrl: "view/job/modal/job-variation-kpi-add-modal.html?@PROJECT_VERSION@",
			controller: 'JobVariationKpiAddModalCtrl',
			size: 'lg',
			backdrop: 'static',
			scope: $scope
		});
	}
	
	$scope.onDelete = function(kpi) {
//		var selectedRows = $scope.gridApi.selection.getSelectedRows();
//		if(selectedRows.length == 0){
//			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select row to delete.");
//			return;
//		}else{
//			const ids = selectedRows.map(function(row){return row.id;});
			variationKpiService.deleteByJobNo($scope.jobNo, [kpi.id])
			.then(function(data) {
//				$scope.gridApi.selection.clearSelectedRows();
//				$scope.gridSelectedRows = [];
				$scope.getPage();
			});
//		}
	}
	
	$scope.onUpdate = function() {
//		if($scope.gridDirtyRows.length > 0){
//			var kpis = [];
//			$scope.gridDirtyRows.forEach(function(row) {
//				kpis.push(row.entity);
//			});
			
			variationKpiService.saveListByJobNo($scope.jobNo, $scope.kpiList)
			.then(function(data) {
//				$scope.gridDirtyRows = [];
				if(data) $scope.getPage();
				$scope.modified = false;
			}, function(error) {
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Fail to update record");
			});
//		}
	}
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;
		
		$scope.gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			
        });
	
		$scope.gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			if(colDef.field == 'month' && (newValue > 12 || newValue < 0)) {
				newValue = oldValue;
				rowEntity.month = oldValue;
			}
			if(colDef.field == 'year' && (newValue > 2099 || newValue < 1970)) {
				newValue = oldValue;
				rowEntity.year = oldValue;
			}
			if(newValue != oldValue) $scope.gridApi.rowEdit.setRowsDirty( [rowEntity]);
			$scope.gridDirtyRows = $scope.gridApi.rowEdit.getDirtyRows($scope.gridApi);
			$scope.isDirty = $scope.gridDirtyRows.length > 0;
		});
		
		$scope.gridApi.selection.on.rowSelectionChanged($scope,function(row){
			$scope.gridSelectedRows = $scope.gridApi.selection.getSelectedRows();
			$scope.isSelected = $scope.gridSelectedRows.length > 0;
		});
		
		$scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
	        if (sortColumns.length == 0) {
	          paginationOptions.sort = null;
	        } else {
	          paginationOptions.direction= sortColumns[0].sort.direction.toUpperCase();
	          paginationOptions.property= sortColumns[0].name;
	        }
	        $scope.getPage();
	      });
		$scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	        paginationOptions.pageNumber = newPage;
	        paginationOptions.pageSize = pageSize;
	        $scope.getPage();
	      });
		$scope.gridApi.core.on.filterChanged($scope, function () {
			if (angular.isDefined($scope.filterTimeout)) {
			    $timeout.cancel($scope.filterTimeout);
			}
			$scope.filterTimeout = $timeout(function () {
				$scope.getPage();
			}, 500);
	      });

	}
	
	$scope.getTextAreaHeight = function() {
		const base = 35;
		let h = base;
		if($scope.gridSelectedRows.length > 0) h += base;
		if($scope.gridDirtyRows.length > 0) h += base;
		return h + "px";
	}
	$scope.getButtonClass = function(type) {
		return type == 'update' ? $scope.gridSelectedRows.length > 0 ? 'col-md-6' : 'col-md-12' :
				type == 'delete' ? $scope.gridDirtyRows.length > 0 ? 'col-md-6' : 'col-md-12' 
				: 'col-md-12';
	}
}]);
