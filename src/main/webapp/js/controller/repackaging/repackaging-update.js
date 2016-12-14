mainApp.controller('RepackagingUpdateCtrl', ['$scope' ,'modalService', 'resourceSummaryService', 'unitService', '$cookies', '$stateParams', '$state', 'uiGridConstants', 'confirmService', 'subcontractService', '$location', 'roundUtil',
                                             function($scope, modalService, resourceSummaryService, unitService, $cookies, $stateParams, $state, uiGridConstants, confirmService, subcontractService, $location, roundUtil) {
	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");

	$scope.repackagingId = $cookies.get("repackagingId");
	
	
	loadResourceSummaries();
	getAwardedSubcontractNos();
	getUnitOfMeasurementList();
	
	
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
			enableSelectAll: true,
			multiSelect: true,
			enableCellEditOnFocus : true,
			showColumnFooter : true,
			showGridFooter : false,
			//showColumnFooter : true,
			exporterMenuPdf: false,

			rowEditWaitInterval :-1,
			
			columnDefs: [
			             { field: 'packageNo', displayName: "Subcontract No."},
			             { field: 'objectCode', cellClass: "blue"},
			             { field: 'subsidiaryCode', cellClass: "blue"},
			             { field: 'resourceDescription', displayName: "Description", cellClass: "blue"},
			             { field: 'unit', cellClass: "blue", enableFiltering: false, 
			            	 editableCellTemplate: 'ui-grid/dropdownEditor',
			            	 editDropdownValueLabel: 'value', editDropdownOptionsArray: $scope.units
			             },
			             { field: 'quantity', enableCellEdit: true, enableFiltering: false, 
			            	cellClass: 'text-right blue', cellFilter: 'number:2'},
			             { field: 'rate', enableCellEdit: false, enableCellEdit: false, enableFiltering: false, 
			            		cellClass: 'text-right', cellFilter: 'number:2'},
			             { field: 'amountBudget', displayName: "Amount", enableCellEdit: false, enableCellEdit: false, enableFiltering: false, 
			            	cellClass: 'text-right', cellFilter: 'number:2',
			            	 aggregationType: uiGridConstants.aggregationTypes.sum,
			            	 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'
			             },
			             { field: 'postedIVAmount', displayName: "Posted Amount", enableCellEdit: false, enableFiltering: false, 
			            	cellClass: 'text-right', cellFilter: 'number:2',
			            	 aggregationType: uiGridConstants.aggregationTypes.sum,
			            	 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'
			             },
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
	};

	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;
		
		gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			if(colDef.name != 'excludeDefect' && colDef.name != 'excludeLevy'){
				if(rowEntity.postedIVAmount != 0){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Selected field cannot be edited - resource has posted IV amount.");
					return;
				}
				
				if(rowEntity.objectCode.substring(0, 2) == "14"){
					if($scope.AwardedSubcontractNos.indexOf(rowEntity.packageNo) >=0 ){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Subcontract "+rowEntity.packageNo+" has been awarded.");
						return;
					}else {
						if(rowEntity.packageNo !=null && rowEntity.packageNo.length > 0){
							var modalOptions = {
									bodyText: "You will be directed to the corresponding non-awarded subcontract to manage the resources. Proceed?"
							};

							confirmService.showModal({}, modalOptions).then(function (result) {
								if(result == "Yes"){
									$cookies.put('subcontractNo', rowEntity.packageNo);
									$location.path('/subcontract-award/tab/assign');
								}
							});
						}else {
							modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please go to corresponding non-awarded subcontract to assign resources.");
							return;
						}
					}						
				}
				else if(colDef.name == 'packageNo'){
					if(rowEntity.packageNo !=null && rowEntity.packageNo.length > 0){
						if(rowEntity.objectCode.substring(0, 2) == "13"){
							var modalOptions = {
									bodyText: "Are you sure to remove material package from Material resource? It cannot be added back again."
							};

							confirmService.showModal({}, modalOptions).then(function (result) {
								if(result == "Yes"){
									rowEntity.packageNo = '';
									$scope.gridApi.rowEdit.setRowsDirty([rowEntity]);
								}
							});

						}
					}else if((rowEntity.packageNo ==null || rowEntity.packageNo.length == 0)){
						if(rowEntity.objectCode.substring(0, 2) == "13"){
							modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Material resource with object code beginning with '13' can only be removed from the package(assign to a blank package).");
							return;
						}else if(rowEntity.objectCode.substring(0, 2) == "11" || rowEntity.objectCode.substring(0, 2) == "12" || rowEntity.objectCode.substring(0, 2) == "15" || rowEntity.objectCode.substring(0, 2) == "19"){
							modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Resource with object code beginning with '11', '12', '15', '19' cannot be assigned to a subcontract.");
							return;
						}
					}
				}
				else if(colDef.name == 'quantity' && rowEntity.resourceDescription != 'Genuine Markup of Change Order'){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Only the Quantity of Genuine Markup of Change Order can be edited.");
					return;
				}
				else if (rowEntity.objectCode == "199999" && rowEntity.subsidiaryCode== "99019999"){
					if(rowEntity.resourceDescription == 'Genuine Markup of Change Order' || rowEntity.resourceDescription == 'Genuine Markup'){
						if(colDef.name == 'resourceDescription'){
							modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Resource description of Genuine Markup cannot be edited.");
							return;
						}else if(colDef.name == 'objectCode'){
							modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Object Code of Genuine Markup cannot be edited.");
							return;
						}else if(colDef.name == 'subsidiaryCode'){
							modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Subsidiary Code of Genuine Markup cannot be edited.");
							return;
						}
					}
				}
				
			}
        });
	
		gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			if(colDef.name == "objectCode" && rowEntity.objectCode != null && rowEntity.objectCode.length < 6){
				rowEntity.objectCode = oldValue;
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Object code should be in 6 digits.");
				return;
			}
			else if(colDef.name == "subsidiaryCode"){
				if(rowEntity.subsidiaryCode != null && rowEntity.subsidiaryCode.length < 6){
					rowEntity.subsidiaryCode = oldValue;
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Subsidiary code should be in 8 digits.");
					return;
				}
				if(rowEntity.resourceType == "VO" 
					&& rowEntity.subsidiaryCode.substring(0, 1) != "4" 
						&& rowEntity.subsidiaryCode.substring(2, 4) != "80"){
					rowEntity.subsidiaryCode = oldValue;
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Subsidiary code of VO should start with [4X80XXXX].");
					return;
				}
			}
			else if (colDef.name == "quantity"){
				rowEntity.amountBudget = roundUtil.round(rowEntity.quantity * rowEntity.rate, 2);
			}
			
			$scope.gridApi.rowEdit.setRowsDirty( [rowEntity]);
			$scope.gridDirtyRows = $scope.gridApi.rowEdit.getDirtyRows($scope.gridApi);
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
			if (selectedRows[i]['packageNo'] != null && selectedRows[i]['packageNo'].length > 0){
				if(action != 'split' || selectedRows[i]['objectCode'].substring(0, 2) != "13"){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Resources with assigned subcontract cannot be edited here.");
					return false;
					
				}
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

	$scope.update = function() {
		$scope.disableButtons = true;
		var gridRows = $scope.gridApi.rowEdit.getDirtyRows();
		var dataRows = gridRows.map( function( gridRow ) { return gridRow.entity; });
		
		if(dataRows.length==0){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "No record has been modified");
			$scope.disableButtons = false;
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
					$scope.disableButtons = false;
					return;
				}
			};
			newList.push(tempNewList[i]);
			
		};
		
		updateResourceSummaries(dataRows);
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
	
	function loadResourceSummaries() {
		resourceSummaryService.getResourceSummaries($scope.jobNo, "", "")
		.then(
				function( data ) {
					$scope.gridOptions.data= data;
				});
	}
	
	function getUnitOfMeasurementList() {
		unitService.getUnitOfMeasurementList()
		.then(
				function( data ) {
					angular.forEach(data, function(value, key){
						$scope.units.push({'id': value.unitCode.trim(), 'value': value.unitCode.trim()});
					});
				});
	}
	
	function updateResourceSummaries(resourceSummaryList) {
		resourceSummaryService.updateResourceSummaries($scope.jobNo, resourceSummaryList)
		.then(
				function( data ) {
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
						$scope.disableButtons = false;
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Resources have been updated.");
						$scope.gridDirtyRows = null;
						$state.reload();
					}
				});
	}
	
	function getAwardedSubcontractNos() {
		subcontractService.getAwardedSubcontractNos($scope.jobNo)
		.then(
				function( data ) {
					$scope.AwardedSubcontractNos = data;
				});
	}
	
	
	$scope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams){ 
		if($scope.gridDirtyRows != null && $scope.gridDirtyRows.length >0){
			event.preventDefault();
			var modalOptions = {
					bodyText: "There are unsaved data, do you want to leave without saving?"
			};
			confirmService.showModal({}, modalOptions)
			.then(function (result) {
				if(result == "Yes"){
					$scope.gridDirtyRows = null;
					$state.go(toState.name);
				}
			});
			
		}
	});

}])
.filter('mapExclude', function() {
  var excludeHash = {
    'true': 'Excluded',
    'false': 'Included'
  };

  return function(input) {
      return excludeHash[input];
  };
})
.directive('myCustomDropdown', function() {
  return {
    template: '<select class="form-control input-sm" ng-model="colFilter.term" ng-options="option.id as option.value for option in colFilter.options"></select>'
  };
});