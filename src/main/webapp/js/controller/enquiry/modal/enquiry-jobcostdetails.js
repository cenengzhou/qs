mainApp.controller('EnquiryJobCostDetailsCtrl', ['$scope', 'modalStatus', 'modalParam', '$uibModalInstance', 'adlService', 'uiGridConstants', 'GlobalParameter', 'GlobalHelper', 
                                            function($scope, modalStatus, modalParam, $uibModalInstance, adlService, uiGridConstants, GlobalParameter, GlobalHelper){
	$scope.status = modalStatus;
	$scope.parentScope = modalParam;
	$scope.cancel = function () {
		$uibModalInstance.close();
	};
	$scope.GlobalParameter = GlobalParameter;
	$scope.noJob = $scope.parentScope.searchAccountLedger.jobNo;
	$scope.typeLedger = $scope.parentScope.searchAccountLedger.ledgerType;
	$scope.fromDate = moment($scope.parentScope.searchAccountLedger.fromDate).format(GlobalParameter.MOMENT_DATE_FORMAT);
	$scope.thruDate = moment($scope.parentScope.searchAccountLedger.thruDate).format(GlobalParameter.MOMENT_DATE_FORMAT);

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
	
	var postedOptions = [
	                        {label: 'Posted', value:'Posted'},
	                        {label: 'Unposted', value:'Unposted'}
	                        ];

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
			            	 },
			            	 cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"'
			             },
			             { field: 'explanationAddressBook', width: '200', displayName: 'Explanation', enableCellEdit: false,
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = '';
			            		 if(row.entity.typeDocument === 'JE' || row.entity.typeDocument === 'PX'){
			            			 c +=' text-warning';
			            		 }
			            		 return c;
			            	 }			            	 
			             },
			             { field: 'amount', width: '80', displayName: 'Amount', enableCellEdit: false, cellFilter: 'number:2',
			            	 filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
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
			             { field: 'statusPostDescription', width: '80', displayName: 'Post', enableCellEdit: false,headerCellClass:'gridHeaderText',
			            	 filter: { selectOptions: postedOptions, type: uiGridConstants.filter.SELECT  },			             
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = '';
			            		 if(row.entity.typeDocument === 'JE' || row.entity.typeDocument === 'PX'){
			            			 c +=' text-warning';
			            		 }
			            		 return c;
			            	 }			            	 
			             },
			             { field: 'accountTypeSubLedger', width: '80', displayName: 'SubLedger Type', enableCellEdit: false, visible:false},
			             { field: 'accountSubLedger', width: '80', displayName: 'SubLedger', enableCellEdit: false,
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = '';
			            		 if(row.entity.typeDocument === 'JE' || row.entity.typeDocument === 'PX'){
			            			 c +=' text-warning';
			            		 }
			            		 return c;
			            	 }			            	 
			             },
			             { field: 'entityCompanyKey', width: '80', displayName: 'Company', enableCellEdit: false, visible:false},
			             { field: 'currencyCode', width: '80', displayName: 'Currency', enableCellEdit: false, visible:false},
			             { field: 'quantity', width:'100', displayName: "Quantity", enableCellEdit: false, cellFilter: 'number:3',
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = 'text-right';
			            		 if(row.entity.quantity < 0){
			            			 c +=' red';
			            		 }
			            		 return c;
			            	 }, 
			             },
			             { field: 'accountKey', width:'100', displayName: "Purchase Order", enableCellEdit: false },
			             { field: 'entityInputBy', width: '200', displayName: 'Transaction Input By', enableCellEdit: false, visible:false},
			             { field: 'entityGlPostedBy', width: '200', displayName: 'Transaction Posted By', enableCellEdit: false, visible:false},
			             { field: 'explanationRemark', width: '250', displayName: 'Remark', enableCellEdit: false, visible:false},
			             { field: 'recordKeyMatchedPo', width: '80', displayName: 'PO Record Key', enableCellEdit: false, visible:false},
			             { field: 'typeBatch', width: '80', displayName: 'Batch Type', enableCellEdit: false, visible:false},
			             { field: 'numberBatch', width: '80', displayName: 'Batch No', enableCellEdit: false, visible:false},
			             { field: 'dateBatch', width: '120', displayName: 'Batch Date', cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"', enableCellEdit: false, visible:false},
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
		
	
}]);