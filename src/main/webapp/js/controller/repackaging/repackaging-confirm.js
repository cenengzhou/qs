mainApp.controller('RepackagingConfirmModalCtrl', ['$scope' ,'modalService', 'repackagingService', '$cookieStore',  'uiGridConstants', '$uibModalInstance',
                                             function($scope, modalService, repackagingService, $cookieStore, uiGridConstants, $uibModalInstance) {
	$scope.jobNo = $cookieStore.get("jobNo");
	$scope.jobDescription = $cookieStore.get("jobDescription");


	$scope.repackagingId = $cookieStore.get("repackagingId");
	
	$scope.showChangesOnly = true;
	getRepackagingDetails(true);
	
	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			multiSelect: true,
			enableCellEditOnFocus : true,
			showColumnFooter : true,
			showGridFooter : false,
			//showColumnFooter : true,
			exporterMenuPdf: false,

			columnDefs: [
			             { field: 'packageNo', displayName: "Subcontract No.", width: 60},
			             { field: 'objectCode', width: 80},
			             { field: 'subsidiaryCode', width: 80},
			             { field: 'description', width: 100},
			             { field: 'unit', enableFiltering: false, width: 60},
			             /*{ field: 'rate', enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2'},*/
			             { field: 'previousAmount', enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2',
			            	 aggregationType: uiGridConstants.aggregationTypes.sum,
			            	 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'
			             },
			             { field: 'amount', enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2',
			            	 aggregationType: uiGridConstants.aggregationTypes.sum,
			            	 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'
			             },
			             { field: 'variance', enableFiltering: false, 
			            	cellClass: 'text-right', cellFilter: 'number:2',
			            	 aggregationType: uiGridConstants.aggregationTypes.sum,
			            	 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'
			             },
			             { field: 'resourceType', displayName: "Type", width: 60}
			           ]
	};

	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;
	}

	$scope.confirm = function() {
		repackagingService.confirmAndPostRepackaingDetails($scope.repackagingId)
		.then(
				function( data ) {
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Repackaging has been confirmed.");
						//$state.reload();
					}
				});
	}
	
	$scope.refreshData = function(){
		getRepackagingDetails($scope.showChangesOnly);
	}
	
	
	function getRepackagingDetails(showChangesOnly) {
		repackagingService.getRepackagingDetails($scope.repackagingId, showChangesOnly)
		.then(
				function( data ) {
					console.log(data);
					$scope.gridOptions.data= data.currentPageContentList;
					$scope.totalBudget = data.totalBudget;
					$scope.previousBudget = data.previousBudget; 
					$scope.previousSellingValue = data.previousSellingValue;
					$scope.totalSellingValue = data.totalSellingValue;
					angular.forEach($scope.gridOptions.data, function(value, key){
						value.variance = value.amount - value.previousAmount;

					});
				});
	}
	
	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
	};


}]);