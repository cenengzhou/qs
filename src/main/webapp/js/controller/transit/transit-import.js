mainApp.controller('TransitImportCtrl', ['$scope', 'modalService', 'transitService', '$cookies', 'transitService', 'confirmService', '$window', 'rootscopeService', 'uiGridGroupingConstants', 'uiGridConstants', '$location',
                          function($scope, modalService, transitService, $cookies, transitService, confirmService, $window, rootscopeService, uiGridGroupingConstants, uiGridConstants, $location) {
	rootscopeService.setSelectedTips('');
	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");
	
    loadData();
	
    function loadData(){
    	 getTransit();
    	 if($location.path().indexOf("/BQ")>0){
    	        loadBqItems();    		 
    	 }else if($location.path().indexOf("/resource")){
    	        loadResources();    		 
    	 }
    }
    
    
    function getTransit(){
    	$scope.disableImportBQ = true;
    	$scope.disableImportResources = true;
    	$scope.disableConfirmResouces = true;
    	$scope.disablePrintReport = true;
    	$scope.disableCompleteTransit = true;
    	$scope.resourceEdit = true;
    	
    	transitService.getTransit($scope.jobNo)
    	.then(function(data){
	    	$scope.transit = data;
	    	$scope.$emit("UpdatedTransitStatus", data.status);
	    	if(data instanceof Object) {
	    		if($scope.transit.status === 'Header Created'){
	    			$scope.disableImportBQ = false;
	    			$scope.disableImportResources = false;
	    		} else if($scope.transit.status === 'BQ Items Imported'){
	    			$scope.disableImportBQ = false;
	    			$scope.disableImportResources = false;
	    		} else if($scope.transit.status === 'Resources Imported' || $scope.transit.status === 'Resources Updated') {
	    			$scope.disableImportBQ = false;
	    			$scope.disableImportResources = false;
	    			$scope.disableConfirmResouces = false;
	    		} else if($scope.transit.status === 'Resources Confirmed') {
	    			$scope.disableImportBQ = false;
	    			$scope.disableImportResources = false;
	    			$scope.disableConfirmResouces = false;
	    			$scope.disablePrintReport = false;
	    		} else if($scope.transit.status === 'Report Printed') {
	    			$scope.disableImportBQ = false;
	    			$scope.disableImportResources = false;
	    			$scope.disableConfirmResouces = false;
	    			$scope.disablePrintReport = false;
	    			$scope.disableCompleteTransit = false;
	    		}else if($scope.transit.status === 'Transit Completed') {
	    			$scope.resourceEdit = false;
	    			$scope.disablePrintReport = false;
	    		}
	    	}

    	}, function(data){
	    	modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Error:" + data.replace('<br/>', '\n'));
	    })
    }
    
   
    
    $scope.onSubmitTransitUpload = function(item, type){
    	if(item.files[0] != null){
        	var filename = item.files[0].name.split('.').pop();
        	
        	if(filename != 'xlsx' && filename != 'xls'){
        		modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'Please select file with extension .xlsx or .xls for import.');
        		return;
        	}
        	
        	
    		var formData = new FormData();
    		formData.append('files', item.files[0]);
    		formData.append('type', type);
    		formData.append('jobNumber', $scope.jobNo);
    		
    		transitService.transitUpload(formData)
    		.then(function(data){
    			item.value = null;
    			var msg = data;
    			var message = '';
    			var status = '';
    			var typeStr = '';
    			getTransit();
    			if(type === 'BQ'){
    				loadBqItems();
    				typeStr = 'BQ Items';
    			} else if(type === 'Resource'){
    				loadResources();
    				typeStr = 'Resources';
    			}
    			
    			var url='service/report/transitDownload?type=';
    			var showDialog = false;
    			
    			if(msg.success){
    				if(msg.haveWarning){
    					message = msg.numRecordImported + ' ' + typeStr + ' have been imported with warning. '
    					message += 'Would you like to download the warning report?';
    					status = 'Warn';
    					url+= 'SUCCESS_WITH_WARNING';
    					showDialog = true;
    				} else {
    					message = msg.numRecordImported + ' ' + typeStr + ' imported successfully.'
    					status = 'Success';
    					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', status, message);
    				}
    			} else {
    				message = 'Failed to import ' + typeStr + '. Would you like to download the error report?';
    				status = "Fail";
    				url+='TERROR';
    				showDialog = true;
    			}
    			if(showDialog)
    			confirmService.show({}, {bodyText:message})
    			.then(function(response){
    				if(response === 'Yes') {
    					$window.open(url, 'transitDownload', '_blank');
    				}
    			});
    			
    		}, function(data){
    			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data.replace('<br/>', '\n') );
    		});
    	}
		
    }
   
    
    
    function loadBqItems(){
		transitService.getTransitBQItems($scope.jobNo)
		.then(function(data) {
			$scope.addBpiColumnData(data);
			$scope.bqGridOptions.data = data;
		});
    }
 
    function loadResources(){
		transitService.getTransitResources($scope.jobNo)
		.then(function(data) {
			$scope.addBpiColumnData(data);
			$scope.resourcesGridOptions.data = data;
		});
    }
    
    $scope.addBpiColumnData = function(data){
    	data.forEach(function(d){
    		d.bpiString = bpiString;
    	})
    }
    
    function bpiString (type){
    	var obj = this;
    	if(type === 'Resource'){
    		obj = this.transitBpi;
    	}
    	var bpi = '';
    	bpi += obj.billNo === null ? '/' : obj.billNo + '/';
		bpi += obj.subBillNo === null ? '//' : obj.subBillNo + '//';
		bpi += obj.pageNo == null ? '/' : obj.pageNo + '/';
		bpi += obj.itemNo == null ? '' : obj.itemNo;
		return bpi;
    }
    
    $scope.resourceApplyTo = 0;
    $scope.resourceApplyOptions = [{id:0, value:'Object Code', length:6, regex:'\\d{6}'}, {id:1, value:'Subsidiary Code', length:8, regex:'\\d{8}'}];
    
    $scope.onResourceApplyTo = function(){
    	if($scope.resourceApplyToText.length != $scope.resourceApplyOptions[resourceApplyTo].length){
    		modalService
    	}
    }
    
    $scope.bqGridOptions = {
    		enableFiltering : true,
    		enableColumnResizing : true,
    		enableGridMenu : true,
    		enableRowSelection : true,
    		enableSelectAll : true,
    		enableFullRowSelection : false,
    		multiSelect : true,
    		showGridFooter : true,
    		showColumnFooter: true,
    		groupingNullLabel: '',
    		enableCellEditOnFocus : false,
    		enablePaginationControls : true,
    		allowCellFocus : false,
    		exporterMenuPdf: false,
    		enableCellSelection : false,
    		columnDefs : [ 
    			{
    				field : 'billNo',
    				displayName : 'Bill No.',
    				enableCellEdit : false,
    				//grouping: { groupPriority: 0 }, sort: { priority: 0, direction: 'asc' },
    			},{
    				field : 'subBillNo',
    				displayName : 'Sub-Bill No.',
    				enableCellEdit : false,
    				//grouping: { groupPriority: 1 }, sort: { priority: 1, direction: 'asc' }
    			},{
    				field : 'pageNo',
    				displayName : 'Page No.',
    				enableCellEdit : false,
    				//grouping: { groupPriority: 2 }, sort: { priority: 2, direction: 'asc' },    			
    			},{
    				field : 'itemNo',
    				displayName : 'Item No.',
    				enableCellEdit : false,
    				//grouping: { groupPriority: 3 }, sort: { priority: 3, direction: 'asc' },

    			},
    			{
    				field : 'bpiString()',
    				displayName : 'Bill Item',
    				enableCellEdit : false,
    			}, 
    			{
    				field : 'description',
    				displayName : 'Description',
    				enableCellEdit : false,
    				width: 250,
    				cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    					if (row.entity.unit === null) {
    						return 'blue';
    					}
    				}

    			}, {
    				field : 'unit',
    				displayName : 'Unit',
    				enableCellEdit : false,
    			}, {
    				field : 'quantity',
    				displayName : 'Quantity',
    				cellFilter : 'number:3',
    				cellClass: 'text-right',
    				enableCellEdit : false,
    				width: 180
    			}, {
    				field : 'sellingRate',
    				displayName : 'Selling Rate',
    				cellFilter : 'number:2',
    				cellClass: 'text-right',
    				enableCellEdit : false,
    				width: 180
    			}, {
    				field : 'value',
    				displayName : 'Value',
    				width: 180,
    				enableCellEdit : false,
    				cellFilter : 'number:2',
    				cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
    					var c = 'text-right';
    					if (row.entity.value < 0) {
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
    				aggregationType: uiGridConstants.aggregationTypes.sum
    			},
    			]
    };
    
	$scope.bqGridOptions.onRegisterApi = function (gridApi) {
		  $scope.bqGridApi = gridApi;
		  /*$scope.bqGridApi.grid.registerDataChangeCallback(function() {
	          $scope.bqGridApi.treeBase.expandAllRows();
	        });*/

//		  gridApi.grid.refresh();
	};
        
    $scope.resourcesGridOptions = {
    		enableFiltering : true,
    		enableColumnResizing : true,
    		enableGridMenu : true,
    		enableRowSelection : true,
    		enableSelectAll : true,
    		enableFullRowSelection : false,
    		multiSelect : true,
    		showGridFooter : true,
    		showColumnFooter: true,
    		groupingNullLabel: '',
    		enablePaginationControls : true,
    		allowCellFocus : false,
    		enableCellSelection : false,
    		enableCellEdit : false,
    		enableCellEditOnFocus : true,
    		exporterMenuPdf: false,
    		cellEditableCondition: function($scope) {
    			return $scope.row.entity.transitBpi.transit.status !== 'Transit Completed';
    		},
    		columnDefs : [ 
    		{
    			field : 'bpiString("Resource")',
    			displayName : 'B/P/I',
    			enableCellEdit : false,
    			
    		},
    		{
    			field : 'transitBpi.billNo',
    			displayName : 'Bill No.',
    			enableCellEdit : false,
    			//grouping: { groupPriority: 0 }, sort: { priority: 0, direction: 'asc' },
    		},{
    			field : 'transitBpi.subBillNo',
    			displayName : 'Sub-Bill No.',
    			enableCellEdit : false,
    			//grouping: { groupPriority: 1 }, sort: { priority: 1, direction: 'asc' },
    		},{
    			field : 'transitBpi.pageNo',
    			displayName : 'Page No.',
    			enableCellEdit : false,
    			//grouping: { groupPriority: 2 }, sort: { priority: 2, direction: 'asc' },
    		},{
    			field : 'transitBpi.itemNo',
    			displayName : 'Item No.',
    			enableCellEdit : false,
    			//grouping: { groupPriority: 3 }, sort: { priority: 3, direction: 'asc' },

    		},
    		{
    			field : 'type',
    			displayName : 'Type',
    			enableCellEdit : false,
    			
    		}, {
    			field : 'resourceCode',
    			displayName : 'Resource Code',
    			enableCellEdit : false,
    			
    		}, {
    			field : 'objectCode',
    			displayName : 'Object Code',
    			enableCellEdit : true,
    			cellClass : function(){
    				if($scope.resourceEdit === true) return 'text-primary';
    			}    			
    		}, {
    			field : 'subsidiaryCode',
    			displayName : 'Subsidiary Code',
    			enableCellEdit : true,
    			cellClass : function(){
    				if($scope.resourceEdit === true) return 'text-primary';
    			}    	    			
    		}, {
    			field : 'description',
    			displayName : "Description",
    			enableCellEdit : true,
    			width: 130,
    			cellClass : function(){
    				if($scope.resourceEdit === true) return 'text-primary';
    			}    	
    		}, {
    			field : 'unit',
    			displayName : "Unit",
    			enableCellEdit : false,
    			
    		}, {
    			field : 'waste',
    			displayName : "Waste",
    			enableCellEdit : false,
    			width: 130,
    			cellFilter : 'number:3',
    			cellClass: 'text-right',
   			
    		}, {
    			field : 'totalQuantity',
    			displayName : "Total Quantity",
    			enableCellEdit : false,
    			width: 130,
    			cellFilter : 'number:3',
    			cellClass: 'text-right',
  			
    		}, {
    			field : 'rate',
    			displayName : "Rate",
    			enableCellEdit : false,
    			width: 130,
    			cellFilter : 'number:2',
    			cellClass: 'text-right',
   			
    		}, {
    			field : 'value',
    			displayName : "Value",
    			enableCellEdit : false,
    			width: 130,
    			cellFilter : 'number:2',
    			cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
    				var c = 'text-right';
    				if (row.entity.value < 0) {
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
    			aggregationType: uiGridConstants.aggregationTypes.sum
    			/*treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
    			customTreeAggregationFinalizerFn: function(aggregation) {
    				aggregation.rendered = aggregation.value;
    			}*/

    		},
    		{
    			field : 'packageNo',
    			enableCellEdit : true,
    			width: 100,
    			cellClass : function(){
    				if($scope.resourceEdit === true) return 'text-primary';
    			}   
    		}
    		]
    	};
        
		$scope.resourcesGridOptions.onRegisterApi = function (gridApi) {
			  $scope.resourcesGridApi = gridApi;
			  gridApi.rowEdit.on.saveRow($scope, $scope.saveRow);
			  /*$scope.resourcesGridApi.grid.registerDataChangeCallback(function() {
		          $scope.resourcesGridApi.treeBase.expandAllRows();
		        });*/
//			  gridApi.grid.refresh();
		};

		 $scope.saveRow = function( rowEntity ) {
			 var promise = transitService.saveTransitResources($scope.jobNo, rowEntity);
			 $scope.resourcesGridApi.rowEdit.setSavePromise(rowEntity, promise);
		 };


		$scope.onResourceApplyChange = function(applyType, val) {
			var dataRows = $scope.resourcesGridApi.core.getVisibleRows($scope.resourcesGridApi.grid).map( function( gridRow ) { return gridRow.entity; });
			var dataRowsForSubmit = {};
			var code = val;
			var type = applyType;
			angular.forEach(dataRows, function(row){
				type === 'Object Code' ? row.objectCode = code : row.subsidiaryCode = code;
			})
			transitService.saveTransitResourcesList($scope.jobNo, dataRows)
			.then(function(data){
				if(data === '') {
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success',  "Updated " 
							+ type + ' to ' + code + ' for ' + dataRows.length + " records");
				} else {
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data.replace('</br>', ''));
				}
				loadResources();
			}, function(data){
				loadResources();
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Fail " + data.replace('</br>', ''));
			});
			
		};


}]);