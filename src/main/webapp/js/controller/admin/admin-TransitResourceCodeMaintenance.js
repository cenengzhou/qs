mainApp.controller('AdminTransitResourceCodeMaintenanceCtrl', function($scope,
		$rootScope, $http, modalService) {
	$http.post("service/transit/ObtainTransitCodeMatcheList").then(function(response) {
		$scope.gridOptions.data = response.data;
	});

	$scope.onSubmit = function(){
		var type = uploadResourceCodeForm.type.value;
		var jobNumber = uploadResourceCodeForm.jobNumber.value;
		$http.post("gammonqs/transitUpload.smvc?type="+type+"&jobNumber="+jobNumber)
		.then(function(response){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "File uploaded.");;
		}, function(response){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Status:" + response.statusText );
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
		enablePaginationControls: true,
		paginationPageSizes: [25, 50, 100, 150, 200],
	    paginationPageSize: 25,
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
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	};
	
});
