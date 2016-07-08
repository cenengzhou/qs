mainApp.controller('TenderVarianceCtrl', ['$scope', 'tenderVarianceService', 'tenderService', 'subcontractService', 'modalService', '$state',
                                         function ($scope, tenderVarianceService, tenderService, subcontractService, modalService, $state) {
	
	getSubcontract();
	getRecommendedTender();

	$scope.gridOptions = {
			enableSorting: false,
			enableFiltering: false,
			enableColumnResizing : true,
			enableGridMenu : true,

			enableCellEditOnFocus : true,
			exporterMenuPdf: false,
			rowEditWaitInterval :-1,

			columnDefs: [
						{ field: 'id', visible: false},
			             { field: 'clause', displayName: " ", 
							cellTemplate: '<div class="ui-grid-cell-contents2">{{grid.getCellValue(row, col)}}</div>'}, 
			             { field: 'generalCondition', displayName: "General Condition of Sub-Contract",
			            	 cellTemplate: '<div class="ui-grid-cell-contents2">{{grid.getCellValue(row, col)}}</div>'}, 
			             { field: 'proposedVariance', displayName: "Posted Variance to the General Conditioin Condition",
			            	 cellTemplate: '<div class="ui-grid-cell-contents2">{{grid.getCellValue(row, col)}}</div>'}, 
			             { field: 'reason', displayName: "Reason for the Variance",
		            		 cellTemplate: '<div class="ui-grid-cell-contents2">{{grid.getCellValue(row, col)}}</div>'}, 
			             { name: 'Action',
			            	 cellTemplate:'<button class="btn btn-app btn-success m-t-10" ng-click="grid.appScope.deleteRow(row)"><i class="fa fa-remove"></i> Delete </button>',
		                 }
			             ]
	
	};

	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;
	}

	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	
	$scope.addData = function() {
		$scope.gridOptions.data.push({
			"clause": "",
			"generalCondition": "",
			"proposedVariance": "",
			"reason": ""
		});
	};
	
	$scope.deleteRow = function(row) {
		var index = $scope.gridOptions.data.indexOf(row.entity);
		  $scope.gridOptions.data.splice(index, 1);
	};


	//Save Function
	$scope.save = function () {
		var ta = $scope.gridOptions.data;
		
		if($scope.tenderNo ==null){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select a recommended tener before doing tender variance.");
		}else{
			createTenderVariance(ta);
		}
	};

	
	function getSubcontract(){
		subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					$scope.subcontract = data;
				});
	}
	

	function getRecommendedTender() {
		tenderService.getRecommendedTender($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					if(data.length==0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "No recommended tener exists.");
					}else{
						$scope.tender = data;
						getTenderVarianceList($scope.tender.vendorNo);
					}
				});
	}

	function getTenderVarianceList(tenderNo) {
		tenderVarianceService.getTenderVarianceList($scope.jobNo, $scope.subcontractNo, tenderNo)
		.then(
				function( data ) {
					$scope.gridOptions.data = data;
				});
	}
	
	function createTenderVariance(tenderVarianceList) {
		tenderVarianceService.createTenderVariance($scope.jobNo, $scope.subcontractNo, $scope.tender.vendorNo, tenderVarianceList)
		.then(
				function( data ) {
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Tender Variance has been updated.");
						$state.reload();
					}
				});
	}


}]);

