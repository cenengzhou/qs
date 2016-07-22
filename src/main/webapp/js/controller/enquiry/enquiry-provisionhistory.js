
mainApp.controller('EnquiryProvisionHistoryCtrl', ['$scope' , '$rootScope', '$http', 'modalService', 'blockUI', 'subcontractService', 'GlobalHelper', 'uiGridConstants',
                                  function($scope , $rootScope, $http, modalService, blockUI, subcontractService, GlobalHelper, uiGridConstants) {
	
	$scope.blockEnquiryProvisionHistory = blockUI.instances.get('blockEnquiryProvisionHistory');
	$scope.currentDate = new Date();
	$scope.searchCumMovement = false;
	$scope.searchYear = $scope.currentDate.getFullYear();
	$scope.searchMonth = $scope.currentDate.getMonth() + 1;
	
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
			columnDefs: [
			             { field: 'packageNo', displayName: "Package No.", enableCellEdit: false },
//			             { field: 'postedMonth', displayName: "Posted Month", enableCellEdit: false},
//			             { field: 'postedYr', displayName: "Posted Year", enableCellEdit: false},
			             { field: 'objectCode', displayName: "Object", enableCellEdit: false},
			             { field: 'subsidiaryCode', displayName: "Subsidiary", enableCellEdit: false},
			             { field: 'scRate', displayName: "SC Rate", cellFilter: 'number:2', enableCellEdit: false},
			             { field: 'cumLiabilitiesQty', cellFilter: 'number:3', 
			            	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = 'text-right';
			            		 if(row.entity.cumLiabilitiesQty < 0){
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
			            	 displayName: "Cumulative Workdone Qty", enableCellEdit: false
			             },
			             { field: 'cumulativeCertifiedQuantity', cellFilter: 'number:3',
			            	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = 'text-right';
			            		 if(row.entity.cumulativeCertifiedQuantity < 0){
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
			            	 displayName: "Cumulative Certified Qty", enableCellEdit: false
			             },
			             { field: 'cumLiabilitiesAmount', cellFilter: 'number:2',
			            	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = 'text-right f-w-500';
			            		 if(row.entity.cumLiabilitiesAmount < 0){
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
			            	 displayName: "Cumulative Workdone Amount", enableCellEdit: false
			             },
			             { field: 'postedCertAmount', cellFilter: 'number:2', 
			            	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = 'text-right f-w-500';
			            		 if(row.entity.postedCertAmount < 0){
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
			            	 displayName: "Posted Certified Amount", enableCellEdit: false
			             },
			             { field: 'provision', cellFilter: 'number:2', 
			            	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 var c = 'text-right f-w-500';
			            		 if(row.entity.provision < 0){
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
			            	 displayName: "Provision", enableCellEdit: false
			             },
			             { field: 'createdUser', displayName: "Username", enableCellEdit: false},
            			 ]
	};
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	$scope.loadGridData = function(){
		$scope.blockEnquiryProvisionHistory.start('Loading...');
		subcontractService.searchProvisionHistory($scope.jobNo, $scope.searchSubcontractNo, $scope.searchYear, $scope.searchMonth)
		.then(function(data){
				if(angular.isArray(data)){
					$scope.addProvisionData(data);
					$scope.gridOptions.data = data;
				} 
				$scope.blockEnquiryProvisionHistory.stop();
		}, function(data){
			$scope.blockEnquiryProvisionHistory.stop();
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data ); 
		});
	}
	
    $scope.addProvisionData = function(data){
    	data.forEach(function(d){
    		if(GlobalHelper.checkNull(d.cumLiabilitiesAmount, d.postedCertAmount)) {
    			d.provision = 0;
    		} else {
    			d.provision = d.cumLiabilitiesAmount - d.postedCertAmount;
    		}
    	});
    }
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	$scope.loadGridData();
	
}]);