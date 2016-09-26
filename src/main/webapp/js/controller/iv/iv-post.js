mainApp.controller('IVPostCtrl', ['$scope' , 'resourceSummaryService', 'subcontractService', 'uiGridConstants', '$timeout', 'roundUtil', 'modalService', '$state',
                                    function($scope , resourceSummaryService, subcontractService, uiGridConstants, $timeout, roundUtil, modalService, $state) {
	
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
				            	 cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
				            			var c = 'text-right';
				            			if (row.entity.amountBudget < 0) {
				            				c += ' red';
				            			}
				            			return c;
				            		},
				            		aggregationHideLabel : true,
				            		aggregationType : uiGridConstants.aggregationTypes.sum,
				            		footerCellTemplate : '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:2 }}</div>',
				            		footerCellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
				            			var c = 'text-right';
				            			if (col.getAggregationValue() < 0) {
				            				c += ' red';
				            			}
				            			return c;
				            		},
				            		cellFilter : 'number:2',
	            		 },
            			 {field: 'currIVAmount', displayName: "Cum. IV Amount", enableFiltering: false, 
			            	 cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            			var c = 'text-right';
			            			if (row.entity.currIVAmount < 0) {
			            				c += ' red';
			            			}
			            			return c;
			            		},
			            		aggregationHideLabel : true,
			            		aggregationType : uiGridConstants.aggregationTypes.sum,
			            		footerCellTemplate : '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:2 }}</div>',
			            		footerCellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            			var c = 'text-right';
			            			if (col.getAggregationValue() < 0) {
			            				c += ' red';
			            			}
			            			return c;
			            		},
			            		cellFilter : 'number:2',
			            	},
        				 {field: 'ivMovement', displayName: "IV Movement", enableFiltering: false, 
				            	 cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
				            			var c = 'text-right';
				            			if (row.entity.ivMovement < 0) {
				            				c += ' red';
				            			}
				            			return c;
				            		},
				            		aggregationHideLabel : true,
				            		aggregationType : uiGridConstants.aggregationTypes.sum,
				            		footerCellTemplate : '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:2 }}</div>',
				            		footerCellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
				            			var c = 'text-right';
				            			if (col.getAggregationValue() < 0) {
				            				c += ' red';
				            			}
				            			return c;
				            		},
				            		cellFilter : 'number:2',
        				 },
    					 {field: 'postedIVAmount', displayName: "Posted IV Amount", enableCellEdit: false, enableFiltering: false, 
    		            	 cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
 		            			var c = 'text-right';
 		            			if (row.entity.postedIVAmount < 0) {
 		            				c += ' red';
 		            			}
 		            			return c;
 		            		},
 		            		aggregationHideLabel : true,
 		            		aggregationType : uiGridConstants.aggregationTypes.sum,
 		            		footerCellTemplate : '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:2 }}</div>',
 		            		footerCellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
 		            			var c = 'text-right';
 		            			if (col.getAggregationValue() < 0) {
 		            				c += ' red';
 		            			}
 		            			return c;
 		            		},
 		            		cellFilter : 'number:2',
    					 },
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
	}

	$scope.post = function() {
		postIVAmounts();
	}

	function loadData() {
		getResourceSummaries();
	}
	
	function getResourceSummaries() {
		resourceSummaryService.getResourceSummaries($scope.jobNo, "", "")
		.then(
				function( data ) {
					$scope.nonFinalizedMovementAmount = 0;
					$scope.finalizedMovementAmount = 0;
					angular.forEach(data, function(value, key){
						value.ivMovement = value.currIVAmount - value.postedIVAmount;

					});

					$timeout(function () {
						$scope.postedIVAmount = $scope.gridApi.grid.columns[11].getAggregationValue();
						$scope.cumulativeIVAmount = $scope.gridApi.grid.columns[9].getAggregationValue();
						$scope.ivMovement = $scope.gridApi.grid.columns[10].getAggregationValue();
					}, 100);

					
					
					var filteredData = data.filter(function(obj) {
					    return (obj.ivMovement != 0);
					});
					$scope.gridOptions.data = filteredData;
					
				});
	}


	function postIVAmounts() {
		resourceSummaryService.postIVAmounts($scope.jobNo, false)
		.then(
				function( data ) {
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "IV has been posted.");
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
})
.directive('myCustomDropdown', function() {
	return {
		template: '<select class="form-control input-sm" ng-model="colFilter.term" ng-options="option.id as option.value for option in colFilter.options"></select>'
	};
});;