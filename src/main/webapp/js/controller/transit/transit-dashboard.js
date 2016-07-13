mainApp.controller('TransitCtrl', ['$scope', 'colorCode', 'modalService', 'transitService', 'budgetpostingService', '$cookieStore', 'transitService',  '$window', '$timeout', 'blockUI',
                          function($scope, colorCode, modalService, transitService, budgetpostingService, $cookieStore, transitService, $window, $timeout, blockUI) {
	$scope.loading = true;
	$scope.jobNo = $cookieStore.get("jobNo");
	$scope.jobDescription = $cookieStore.get("jobDescription");
	
	$scope.blockStepBar = blockUI.instances.get('blockStepBar');
	
    Chart.defaults.global.colours = ['#00c0ef', '#FF5252'];

    var sellingJson = {
    		"data": [],
    		"labels": ["Labour", "Plant", "Material", "Subcontract", "Others"],
    		"colours": [{
    			"strokeColor": colorCode.red,
    			"pointHighlightStroke": colorCode.lightRed
    		}, 
    		{
    			"strokeColor": colorCode.blue,
    			"pointHighlightStroke": colorCode.lightBlue
    		},
    		{
    			"strokeColor": colorCode.green,
    			"pointHighlightStroke": colorCode.lightGreen
    		},
    		{
    			"strokeColor": colorCode.purple,
    			"pointHighlightStroke": colorCode.lightPurple
    		},
    		{
    			"strokeColor": colorCode.grey,
    			"pointHighlightStroke": colorCode.lightGrey
    		}],
    };
    
    sellingJson.data.push(
    		30000000,
    		400000000,
    		500000000,
    		2000000000,
    		70000000
    );
    $scope.selling = sellingJson;
    
   

    var barChartJson = {
    		'labels' : [''],
    		'series' : ['Selling Amount', 'ECA Amount'],
    		'data' : [],
    		"colours": [{
    			'strokeColor': colorCode.red,
    			'fillColor': colorCode.red,
    			'highlightFill': colorCode.red,
    			'highlightStroke': colorCode.red
    		}, 
    		{
    			"strokeColor": colorCode.blue,
    			'fillColor': colorCode.blue,
    			'highlightFill': colorCode.blue,
    			'highlightStroke': colorCode.blue
    		}],
    		 'options' : {
     	       'showTooltips' : true,
     	        'responsive' : false,
     	        'maintainAspectRatio' : true,
     	       'scaleLabel': " <%= Number(value / 1000000) + ' M'%>"
     	     }
     
    };
    
    barChartJson.data.push(
    		[3000000000], [2800000000]
    );
    
    $scope.barChartParameters = barChartJson;
    
    $scope.showPanel = 'dashboard';
    $scope.step = 0;
    
    $scope.normalButtonStyle = 'btn-primary';
    $scope.disabledButtonStyle = 'btn-grey disabled';
    $scope.normalTextStyle = 'text-info';
    $scope.disabledTextStyle = 'text-grey';
    
    function initStatus(){
	    $scope.importBqStatus = false;
	    $scope.importResourcesStatus = false;
	    $scope.confirmResourcesStatus = false;
	    $scope.printReportStatus = false;
	    $scope.completeTransitStatus = false;
	    
	    $scope.importBqMessage = 'Please create a header before importing items';
	    $scope.importResourcesMessage = 'Please import BQ before importing resources';
	    $scope.confirmResourcesMessage = 'Please import BQ and Resources before confirm resources';
	    $scope.printReportMessage = 'Please confirm resources before print report';
	    $scope.completeTransitMessage = 'Please print report before complete transit';
	    $scope.reImportBq = 'Please note that if you re-import BQ items, all current items and resources will be deleted';
    }
    initStatus();
    
    $scope.getStyle = function(item){
    	switch(item){
    	case 'ImportBqButton':
    		return $scope.importBqStatus ? $scope.normalButtonStyle : $scope.disabledButtonStyle;
    		break;
    	case 'ImportBqText':
    		return $scope.importBqStatus ? $scope.normalTextStyle : $scope.disabledTextStyle;
    		break;
    	case 'ImportResourcesButton':
    		return $scope.importResourcesStatus ? $scope.normalButtonStyle : $scope.disabledButtonStyle;
    		break;
    	case 'ImportResourcesText':
    		return $scope.importResourcesStatus ? $scope.normalTextStyle : $scope.disabledTextStyle;
    		break;
    	case 'ConfirmResourcesButton':
    		return $scope.confirmResourcesStatus ? $scope.normalButtonStyle : $scope.disabledButtonStyle;
    		break;
    	case 'ConfirmResourcesText':
    		return $scope.confirmResourcesStatus ? $scope.normalTextStyle : $scope.disabledTextStyle;
    		break;
    	case 'PrintReportButton':
    		return $scope.printReportStatus ? $scope.normalButtonStyle : $scope.disabledButtonStyle;
    		break;
    	case 'PrintReportText':
    		return $scope.printReportStatus ? $scope.normalTextStyle : $scope.disabledTextStyle;
    		break;
    	case 'CompleteTransitButton':
    		return $scope.completeTransitStatus ? $scope.normalButtonStyle : $scope.disabledButtonStyle;
    		break;
    	case 'CompleteTransitText':
    		return $scope.completeTransitStatus ? $scope.normalTextStyle : $scope.disabledTextStyle;
    		break;
    	}
    }
    
    $scope.getMessage = function(item){
    	switch(item){
    	case 'ImportBq':
    		return $scope.importBqStatus ? '' : $scope.importBqMessage;
    		break;
    	case 'ReImportBq':
    		return $scope.reImportBq;
    		break;
    	case 'ImportResources':
    		return $scope.importResourcesStatus ? '' : $scope.importResourcesMessage;
    		break;
    	case 'ConfirmResources':
    		return $scope.confirmResourcesStatus ? '' : $scope.confirmResourcesMessage;
    		break;
      	case 'PrintReport':
    		return $scope.printReportStatus ? '' : $scope.printReportMessage;
    		break;
     	case 'CompleteTransit':
    		return $scope.completeTransitStatus ? '' : $scope.completeTransitMessage;
    		break;
     	}
    }
    
    $scope.getTransit = function(){
    	$scope.blockStepBar.start();
    	transitService.getTransit($scope.jobNo)
    	.then(function(data){
	    	if(data instanceof Object) {
	    		$scope.transit = data;
	    		if($scope.transit.status === 'Header Created'){
	    			$scope.step = 1;
	    		} else if($scope.transit.status === 'BQ Items Imported'){
	    			$scope.step = 2
	    		} else if($scope.transit.status === 'Resources Imported') {
	    			$scope.step = 3;
	    		} else if($scope.transit.status === 'Resources Updated') {
	    			$scope.step = 4;
	    		} else if($scope.transit.status === 'Resources Confirmed') {
	    			$scope.step = 5;
	    		} else if($scope.transit.status === 'Report Printed') {
	    			$scope.step = 6;
	    		}else if($scope.transit.status === 'Transit Completed') {
	    			$scope.step = 7;
	    		}
	    		console.log('Current step:' + $scope.step + ' status:' + $scope.transit.status);
	    	}
    		
    		if($scope.step >= 0) {
    			initStatus();
    		}
    		
    		//Header Created
    		if($scope.step >= 1) {
    			$scope.importBqMessage = '';
    			$scope.importBqStatus = true;
    		}
    		
    		//BQ Items Imported
    		if($scope.step >= 2) {
    			$scope.importBqMessage = 'BQ Items Imported';
    			$scope.importResourcesMessage = '';
    			$scope.importResourcesStatus = true;
    		}

    		//Resources Imported
    		if($scope.step >= 3) {
    			$scope.importResourcesMessage = 'Resources Imported';
    			$scope.importResourcesStatus = false;
    			$scope.confirmResourcesMessage = 'Make sure you want to confirm resources and group into packages';
    			$scope.confirmResourcesStatus = true;
    		}

    		//Resources Updated
    		if($scope.step >= 4) {
    			$scope.importResourcesMessage = 'Resources Updated';
    		}

    		//Resources Confirmed
    		if($scope.step >= 5) {
    			$scope.confirmResourcesMessage = 'Resources Confirmed';
    			$scope.confirmResourcesStatus = false;
    			$scope.printReportMessage = '';
    			$scope.printReportStatus = true;
    		}

    		//Report Printed
    		if($scope.step >= 6) {
    			$scope.completeTransitMessage = '';
    			$scope.completeTransitStatus = true;
    		}

    		//Transit Completed
    		if($scope.step >= 7) {
    			$scope.importBqMessage = 'Transit for the job has already been completed';
    			$scope.importResourcesMessage = 'Transit for the job has already been completed';
    			$scope.confirmResourcesMessage = 'Transit for the job has already been completed';
    			$scope.printReportMessage = 'Transit for the job has already been completed';
    			$scope.completeTransitMessage = 'Transit for the job has already been completed';
    			$scope.completeTransitStatus = false;
    		}
    		
    		$scope.blockStepBar.stop();
	    }, function(data){
	    	modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Error:" + data);
	    })
    }
    $scope.getTransit();
    
    $scope.onSubmitTransitUpload = function(item, type){
    	$scope.blockStepBar.start();
		var formData = new FormData();
		formData.append('files', item.files[0]);
		formData.append('type', type);
		formData.append('jobNumber', $scope.jobNo);
		transitService.transitUpload(formData)
		.then(function(data){
			item.value = null;
			$scope.getTransit();
			if(type === 'BQ'){
				$scope.showBqItems();
			} else if(type === 'Resource'){
				$scope.showResourcesItems();
			}
			
			var msg = data;
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', msg.success? 'Success' : 'Fail', 
					"Success:" + msg.success + 
					"\r\nNumber Record Imported:" + msg.numRecordImported + 
					"\r\nHave warning:" + msg.haveWarning);
			$scope.blockStepBar.stop();
		}, function(data){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data );
		});
    }
    
    $scope.showBqItems = function(){
        $scope.loadBqItems();
		$scope.showPanel = 'viewBq';
    }
    
    $scope.showResourcesItems = function(){
    	$scope.loadResources();
		$scope.showPanel = 'viewResources';
    }
    
    $scope.openPrintReport = function(url){
    	var wnd = $window.open(url, 'Print Report', '_blank');
    	$timeout(function(){
    		wnd.close();
    	}, 2000);
    	
    	$timeout(function(){
    		$scope.getTransit();
    	}, 3000);
    }
    
    $scope.confirmResources = function(){
    	$scope.blockStepBar.start();
    	transitService.confirmResourcesAndCreatePackages($scope.jobNo)
    	.then(function(data){
    		$scope.getTransit();
    		if(data === ''){
    			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', 'Resources confirmed' );
    		}else{
    			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', data.replace('<br/>', '\n') );
    		}
    		$scope.blockStepBar.stop();
    	}, function(data){
    		modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data );
    	});
    }
    
    $scope.completeTransitMsg = '';
    $scope.completeTransitErr = false;
    $scope.completeTransit = function(){
    	$scope.blockStepBar.start();
    	transitService.completeTransit($scope.jobNo)
    	.then(function(data){
    		$scope.getTransit();
    		$scope.loadBqItems();
    		$scope.loadResources();
    		$scope.completeTransitMsg = '';
    		$scope.completeTransitErr = false;
    		
    		if(data === ''){
    			$scope.blockStepBar.start();
    			$scope.completeTransitMsg += 'Complete Transit: Success\n';
    			budgetpostingService.postBudget($scope.jobNo)
    			.then(function(data){
    				if(data === ''){
    					$scope.completeTransitMsg += 'Budget posting: Success\n';
    				} else {
    					$scope.completeTransitMsg += 'Budget posting:' + data + '\n';
    					$scope.completeTransitErr = true;
    				}
    				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', $scope.completeTransitErr ? 'Fail' : 'Success', msg.replace('<br/>', '\n') );
    				$scope.blockStepBar.stop();
    			}, function(data){
    				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', msg.replace('<br/>', '\n') + data);
    				$scope.blockStepBar.stop();
    			});
    		} else {
    			$scope.completeTransitMsg +=  'Complete Transit:' + data + '\n';
    			$scope.completeTransitErr = true;
    			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', $scope.completeTransitMsg.replace('<br/>', '\n') + data);
    		}
    		$scope.blockStepBar.stop();
    	}, function(data){
    		modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data );
    		$scope.blockStepBar.stop();
    	});
   	
    }
    
    $scope.loadBqItems = function(){
		transitService.getTransitBQItems($scope.jobNo)
		.then(function(data) {
			$scope.addBpiColumnData(data);
			$scope.bqGridOptions.data = data;
		});
    }
 
    $scope.loadResources = function(){
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
      
    $scope.bqGridOptions = {
    		enableFiltering : true,
    		enableColumnResizing : true,
    		enableGridMenu : true,
    		enableRowSelection : true,
    		enableSelectAll : true,
    		enableFullRowSelection : false,
    		multiSelect : true,
    		showGridFooter : true,
    		enableCellEditOnFocus : false,
    		enablePaginationControls : true,
    		paginationPageSizes : [ 25, 50, 100, 150, 200 ],
    		paginationPageSize : 25,
    		allowCellFocus : false,
    		enableCellSelection : false,
    		columnDefs : [ 
    		{
    			field : 'bpiString()',
    			displayName : "Bill Item",
    			enableCellEdit : false,
    			
    		}, {
    			field : 'description',
    			displayName : "Description",
    			enableCellEdit : false,
    			cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    		          if (row.entity.unit === '') {
    		            return 'blue';
    		          }
    		        }
    			
    		}, {
    			field : 'unit',
    			displayName : "Unit",
    			enableCellEdit : false,
    			
    		}, {
    			field : 'quantity',
    			displayName : "Quantity",
    			enableCellEdit : false,
    			
    		}, {
    			field : 'sellingRate',
    			displayName : "Selling Rate",
    			enableCellEdit : false,
    			
    		}, {
    			field : 'sequenceNo',
    			displayName : "Sequence No",
    			enableCellEdit : false,
    			
    		}, {
    			field : 'value',
    			displayName : "Value",
    			enableCellEdit : false,
    			
    		}
    		]
    	};
    
	$scope.bqGridOptions.onRegisterApi = function (gridApi) {
		  $scope.bqGridApi = gridApi;
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
    		enableCellEditOnFocus : false,
    		enablePaginationControls : true,
    		paginationPageSizes : [ 25, 50, 100, 150, 200 ],
    		paginationPageSize : 25,
    		allowCellFocus : false,
    		enableCellSelection : false,
    		columnDefs : [ 
    		{
    			field : 'bpiString("Resource")',
    			displayName : "B/P/I",
    			enableCellEdit : false,
    			
    		},{
    			field : 'type',
    			displayName : "Type",
    			enableCellEdit : false,
    			
    		}, {
    			field : 'resourceCode',
    			displayName : "Resource Code",
    			enableCellEdit : false,
    			
    		}, {
    			field : 'objectCode',
    			displayName : "Object Code",
    			enableCellEdit : true,
    			cellStyle : 'blue'
    			
    		}, {
    			field : 'subsidiaryCode',
    			displayName : "Subsidiary Code",
    			enableCellEdit : true,
    			cellStyle : 'blue'
    			
    		}, {
    			field : 'description',
    			displayName : "Page No",
    			enableCellEdit : true,
    			cellStyle : 'blue'
    			
    		}, {
    			field : 'unit',
    			displayName : "Unit",
    			enableCellEdit : false,
    			
    		}, {
    			field : 'waste',
    			displayName : "Waste",
    			enableCellEdit : false,
    			
    		}, {
    			field : 'totalQuantity',
    			displayName : "Total Quantity",
    			enableCellEdit : false,
    			
    		}, {
    			field : 'rate',
    			displayName : "Rate",
    			enableCellEdit : false,
    			
    		}, {
    			field : 'value',
    			displayName : "Value",
    			enableCellEdit : false,
    			
    		}
    		]
    	};
        
		$scope.resourcesGridOptions.onRegisterApi = function (gridApi) {
			  $scope.resourcesGridApi = gridApi;
//			  gridApi.grid.refresh();
		};
	      
		$scope.afterCellEdit = function(rowEntity, colDef) {
			$scope.resourcesGridApi.rowEdit.setRowsDirty( [rowEntity]);
			$scope.gridDirtyRows = $scope.resourcesGridApi.rowEdit.getDirtyRows($scope.resourcesGridApi);
		};

		$scope.onUpdate = function() {
			var dataRows = $scope.gridDirtyRows.map(function(gridRow) {
				return gridRow.entity;
			});
			subcontractService.updateMultipleSystemConstants(dataRows)
			.then(function(data){
				$scope.gridApi.rowEdit.setRowsClean(dataRows);
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Updated " + dataRows.length + " records");;
			}, function(data){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data );
			});
			
		};


    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	

 
}]);