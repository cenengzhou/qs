mainApp.controller('AdminTransitUOMMaintenanceCtrl', function($scope,
		$rootScope, $http, modalService) {
	
	$scope.onSubmit = function(){
		var formData = new FormData();
		formData.append('files', uploadFile1.files[0]);
		formData.append('type', 'Unit Code Matching');
		formData.append('jobNumber', $scope.jobNo);
		$http({
			method: 'POST',
			url: 'gammonqs/transitUpload.smvc',
			data: formData,
            headers: {'Content-Type': undefined}
        })
		.then(function(response){
			$scope.loadData();
			var msg = response.data;
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', 
					"Success:" + msg.success + 
					"\r\nNumber Record Imported:" + msg.numRecordImported + 
					"\r\nHave warning:" + msg.haveWarning);
		}, function(response){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Status:" + response.statusText );
		});
	};
	
	$scope.loadData = function(){
		$http.post("service/transit/ObtainTransitUomMatcheList").then(
			function(response) {
				$scope.gridOptions.data = response.data;
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
		enablePaginationControls : true,
		paginationPageSizes : [ 25, 50, 100, 150, 200 ],
		paginationPageSize : 25,
		allowCellFocus : false,
		enableCellSelection : false,
		columnDefs : [ {
			field : 'causewayUom',
			displayName : "Causeway UOM",
			enableCellEdit : false
		}, {
			field : 'jdeUom',
			displayName : "JDE UOM",
			enableCellEdit : false
		} ]
	};
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	};
	
});
