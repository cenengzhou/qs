mainApp.controller('EnquirySubcontractDetailCtrl', 
		['$scope', '$http', 'modalService', 'blockUI', 'subcontractService', 'uiGridConstants', 'GlobalHelper',
function($scope, $http, modalService, blockUI, subcontractService, uiGridConstants, GlobalHelper) {

//	$scope.blockEnquirySubcontractDetail = blockUI.instances.get('blockEnquirySubcontractDetail');

	$scope.gridOptions = {
		enableFiltering : true,
		enableColumnResizing : true,
		enableGridMenu : true,
		enableRowSelection : true,
		enableSelectAll : true,
		enableFullRowSelection : false,
		multiSelect : true,
		showGridFooter : true,
		showColumnFooter : true,
		enableCellEditOnFocus : false,
		allowCellFocus : false,
		enableCellSelection : false,
		exporterMenuPdf: false,
		columnDefs : [
				{
					field : 'subcontract.packageNo',
					width : '100',
					displayName : "Subcontract No.",
					enableCellEdit : false
				},
				{
					field : 'sequenceNo',
					width : '100',
					displayName : "Sequence No",
					enableCellEdit : false
				},
				{
					field : 'resourceNo',
					width : '100',
					displayName : "Resource No",
					enableCellEdit : false
				},
				{
					field : 'billItem',
					width : '100',
					displayName : "Bill Item",
					enableCellEdit : false
				},
				{
					field : 'description',
					width : '200',
					displayName : "Description",
					enableCellEdit : false
				},
				{
					field : 'scRate',
					width : '100',
					cellFilter : 'number:2',
					displayName : "SC Rate",
					enableCellEdit : false
				},
				{
					field : 'objectCode',
					width : '100',
					displayName : "Object",
					enableCellEdit : false
				},
				{
					field : 'subsidiaryCode',
					width : '100',
					displayName : "Subsidiary",
					enableCellEdit : false
				},
				{
					field : 'lineType',
					width : '100',
					displayName : "Line Type",
					enableCellEdit : false
				},
				{
					field : 'approved',
					width : '100',
					displayName : "Approved",
					enableCellEdit : false
				},
				{
					field : 'unit',
					width : '100',
					displayName : "Unit",
					enableCellEdit : false
				},
				{
					field : 'remark',
					width : '100',
					displayName : "Remark",
					enableCellEdit : false
				},
				{
					field : 'postedCertifiedQuantity',
					width : '100',
					displayName : "Posted Certified Qty",
					filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
					cellClass : function(grid, row,
							col, rowRenderIndex,
							colRenderIndex) {
						var c = 'text-right';
						if (row.entity.postedCertifiedQuantity < 0) {
							c += ' red';
						}
						return c;
					},
					aggregationHideLabel : true,
					aggregationType : uiGridConstants.aggregationTypes.sum,
					footerCellTemplate : '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:2 }}</div>',
					footerCellClass : function(grid,
							row, col, rowRenderIndex,
							colRenderIndex) {
						var c = 'text-right';
						if (col.getAggregationValue() < 0) {
							c += ' red';
						}
						return c;
					},
					cellFilter : 'number:2',
					enableCellEdit : false
				},
				{
					field : 'cumCertifiedQuantity',
					width : '100',
					displayName : "Cumulative Certified Qty",
					filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
					cellClass : function(grid, row,
							col, rowRenderIndex,
							colRenderIndex) {
						var c = 'text-right';
						if (row.entity.cumCertifiedQuantity < 0) {
							c += ' red';
						}
						return c;
					},
					aggregationHideLabel : true,
					aggregationType : uiGridConstants.aggregationTypes.sum,
					footerCellTemplate : '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:2 }}</div>',
					footerCellClass : function(grid,
							row, col, rowRenderIndex,
							colRenderIndex) {
						var c = 'text-right';
						if (col.getAggregationValue() < 0) {
							c += ' red';
						}
						return c;
					},
					cellFilter : 'number:2',
					enableCellEdit : false
				},
				{
					field : 'amountCumulativeCert',
					width : '100',
					displayName : "Cumulative Certified Amt",
					filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
					cellClass : function(grid, row,
							col, rowRenderIndex,
							colRenderIndex) {
						var c = 'text-right';
						if (row.entity.amountCumulativeCert < 0) {
							c += ' red';
						}
						return c;
					},
					aggregationHideLabel : true,
					aggregationType : uiGridConstants.aggregationTypes.sum,
					footerCellTemplate : '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:2 }}</div>',
					footerCellClass : function(grid,
							row, col, rowRenderIndex,
							colRenderIndex) {
						var c = 'text-right';
						if (col.getAggregationValue() < 0) {
							c += ' red';
						}
						return c;
					},
					cellFilter : 'number:2',
					enableCellEdit : false
				},
				{
					field : 'amountPostedCert',
					width : '100',
					displayName : "Posted Certified Amt",
					filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
					cellClass : function(grid, row,
							col, rowRenderIndex,
							colRenderIndex) {
						var c = 'text-right';
						if (row.entity.amountPostedCert < 0) {
							c += ' red';
						}
						return c;
					},
					aggregationHideLabel : true,
					aggregationType : uiGridConstants.aggregationTypes.sum,
					footerCellTemplate : '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:2 }}</div>',
					footerCellClass : function(grid,
							row, col, rowRenderIndex,
							colRenderIndex) {
						var c = 'text-right';
						if (col.getAggregationValue() < 0) {
							c += ' red';
						}
						return c;
					},
					cellFilter : 'number:2',
					enableCellEdit : false
				},
				{
					field : 'amountCumulativeWD',
					width : '100',
					displayName : "Cumulative Work Done Amt",
					filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
					cellClass : function(grid, row,
							col, rowRenderIndex,
							colRenderIndex) {
						var c = 'text-right';
						if (row.entity.amountCumulativeWD < 0) {
							c += ' red';
						}
						return c;
					},
					aggregationHideLabel : true,
					aggregationType : uiGridConstants.aggregationTypes.sum,
					footerCellTemplate : '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:2 }}</div>',
					footerCellClass : function(grid,
							row, col, rowRenderIndex,
							colRenderIndex) {
						var c = 'text-right';
						if (col.getAggregationValue() < 0) {
							c += ' red';
						}
						return c;
					},
					cellFilter : 'number:2',
					enableCellEdit : false
				},
				{
					field : 'amountPostedWD',
					width : '100',
					displayName : "Posted Work Done Amt",
					filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
					cellClass : function(grid, row,
							col, rowRenderIndex,
							colRenderIndex) {
						var c = 'text-right';
						if (row.entity.amountPostedWD < 0) {
							c += ' red';
						}
						return c;
					},
					aggregationHideLabel : true,
					aggregationType : uiGridConstants.aggregationTypes.sum,
					footerCellTemplate : '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:2 }}</div>',
					footerCellClass : function(grid,
							row, col, rowRenderIndex,
							colRenderIndex) {
						var c = 'text-right';
						if (col.getAggregationValue() < 0) {
							c += ' red';
						}
						return c;
					},
					cellFilter : 'number:2',
					enableCellEdit : false
				},
				{
					field : 'quantity',
					width : '100',
					displayName : "Quantity",
					cellFilter: 'number:2',
					cellClass: 'text-right',
					filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
					enableCellEdit : false
				},
				{
					field : 'newQuantity',
					width : '100',
					cellFilter : 'number:2',
					cellClass: 'text-right',
					displayName : "New Quantity",
					filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
					enableCellEdit : false
				},
				{
					field : 'originalQuantity',
					width : '100',
					cellFilter : 'number:2',
					cellClass: 'text-right',
					displayName : "Original Qty",
					filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
					enableCellEdit : false
				},
				// { field: 'tenderAnalysisDetail_ID',
				// width:'100', displayName: "Name",
				// enableCellEdit: false },
				{
					field : 'amountSubcontract',
					width : '100',
					displayName : "Subcontract Amt",
					filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
					cellClass : function(grid, row,
							col, rowRenderIndex,
							colRenderIndex) {
						var c = 'text-right';
						if (row.entity.amountSubcontract < 0) {
							c += ' red';
						}
						return c;
					},
					aggregationHideLabel : true,
					aggregationType : uiGridConstants.aggregationTypes.sum,
					footerCellTemplate : '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:2 }}</div>',
					footerCellClass : function(grid,
							row, col, rowRenderIndex,
							colRenderIndex) {
						var c = 'text-right';
						if (col.getAggregationValue() < 0) {
							c += ' red';
						}
						return c;
					},
					cellFilter : 'number:2',
					enableCellEdit : false
				},
				{
					field : 'amountBudget',
					width : '100',
					displayName : "Budget Amt",
					filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
					cellClass : function(grid, row,
							col, rowRenderIndex,
							colRenderIndex) {
						var c = 'text-right';
						if (row.entity.amountBudget < 0) {
							c += ' red';
						}
						return c;
					},
					aggregationHideLabel : true,
					aggregationType : uiGridConstants.aggregationTypes.sum,
					footerCellTemplate : '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:2 }}</div>',
					footerCellClass : function(grid,
							row, col, rowRenderIndex,
							colRenderIndex) {
						var c = 'text-right';
						if (col.getAggregationValue() < 0) {
							c += ' red';
						}
						return c;
					},
					cellFilter : 'number:2',
					enableCellEdit : false
				}/*,
				{
					field : 'amountSubcontractTBA',
					width : '100',
					displayName : "Subcontract TBA Amt",
					filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
					cellClass : function(grid, row,
							col, rowRenderIndex,
							colRenderIndex) {
						var c = 'text-right';
						if (row.entity.amountSubcontractTBA < 0) {
							c += ' red';
						}
						return c;
					},
					aggregationHideLabel : true,
					aggregationType : uiGridConstants.aggregationTypes.sum,
					footerCellTemplate : '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:2 }}</div>',
					footerCellClass : function(grid,
							row, col, rowRenderIndex,
							colRenderIndex) {
						var c = 'text-right';
						if (col.getAggregationValue() < 0) {
							c += ' red';
						}
						return c;
					},
					cellFilter : 'number:2',
					enableCellEdit : false
				}*/ ]
	};

	$scope.gridOptions.onRegisterApi = function(gridApi) {
		$scope.gridApi = gridApi;
	}

	$scope.loadGridData = function() {
//		$scope.blockEnquirySubcontractDetail.start('Loading...');
		subcontractService
				.getSCDetailList($scope.jobNo)
				.then(
						function(data) {
							if (angular.isArray(data)) {
								$scope.gridOptions.data = data;
							}
//							$scope.blockEnquirySubcontractDetail.stop();
						},
						function(data) {
//							$scope.blockEnquirySubcontractDetail.stop();
							modalService
									.open(
											'md',
											'view/message-modal.html',
											'MessageModalCtrl',
											'Fail',
											data);
						});
	}

	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	$scope.loadGridData();

} ]);