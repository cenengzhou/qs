mainApp.controller('AdminTransitResourceCodeMaintenanceCtrl', 
		['$scope', '$http', 'modalService', 'blockUI', 'transitService',
		 function($scope, $http, modalService, blockUI, transitService) {
	
		$scope.blockCodeMaintenance = blockUI.instances.get('blockCodeMaintenance');
		$scope.loadData = function() {
//		$scope.blockCodeMaintenance.start('Loading...')
		transitService.obtainTransitCodeMatcheList().then(
				function(data) {
//					$scope.blockCodeMaintenance.stop();
					$scope.gridOptions.data = data;
				});
	};
	$scope.loadData();

	$scope.onSubmit = function() {
		var formData = new FormData();
		formData.append('files', uploadFile1.files[0]);
		formData.append('type', 'Resource Code Matching');
		formData.append('jobNumber', $scope.jobNo);
		transitService.transitUpload(formData)
		.then(
				function(data) {
					uploadFile1.value = null;
					$scope.loadData();
					var msg = data;
					modalService.open('md', 'view/message-modal.html',
							'MessageModalCtrl', 'Success', "Success:"
									+ msg.success
									+ "\r\nNumber Record Imported:"
									+ msg.numRecordImported
									+ "\r\nHave warning:" + msg.haveWarning);
				},
				function(data) {
					modalService.open('md', 'view/message-modal.html',
							'MessageModalCtrl', 'Fail', data);
				});
	};

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
		columnDefs : [ {
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
	};

	$scope.gridOptions.onRegisterApi = function(gridApi) {
		$scope.gridApi = gridApi;
	};

}]);
