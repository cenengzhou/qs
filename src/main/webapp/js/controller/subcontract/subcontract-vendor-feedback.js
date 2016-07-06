mainApp.controller("SubcontractVendorFeedbackModalCtrl", ['$scope', '$location', '$uibModalInstance', 'uiGridConstants', 'modalParam', '$cookieStore', 'tenderService', '$state', 'modalService', 
                                                          function ($scope, $location, $uibModalInstance, uiGridConstants, modalParam, $cookieStore, tenderService, $state, modalService) {

	$scope.vendorNo= modalParam;
	$scope.jobNo = $cookieStore.get("jobNo");
	$scope.subcontractNo = $cookieStore.get("subcontractNo");

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

	loadTenderDetail();


	$scope.gridOptions = {
			enableSorting: true,
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			//enableRowSelection: true,
			//enableFullRowSelection: true,
			//multiSelect: false,
			//showGridFooter : true,
			showColumnFooter : true,
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
			             { field: "amountSubcontract" ,displayName:"SC Amount", cellClass: "grid-blue", enableCellEdit: true, width:90 },
			             { field: "amountForeign" ,displayName:"Amount (HKD)", enableCellEdit: false, width:100 }
			             ]


	};
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;

		gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			if(colDef.name == "rateSubcontract"){
				rowEntity.rateSubcontract  = round(newValue, 2);
				rowEntity.amountSubcontract = round(rowEntity.quantity * rowEntity.rateSubcontract, 2);
				rowEntity.amountForeign = round(rowEntity.quantity * rowEntity.rateSubcontract * $scope.tender.exchangeRate, 2);
			}
			else if(colDef.name == "amountSubcontract"){
				rowEntity.amountSubcontract = round(newValue, 2);
				rowEntity.rateSubcontract = round(rowEntity.amountSubcontract / rowEntity.quantity, 2);
				rowEntity.amountForeign = round(rowEntity.quantity * rowEntity.rateSubcontract * $scope.tender.exchangeRate, 2);
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
			 gridData[i]['amountForeign'] = round($scope.tender.exchangeRate * gridData[i]['rateSubcontract'] * gridData[i]['quantity'], 2);
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
		tenderService.updateTenderDetails($scope.jobNo, $scope.subcontractNo, $scope.vendorNo, $scope.currencyCode.selected, $scope.tender.exchangeRate, taData, true)
		.then(
				function( data ) {
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						$uibModalInstance.close();
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

