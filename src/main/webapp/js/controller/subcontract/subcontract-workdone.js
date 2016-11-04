mainApp.controller('SubcontractWorkdoneCtrl', ['$scope', 'subcontractService', 'resourceSummaryService', 'uiGridConstants', 'modalService', '$state', 'roundUtil', '$timeout', 'blockUI',
                                               function ($scope, subcontractService, resourceSummaryService, uiGridConstants, modalService, $state, roundUtil, $timeout, blockUI) {

	$scope.percent = "";
	$scope.lastID ="";
	
	$scope.disableButton = true;

	 $scope.edit = true;
	 $scope.canEdit = function() { 
		 return $scope.edit; 
	};
	
	getSubcontract();
	getSubcontractDetailForWD();
	getResourceSummariesBySC();
	
	$scope.gridDirtyRowsWD = [];
	$scope.resetData = [];
	$scope.statusArray = [];
	var MSG_ZERO_RATE = 'No work done is allowed with ZERO subcontract rate for budgeted item.';
	var MSG_WRONG_QTY = 'Cumulative WD Qty not equal to Cumulative WD Amount / SC Rate';
	var MSG_WRONG_AMT = 'Cumulative WD Amount not equal to Cumulative WD Qty * SC Rate';
	var MSG_NOCHANGE = 'Cumulative WD Amount is same as row record';
	var MSG_IMPORTED = 'Record imported into the grid';
	var STATUS_IGNORED = 'Ignored';
	var STATUS_IMPORTED = 'Imported';
	var STATUS_NOCHANGE = 'No Change';

	$scope.initBlockUI = function(grid, newObjects){
		if(!$scope.canEdit()) return;
		blockUI.start();
		$timeout(function(){
			$scope.importData(grid, newObjects);
		}, 100);
		
	}
	
    $scope.importData = function(grid, newObjects){
    	var importArray = [];
    	$scope.statusArray = [];
    	newObjects.forEach(function(importObj){
    		var newObj = {};
    		var importKey = getImportKey(importObj);
    		newObj.cumWorkDoneQuantity = importObj.cumWorkDoneQuantity;
    		newObj.amountCumulativeWD = importObj.amountCumulativeWD;
    		importArray[importKey] = newObj;
		});
    	for(var i = 0; i<grid.rows.length; i++){
    		var entityKey = getImportKey(grid.rows[i].entity);
    		var importObj = importArray[entityKey]; 
    		var statusObj = {};
    		statusObj.rowNum = i + 2;
    		statusObj.rowDescription = grid.rows[i].entity.resourceDescription;
    		if(importObj === undefined){
    			statusObj.message = MSG_IMPORT_OBJECT_NOT_FOUND;
    			statusObj.importStatus = STATUS_IGNORED;
    		} else {
    			updateRow(grid.rows[i], importObj, statusObj);
    		}
    		$scope.statusArray.push(statusObj);
    	}
  	  $scope.gridApi.grid.refresh();
  	  $scope.gridDirtyRowsWD = $scope.gridApi.rowEdit.getDirtyRows($scope.gridApi);
  	  $scope.gridOptionsIV.data = [];
  	  $scope.showStatus();
  	  $timeout(function(){
  		  blockUI.stop();
  	  },100);
    }
    
    $scope.showStatus = function(){
    	modalService.open('md', 'view/excelupload-modal.html', 'ExcelUploadModalCtrl', 'Warn', $scope.statusArray);
    }
    
    function updateRow(row, importObj, statusObj){
    	if(validateZeroRate(row.entity)) {
    		statusObj.message = MSG_ZERO_RATE;
    		statusObj.importStatus = STATUS_IGNORED;
    		return;
    	}

    	if(validateSameFloatValue(importObj.cumWorkDoneQuantity, row.entity.cumWorkDoneQuantity) 
    			&& validateSameFloatValue(importObj.amountCumulativeWD, row.entity.amountCumulativeWD)){
    		statusObj.message = MSG_NOCHANGE;
    		statusObj.importStatus = STATUS_NOCHANGE;
    		return;
    	} else {
    		importObj.cumWorkDoneQuantity = roundUtil.round(importObj.amountCumulativeWD / row.entity.scRate, 4);
    		if(!validateCumWDQty(importObj, row.entity)){
        		statusObj.message = MSG_WRONG_QTY;
        		statusObj.importStatus = STATUS_IGNORED;
        		return; 			
    		} 

    		if(!validateCumWDAmt(importObj, row.entity)) {
        		statusObj.message = MSG_WRONG_AMT;
        		statusObj.importStatus = STATUS_IGNORED;
        		return
    		}
    	}
    	
    	row.entity.cumWorkDoneQuantity = roundUtil.round(importObj.cumWorkDoneQuantity, 4);
    	row.entity.amountCumulativeWD = roundUtil.round(importObj.amountCumulativeWD, 2);
    	statusObj.message = MSG_IMPORTED;
    	statusObj.importStatus = STATUS_IMPORTED;
		$scope.gridApi.rowEdit.setRowsDirty( [row.entity]);
		return;
    }
    
    function getImportKey(obj){
    	return escape('@' + parseType('String', obj.id) + '-' + '@');
    }
    
    function parseType(type, value){
    	switch(type){
    		case 'String':
    			return value ? String(value) : null;
    			break;
    		case 'Float':
    			return parseFloat(value);
    			break;
    		case 'Boolean':
    			return value.toLowerCase() === 'true' ? true : false;
    			break;
    		default:
    			return value;
    	}  
      }
      
	$scope.gridOptions = {
			enableSorting: false,
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableFullRowSelection: true,
			multiSelect: false,
			showGridFooter : true,
			showColumnFooter : true,
			rowEditWaitInterval :-1,
			importerShowMenu: true,
			importerDataAddCallback: $scope.initBlockUI,
			//enableCellEditOnFocus : true,

			exporterMenuPdf: false,
			
			
			columnDefs: [
		             	{ field: 'id', displayName: "ID", width: 20, enableCellEdit: false, visible:false},
			             { field: 'lineType', width: 40, enableCellEdit: false, pinnedLeft:true},
		             	 { field: 'description', width: 150, enableCellEdit: false, pinnedLeft:true},

			             {field: 'amountBudget', displayName: "Budget Amount", width: 150, visible:false, enableCellEdit: false,enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2'},
			             {field: 'amountSubcontract', displayName: "Subcontract Amount", width: 150, enableCellEdit: false, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2'},
			             {field: 'cumWorkDoneQuantity', displayName: "Cum. Workdone Quantity", width: 150, cellEditableCondition : $scope.canEdit, 
			            	cellClass: 'text-right blue', cellFilter: 'number:2', enableFiltering: false ,
			            	aggregationType: uiGridConstants.aggregationTypes.sum,
			            	footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'},
			             {field: 'amountCumulativeWD', displayName: "Cum. Workdone Amount", width: 150, cellEditableCondition : $scope.canEdit, 
			            	cellClass: 'text-right blue', cellFilter: 'number:2', enableFiltering: false ,
			            	aggregationType: uiGridConstants.aggregationTypes.sum,
			            	footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'},
			             {field: 'amountPostedWD', displayName: "Posted Workdone Amount", width: 150, enableCellEdit: false, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2',
	            			aggregationType: uiGridConstants.aggregationTypes.sum,
	            		 	footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'},
            		 	{field: 'amountCumulativeCert', displayName: "Cumulative Certified Amount", width: 150, enableCellEdit: false, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2',
	            		 	aggregationType: uiGridConstants.aggregationTypes.sum,
        		 			footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'},
			             {field: 'amountPostedCert', displayName: "Posted Certified Amount", width: 150, visible:false, enableCellEdit: false, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2'},
			             {field: 'quantity', width: 100, enableCellEdit: false, enableFiltering: false ,cellClass: 'text-right', cellFilter: 'number:4'},

	            		 {field: 'costRate', displayName:"Cost Rate", width: 60, enableCellEdit: false},
	            		 {field: 'scRate', displayName: "Sc Rate", width: 60, enableCellEdit: false, enableFiltering: false},
		            	 {field: 'billItem', displayName:"Bill Item", width: 80, enableCellEdit: false},
			             {field: 'objectCode', displayName: "Object Code", width: 70, enableCellEdit: false},
			             {field: 'subsidiaryCode', displayName: "Subsidiary Code", width: 80, enableCellEdit: false},
			             
			             {field: 'projectedProvision', displayName: "Projected Provision", width: 120, visible:false, enableCellEdit: false, enableFiltering: false},
			             {field: 'provision', displayName: "Provision", width: 120, visible:false, enableCellEdit: false, enableFiltering: false},

			             {field: 'altObjectCode', width: 100, visible:false, enableCellEdit: false},

			             {field: 'approved', width: 80, enableCellEdit: false},
			             {field: 'unit', width: 80, visible:false, enableCellEdit: false},

			             {field: 'remark', width: 80, visible:false, enableCellEdit: false},
			             {field: 'contraChargeSCNo', width: 80, visible:false, enableCellEdit: false},
			             {field: 'sequenceNo', width: 80, visible:false, enableCellEdit: false},
			             {field: 'resourceNo', width: 80, visible:false, enableCellEdit: true},
			             {field: 'balanceType', width: 80, visible:false, enableCellEdit: false}
			             ]

	
	};
	
    function validateSameFloatValue(value1, value2){
    	return parseFloat(value1) === parseFloat(value2);
    }
    
	function validateZeroRate(rowEntity){
		return rowEntity.scRate == 0 && rowEntity.costRate > 0;
	}

	function validateCumWDQty(newObj, currentObj){
		return roundUtil.round(newObj.cumWorkDoneQuantity, 4) == roundUtil.round(newObj.amountCumulativeWD / currentObj.scRate, 4);
	}
	
	function validateCumWDAmt(newObj, currentObj){
		return roundUtil.round(newObj.amountCumulativeWD, 2) == roundUtil.round(newObj.cumWorkDoneQuantity * currentObj.scRate, 2);
	}
	
	$scope.gridOptions.onRegisterApi= function(gridApi){
		$scope.gridApi = gridApi;

		 gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			 if(validateZeroRate(rowEntity)){
				 if(oldValue == 0)
					 modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', MSG_ZERO_RATE);					 
			}
        });
		
		gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			if(newValue != oldValue && newValue.trim().length >0){
				if(colDef.name == "cumWorkDoneQuantity"){
					rowEntity.cumWorkDoneQuantity = roundUtil.round(newValue, 4);
					rowEntity.amountCumulativeWD = roundUtil.round(rowEntity.cumWorkDoneQuantity * rowEntity.scRate, 2);
				}
				if(colDef.name == "amountCumulativeWD"){
					rowEntity.amountCumulativeWD = roundUtil.round(newValue, 2);
					rowEntity.cumWorkDoneQuantity = roundUtil.round(rowEntity.amountCumulativeWD / rowEntity.scRate, 4);
				}
				console.log(rowEntity);
				updateWDandIV(rowEntity);
			}else{
				if(colDef.name == "cumWorkDoneQuantity")
					rowEntity.cumWorkDoneQuantity = oldValue;
				else if(colDef.name == "amountCumulativeWD")
					rowEntity.amountCumulativeWD = oldValue;
			}
				
		});

		gridApi.selection.on.rowSelectionChanged($scope,function(row){
			 if($scope.lastID != row.entity.id){
					$scope.lastID = row.entity.id;
						
					if(row.entity.costRate != 0) {
		            	getResourceSummariesByLineType(row.entity.objectCode, row.entity.subsidiaryCode, row.entity.lineType, row.entity.resourceNo);
					} else {
		            	$scope.gridOptionsIV.data.splice (0, $scope.gridOptionsIV.data.length);
						$scope.disableButton = true;
		            }
				}
		});
	}
	

	$scope.gridOptionsIV = {
			enableSorting: false,
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			showGridFooter : true,
			showColumnFooter : true,
			enableCellEditOnFocus : true,
			exporterMenuPdf: false,

			rowEditWaitInterval :-1,
			
			columnDefs: [
			             { field: 'id', width:50 , visible:false, enableCellEdit: false},
			             { field: 'packageNo', width:70, displayName:"Subcontract No.", visible:false, enableCellEdit: false},
			             { field: 'objectCode', enableCellEdit: false , width:70, pinnedLeft:true},
			             { field: 'subsidiaryCode',width:80, enableCellEdit: false, pinnedLeft:true},
			             { field: 'resourceDescription', width:80, displayName: "Description", enableCellEdit: false },
			             {field: 'cumQuantity', displayName: "Cum. Quantity", width:130, enableFiltering: false, cellClass: 'text-right blue', cellFilter: 'number:2', 
			            	 cellEditableCondition : $scope.canEdit, 
			            	 aggregationType: uiGridConstants.aggregationTypes.sum,
			            	footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'},
			             {field: 'currIVAmount', displayName: "Cum. IV Amount", width:130, enableFiltering: false, cellClass: 'text-right blue', cellFilter: 'number:2', 
			            	 cellEditableCondition : $scope.canEdit, 
			            	 aggregationType: uiGridConstants.aggregationTypes.sum,
			            	footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'},
	            		 {field: 'postedIVAmount', width:130,  enableCellEdit: false, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2',  
	            			aggregationType: uiGridConstants.aggregationTypes.sum,
			            	footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'},
            			 {field: 'amountBudget',width:100,  displayName:"Budget Amount", cellClass: 'text-right', cellFilter: 'number:2', enableCellEdit: false, enableFiltering: false },
            			 { field: 'unit', width:50, enableCellEdit: false, enableFiltering: false},
			             { field: 'quantity', width:100, enableCellEdit: false ,enableFiltering: false},
			             { field: 'rate', width:100, enableCellEdit: false, enableFiltering: false }
            			 ]

	};
	
	$scope.gridOptionsIV.onRegisterApi= function(gridApi){
		$scope.gridApiIV = gridApi;

		 gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
	           
	        });
		
		gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			if(newValue != oldValue && newValue.trim().length >0){
				if(colDef.name == "cumQuantity"){
					rowEntity.cumQuantity = roundUtil.round(newValue, 4);
					rowEntity.currIVAmount = roundUtil.round(rowEntity.cumQuantity * rowEntity.rate, 2);
				}
				if(colDef.name == "currIVAmount"){
					rowEntity.currIVAmount = roundUtil.round(newValue, 2);
					rowEntity.cumQuantity = roundUtil.round(rowEntity.currIVAmount / rowEntity.rate, 4);
				}
			}else{
				if(colDef.name == "cumQuantity")
					rowEntity.cumQuantity = oldValue;
				else if(colDef.name == "currIVAmount")
					rowEntity.currIVAmount = oldValue;
			}
		});

	}
	

	$scope.save = function () {
		var currTotalCumIVQty = roundUtil.round($scope.gridApiIV.grid.columns[5].getAggregationValue(), 4);
		var currTotalCumIVAmount = roundUtil.round($scope.gridApiIV.grid.columns[6].getAggregationValue(), 2);
		
		if(currTotalCumIVQty != $scope.totalCumIVQty || currTotalCumIVAmount != $scope.totalCumIVAmount){
			 modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Total Cumulative Work Done Quantity/Amount is not balanced.");
			 return;
		}else{
			var gridRows = $scope.gridApiIV.rowEdit.getDirtyRows();
			var dataRows = gridRows.map( function( gridRow ) { return gridRow.entity; });
			
			if(dataRows.length==0){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "No record has been modified");
			}else{
				updateIVForSubcontract(dataRows);
			}
			
		}
	};

	$scope.applyPercent = function (){
		if(!$scope.canEdit()) return;
		if($scope.percent != null)
			updateWDandIVByPercent($scope.percent);
		else
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please input %");
	}
	
	$scope.recalculateIV = function recalculateResourceSummaryIV() {
		subcontractService.recalculateResourceSummaryIV($scope.jobNo, $scope.subcontractNo, false)
	   	 .then(
				 function( data ) {
					 if(data == false)
						 modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "IV cannot be recalculated.");
					 else{
						 modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "IV has been recalculated.");
						 $state.reload();
					 }
				 });
    }

	function getSubcontractDetailForWD() {
		subcontractService.getSubcontractDetailForWD($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					$scope.gridOptions.data = data;
				});
	}
	
	function getResourceSummariesByLineType(objectCode, subsidiaryCode ,lineType, resourceNo) {
		resourceSummaryService.getResourceSummariesByLineType($scope.jobNo, $scope.subcontractNo, objectCode, subsidiaryCode, lineType, resourceNo)
	   	 .then(
				 function( data ) {
					 if(data != null && data.length > 0){
						$scope.disableButton = false;
						$scope.gridOptionsIV.data=  data;
						
						$timeout(function () {
							$scope.totalCumIVQty = roundUtil.round($scope.gridApiIV.grid.columns[5].getAggregationValue(), 4);
							$scope.totalCumIVAmount = roundUtil.round($scope.gridApiIV.grid.columns[6].getAggregationValue(), 2);
						}, 100);
						
					 }
				 });
	    }
	
	function getResourceSummariesBySC() {
		resourceSummaryService.getResourceSummariesBySC($scope.jobNo, $scope.subcontractNo)
	   	 .then(
				 function( data ) {
					 $scope.gridOptionsIV.data= data;
				 });
	    }
	
	function updateWDandIV(scDetail) {
		subcontractService.updateWDandIV($scope.jobNo, $scope.subcontractNo, scDetail)
	   	 .then(
				 function( data ) {
					 if(data.length!=0){
						 modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', data);
						 $state.reload();
						}
					 else
						 getResourceSummariesByLineType(scDetail.objectCode, scDetail.subsidiaryCode, scDetail.lineType, scDetail.resourceNo);
				 });
	    }
	
	function updateWDandIVByPercent(percent) {
		subcontractService.updateWDandIVByPercent($scope.jobNo, $scope.subcontractNo, percent)
	   	 .then(
				 function( data ) {
					 if(data.length!=0)
						 modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', data);
					 $state.reload();
				 });
	    }
	
	function getSubcontract(){
		subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					$scope.subcontract = data;

					if(data.scStatus < 500 || data.paymentStatus == 'F' || data.splitTerminateStatus ==1  || data.splitTerminateStatus ==2 || data.splitTerminateStatus ==4|| data.submittedAddendum ==1)
						$scope.edit = false;
					else
						$scope.edit = true;
				});
	}

	function updateIVForSubcontract(ivList){
		resourceSummaryService.updateIVForSubcontract(ivList)
	   	 .then(
				 function( data ) {
					 if(data.length!=0)
						 modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', data);
					 $state.reload();
				 });
	}
	
	$scope.saveWDList = function (){
		var gridRows = $scope.gridApi.rowEdit.getDirtyRows();
		var dataRows = gridRows.map( function( gridRow ) { return gridRow.entity; });

		subcontractService.updateWDandIVList($scope.jobNo, $scope.subcontractNo, dataRows)
	   	 .then(
				 function( data ) {
					 if(data.length!=0){
						 modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', data);
					 } 
					 $state.reload();
				 });
	}

}]);

