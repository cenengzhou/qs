
mainApp.controller('EnquiryJobCostCtrl', ['$scope', '$http', 'modalService', 'blockUI', 'GlobalParameter', 'uiGridConstants', 'adlService', 'modalService', 'GlobalHelper',
                                  function($scope, $http,  modalService, blockUI, GlobalParameter, uiGridConstants, adlService, modalService, GlobalHelper) {
	$scope.GlobalParameter = GlobalParameter;
	$scope.currentDate = new Date();
	$scope.showCumulative = true;
	$scope.searchYear = $scope.currentDate.getFullYear().toString().substring(2,4);
	$scope.searchMonth = $scope.currentDate.getMonth() + 1;
	$scope.searchJobNo = $scope.jobNo;
	$scope.searchAccountLedger = {};
	
	$scope.showJobCostDetails = function(entity){
		$scope.searchAccountLedger.jobNo = $scope.searchJobNo;
		$scope.searchAccountLedger.accountObject = entity.accountObject;
		$scope.searchAccountLedger.accountSubsidiary = entity.accountSubsidiary;
		$scope.searchAccountLedger.subcontractNo = $scope.searchSubcontractNo;
		$scope.searchAccountLedger.accountCode = $scope.searchJobNo +'.'+ entity.accountObject+ '.' + entity.accountSubsidiary;
		$scope.searchAccountLedger.ledgerType = 'AA';
		$scope.searchAccountLedger.subLedger = $scope.searchSubcontractNo;
		$scope.searchAccountLedger.subLedgerType = $scope.searchSubcontractNo != null?"X":null;
		$scope.searchAccountLedger.postFlag = "";
		$scope.searchAccountLedger.fromDate = new Date();
		$scope.searchAccountLedger.thruDate = new Date();
		//from year - yearComboBox=110-->1900+110=2010
		//movement --> current year
		//cumulative --> current year-2
		if($scope.showCumulative){
			$scope.searchAccountLedger.fromDate.setFullYear($scope.searchAccountLedger.fromDate.getYear()-1 + 1900);
		}
		//from month - periodComboBox=3-->March, setMonth=2
		//January --> January
		//Other months --> current month-1
		//from date
		//January --> 1st
		//Other months --> 26th
		$scope.searchAccountLedger.fromDate.setDate(1);
		if($scope.searchMonth === 1){
			$scope.searchAccountLedger.fromDate.setMonth(0);
			$scope.searchAccountLedger.fromDate.setDate(1);
		} else {
			$scope.searchAccountLedger.fromDate.setMonth($scope.searchMonth - 2);
			$scope.searchAccountLedger.fromDate.setDate(26);
		}
		
		if($scope.searchAccountLedger.thruDate.getMonth() === 11){
			$scope.searchAccountLedger.thruDate.setDate(29);
		} else {
			$scope.searchAccountLedger.thruDate.setDate(25);
		}
		
		//thru year
		//thru month
		//thru date
		//December --> 29th
		//Other months --> 25th
		
		//showAccountLedgerEnquiryDetailPanel(accountCode, ledgerType, subLedger, subLedgerType, fromDate, thruDate, postFlag);
		modalService.open('lg', 'view/enquiry/modal/enquiry-jobcostdetails.html', 'EnquiryJobCostDetailsCtrl', 'Success', $scope);
		};
	
	$scope.columnDefs = [
			             { field: 'accountObject', width: '100', displayName: 'Object', enableCellEdit: false},
			             { field: 'accountSubsidiary', width: '100', displayName: 'Subsidiary', enableCellEdit: false},
			             { field: 'accountDescription', width: '300', displayName: 'Description', enableCellEdit: false},
			             { field: 'jiAmountPeriod', width: '230', displayName: "Internal Valuation (Movement)", aggregationHideLabel: true, cellFilter: 'number:2', enableCellEdit: false, 
			            	 filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = 'text-right';
			            		 if(row.entity.jiAmountPeriod < 0){
			            			 c +=' red';
			            		 }
			            		 return c;
			            	 }, 
			            	 aggregationHideLabel: true, aggregationType: uiGridConstants.aggregationTypes.sum,
			            	 footerCellTemplate: '<div class="ui-grid-cell-contents">{{col.getAggregationValue() | number:2 }}</div>',
			            	 footerCellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = 'text-right';
			            		 if(col.getAggregationValue() < 0){
			            			 c +=' red';
			            		 }
			            		 return c;
			            	 } 
			             },
			             { field: 'jiAmountAccum', width: '230', displayName: "Internal Valuation (Cumulative)", aggregationHideLabel: true, cellFilter: 'number:2', enableCellEdit: false,
			            	 filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = 'text-right';
			            		 if(row.entity.jiAmountAccum < 0){
			            			 c +=' red';
			            		 }
			            		 return c;
			            	 }, 
			            	 aggregationHideLabel: true, aggregationType: uiGridConstants.aggregationTypes.sum,
			            	 footerCellTemplate: '<div class="ui-grid-cell-contents">{{col.getAggregationValue() | number:2 }}</div>',
			            	 footerCellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = 'text-right';
			            		 if(col.getAggregationValue() < 0){
			            			 c +=' red';
			            		 }
			            		 return c;
			            	 }
			             },
			             { field: 'aaAmountPeriod', width: '200', displayName: 'Actual Value (Movement)', cellFilter: 'number:2', enableCellEdit: false,
			            	 filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = 'text-right';
			            		 if(row.entity.aaAmountPeriod < 0){
			            			 c +=' red';
			            		 }
			            		 return c;
			            	 }, 
			            	 aggregationHideLabel: true, aggregationType: uiGridConstants.aggregationTypes.sum,
			            	 footerCellTemplate: '<div class="ui-grid-cell-contents">{{col.getAggregationValue() | number:2 }}</div>',
			            	 footerCellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = 'text-right';
			            		 if(col.getAggregationValue() < 0){
			            			 c +=' red';
			            		 }
			            		 return c;
			            	 }
			             },
			             { field: 'aaAmountAccum', width: '200', displayName: 'Actual Value (Cumulative)', cellFilter: 'number:2', enableCellEdit: false,
			            	 filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = 'text-right';
			            		 if(row.entity.aaAmountAccum < 0){
			            			 c +=' red';
			            		 }
			            		 return c;
			            	 }, 
			            	 aggregationHideLabel: true, aggregationType: uiGridConstants.aggregationTypes.sum,
			            	 footerCellTemplate: '<div class="ui-grid-cell-contents">{{col.getAggregationValue() | number:2 }}</div>',
			            	 footerCellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = 'text-right';
			            		 if(col.getAggregationValue() < 0){
			            			 c +=' red';
			            		 }
			            		 return c;
			            	 }
			             },
			             { field: 'variancePeriod', width: '180', displayName: 'Variance (Movement)', cellFilter: 'number:2', enableCellEdit: false,
			            	 filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = 'text-right';
			            		 if(row.entity.variancePeriod < 0){
			            			 c +=' red';
			            		 }
			            		 return c;
			            	 }, 
			            	 aggregationHideLabel: true, aggregationType: uiGridConstants.aggregationTypes.sum,
			            	 footerCellTemplate: '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:2 }}</div>',
			            	 footerCellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = 'text-right';
			            		 if(col.getAggregationValue() < 0){
			            			 c +=' red';
			            		 }
			            		 return c;
			            	 }
			             },
			             { field: 'varianceAccum', width: '180', displayName: 'Variance (Cumulative)', cellFilter: 'number:2', enableCellEdit: false,
			            	 filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = 'text-right';
			            		 if(row.entity.varianceAccum < 0){
			            			 c +=' red';
			            		 }
			            		 return c;
			            	 }, 
			            	 aggregationHideLabel: true, aggregationType: uiGridConstants.aggregationTypes.sum,
			            	 footerCellTemplate: '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:2 }}</div>',
			            	 footerCellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = 'text-right';
			            		 if(col.getAggregationValue() < 0){
			            			 c +=' red';
			            		 }
			            		 return c;
			            	 }
			             },
			             {name: 'Details', width: '100', displayName: '', enableCellEdit: false, enableFiltering: false,
			            	 cellTemplate: '<div class="col-md-12"><button class="btn btn-sm icon-btn btn-default" ng-click="grid.appScope.showJobCostDetails(row.entity)"> <span class="fa fa-file-o" style="padding-left:10px;" ></span> Details</button></div>'
			             }
            			 ];
	
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
			enableCellEditOnFocus : false,
			totalServerItems: 0,
			allowCellFocus: false,
			enableCellSelection: false,
			exporterMenuPdf: false,
			columnDefs: $scope.columnDefs
	};
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	$scope.triggerShowCumulative = function(){
		// odd - Period (3,5,7), even - Cumulative (4,6,8)
		if($scope.showCumulative){
			$scope.gridOptions.columnDefs[3].visible = false;
	    	$scope.gridOptions.columnDefs[5].visible = false;
	    	$scope.gridOptions.columnDefs[7].visible = false;
	    	
	    	$scope.gridOptions.columnDefs[4].visible = true;
	    	$scope.gridOptions.columnDefs[6].visible = true;
	    	$scope.gridOptions.columnDefs[8].visible = true;
		}
		else{
			$scope.gridOptions.columnDefs[3].visible = true;
	    	$scope.gridOptions.columnDefs[5].visible = true;
	    	$scope.gridOptions.columnDefs[7].visible = true;
	    	
	    	$scope.gridOptions.columnDefs[4].visible = false;
	    	$scope.gridOptions.columnDefs[6].visible = false;
	    	$scope.gridOptions.columnDefs[8].visible = false;
		}
		$scope.gridApi.grid.refresh();
	}
	
	$scope.loadGridData = function(){
		adlService.getMonthlyJobCostList($scope.searchJobNo, $scope.searchSubcontractNo, $scope.searchYear, $scope.searchMonth)
		    .then(function(data) {
		    	$scope.gridOptions.data = data;
		    	$scope.triggerShowCumulative();
			}, function(data){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data ); 
		});
	}
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	
	$scope.loadGridData();
}]);