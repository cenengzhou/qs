mainApp.controller('CodeMatchEnquiryModalCtrl', 
		['$scope', '$http', 'modalService', 'blockUI', 'transitService', 'modalStatus', '$uibModalInstance',
		 function($scope, $http, modalService, blockUI, transitService, modalStatus, $uibModalInstance) {
	$scope.type = modalStatus;
	switch(modalStatus){
	case 'Resource':
		$scope.loadFn = transitService.obtainTransitCodeMatcheList;
		$scope.colDef = [ {
			field : 'matchingType',
			displayName : "Matching Type",
			enableCellEdit : false
		}, {
			field : 'resourceCode',
			displayName : "Resource Code",
			enableCellEdit : false
		}, {
			field : 'objectCode',
			displayName : "Object Code",
			enableCellEdit : false
		}, {
			field : 'subsidiaryCode',
			displayName : "Subsidiary Code",
			enableCellEdit : false
		} ]
		break;
	case 'Unit':
		$scope.loadFn = transitService.obtainTransitUomMatcheList;
		$scope.colDef = [ {
			field : 'causewayUom',
			displayName : "Causeway UOM",
			enableCellEdit : false
		}, {
			field : 'jdeUom',
			displayName : "JDE UOM",
			enableCellEdit : false
		} ]
		break;
	}
	
	
	$scope.loadData = function() {
		$scope.loadFn().then(
			function(data) {
				$scope.gridOptions.data = data;
			});
	};
	$scope.loadData();

	$scope.gridOptions = {
		enableFiltering : true,
		enableColumnResizing : true,
		enableGridMenu : true,
		enableRowSelection : true,
		enableSelectAll : true,
		enableFullRowSelection : false,
		multiSelect : true,
		showGridFooter : true,
		enableCellEditOnFocus : false,
		allowCellFocus : false,
		enableCellSelection : false,
		enablePaginationControls : true,
		exporterMenuPdf: false,
		columnDefs : $scope.colDef
	};

	$scope.gridOptions.onRegisterApi = function(gridApi) {
		$scope.gridApi = gridApi;
	};

	//Close Window for Enquiry Screen
	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
	};

}]);
