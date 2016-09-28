mainApp.controller("RepackagingSplitModalCtrl", ['$scope', '$location', 'unitService', 'resourceSummaryService', '$uibModalInstance', 'uiGridConstants', '$cookies', 'modalStatus', 'modalParam', 'modalService', 'roundUtil', '$state', 
                                                 function ($scope, $location, unitService, resourceSummaryService, $uibModalInstance, uiGridConstants, $cookies, modalStatus, modalParam, modalService, roundUtil, $state) {

	var action = modalStatus;
	
	$scope.jobNo = $cookies.get("jobNo");
	//$scope.repackagingId = $cookies.get("repackagingId");
	
	var resourceType = modalParam[0]['resourceType'];
	var jobInfo = modalParam[0]['jobInfo'];
	
	var optionList = [{ id: 'true', value: 'Excluded' },
	                  { id: 'false', value: 'Included' }
	];

	$scope.units=[];
	getUnitOfMeasurementList();

	$scope.gridOptions = {
			enableSorting: true,
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableColumnMoving: false,
			showColumnFooter : true,
			//fastWatch : true,
			exporterMenuPdf: false,
			enableCellEditOnFocus : true,


			columnDefs: [
			             /*{ field: 'packageNo', displayName: "Subcontract No."},*/
			             { field: 'objectCode'},
			             { field: 'subsidiaryCode'},
			             { field: 'resourceDescription', displayName: "Description"},
			             { field: 'unit', 
			            	 editableCellTemplate: 'ui-grid/dropdownEditor',
			            	 editDropdownValueLabel: 'value', editDropdownOptionsArray: $scope.units},
		            	 { field: 'quantity', cellClass: 'text-right', cellFilter: 'number:4',
			            	 aggregationType: uiGridConstants.aggregationTypes.sum,
			            	 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'},
		            	 { field: 'rate', cellClass: 'text-right', cellFilter: 'number:4'},
		            	 { field: 'amountBudget', displayName: "Amount", aggregationType: uiGridConstants.aggregationTypes.sum,
			            	 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'},
		            	 { field: 'resourceType', displayName: "Type"},
		            	 { field: 'excludeDefect', displayName: "Defect", 
		            		 editableCellTemplate: 'ui-grid/dropdownEditor',
		            		 cellFilter: 'mapExclude', editDropdownValueLabel: 'value',  editDropdownOptionsArray: optionList
		            	 },
		            	 { field: 'excludeLevy', displayName: "Levy", 
		            		 editableCellTemplate: 'ui-grid/dropdownEditor',
		            		 cellFilter: 'mapExclude', editDropdownValueLabel: 'value',  editDropdownOptionsArray: optionList
		            	 }
		            	 ]

	};

	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;
	}
	
	$scope.gridOptions.data = modalParam;

	$scope.gridOptionsSplit = {
			enableSorting: true,
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableColumnMoving: false,
			showColumnFooter : true,
			//fastWatch : true,
			exporterMenuPdf: false,
			enableCellEditOnFocus : true,

			rowEditWaitInterval :-1,
			
			columnDefs: [
			             /*{ field: 'packageNo', displayName: "Subcontract No."},*/
			             { field: 'objectCode'},
			             { field: 'subsidiaryCode'},
			             { field: 'resourceDescription', displayName: "Description"},
			             { field: 'unit', 
			            	 editableCellTemplate: 'ui-grid/dropdownEditor',
			            	 editDropdownValueLabel: 'value', editDropdownOptionsArray: $scope.units},
			            	 { field: 'quantity', cellClass: 'text-right', cellFilter: 'number:4',
				            	 aggregationType: uiGridConstants.aggregationTypes.sum,
				            	 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'},
			            	 { field: 'rate', cellClass: 'text-right', cellFilter: 'number:4'},
			            	 { field: 'amountBudget', displayName: "Amount",
			            		 cellClass: 'text-right', cellFilter: 'number:2',
				            	 aggregationType: uiGridConstants.aggregationTypes.sum,
				            	 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'},
			            	 { field: 'resourceType', displayName: "Type", enableCellEdit: false},
			            	 { field: 'excludeDefect', displayName: "Defect", 
			            		 editableCellTemplate: 'ui-grid/dropdownEditor',
			            		 cellFilter: 'mapExclude', editDropdownValueLabel: 'value',  editDropdownOptionsArray: optionList
			            	 },
			            	 { field: 'excludeLevy', displayName: "Levy", 
			            		 editableCellTemplate: 'ui-grid/dropdownEditor',
			            		 cellFilter: 'mapExclude', editDropdownValueLabel: 'value',  editDropdownOptionsArray: optionList
			            	 }
			            	 ]

	};
	
	$scope.gridOptionsSplit.onRegisterApi = function (gridApi) {
		$scope.gridApiSplit = gridApi;

		gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			
			if(colDef.name == "objectCode" && rowEntity.objectCode != null && rowEntity.objectCode.length < 6){
				rowEntity.objectCode = oldValue;
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Object code should be in 6 digits.");
				return;
			}
			if(colDef.name == "subsidiaryCode"){
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
			
			
			if(rowEntity.quantity!=null && rowEntity.rate != null && rowEntity.amountBudget != null){
				if(colDef.name == "quantity"){
					rowEntity.quantity = roundUtil.round(newValue, 4);
					rowEntity.amountBudget = roundUtil.round(rowEntity.quantity * rowEntity.rate, 2);
				}
				else if(colDef.name == "rate"){
					rowEntity.rate = roundUtil.round(newValue, 2);
					rowEntity.amountBudget = roundUtil.round(rowEntity.quantity * rowEntity.rate, 2);
				}
				else if(colDef.name == "amountBudget"){
					rowEntity.amountBudget  = roundUtil.round(newValue, 2);
					rowEntity.quantity = roundUtil.round(rowEntity.amountBudget/rowEntity.rate, 4);
				}
			}
		});
		
		gridApi.selection.on.rowSelectionChanged($scope,function(row){
			$scope.removeRowIndex = $scope.gridOptionsSplit.data.indexOf(row.entity);
		});

	}
	
	
	$scope.addData = function() {
		$scope.gridOptionsSplit.data.push({
			"objectCode": $scope.gridOptions.data[0]['objectCode'],
			"subsidiaryCode": $scope.gridOptions.data[0]['subsidiaryCode'],
			"resourceDescription": $scope.gridOptions.data[0]['resourceDescription'],
			"unit": $scope.gridOptions.data[0]['unit'],
			"quantity": $scope.gridOptions.data[0]['quantity'],
			"rate": $scope.gridOptions.data[0]['rate'],
			"amountBudget": $scope.gridOptions.data[0]['amountBudget'],
			"resourceType": $scope.gridOptions.data[0]['resourceType'],
			"excludeDefect": $scope.gridOptions.data[0]['excludeDefect'],
			"excludeLevy": $scope.gridOptions.data[0]['excludeLevy'],
		});
	};
	
	$scope.copyData = function() {
		var resources = $scope.gridApi.selection.getSelectedRows();
		if(resources.length == 0){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select resources from above to copy.");
			return;
		}

		for (i in resources){
			$scope.gridOptionsSplit.data.push({
				"objectCode": resources[i]['objectCode'],
				"subsidiaryCode": resources[i]['subsidiaryCode'],
				"resourceDescription": resources[i]['resourceDescription'],
				"unit": resources[i]['unit'],
				"quantity": resources[i]['quantity'],
				"rate": resources[i]['rate'],
				"amountBudget": resources[i]['amountBudget'],
				"resourceType": resources[i]['resourceType'],
				"excludeDefect": resources[i]['excludeDefect'],
				"excludeLevy": resources[i]['excludeLevy'],
			});
		}


	};
	
	$scope.deleteData = function() {
		var selectedRows = $scope.gridApiSplit.selection.getSelectedRows();
		if(selectedRows.length == 0){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select resource to delete.");
			return;
		}
		if($scope.removeRowIndex != null){
			if($scope.removeRowIndex == 0)
				$scope.gridOptionsSplit.data.splice(0,1);
			else
				$scope.gridOptionsSplit.data.splice($scope.removeRowIndex, $scope.removeRowIndex);
		}

	};
	
	//Save Function
	$scope.save = function () {
		var totalAmountBeforeSplit = roundUtil.round($scope.gridApi.grid.columns[7].getAggregationValue(), 2);
		var totalAmountAfterSplit = roundUtil.round($scope.gridApiSplit.grid.columns[7].getAggregationValue(), 2);
		
		if(totalAmountAfterSplit != totalAmountBeforeSplit ){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Total amount does not match.");
			return;
		}
		
		if(action == "Split"){
			if($scope.gridOptionsSplit.data.length < 2){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please split into at least 2 new resources.");
				return;
			}
		}else if (action == "Merge"){
			if($scope.gridOptionsSplit.data.length != 1){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Resources should be merged into one resource only.");
				return;
			}
		}
		
		for (i in $scope.gridOptionsSplit.data){
			$scope.gridOptionsSplit.data[i]['jobInfo'] = jobInfo;
		}
		
		var resourceSummarySplitMergeWrapper = {};
		resourceSummarySplitMergeWrapper.oldResourceSummaryList = $scope.gridOptions.data;
		resourceSummarySplitMergeWrapper.newResourceSummaryList = $scope.gridOptionsSplit.data;
		
		var tempNewList = $scope.gridOptionsSplit.data;
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
		
		splitOrMergeResources(resourceSummarySplitMergeWrapper);
		
	};

	
	
	function getUnitOfMeasurementList() {
		unitService.getUnitOfMeasurementList()
		.then(
				function( data ) {
					angular.forEach(data, function(value, key){
						$scope.units.push({'id': value.unitCode.trim(), 'value': value.unitCode.trim()});
					});
				});
	}
	
	function splitOrMergeResources(resourceSummarySplitMergeWrapper) {
		resourceSummaryService.splitOrMergeResources($scope.jobNo, resourceSummarySplitMergeWrapper)
		.then(
				function( data ) {
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Resources have been updated.");
						$uibModalInstance.close();
						$state.reload();
					}
				});
	}
	
	
	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
	};

	//Listen for location changes and call the callback
	$scope.$on('$locationChangeStart', function(event){
		$uibModalInstance.close();
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

