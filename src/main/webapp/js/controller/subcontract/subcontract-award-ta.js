mainApp.controller('SubcontractTACtrl', ['$scope', 'resourceSummaryService', 'tenderService', 'subcontractService', 'modalService', '$state', 'uiGridConstants', 'confirmService', 'roundUtil',
                                         function ($scope, resourceSummaryService, tenderService, subcontractService, modalService, $state, uiGridConstants, confirmService, roundUtil) {
	
	loadData();
	
	var accountBalance = {};

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
			             { field: 'objectCode'},
			             { field: 'subsidiaryCode'},
			             { field: 'resourceDescription', displayName: "Description"},
			             { field: 'unit'},
			             { field: 'quantity', enableFiltering: false},
			             { field: 'rate', enableFiltering: false},
			             { field: 'amountBudget', displayName: "Amount", enableFiltering: false, aggregationType: uiGridConstants.aggregationTypes.sum,
		            	 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'}
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
			             { field: 'objectCode'},
			             { field: 'subsidiaryCode'},
			             { field: 'description'},
			             { field: 'unit'},
			             { field: 'quantity', enableFiltering: false},
			             { field: 'rateBudget', enableFiltering: false},
			             { field: 'amountBudget', displayName: "Amount", enableFiltering: false, aggregationType: uiGridConstants.aggregationTypes.sum,
			            	 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'}

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
					rowEntity.rateBudget = roundUtil.round(newValue, 2);
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

	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};


	$scope.addData = function() {
		$scope.gridOptionsTa.data.push({
			"objectCode": "",
			"subsidiaryCode": "",
			"description": "",
			"unit": "",
			"quantity": 0,
			"rateBudget": 1,
			"amountBudget": 0
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
				"unit": resources[i]['unit'],
				"quantity": resources[i]['quantity'],
				"rateBudget": resources[i]['rate'],
				"amountBudget": resources[i]['amountBudget']
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
			var ta = $scope.gridOptionsTa.data;

			//Validate Account Balance
			var taBalance = JSON.parse(JSON.stringify(accountBalance));

			for (i in ta){
				var objectCode = ta[i]['objectCode'];
				var subsidiaryCode = ta[i]['subsidiaryCode'];
				var accountCode =  (objectCode).concat("-".concat(subsidiaryCode));

				if(Object.keys(taBalance).indexOf(accountCode) >= 0)
					taBalance[accountCode] =  taBalance[accountCode] - ta[i]['amountBudget'];

			}

			for (i in taBalance){
				//console.log("taBalance[accountCode]: "+taBalance[accountCode]);
				if(taBalance[i] != 0){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Amounts are not balanced for account: "+accountCode);
					return;
				}
			}


			//Update
			var newTADetailList = [];
			for (i in ta){
				var newTADetail = {
						//billItem : ta[i]['billItem'],
						objectCode: ta[i]['objectCode'],
						subsidiaryCode: ta[i]['subsidiaryCode'],
						description: ta[i]['description'],
						unit: ta[i]['unit'],
						quantity: ta[i]['quantity'],
						rateBudget: ta[i]['rateBudget'],
						amountBudget: ta[i]['amountBudget'],
				}
				newTADetailList.push(newTADetail);
			}
			//console.log(newTADetailList);


			if($scope.subcontract.scStatus == "160"){
				var modalOptions = {
						bodyText: 'All existing tenders and tender details will be deleted. Continue?'
				};


				confirmService.showModal({}, modalOptions).then(function (result) {
					if(result == "Yes"){
						updateTenderDetails(newTADetailList);
					}
				});
			}else{
				updateTenderDetails(newTADetailList);
			}
		}else{
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Subcontract does not exist.");
		}
		
	};

	function loadData(){
		if($scope.subcontractNo!="" && $scope.subcontractNo!=null){
			getSubcontract();
			getResourceSummariesBySC();
			getTenderDetailList();
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
							accountBalance[accountCode] =  accountBalance[accountCode] + data[i]['amountBudget'];
						else
							accountBalance[accountCode] =  data[i]['amountBudget'];

					}
					//console.log(accountBalance);

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
					//console.log(data);
					$scope.gridOptionsTa.data= data;

				});
	}
	
	function updateTenderDetails(taData) {
		tenderService.updateTenderDetails($scope.jobNo, $scope.subcontractNo, 0, "", "", "", "", taData, true)
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



}]);

