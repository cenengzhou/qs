mainApp.controller('EnquirySubcontractDetailCtrl', 
		['$scope', '$http', 'modalService', 'blockUI', 'subcontractService', 'uiGridConstants', 'GlobalHelper',
function($scope, $http, modalService, blockUI, subcontractService, uiGridConstants, GlobalHelper) {
	$scope.commonKeyValue = {};
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
					displayName : "Subcontract No."
				},
				{
					field : 'lineType',
					width : '100',
					displayName : "Line Type"
				},
				{
					field : 'billItem',
					width : '100',
					displayName : "Bill Item"
				},
				{
					field : 'description',
					width : '200',
					displayName : "Description"
				},
				{
					field : 'objectCode',
					width : '80',
					displayName : "Object"
				},
				{
					field : 'subsidiaryCode',
					width : '80',
					displayName : "Subsidiary"
				},
				{
					field : 'quantity',
					width : '100',
					displayName : "Quantity",
					cellFilter: 'number:4',
					cellClass: 'text-right',
					filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN'])
				},
				{
					field : 'scRate',
					width : '100',
					cellClass: 'text-right',
					cellFilter : 'number:4',
					displayName : "SC Rate"
				},
				{
					field : 'amountSubcontract',
					width : '150',
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
					
				},
				{
					field : 'costRate',
					width : '100',
					cellClass: 'text-right',
					cellFilter : 'number:4'
				},
				{
					field : 'amountBudget',
					width : '150',
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
					cellFilter : 'number:2'
				},
				{
					field : 'amountCumulativeCert',
					width : '150',
					displayName : "Cum. Cert. Amt",
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
					
				},
				{
					field : 'amountPostedCert',
					width : '150',
					displayName : "Posted Cert. Amt",
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
					cellFilter : 'number:2'
				},
				{
					field : 'cumWorkDoneQuantity',
					width : '150',
					displayName : "Cum. Work Done Qty",
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
					cellFilter : 'number:4'
				},
				{
					field : 'amountCumulativeWD',
					width : '150',
					displayName : "Cum. Work Done Amt",
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
					cellFilter : 'number:2'
				},
				{
					field : 'amountPostedWD',
					width : '150',
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
					cellFilter : 'number:2'
				},
				{
					field : 'ivamount',
					width : '150',
					displayName : "IV Amt",
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
					cellFilter : 'number:2'
				},
				{
					field : 'projectedProvision',
					width : '150',
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
					cellFilter : 'number:2'
				},
				{
					field : 'provision',
					width : '150',
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
					cellFilter : 'number:2'
				},
				{
					field : 'approved',
					width : '80',
					displayName : "Approved"
				},
				{
					field : 'unit',
					width : '60',
					displayName : "Unit"
				},
				{
					field : 'remark',
					width : '100',
					displayName : "Remark"
				},
				{
					field : 'sequenceNo',
					width : '80',
					displayName : "Sequence No"
				},
				{
					field : 'resourceNo',
					width : '80',
					displayName : "Resource No"
				}]
	};

	$scope.gridOptions.onRegisterApi = function(gridApi) {
		$scope.gridApi = gridApi;
	}

	$scope.loadGridData = function() {
		$scope.commonKeyValue['jobNo'] = $scope.jobNo;
		subcontractService
				.getSCDetailList($scope.commonKeyValue)
				.then(
						function(data) {
							if (angular.isArray(data)) {
								$scope.gridOptions.data = data;
							}
						},
						function(data) {
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