
mainApp.controller('EnquirySubcontractCtrl', ['$scope', '$http', 'modalService', 'blockUI', 'GlobalParameter', 'subcontractService', 'uiGridConstants', '$timeout', 'GlobalHelper', '$state',
                                      function($scope, $http, modalService, blockUI, GlobalParameter, subcontractService, uiGridConstants, $timeout, GlobalHelper, $state) {
	$scope.currentDate = new Date(); // Default: Today
	$scope.searchPeriod = moment().format('YYYY-MM');
	$scope.GlobalParameter = GlobalParameter;
	$scope.commonKeyValue = {};
	$scope.commonKeyValue['jobInfo.jobNumber'] = $scope.jobNo;
	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			enableFullRowSelection: false,
			multiSelect: true,
			showGridFooter : true,
			showColumnFooter: true,
			enableCellEditOnFocus : false,
			allowCellFocus: false,
			enableCellSelection: false,
			exporterMenuPdf: false,
			columnDefs: [
			             { field: 'jobInfo.company', width:'80', displayName: "Company", enableCellEdit: false },
			             { field: 'jobInfo.division', width:'80', displayName: "Division", enableCellEdit: false },
			             { field: 'jobInfo.jobNo', width:'60', displayName: "Job", enableCellEdit: false},
			             { field: 'jobInfo.completionStatus', width:'60', displayName: "Completion Status", enableCellEdit: false},
			             { field: 'jobInfo.soloJV', width:'80', displayName: "Solo/JV", enableCellEdit: false},
			             { field: 'jobInfo.jvPercentage', width:'60', displayName: "JV%", enableCellEdit: false},
			             { field: 'jobInfo.employer', width:'60', displayName: "Client No", enableCellEdit: false},
			             { field: 'packageNo', width:'60', displayName: "Subcontract", enableCellEdit: false},
			             { field: 'vendorNo', width:'80', displayName: "Subcontractor No.", enableCellEdit: false}, // TODO:ADL
			             { field: 'nameSubcontractor', width:'180', displayName: "Subcontractor Name", enableCellEdit: false}, // TODO:ADL
			             { field: 'description', width:'180', displayName: "Description", enableCellEdit: false},
			             { field: 'paymentCurrency', width:'60', displayName: "Currency", enableCellEdit: false},
			             { field: 'splitTerminateStatusText', width:'110', displayName: "Split Terminate Status", enableCellEdit: false},
			             { field: 'paymentStatusText', width:'80', displayName: "Payment Status", enableCellEdit: false},
			             { field: 'paymentTerms', width:'80', displayName: "Payment Terms", enableCellEdit: false},
			             { field: 'subcontractTerm', width:'120', displayName: "Subcontract Terms", enableCellEdit: false},
			             { field: 'subcontractorNature', width:'100', displayName: "Subcontractor Nature", enableCellEdit: false},
			             { field: 'originalSubcontractSum', width:'120', displayName: "Original Subcontract Sum", 
			            	 filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
			            	 cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            			var c = 'text-right';
			            			if (row.entity.originalSubcontractSum < 0) {
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
			            	 enableCellEdit: false},
			             { field: 'remeasuredSubcontractSum', width:'160', displayName: "Remeasured Subcontract Sum", 
			            		 filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
			            		 cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
				            			var c = 'text-right';
				            			if (row.entity.remeasuredSubcontractSum < 0) {
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
			            		 enableCellEdit: false
			            }, 
			             { field: 'approvedVOAmount', width:'120', displayName: "Addendum", 
			            	filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
			            	cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            			var c = 'text-right';
			            			if (row.entity.approvedVOAmount < 0) {
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
			            	enableCellEdit: false
			            },
			             { field: 'subcontractSum', width:'160', displayName: "Revised Subcontract Sum", 
			            	filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
			            	cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            			var c = 'text-right';
			            			if (row.entity.subcontractSum < 0) {
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

			            	enableCellEdit: false
		            	}, //TODO: on screen
			             { field: 'accumlatedRetention', width:'130', displayName: "Accumlated Retention (RT)", 
		            		filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']), 
		            		cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            			var c = 'text-right';
			            			if (row.entity.accumlatedRetention < 0) {
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
		            		enableCellEdit: false
		            	},
			             { field: 'retentionReleased', width:'130', displayName: "Retention Released (RR+RA)", 
		            		filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
		            		cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            			var c = 'text-right';
			            			if (row.entity.retentionReleased < 0) {
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
		            		enableCellEdit: false
		            	},
			             { field: 'retentionBalance', width:'120', displayName: "Retention Balance", 
		            		filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
		            		cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            			var c = 'text-right';
			            			if (row.entity.retentionBalance < 0) {
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
		            		enableCellEdit: false
		            		},	// TODO: on screen
			             { field: 'totalNetPostedCertifiedAmount', width:'120', displayName: "Net Certified (excl. RT, CC)", 
		            			filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
		            			cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
				            			var c = 'text-right';
				            			if (row.entity.totalNetPostedCertifiedAmount < 0) {
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
		            			enableCellEdit: false
            			},	//TODO: on screen
			             { field: 'totalPostedCertifiedAmount', width:'120', displayName: "Posted Certified", 
            				filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
            				cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
		            			var c = 'text-right';
		            			if (row.entity.totalPostedCertifiedAmount < 0) {
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
            				enableCellEdit: false
            			},
			             { field: 'totalCumCertifiedAmount', width:'120', displayName: "Cumulative Certified", 
            				filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
            				cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
		            			var c = 'text-right';
		            			if (row.entity.totalCumCertifiedAmount < 0) {
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
            				enableCellEdit: false
            			},
			             { field: 'totalCumWorkDoneAmount', width:'120', displayName: "Cumulative WorkDone", 
            				filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
            				cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
		            			var c = 'text-right';
		            			if (row.entity.totalCumWorkDoneAmount < 0) {
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
            				enableCellEdit: false
        				},
			             { field: 'totalProvisionAmount', width:'120', displayName: "Provision", 
        					filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
        					cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
		            			var c = 'text-right';
		            			if (row.entity.totalProvisionAmount < 0) {
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
        					enableCellEdit: false
        				},	//TODO: on screen
			             { field: 'balanceToCompleteAmount', width:'120', displayName: "Balance to Complete", 
        					filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
        					cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
		            			var c = 'text-right';
		            			if (row.entity.balanceToCompleteAmount < 0) {
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

        					enableCellEdit: false
        					}, // TODO: on screen
			             { field: 'totalCCPostedCertAmount', width:'120', displayName: "Contra Charge", 
        						filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
        						cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
 		            			var c = 'text-right';
 		            			if (row.entity.totalCCPostedCertAmount < 0) {
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
        						enableCellEdit: false
        					},
        					{ field: 'totalMOSPostedCertAmount', width:'120', displayName: "Material On Site", 
        						filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
        						cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
        							var c = 'text-right';
        							if (row.entity.totalMOSPostedCertAmount < 0) {
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

        						enableCellEdit: false
        					},
        					{ field: 'totalAPPostedCertAmount', width:'120', displayName: "Advanced Payment", 
        						filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
        						cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
        							var c = 'text-right';
        							if (row.entity.totalAPPostedCertAmount < 0) {
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

        						enableCellEdit: false
        					},
							{ field: 'totalRecoverableAmount', width:'120', displayName: "Recoverable",
								filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
								cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
									var c = 'text-right';
									if (row.entity.totalRecoverableAmount < 0) {
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

								enableCellEdit: false
							},
							{ field: 'totalNonRecoverableAmount', width:'120', displayName: "Non-Recoverable",
								filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
								cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
									var c = 'text-right';
									if (row.entity.totalNonRecoverableAmount < 0) {
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

        						enableCellEdit: false
        					},
							{ field: 'internalJobNo', width:'120', displayName: "Internal Job No", enableCellEdit: false},
							{ field: 'workscope', width:'120', displayName: "Workscope", enableCellEdit: false}
            			 ]
	};
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	$scope.loadGridData = function(){
		if(!$scope.commonKeyValue['jobInfo.jobNumber'] && !$scope.commonKeyValue['jobInfo.company'] && !$scope.commonKeyValue['jobInfo.division']){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'Please enter Job No., Company or Division' ); 
			return;
		}
		$scope.searchYear = $scope.searchPeriod.substring(0,4);
		$scope.searchMonth = $scope.searchPeriod.substring(5,7);
		$scope.awardOnly = true;
		$scope.commonKeyValue['packageType'] = 'S';
		subcontractService.getSubcontractSnapshotList($scope.searchYear, $scope.searchMonth, $scope.awardOnly, $scope.commonKeyValue)
		.then(function(data){
				$scope.gridOptions.data = data;
			}, function(data){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data ); 
			})
	}
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	$scope.loadGridData();
	
	$scope.recalculateSCSummary = function (){
		subcontractService.calculateTotalWDandCertAmount($scope.commonKeyValue['jobInfo.jobNumber'], '', true)
		.then(
				function( data ) {
					if(data){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Subcontract Summary has been recalculated.");
						$state.reload();
					}
					else
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Subcontract Summary cannot be recalculated.");
					
				});
	}
	
}]);