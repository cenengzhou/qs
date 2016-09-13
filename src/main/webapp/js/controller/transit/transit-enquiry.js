mainApp.controller('TransitEnquiryCtrl', ['$scope', 'transitService', 'modalService', '$uibModalInstance', 'GlobalParameter',
                                            function($scope, transitService, modalService, $uibModalInstance, GlobalParameter) {

	
	getIncompleteTransitList();


	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			multiSelect: true,
			enableCellEditOnFocus : true,
			exporterMenuPdf: false,


			columnDefs: [
			             { field: 'jobNumber'},
			             { field: 'estimateNo'},
			             { field: 'jobDescription'},
			             { field: 'company'},
			             { field: 'status'},
			             { field: 'matchingCode'},
			             { field: 'lastModifiedUser'},
			             { field: 'lastModifiedDate', enableFiltering: false, cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"'}
			             ]
	};

	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;
	}



	function getIncompleteTransitList() {
		transitService.getIncompleteTransitList()
		.then(
				function( data ) {
					$scope.gridOptions.data= data;
				});
	}


	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
	};

	//Listen for location changes and call the callback
	$scope.$on('$locationChangeStart', function(event){
		$uibModalInstance.close();
	});

}]);