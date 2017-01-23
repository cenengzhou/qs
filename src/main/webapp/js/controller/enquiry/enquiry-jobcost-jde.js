
mainApp.controller('EnquiryJobCostJdeCtrl', ['$scope', '$http', '$timeout', 'modalService', 'blockUI', 'GlobalParameter', 'uiGridConstants', 'adlService', 'jdeService', 'modalService', 'GlobalHelper',
                                  function($scope, $http, $timeout, modalService, blockUI, GlobalParameter, uiGridConstants, adlService, jdeService, modalService, GlobalHelper) {
	window.scope = $scope;
	$scope.GlobalParameter = GlobalParameter;
	$scope.showCumulative = true;
	$scope.searchJobNo = $scope.jobNo;
//	$scope.searchPeriod = moment().format('YYYY-MM');
	$scope.jdesearchDate = {};
	$scope.jdesearchDate.title = 'Exact date:';
	$scope.jdesearchDate.startDate = moment().month(moment().month() ).format(GlobalParameter.MOMENT_DATE_FORMAT);
	$scope.jdesearchDate.endDate = moment().format(GlobalParameter.MOMENT_DATE_FORMAT);
	$scope.searchAccountLedger = {};
	$scope.postFlag = '*';
	$scope.showJobCostDetails = function(entity){		
		var fromYear, fromMonth, toYear, toMonth;
		var nextFromDate = getPeriod($scope.jdesearchDate.startDate, 'from');
		var nextThruDate = getPeriod($scope.jdesearchDate.endDate, 'to');
		fromYear = nextFromDate.year;
		fromMonth = nextFromDate.month;
		toYear = nextThruDate.year;
		toMonth = nextThruDate.month;
		
		var nextDate = $scope.getNextDate(fromYear, fromMonth, toYear, toMonth);
		$scope.searchAccountLedger.jobNo = $scope.searchJobNo;
		$scope.searchAccountLedger.accountObject = entity.objectCode;
		$scope.searchAccountLedger.accountSubsidiary = entity.subsidiaryCode;
		$scope.searchAccountLedger.subcontractNo = $scope.searchSubcontractNo;
		$scope.searchAccountLedger.accountCode = $scope.searchJobNo +'.'+ entity.objectCode+ '.' + entity.subsidiaryCode;
		$scope.searchAccountLedger.ledgerType = 'AA';
		$scope.searchAccountLedger.subLedger = $scope.searchSubcontractNo;
		$scope.searchAccountLedger.subLedgerType = $scope.searchSubcontractNo != null?"X":null;
		$scope.searchAccountLedger.postFlag = $scope.postFlag;
		$scope.searchAccountLedger.fromDate = nextDate.fromDate;
		$scope.searchAccountLedger.thruDate = nextDate.thruDate;
		
		modalService.open('1000px', 'view/enquiry/modal/enquiry-jobcostdetails.html', 'EnquiryJobCostDetailsAdlCtrl', 'Success', $scope);
		};
	
		function getPeriod(date, toFrom){
			var periodDate = new Date(date);
			var yearMonth = {};
			switch(toFrom){
			case 'to':
			case 'from':
				if(periodDate.getDate()>25 && periodDate.getMonth() != 11){
					periodDate.setMonth(periodDate.getMonth()+1);
				}
				break;
			}
			yearMonth.year = periodDate.getFullYear();
			yearMonth.month = periodDate.getMonth();
			return yearMonth;
		} 
		
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
			             { field: 'objectCode', width: '100', displayName: 'Object', enableCellEdit: false},
			             { field: 'subsidiaryCode', width: '100', displayName: 'Subsidiary', enableCellEdit: false},
			             { field: 'accountCodeDescription', width: '300', displayName: 'Description', enableCellEdit: false},
//			             { field: 'ledgerJIType', width: '100', displayName: 'ledgerJIType', enableCellEdit: false},
//			             { field: 'ledgerAAType', width: '100', displayName: 'ledgerAAType', enableCellEdit: false},
			             { field: 'amountJI', width: '230', displayName: "Internal Value (JI)", aggregationHideLabel: true, cellFilter: 'number:2', enableCellEdit: false, 
			            	 filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = 'text-right';
			            		 if(row.entity.amountJI < 0){
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
			             { field: 'amountAA', width: '200', displayName: 'Actual Value (AA)', cellFilter: 'number:2', enableCellEdit: false,
			            	 filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = 'text-right';
			            		 if(row.entity.amountAA < 0){
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
			             { field: 'variance', width: '200', displayName: 'Variance (JI-AA)', cellFilter: 'number:2', enableCellEdit: false,
			            	 filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = 'text-right';
			            		 if(row.entity.variance < 0){
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
	
	$scope.loadGridData = function(){
		var fromDate, thruDte, year, month;
		if($scope.searchSubcontractNo) {
			$scope.subLedgerTyper = 'X';
		} else {
			$scope.subLedgerTyper = '';
			$scope.searchSubcontractNo = '';
		}
//		byDate
//			totalFlag = "I"
//			fromDate = fromDateTextField.getValue();
//			thruDate = thruDateTextField.getValue();
//
//		byPeriod
//			year = yearComboBox.getText().substring(yearComboBox.getText().trim().length()-2, yearComboBox.getText().trim().length());
//					period = periodComboBox.getText();
//			cumulative 
//				totalFlag = "I"
//			movement
//				totalFlag = "P";
		if(!$scope.showCumulative) {		//byDate => movement
			$scope.totalFlag = 'I';
			fromDate = moment($scope.jdesearchDate.startDate).format(GlobalParameter.MOMENT_DATE_FORMAT);
			thruDate = moment($scope.jdesearchDate.endDate).format(GlobalParameter.MOMENT_DATE_FORMAT);
			year = '';
			month = '';
		} else {							//byPeriod => cumulative
			$scope.totalFlag = 'I';
			fromDate = '';
			thruDate = '';
			year = $scope.searchJdePeriod.substring(2,4);
			month = $scope.searchJdePeriod.substring(5,7);
		}
		jdeService.getAccountBalanceByDateRangeList(
				$scope.searchJobNo, 
				$scope.searchSubcontractNo, 
				$scope.subLedgerTyper,
				$scope.totalFlag,
				$scope.postFlag,
				fromDate,
				thruDate, 
				year,
				month
		)
		.then(function(data){
			if(data){
				data.forEach(function(row){
					if(typeof row.amountJI == 'number' && typeof row.amountAA == 'number'){
						row.variance = row.amountJI - row.amountAA;
					}
				})
				$scope.gridOptions.data = data;
			}
		});
	}
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	
	$scope.openDropdown = function( $event){
		angular.element('input[name="' + $event.currentTarget.nextElementSibling.name + '"').click();
	}

	$scope.triggerCumulative = function(){
		if($scope.showCumulative){
			var period = getPeriod(moment($scope.jdesearchDate.startDate).format(GlobalParameter.MOMENT_DATE_FORMAT),'from');
			$scope.searchJdePeriod = period.year + '-' + (period.month+1);
		} else {
			var year = $scope.searchJdePeriod.substring(0,4);
			var month = $scope.searchJdePeriod.substring(5.7) - 1;
			var nextDate = $scope.getNextDate(year, month-1, year, month);
			$scope.jdesearchDate.DateRangePicker.setStartDate(moment(nextDate.fromDate).format($scope.jdesearchDate.format));
			$scope.jdesearchDate.DateRangePicker.setEndDate(moment(nextDate.thruDate).format($scope.jdesearchDate.format));
			$scope.jdesearchDate.DateRangePicker.callback($scope.jdesearchDate.DateRangePicker.startDate, $scope.jdesearchDate.DateRangePicker.endDate);
		}
		$scope.loadGridData();
	}
	$scope.triggerCumulative();
}]);