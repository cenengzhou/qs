mainApp.controller('EnquirySupplierLedgerDetailsCtrl', 
			['$scope', 'modalStatus', 'modalParam', '$uibModalInstance', 'jobcostService', 'masterListService', 'uiGridConstants', 'GlobalHelper', 'GlobalParameter',
    function($scope, modalStatus, modalParam, $uibModalInstance, jobcostService, masterListService, uiGridConstants, GlobalHelper, GlobalParameter){
	$scope.status = modalStatus;
	$scope.parentScope = modalParam;
	$scope.cancel = function () {
		$uibModalInstance.close();
	};
	
	$scope.entity = $scope.parentScope.searchEntity;
	
	$scope.loadSupplierLedgerList = function(){
		jobcostService.getAPPaymentHistories(
				$scope.entity.company, 
				$scope.entity.documentType, 
				$scope.entity.supplierNumber, 
				$scope.entity.documentNumber)
		.then(function(data){
			if(angular.isArray(data)){
				$scope.gridOptions.data = data;
			};
		});
		masterListService.getSubcontractorList($scope.entity.supplierNumber)
		.then(function(data){
			if(angular.isArray(data)){
				$scope.supplierName = data[0].vendorName;
			}
		})
	}
	
	$scope.loadSupplierLedgerList();
	
	var postedOptions = [
	                        {label: 'Posted', value:'Posted'},
	                        {label: 'Unposted', value:'Unposted'}
	                        ];

	$scope.columnDefs = [			             
			             { field: 'paymentDate', displayName: "Payment Date", cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"'},
			             { field: 'paymntAmount', displayName: "Payment Amount", aggregationHideLabel: true, cellFilter: 'number:2', enableCellEdit: false, 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.paymntAmount);
			            	 }, 
			            	 aggregationHideLabel: true, aggregationType: uiGridConstants.aggregationTypes.sum,
			            	 footerCellTemplate: '<div class="ui-grid-cell-contents">{{col.getAggregationValue() | number:2 }}</div>',
			            	 footerCellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(col.getAggregationValue());
			            	 } 
			             }
			            ];

	$scope.gridOptions = {
			enableFiltering: false,
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
		
	
}]);