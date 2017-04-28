mainApp.controller('RepackagingAssignResourcesCtrl', ['$scope', 'resourceSummaryService', 'subcontractService', 'modalService', 'confirmService', '$state', 'paymentService', 'jdeService',
                                             function($scope, resourceSummaryService, subcontractService, modalService, confirmService, $state, paymentService, jdeService ) {

	loadData();
	
	$scope.editable = true;
	$scope.mySelections=[];

	
	$scope.units=[];

	var optionList = [{ id: '', value: '' },
	                  { id: 'true', value: 'Excluded' },
	                  { id: 'false', value: 'Included' }
	];
	
	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: false,
			multiSelect: true,
			showGridFooter : false,
			enableCellEditOnFocus : true,
			//showColumnFooter : true,
			exporterMenuPdf: false,
			
			rowEditWaitInterval :-1,
			
			columnDefs: [
			             { field: 'packageNo', displayName: "Subcontract No", cellClass: "blue", enableCellEdit: false},
			             { field: 'objectCode', cellClass: "blue"},
			             { field: 'subsidiaryCode', cellClass: "blue"},
			             { field: 'resourceDescription', displayName: "Description", cellClass: "blue"},
			             { field: 'unit', cellClass: "blue", enableFiltering: false, 
			            	 editableCellTemplate: 'ui-grid/dropdownEditor',
			            	 editDropdownValueLabel: 'value', editDropdownOptionsArray: $scope.units
			             },
			             { field: 'quantity', enableCellEdit: false, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:4'},
			             { field: 'rate', enableCellEdit: false, enableCellEdit: false, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:4'},
			             { field: 'amountBudget', displayName: "Amount",  enableCellEdit: false, enableCellEdit: false, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2'},
			             { field: 'postedIVAmount', displayName: "Posted Amount", enableCellEdit: false, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2'},
			             { field: 'resourceType', displayName: "Type", enableCellEdit: false},
			             { field: 'excludeDefect', displayName: "Defect", cellClass: "blue", 
			            	 filterHeaderTemplate: '<div class="ui-grid-filter-container" ng-repeat="colFilter in col.filters"><div my-custom-dropdown></div></div>', 
			                 filter: { 
			                   term: '',
			                   options: optionList
			                 }, 
			            	 editableCellTemplate: 'ui-grid/dropdownEditor',
			            	 cellFilter: 'mapExclude', editDropdownValueLabel: 'value',  editDropdownOptionsArray: optionList
			             },
			             { field: 'excludeLevy', displayName: "Levy", cellClass: "blue", 
			            	 filterHeaderTemplate: '<div class="ui-grid-filter-container" ng-repeat="colFilter in col.filters"><div my-custom-dropdown></div></div>', 
			                 filter: { 
			                   term: '',
			                   options: optionList
			                 }, 
			            	 editableCellTemplate: 'ui-grid/dropdownEditor',
			            	 cellFilter: 'mapExclude', editDropdownValueLabel: 'value',  editDropdownOptionsArray: optionList
			             }
			             ]
          /* rowTemplate: "<div ng-dblclick=\"grid.appScope.onDblClickRow(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>",*/
	};

	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;
		
		gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			if(colDef.name != 'excludeDefect' && colDef.name != 'excludeLevy'){
				if(rowEntity.postedIVAmount != 0){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Selected field cannot be edited - resource has posted IV amount.");
					return;
				}
			}
			if($scope.uneditableResourceSummaryID.indexOf(rowEntity.id) >=0 ){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Subcontract "+rowEntity.packageNo+" has been paid in Payment Requisition.");
				return;
			}
        });
		
		gridApi.selection.on.rowSelectionChanged($scope,function(row){
			if(row.entity.postedIVAmount != 0){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Selected field cannot be edited - resource has posted IV amount.");
				row.isSelected = false;
				return;
			}
			
			if(row.entity.quantity <= 0){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "The selected resource is not available because the Quantity is less than or equal to 0 (zero). Please pick another resource for Tendering.");
				row.isSelected = false;
				return;
			}

			if(row.isSelected) { 
				row.entity.packageNo = $scope.subcontractNo;
				$scope.gridApi.rowEdit.setRowsDirty([row.entity]);
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
			if (selectedRows[i]['postedIVAmount'] != 0){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Selected resource has posted IV amount.");
				return false;
			}
		}
		
		return true;
	}
	
	$scope.deleteResources = function(){
		var selectedRows = $scope.gridApi.selection.getSelectedRows();
		//console.log(selectedRows);
		if(selectedRows.length == 0){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select resources to delete.");
			return;
		}
		deleteResources(selectedRows);
	}
	
	$scope.save = function () {
		if($scope.subcontractNo!="" && $scope.subcontractNo!=null){

			paymentService.getLatestPaymentCert($scope.jobNo, $scope.subcontractNo)
			.then(
					function( data ) {
						if(data){
							if(data.paymentStatus == 'PND'){
								var modalOptions = {
										bodyText: "Payment Requisition with status 'Pending' will be deleted. Proceed?"
								};

								confirmService.showModal({}, modalOptions).then(function (result) {
									if(result == "Yes"){
										proceedToSave();
									}
								});
							}else if(data.paymentStatus == 'APR'){
								proceedToSave();
							}else{
								modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Payment Requisition is being submitted. No amendment is allowed.");
							}
						}else{
							proceedToSave();
						}
					});
			
			
			
		}else{
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Subcontract does not exist.");
		}
	};
			
	function proceedToSave(){
		var gridRows = $scope.gridApi.rowEdit.getDirtyRows();
		var dataRows = gridRows.map( function( gridRow ) { return gridRow.entity; });
		//var dataRows =	$scope.gridOptions.data;
		
		if(dataRows.length==0){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "No record has been modified");
			return;
		}
		
		var tempNewList = dataRows;
		var newList = [];
		
		for (i in tempNewList) {
			for (j in newList) {
				if(newList[j].objectCode === tempNewList[i].objectCode
				   && newList[j].subsidiaryCode === tempNewList[i].subsidiaryCode
				   && newList[j].resourceDescription === tempNewList[i].resourceDescription
				   && newList[j].rate === tempNewList[i].rate
				   && newList[j].unit === tempNewList[i].unit
				   ){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 
							"Duplicate resource: Object Code: " + tempNewList[i].objectCode + ", Subsidiary Code: " + tempNewList[i].subsidiaryCode +
							 ", Description: " + tempNewList[i].resourceDescription + 
							", Unit: " + tempNewList[i].unit + ", Rate: " + tempNewList[i].rate);
					return;
				}
			};
			newList.push(tempNewList[i]);
			
		};
		
		if($scope.subcontract.scStatus == "160"){
			var modalOptions = {
					bodyText: 'Existing tenders and tender details will be changed or deleted. Continue?'
			};


			confirmService.showModal({}, modalOptions).then(function (result) {
				if(result == "Yes"){
					updateResourceSummaries(dataRows);
				}
			});
		}else{
			updateResourceSummaries(dataRows);
		}
	}
	
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
			getUneditableResourceSummaryID();
			getUnitOfMeasurementList();
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
	
	function getUnitOfMeasurementList() {
		jdeService.getUnitOfMeasurementList()
		.then(
				function( data ) {
					angular.forEach(data, function(value, key){
						$scope.units.push({'id': value.unitCode.trim(), 'value': value.unitCode.trim()});
					});
				});
	}
	
	function getUneditableResourceSummaryID() {
		resourceSummaryService.getUneditableResourceSummaryID($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					$scope.uneditableResourceSummaryID = data;
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