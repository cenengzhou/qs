mainApp.controller('EnquiryJobCostDetailsCtrl', ['$scope', '$timeout', '$state', 'modalStatus', 'modalParam', '$uibModalInstance', 'adlService', 'uiGridConstants', 'GlobalParameter', 'GlobalHelper', 'GlobalMessage', 'confirmService', 
                                            function($scope, $timeout, $state, modalStatus, modalParam, $uibModalInstance, adlService, uiGridConstants, GlobalParameter, GlobalHelper, GlobalMessage, confirmService){
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
	$scope.searchYear = $scope.parentScope.searchYear;
	$scope.searchMonth = $scope.parentScope.searchMonth;
	$scope.typeDocument = null;
	$scope.noSubcontract = $scope.parentScope.searchAccountLedger.subcontractNo;
	$scope.codeObject = $scope.parentScope.searchAccountLedger.accountObject;
	$scope.codeSubsidiary = $scope.parentScope.searchAccountLedger.accountSubsidiary;
	$scope.postFlag = $scope.parentScope.searchAccountLedger.postFlag;
	
	$scope.loadAccountLedgerList = function(){
		$scope.yearStart = $scope.searchYear.substring(2,4);
		$scope.yearEnd = $scope.yearStart;
		$scope.monthStart = $scope.searchMonth;
		$scope.monthEnd = $scope.monthStart;
		
		adlService.getAccountLedgerList($scope.noJob, $scope.typeLedger, $scope.yearStart, $scope.yearEnd, 
		$scope.monthStart, $scope.monthEnd, $scope.typeDocument, $scope.noSubcontract, $scope.codeObject, $scope.codeSubsidiary)
		.then(function(data){
			if(angular.isArray(data)){
				$scope.gridOptions.data = data;
				addRecordKeyMatchedPoSplit();
			};
		});
	}
	
	$scope.loadAccountLedgerList();
	function addRecordKeyMatchedPoSplit() {
		$scope.gridOptions.data.forEach(function(d){
			if(d.recordKeyMatchedPo){
				d.recordKeyMatchedPoSplit = d.recordKeyMatchedPo.split('-')[1];
			}
		});
	}
	$scope.loadAccountLedgerListByGlDate = function(){
		adlService.getAccountLedgerListByGlDate(getSearchObject())
		.then(function(data){
			if(angular.isArray(data)){
				$scope.gridOptions.data = data;
				addRecordKeyMatchedPoSplit();
			}
		});
	}
	
//	$scope.loadAccountLedgerListByGlDate();
	
	$scope.goAccountLedger = function(){
		confirmService.show({}, {bodyText:GlobalMessage.navigateToAccountLedgerEnquiry})
		.then(function(response){
			if(response === 'Yes') {
				$scope.cancel();
				$state.go('enquiry.accountLedger', {searchObject: getSearchObject()});
			}
		});
	}
	
	function getSearchObject(){
		var searchObjectMap = {};
		$scope.yearStart = $scope.searchYear.substring(0,4);
		$scope.yearEnd = $scope.yearStart;
		$scope.monthStart = $scope.searchMonth - 2;
		$scope.monthEnd = $scope.searchMonth - 1;
		var nextDate = $scope.parentScope.getNextDate($scope.yearStart, $scope.monthStart, $scope.yearEnd, $scope.monthEnd);
		searchObjectMap.noJob = $scope.noJob;
		searchObjectMap.typeLedger = $scope.typeLedger;
		searchObjectMap.fromDate = nextDate.fromDate;
		searchObjectMap.thruDate = nextDate.thruDate;
		searchObjectMap.typeDocument = $scope.typeDocument;
		searchObjectMap.noSubcontract = $scope.noSubcontract;
		searchObjectMap.codeObject = $scope.codeObject;
		searchObjectMap.codeSubsidiary = $scope.codeSubsidiary;
		return searchObjectMap;
	}
	
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
			             { field: 'amount', width: '120', displayName: 'Amount', enableCellEdit: false, cellFilter: 'number:2',
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
			             { field: 'recordKeyMatchedPoSplit', width:'100', displayName: "PO No.", enableCellEdit: false },
			             { field: 'entityInputBy', width: '200', displayName: 'Transaction Input By', enableCellEdit: false, visible:false},
			             { field: 'entityGlPostedBy', width: '200', displayName: 'Transaction Posted By', enableCellEdit: false, visible:false},
			             { field: 'explanationRemark', width: '250', displayName: 'Remark', enableCellEdit: false, visible:true},
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

	$timeout(function(){
		angular.element('input[name$=".dateRange"').daterangepicker({
		    showDropdowns: true,
		    startDate: $scope.fromDate,
		    endDate: $scope.thruDate,
		    autoApply: true,
		    viewMode: 'months',
			locale: {
			      format: 'YYYY-MM'//GlobalParameter.MOMENT_DATE_FORMAT
			    },

		}, function(start, end) {
			$scope.fromDate = start;
			$scope.thruDate = end;
	       }
		)
	}, 500);
	
	$scope.openDropdown = function( $event){
		angular.element('input[name="' + $event.currentTarget.nextElementSibling.name + '"').click();
	}

	
}]);