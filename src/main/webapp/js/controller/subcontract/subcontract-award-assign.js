
mainApp.controller('RepackagingAssignResourcesCtrl', ['$scope', 'resourceSummaryService', '$cookieStore', '$location', 'modalService',
                                             function($scope, resourceSummaryService, $cookieStore, $location, modalService) {
	$scope.jobNo = $cookieStore.get("jobNo");
	$scope.subcontractNo = $cookieStore.get("subcontractNo");
	
	loadResourceSummaries();
	
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
			
			rowEditWaitInterval :-1,
			
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

     
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	
	$scope.save = function () {
		var gridRows = $scope.gridApi.rowEdit.getDirtyRows();
		var dataRows = gridRows.map( function( gridRow ) { return gridRow.entity; });
		console.log(dataRows);
		
		if(dataRows.length==0){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "No records have been modified");
			return;
		}
		updateResourceSummaries(dataRows);
	};
			
	
	
	function loadResourceSummaries() {
   	 resourceSummaryService.getResourceSummaries($scope.jobNo, $scope.subcontractNo, "14*")
   	 .then(
			 function( data ) {
				 //console.log(data);
				 $scope.gridOptions.data= data;
			 });
    }
	
	function updateResourceSummaries(resourceSummaryList) {
	   	 resourceSummaryService.updateResourceSummaries($scope.jobNo, resourceSummaryList)
	   	 .then(
				 function( data ) {
					 console.log(data);
					 if(data.length!=0){
							modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
						}else{
							modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Resources has been updated.");
						}
				 });
	    }
	
	
}]);