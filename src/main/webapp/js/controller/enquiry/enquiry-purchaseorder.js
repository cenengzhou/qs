
mainApp.controller('EnquiryPurchaseOrderCtrl', ['$scope' , '$rootScope', '$http', 'modalService', 'blockUI', 'SessionHelper', 
                                  function($scope , $rootScope, $http, modalService, blockUI, SessionHelper) {
	
	$scope.blockEnquiryPurchaseOrder = blockUI.instances.get('blockEnquiryPurchaseOrder');
	$scope.blockEnquiryPurchaseOrder.start('Under Construction');
	
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
			paginationPageSizes : [ ],
			paginationPageSize : 100,
			allowCellFocus: false,
			enableCellSelection: false,
			columnDefs: [
//			             { field: 'principal.UserName', displayName: "Name", enableCellEdit: false },
//			             { field: 'authType', displayName: "AuthType", enableCellEdit: false },
//			             { field: 'sessionId', displayName: "Session Id", enableCellEdit: false},
//			             { field: 'creationTime', enableCellEdit: false, cellFilter: 'date:\'MM/dd/yyyy h:mm:ss a Z\''},
//			             { field: 'lastAccessedTime', enableCellEdit: false, cellFilter: 'date:\'MM/dd/yyyy h:mm:ss a Z\''},
//			             { field: 'lastRequest', enableCellEdit: false, cellFilter: 'date:\'MM/dd/yyyy h:mm:ss a Z\''},
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