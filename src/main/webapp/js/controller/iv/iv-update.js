mainApp.controller('IVUpdateCtrl', ['$scope' , 'resourceSummaryService', 'uiGridConstants', function($scope , resourceSummaryService, uiGridConstants) {
	
	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			//enableFullRowSelection: true,
			//multiSelect: true,
			showGridFooter : true,
			showColumnFooter : true,
			//fastWatch : true,

			enableCellEditOnFocus : true,

			paginationPageSizes: [50],
			paginationPageSize: 50,

			//Single Filter
			/*onRegisterApi: function(gridApi){
				$scope.gridApi = gridApi;
				$scope.gridApi.grid.registerRowsProcessor( $scope.singleFilter);
			},*/
			columnDefs: [
			             { field: 'packageNo', enableCellEdit: false, width:80, displayName:"Package No."},
			             { field: 'objectCode', enableCellEdit: false , width:100},
			             { field: 'subsidiaryCode',enableCellEdit: false},
			             { field: 'resourceDescription', displayName: "Description", enableCellEdit: false },
			             { field: 'unit', enableCellEdit: false, enableFiltering: false},
			             { field: 'quantity', enableCellEdit: false ,enableFiltering: false},
			             { field: 'rate', enableCellEdit: false, enableFiltering: false },
			             {field: 'amount', enableCellEdit: false, enableFiltering: false },
			             {field: 'currIVAmount', displayName: "Cum. IV Amount", enableFiltering: false, aggregationType: uiGridConstants.aggregationTypes.sum,
		            	 cellTemplate: '<div class="ui-grid-cell-contents" style="color:blue;text-align:right;">{{COL_FIELD}}</div>',
		            	 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;" >{{col.getAggregationValue() | number:2 }}</div>'},
		            	 {field: 'ivMovement', enableFiltering: false, aggregationType: uiGridConstants.aggregationTypes.sum,
	            		 cellTemplate: '<div class="ui-grid-cell-contents" style="color:blue;text-align:right;">{{COL_FIELD}}</div>',
	            		 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'},
	            		 {field: 'postedIVAmount',  enableCellEdit: false, enableFiltering: false, aggregationType: uiGridConstants.aggregationTypes.sum, 
            			 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;" >{{col.getAggregationValue() | number:2 }}</div>'},
            			 {field: 'levyExcluded', enableCellEdit: false, enableFiltering: false },
            			 {field: 'defectExcluded', enableCellEdit: false, enableFiltering: false}
            			 ]
			
			

	};

	
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
		  /*gridApi.selection.on.rowSelectionChanged($scope, function () {
	            $scope.selection = gridApi.selection.getSelectedRows();
	        });*/
	}
	
	loadIVData();
     
     function loadIVData() {
    	 resourceSummaryService.getResourceSummaries($scope.jobNo, "", "")
    	 .then(
			 function( data ) {
				 //console.log(data);
				 $scope.gridOptions.data = data;
			 });
     }
	
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};

	
}]);