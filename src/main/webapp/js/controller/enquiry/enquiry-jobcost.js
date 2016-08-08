
mainApp.controller('EnquiryJobCostCtrl', ['$scope' , '$rootScope', '$http', 'modalService', 'blockUI', 'GlobalParameter', 'uiGridConstants', 'adlService', 
                                  function($scope , $rootScope, $http,  modalService, blockUI, GlobalParameter, uiGridConstants, adlService) {
	$scope.GlobalParameter = GlobalParameter;
	$scope.currentDate = new Date();
	$scope.showCumulative = true;
	$scope.searchYear = $scope.currentDate.getFullYear().toString().substring(2,4);
	$scope.searchMonth = $scope.currentDate.getMonth() + 1;
	$scope.searchJobNo = $scope.jobNo;

	$scope.columnDefs = [
			             { field: 'accountObject', displayName: 'Object', enableCellEdit: false},
			             { field: 'accountSubsidiary', displayName: 'Subsidiary', enableCellEdit: false},
			             { field: 'accountDescription', displayName: 'Description', enableCellEdit: false},
			             { field: 'jiAmountPeriod', displayName: "Internal Valuation", aggregationHideLabel: true, cellFilter: 'number:2', enableCellEdit: false, 
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
			             { field: 'jiAmountAccum', displayName: "Internal Valuation", aggregationHideLabel: true, cellFilter: 'number:2', enableCellEdit: false,
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
			             { field: 'aaAmountPeriod', displayName: 'Actual Value', cellFilter: 'number:2', enableCellEdit: false,
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
			             { field: 'aaAmountAccum', displayName: 'Actual Value', cellFilter: 'number:2', enableCellEdit: false,
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
			             { field: 'variancePeriod', displayName: 'Variance', cellFilter: 'number:2', enableCellEdit: false,
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
			             { field: 'varianceAccum', displayName: 'Variance', cellFilter: 'number:2', enableCellEdit: false,
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
			paginationPageSizes : [],
			paginationPageSize : 100,
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