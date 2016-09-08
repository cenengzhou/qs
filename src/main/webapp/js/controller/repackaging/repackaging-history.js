mainApp.controller("RepackagingHistoryModalCtrl", ['$scope', '$location', 'repackagingService', '$cookies', '$uibModalInstance',
                                                 function ($scope, $location, repackagingService, $cookies, $uibModalInstance) {

	
	$scope.jobNo = $cookies.get("jobNo");
	
	getRepackagingListByJobNo();
	

	$scope.gridOptions = {
			enableSorting: true,
			enableFiltering: false,
			enableColumnResizing : true,
			enableGridMenu : true,
			multiSelect: false,
			enableColumnMoving: false,
			showColumnFooter : false,
			exporterMenuPdf: false,
			enableCellEditOnFocus : true,


			columnDefs: [
			             { field: 'repackagingVersion', displayName: "Version", width: 80},
			             { field: 'createDate', width: 100},
			             { field: 'lastModifiedUser', width: 120},
		            	 { field: 'totalResourceAllowance', displayName: "Total Budget", cellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;">{{COL_FIELD| number:2}}</div>'},
		            	 { field: 'status', width: 60},
		            	 { field: 'statusDescription', width: 100},
		            	 { field: 'remarks', width: 150},
		            	 { field: 'id', width: 50, visible: false}
		            	 ]

	};

	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;
	}
	
	$scope.viewRepackaging = function(){
		var selectedRows = $scope.gridApi.selection.getSelectedRows();
		if(selectedRows.length == 0){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select row to delete.");
			return;
		}else{
			$scope.$emit("GetSelectedRepackagingVersion", selectedRows[0]['id']);
			$uibModalInstance.close();
		}
		
	}
	
	function getRepackagingListByJobNo(){
		repackagingService.getRepackagingListByJobNo($scope.jobNo)
		.then(
				function( data ) {
					$scope.gridOptions.data = data;
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


