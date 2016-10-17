
mainApp.controller('EnquiryPurchaseOrderDetailCtrl', ['$scope' , '$rootScope', '$http', 'modalService', 'blockUI', 'SessionHelper', 'GlobalParameter', 'GlobalHelper',
                                  function($scope , $rootScope, $http, modalService, blockUI, SessionHelper, GlobalParameter, GlobalHelper) {
	
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
	
	$scope.loadGridData = function(){
		SessionHelper.getCurrentSessionId()
		.then(function(data){
			$rootScope.sessionId = data;
			SessionHelper.getSessionList()
		    .then(function(data) {
				if(angular.isArray(data)){
					$scope.gridOptions.data = data;
				} else {
					SessionHelper.getCurrentSessionId().then;
				}
			});			
		})

	}
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
//	$scope.loadGridData();
	
}]);