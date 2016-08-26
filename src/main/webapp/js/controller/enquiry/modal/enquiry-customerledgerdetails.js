mainApp.controller('EnquiryCustomerLedgerDetailsCtrl', ['$scope', 'modalStatus', 'modalParam', '$uibModalInstance', 'mainCertService', 'uiGridConstants', 'GlobalHelper',
                                            function($scope, modalStatus, modalParam, $uibModalInstance, mainCertService, uiGridConstants, GlobalHelper){
	$scope.status = modalStatus;
	$scope.parentScope = modalParam;
	$scope.cancel = function () {
		$uibModalInstance.close();
	};
	
	$scope.entity = $scope.parentScope.searchEntity;
	
	$scope.loadAccountLedgerList = function(){
		
		mainCertService.getMainCertReceiveDateAndAmount($scope.entity.company, $scope.entity.documentNumber)
		.then(function(data){
			if(angular.isArray(data)){
				$scope.gridOptions.data = data;
			};
		});
	}
	
	$scope.loadAccountLedgerList();
	
	var postedOptions = [
	                        {label: 'Posted', value:'Posted'},
	                        {label: 'Unposted', value:'Unposted'}
	                        ];

	$scope.columnDefs = [			             
			             { field: 'paymentDate', displayName: "Payment Date", cellFilter: 'date:"dd/MM/yyyy"'},
			             { field: 'paymentAmount', displayName: "Payment Amount", aggregationHideLabel: true, cellFilter: 'number:2', enableCellEdit: false, 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.paymentAmount);
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