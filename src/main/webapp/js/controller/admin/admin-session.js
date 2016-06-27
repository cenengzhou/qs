
mainApp.controller('AdminSessionCtrl', ['$scope' , '$http', 'colorCode', 'SessionHelper', '$rootScope', function($scope , $http, colorCode, SessionHelper, $rootScope) {
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
			paginationPageSizes: [50],
			paginationPageSize: 50,
			allowCellFocus: false,
			enableCellSelection: false,
			isRowSelectable: function(row){
				if(row.entity.sessionId === $rootScope.sessionId){
					return false;
				}
				return true;
			},
			columnDefs: [
			             { field: 'principal.UserName', displayName: "Name", enableCellEdit: false },
			             { field: 'authType', displayName: "AuthType", enableCellEdit: false },
			             { field: 'sessionId', displayName: "Session Id", enableCellEdit: false},
			             { field: 'creationTime', enableCellEdit: false, cellFilter: 'date:\'MM/dd/yyyy h:mm:ss a Z\''},
			             { field: 'lastAccessedTime', enableCellEdit: false, cellFilter: 'date:\'MM/dd/yyyy h:mm:ss a Z\''},
			             { field: 'lastRequest', enableCellEdit: false, cellFilter: 'date:\'MM/dd/yyyy h:mm:ss a Z\''},
			             { field: 'maxInactiveInterval', enableCellEdit: false},
            			 ]
	};
	 
	 $scope.expiredSelected = function(){
//		  angular.forEach($scope.gridApi.selection.getSelectedRows(), function (data, index) {
//		    $scope.gridOptions.data.splice($scope.gridOptions.data.lastIndexOf(data), 1);
//		  });
		 var selectedRows = $scope.gridApi.selection.getSelectedRows();
			var sessionIds = new Array(selectedRows.length)
			for(var i=0; i<selectedRows.length;i++){
				sessionIds[i] = selectedRows[i].sessionId;
			}
			SessionHelper.invalidateSessionList(sessionIds)
			.then(function(data){
				if(data){
					$scope.loadGridData();
				}
			});
		}
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	$scope.loadGridData = function(){
		SessionHelper.getCurrentSessionId()
		.then(function(data){
			$rootScope.sessionId = data;
			$http.post('service/GetSessionList')
		    .then(function(response) {
				if(angular.isArray(response.data)){
					$scope.gridOptions.data = response.data;
				} else {
					SessionHelper.getCurrentSessionId().then;
				}
			});			
		})

	}
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	$scope.loadGridData();
}]);
