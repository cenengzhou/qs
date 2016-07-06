mainApp.controller('SubcontractTACtrl', ['$scope', 'resourceSummaryService', 'tenderService', 'modalService', '$state', 'uiGridConstants',
                                         function ($scope, resourceSummaryService, tenderService, modalService, $state, uiGridConstants) {
	var accountBalance = {};
	loadResourceSummaries();
	loadTenderDetailList();

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
					rowEntity.quantity = round(newValue, 4);
					rowEntity.amountBudget = round(rowEntity.quantity * rowEntity.rateBudget, 2);
				}
				else if(colDef.name == "rateBudget"){
					rowEntity.rateBudget = round(newValue, 2);
					rowEntity.amountBudget = round(rowEntity.quantity * rowEntity.rateBudget, 2);
				}
				else if(colDef.name == "amountBudget"){
					rowEntity.amountBudget  = round(newValue, 2);
					rowEntity.quantity = round(rowEntity.amountBudget/rowEntity.rateBudget, 4);
				}
			}
		});

		gridApi.selection.on.rowSelectionChanged($scope,function(row){
			$scope.removeRowIndex = $scope.gridOptionsTa.data.indexOf(row.entity);
			console.log("$scope.removeRowIndex "+$scope.removeRowIndex);
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
		var ta = $scope.gridOptionsTa.data;
		console.log(ta);
		
		//Validate Account Balance
		var taBalance = JSON.parse(JSON.stringify(accountBalance));
		
		for (i in ta){
			var objectCode = ta[i]['objectCode'];
			var subsidiaryCode = ta[i]['subsidiaryCode'];

			if(Object.keys(taBalance).includes((objectCode).concat(subsidiaryCode)))
				taBalance[(objectCode).concat(subsidiaryCode)] =  taBalance[(objectCode).concat(subsidiaryCode)] - ta[i]['amountBudget'];

		}
		
		for (accountCode in taBalance){
			//console.log("taBalance[accountCode]: "+taBalance[accountCode]);
			if(taBalance[accountCode] != 0){
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
		
		console.log(newTADetailList);
		
		updateTenderDetails(newTADetailList);
	};



	function loadResourceSummaries() {
		resourceSummaryService.getResourceSummariesBySC($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					$scope.gridOptions.data= data;


					for (i in data){
						var objectCode = data[i]['objectCode'];
						var subsidiaryCode = data[i]['subsidiaryCode'];

						if(Object.keys(accountBalance).includes((objectCode).concat(subsidiaryCode)))
							accountBalance[(objectCode).concat(subsidiaryCode)] =  accountBalance[(objectCode).concat(subsidiaryCode)] + data[i]['amountBudget'];
						else
							accountBalance[(objectCode).concat(subsidiaryCode)] =  data[i]['amountBudget'];

					}
					//console.log(accountBalance);

				});
	}

	function loadTenderDetailList() {
		tenderService.getTenderDetailList($scope.jobNo, $scope.subcontractNo, 0)
		.then(
				function( data ) {
					//console.log(data);
					$scope.gridOptionsTa.data= data;

				});
	}
	
	function updateTenderDetails(taData) {
		tenderService.updateTenderDetails($scope.jobNo, $scope.subcontractNo, 0, "", "", taData, true)
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


	//Rounding Util
	function round(value, decimals) {
		return Number(Math.round(value+'e'+decimals)+'e-'+decimals);
	}


}]);

