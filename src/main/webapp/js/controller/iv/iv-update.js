mainApp.controller('IVUpdateCtrl', ['$scope' , 'resourceSummaryService', 'subcontractService', 'uiGridConstants', '$timeout', 'roundUtil', 'modalService', '$state',
                                    function($scope , resourceSummaryService, subcontractService, uiGridConstants, $timeout, roundUtil, modalService, $state) {
	
	var awardedSubcontractNos = [];
	var uneditableUnawardedSubcontractNos = [];
	var optionList = [{ id: 'true', value: 'Excluded' },
	                  { id: 'false', value: 'Included' }
	];

	loadData();

	
	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			//enableFullRowSelection: true,
			//multiSelect: true,
			//showGridFooter : true,
			showColumnFooter : true,
			//fastWatch : true,

			enableCellEditOnFocus : true,
			rowEditWaitInterval :-1,
			/*paginationPageSizes: [50],
			paginationPageSize: 50,
*/

			columnDefs: [
			             { field: 'packageNo', displayName: "Subcontract No.", enableCellEdit: false, width:80},
			             { field: 'objectCode', enableCellEdit: false , width:80},
			             { field: 'subsidiaryCode',enableCellEdit: false, width:80},
			             { field: 'resourceDescription', displayName: "Description", enableCellEdit: false },
			             { field: 'unit', enableCellEdit: false, enableFiltering: false, width:60},
			             { field: 'quantity', enableCellEdit: false ,enableFiltering: false, width:100, 
			            	 cellClass: 'text-right', cellFilter: 'number:2'},
		            	 { field: 'rate', enableCellEdit: false, enableFiltering: false, width:100,
		            		cellClass: 'text-right', cellFilter: 'number:2'},
	            		 {field: 'amountBudget', displayName: "Amount", enableCellEdit: false, enableFiltering: false,
	            			cellClass: 'text-right', cellFilter: 'number:2'},
            			 {field: 'currIVAmount', displayName: "Cum. IV Amount", enableFiltering: false, aggregationType: uiGridConstants.aggregationTypes.sum,
            				 cellTemplate: '<div class="ui-grid-cell-contents" style="color:blue;text-align:right;">{{COL_FIELD | number:2}}</div>',
            				 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;" >{{col.getAggregationValue() | number:2 }}</div>'},
        				 {field: 'ivMovement', displayName: "IV Movement", enableFiltering: false, 
            				 aggregationType: uiGridConstants.aggregationTypes.sum,
        					 cellTemplate: '<div class="ui-grid-cell-contents" style="color:blue;text-align:right;">{{COL_FIELD | number:2}}</div>',
        					 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'},
    					 {field: 'postedIVAmount', displayName: "Posted IV Amount", enableCellEdit: false, enableFiltering: false, 
    						cellClass: 'text-right', cellFilter: 'number:2',
    						 aggregationType: uiGridConstants.aggregationTypes.sum, 
    						 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;" >{{col.getAggregationValue() | number:2 }}</div>'},
						 {field: 'excludeLevy', displayName: "Levy", enableCellEdit: false, width:80, 
    							 filterHeaderTemplate: '<div class="ui-grid-filter-container" ng-repeat="colFilter in col.filters"><div my-custom-dropdown></div></div>', 
    							 filter: { 
    								 term: '',
    								 options: optionList
    							 }, 
    							 cellFilter: 'mapExclude'},
						 {field: 'excludeDefect', displayName: "Defect",  enableCellEdit: false, width:80, 
							 filterHeaderTemplate: '<div class="ui-grid-filter-container" ng-repeat="colFilter in col.filters"><div my-custom-dropdown></div></div>', 
							 filter: { 
								 term: '',
								 options: optionList
							 }, 
							 cellFilter: 'mapExclude'}
						 ]
	};


	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;
		
		gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			if(rowEntity.amountBudget == 0){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Amount of this resource is zero.");
				return;
			}
			if(rowEntity.packageNo !=null && rowEntity.packageNo.length > 0 && rowEntity.objectCode.indexOf("14") >= 0){
				if(awardedSubcontractNos.indexOf(rowEntity.packageNo) >= 0){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please update IV for this resource in the subcontract details section");
					return;
				}
				else if(uneditableUnawardedSubcontractNos.indexOf(rowEntity.packageNo) >= 0){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please update IV for this resource in the subcontract details section");
					return;
				}
			}
		});
	
		gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			if(colDef.name == "currIVAmount"){
				if(rowEntity.currIVAmount > rowEntity.amountBudget){
					rowEntity.currIVAmount = oldValue;
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Cumulative IV Amount cannot be greater than budget amount.");
					return;
				}else{
					rowEntity.currIVAmount  = roundUtil.round(newValue, 2);
					rowEntity.ivMovement = roundUtil.round(rowEntity.currIVAmount - rowEntity.postedIVAmount, 2);
				}
			}
			else if(colDef.name == "ivMovement"){
				var cumIVAmount = roundUtil.round(rowEntity.ivMovement + rowEntity.postedIVAmount, 2); 
				if(cumIVAmount > rowEntity.amountBudget){
					rowEntity.ivMovement = oldValue;
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Cumulative IV Amount cannot be greater than budget amount.");
					return;
				}else{
					rowEntity.ivMovement = roundUtil.round(newValue, 2);
					rowEntity.currIVAmount = cumIVAmount;
				}
			}
		});
		/*gridApi.selection.on.rowSelectionChanged($scope, function () {
	            $scope.selection = gridApi.selection.getSelectedRows();
	        });*/
	}

	
	$scope.applyPercentage = function(){
		if($scope.percent != null){
			angular.forEach($scope.gridOptions.data, function(value, key){
				value.currIVAmount = value.amountBudget * ($scope.percent/100);
				value.ivMovement = value.currIVAmount - value.postedIVAmount;
			});
			$scope.gridApi.grid.refresh();
		}			
		else
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please input %");
	}

	
	$scope.update = function() {
		//$scope.gridOptions.columnDefs.splice(9, 1);
		var gridRows = $scope.gridApi.rowEdit.getDirtyRows();
		var dataRows = gridRows.map( function( gridRow ) { return gridRow.entity; });
		
		if(dataRows.length==0){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "No record has been modified");
			return;
		}
		
		console.log(dataRows);
		
		updateIVAmount(dataRows);
	}
	
	
	function loadData() {
		getResourceSummaries();
		getUnawardedSubcontractNosUnderPaymentRequisition();
		getAwardedSubcontractNos();
	}
	
	function getResourceSummaries() {
		resourceSummaryService.getResourceSummaries($scope.jobNo, "", "")
		.then(
				function( data ) {
					$scope.gridOptions.data = data;

					$scope.nonFinalizedMovementAmount = 0;
					$scope.finalizedMovementAmount = 0;
					angular.forEach(data, function(value, key){
						value.ivMovement = value.currIVAmount - value.postedIVAmount;

						/*if(){
							$scope.nonFinalizedMovementAmount += ;
							$scope.finalizedMovementAmount += ;
						}*/

					});

					$timeout(function () {
						$scope.postedIVAmount = $scope.gridApi.grid.columns[11].getAggregationValue();
						$scope.cumulativeIVAmount = $scope.gridApi.grid.columns[9].getAggregationValue();
						$scope.ivMovement = $scope.gridApi.grid.columns[10].getAggregationValue();
					}, 100);

				});
	}

	function getUnawardedSubcontractNosUnderPaymentRequisition() {
		subcontractService.getUnawardedSubcontractNosUnderPaymentRequisition($scope.jobNo)
		.then(
				function( data ) {
					uneditableUnawardedSubcontractNos = data;
				});
	}
	
	function getAwardedSubcontractNos() {
		subcontractService.getAwardedSubcontractNos($scope.jobNo)
		.then(
				function( data ) {
					awardedSubcontractNos = data;
				});
	}

	function updateIVAmount(resourceSummaryList) {
		resourceSummaryService.updateIVAmount(resourceSummaryList)
		.then(
				function( data ) {
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "IV has been updated.");
						$state.reload();
					}
				});
	}
	
	$scope.read = function (workbook) {
        /* DO SOMETHING WITH workbook HERE */
        console.log(workbook);
      }

      $scope.error = function (e) {
        /* DO SOMETHING WHEN ERROR IS THROWN */
        console.log(e);
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
})
.directive('myCustomDropdown', function() {
	return {
		template: '<select class="form-control input-sm" ng-model="colFilter.term" ng-options="option.id as option.value for option in colFilter.options"></select>'
	};
});;