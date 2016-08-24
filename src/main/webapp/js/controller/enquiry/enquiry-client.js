
mainApp.controller('EnquiryClientCtrl', ['$scope' , '$rootScope', '$http', 'modalService', 'blockUI', 'subcontractorService', 
                                function($scope , $rootScope, $http, modalService, blockUI, subcontractorService) {
	
//	$scope.blockEnquiryClient = blockUI.instances.get('blockEnquiryClient');
	$scope.searchClient = '';
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
			exporterMenuPdf: false,
			enableCellSelection: false,
			columnDefs: [
			             { field: 'subcontractorNo', displayName: "Client Number", enableCellEdit: false },
			             { field: 'subcontractorName', displayName: "Client Name", enableCellEdit: false }
            			 ]
	};
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	$scope.loadGridData = function(){
//		$scope.blockEnquiryClient.start('Loading...')
		subcontractorService.obtainClientWrappers('*')
		    .then(function(data) {
				if(angular.isArray(data)){
					$scope.gridOptions.data = data;
//					$scope.blockEnquiryClient.stop();
				}
		}, function(data){
//			$scope.blockEnquiryClient.stop();
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data ); 
		});
	}
	$scope.loadGridData();
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
}]);