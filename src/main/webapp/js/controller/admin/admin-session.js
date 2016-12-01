
mainApp.controller('AdminSessionCtrl', 
		['$scope' , '$http', '$timeout', 'colorCode', 'SessionHelper', 'rootscopeService', 'GlobalParameter', '$q',
		 function($scope , $http, $timeout, colorCode, SessionHelper, rootscopeService, GlobalParameter, $q) {
	rootscopeService.setSelectedTips('');
	rootscopeService.gettingSessionId()
	.then(function(response){
		$scope.sessionId = response.sessionId;
	});
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
			isRowSelectable: function(row){
				 if(row.entity.sessionId === $scope.sessionId){
					 return false;
				 } else {
					 return true;
				 }
			},
			columnDefs: [
			             { field: 'principal.fullname', displayName: "Name", enableCellEdit: false },
			             { field: 'authType', displayName: "AuthType", enableCellEdit: false },
			             { field: 'sessionId', displayName: "Session Id", enableCellEdit: false},
			             { field: 'creationTime', enableCellEdit: false, cellFilter: 'date:"' + GlobalParameter.DATETIME_FORMAT +'"'},
			             { field: 'lastAccessedTime', enableCellEdit: false, cellFilter: 'date:"' + GlobalParameter.DATETIME_FORMAT +'"'},
			             { field: 'lastRequest', enableCellEdit: false, cellFilter: 'date:"' + GlobalParameter.DATETIME_FORMAT +'"'},
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
					$timeout(function(){
						$scope.loadGridData();
					}, 500);
				}
			});
		}
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	$scope.loadGridData = function(){
		rootscopeService.gettingSessionId()
		.then(function(data){
			SessionHelper.getSessionList()
		    .then(function(data) {
				if(angular.isArray(data)){
					$scope.gridOptions.data = data;
				}
			});			
		})

	}
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	$scope.loadGridData();
}]);
