mainApp.controller('AddendumDetailUpdateCtrl', ['$scope', 'resourceSummaryService', 'subcontractService', 'addendumService', 'repackagingService', 'modalService', '$state', '$cookies', '$uibModalInstance',
                                            function($scope, resourceSummaryService, subcontractService, addendumService, repackagingService, modalService, $state, $cookies, $uibModalInstance) {

	var jobNo = $cookies.get('jobNo');
	var subcontractNo = $cookies.get('subcontractNo');
	var addendumNo = $cookies.get('addendumNo');

	var addendumDetailHeaderRef = $cookies.get('addendumDetailHeaderRef');

	getSCDetailForAddendumUpdate();

	$scope.gridOptions = {
			enableSorting: true,
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableColumnMoving: true,
			//enableRowSelection: true,
			//enableFullRowSelection: true,
			//multiSelect: false,
			//showGridFooter : true,
			showColumnFooter : true,
			//fastWatch : true,

			exporterMenuPdf: false,
			
			//Single Filter
			onRegisterApi: function(gridApi){
				$scope.gridApi = gridApi;
			},
	
	
			columnDefs: [
			             { field: 'lineType', width: 50},
			             { field: 'billItem', width: 100},
			             { field: 'description', width: 100},
			             { field: 'quantity', width: 100},
			             {field: 'toBeApprovedQuantity', width: 100},

			             {field: 'costRate', width: 80},
			             {field: 'scRate', width: 80},
			             {field: 'toBeApprovedRate', width: 80},
			             {field: 'objectCode', width: 100},
			             {field: 'subsidiaryCode', width: 100},

			             {field: 'amountBudget', displayName: "Budget Amount", width: 150},
			             {field: 'amountSubcontract', displayName: "SC Amount", width: 150},
			             {field: 'amountSubcontractTBA', displayName: "TBA Amount", width: 150},

			             { field: 'amountCumulativeWD', displayName: "Cum WD Amount", width: 150},
			             { field: 'amountPostedWD', displayName: "Posted WD Amount", width: 150},
			             { field: 'amountCumulativeCert', displayName: "Cum Certified Amount", width: 150},
			             { field: 'amountPostedCert', displayName: "Posted Certified Amount", width: 150},

			             {field: 'projectedProvision', width: 150},
			             {field: 'provision', width: 150},

			             {field: 'altObjectCode', width: 100},


			             {field: 'approved', width: 100},
			             {field: 'unit', width: 100},

			             {field: 'remark', width: 100},
			             {field: 'contraChargeSCNo', width: 100},
			             {field: 'sequenceNo', width: 100},
			             {field: 'resourceNo', width: 100},
			             {field: 'balanceType', width: 100}
			             ]

			
	};

	function getSCDetailForAddendumUpdate() {
		subcontractService.getSCDetailForAddendumUpdate(jobNo, subcontractNo)
		.then(
				function( data ) {
					$scope.gridOptions.data = data;
				});
	}
	
	$scope.updateDetail = function () {
		if(subcontractNo!="" && subcontractNo!=null){
			var dataRows = $scope.gridApi.selection.getSelectedRows();
			if(dataRows.length == 0){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select rows to update addendum.");
				return;
			}
			
			addAddendumFromResourceSummaries(dataRows);

		}else{
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Subcontract does not exist.");
		}
	};
	
	$scope.deleteDetail = function () {
		if(subcontractNo!="" && subcontractNo!=null){
			var dataRows = $scope.gridApi.selection.getSelectedRows();
			if(dataRows.length == 0){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select rows to insert addendum.");
				return;
			}
			
			addAddendumFromResourceSummaries(dataRows);

		}else{
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Subcontract does not exist.");
		}
	};


	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
	};

	//Listen for location changes and call the callback
	$scope.$on('$locationChangeStart', function(event){
		$uibModalInstance.close();
	});

}]);