mainApp.controller("SubcontractVendorFeedbackModalCtrl", ['$scope', '$location', '$uibModalInstance', 'uiGridConstants', 'modalParam', '$cookies', 'tenderService', '$state', 'modalService', 'roundUtil',
                                                          function ($scope, $location, $uibModalInstance, uiGridConstants, modalParam, $cookies, tenderService, $state, modalService, roundUtil) {

	$scope.vendorNo= modalParam;
	$scope.jobNo = $cookies.get("jobNo");
	$scope.subcontractNo = $cookies.get("subcontractNo");

	$scope.currencyCode = {
			options: ["HKD",
			          "USD",
			          "INR",
			          "GBP",
			          "EUR",
			          "AUD",
			          "THB",
			          "TWD",
			          "PHP",
			          "JPY",
			          "SGD",
			          "CAD",
			          "CNY",
			          "MOP"
			          ],
			          selected: "HKD"
	};
	
	$scope.statusChangeExecutionOfSC = {
			options: ["Y",
			          "N",
			          "N/A"
			          ],
			          selected: "N/A"
	};

	loadTenderDetail();


	$scope.gridOptions = {
			enableSorting: true,
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			//enableRowSelection: true,
			//enableFullRowSelection: true,
			//multiSelect: false,
			showGridFooter : false,
			showColumnFooter : false,
			//fastWatch : true,

			enableCellEditOnFocus : true,
			exporterMenuPdf: false,
			rowEditWaitInterval :-1,


			columnDefs: [
			             { field: "billItem", displayName:"B/P/I", enableCellEdit: false, width:50},
			             { field: "objectCode", enableCellEdit: false, width:60 },
			             { field: "subsidiaryCode" , enableCellEdit: false, width:80 },
			             { field: "description" , enableCellEdit: false,  width:150 },
			             { field: "unit" , enableCellEdit: false, width:50 },
			             { field: "quantity" , enableCellEdit: false, width:80},
			             { field: "rateBudget" , displayName:"Budget Rate", enableCellEdit: false, width:80},
			             /*{ field: "amountBudget" ,displayName:"Budget", enableCellEdit: false, width:100 },*/
			             { field: "rateSubcontract" , displayName:"SC Rate", enableCellEdit: true, cellClass: "grid-theme-blue", width:80 },
			             { field: "amountSubcontract" ,displayName:"SC Amount", cellClass: "blue", enableCellEdit: true, width:90 },
			             { field: "amountForeign" ,displayName:"Amount (HKD)", enableCellEdit: false, width:100 }
			             ]


	};
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;

		gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			if(colDef.name == "rateSubcontract"){
				rowEntity.rateSubcontract  = roundUtil.round(newValue, 2);
				rowEntity.amountSubcontract = roundUtil.round(rowEntity.quantity * rowEntity.rateSubcontract, 2);
				rowEntity.amountForeign = roundUtil.round(rowEntity.quantity * rowEntity.rateSubcontract * $scope.tender.exchangeRate, 2);
			}
			else if(colDef.name == "amountSubcontract"){
				rowEntity.amountSubcontract = roundUtil.round(newValue, 2);
				rowEntity.rateSubcontract = roundUtil.round(rowEntity.amountSubcontract / rowEntity.quantity, 2);
				rowEntity.amountForeign = roundUtil.round(rowEntity.quantity * rowEntity.rateSubcontract * $scope.tender.exchangeRate, 2);
			}
		});

	}

	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};

	
	$scope.recalculateForeignAmount = function(){
		 var gridData = $scope.gridOptions.data;
		 
		 for (i in gridData){
				$scope.tender.exchangeRate;
			 gridData[i]['amountForeign'] = roundUtil.round($scope.tender.exchangeRate * gridData[i]['rateSubcontract'] * gridData[i]['quantity'], 2);
		 }
		 
		 $scope.gridApi.grid.refresh();

	}

	//Save Function
	$scope.save = function () {
		if (false === $('form[name="form-wizard-step-1"]').parsley().validate()) {
			event.preventDefault();  
			return;
		}
		
		var ta = $scope.gridOptions.data;
		var newTADetailList = [];
		console.log(ta);
		
		for (i in ta){
			var newTADetail = {
					billItem : ta[i]['billItem'],
					objectCode: ta[i]['objectCode'],
					subsidiaryCode: ta[i]['subsidiaryCode'],
					description: ta[i]['description'],
					unit: ta[i]['unit'],
					quantity: ta[i]['quantity'],
					rateSubcontract: ta[i]['rateSubcontract'],
					rateBudget: ta[i]['rateBudget'],
					amountBudget: ta[i]['amountBudget'],
					amountSubcontract: ta[i]['amountSubcontract'],
					amountForeign: ta[i]['amountForeign']
					
			}
			newTADetailList.push(newTADetail);
		}
		
		//console.log(newTADetailList);
		
		updateTenderDetails(newTADetailList);
	};

	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
		$state.reload();
	};

	$scope.$on('$locationChangeStart', function(event, $uibModalStack){
		console.log("Location changed");
		var confirmed = window.confirm("Are you sure to exit this page?");
		console.log("confirmed: "+confirmed);
		if(confirmed){
			$uibModalInstance.close();
		}
		else{
			event.preventDefault();            
		}
	});



	function loadTenderDetail(){
		getTender();
		getTenderDetailList();

	};


	function getTender(){
		tenderService.getTender($scope.jobNo, $scope.subcontractNo, $scope.vendorNo)
		.then(
				function( data ) {
					//console.log(data);
					$scope.tender = data;
					$scope.currencyCode.selected = data.currencyCode;
					$scope.statusChangeExecutionOfSC.selected = data.statusChangeExecutionOfSC;

				});
	}

	function getTenderDetailList(){
		tenderService.getTenderDetailList($scope.jobNo, $scope.subcontractNo, $scope.vendorNo)
		.then(
				function( data ) {
					console.log(data);
					$scope.gridOptions.data = data;
					$scope.gridApi.grid.refresh();
				});
	}

	function updateTenderDetails(taData) {
		tenderService.updateTenderDetails($scope.jobNo, $scope.subcontractNo, $scope.vendorNo, $scope.currencyCode.selected, $scope.tender.exchangeRate, $scope.tender.remarks, $scope.statusChangeExecutionOfSC.selected,
				taData, true)
		.then(
				function( data ) {
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						$uibModalInstance.close();
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Tender feedback rate has been updated.");
						$state.reload();
						
					}
				});
	}
	
	
}]);

