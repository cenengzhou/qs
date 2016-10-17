mainApp.controller('PaymentDetailsCtrl', ['$scope' , '$stateParams', '$cookies', 'paymentService', 'modalService', 'roundUtil', '$state', 'GlobalParameter', 'uiGridConstants',
                                          function($scope, $stateParams, $cookies, paymentService, modalService, roundUtil, $state, GlobalParameter, uiGridConstants) {
	loadData();
	$scope.disableButtons = true;
	$scope.GlobalParameter = GlobalParameter;
	 $scope.edit = true;
	 $scope.canEdit = function() { 
		 return $scope.edit; 
	};
	
	$scope.resetData = [];
	$scope.statusArray = [];
	
	var STATUS_IGNORED = 'Ignored';
	var STATUS_IMPORTED = 'Imported';
	var STATUS_NOCHANGE = 'No Change';
	var MSG_IMPORT_OBJECT_NOT_FOUND = 'Import object not found';
	var MSG_NOCHANGE = 'Cumulative IV Amount and IV Movement are same as row record';
	var MSG_WRONG_CURRIVAMOUNT = 'Cumulative IV Amount not equal to IV Movement + Posted IV Amount. ';
	var MSG_WRONG_IVMOVEMENT = 'IV Movement not equal to Cumulative IV Amount - Posted IV Amount. ';
	var MSG_IMPORTED = 'Record imported into the grid'

	 $scope.importData = function(grid, newObjects){
		if(!$scope.canEdit()){
			return;
		}
    	var importArray = [];
    	$scope.statusArray = [];
    	newObjects.forEach(function(importObj){
    		var newObj = {};
    		var importKey = getImportKey(importObj);
    		newObj.cumAmount = importObj.cumAmount;
    		newObj.movementAmount = importObj.movementAmount;
    		importArray[importKey] = newObj;
		});
    	for(var i = 0; i<grid.rows.length; i++){
    		var entityKey = getImportKey(grid.rows[i].entity);
    		var importObj = importArray[entityKey]; 
    		var statusObj = {};
    		statusObj.rowNum = i + 2;
    		statusObj.rowDescription = grid.rows[i].entity.description;
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
    	modalService.open('md', 'view/excelupload-modal.html', 'ExcelUploadModalCtrl', 'Warn', $scope.statusArray);
    }
    
    function updateRow(row, importObj, statusObj){
    	if(!validateLineTypeNotInArray(row.entity, lineTypeGeneratedBySystem) ){
    		statusObj.message = validateMessage(row.entity, MSG_LINETYPE_GENERATED_BY_SYSTEM);
    		statusObj.importStatus = STATUS_IGNORED;
    		return;
    	}
    	
    	if(validateSameFloatValue(importObj.cumAmount, row.entity.cumAmount) 
    			&& validateSameFloatValue(importObj.movementAmount, row.entity.movementAmount) ){
    		statusObj.message = MSG_NOCHANGE;
    		statusObj.importStatus = STATUS_NOCHANGE;
    		return;
    	} 
//
		if(!validateSameFloatValue(
				parseFloat(importObj.movementAmount), 
				parseFloat(importObj.cumAmount) - row.entity.postedAmount) ){
    		statusObj.message = MSG_WRONG_IVMOVEMENT;
    		statusObj.importStatus = STATUS_IGNORED;
    		return; 			
		} 

		if(!validateSameFloatValue(
				parseFloat(importObj.cumAmount), 
				parseFloat(importObj.movementAmount) + row.entity.postedAmount) ){
    		statusObj.message = MSG_WRONG_CURRIVAMOUNT;
    		statusObj.importStatus = STATUS_IGNORED;
    		return; 			
		} 

    	row.entity.cumAmount = roundUtil.round(importObj.cumAmount, 2);
    	row.entity.movementAmount = roundUtil.round(importObj.movementAmount, 2);
    	statusObj.message = MSG_IMPORTED;
    	statusObj.importStatus = STATUS_IMPORTED;
		$scope.gridApi.rowEdit.setRowsDirty( [row.entity]);
		return;
    }

    function getImportKey(obj){
    	var importKey = escape('@' + 
    	parseType('String', obj.lineType) + '-' + 
    	parseType('String', obj.billItem) + '-' + 
		parseType('String', (obj.description+'').replace(/[\r\n]/g, '')) + '-' + 
		parseType('String', obj.scSeqNo) + '-' + 
		parseType('String', obj.objectCode) + '-' + 
		parseType('String', obj.subsidiaryCode) + '-' + 
		'@');
    	return importKey;
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
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			importerShowMenu: true,
			importerDataAddCallback: $scope.importData,
			enableRowSelection: true,
			enableSelectAll: true,
			//enableFullRowSelection: true,
			multiSelect: true,
			enableCellEditOnFocus : true,
			showGridFooter : false,
			showColumnFooter : true,
			exporterMenuPdf: false,
			
			rowEditWaitInterval :-1,

			columnDefs: [
			             { field: 'lineType', width: 80, enableCellEdit: false},
			             { field: 'subcontractDetail.amountSubcontract', displayName: "Subcontract Amount", cellClass: 'text-right', cellFilter: 'number:2', enableCellEdit: false, visible: true},
			             { field: 'movementAmount', enableFiltering: false, cellClass: 'text-right blue', cellFilter: 'number:2', 
			            	 cellEditableCondition : $scope.canEdit,
			            	 /* cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
						          if (grid.getCellValue(row,col) != 'GR') {
						            return 'blue';
						          }
						        }*/
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
			             },
			             { field: 'cumAmount', displayName: 'Cumulative Amount', enableFiltering: false, 
			            	 cellClass: 'blue text-right', cellFilter: 'number:2',
			            	 cellEditableCondition : $scope.canEdit,
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
			             },
			             { field: 'postedAmount', displayName: 'Cumulative Posted Amount', enableCellEdit: false,
		            	 cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
		            			var c = 'text-right';
		            			if (row.entity.postedAmount < 0) {
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
			             { field: 'description', width: 150, enableCellEdit: false},
			             { field: 'objectCode', width: 80, enableCellEdit: false},
			             {field: 'subsidiaryCode', width: 80, enableCellEdit: false},
			             { field: 'billItem', width: 100, enableCellEdit: false},
			             { field: 'scSeqNo', width: 80, displayName: "Sequence No", enableCellEdit: false},
			             ]
	};
	

	      
	var lineTypeGeneratedBySystem = ['GR', 'GP', 'RT', 'MR'];
	var MSG_LINETYPE_GENERATED_BY_SYSTEM = 'System generated';
	var MSG_NOEQU_CUMAMOUNT = '';
	function validateLineTypeNotInArray(rowEntity, lineTypeArray) {
		return lineTypeArray.indexOf(rowEntity.lineType) < 0;
	}
	function validateMessage(rowEntity, messageType){
		switch (messageType){
		case MSG_LINETYPE_GENERATED_BY_SYSTEM:
			return rowEntity.lineType + ' is generated by system.';
		default:
			return 'Error';
		}
	}
	
    function validateSameFloatValue(value1, value2){
    	return parseFloat(value1) === parseFloat(value2);
    }
    
    function validateCumAmount(rowEntity){
    	return  roundUtil.round(rowEntity.cumAmount,2) - roundUtil.round(rowEntity.postedAmount,2) === roundUtil.round(rowEntity.movementAmount,2);
    }
    
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;

		gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			console.log(validateLineTypeNotInArray(rowEntity, lineTypeGeneratedBySystem));
			if(!validateLineTypeNotInArray(rowEntity, lineTypeGeneratedBySystem)){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', validateMessage(rowEntity, MSG_LINETYPE_GENERATED_BY_SYSTEM));
			}
		});
		
		gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			if(validateSameFloatValue(newValue, oldValue)) return;
			if(validateLineTypeNotInArray(rowEntity, lineTypeGeneratedBySystem)){
				if(colDef.name == "cumAmount"){
					rowEntity.cumAmount  = roundUtil.round(newValue, 2);
					rowEntity.movementAmount = roundUtil.round(rowEntity.cumAmount - rowEntity.postedAmount, 2);
				}
				if(colDef.name == "movementAmount"){
					rowEntity.movementAmount  = roundUtil.round(newValue, 2);
					rowEntity.cumAmount = roundUtil.round(rowEntity.movementAmount + rowEntity.postedAmount, 2);
				}
			}
			$scope.gridApi.rowEdit.setRowsDirty( [rowEntity]);
			$scope.gridDirtyRows = $scope.gridApi.rowEdit.getDirtyRows($scope.gridApi);
		});

	}
	
	$scope.update = function(){
		if($scope.payment.paymentStatus == "PND"){
			var gridRows = $scope.gridApi.rowEdit.getDirtyRows();
			var dataRows = gridRows.map( function( gridRow ) { return gridRow.entity; });

			if(dataRows.length==0){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "No record has been modified");
				return;
			}

			updatePaymentDetails($scope.gridOptions.data);
		}
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
		if($cookies.get('paymentCertNo') != ""){
			getPaymentCert();
		}else
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please create a payment certificate.");
	}
	
	function getPaymentCert() {
		paymentService.getPaymentCert($scope.jobNo, $scope.subcontractNo, $cookies.get('paymentCertNo'))
		.then(
				function( data ) {
					var payment = data;
					$scope.payment = data;
					
					if($scope.payment.paymentStatus == "PND")
						$scope.disableButtons = false;

					getPaymentDetailList();
				});
	}

	function getPaymentDetailList() {
		paymentService.getPaymentDetailList($scope.jobNo, $scope.subcontractNo, $cookies.get('paymentCertNo'))
		.then(
				function( data ) {
					if($scope.payment.paymentStatus!="PND")
						$scope.edit = false;
					else
						$scope.edit = true;
					$scope.resetData = [];
					angular.forEach(data, function(value, key){
						value.postedAmount = value.cumAmount - value.movementAmount;
						var newObj = angular.copy(value);
						$scope.resetData.push(newObj);
						/*if(value.lineType == "GR")
							$scope.edit = false;
						else
							$scope.edit = true;*/
					});
					
					$scope.gridOptions.data = data;
					
					$scope.gridApi.grid.refresh();
				});
	}
	
	function updatePaymentDetails(paymentDetails) {
		paymentService.updatePaymentDetails($scope.jobNo, $scope.subcontractNo, $cookies.get('paymentCertNo'), "I", paymentDetails)
		.then(
				function( data ) {
					if(data.length != 0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Payment Details have been updated.");
						$state.reload();
					}
				});
	}


}]);