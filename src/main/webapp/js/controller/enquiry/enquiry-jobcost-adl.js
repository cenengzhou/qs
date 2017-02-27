
mainApp.controller('EnquiryJobCostAdlCtrl', ['$scope', '$http', 'modalService', 'blockUI', 'GlobalParameter', 'uiGridConstants', 'adlService', 'modalService', 'GlobalHelper',
                                  function($scope, $http,  modalService, blockUI, GlobalParameter, uiGridConstants, adlService, modalService, GlobalHelper) {
	$scope.GlobalParameter = GlobalParameter;
	$scope.yearMonth = moment().format('YYYY-MM');
	$scope.showCumulative = true;
	$scope.searchFrom = $scope.yearMonth;
	$scope.searchTo = $scope.yearMonth;
	$scope.searchJobNo = $scope.jobNo;
	$scope.searchAccountLedger = {};
	
	$scope.showJobCostDetails = function(entity){
		var fromYear, fromMonth, toYear, toMonth;
		if($scope.showCumulative){
			fromYear = $scope.fromYear;
			fromMonth = $scope.fromMonth - 1;
			toYear = fromYear;
			toMonth = fromMonth;
		} else {
			fromYear = $scope.fromYear;
			fromMonth = $scope.fromMonth - 1;
			toYear = $scope.toYear;
			toMonth = $scope.toMonth - 1;
		}
		var nextDate = $scope.getNextDate(fromYear, fromMonth, toYear, toMonth);
		$scope.searchAccountLedger.jobNo = $scope.searchJobNo;
		$scope.searchAccountLedger.accountObject = entity.accountObject;
		$scope.searchAccountLedger.accountSubsidiary = entity.accountSubsidiary;
		$scope.searchAccountLedger.subcontractNo = $scope.searchSubcontractNo;
		$scope.searchAccountLedger.accountCode = $scope.searchJobNo +'.'+ entity.accountObject+ '.' + entity.accountSubsidiary;
		$scope.searchAccountLedger.ledgerType = 'AA';
		$scope.searchAccountLedger.subLedger = $scope.searchSubcontractNo;
		$scope.searchAccountLedger.subLedgerType = $scope.searchSubcontractNo != null?"X":null;
		$scope.searchAccountLedger.postFlag = "";
		$scope.searchAccountLedger.fromDate = nextDate.fromDate;
		$scope.searchAccountLedger.thruDate = nextDate.thruDate;
		
		//showAccountLedgerEnquiryDetailPanel(accountCode, ledgerType, subLedger, subLedgerType, fromDate, thruDate, postFlag);
		modalService.open('1000px', 'view/enquiry/modal/enquiry-jobcostdetails.html', 'EnquiryJobCostDetailsAdlCtrl', 'Success', $scope);
		};
	
		$scope.getNextDate = function(searchYearFrom,searchMonthFrom, searchYearTo,searchMonthTo, showCumulative){
			var nextDate = {};
			nextDate.fromDate = new Date();
			nextDate.thruDate = new Date();
			//from year - yearComboBox=110-->1900+110=2010
			//movement --> current year
			//cumulative --> current year-2
//			if(showCumulative){
//				nextDate.fromDate.setFullYear(nextDate.fromDate.getYear()-1 + 1900);
//			}
			//from month - periodComboBox=3-->March, setMonth=2
			//January --> January
			//Other months --> current month-1
			//from date
			//January --> 1st
			//Other months --> 26th
			nextDate.fromDate.setFullYear(searchYearFrom);
			nextDate.thruDate.setFullYear(searchYearTo);

			nextDate.fromDate.setDate(26);
			nextDate.fromDate.setMonth(searchMonthFrom);
//			if(searchMonthFrom === 0){
//				nextDate.fromDate.setDate(1);
//			} else {
//				nextDate.fromDate.setDate(26);
//			}

			nextDate.thruDate.setDate(25);
			nextDate.thruDate.setMonth(searchMonthTo);
			//thru year
			//thru month
			//thru date
			//December --> 31th
			//Other months --> 25th
			
			switch(searchMonthFrom){
			case -1:
				nextDate.fromDate.setFullYear(searchYearFrom);
				nextDate.fromDate.setMonth(0);
				nextDate.fromDate.setDate(1);
				break;
			default:
				break;
			}
			switch(searchMonthTo){
			case 11:
				nextDate.thruDate.setDate(31);
				break;
			default:
				break;
			}
			return nextDate;
		}
		
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
		$scope.loadGridData();
		if($scope.showCumulative){
			$scope.gridOptions.data = $scope.cumulativeData;
			$scope.gridOptions.columnDefs[3].visible = false;
	    	$scope.gridOptions.columnDefs[5].visible = false;
	    	$scope.gridOptions.columnDefs[7].visible = false;
	    	
	    	$scope.gridOptions.columnDefs[4].visible = true;
	    	$scope.gridOptions.columnDefs[6].visible = true;
	    	$scope.gridOptions.columnDefs[8].visible = true;
		}
		else{
			$scope.gridOptions.data = $scope.movementData;
			$scope.gridOptions.columnDefs[3].visible = true;
	    	$scope.gridOptions.columnDefs[5].visible = true;
	    	$scope.gridOptions.columnDefs[7].visible = true;
	    	
	    	$scope.gridOptions.columnDefs[4].visible = false;
	    	$scope.gridOptions.columnDefs[6].visible = false;
	    	$scope.gridOptions.columnDefs[8].visible = false;
		}
	}
	
	$scope.loadGridData = function(){
		checkFromTo();
		if($scope.showCumulative){
			$scope.fromYear = $scope.searchFrom.substring(0,4);
			$scope.fromMonth = parseInt($scope.searchFrom.substring(5,7));
			$scope.toYear = $scope.fromYear
			$scope.toMonth = $scope.fromMonth;
			
			adlService.getMonthlyJobCostList(
					$scope.searchJobNo, 
					$scope.searchSubcontractNo, 
					parseInt($scope.fromYear.substring(2,4)), 
					$scope.fromMonth
			)
			.then(function(data){
				$scope.gridOptions.data = $scope.cumulativeData = data.filter(function(obj){
					return (obj.jiAmountAccum != 0) && (obj.aaAmountAccum != 0);
				});
			})
		} else {
			$scope.fromYear = $scope.searchFrom.substring(0,4);
			$scope.fromMonth = parseInt($scope.searchFrom.substring(5,7));
			$scope.toYear = $scope.searchTo.substring(0,4);
			$scope.toMonth = parseInt($scope.searchTo.substring(5,7));
			
			adlService.getMonthlyJobCostListByPeroidRange(
					$scope.searchJobNo, 
					$scope.searchSubcontractNo, 
					parseInt($scope.fromYear.substring(2,4)), 
					$scope.fromMonth,
					parseInt($scope.toYear.substring(2,4)),
					$scope.toMonth
			)
		    .then(function(data) {
		    	$scope.gridOptions.data = $scope.movementData = data.filter(function(obj){
		    		return (obj.jiAmountPeriod != 0) && (obj.aaAmountPeriod != 0);
		    	});
			}, function(data){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data ); 
			});
		}
	}
	
	function checkFromTo(){
		if(new Date($scope.searchFrom).getTime() > new Date($scope.searchTo).getTime()){
			var tmpDate = $scope.searchFrom;
			$scope.searchFrom = $scope.searchTo;
			$scope.searchTo = tmpDate;
		}
	}
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	
	$scope.triggerShowCumulative();
}]);