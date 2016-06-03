
mainApp.controller('AdminSessionCtrl', ['$scope' , '$http', 'colorCode', function($scope , $http, colorCode) {
	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			//enableFullRowSelection: true,
			//multiSelect: true,
			showGridFooter : true,
			//showColumnFooter : true,
			//fastWatch : true,

			enableCellEditOnFocus : false,

			paginationPageSizes: [50],
			paginationPageSize: 50,
			allowCellFocus: false,
			
			//Single Filter
			/*onRegisterApi: function(gridApi){
				$scope.gridApi = gridApi;
				$scope.gridApi.grid.registerRowsProcessor( $scope.singleFilter);
			},*/
			columnDefs: [
			             { field: 'lastRequest', enableCellEdit: false, cellFilter: 'date:\'MM/dd/yyyy h:mm:ss a Z\''},
			             { field: 'principal.username', enableCellEdit: false },
			             { field: 'sessionId',enableCellEdit: false},
			             { field: 'expired', enableCellEdit: false, 
			            	 filter:{term: 'false'}
			             }
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
		 $http({
		        method: "post",
		        url: "service/InvalidateSession",
		        data: sessionIds
		    })
			.success(function(data) {
				$scope.loadGridData();
			})
			.error(function(data){
				alert("error:" + data);
			});
		}
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	$scope.loadGridData = function(){
		$http({
		    method: "post",
		    url: "service/GetSpringSessionListSP.json",
		    dataType: "application/json;charset=UTF-8",
		    })
			.success(function(data) {
				$scope.gridOptions.data = data;
			});
	}
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	$scope.loadGridData();
}]);
