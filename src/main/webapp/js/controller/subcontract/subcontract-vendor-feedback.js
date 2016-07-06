mainApp.controller("SubcontractVendorFeedbackModalCtrl", ['$scope', '$location', '$uibModalInstance', 'uiGridConstants', 'modalParam', '$cookieStore', 'tenderService', '$state', 'modalService', 
                                                          function ($scope, $location, $uibModalInstance, uiGridConstants, modalParam, $cookieStore, tenderService, $state, modalService) {

	$scope.vendorNo= modalParam;
	$scope.jobNo = $cookieStore.get("jobNo");
	$scope.subcontractNo = $cookieStore.get("subcontractNo");

	$scope.currencyCode = {
			options: [
			          "HKD",
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
			          selected: ""
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

			//Single Filter
			onRegisterApi: function(gridApi){
				$scope.gridApi = gridApi;

				gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
					//Do your REST call here via $http.get or $http.post

					//Alert to show what info about the edit is available
					rowEntity.feedbackRateHK = rowEntity.feedbackRate * $scope.exchangeRate;
				});

			},


			columnDefs: [
			             { field: "billItem", displayName:"B/P/I", enableCellEdit: false, width:50},
			             { field: "objectCode", enableCellEdit: false, width:60 },
			             { field: "subsidiaryCode" , enableCellEdit: false, width:80 },
			             { field: "description" , enableCellEdit: false,  width:150 },
			             { field: "unit" , enableCellEdit: false, width:50 },
			             { field: "quantity" , enableCellEdit: false},
			             { field: "rateBudget" , displayName:"Budget Rate", enableCellEdit: false, width:90 },
			             { field: "rateSubcontract" , displayName:"SC Rate", enableCellEdit: true, cellClass: "grid-theme-blue", width:120 },
			             { field: "rateForeign" ,displayName:"Foreign Rate", cellClass: "grid-blue", enableCellEdit: false, width:170 }
			             ]


	};

	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};


	//Save Function
	$scope.save = function () {
		var ta = $scope.gridOptions.data;
		console.log(ta);
		
		var newTADetailList = [];
		
		for (i in ta){
			var newTADetail = {
					billItem : ta[i]['billItem'],
					objectCode: ta[i]['objectCode'],
					subsidiaryCode: ta[i]['subsidiaryCode'],
					unit: ta[i]['unit'],
					quantity: ta[i]['quantity'],
					rateSubcontract: ta[i]['rateSubcontract'],
					rateBudget: ta[i]['rateBudget']
			}
			newTADetailList.push(newTADetail);
		}
		
		console.log(newTADetailList);
		
		updateTenderDetails(ta);
		$uibModalInstance.close();
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
		console.log($scope.jobNo, $scope.subcontractNo, $scope.vendorNo);
		tenderService.getTenderDetailList($scope.jobNo, $scope.subcontractNo, $scope.vendorNo)
		.then(
				function( data ) {
					//console.log(data);
					$scope.gridOptions.data = data;
				});
	}

	function updateTenderDetails(taData) {
		tenderService.updateTenderDetails($scope.jobNo, $scope.subcontractNo, $scope.vendorNo, "", $scope.tender.exchangeRate, taData, true)
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

