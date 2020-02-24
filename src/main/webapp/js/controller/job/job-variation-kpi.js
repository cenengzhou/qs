mainApp.controller('JobVariationKpiCtrl', ['$scope','variationKpiService', '$cookies', 'modalService', '$sce','$state', 'GlobalParameter', 'rootscopeService', '$timeout',
                                   function($scope, variationKpiService, $cookies, modalService, $sce,$state, GlobalParameter, rootscopeService, $timeout) {
	$scope.GlobalParameter = GlobalParameter;

	$scope.jobNo = $cookies.get("jobNo");
	$scope.gridDirtyRows = [];
	$scope.gridSelectedRows = [];
	$scope.isSelected = 0;
	$scope.isDirty = 0;
	
	var paginationOptions = {
		    pageNumber: 1,
		    pageSize: 5,
		    sort: null
		  };
	$scope.kpi = {
			jobNo: $scope.jobNo,
			period: moment().format('YYYY-MM'),
			numberIssued:0,
			amountIssued:0.00,
			numberSubmitted:0,
			amountSubmitted: 0.00,
			numberAssessed: 0,
			amountAssessed: 0.00,
			numberApplied: 0,
			amountApplied: 0.00,
			numberCertified: 0,
			amountCertified: 0.00,
			remarks: ""
			};
	
	$scope.fields = [
		{type: 'job', description: 'Project', field: 'noJob', order: 1},
		{type: 'month', description: 'Month', field: 'month', order: 2},
		{type: 'number', description: 'Number of Issued', field: 'numberIssued', order: 3},
		{type: 'amount', description: 'Amount of Issued', field: 'amountIssued', order: 4},
		{type: 'number', description: 'Number of Submitted', field: 'numberSubmitted', order: 5},
		{type: 'amount', description: 'Amount of Submitted', field: 'amountSubmitted', order: 6},
		{type: 'number', description: 'Number of Assessed', field: 'numberAssessed', order: 7},
		{type: 'amount', description: 'Amount of Assessed', field: 'amountAssessed', order: 8},
		{type: 'number', description: 'Number of Applied', field: 'numberApplied', order: 9},
		{type: 'amount', description: 'Amount of Applied', field: 'amountApplied', order: 10},
		{type: 'number', description: 'Number of Certified', field: 'numberCertified', order: 11},
		{type: 'amount', description: 'Amount of Certified', field: 'amountCertified', order: 12}
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
		    paginationPageSizes: [5, 25, 50],
		    paginationPageSize: 5,
		    paginationCurrentPage: 1,
		    useExternalPagination: true,
		    useExternalSorting: true,
		    useExternalFiltering: true,
			columnDefs: [
						 { field: 'id', enableCellEdit: false, visible: false},
						 { field: 'noJob', width:100, displayName: "Project", enableCellEdit: false, enableFiltering: false},
						 { field: 'year', width:100, displayName: "Year", enableCellEdit: true},
						 { field: 'month', width:100, displayName: "Month", enableCellEdit: true},
						 { field: 'numberIssued', width:150, displayName: "No.Issued", enableCellEdit: true},
						 { field: 'amountIssued', width:150, displayName: "Amt.Issued", enableCellEdit: true},
						 { field: 'numberSubmitted', width:150, displayName: "No.Submitted", enableCellEdit: true},
						 { field: 'amountSubmitted', width:150, displayName: "Amt.Submitted", enableCellEdit: true},
						 { field: 'numberAssessed', width:150, displayName: "No.Assessed", enableCellEdit: true},
						 { field: 'amountAssessed', width:150, displayName: "Amt.Assessed", enableCellEdit: true},
						 { field: 'numberApplied', width:150, displayName: "No.Applied", enableCellEdit: true},
						 { field: 'amountApplied', width:150, displayName: "Amt.Applied", enableCellEdit: true},
						 { field: 'numberCertified', width:150, displayName: "No.Certified", enableCellEdit: true},
						 { field: 'amountCertified', width:150, displayName: "Amt.Certified", enableCellEdit: true},
						 { field: 'remarks', width:250, displayName: "Remarks", enableCellEdit: true}
			           ]
	};
	var getPage = function() {
	    getVariationKpiList(paginationOptions.pageNumber, paginationOptions.pageSize, paginationOptions.direction, paginationOptions.property);
	};
		
	function loadData() {
		getVariationKpiList(paginationOptions.pageNumber, paginationOptions.pageSize);
	} loadData();
	
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
		var remarks = $scope.gridApi ? $scope.gridApi.grid.columns[15].filters[0].term : "";
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
				remarks
		)
		.then(function(data){
			$scope.gridOptions.data = data.content;
			$scope.gridOptions.totalItems = data.totalElements;
			$scope.totalItems = data.totalElements;
			if($scope.gridOptions > 50) {
				$scope.gridOptions.paginationPageSizes = [5, 25, 50, data.totalElements];
			} else {
				$scope.gridOptions.paginationPageSizes = [5, 25, 50];
			}
			$scope.gridApi.selection.clearSelectedRows();
		});
	}
	$scope.onAdd = function() {
		if($scope.addForm.$valid){
			var period = moment($scope.kpi.period);
			$scope.kpi.year = period.year();
			$scope.kpi.month = period.month() + 1;
			variationKpiService.saveByJobNo($scope.jobNo, $scope.kpi)
			.then(function(data) {
				if(data) loadData();
			});
		}
	}
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;
		
		$scope.gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			
        });
	
		$scope.gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
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
	        getPage();
	      });
		$scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	        paginationOptions.pageNumber = newPage;
	        paginationOptions.pageSize = pageSize;
	        getPage();
	      });
		$scope.gridApi.core.on.filterChanged($scope, function () {
			if (angular.isDefined($scope.filterTimeout)) {
			    $timeout.cancel($scope.filterTimeout);
			}
			$scope.filterTimeout = $timeout(function () {
			    getPage();
			}, 500);
	      });

	}
	
	$scope.getTextAreaHeight = function() {
		const base = 35;
		let h = base;
		if($scope.isSelected) h += base;
		if($scope.isDirty) h += base;
		return h + "px";
	}
}]);
