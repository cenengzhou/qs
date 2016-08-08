
mainApp.controller('EnquiryIvHistoryCtrl', ['$scope' , '$rootScope', '$http', 'modalService', 'blockUI', 'ivpostinghistService', 'uiGridConstants',
                                  function($scope , $rootScope, $http, modalService, blockUI, ivpostinghistService, uiGridConstants ) {
	
//	$scope.blockEnquiryIvHistory = blockUI.instances.get('blockEnquiryIvHistory');
	
	$scope.searchJobNo = $scope.jobNo;
	$scope.searchToDate = moment().format('DD MMM YYYY');
	$scope.searchFromDate =  moment().year(moment().year() -1 ).format('DD MMM YYYY');
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
			paginationPageSizes : [ ],
			paginationPageSize : 100,
			allowCellFocus: false,
			enableCellSelection: false,
			exporterMenuPdf: false,
			columnDefs: [
			             { field: 'createdDate', displayName: 'Posting Date', enableCellEdit: false, cellFilter: 'date:\'MM/dd/yyyy\''},
			             { field: 'packageNo', displayName: 'Package No.', enableCellEdit: false },
			             { field: 'objectCode', displayName: 'Object', enableCellEdit: false},
			             { field: 'subsidiaryCode', displayName: 'Subsidiary', enableCellEdit: false},
			             { field: 'resourceDescription', displayName: 'Resource Description', enableCellEdit: false},
			             { field: 'unit', displayName: 'Unit', enableCellEdit: false},
			             { field: 'rate', displayName: 'Rate', cellFilter: 'number:2', cellClass: 'text-right', enableCellEdit: false},
			             { field: 'ivMovementAmount', cellFilter: 'number:2', displayName: 'IV Movement Amount', 
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
//		$scope.blockEnquiryIvHistory.start('Loading...')
		ivpostinghistService.obtainIVPostingHistoryList($scope.searchJobNo, $scope.searchFromDate, $scope.searchToDate)
		.then(function(data) {
				if(angular.isArray(data)){
					$scope.gridOptions.data = data;
				}
//				$scope.blockEnquiryIvHistory.stop();
		}, function(data){
//			$scope.blockEnquiryIvHistory.stop();
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data ); 
		})
	}
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	$scope.loadGridData();
	
}]);