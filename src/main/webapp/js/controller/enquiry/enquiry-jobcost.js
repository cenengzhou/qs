
mainApp.controller('EnquiryJobCostCtrl', ['$scope' , '$rootScope', '$http', 'modalService', 'blockUI', 'GlobalParameter', 'adlService', 
                                  function($scope , $rootScope, $http,  modalService, blockUI, GlobalParameter, adlService) {
	$scope.GlobalParameter = GlobalParameter;
	$scope.blockJobCost = blockUI.instances.get('blockJobCost');
	
	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			enableFullRowSelection: false,
			multiSelect: true,
			showGridFooter : true,
			enableCellEditOnFocus : false,
			paginationPageSizes : [ 25, 50, 100, 150, 200 ],
			paginationPageSize : 25,
			allowCellFocus: false,
			enableCellSelection: false,
			columnDefs: [
			             { field: 'accountMaster.accountCodeKey', displayName: 'Account Code Key', enableCellEdit: false },
			             { field: 'fiscalYear', displayName: 'Fiscal Year', enableCellEdit: false },
			             { field: 'accountPeriod', displayName: 'Account Period', enableCellEdit: false},
			             { field: 'entityBusinessUnitKey', displayName: 'Entity Business Unit Key', enableCellEdit: false},
			             { field: 'accountObject', displayName: 'Account Object', enableCellEdit: false},
			             { field: 'accountSubsidiary', displayName: 'Account Subsidiary', enableCellEdit: false},
			             { field: 'accountDescription', displayName: 'Account Description', enableCellEdit: false},
			             { field: 'currencyLocal', displayName: 'Currency', enableCellEdit: false},
			             { field: 'aaAmountPeriod', displayName: 'AA Amount Period', enableCellEdit: false},
			             { field: 'jiAmountPeriod', displayName: 'JI Amount Period', enableCellEdit: false},
			             { field: 'aaAmountAccum', displayName: 'AA Amount Accum', cellClass: 'text-right', cellFilter: 'number:4', enableCellEdit: false},
			             { field: 'jiAmountAccum', displayName: 'JI Amount Accum', cellClass: 'text-right', cellFilter: 'number:4', enableCellEdit: false}
            			 ]
	};
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	$scope.loadGridData = function(){
		$scope.blockJobCost.start('Loading...')
		adlService.getMonthlyJobCostList($scope.jobNo, $scope.searchSubcontractNo, $scope.searchYear, $scope.searchMonth)
		    .then(function(data) {
				if(angular.isArray(data)){
					$scope.gridOptions.data = data;
				} 
				$scope.blockJobCost.stop();
			}, function(data){
			$scope.blockAccountLedger.stop();
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data ); 
		});
	}
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	$scope.loadGridData();
	
}]);