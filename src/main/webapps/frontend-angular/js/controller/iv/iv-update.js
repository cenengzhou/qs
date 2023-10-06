mainApp.controller('IVUpdateCtrl', ['$scope' , 'resourceSummaryService', 'subcontractService', 'uiGridConstants', '$timeout', '$interval', 'roundUtil', 'modalService', '$state', 'uiGridValidateService', '$q', 'uiGridImporterService', 'rootscopeService', 'repackagingService', 'blockUI', '$cookies', 'confirmService',
                                    function($scope , resourceSummaryService, subcontractService, uiGridConstants, $timeout, $interval, roundUtil, modalService, $state, uiGridValidateService, $q, uiGridImporterService, rootscopeService, repackagingService, blockUI, $cookies, confirmService) {
	rootscopeService.setSelectedTips('');
	var awardedSubcontractNos = [];
	var uneditableUnawardedSubcontractNos = [];
	var finalizedSubcontractNos = [];
	var optionList = [{ id: '', value: '' },
	                  { id: 'true', value: 'Excluded' },
	                  { id: 'false', value: 'Included' }
	];

	loadData();
	$scope.repackaging = {};
	function getLatestRepackaging() {
		repackagingService.getLatestRepackaging($scope.jobNo)
		.then(
				function( data ) {
					if(data != null){
						$scope.repackaging = data;
					}
				});

	}
	
	function isRepackagingLocked(){
		if($scope.repackaging.status !== '900') {
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', MSG_LOCK_REPACKAGING);
			return false;
		}
		return true;
	}
	
	getLatestRepackaging();
	$scope.gridDirtyRows = [];
	$scope.resetData = [];
	$scope.statusArray = [];
	var MSG_ZERO_AMOUNT = 'Amount of this resource is zero';
	var MSG_LOCK_REPACKAGING = 'Please lock the resources in Repackaging before updating Internal Valuation';
	var MSG_UPDATE_SUBCONTRACT_DETAILS = 'Please update IV for this resource in the subcontract details section';
	var MSG_GREATER_THEN_BUDGET = 'Cumulative IV Amount cannot be greater than budget amount';
	var MSG_IMPORT_OBJECT_NOT_FOUND = 'Import object not found';
	var MSG_NOCHANGE = 'Cumulative IV Amount is same as row record';
	var MSG_WRONG_CURRIVAMOUNT = 'Cumulative IV Amount not equal to IV Movement + Posted IV Amount. ';
	var MSG_WRONG_IVMOVEMENT = 'IV Movement not equal to Cumulative IV Amount - Posted IV Amount. ';
	var MSG_IMPORTED = 'Record imported into the grid'
	
	var STATUS_IGNORED = 'Ignored';
	var STATUS_IMPORTED = 'Imported';
	var STATUS_NOCHANGE = 'No Change';

	$scope.initBlockUI = function(grid, newObjects){
		blockUI.start();
		$timeout(function(){
			$scope.importData(grid, newObjects);
		}, 100);
		
	}
	
    $scope.importData = function(grid, newObjects){
    	
		if(!isRepackagingLocked()){
			return;
		}
    	var importArray = [];
    	$scope.statusArray = [];
    	newObjects.forEach(function(importObj){
    		var newObj = {};
    		var importKey = getImportKey(importObj);
    		newObj.currIVAmount = importObj.currIVAmount;
//    		newObj.ivMovement = importObj.ivMovement;
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
  	  $scope.gridDirtyRows = $scope.gridApi.rowEdit.getDirtyRows($scope.gridApi);
  	  $scope.showStatus();
  	  $timeout(function(){
  		  blockUI.stop();
  	  },100);
    }
    
    $scope.showStatus = function(){
    	modalService.open('md', 'view/excelupload-modal.html', 'ExcelUploadModalCtrl', 'Warn', $scope.statusArray);
    }
    
    function updateRow(row, importObj, statusObj){
    	if(validateAmountBudgetEqZero(row.entity)) {
    		statusObj.message = MSG_ZERO_AMOUNT;
    		statusObj.importStatus = STATUS_IGNORED;
    		return;
    	}
    	
    	if(validateSubcontractNos(row.entity)){
    		statusObj.message = MSG_UPDATE_SUBCONTRACT_DETAILS;
    		statusObj.importStatus = STATUS_IGNORED;
    		return;
    	}
    	
    	if(validateSameFloatValue(importObj.currIVAmount, row.entity.currIVAmount)) {// && validateSameFloatValue(importObj.ivMovement, row.entity.ivMovement)){
    		statusObj.message = MSG_NOCHANGE;
    		statusObj.importStatus = STATUS_NOCHANGE;
    		return;
    	} else {
    		importObj.ivMovement = parseFloat(importObj.currIVAmount) - parseFloat(row.entity.postedIVAmount);
    		if(validateGreaterThenBudget(importObj, row.entity)){
        		statusObj.message = MSG_GREATER_THEN_BUDGET;
        		statusObj.importStatus = STATUS_IGNORED;
        		return; 			
    		} 

    		if(validateWrongIVAmount(importObj, row.entity)) {
        		statusObj.message = MSG_WRONG_CURRIVAMOUNT;
        		statusObj.importStatus = STATUS_IGNORED;
        		return
    		}
    		
    		if(validateWrongIVMovement(importObj, row.entity)){
    			statusObj.message = MSG_WRONG_IVMOVEMENT;
    			statusObj.importStatus = STATUS_IGNORED;
    			return
    		}
    	}
    	
    	row.entity.currIVAmount = roundUtil.round(importObj.currIVAmount, 2);
    	row.entity.ivMovement = roundUtil.round(importObj.ivMovement, 2);
    	statusObj.message = MSG_IMPORTED;
    	statusObj.importStatus = STATUS_IMPORTED;
		$scope.gridApi.rowEdit.setRowsDirty( [row.entity]);
		return;
    }
    
    function getImportKey(obj){
    	return escape('@' + 
    	parseType('String', obj.packageNo) + '-' + 
    	parseType('String', obj.objectCode) + '-' + 
		parseType('String', obj.subsidiaryCode) + '-' + 
		parseType('String', (obj.resourceDescription+'').replace(/[\r\n]/g, '')) + '-' + 
		parseType('String', obj.unit) + '-' + 
		obj.rate + '@');
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
      
	$scope.readCsv = function(fileObject){
		$scope.gridApi.importer.importFile( fileObject );
	}
//	
//	$scope.readXls = function (workbook) {
//		var headerNames = XLSX.utils.sheet_to_json( workbook.Sheets[workbook.SheetNames[0]], { header: 1 })[0];
//		var sheetObjects = XLSX.utils.sheet_to_json(workbook.Sheets[workbook.SheetNames[0]]);
//		var gridHeaders = uiGridImporterService.processHeaders($scope.gridApi.grid, headerNames);
//		var typeArray = ['String','String','String','String','String','Float','Float','Float','Float','Float','Float','Boolean','Boolean'];
//		var importArray  = sheetToGridHeader(headerNames, gridHeaders, typeArray, sheetObjects);
//        var newObjects = [];
//        var newObject;
//		importArray.forEach(  function( value, index ) {
//            newObject = uiGridImporterService.newObject( $scope.gridApi.grid );
//            angular.extend( newObject, value );
//            newObject = $scope.gridApi.grid.options.importerObjectCallback( $scope.gridApi.grid, newObject );
//            newObjects.push( newObject );
//          });
//
//		uiGridImporterService.addObjects( $scope.gridApi.grid, newObjects );
//      }
//
//  $scope.error = function (e) {
//    console.log(e);
//  }
//  
//  function sheetToGridHeader(headerNames, gridHeaders, typeArray, sheetObjects){
//	  var newObjects  = [];
//	  sheetObjects.forEach(function(so){
//			var obj = {};
//			for(var i = 0; i< headerNames.length; i++){
//				obj[gridHeaders[i]] = parseType(typeArray[i],so[headerNames[i]]);
//			}
//			newObjects.push(obj);
//	  })
//	  return newObjects;		
//  }
//  
	$scope.importAction = null;
    $scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			showGridFooter : false,
			showColumnFooter : true,
			importerShowMenu: true,
			importerDataAddCallback: $scope.initBlockUI,
			enableCellEdit : true,
			enableCellEditOnFocus : true,
			rowEditWaitInterval :-1,
			exporterMenuPdf: false,
			columnDefs: [{ field: 'id', enableCellEdit: false, width:80, visible:false},
			             { field: 'packageNo', displayName: "Subcontract No.", enableCellEdit: false, width:80},
			             { field: 'objectCode', displayName: 'Object Code',  enableCellEdit: false , width:80},
			             { field: 'subsidiaryCode', displayName: 'Subsidiary Code', enableCellEdit: false, width:80},
			             { field: 'resourceDescription', displayName: "Description", enableCellEdit: false },
			             { field: 'unit', enableCellEdit: false, enableFiltering: false, width:60},
			             { field: 'quantity', enableCellEdit: false ,enableFiltering: false, width:100, 
			            	 cellClass: 'text-right', cellFilter: 'number:4'},
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
            			 {field: 'currIVAmount', displayName: "Cum. IV Amount", enableFiltering: false, aggregationType: uiGridConstants.aggregationTypes.sum,
            				 cellTemplate: '<div class="ui-grid-cell-contents" style="color:blue;text-align:right;" ng-class="{invalid:grid.validate.isInvalid(row.entity,col.colDef)}" \
            					 title={{grid.validate.getTitleFormattedErrors(row.entity,col.colDef)}}>{{COL_FIELD | number:2}}</div>',
            				 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;" >{{col.getAggregationValue() | number:2 }}</div>',
 		            		footerCellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
		            			var c = 'text-right';
		            			if (col.getAggregationValue() < 0) {
		            				c += ' red';
		            			}
		            			return c;
		            		},
            			 },
        				 {field: 'ivMovement', displayName: "IV Movement", enableFiltering: false, 
            				 aggregationType: uiGridConstants.aggregationTypes.sum,
        					 cellTemplate: '<div class="ui-grid-cell-contents" style="color:blue;text-align:right;" ng-class="{invalid:grid.validate.isInvalid(row.entity,col.colDef)}" \
            					 title={{grid.validate.getTitleFormattedErrors(row.entity,col.colDef)}}>{{COL_FIELD | number:2}}</div>',
        					 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>',
 		            		footerCellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
		            			var c = 'text-right';
		            			if (col.getAggregationValue() < 0) {
		            				c += ' red';
		            			}
		            			return c;
		            		},
        				 },
    					 {field: 'postedIVAmount', displayName: "Posted IV Amount", enableCellEdit: false, enableFiltering: false, 
    						cellClass: 'text-right', cellFilter: 'number:2',
    						 aggregationType: uiGridConstants.aggregationTypes.sum, 
    						 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;" >{{col.getAggregationValue() | number:2 }}</div>',
 		            		footerCellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
		            			var c = 'text-right';
		            			if (col.getAggregationValue() < 0) {
		            				c += ' red';
		            			}
		            			return c;
		            		},
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
							 cellFilter: 'mapExclude'},
						 {field: 'finalized', displayName: "Final Post",  enableCellEdit: false, width:80}
						 ]
	};

    function validateAmountBudgetEqZero(rowEntity){
    	return rowEntity.amountBudget === 0;
    }

    function validateSubcontractNos(rowEntity){
    	return rowEntity.packageNo && rowEntity.objectCode.indexOf('14') >= 0;
    }
    
    function validateSameFloatValue(value1, value2){
    	return parseFloat(value1) === parseFloat(value2);
    }
    
    function validateGreaterThenBudget(newObj, currentObj){
    	if(parseFloat(currentObj.amountBudget)>0){
        	if((parseFloat(currentObj.amountBudget) < parseFloat(newObj.currIVAmount)) || parseFloat(newObj.currIVAmount) < 0){
        		return true;
        	}
        	if((parseFloat(currentObj.amountBudget) < parseFloat(newObj.ivMovement)+parseFloat(currentObj.postedIVAmount)) || (parseFloat(newObj.ivMovement)+parseFloat(currentObj.postedIVAmount)) < 0){
        		return true;
        	}
    	}else{
        	if((parseFloat(currentObj.amountBudget) > parseFloat(newObj.currIVAmount)) || parseFloat(newObj.currIVAmount) > 0){
        		return true;
        	}
        	if((parseFloat(currentObj.amountBudget) > parseFloat(newObj.ivMovement)+parseFloat(currentObj.postedIVAmount)) || (parseFloat(newObj.ivMovement)+parseFloat(currentObj.postedIVAmount)) > 0){
        		return true;
        	}
    	}
    	return false;
    }
    
    function validateGreaterThenBudgetManual(row, colDef){
    	if(row.amountBudget>0){
    		if(colDef.name == 'currIVAmount'){
            	if((parseFloat(row.amountBudget) < parseFloat(row.currIVAmount)) || parseFloat(row.currIVAmount) < 0){
            		return true;
            	}
    			
    		}else if(colDef.name == 'ivMovement'){
            	if((parseFloat(row.amountBudget) < parseFloat(row.ivMovement)+parseFloat(row.postedIVAmount)) || (parseFloat(row.ivMovement)+parseFloat(row.postedIVAmount)) < 0){
            		return true;
            	}
    		}
    	}else{
    		if(colDef.name == 'currIVAmount'){
	        	if((parseFloat(row.amountBudget) > parseFloat(row.currIVAmount)) || parseFloat(row.currIVAmount) > 0){
	        		return true;
	        	}
        	}
    		else if(colDef.name == 'ivMovement'){
	        	if((parseFloat(row.amountBudget) > parseFloat(row.ivMovement)+parseFloat(row.postedIVAmount)) || (parseFloat(newObjrow.ivMovement)+parseFloat(row.postedIVAmount)) > 0){
	        		return true;
	        	}
        	}
    	}
    	return false;
    }
    
    function isUneditableOrAwardedSubcontract(packageNo){
    	if(awardedSubcontractNos.indexOf(packageNo) >= 0 ||
			uneditableUnawardedSubcontractNos.indexOf(packageNo) >= 0){
    		return true;
    	} else {
    		return false;
    	}
    }
    
    function validateWrongIVAmount(newObj, currentObj){
    	return parseFloat(newObj.currIVAmount) !== parseFloat(newObj.ivMovement) + parseFloat(currentObj.postedIVAmount);
    }

    function validateWrongIVMovement(newObj, currentObj){
    	return parseFloat(newObj.ivMovement) !== parseFloat(newObj.currIVAmount) - parseFloat(currentObj.postedIVAmount);
    }
    
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;
		gridApi.rowEdit.on.saveRow($scope, $scope.saveRow);
		$scope.importAction = $scope.gridApi.importer.importFile;
		gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			if(!isRepackagingLocked()){
				return;
			}
			if(validateAmountBudgetEqZero(rowEntity)){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', MSG_ZERO_AMOUNT);
				return;
			}
			if(validateSubcontractNos(rowEntity)){
				if(awardedSubcontractNos.indexOf(rowEntity.packageNo) >= 0 && finalizedSubcontractNos.indexOf(rowEntity.packageNo) < 0){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', MSG_UPDATE_SUBCONTRACT_DETAILS);
					return;
				}
				else if(uneditableUnawardedSubcontractNos.indexOf(rowEntity.packageNo) >= 0){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', MSG_UPDATE_SUBCONTRACT_DETAILS);
					return;
				}
			}
		});
	
		gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			if(!validateSameFloatValue(newValue, oldValue)){
				if(colDef.name == "currIVAmount"){
					if(validateGreaterThenBudgetManual(rowEntity, colDef)){
						rowEntity.currIVAmount = oldValue;
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', MSG_GREATER_THEN_BUDGET+"1");
						return;
					}else{
						rowEntity.currIVAmount  = roundUtil.round(newValue, 2);
						rowEntity.ivMovement = roundUtil.round(rowEntity.currIVAmount - rowEntity.postedIVAmount, 2);
					}
				}
				else if(colDef.name == "ivMovement"){
					if(validateGreaterThenBudgetManual(rowEntity, colDef)){
						rowEntity.ivMovement = oldValue;
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', MSG_GREATER_THEN_BUDGET+"2");
						return;
					}else{
						var cumIVAmount = roundUtil.round(parseFloat(rowEntity.ivMovement) + parseFloat(rowEntity.postedIVAmount), 2); 
						rowEntity.ivMovement = roundUtil.round(newValue, 2);
						rowEntity.currIVAmount = cumIVAmount;
					}
				}
				$scope.gridApi.rowEdit.setRowsDirty( [rowEntity]);
				$scope.gridDirtyRows = $scope.gridApi.rowEdit.getDirtyRows($scope.gridApi);
			}
		});
		/*gridApi.selection.on.rowSelectionChanged($scope, function () {
	            $scope.selection = gridApi.selection.getSelectedRows();
	        });*/
	}

	$scope.applyPercentage = function(){
		if(!isRepackagingLocked()) return;
		if($scope.percent != null){
//			var rows = $scope.gridApi.selection.getSelectedRows();
			var rows = $scope.gridApi.core.getVisibleRows();
			angular.forEach(rows, function(value, key){
				if(!validateAmountBudgetEqZero(value.entity) &&
					!isUneditableOrAwardedSubcontract(value.entity.packageNo)){
					value.entity.currIVAmount = value.entity.amountBudget * ($scope.percent/100);
					value.entity.ivMovement = roundUtil.round(value.entity.currIVAmount - value.entity.postedIVAmount, 2);
					$scope.gridApi.rowEdit.setRowsDirty([value.entity]);
				}
			});
			$scope.gridApi.grid.refresh();
		}			
		else
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please input % between 0 ~ 100");
	}

	
	$scope.update = function() {
		//$scope.gridOptions.columnDefs.splice(9, 1);
		var gridRows = $scope.gridApi.rowEdit.getDirtyRows();
		var dataRows = gridRows.map( function( gridRow ) { return gridRow.entity; });
		
		if(dataRows.length==0){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "No record has been modified");
			return;
		}
		
		
		updateIVAmount(dataRows);
	}
	
	$scope.reset = function(){
		$scope.gridOptions.data = angular.copy($scope.resetData);
		var dataRows = $scope.gridDirtyRows.map(function(gridRow) {
			return gridRow.entity;
		});
		$scope.gridApi.rowEdit.setRowsClean(dataRows);
		$scope.gridDirtyRows = [];
		$scope.statusArray = [];
	}
	
	function loadData() {
		getResourceSummaries();
		getUnawardedSubcontractNosUnderPaymentRequisition();
		getAwardedSubcontractNos();
		getFinalizedSubcontractNos();
	}
	
	function getResourceSummaries() {
		resourceSummaryService.getResourceSummaries($scope.jobNo, "", "")
		.then(
				function( data ) {
					$scope.gridOptions.data = data;
					$scope.nonFinalizedMovementAmount = 0;
					$scope.finalizedMovementAmount = 0;
					$scope.resetData = [];
					angular.forEach(data, function(value, key){
						var newObj = angular.copy(value);
						$scope.resetData.push(newObj);

					});
					
					$timeout(function () {
						$scope.postedIVAmount = $scope.gridApi.grid.columns[12].getAggregationValue();
						$cookies.put('postedIVAmount', $scope.postedIVAmount);
						$scope.cumulativeIVAmount = $scope.gridApi.grid.columns[10].getAggregationValue();
						$cookies.put('cumulativeIVAmount', $scope.cumulativeIVAmount);
						$scope.ivMovement = $scope.gridApi.grid.columns[11].getAggregationValue();
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

	function getFinalizedSubcontractNos() {
		subcontractService.getFinalizedSubcontractNos($scope.jobNo)
		.then(
				function( data ) {
					if(data)
						finalizedSubcontractNos = data;
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
						$scope.gridDirtyRows = null;
						$state.reload();
					}
				});
	}
	
	$scope.recalculateIV = function() {
		resourceSummaryService.recalculateResourceSummaryIVbyJob($scope.jobNo)
		.then(
				function( data ) {
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "IV has been recalculated.");
						$state.reload();
					}
				});
	}
	
	$scope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams){ 
		if($scope.gridDirtyRows != null && $scope.gridDirtyRows.length >0){
			event.preventDefault();
			var modalOptions = {
					bodyText: "There are unsaved data, do you want to leave without saving?"
			};
			confirmService.showModal({}, modalOptions)
			.then(function (result) {
				if(result == "Yes"){
					$scope.gridDirtyRows = null;
					$state.go(toState.name);
				}
			});
			
		}
	});
	
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