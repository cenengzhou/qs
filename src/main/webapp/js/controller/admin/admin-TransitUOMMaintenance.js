mainApp.controller('AdminTransitUOMMaintenanceCtrl', function($scope,
		$rootScope, $http, modalService) {
	
	$scope.onSubmit = function(){
		var type = uploadUOMForm.type.value;
		var jobNumber = uploadUOMForm.jobNumber.value;
		var formData = new FormData(document.getElementById('uploadUOMForm'));
		$http.post("gammonqs/transitUpload.smvc?type="+type+"&jobNumber="+jobNumber, formData)
		.then(function(response){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "File uploaded.");;
		}, function(response){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Status:" + response.statusText );
		});
	};
	
	$http.post("service/transit/ObtainTransitUomMatcheList").then(
			function(response) {
				$scope.gridOptions.data = response.data;
			});

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
