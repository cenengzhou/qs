
mainApp.controller('EnquirySupplierLedgerCtrl', 
				['$scope' , '$rootScope', '$http', 'modalService', 'subcontractService', 'jobcostService', 'GlobalHelper', 'uiGridConstants', 'GlobalParameter',
		 function($scope , $rootScope, $http, modalService, subcontractService, jobcostService, GlobalHelper, uiGridConstants, GlobalParameter ) {
	
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
			columnDefs: [
			             {name: 'Details', width: '110', displayName: 'Payment Dates', enableCellEdit: false, enableFiltering: false, pinnedLeft:true,
			            	 cellTemplate: '<div class="col-md-12"><button class="btn btn-sm icon-btn btn-warning" ng-click="grid.appScope.showPaymentHistory(row.entity)"> <span class="fa fa-dollar" style="padding-left:10px;" ></span> View</button></div>'
			             },
			             { field: 'jobNumber', displayName: "Job No", width: '80', enableCellEdit: false },
			             { field: 'invoiceNumber', displayName: "Invoice No", width: '150', enableCellEdit: false },
			             { field: 'subledger', displayName: "Subledger", width: '80', enableCellEdit: false },
			             { field: 'supplierNumber', displayName: "Supplier No", width: '80', enableCellEdit: false },
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
			             { field: 'foreignAmountOpen', displayName: "Foreign Open Amount", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', enableCellEdit: false, 
			            	 filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.foreignAmountOpen);
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
			             { field: 'invoiceDate', width: '100', displayName: "Invoice Date", cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"'},
			             { field: 'glDate', width: '100', displayName: "G/L Date", cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"'},
			             { field: 'dueDate', width: '100', displayName: "Due Date", cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"'},
			             { field: 'batchNumber', displayName: "Batch No", width: '100', enableCellEdit: false },
			             { field: 'batchType', displayName: "Batch Type", width: '100', enableCellEdit: false },
			             { field: 'batchDate', width: '100', displayName: "Batch Date", cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"'},
			             { field: 'subledgerType', displayName: "Subledge Type", width: '100', enableCellEdit: false }
            			 ]
	};
	
	$scope.showPaymentHistory = function(entity){
		$scope.searchEntity = entity;
		modalService.open('md', 'view/enquiry/modal/enquiry-supplierledgerdetails.html', 'EnquirySupplierLedgerDetailsCtrl', 'Success', $scope);
	};
	

	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	$scope.searchJobNo = $scope.jobNo;
	$scope.loadGridData = function(){
		if($scope.searchSubcontractNo && !$scope.searchSupplierNo ){
			subcontractService.getSubcontract($scope.searchJobNo, $scope.searchSubcontractNo)
			.then(function(data){
				if(angular.isObject(data)){
					$scope.searchSupplierNo = data.vendorNo;
					$scope.obtainAPRecordList();
				} else {
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'Subcontract ' + $scope.searchSubcontractNo + ' does not exist' ); 
				}
			}, function(error){
//				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', error ); 
			});
		} else {
			$scope.obtainAPRecordList();
		}
		
	}
	
	$scope.obtainAPRecordList = function(){
		jobcostService.obtainAPRecordList(
				$scope.searchJobNo, 
				$scope.searchInvoiceNo, 
				$scope.searchSupplierNo, 
				$scope.searchDocumentNo, 
				$scope.searchDocumentType, 
				$scope.searchSubcontractNo, 
				null)
				.then(function(data){
					if(angular.isObject(data)){
						$scope.gridOptions.data = data;
					}
				}, function(error){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', error ); 
				})
	}
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
//	$scope.loadGridData();
	
}]);