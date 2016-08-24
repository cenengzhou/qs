
mainApp.controller('EnquiryPerformanceAppraisalCtrl', ['$scope' , '$rootScope', '$http', 'modalService', 'unitService', 'GlobalParameter', 'subcontractService',
                                  function($scope , $rootScope, $http, modalService, unitService, GlobalParameter, subcontractService) {
	
	$scope.GlobalParameter = GlobalParameter;
	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			enableFullRowSelection: false,
			multiSelect: true,
			showGridFooter : true,
			enableCellEditOnFocus : false,
			allowCellFocus: false,
			enableCellSelection: false,
			exporterMenuPdf: false,
			columnDefs: [
			             { field: 'jobNumber', width:'100', displayName: "Job No", enableCellEdit: false },
			             { field: 'subcontractNumber', displayName: "Subcontract No", enableCellEdit: false },
			             { field: 'subcontractDescription', displayName: "Subcontract Description", enableCellEdit: false },
			             { field: 'reviewNumber', width:'100', displayName: "Review No", enableCellEdit: false },
			             { field: 'vendorNumber', width:'100', displayName: "Vendor No", enableCellEdit: false },
			             { field: 'vendorName', displayName: "Vendor Name", enableCellEdit: false },
			             { field: 'score', width:'100', displayName: "Performance", enableCellEdit: false },
			             { field: 'performanceGroup', displayName: "Performance Group", enableCellEdit: false },
			             { field: 'groupString', displayName: "Performance Group Description", enableCellEdit: false },
			             { field: 'statusString', width:'100',  displayName: "Status", enableCellEdit: false }
            			 ]
	};
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	$scope.performanceAppraisalGroup = [];
	$scope.loadOptionsValue = function(){
		unitService.getAppraisalPerformanceGroupMap()
		.then(function(data){
			if(angular.isObject(data)){
				angular.forEach(data, function(value, key){
					$scope.performanceAppraisalGroup.push({
					    id: key,
					    value: value
					  });
				})
			
			}
			$scope.loadGridData();
		});

	}
	$scope.loadOptionsValue();
	$scope.searchJobNo = $scope.jobNo;
	$scope.loadGridData = function(){
		subcontractService.getPerforamceAppraisalsList(
				$scope.searchJobNo,
				$scope.searchVendorNo,
				$scope.searchSubcontractNo,
				$scope.searchGroup,
				$scope.searchStatus)
				.then(function(data){
					if(angular.isObject(data)){
						$scope.addDataString(data);
						$scope.gridOptions.data = data;
					} else {
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'Cannot access Job:' + $scope.searchJobNo);
					}
				});
	}
	
	$scope.addDataString = function(data){
		angular.forEach(data, function(item){
			item.groupString = GlobalParameter.getValueById($scope.performanceAppraisalGroup, item.performanceGroup.trim());
			item.statusString = GlobalParameter.getValueById(GlobalParameter.PerformanceAppraisalStatus, item.status.trim())
		})
	}
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
//	$scope.loadGridData();
	
}]);