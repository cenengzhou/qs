
mainApp.controller('RepackagingAssignResourcesCtrl', ['$scope', 'resourceSummaryService', 'subcontractService', 'modalService', 'confirmService', '$state', 
                                             function($scope, resourceSummaryService, subcontractService, modalService, confirmService, $state) {

	loadData();
	
	$scope.editable = true;
	$scope.mySelections=[];
	
	
	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			multiSelect: true,
			showGridFooter : false,
			//showColumnFooter : true,
			exporterMenuPdf: false,
			
			columnDefs: [
			             { field: 'packageNo', displayName: "Subcontract No", cellClass: "blue", enableCellEdit: false},
			             { field: 'objectCode', enableCellEdit: false},
			             { field: 'subsidiaryCode', enableCellEdit: false},
			             { field: 'resourceDescription', displayName: "Description", enableCellEdit: false},
			             { field: 'unit',  enableCellEdit: false},
			             { field: 'quantity', enableCellEdit: false, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:4'},
			             { field: 'rate', enableCellEdit: false, enableCellEdit: false, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:4'},
			             { field: 'amountBudget', displayName: "Amount",  enableCellEdit: false, enableCellEdit: false, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2'},
			             { field: 'postedIVAmount', displayName: "Posted Amount", enableCellEdit: false, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2'},
			             { field: 'resourceType', displayName: "Type", enableCellEdit: false},
			             { field: 'excludeDefect', displayName: "Defect", enableCellEdit: false, cellFilter: 'mapExclude'},
			             { field: 'excludeLevy', displayName: "Levy", enableCellEdit: false, cellFilter: 'mapExclude'}
			             ]
          /* rowTemplate: "<div ng-dblclick=\"grid.appScope.onDblClickRow(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>",*/
	};

	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;
		
		gridApi.selection.on.rowSelectionChanged($scope,function(row){
			if(row.isSelected) { 
				row.entity.packageNo = $scope.subcontractNo;
			}else{
				row.entity.packageNo = '';
			}
		});

	}

	//Open Window
	$scope.open = function(view){

		if(view=="split"){
			var valid = validate(view);
			if(valid){
				var selectedRows = $scope.gridApi.selection.getSelectedRows();
				modalService.open('lg', 'view/repackaging/modal/repackaging-split.html', 'RepackagingSplitModalCtrl', 'Split', selectedRows);
			}
		}else if (view=="merge"){
			var valid = validate(view);
			if(valid){
				var selectedRows = $scope.gridApi.selection.getSelectedRows();
				modalService.open('lg', 'view/repackaging/modal/repackaging-split.html', 'RepackagingSplitModalCtrl' , 'Merge', selectedRows);
			}
		}else if (view=="add"){
			modalService.open('md', 'view/repackaging/modal/repackaging-add.html', 'RepackagingAddModalCtrl');
		}
	};
     
	
	var validate = function(action){
		var selectedRows = $scope.gridApi.selection.getSelectedRows();
		if(action == 'split' && selectedRows.length != 1){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please only select 1 row to split.");
			return false;
		}
		else if(action == 'merge' && selectedRows.length < 2){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select at least 2 rows to merge.");
			return false;
		}
		
		var resourceType = '-';
		for (i in selectedRows){
			if(resourceType == '-'){
				resourceType = selectedRows[i]['resourceType'];
			}
			if(selectedRows[i]['resourceType'] != resourceType){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Resources cannot be merged - resources must have the same type.");
				return false;
			}
			/*if (selectedRows[i]['packageNo'] != null && selectedRows[i]['packageNo'].length > 0){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Resources with assigned subcontract cannot be edited here.");
				return false;
			}*/
			if (selectedRows[i]['postedIVAmount'] != 0){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Selected resource has posted IV amount.");
				return false;
			}
		}
		
		return true;
	}
	
	$scope.deleteResources = function(){
		var selectedRows = $scope.gridApi.selection.getSelectedRows();
		console.log(selectedRows);
		if(selectedRows.length == 0){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select resources to delete.");
			return;
		}
		deleteResources(selectedRows);
	}
	
	$scope.save = function () {
		if($scope.subcontractNo!="" && $scope.subcontractNo!=null){
//			var gridRows = $scope.gridApi.rowEdit.getDirtyRows();
//			var dataRows = gridRows.map( function( gridRow ) { return gridRow.entity; });
			var dataRows =	$scope.gridOptions.data;
			
			if(dataRows.length==0){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "No record has been modified");
				return;
			}
			
			if($scope.subcontract.scStatus == "160"){
				var modalOptions = {
						bodyText: 'All existing tenders and tender details will be deleted. Continue?'
				};


				confirmService.showModal({}, modalOptions).then(function (result) {
					if(result == "Yes"){
						updateResourceSummaries(dataRows);
					}
				});
			}else{
				updateResourceSummaries(dataRows);
			}
		}else{
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Subcontract does not exist.");
		}
	};
			
	
	function getResourceSummaries() {
   	 resourceSummaryService.getResourceSummaries($scope.jobNo, $scope.subcontractNo, "14*")
   	 .then(
			 function( data ) {
				 //console.log(data);
				 $scope.gridOptions.data= data;
			 });
    }

	
	function loadData(){
		if($scope.subcontractNo!="" && $scope.subcontractNo!=null){
			getSubcontract();
			getResourceSummaries();
		}/*else{
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Subcontract does not exist.");
		}*/
	}
	
	function getSubcontract(){
		subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					$scope.subcontract = data;
					if($scope.subcontract.scStatus =="330" || $scope.subcontract.scStatus =="500")
						$scope.disableButtons = true;
					else
						$scope.disableButtons = false;
				});
	}
	
	function updateResourceSummaries(resourceSummaryList) {
		resourceSummaryService.updateResourceSummaries($scope.jobNo, resourceSummaryList)
		.then(
				function( data ) {
					//console.log(data);
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Resources have been updated.");
						$state.reload();
					}
				});
	}
	
	function deleteResources(rowsToDelete) {
		resourceSummaryService.deleteResources(rowsToDelete)
		.then(
				function( data ) {
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Resources have been deleted.");
						$state.reload();
					}
				});
	}
	
	
}])
.filter('mapExclude', function() {
  var excludeHash = {
    'true': 'Excluded',
    'false': 'Included'
  };

  return function(input) {
      return excludeHash[input];
  };
});