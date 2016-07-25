
mainApp.controller('EnquiryJobCostCtrl', ['$scope' , '$rootScope', '$http', 'modalService', 'blockUI', 'GlobalParameter', 'uiGridConstants', 'adlService', 
                                  function($scope , $rootScope, $http,  modalService, blockUI, GlobalParameter, uiGridConstants, adlService) {
	$scope.GlobalParameter = GlobalParameter;
	$scope.blockJobCost = blockUI.instances.get('blockJobCost');
	
	$scope.columnDefs = [
			             { field: 'accountObject', displayName: 'Object', enableCellEdit: false},
			             { field: 'accountSubsidiary', displayName: 'Subsidiary', enableCellEdit: false},
			             { field: 'accountDescription', displayName: 'Description', enableCellEdit: false},
			             { field: 'internalValue', displayName: 'Internal Value', aggregationHideLabel: true, 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = 'text-right';
			            		 if(row.entity.internalValue < 0){
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
			            	 }, 
			            	 cellFilter: 'number:2', enableCellEdit: false
			             },
			             { field: 'actualValue', displayName: 'Actual Value', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = 'text-right';
			            		 if(row.entity.actualValue < 0){
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
			            	 }, 
			            	 cellFilter: 'number:2', enableCellEdit: false
			             },
			             { field: 'variance', displayName: 'Variance', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = 'text-right';
			            		 if(row.entity.variance < 0){
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
			            	 }, 
			            	 cellFilter: 'number:2', enableCellEdit: false
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
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
//		$scope.changeCumMovement();
	}
	
	$scope.changeCumMovement = function(){
		if($scope.searchCumMovement === false){
			$scope.gridOptions.data = $scope.cumulativeData;
		} else {
			$scope.gridOptions.data = $scope.movementData;
		}
		$scope.gridApi.grid.refresh();
	}
	$scope.currentDate = new Date();
	$scope.searchCumMovement = false;
	$scope.searchYear = $scope.currentDate.getFullYear().toString().substring(2,4);
	$scope.searchMonth = $scope.currentDate.getMonth() + 1;
	$scope.searchJobNo = $scope.jobNo;
	$scope.cumulativeData = {};
	$scope.movementData = {};
    $scope.addVarianceData = function(data){
    	$scope.cumulativeData = angular.copy(data);
    	$scope.movementData = angular.copy(data);
    	$scope.cumulativeData.forEach(function(d){
    		d.actualValue = d.aaAmountAccum;
    		d.internalValue = d.jiAmountAccum;
    		d.variance = d.jiAmountAccum - d.aaAmountAccum;
    	});
    	$scope.movementData.forEach(function(d){
    		d.actualValue = d.aaAmountPeriod;
    		d.internalValue = d.jiAmountPeriod;
    		d.variance = d.jiAmountPeriod - d.aaAmountPeriod;
    	});
    }
	
	$scope.loadGridData = function(){
		$scope.blockJobCost.start('Loading...')
		adlService.getMonthlyJobCostList($scope.searchJobNo, $scope.searchSubcontractNo, $scope.searchYear, $scope.searchMonth)
		    .then(function(data) {
				if(angular.isArray(data)){
					$scope.addVarianceData(data);
					$scope.changeCumMovement();
				} 
				$scope.blockJobCost.stop();
			}, function(data){
			$scope.blockAccountLedger.stop();
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data ); 
		});
	}
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	$scope.loadGridData();
	
}]);