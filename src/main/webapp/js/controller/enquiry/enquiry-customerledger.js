
mainApp.controller('EnquiryCustomerLedgerCtrl', ['$scope' , '$http', 'modalService', 'jobcostService', 'uiGridConstants', 'GlobalHelper', 'GlobalParameter',
                                  function($scope , $http, modalService, jobcostService, uiGridConstants, GlobalHelper, GlobalParameter) {
	
	
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
			             {name: 'Details', width: '110', displayName: 'Receipt Dates', enableCellEdit: false, enableFiltering: false, pinnedLeft:true,
			            	 cellTemplate: '<div class="col-md-12"><button class="btn btn-sm icon-btn btn-warning" ng-click="grid.appScope.showReceiptHistory(row.entity)"> <span class="fa fa-history" style="padding-left:10px;" ></span> View</button></div>'
			             },
			             { field: 'jobNumber', displayName: "Job No", width: '80', enableCellEdit: false },
			             { field: 'reference', displayName: "Reference", width: '100', enableCellEdit: false },
			             { field: 'customerNumber', displayName: "Customer No", width: '100', enableCellEdit: false },
			             { field: 'customerDescription', displayName: "Customer Description", width: '250', enableCellEdit: false },
			             { field: 'documentType', displayName: "Document Type", width: '120', enableCellEdit: false },
			             { field: 'documentNumber', displayName: "Document No", width: '120', enableCellEdit: false },
			             { field: 'grossAmount', displayName: "Gross Amount", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', enableCellEdit: false, 
			            	 filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.grossAmount);
			            	 }, 
			            	 aggregationHideLabel: true, aggregationType: uiGridConstants.aggregationTypes.sum,
			            	 footerCellTemplate: '<div class="ui-grid-cell-contents">{{col.getAggregationValue() | number:2 }}</div>',
			            	 footerCellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(col.getAggregationValue());
			            	 } 
			             },
			             { field: 'openAmount', displayName: "Open Amount", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', enableCellEdit: false, 
			            	 filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.openAmount);
			            	 }, 
			            	 aggregationHideLabel: true, aggregationType: uiGridConstants.aggregationTypes.sum,
			            	 footerCellTemplate: '<div class="ui-grid-cell-contents">{{col.getAggregationValue() | number:2 }}</div>',
			            	 footerCellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(col.getAggregationValue());
			            	 } 
			             },
			             { field: 'foreignAmount', displayName: "Foreign Amount", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', enableCellEdit: false, 
			            	 filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.foreignAmount);
			            	 }, 
			            	 aggregationHideLabel: true, aggregationType: uiGridConstants.aggregationTypes.sum,
			            	 footerCellTemplate: '<div class="ui-grid-cell-contents">{{col.getAggregationValue() | number:2 }}</div>',
			            	 footerCellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(col.getAggregationValue());
			            	 } 
			             },
			             { field: 'foreignOpenAmount', displayName: "Foreign Open Amount", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', enableCellEdit: false, 
			            	 filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.foreignOpenAmount);
			            	 }, 
			            	 aggregationHideLabel: true, aggregationType: uiGridConstants.aggregationTypes.sum,
			            	 footerCellTemplate: '<div class="ui-grid-cell-contents">{{col.getAggregationValue() | number:2 }}</div>',
			            	 footerCellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(col.getAggregationValue());
			            	 } 
			             },
			             { field: 'payStatus', displayName: "Pay Status", width: '100', enableCellEdit: false },
			             { field: 'company', displayName: "Company", width: '100', enableCellEdit: false },
			             { field: 'currency', displayName: "Currency Code",width: '120',  enableCellEdit: false },
			             { field: 'glDate', width: '100', displayName: "G/L Date", cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"', 
			            	 filterCellFiltered:true, },
			             { field: 'invoiceDate', width: '100', displayName: "Invoice Date", cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"', 
				            	 filterCellFiltered:true, },
			             { field: 'dueDate', width: '100', displayName: "Due Date", cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"', 
					            	 filterCellFiltered:true, },
			             { field: 'dateClosed', width: '100', displayName: "Date Closed", cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"', 
						            	 filterCellFiltered:true, },
			             { field: 'batchNumber', displayName: "Batch No", width: '100', enableCellEdit: false },
			             { field: 'batchType', displayName: "Batch Type", width: '100', enableCellEdit: false },
			             { field: 'batchDate', width: '100', displayName: "Batch Date", cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"', 
			            	 filterCellFiltered:true, },
			             { field: 'remark', displayName: "Remark", width: '250', enableCellEdit: false }
            			 ]
	};
	
	$scope.showReceiptHistory = function(entity){
		$scope.searchEntity = entity;
		modalService.open('md', 'view/enquiry/modal/enquiry-customerledgerdetails.html', 'EnquiryCustomerLedgerDetailsCtrl', 'Success', $scope);
		};
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	$scope.loadGridData = function(){
		jobcostService.getARRecordList($scope.searchJobNo, $scope.searchReference, $scope.searchCustomerNo, $scope.searchDocumentNo, $scope.searchDocumentType)
		.then(function(data){
			if(angular.isObject(data)){
				$scope.gridOptions.data = data;
			} else {
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'Cannot access Job:' + $scope.searchJobNo);
			}
		})
	}
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	$scope.loadGridData();
	
}]);