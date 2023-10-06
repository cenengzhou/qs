mainApp.controller('SubcontractTACtrl', ['$scope', 'resourceSummaryService', 'tenderService', 'subcontractService', 'jdeService', 'modalService', '$state', 'uiGridConstants', 'confirmService', 'roundUtil', 'paymentService',
                                         function ($scope, resourceSummaryService, tenderService, subcontractService, jdeService, modalService, $state, uiGridConstants, confirmService, roundUtil, paymentService) {

	$scope.units=[];
	var accountBalance = {};
	
	loadData();

	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			//enableRowSelection: true,
			enableFullRowSelection: true,
			multiSelect: true,
			//showGridFooter : true,
			showColumnFooter : true,
			//fastWatch : true,

			exporterMenuPdf: false,

			columnDefs: [
			             { field: 'objectCode', width: 60},
			             { field: 'subsidiaryCode', width: 80},
			             { field: 'resourceDescription', displayName: "Description", width: 100},
			             { field: 'quantity', enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2', width: 100},
			             { field: 'rate', enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2', width: 100},
			             { field: 'amountBudget', displayName: "Amount", width: 100, enableFiltering: false, 
			            	 cellClass: 'text-right', cellFilter: 'number:2',
			            	 aggregationType: uiGridConstants.aggregationTypes.sum,
			            	 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'},
		            	 { field: 'unit', width: 60},
			             { field: 'id', visible: false}
			             ]

	};


	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;
	}

	$scope.gridOptionsTa = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			multiSelect: false,
			showColumnFooter : true,
			enableCellEditOnFocus : true,
			exporterMenuPdf: false,
			rowEditWaitInterval :-1,


			columnDefs: [
			             { field: 'objectCode', width: 60},
			             { field: 'subsidiaryCode', width: 80},
			             { field: 'description', width: 100},
			             { field: 'quantity', enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:4', width: 100},
			             { field: 'rateBudget', enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:4', width: 100},
			             { field: 'amountBudget', displayName: "Amount", width: 100, enableFiltering: false, 
			            	 cellClass: 'text-right', cellFilter: 'number:2',
			            	 aggregationType: uiGridConstants.aggregationTypes.sum,
			            	 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'},
			            { field: 'unit',  editableCellTemplate: 'ui-grid/dropdownEditor', width: 60,
				            	 editDropdownValueLabel: 'value', editDropdownOptionsArray: $scope.units},
			            { field: 'resourceNo', visible: false}	 

			             ]

	};

	$scope.gridOptionsTa.onRegisterApi = function (gridApi) {
		$scope.gridApiTa = gridApi;

		gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			if(colDef.name == 'objectCode' && rowEntity.objectCode.length!=6){
				rowEntity.objectCode = oldValue;
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Object code must be 6 digits in length.");
				return;
			}
			else if(colDef.name == "subsidiaryCode" && rowEntity.subsidiaryCode.length!=8){
				rowEntity.subsidiaryCode = oldValue;
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Subsidiary code must be 8 digits in length.");
				return;
			}

			if(rowEntity.quantity!=null && rowEntity.rateBudget != null && rowEntity.amountBudget != null){
				if(colDef.name == "quantity"){
					rowEntity.quantity = roundUtil.round(newValue, 4);
					rowEntity.amountBudget = roundUtil.round(rowEntity.quantity * rowEntity.rateBudget, 2);
				}
				else if(colDef.name == "rateBudget"){
					rowEntity.rateBudget = roundUtil.round(newValue, 4);
					rowEntity.amountBudget = roundUtil.round(rowEntity.quantity * rowEntity.rateBudget, 2);
				}
				else if(colDef.name == "amountBudget"){
					rowEntity.amountBudget  = roundUtil.round(newValue, 2);
					rowEntity.quantity = roundUtil.round(rowEntity.amountBudget/rowEntity.rateBudget, 4);
				}
			}
		});

		gridApi.selection.on.rowSelectionChanged($scope,function(row){
			$scope.removeRowIndex = $scope.gridOptionsTa.data.indexOf(row.entity);
		});

	}


	$scope.addData = function() {
		$scope.gridOptionsTa.data.push({
			"objectCode": "",
			"subsidiaryCode": "",
			"description": "",
			"quantity": 0,
			"rateBudget": 1,
			"amountBudget": 0, 
			"unit": "AM"
		});
	};


	$scope.copyData = function() {
		var resources = $scope.gridApi.selection.getSelectedRows();
		if(resources.length == 0){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select Resource Summaries to copy.");
			return;
		}

		for (i in resources){
			$scope.gridOptionsTa.data.push({
				"objectCode": resources[i]['objectCode'],
				"subsidiaryCode": resources[i]['subsidiaryCode'],
				"description": resources[i]['resourceDescription'],
				"quantity": resources[i]['quantity'],
				"rateBudget": resources[i]['rate'],
				"amountBudget": resources[i]['amountBudget'],
				"resourceNo": resources[i]['id'],
				"unit": resources[i]['unit']
			});
		}


	};


	$scope.deleteData = function() {
		var selectedRows = $scope.gridApiTa.selection.getSelectedRows();
		if(selectedRows.length == 0){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select Tender Analysis Details to delete.");
			return;
		}
		if($scope.removeRowIndex != null){
			if($scope.removeRowIndex == 0)
				$scope.gridOptionsTa.data.splice(0,1);
			else
				$scope.gridOptionsTa.data.splice($scope.removeRowIndex, $scope.removeRowIndex);
		}

	};


	//Save Function
	$scope.save = function () {
		if($scope.subcontractNo!="" && $scope.subcontractNo!=null){
			
			paymentService.getLatestPaymentCert($scope.jobNo, $scope.subcontractNo)
			.then(
					function( data ) {
						if(data){
							if(data.paymentStatus == 'PND'){
								var modalOptions = {
										bodyText: "Payment Requisition with status 'Pending' will be deleted. Proceed?"
								};

								confirmService.showModal({}, modalOptions).then(function (result) {
									if(result == "Yes"){
										proceedToSave();
									}
								});
							}else if(data.paymentStatus == 'APR'){
								proceedToSave();
							}else{
								modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Payment Requisition is being submitted. No amendment is allowed.");
							}
						}else{
							proceedToSave();
						}
					});
			
		
		}else{
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Subcontract does not exist.");
		}
		
	};

	function loadData(){
		if($scope.subcontractNo!="" && $scope.subcontractNo!=null){
			getSubcontract();
			getResourceSummariesBySC();
			getTenderDetailList();
			getUnitOfMeasurementList();
		}
	}
	
	function proceedToSave(){
		var ta = $scope.gridOptionsTa.data;
		if(ta.length == 0){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please input Tender Analysis Details with total amount equals to Resource Summaries.");
			return;
		}

		var taBalance = JSON.parse(JSON.stringify(accountBalance));
			
		for (i in ta){
			var objectCode = ta[i]['objectCode'];
			var subsidiaryCode = ta[i]['subsidiaryCode'];
			var accountCode =  (objectCode).concat("-".concat(subsidiaryCode));

			if(ta[i].quantity <= 0){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Tender Analysis Details with Quantity less than or equal to 0 is invalid.");
				return;
			}
			
			if(Object.keys(taBalance).indexOf(accountCode) >= 0)
				taBalance[accountCode] =  roundUtil.round(taBalance[accountCode] - ta[i]['amountBudget'], 2);
			else{
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Invalid account code: "+accountCode);
				return;
			}

		}

		for (i in taBalance){
			if(taBalance[i] != 0){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Amounts are not balanced for account: "+accountCode + " : "+taBalance[i]);
				return;
			}
		}

		if($scope.subcontract.scStatus >= "160"){
			var modalOptions = {
					bodyText: 'Existing tenders and tender details will be changed or deleted. Continue?'
			};


			confirmService.showModal({}, modalOptions).then(function (result) {
				if(result == "Yes"){
					updateTenderDetails(ta);
				}
			});
		}else{
			updateTenderDetails(ta);
		}
	}

	function getResourceSummariesBySC() {
		resourceSummaryService.getResourceSummariesBySC($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					$scope.gridOptions.data= data;

					for (i in data){
						var objectCode = data[i]['objectCode'];
						var subsidiaryCode = data[i]['subsidiaryCode'];
						var accountCode =  (objectCode).concat("-".concat(subsidiaryCode));
						
						if(Object.keys(accountBalance).indexOf(accountCode) >= 0)
							accountBalance[accountCode] =  roundUtil.round(accountBalance[accountCode] + data[i]['amountBudget'], 2);
						else
							accountBalance[accountCode] =  data[i]['amountBudget'];
					}
				});
	}
	
	function getSubcontract(){
		subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					$scope.subcontract = data;
					
					if($scope.subcontract.scStatus =="330" || $scope.subcontract.scStatus =="500")
						$scope.disableButtons = true;
					else
						$scope.disableButtons = false;
				});
	}
	

	function getTenderDetailList() {
		tenderService.getTenderDetailList($scope.jobNo, $scope.subcontractNo, 0)
		.then(
				function( data ) {
					$scope.gridOptionsTa.data= data;

				});
	}
	
	function updateTenderDetails(taData) {
		tenderService.updateTenderDetails($scope.jobNo, $scope.subcontractNo, 0, "", "", "", "", "", taData, true)
		.then(
				function( data ) {
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Tender Details have been updated.");
						$state.reload();
					}
				});

		}

	function getUnitOfMeasurementList() {
		jdeService.getUnitOfMeasurementList()
		.then(
				function( data ) {
					angular.forEach(data, function(value, key){
						$scope.units.push({'id': value.unitCode.trim(), 'value': value.unitCode.trim()});
					});
				});
	}

}]);

