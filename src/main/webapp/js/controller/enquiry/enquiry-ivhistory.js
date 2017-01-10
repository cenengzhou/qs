
mainApp.controller('EnquiryIvHistoryCtrl', ['$scope', '$http', '$timeout', 'modalService', 'blockUI', 'resourceSummaryService', 'uiGridConstants', 'GlobalParameter', 'GlobalHelper',
                                  function($scope, $http, $timeout, modalService, blockUI, resourceSummaryService, uiGridConstants, GlobalParameter, GlobalHelper) {
	
//	$scope.blockEnquiryIvHistory = blockUI.instances.get('blockEnquiryIvHistory');
	$scope.GlobalParameter = GlobalParameter;
	$scope.searchJobNo = $scope.jobNo;
	$scope.searchToDate = moment().format(GlobalParameter.MOMENT_DATE_FORMAT);
	$scope.searchFromDate =  moment().year(moment().year() -1 ).format(GlobalParameter.MOMENT_DATE_FORMAT);
//	$scope.searchFromDate.setFullYear($scope.searchToDate.getFullYear()-1);
	
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
			             { field: 'createdDate', displayName: 'Posting Date', enableCellEdit: false, cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"', 
			            	 filterCellFiltered:true, },
			             { field: 'packageNo', displayName: 'Package No.', enableCellEdit: false },
			             { field: 'objectCode', displayName: 'Object', enableCellEdit: false},
			             { field: 'subsidiaryCode', displayName: 'Subsidiary', enableCellEdit: false},
			             { field: 'resourceDescription', displayName: 'Resource Description', enableCellEdit: false},
			             { field: 'unit', displayName: 'Unit', enableCellEdit: false},
			             { field: 'rate', displayName: 'Rate', cellFilter: 'number:2', cellClass: 'text-right', enableCellEdit: false, 
			            	 		filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
			             },
			             { field: 'ivMovementAmount', cellFilter: 'number:2', displayName: 'IV Movement Amount', 
			            	 filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = 'text-right';
			            		 if(row.entity.ivMovementAmount < 0){
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
			            	 }, enableCellEdit: false},
			             { field: 'createdUser', displayName: 'Username', enableCellEdit: false},
			             { field: 'documentNo', displayName: 'Document No.', enableCellEdit: false}
            			 ]
	};
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	$scope.loadGridData = function(){
		resourceSummaryService.obtainIVPostingHistoryList($scope.searchJobNo, $scope.searchFromDate, $scope.searchToDate)
		.then(function(data) {
				if(angular.isArray(data)){
					$scope.gridOptions.data = data;
				}
		}, function(data){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data ); 
		})
	}
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	$scope.loadGridData();
	
	$timeout(function(){
	angular.element('input[name$=".dateRange"').daterangepicker({
	    showDropdowns: true,
	    startDate: $scope.searchFromDate,
	    endDate: $scope.searchToDate,
	    autoApply: true,
	    viewMode: 'months',
		locale: {
		      format: GlobalParameter.MOMENT_DATE_FORMAT
		    },

	}, function(start, end) {
		$scope.searchFromDate = start;
		$scope.searchToDate = end;
       }
	)
	}, 500);
	$scope.openDropdown = function( $event){
		angular.element('input[name="' + $event.currentTarget.nextElementSibling.name + '"').click();
	}

}]);