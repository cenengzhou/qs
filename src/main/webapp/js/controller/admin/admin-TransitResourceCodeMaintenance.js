mainApp.controller('AdminTransitResourceCodeMaintenanceCtrl', function($scope,
		$rootScope, $http, modalService) {
	$scope.loadData = function() {
		$http.post("service/transit/ObtainTransitCodeMatcheList").then(
				function(response) {
					$scope.gridOptions.data = response.data;
				});
	};
	$scope.loadData();

	$scope.onSubmit = function() {
		var formData = new FormData();
		formData.append('files', uploadFile1.files[0]);
		formData.append('type', 'Resource Code Matching');
		formData.append('jobNumber', $scope.jobNo);
		$http({
			method : 'POST',
			url : 'gammonqs/transitUpload.smvc',
			data : formData,
			headers : {
				'Content-Type' : undefined
			}
		}).then(
				function(response) {
					$scope.loadData();
					var msg = response.data;
					modalService.open('md', 'view/message-modal.html',
							'MessageModalCtrl', 'Success', "Success:"
									+ msg.success
									+ "\r\nNumber Record Imported:"
									+ msg.numRecordImported
									+ "\r\nHave warning:" + msg.haveWarning);
				},
				function(response) {
					modalService.open('md', 'view/message-modal.html',
							'MessageModalCtrl', 'Fail', "Status:"
									+ response.statusText);
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
		paginationPageSizes : [ 25, 50, 100, 150, 200 ],
		paginationPageSize : 25,
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

});
