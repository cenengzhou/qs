mainApp.controller('SubcontractTACtrl', ['$scope', '$http', '$location', 'resourceSummaryService',
                                         function ($scope, $http, $location, resourceSummaryService) {
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
			             { field: 'objectCode'},
			             { field: 'subsidiaryCode'},
			             { field: 'resourceDescription'},
			             { field: 'unit'},
			             { field: 'quantity', enableFiltering: false},
			             { field: 'rate', enableFiltering: false},
			             { field: 'amount', enableFiltering: false},
			             ]

	};

	loadResourceSummaries();
    
	 function loadResourceSummaries() {
    	 resourceSummaryService.getResourceSummaries($scope.jobNo, "", "14*")
    	 .then(
			 function( data ) {
				 console.log(data);
				 $scope.gridOptions.data= data;
			 });
     }

	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};


	//Save Function
	$scope.save = function () {
		//$location.path("/subcontract-flow");
		//$uibModalInstance.close();
	};

}]);

