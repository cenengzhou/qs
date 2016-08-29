mainApp.controller('IVUpdateCtrl', ['$scope' , 'resourceSummaryService', 'subcontractService', 'uiGridConstants', '$timeout', '$interval', 'roundUtil', 'modalService', '$state', 'uiGridValidateService', '$q', 'uiGridImporterService',
                                    function($scope , resourceSummaryService, subcontractService, uiGridConstants, $timeout, $interval, roundUtil, modalService, $state, uiGridValidateService, $q, uiGridImporterService) {
	
	var awardedSubcontractNos = [];
	var uneditableUnawardedSubcontractNos = [];
	var optionList = [{ id: 'true', value: 'Excluded' },
	                  { id: 'false', value: 'Included' }
	];

	loadData();

	$scope.gridDirtyRows = [];
	$scope.resetData = [];
	$scope.statusArray = [];
	var MSG_ZERO_AMOUNT = 'Amount of this resource is zero';
	var MSG_UPDATE_SUBCONTRACT_DETAILS = 'Please update IV for this resource in the subcontract details section';
	var MSG_GREATER_THEN_BUDGET = 'Cumulative IV Amount cannot be greater than budget amount';
	var MSG_IMPORT_OBJECT_NOT_FOUND = 'Import object not found';
	var MSG_NOT_EQUAL_AMOUNTBUDGET = 'Cumulative IV Amount + IV Movement != Budget amount';
	var MSG_NOCHANGE = 'Cumulative IV Amount and IV Movement are same as row record';
	var STATUS_IGNORED = 'Ignored';
	var STATUS_IMPORTED = 'Imported';
	var STATUS_NOCHANGE = 'No Change';
    $scope.importData = function(grid, newObjects){
    	var importArray = [];
    	$scope.statusArray = [];
    	newObjects.forEach(function(importObj){
    		var newObj = {};
    		var importKey = getImportKey(importObj);
    		newObj.currIVAmount = importObj.currIVAmount;
    		newObj.ivMovement = importObj.ivMovement;
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
    }
    
    $scope.showStatus = function(){
    	modalService.open('md', 'view/iv/modal/iv-importmodal.html', 'IVImportModalCtrl', 'Warn', $scope.statusArray);
    }
    
    function updateRow(row, importObj, statusObj){
    	if(row.entity.amountBudget === 0) {
    		statusObj.message = MSG_ZERO_AMOUNT;
    		statusObj.importStatus = STATUS_IGNORED;
    		return;
    	}
    	
    	if(row.entity.packageNo && row.entity.objectCode.indexOf('14') >= 0 && 
    			(awardedSubcontractNos.indexOf(row.entity.packageNo) >= 0 || uneditableUnawardedSubcontractNos.indexOf(row.entity.packageNo) >= 0)){
    		statusObj.message = MSG_UPDATE_SUBCONTRACT_DETAILS;
    		statusObj.importStatus = STATUS_IGNORED;
    		return;
    	}
    	
    	if(parseFloat(row.entity.amountBudget) + parseFloat(importObj.ivMovement) !== parseFloat(importObj.currIVAmount)){
    		statusObj.message = MSG_NOT_EQUAL_AMOUNTBUDGET;
    		statusObj.importStatus = STATUS_IGNORED;
    		return;
    	}
    	
    	if(parseFloat(importObj.currIVAmount) === row.entity.currIVAmount && parseFloat(importObj.ivMovement) === row.entity.ivMovement){
    		statusObj.message = MSG_NOCHANGE;
    		statusObj.importStatus = STATUS_NOCHANGE;
    		return;
    	}
    	
    	row.entity.currIVAmount = importObj.currIVAmount;
    	row.entity.ivMovement = importObj.ivMovement;
    	statusObj.message = STATUS_IMPORTED;
    	statusObj.importStatus = STATUS_IMPORTED;
		$scope.gridApi.rowEdit.setRowsDirty( [row.entity]);
		return;
    }
    
    function getImportKey(obj){
    	return escape('@' + 
    	parseType('String', obj.packageNo) + '-' + 
    	parseType('String', obj.objectCode) + '-' + 
		parseType('String', obj.subsidiaryCode) + '-' + 
		parseType('String', obj.resourceDescription.replace(/[\r\n]/g, '')) + '-' + 
		parseType('String', obj.unit) + '-' + 
		obj.rate + '@');
    }
    
	$scope.readCsv = function(fileObject){
		$scope.gridApi.importer.importFile( fileObject );
	}
	
	$scope.readXls = function (workbook) {
		var headerNames = XLSX.utils.sheet_to_json( workbook.Sheets[workbook.SheetNames[0]], { header: 1 })[0];
		var sheetObjects = XLSX.utils.sheet_to_json(workbook.Sheets[workbook.SheetNames[0]]);
		var gridHeaders = uiGridImporterService.processHeaders($scope.gridApi.grid, headerNames);
		var typeArray = ['String','String','String','String','String','Float','Float','Float','Float','Float','Float','Boolean','Boolean'];
		var importArray  = sheetToGridHeader(headerNames, gridHeaders, typeArray, sheetObjects);
        var newObjects = [];
        var newObject;
		importArray.forEach(  function( value, index ) {
            newObject = uiGridImporterService.newObject( $scope.gridApi.grid );
            angular.extend( newObject, value );
            newObject = $scope.gridApi.grid.options.importerObjectCallback( $scope.gridApi.grid, newObject );
            newObjects.push( newObject );
          });

		uiGridImporterService.addObjects( $scope.gridApi.grid, newObjects );
      }

  $scope.error = function (e) {
    console.log(e);
  }
  
  function sheetToGridHeader(headerNames, gridHeaders, typeArray, sheetObjects){
	  var newObjects  = [];
	  sheetObjects.forEach(function(so){
			var obj = {};
			for(var i = 0; i< headerNames.length; i++){
				obj[gridHeaders[i]] = parseType(typeArray[i],so[headerNames[i]]);
			}
			newObjects.push(obj);
	  })
	  return newObjects;		
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
  
  function to_json(workbook) {
		var result = {};
		workbook.SheetNames.forEach(function(sheetName) {
			var roa = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[sheetName]);
			if(roa.length > 0){
				result[sheetName] = roa;
			}
		});
		return result;
	}

	uiGridValidateService.setValidator('validatCurrIVAmount',
		    function(argument) {
		      return function(newValue, oldValue, rowEntity, colDef) {
		        if (!newValue) {
		          return true; 
		        } else {
		        	if(rowEntity.currIVAmount > rowEntity.amountBudget){
						return false;
					}else{
						return true;
					}
		        }
		      };
		    },
		    function(argument) {
		      return MSG_GREATER_THEN_BUDGET;
		    }
	  );

	$scope.saveRow = function( rowEntity ) {
        var promise = $q.defer();
        $scope.gridApi.rowEdit.setSavePromise( rowEntity, promise.promise );

          if(rowEntity.amountBudget == 0){
        	  promise.reject();
			}
			if(rowEntity.packageNo !=null && rowEntity.packageNo.length > 0 && rowEntity.objectCode.indexOf("14") >= 0){
				if(awardedSubcontractNos.indexOf(rowEntity.packageNo) >= 0){
					promise.reject();
					return;
				}
				else if(uneditableUnawardedSubcontractNos.indexOf(rowEntity.packageNo) >= 0){
					promise.reject();
					return;
				}
			}
			var cumIVAmount = roundUtil.round(rowEntity.ivMovement + rowEntity.postedIVAmount, 2); 
			if(rowEntity.currIVAmount > rowEntity.amountBudget){
				uiGridValidateService.setInvalid(rowEntity, $scope.gridApi.grid.columns[9].colDef);
				uiGridValidateService.setError(rowEntity, $scope.gridApi.grid.columns[9].colDef, 'validatCurrIVAmount');
					promise.resolve();
					return;
				}else{
//					rowEntity.currIVAmount  = roundUtil.round(rowEntity.currIVAmount, 2);
//					rowEntity.ivMovement = roundUtil.round(rowEntity.currIVAmount - rowEntity.postedIVAmount, 2);
					uiGridValidateService.setValid(rowEntity, $scope.gridApi.grid.columns[9].colDef);

			}
			
				
//				if(cumIVAmount > rowEntity.amountBudget){
//					promise.reject();
//					return;
//				}else{
//					rowEntity.ivMovement = roundUtil.round(rowEntity.ivMovement, 2);
//					rowEntity.currIVAmount = cumIVAmount;
//				}
				promise.resolve();
				$scope.gridApi.rowEdit.setRowsDirty( [rowEntity]);
      };
	
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
			importerShowMenu: false,
			importerDataAddCallback: $scope.importData,
			enableCellEdit : true,
			enableCellEditOnFocus : true,
			rowEditWaitInterval :-1,
			/*paginationPageSizes: [50],
			paginationPageSize: 50,
*/
			columnDefs: [
			             { field: 'packageNo', displayName: "Subcontract No.", enableCellEdit: false, width:80},
			             { field: 'objectCode', displayName: 'Object Code',  enableCellEdit: false , width:80},
			             { field: 'subsidiaryCode', displayName: 'Subsidiary Code', enableCellEdit: false, width:80},
			             { field: 'resourceDescription', displayName: "Description", enableCellEdit: false },
			             { field: 'unit', enableCellEdit: false, enableFiltering: false, width:60},
			             { field: 'quantity', enableCellEdit: false ,enableFiltering: false, width:100, 
			            	 cellClass: 'text-right', cellFilter: 'number:2'},
		            	 { field: 'rate', enableCellEdit: false, enableFiltering: false, width:100,
		            		cellClass: 'text-right', cellFilter: 'number:2'},
	            		 {field: 'amountBudget', displayName: "Amount", enableCellEdit: false, enableFiltering: false,
	            			cellClass: 'text-right', cellFilter: 'number:2'},
            			 {field: 'currIVAmount', displayName: "Cum. IV Amount", enableFiltering: false, aggregationType: uiGridConstants.aggregationTypes.sum,
	            				/* validators: {required: true, validatCurrIVAmount:'currIVAmount'}, cellTemplate: 'ui-grid/cellTitleValidator' , */
            				 cellTemplate: '<div class="ui-grid-cell-contents" style="color:blue;text-align:right;" ng-class="{invalid:grid.validate.isInvalid(row.entity,col.colDef)}" \
            					 title={{grid.validate.getTitleFormattedErrors(row.entity,col.colDef)}}>{{COL_FIELD | number:2}}</div>',
            				 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;" >{{col.getAggregationValue() | number:2 }}</div>'},
        				 {field: 'ivMovement', displayName: "IV Movement", enableFiltering: false, 
            				 aggregationType: uiGridConstants.aggregationTypes.sum,
        					 cellTemplate: '<div class="ui-grid-cell-contents" style="color:blue;text-align:right;" ng-class="{invalid:grid.validate.isInvalid(row.entity,col.colDef)}" \
            					 title={{grid.validate.getTitleFormattedErrors(row.entity,col.colDef)}}>{{COL_FIELD | number:2}}</div>',
        					 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'},
    					 {field: 'postedIVAmount', displayName: "Posted IV Amount", enableCellEdit: false, enableFiltering: false, 
    						cellClass: 'text-right', cellFilter: 'number:2',
    						 aggregationType: uiGridConstants.aggregationTypes.sum, 
    						 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;" >{{col.getAggregationValue() | number:2 }}</div>'},
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
		gridApi.rowEdit.on.saveRow($scope, $scope.saveRow);

		gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			if(rowEntity.amountBudget == 0){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', MSG_ZERO_AMOUNT);
				return;
			}
			if(rowEntity.packageNo !=null && rowEntity.packageNo.length > 0 && rowEntity.objectCode.indexOf("14") >= 0){
				if(awardedSubcontractNos.indexOf(rowEntity.packageNo) >= 0){
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
			if(colDef.name == "currIVAmount"){
				if(rowEntity.currIVAmount > rowEntity.amountBudget){
					rowEntity.currIVAmount = oldValue;
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', MSG_GREATER_THEN_BUDGET);
					return;
				}else{
					rowEntity.currIVAmount  = roundUtil.round(newValue, 2);
					rowEntity.ivMovement = roundUtil.round(rowEntity.currIVAmount - rowEntity.postedIVAmount, 2);
				}
			}
			else if(colDef.name == "ivMovement"){
				var cumIVAmount = roundUtil.round(parseFloat(rowEntity.ivMovement) + parseFloat(rowEntity.postedIVAmount), 2); 
				if(cumIVAmount > rowEntity.amountBudget){
					rowEntity.ivMovement = oldValue;
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', MSG_GREATER_THEN_BUDGET);
					return;
				}else{
					rowEntity.ivMovement = roundUtil.round(newValue, 2);
					rowEntity.currIVAmount = cumIVAmount;
				}
			}
			$scope.gridApi.rowEdit.setRowsDirty( [rowEntity]);
			$scope.gridDirtyRows = $scope.gridApi.rowEdit.getDirtyRows($scope.gridApi);
		});
		/*gridApi.selection.on.rowSelectionChanged($scope, function () {
	            $scope.selection = gridApi.selection.getSelectedRows();
	        });*/
	}

	$scope.applyPercentage = function(){
		if($scope.percent != null){
			angular.forEach($scope.gridOptions.data, function(value, key){
				value.currIVAmount = value.amountBudget * ($scope.percent/100);
				value.ivMovement = value.currIVAmount - value.postedIVAmount;
			});
			$scope.gridApi.grid.refresh();
		}			
		else
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please input %");
	}

	
	$scope.update = function() {
		//$scope.gridOptions.columnDefs.splice(9, 1);
		var gridRows = $scope.gridApi.rowEdit.getDirtyRows();
		var dataRows = gridRows.map( function( gridRow ) { return gridRow.entity; });
		
		if(dataRows.length==0){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "No record has been modified");
			return;
		}
		
		console.log(dataRows);
		
		updateIVAmount(dataRows);
	}
	
	$scope.reset = function(){
		$scope.gridOptions.data = $scope.resetData;
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
	}
	
	function getResourceSummaries() {
		resourceSummaryService.getResourceSummaries($scope.jobNo, "", "")
		.then(
				function( data ) {
					$scope.gridOptions.data = data;
					$scope.nonFinalizedMovementAmount = 0;
					$scope.finalizedMovementAmount = 0;
					angular.forEach(data, function(value, key){
						value.ivMovement = value.currIVAmount - value.postedIVAmount;
						var newObj = angular.copy(value);
						$scope.resetData.push(newObj);
						/*if(){
							$scope.nonFinalizedMovementAmount += ;
							$scope.finalizedMovementAmount += ;
						}*/

					});
					
					$timeout(function () {
						$scope.postedIVAmount = $scope.gridApi.grid.columns[11].getAggregationValue();
						$scope.cumulativeIVAmount = $scope.gridApi.grid.columns[9].getAggregationValue();
						$scope.ivMovement = $scope.gridApi.grid.columns[10].getAggregationValue();
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

	function updateIVAmount(resourceSummaryList) {
		resourceSummaryService.updateIVAmount(resourceSummaryList)
		.then(
				function( data ) {
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "IV has been updated.");
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