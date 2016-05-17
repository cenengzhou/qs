
mainApp.controller('IVUpdateCtrl', ['$scope' , '$http', 'colorCode', function($scope , $http, colorCode) {
	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			//enableFullRowSelection: true,
			//multiSelect: true,
			showGridFooter : true,
			//showColumnFooter : true,
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
			             { field: 'description', enableCellEdit: false },
			             { field: 'unit', enableCellEdit: false, enableFiltering: false},
			             { field: 'quantity', enableCellEdit: false ,enableFiltering: false},
			             { field: 'rate', enableCellEdit: false, enableFiltering: false },
			             {field: 'amount', enableCellEdit: false, enableFiltering: false },
			             {field: 'cumIvAmount', enableFiltering: false, 
		            	 cellTemplate: '<div class="ui-grid-cell-contents" style="color:blue;text-align:right;">{{COL_FIELD}}</div>'},
		            	 {field: 'ivMovement', enableFiltering: false,
	            		 cellTemplate: '<div class="ui-grid-cell-contents" style="color:blue;text-align:right;">{{COL_FIELD}}</div>'},
	            		 {field: 'postedIvAmount', enableFiltering: false,
            			 cellTemplate: '<div class="ui-grid-cell-contents" style="color:blue;text-align:right;">{{COL_FIELD}}</div>'},
            			 {field: 'levyExcluded', enableCellEdit: false, enableFiltering: false },
            			 {field: 'defectExcluded', enableCellEdit: false, enableFiltering: false}
            			 ]
			
			

	};

	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	
	$http.get('http://localhost:8080/QSrevamp2/data/iv.json')
	.success(function(data) {
		$scope.gridOptions.data = data;
	});

	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};

	
}]);