mainApp.controller('EnquiryJobCostDetailsCtrl', ['$scope', 'modalStatus', 'modalParam', '$uibModalInstance', 'adlService', 'uiGridConstants',
                                            function($scope, modalStatus, modalParam, $uibModalInstance, adlService, uiGridConstants){
	$scope.status = modalStatus;
	$scope.parentScope = modalParam;
	
	$scope.cancel = function () {
		$uibModalInstance.close();
	};
	
	$scope.noJob = $scope.parentScope.searchAccountLedger.jobNo;
	$scope.typeLedger = $scope.parentScope.searchAccountLedger.ledgerType;
	$scope.fromDate = moment($scope.parentScope.searchAccountLedger.fromDate).format('YYYY-MM-DD');
	$scope.thruDate = moment($scope.parentScope.searchAccountLedger.thruDate).format('YYYY-MM-DD');

	$scope.typeDocument = null;
	$scope.noSubcontract = $scope.parentScope.searchAccountLedger.subcontractNo;
	$scope.codeObject = $scope.parentScope.searchAccountLedger.accountObject;
	$scope.codeSubsidiary = $scope.parentScope.searchAccountLedger.accountSubsidiary;
	$scope.postFlag = $scope.parentScope.searchAccountLedger.postFlag;
	
	$scope.loadAccountLedgerList = function(){
		$scope.yearStart = new Date($scope.fromDate).getFullYear().toString().substr(2,2);
		$scope.yearEnd = new Date($scope.thruDate).getFullYear().toString().substr(2,2);
		$scope.monthStart = new Date($scope.fromDate).getMonth()+1;
		$scope.monthEnd = new Date($scope.thruDate).getMonth()+1;
		
		adlService.getAccountLedgerList($scope.noJob, $scope.typeLedger, $scope.yearStart, $scope.yearEnd, 
		$scope.monthStart, $scope.monthEnd, $scope.typeDocument, $scope.noSubcontract, $scope.codeObject, $scope.codeSubsidiary)
		.then(function(data){
			if(angular.isArray(data)){
				$scope.gridOptions.data = data;
			};
		});
	}
	
	$scope.loadAccountLedgerList();
	
	$scope.columnDefs = [
			             { field: 'typeDocument', width: '80', displayName: 'DOC Type', enableCellEdit: false,
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = '';
			            		 if(row.entity.typeDocument === 'JE' || row.entity.typeDocument === 'PX'){
			            			 c +=' text-warning';
			            		 }
			            		 return c;
			            	 }
			             },
			             { field: 'numberDocument', width: '80', displayName: 'DOC Number', enableCellEdit: false,
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = '';
			            		 if(row.entity.typeDocument === 'JE' || row.entity.typeDocument === 'PX'){
			            			 c +=' text-warning';
			            		 }
			            		 return c;
			            	 }			            	 
			             },
			             { field: 'dateGl', width: '80', displayName: 'G/L Date', enableCellEdit: false,
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = '';
			            		 if(row.entity.typeDocument === 'JE' || row.entity.typeDocument === 'PX'){
			            			 c +=' text-warning';
			            		 }
			            		 return c;
			            	 }			            	 
			             },
			             { field: 'explanationRemark', width: '200', displayName: 'Explanation', enableCellEdit: false,
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = '';
			            		 if(row.entity.typeDocument === 'JE' || row.entity.typeDocument === 'PX'){
			            			 c +=' text-warning';
			            		 }
			            		 return c;
			            	 }			            	 
			             },
			             { field: 'amount', width: '80', displayName: 'Amount', enableCellEdit: false, cellFilter: 'number:2',
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = 'text-right';
				            	if(row.entity.typeDocument === 'JE' || row.entity.typeDocument === 'PX'){
				            		c +=' text-warning';
				            	 } else if(row.entity.amount < 0){
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
			             { field: 'accountTypeLedger', width: '80', displayName: 'Ledger Type', enableCellEdit: false,
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = '';
			            		 if(row.entity.typeDocument === 'JE' || row.entity.typeDocument === 'PX'){
			            			 c +=' text-warning';
			            		 }
			            		 return c;
			            	 }			            	 
			             },
			             { field: 'statusPost', width: '80', displayName: 'Post', enableCellEdit: false,
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = '';
			            		 if(row.entity.typeDocument === 'JE' || row.entity.typeDocument === 'PX'){
			            			 c +=' text-warning';
			            		 }
			            		 return c;
			            	 }			            	 
			             },
			             { field: 'accountSubLedger', width: '80', displayName: 'SubLedger', enableCellEdit: false,
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = '';
			            		 if(row.entity.typeDocument === 'JE' || row.entity.typeDocument === 'PX'){
			            			 c +=' text-warning';
			            		 }
			            		 return c;
			            	 }			            	 
			             },
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
		
	
}]);