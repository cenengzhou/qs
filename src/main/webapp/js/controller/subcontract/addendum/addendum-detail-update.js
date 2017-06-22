mainApp.controller('AddendumDetailUpdateCtrl', ['$scope', 'resourceSummaryService', 'subcontractService', 'addendumService', 'repackagingService', 'modalService', '$state', '$cookies', '$uibModalInstance',
                                            function($scope, resourceSummaryService, subcontractService, addendumService, repackagingService, modalService, $state, $cookies, $uibModalInstance) {

	var jobNo = $cookies.get('jobNo');
	var subcontractNo = $cookies.get('subcontractNo');
	var addendumNo = $cookies.get('addendumNo');

	var addendumDetailHeaderRef = $cookies.get('addendumDetailHeaderRef');
	if(addendumDetailHeaderRef=='Empty')
		addendumDetailHeaderRef = '';
	
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
			             { field: 'quantity', width: 100, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:4' },

			             {field: 'scRate', width: 80, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:4' },
			             {field: 'objectCode', width: 100},
			             {field: 'subsidiaryCode', width: 100},
			             {field: 'amountSubcontract', displayName: "SC Amount", width: 150, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2' },

			             { field: 'amountCumulativeWD', displayName: "Cum WD Amount", width: 150, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2' },
			             { field: 'amountPostedWD', displayName: "Posted WD Amount", width: 150, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2' },
			             { field: 'amountCumulativeCert', displayName: "Cum Certified Amount", width: 150, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2' },
			             { field: 'amountPostedCert', displayName: "Posted Certified Amount", width: 150, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2' },

			             {field: 'costRate', width: 80, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:4' },
			             {field: 'amountBudget', displayName: "Budget Amount", width: 150, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2' },
			             {field: 'projectedProvision', width: 150, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2' },
			             {field: 'provision', width: 150, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2' },

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
		$scope.disableButtons = true;
		if(subcontractNo!="" && subcontractNo!=null){
			var dataRows = $scope.gridApi.selection.getSelectedRows();
			if(dataRows.length == 0 || dataRows.length > 1){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select 1 row to update addendum.");
				$scope.disableButtons = false;
				return;
			}
			
			
			//Set Header if exist
			var idHeaderRef = null;
			if(addendumDetailHeaderRef!=null && addendumDetailHeaderRef.length !=0){
				idHeaderRef = addendumDetailHeaderRef;
			}
			
			var addendumDetailToAdd = {
					bpi:				dataRows[0].billItem,
					codeObject: 		dataRows[0].objectCode,	
					codeSubsidiary:		dataRows[0].subsidiaryCode,
					description:		dataRows[0].description,
					quantity:			dataRows[0].quantity,
					rateAddendum:		dataRows[0].scRate, 
					amtAddendum:		dataRows[0].amountSubcontract, 
					rateBudget: 		dataRows[0].costRate,
					amtBudget: 			dataRows[0].amountBudget,
					unit:				$scope.unit,
					remarks:			dataRows[0].remark,
					typeVo : 			dataRows[0].lineType,
					idHeaderRef:		idHeaderRef,
					idSubcontractDetail:dataRows[0]
			}
			
			$uibModalInstance.close();
			modalService.open('lg', 'view/subcontract/addendum/addendum-detail-add-modal.html', 'AddendumDetailsAddCtrl', 'ADD', addendumDetailToAdd);

		}else{
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Subcontract does not exist.");
		}
	};
	
	$scope.deleteDetail = function () {
		$scope.disableButtons = true;
		if(subcontractNo!="" && subcontractNo!=null){
			var dataRows = $scope.gridApi.selection.getSelectedRows();
			if(dataRows.length == 0){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select rows to insert addendum.");
				$scope.disableButtons = false;
				return;
			}
			
			deleteAddendumFromSCDetails(dataRows);

		}else{
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Subcontract does not exist.");
		}
	};


	function deleteAddendumFromSCDetails(subcontractDetailList){
		addendumService.deleteAddendumFromSCDetails(jobNo, subcontractNo, addendumNo, addendumDetailHeaderRef, subcontractDetailList)
		.then(
				function( data ) {
					if(data.length != 0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
						$scope.disableButtons = false;
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Addendum has been created.");
						$uibModalInstance.close();
						$state.reload();
					}
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