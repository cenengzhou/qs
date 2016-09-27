mainApp.controller('SubcontractDetailsCtrl', ['$scope' , 'subcontractService', 'uiGridConstants', 'uiGridGroupingConstants', 'GlobalHelper', '$filter',
                                              function($scope , subcontractService, uiGridConstants, uiGridGroupingConstants, GlobalHelper, $filter) {

	getSubcontract();
	getSCDetails();
	
	$scope.getExcludeRetentionMessage = function(){
		var msg = '';
		if($scope.subcontract && $scope.subcontract.retentionBalance > 0) {
			msg = 'excluded retention balance : ' + $filter('number')($scope.subcontract.retentionBalance);
		}
		return msg;
	}
	$scope.getExcludeRetentionValue = function(aggregationValue){
		var value = aggregationValue;
		if($scope.subcontract && $scope.subcontract.retentionBalance > 0) {
			value = aggregationValue - $scope.subcontract.retentionBalance;
		}
		return value;
	}
	$scope.totalAmountExcludeRetentionTemplate = '<div class="ui-grid-cell-contents" \
		title="{{grid.appScope.getExcludeRetentionMessage()}}">{{grid.appScope.getExcludeRetentionValue(col.getAggregationValue()) | number:2 }}\
		<span class="red small" ng-if="grid.appScope.getExcludeRetentionMessage().length > 0">*</span></div>';
	
	$scope.gridOptions = {
			enableSorting: true,
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableColumnMoving: true,
			showGridFooter : false,
			showColumnFooter : true,
			treeRowHeaderAlwaysVisible:true,
			exporterMenuPdf: false,
			groupingNullLabel: '',
			//Single Filter
			onRegisterApi: function(gridApi){
				$scope.gridApi = gridApi;
				  $scope.gridApi.grid.registerDataChangeCallback(function() {
			          $scope.gridApi.treeBase.expandAllRows();
			        });
			},

			columnDefs: [
			             { field : 'catalog',
			            	 width: 120,
			            	 displayName : 'Group',
			            	 enableCellEdit : false,
			            	 grouping: { groupPriority: 0 }, sort: { priority: 0, direction: 'asc' },
			            	 treeAggregation: {type: uiGridGroupingConstants.aggregation.SUM},
			            	 
			             },
			             { field: 'lineType', width: 50},
			             { field: 'billItem', width: 100},
			             { field: 'description', width: 100},
			             { field: 'quantity', width: 100, cellClass: 'text-right', cellFilter: 'number:4'},
			             //{field: 'toBeApprovedQuantity', width: 100},

			             {field: 'costRate', width: 80, cellClass: 'text-right', cellFilter: 'number:4'},
			             {field: 'scRate', width: 80, cellClass: 'text-right', cellFilter: 'number:4'},
			             //{field: 'toBeApprovedRate', width: 80},
			             {field: 'objectCode', width: 100},
			             {field: 'subsidiaryCode', width: 100},

			             {field: 'amountBudget', displayName: "Budget Amount", width: 150, cellClass: 'text-right', cellFilter: 'number:2',
			            	 cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
			     				var c = 'text-right';
			     				if (row.entity.amountBudget < 0) {
			     					c += ' red';
			     				}
			     				return c;
			     			},
						     footerCellTemplate : $scope.totalAmountExcludeRetentionTemplate,
						     footerCellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
						     					var c = 'text-right';
						     					if (col.getAggregationValue() < 0) {
						     						c += ' red';
						     					}
						     					return c;
						     				},
		     				treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
		     		        customTreeAggregationFinalizerFn: function(aggregation) {
		     		           aggregation.rendered = aggregation.value;
		     		         }
			             },
			             {field: 'amountSubcontract', displayName: "SC Amount", width: 150, cellClass: 'text-right', cellFilter: 'number:2',
			            	 cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
				     				var c = 'text-right';
				     				if (row.entity.amountSubcontract < 0) {
				     					c += ' red';
				     				}
				     				return c;
				     			},
							     footerCellTemplate : '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:2 }}</div>',
							     footerCellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
							     					var c = 'text-right';
							     					if (col.getAggregationValue() < 0) {
							     						c += ' red';
							     					}
							     					return c;
							     				},
			     				treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
			     		        customTreeAggregationFinalizerFn: function(aggregation) {
			     		           aggregation.rendered = aggregation.value;
			     		         }	 
			             
			             },

			             { field: 'amountCumulativeWD', displayName: "Cum WD Amount", width: 150, cellClass: 'text-right', cellFilter: 'number:2',
			            	 cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
				     				var c = 'text-right';
				     				if (row.entity.amountCumulativeWD < 0) {
				     					c += ' red';
				     				}
				     				return c;
				     			},
							     footerCellTemplate : '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:2 }}</div>',
							     footerCellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
							     					var c = 'text-right';
							     					if (col.getAggregationValue() < 0) {
							     						c += ' red';
							     					}
							     					return c;
							     				},
			     				treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
			     		        customTreeAggregationFinalizerFn: function(aggregation) {
			     		           aggregation.rendered = aggregation.value;
			     		         }	 
			             
			             },
			             
			             { field: 'amountPostedWD', displayName: "Posted WD Amount", width: 150, cellClass: 'text-right', cellFilter: 'number:2',
			            	 cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
				     				var c = 'text-right';
				     				if (row.entity.amountPostedWD < 0) {
				     					c += ' red';
				     				}
				     				return c;
				     			},
							     footerCellTemplate : '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:2 }}</div>',
							     footerCellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
							     					var c = 'text-right';
							     					if (col.getAggregationValue() < 0) {
							     						c += ' red';
							     					}
							     					return c;
							     				},
			     				treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
			     		        customTreeAggregationFinalizerFn: function(aggregation) {
			     		           aggregation.rendered = aggregation.value;
			     		         }	 
			             
			             },
			             
			             { field: 'amountCumulativeCert', displayName: "Cum Certified Amount", width: 150, cellClass: 'text-right', cellFilter: 'number:2',
			            	 cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
				     				var c = 'text-right';
				     				if (row.entity.amountCumulativeCert < 0) {
				     					c += ' red';
				     				}
				     				return c;
				     			},
							     footerCellTemplate : '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:2 }}</div>',
							     footerCellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
							     					var c = 'text-right';
							     					if (col.getAggregationValue() < 0) {
							     						c += ' red';
							     					}
							     					return c;
							     				},
			     				treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
			     		        customTreeAggregationFinalizerFn: function(aggregation) {
			     		           aggregation.rendered = aggregation.value;
			     		         }
			             },
			             
			             
			             { field: 'amountPostedCert', displayName: "Posted Certified Amount", width: 150, cellClass: 'text-right', cellFilter: 'number:2',
			            	 cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
				     				var c = 'text-right';
				     				if (row.entity.amountPostedCert < 0) {
				     					c += ' red';
				     				}
				     				return c;
				     			},
							     footerCellTemplate : '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:2 }}</div>',
							     footerCellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
							     					var c = 'text-right';
							     					if (col.getAggregationValue() < 0) {
							     						c += ' red';
							     					}
							     					return c;
							     				},
			     				treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
			     		        customTreeAggregationFinalizerFn: function(aggregation) {
			     		           aggregation.rendered = aggregation.value;
			     		         }

			             },
			             
			             

			             {field: 'projectedProvision', width: 150, cellClass: 'text-right', cellFilter: 'number:2',
			            	 cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
				     				var c = 'text-right';
				     				if (row.entity.projectedProvision < 0) {
				     					c += ' red';
				     				}
				     				return c;
				     			},
							     footerCellTemplate : '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:2 }}</div>',
							     footerCellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
							     					var c = 'text-right';
							     					if (col.getAggregationValue() < 0) {
							     						c += ' red';
							     					}
							     					return c;
							     				},
			     				treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
			     		        customTreeAggregationFinalizerFn: function(aggregation) {
			     		           aggregation.rendered = aggregation.value;
			     		         }
			             
			            },
			             {field: 'provision', width: 150, cellClass: 'text-right', cellFilter: 'number:2',
			            	 cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
				     				var c = 'text-right';
				     				if (row.entity.provision < 0) {
				     					c += ' red';
				     				}
				     				return c;
				     			},
							     footerCellTemplate : '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:2 }}</div>',
							     footerCellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
							     					var c = 'text-right';
							     					if (col.getAggregationValue() < 0) {
							     						c += ' red';
							     					}
							     					return c;
							     				},
			     				treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
			     		        customTreeAggregationFinalizerFn: function(aggregation) {
			     		           aggregation.rendered = aggregation.value;
			     		         }
			             },
			             

			             {field: 'altObjectCode', width: 100},


			             {field: 'approved', width: 100},
			             {field: 'unit', width: 100},

			             {field: 'remark', width: 100},
			             {field: 'contraChargeSCNo', width: 100},
			             {field: 'sequenceNo', width: 100},
			             {field: 'resourceNo', width: 100},
			             {field: 'balanceType', width: 100}
			             ]

			
	};

	function getSubcontract(){
		subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					$scope.subcontract = data;
				});
	}
	
	function getSCDetails() {
		subcontractService.getSCDetails($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					$scope.gridOptions.data = data;
					GlobalHelper.addSubcontractCatalogGroup(data, 'lineType');
				});
	}

}]);