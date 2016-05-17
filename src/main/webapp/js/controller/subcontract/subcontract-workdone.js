mainApp.controller('SubcontractWorkdoneCtrl', ['$scope', '$http', function ($scope, $http) {
	$scope.workdone = 221100; 
	$scope.iv = 221100;
	
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
			             { field: 'id', width:80, displayName:"Cert No.",
			            	 cellTemplate: '<div style="text-decoration:underline;color:blue;text-align:right;cursor:pointer" ng-click="grid.appScope.rowClick(row)">{{COL_FIELD}}</div>'},
			            	 //cellTemplate: '<div class="ui-grid-cell-contents"><span><a ng-click="clicker(row)">{{COL_FIELD}}</a></span></div>' },
			            	 { field: 'name', enableCellEdit: false , width:200 },
			            	 { field: 'gender'/*, cellFilter: 'mapGender'*/ , width:100 },
			            	 { field: 'company' , width:200 },
			            	 { field: 'email' , width:300 },
			            	 { field: 'phone' , width:100 },
			            	 { field: 'age' , width:100 },
			            	 {field: 'balance' , width:350 }
			            	 ]


	};

	
	$http.get('http://localhost:8080/QSrevamp2/data/500_complex.json')
	.success(function(data) {
		$scope.gridOptions.data = data;
		$scope.gridOptions.data[0].age = -5;

	});

	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};




	//Save Function
	$scope.save = function () {

	};

}]);

