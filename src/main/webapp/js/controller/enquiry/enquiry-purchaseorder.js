
mainApp.controller('EnquiryPurchaseOrderCtrl', ['$scope' , '$rootScope', '$http', 'modalService', 'uiGridConstants', 'GlobalParameter', 'jobcostService', 'GlobalParameter', 'GlobalHelper',
                                  function($scope , $rootScope, $http, modalService, uiGridConstants, GlobalParameter, jobcostService, GlobalParameter, GlobalHelper) {
	
	$scope.GlobalParameter = GlobalParameter;
	$scope.searchJobNo = $scope.jobNo;
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
			allowCellFocus: false,
			enableCellSelection: false,
			exporterMenuPdf: false,
			columnDefs: [
			             { field: 'addressNumber', displayName: 'Supplier No', enableCellEdit: false },
			             { field: 'documentOrderInvoiceE', displayName: 'Order No', enableCellEdit: false },
			             { field: 'orderType', displayName: 'Order Type', enableCellEdit: false },
			             { field: 'descriptionLine1', displayName: 'Line Description', enableCellEdit: false },
			             { field: 'descriptionLine2', displayName: 'Line Description 2', enableCellEdit: false },
			             { field: 'objectAccount', displayName: 'Object Code', enableCellEdit: false },
			             { field: 'subsidiary', displayName: 'Subsidiary Code', enableCellEdit: false },
			             { field: 'currencyCodeFrom', displayName: 'Currency', enableCellEdit: false },
			             { field: 'amountExtendedPrice', displayName: 'Original Ordered Amount', cellFilter: 'number:2', enableCellEdit: false,
			            	 filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = 'text-right';
			            		 if(row.entity.amountExtendedPrice < 0){
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
			            	 },
			             },
			             { field: 'dateTransactionJulian', displayName: 'Order Date', enableCellEdit: false, cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"'},
			             { field: 'dtForGLAndVouch1', displayName: 'G/L Date', enableCellEdit: false, cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"'},
            			 ]
	};
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	$scope.loadGridData = function(){
		if($scope.searchOrderNo === undefined && $scope.searchOrderType === undefined && $scope.searchSupplierNo === undefined) {
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'Please search with at least one of the following searching criteria: Supplier Number, Order No or Order Type' ); 
		} else {
		jobcostService.getPORecordList($scope.jobNo, $scope.searchOrderNo, $scope.searchOrderType, $scope.searchSupplierNo)
	    .then(function(data) {
			if(angular.isArray(data)){
				$scope.gridOptions.data = data;
			} 
			
		});	
		}
	}

	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
//	$scope.loadGridData();
	
}]);