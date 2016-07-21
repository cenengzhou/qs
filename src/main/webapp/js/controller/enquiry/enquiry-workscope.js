
mainApp.controller('EnquiryWorkscopeCtrl', ['$scope' , '$rootScope', '$http', 'modalService', 'blockUI', 'unitService', 
                                   function($scope, $rootScope, $http, modalService, blockUI, unitService) {
	
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
			             { field: 'code', displayName: "Code", enableCellEdit: false },
			             { field: 'description', displayName: "Description", enableCellEdit: false }
            			 ]
	};
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	$scope.loadGridData = function(){
		unitService.getAllWorkScopes()
		    .then(function(data) {
				if(angular.isArray(data)){
					$scope.gridOptions.data = data;
				} 
		}, function(data){
			$scope.blockEnquirySubcontractor.stop();
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data ); 
		});

	}
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	$scope.loadGridData();
	
}]);