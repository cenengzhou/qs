
mainApp.controller('RepackagingAssignResourcesCtrl', ['$scope', 'repackagingService', '$cookieStore', '$location',
                                             function($scope, repackagingService, $cookieStore, $location) {
	$scope.jobNo = $cookieStore.get("jobNo");
	
	$scope.editable = true;
	$scope.mySelections=[];
	
	
	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			multiSelect: true,
			enableCellEditOnFocus : true,
			showGridFooter : false,
			//showColumnFooter : true,
			exporterMenuPdf: false,
			
			columnDefs: [
			             { field: 'packageNo', cellClass: "grid-theme-blue", enableCellEdit: true},
			             { field: 'objectCode', enableCellEdit: false},
			             { field: 'subsidiaryCode', enableCellEdit: false},
			             { field: 'resourceDescription', enableCellEdit: false},
			             { field: 'unit'},
			             { field: 'quantity', enableCellEdit: false, enableFiltering: false},
			             { field: 'rate', enableCellEdit: false, enableCellEdit: false, enableFiltering: false},
			             { field: 'amount', enableCellEdit: false, enableCellEdit: false, enableFiltering: false},
			             { field: 'postedIvAmount', displayName: "Posted Amount", enableCellEdit: false, enableFiltering: false},
			             { field: 'resourceType', displayName: "Type", enableCellEdit: false},
			             { field: 'excludeDefect', displayName: "Defect", enableCellEdit: false},
			             { field: 'excludeLevy', displayName: "Levy", enableCellEdit: false}
			             ]
	};

	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;

		gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {

			//Alert to show what info about the edit is available
			//alert('Column: ' + colDef.name + ' feedbackRate: ' + rowEntity.feedbackRate);
			//rowEntity.feedbackRateHK = rowEntity.feedbackRate * $scope.exchangeRate;
		});

	}

	
     loadRepacakgingData();
     
     function loadRepacakgingData() {
    	 repackagingService.getResourceSummaries($scope.jobNo, "", "14*")
    	 .then(
			 function( data ) {
				 console.log(data);
				 $scope.gridOptions.data= data;
			 });
     }
    
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	
	$scope.save = function () {
				
		//$location.path("/subcontract-flow");
		//$uibModalInstance.close();


	};
			
}]);