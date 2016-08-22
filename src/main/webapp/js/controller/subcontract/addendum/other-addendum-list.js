mainApp.controller('OtherAddendumListCtrl', ['$scope' , 'modalService', 'subcontractService','$location', '$state', '$cookies',
                                              function($scope , modalService, subcontractService, $location, $state, $cookies) {
	$cookies.put('scDetailID', '');
	getOtherSubcontractDetails();


	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSorting: false,
			//enableSelectAll: true,
			//enableFullRowSelection: true,
			multiSelect: false,
			//showGridFooter : true,
			//showColumnFooter : true,
			//fastWatch : true,
			exporterMenuPdf: false,
			 
			columnDefs: [
			             { field: 'id',  width:100, visible: false},
			             { field: 'type',displayName:"Type",  width:50 },
			             { field: 'lineType',displayName:"Line Type",  width:50 },
			             { field: 'billItem',  width:100 },
			             {field: 'description' ,  width:100 },
			             { field: 'quantity' ,  width:100, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2'  },
			             { field: 'scRate' , displayName:"SC Rate", width:100, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2'  },
			             { field: 'amountSubcontract' , displayName:"SC Amount", width:100, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2'  },
			             /*{ field: 'rateBudget', displayName:"Budget Rate", width:100, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2'  },
			             { field: 'amtBudget' , displayName:"Budget Amount", width:100, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2'  },*/
			             { field: 'amountPostedCert' , displayName:"Posted Cert. Amount", width:100, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2'  },
			             { field: 'amountPostedWD' , displayName:"Posted WD Amount", width:100, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2'  },
			             {field: 'objectCode' ,  width:100 },
			             {field: 'subsidiaryCode' ,  width:100 },
			             {field: 'unit' ,  width:100 },
			             {field: 'remark' ,  width:150 }
			             ]
	};

	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;
	}


	$scope.edit = function(){
		var dataRows  = $scope.gridApi.selection.getSelectedRows();

		if(dataRows.length != 1){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select a row to edit.");
			return;
		}
		
		$cookies.put('scDetailID', dataRows[0].id);
		
		$location.path('/subcontract/otherAddendum/tab/detail');
	}

	$scope.deleteDetail = function(addendumDetail){
		var dataRows = $scope.gridApi.selection.getSelectedRows();
		if(dataRows.length != 1){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select 1 row to delete.");
			return;
		}
		deleteSubcontractDetailAddendum(dataRows[0].sequenceNo, dataRows[0].lineType);
	}
	
	function getOtherSubcontractDetails(){
		subcontractService.getOtherSubcontractDetails($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					$scope.gridOptions.data = data;
				});
	}
	
	
	function deleteSubcontractDetailAddendum(sequenceNo, lineType){
		subcontractService.deleteSubcontractDetailAddendum($scope.jobNo, $scope.subcontractNo, sequenceNo, lineType)
		.then(
				function( data ) {
					if(data.length != 0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
						$scope.disableButtons = false;
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Subcontract Detail has been deleted.");
						$uibModalInstance.close();
						$state.reload();
					}
				});
	}
	
}]);