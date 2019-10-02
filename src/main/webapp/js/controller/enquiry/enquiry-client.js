
mainApp.controller('EnquiryClientCtrl', ['$scope' , '$http', 'modalService', 'blockUI', 'rootscopeService', 'GlobalHelper',
                                function($scope , $http, modalService, blockUI, rootscopeService, GlobalHelper) {
	
//	$scope.blockEnquiryClient = blockUI.instances.get('blockEnquiryClient');
	$scope.searchClient = '';
	$scope.GlobalHelper = GlobalHelper;
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
			rowTemplate: GlobalHelper.addressBookRowTemplate('addressBookName', 'addressBookNumber'),
			columnDefs: [
			             { field: 'addressBookNumber', displayName: "Client Number", enableCellEdit: false },
			             { field: 'addressBookName', displayName: "Client Name", enableCellEdit: false }
            			 ]
	};
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	$scope.loadGridData = function(){
		rootscopeService.gettingAddressBookListOfClient($scope.searchClient)
		    .then(function(response) {
				if(angular.isArray(response.addressBookListOfClient)){
					$scope.gridOptions.data = response.addressBookListOfClient;
				}
		}, function(data){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data ); 
		});
	}

	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
}]);