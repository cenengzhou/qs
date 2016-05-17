mainApp.controller("RepackagingMergeModalCtrl", ['$scope', '$http', '$location', '$uibModalInstance', 'uiGridConstants', function ($scope, $http, $location, $uibModalInstance, uiGridConstants) {
	$scope.vendorNo= 31347;
	$scope.packageNo= 1004;
	
	
	
	$scope.exchangeRate = 1;
	
	$scope.gridOptions = {
			enableSorting: true,
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableColumnMoving: false,
			//enableRowSelection: true,
			//enableFullRowSelection: true,
			//multiSelect: false,
			//showGridFooter : true,
			showColumnFooter : true,
			//fastWatch : true,

			enableCellEditOnFocus : true,


			//Single Filter
			onRegisterApi: function(gridApi){
				$scope.gridApi = gridApi;
			},
			
			
			/*columnDefs: [
			             { field: "billNo", displayName:"B/P/I", enableCellEdit: false, width:50},
		            	 { field: "objectCode", enableCellEdit: false, width:60 },
		            	 { field: "subsidiaryCode" , enableCellEdit: false, width:80 },
		            	 { field: "description" , enableCellEdit: false,  width:150 },
		            	 { field: "unit" , enableCellEdit: false, width:50 },
		            	 { field: "quantity" , enableCellEdit: false, aggregationType: uiGridConstants.aggregationTypes.sum, width:80 },
		            	 { field: "budgetAmount" , enableCellEdit: false, width:90 }
		            	 { field: "feedbackRate" , enableCellEdit: true, cellClass: "grid-theme-blue", width:120 },
		            	 { field: "feedbackRateHK" ,displayName:"Feedback Rate(HK)", cellClass: "grid-blue", enableCellEdit: false, width:170 }
		    ]
*/

	};

	
	$http.get("http://localhost:8080/QSrevamp2/data/vendor-compare.json")
	.success(function(data) {
		$scope.gridOptions.data = data;
		
		/*console.log("data size:"+ data.length);
		console.log("data1 size:"+ Object.keys(data[0]).length);
		
		var colSize = Object.keys(data[0]).length;
		
		for (i = 0; i < colSize; i++) { 
		    if(i>6){
				console.log("data 0 0:"+ data[0][0]);
				$scope.gridOptions.columnDefs.push({'field' : colName});
		    	//$scope.gridOptions.columnDefs.splice(1, 0, { field: data[0][i], enableSorting: false});
		    }
		}*/
		
		 /*data.forEach( function addDates( row, index ){
			 $scope.gridOptions.columnDefs.splice(1, 0, { field: 'company', enableSorting: false });
	          row.company = "Hello";//Date(today.getDate() + ( index % 14 ) );
	        });
		 */
		 
		
	});

	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};


	//Save Function
	$scope.save = function () {
		$location.path("/subcontract-flow");
		$uibModalInstance.close();
	};

	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
	};
	
	//Listen for location changes and call the callback
    $scope.$on('$locationChangeStart', function(event){
   		 $uibModalInstance.close();
    });
	
}]);

