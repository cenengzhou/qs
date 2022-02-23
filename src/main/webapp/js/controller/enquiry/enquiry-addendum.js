
mainApp.controller('EnquiryAddendumCtrl', ['$scope', '$http', 'modalService', 'blockUI', 'GlobalParameter', 'addendumService', 'uiGridConstants', '$timeout', 'GlobalHelper', '$state',
                                      function($scope, $http, modalService, blockUI, GlobalParameter, addendumService, uiGridConstants, $timeout, GlobalHelper, $state) {
	$scope.currentDate = new Date(); // Default: Today
	$scope.searchPeriod = moment().format('YYYY-MM');
	$scope.GlobalParameter = GlobalParameter;
	$scope.commonKeyValue = {};
	$scope.commonKeyValue['jobNo'] = $scope.jobNo;
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
		    rowTemplate: '<div style="cursor:pointer" ng-click="grid.appScope.viewAddendumDetails(row.entity)">' +
				'  <div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }"  ui-grid-cell></div>' +
				'</div>',
			columnDefs: [
			             { field: 'noJob', width:'90', displayName: "Job No", enableCellEdit: false},
			             { field: 'no', width:'140', displayName: "Addendum No", enableCellEdit: false},
			             { field: 'noSubcontract', width:'140', displayName: "Subcontract No", enableCellEdit: false},
			             { field: 'descriptionSubcontract', width:'200', displayName: "Subcontract Description", enableCellEdit: false},
				         { field: 'noSubcontractor', width:'160', displayName: "Subcontractor No", enableCellEdit: false},
						 { field: 'nameSubcontractor', width:'180', displayName: "Subcontractor Name", enableCellEdit: false},
			             { field: 'title', width:'180', displayName: "Title", enableCellEdit: false},
			             { field: 'amtAddendum', width:'180', displayName: "Addendum Amount",
			            	filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
			            	cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            			var c = 'text-right';
			            			if (row.entity.amtAddendum < 0) {
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
					     { field: 'amtAddendumTotal', width:'200', displayName: "Addendum Total Amount",
							filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
							cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
								var c = 'text-right';
								if (row.entity.amtAddendumTotal < 0) {
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
						{ field: 'amtAddendumTotalTba', width:'250', displayName: "Addendum Total TBA Amount",
							filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
							cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
								var c = 'text-right';
								if (row.entity.amtAddendumTotalTba < 0) {
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
						{ field: 'amtSubcontractRemeasured', width:'250', displayName: "Subcontract Remeasured Amount",
							filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
							cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
								var c = 'text-right';
								if (row.entity.amtSubcontractRemeasured < 0) {
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
						{ field: 'amtSubcontractRevised', width:'250', displayName: "Subcontract Revised Amount",
							filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
							cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
								var c = 'text-right';
								if (row.entity.amtSubcontractRevised < 0) {
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
						{ field: 'amtSubcontractRevisedTba', width:'250', displayName: "Subcontract Revised TBA Amount",
							filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
							cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
								var c = 'text-right';
								if (row.entity.amtSubcontractRevisedTba < 0) {
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
						{ field: 'recoverableAmount', width:'120', displayName: "Recoverable",
							filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
							cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
								var c = 'text-right';
								if (row.entity.recoverableAmount < 0) {
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
						{ field: 'nonRecoverableAmount', width:'150', displayName: "Non-Recoverable",
							filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
							cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
								var c = 'text-right';
								if (row.entity.nonRecoverableAmount < 0) {
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
						{ field: 'status', width:'100', displayName: "Status", enableCellEdit: false},
						{ field: 'usernamePreparedBy', width:'120', displayName: "Prepared By", enableCellEdit: false},
						{ field: 'remarks', width:'180', displayName: "Remarks", enableCellEdit: false},
						{ field: 'statusApproval', width:'140', displayName: "Approval Status", enableCellEdit: false},
						{ field: 'dateApproval', width:'140', displayName: 'Approval Date', cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"',
							filterCellFiltered:true,  enableCellEdit: false},
						{ field: 'dateSubmission', width:'160', displayName: 'Submission Date', cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"',
							filterCellFiltered:true,  enableCellEdit: false}
            			 ]
	};

	$scope.viewAddendumDetails = function(entity) {
		modalService.open('xxlg', 'view/enquiry/modal/enquiry-addendumdetails.html', 'EnquiryAddendumDetailsCtrl', 'Success', entity);
	}
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	$scope.loadGridData = function(){
		if(!$scope.commonKeyValue['jobNo']){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'Please enter Job No' );
			return;
		}
		addendumService.enquireAddendumList($scope.commonKeyValue['jobNo'], $scope.commonKeyValue)
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
	
}]);