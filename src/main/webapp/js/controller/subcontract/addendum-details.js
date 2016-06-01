mainApp.controller('AddendumDetailsCtrl', ['$scope' , '$http', 'colorCode', function($scope , $http, colorCode) {

	$scope.lineTypes = {
			options: [
			"V1 - External VO - No Budget",
			"V2 - Internal VO - No Budget", 
			"V3 - ", 
			"L1 - Claims vs GSL", 
			"L2 - Claims vs other Subcontract",
			"D1 - Day Work for GCL", 
			"D2 - Day Work for other Subcontract",
			"CF - CPF"
			],
			selected: ""
	};

	$scope.units = {
			options: [
			"V1 - External VO - No Budget",
			"V2 - Internal VO - No Budget", 
			"V3 - ", 
			"L1 - Claims vs GSL", 
			"L2 - Claims vs other Subcontract",
			"D1 - Day Work for GCL", 
			"D2 - Day Work for other Subcontract",
			"CF - CPF"
			],
			selected: ""
	};


	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableFullRowSelection: true,
			multiSelect: false,
			//showGridFooter : true,
			//showColumnFooter : true,
			//fastWatch : true,

			enableCellEditOnFocus : true,

			paginationPageSizes: [50],
			paginationPageSize: 50,


			//Single Filter
			onRegisterApi: function(gridApi){
				$scope.gridApi = gridApi;
			},
			columnDefs: [
			             { field: 'lineType', width:80, displayName:"Line Type",
			            	 cellTemplate: '<div style="text-decoration:underline;color:blue;text-align:right;cursor:pointer" ng-click="grid.appScope.rowClick(row)">{{COL_FIELD}}</div>'},
			            	 //cellTemplate: '<div class="ui-grid-cell-contents"><span><a ng-click="clicker(row)">{{COL_FIELD}}</a></span></div>' },
			            	 { field: 'bqItem', enableCellEdit: false , width:100 },
			            	 { field: 'CostRate'/*, cellFilter: 'mapGender'*/ },
			            	 { field: 'ScRate' },
			            	 { field: 'Quantity' },
			            	 { field: 'BudgetAmount' },
			            	 { field: 'toBeApprovedCostRate' },
			            	 {field: 'toBeApprovedScRate' },
			            	 {field: 'toBeApprovedQuantity' },
			            	 {field: 'toBeApprovedBudgetAmount' },
			            	 {field: 'MovementAmount' },
			            	 {field: 'ObjectCode' },
			            	 {field: 'SubsidiaryCode' },
			            	 {field: 'Description' },
			            	 {field: 'Unit' },
			            	 {field: 'Remarks' },
			            	 ]

	};

	/*$http.get('http://localhost:8080//data/cert-data.json')
    	      .success(function(data) {
    	        $scope.gridOptions.data = data;
    	      });*/

	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};


}]);