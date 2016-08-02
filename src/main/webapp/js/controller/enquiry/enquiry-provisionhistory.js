
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
			exporterMenuPdf: false,
			enableCellSelection: false,
			columnDefs: [
			             { field: 'packageNo', displayName: "Subcontract No.", enableCellEdit: false, width:'120' },
			             { field: 'postedMonth', displayName: "Month", enableCellEdit: false, width:'60'},
			             { field: 'postedYr', displayName: "Year", enableCellEdit: false, width:'60'},
			             { field: 'objectCode', displayName: "Object", enableCellEdit: false, width:'60'},
			             { field: 'subsidiaryCode', displayName: "Subsidiary", enableCellEdit: false, width:'100'},
			             { field: 'cumLiabilitiesAmount', displayName: "Workdone Amount (Cumulative)", cellFilter: 'number:2', enableCellEdit: false, width:'220',
								cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
									 var c = 'text-right';
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
								 }
			             },
			             { field: 'postedCertAmount', displayName: "Certified Amount (Posted)", cellFilter: 'number:2', enableCellEdit: false, width:'200',
				            	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
				            		 var c = 'text-right';
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
				            	 }			            	 
			             },
			             { field: 'provision', displayName: "Provision", cellFilter: 'number:2',  enableCellEdit: false, width:'150',
				            	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
				            		 var c = 'text-right';
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
				            	 }
			             },
			             { field: 'scRate', displayName: "SC Rate", cellFilter: 'number:2', enableCellEdit: false, width:'100',
				            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
				            		 var c = 'text-right';
				            		 if(row.entity.scRate < 0){
				            			 c +=' red';
				            		 }
				            		 return c;
				            	 }
		            	 },
			             { field: 'createdUser', displayName: "Username", enableCellEdit: false, width:'120'},
            			 ]
	};
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	$scope.loadGridData = function(){
		$scope.blockEnquiryProvisionHistory.start('Loading...');
		subcontractService.getProvisionPostingHistList($scope.jobNo, $scope.searchSubcontractNo, $scope.searchYear, $scope.searchMonth)
		.then(function(data){
				$scope.gridOptions.data = data;
				$scope.blockEnquiryProvisionHistory.stop();
		}, function(data){
			$scope.blockEnquiryProvisionHistory.stop();
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data ); 
		});
	} 
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	$scope.loadGridData();
	
}]);