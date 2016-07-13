mainApp.controller('AdminTransitUOMMaintenanceCtrl', 
		['$scope', '$rootScope', '$http', 'modalService', 'transitService',
		function($scope, $rootScope, $http, modalService, transitService) {
	
	$scope.onSubmit = function(){
		var formData = new FormData();
		formData.append('files', uploadFile1.files[0]);
		formData.append('type', 'Unit Code Matching');
		formData.append('jobNumber', $scope.jobNo);
		transitService.transitUpload(formData)
		.then(function(data){
			uploadFile1.value = null;
			$scope.loadData();
			var msg = data;
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', 
					"Success:" + msg.success + 
					"\r\nNumber Record Imported:" + msg.numRecordImported + 
					"\r\nHave warning:" + msg.haveWarning);
		}, function(data){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data );
		});
	};
	
	$scope.loadData = function(){
		transitService.obtainTransitUomMatcheList().then(
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
	
}]);
