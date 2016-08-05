
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
			paginationPageSizes : [],
			paginationPageSize : 100,
			allowCellFocus: false,
			exporterMenuPdf: false,
			enableCellSelection: false,
			columnDefs: [
			             { field: 'subcontractorNo', displayName: "Subcontractor Number", enableCellEdit: false },
			             { field: 'subcontractorName', displayName: "Subcontractor Name", enableCellEdit: false },
			             { field: 'businessRegistrationNo', displayName: "Business Registration Number", enableCellEdit: false}
            			 ]
	};
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	$scope.loadGridData = function(){
		if($scope.searchClient !== ''){
//		$scope.blockEnquiryClient.start('Loading...')
		subcontractorService.obtainClientWrappers('*' + $scope.searchClient + '*')
		    .then(function(data) {
				if(angular.isArray(data)){
					$scope.gridOptions.data = data;
//					$scope.blockEnquiryClient.stop();
				}
		}, function(data){
//			$scope.blockEnquiryClient.stop();
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data ); 
		})
		} else {
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'Please enter client number or name to search' ); 
		}

	}
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
}]);