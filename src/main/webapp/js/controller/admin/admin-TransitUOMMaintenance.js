mainApp.controller('AdminTransitUOMMaintenanceCtrl', 
		['$scope', '$rootScope', '$http', 'modalService', 'blockUI', 'transitService',
		function($scope, $rootScope, $http, modalService, blockUI, transitService) {
	
//	$scope.blockUOM = blockUI.instances.get('blockUOM');
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
//		$scope.blockUOM.start('Loading...')
		transitService.obtainTransitUomMatcheList().then(
			function(data) {
//				$scope.blockUOM.stop();
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
