
mainApp.controller('EnquiryWorkScopeCtrl', ['$scope' , '$rootScope', '$http', 'modalService', 'blockUI', 'unitService', 
                                   function($scope, $rootScope, $http, modalService, blockUI, unitService) {
	
	$scope.blockEnquiryWorkScope = blockUI.instances.get('blockEnquiryWorkScope');
	
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
			exporterMenuPdf: false,
			columnDefs: [
			             { field: 'code', displayName: "Code", enableCellEdit: false },
			             { field: 'description', displayName: "Description", enableCellEdit: false }
            			 ]
	};
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	$scope.loadGridData = function(){
		$scope.blockEnquiryWorkScope.start('Loading...');
		unitService.getAllWorkScopes()
		    .then(function(data) {
				if(angular.isArray(data)){
					$scope.gridOptions.data = data;
				} 
				$scope.blockEnquiryWorkScope.stop();
		}, function(data){
			$scope.blockEnquiryWorkScope.stop();
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data ); 
		});

	}
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	$scope.loadGridData();
	
}]);