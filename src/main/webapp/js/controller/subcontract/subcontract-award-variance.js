mainApp.controller('TenderVarianceCtrl', ['$scope', 'tenderVarianceService', 'tenderService', 'subcontractService', 'modalService', '$state',
                                         function ($scope, tenderVarianceService, tenderService, subcontractService, modalService, $state) {
	
	loadData();

	$scope.gridOptions = {
			enableSorting: false,
			enableFiltering: false,
			enableColumnResizing : true,
			enableGridMenu : true,

			enableCellEditOnFocus : true,
			exporterMenuPdf: false,
			rowEditWaitInterval :-1,
			rowHeight: 80,
			
			columnDefs: [
						{ field: 'id', visible: false},
			             { field: 'clause', displayName: " ", width: '20%' }, 
			             { field: 'generalCondition', displayName: "General Condition of Sub-Contract", width: '20%',
			            	 cellTooltip: function(row){ return row.entity.generalCondition; },
			            	 cellTemplate: '<div class="ui-grid-cell-contents wrap" white-space: normal title="TOOLTIP">{{COL_FIELD CUSTOM_FILTERS}}</div>'
			            },
			             { field: 'proposedVariance', displayName: "Proposed Variance to the General Conditioin", width: '30%',
			            	 cellTooltip: function(row){ return row.entity.proposedVariance; },
			            	 cellTemplate: '<div class="ui-grid-cell-contents wrap" white-space: normal title="TOOLTIP">{{COL_FIELD CUSTOM_FILTERS}}</div>'
			            },
			             { field: 'reason', displayName: "Reason for the Variance", width: '20%',
			            	cellTooltip: function(row){ return row.entity.reason; },
			            	 cellTemplate: '<div class="ui-grid-cell-contents wrap" white-space: normal title="TOOLTIP">{{COL_FIELD CUSTOM_FILTERS}}</div>'
			             },
			             { name: 'Action', width: '10%',
			            	 cellTemplate:'<button class="btn btn-app btn-success m-t-10" ng-click="grid.appScope.deleteRow(row)"><i class="fa fa-remove"></i> Delete </button>',
		                 }
			             ]
			/*columnDefs: [
							{ field: 'id', visible: false},
				             { field: 'clause', displayName: " ", 
								}, 
				             { field: 'generalCondition', displayName: "General Condition of Sub-Contract",
				            	 }, 
				             { field: 'proposedVariance', displayName: "Proposed Variance to the General Conditioin",
				            	 }, 
				             { field: 'reason', displayName: "Reason for the Variance",
			            		 }, 
				             { name: 'Action',
				            	 cellTemplate:'<button class="btn btn-app btn-success m-t-10" ng-click="grid.appScope.deleteRow(row)"><i class="fa fa-remove"></i> Delete </button>',
			                 }
				             ]*/
	
	};

	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;
	}

	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	
	$scope.addData = function() {
		$scope.gridOptions.data.push({
			"clause": "",
			"generalCondition": "",
			"proposedVariance": "",
			"reason": ""
		});
	};
	
	$scope.deleteRow = function(row) {
		var index = $scope.gridOptions.data.indexOf(row.entity);
		  $scope.gridOptions.data.splice(index, 1);
	};


	//Save Function
	$scope.save = function () {
		if($scope.subcontractNo!="" && $scope.subcontractNo!=null){
			var ta = $scope.gridOptions.data;

			if($scope.tender ==null || $scope.tender.vendorNo ==null){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select a recommended tender before doing tender variance.");
			}else{
				createTenderVariance(ta);
			}
		}else{
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Subcontract does not exist.");
		}
	};

	function loadData(){
		if($scope.subcontractNo!="" && $scope.subcontractNo!=null){
			getSubcontract();
			getRecommendedTender();
		}
	}
	
	function getSubcontract(){
		subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					$scope.subcontract = data;
					
					if($scope.subcontract.scStatus =="330" || $scope.subcontract.scStatus =="500")
						$scope.disableButtons = true;
					else
						$scope.disableButtons = false;
				});
	}
	

	function getRecommendedTender() {
		tenderService.getRecommendedTender($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					if(data.length==0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select a tenderer before doing tender variance.");
					}else{
						$scope.tender = data;
						getTenderVarianceList($scope.tender.vendorNo);
					}
				});
	}

	function getTenderVarianceList(tenderNo) {
		tenderVarianceService.getTenderVarianceList($scope.jobNo, $scope.subcontractNo, tenderNo)
		.then(
				function( data ) {
					$scope.gridOptions.data = data;
				});
	}
	
	function createTenderVariance(tenderVarianceList) {
		tenderVarianceService.createTenderVariance($scope.jobNo, $scope.subcontractNo, $scope.tender.vendorNo, tenderVarianceList)
		.then(
				function( data ) {
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Tender Variance has been updated.");
						$state.reload();
					}
				});
	}


}]);

