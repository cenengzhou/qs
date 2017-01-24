
mainApp.controller('EnquiryPurchaseOrderDetailCtrl', ['$scope' , '$http', 'modalService', 'blockUI', 'GlobalParameter', 'GlobalHelper',
                                  function($scope , $http, modalService, blockUI, GlobalParameter, GlobalHelper) {
	
	$scope.blockEnquiryPurchaseOrderDetail = blockUI.instances.get('blockEnquiryPurchaseOrderDetail');
	$scope.blockEnquiryPurchaseOrderDetail.start('Under Construction');
	
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
			allowCellFocus: false,
			enableCellSelection: false,
			columnDefs: [
//			             { field: 'principal.fullname', displayName: "Name", enableCellEdit: false },
//			             { field: 'authType', displayName: "AuthType", enableCellEdit: false },
//			             { field: 'sessionId', displayName: "Session Id", enableCellEdit: false},
//			             { field: 'creationTime', enableCellEdit: false, cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"'},
//			             { field: 'lastAccessedTime', enableCellEdit: false, cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"'},
//			             { field: 'lastRequest', enableCellEdit: false, cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"'},
//			             { field: 'maxInactiveInterval', enableCellEdit: false},
            			 ]
	};
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}

	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	
}]);