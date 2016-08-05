
mainApp.controller('EnquirySubcontractCtrl', ['$scope' , '$rootScope', '$http', 'modalService', 'blockUI', 'GlobalParameter', 'subcontractService', 
                                      function($scope , $rootScope, $http, modalService, blockUI, GlobalParameter, subcontractService) {
	$scope.searchJobNo = $scope.jobNo;
	$scope.currentDate = new Date(); // Default: Today
	$scope.searchYear = $scope.currentDate.getFullYear();
	$scope.searchMonth = $scope.currentDate.getMonth()+1;
	$scope.GlobalParameter = GlobalParameter;
//	$scope.blockEnquirySubcontract = blockUI.instances.get('blockEnquirySubcontract');
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
			paginationPageSizes : [ ],
			paginationPageSize : 100,
			allowCellFocus: false,
			enableCellSelection: false,
			exporterMenuPdf: false,
			columnDefs: [
			             { field: 'jobInfo.company', width:'60', displayName: "Company", enableCellEdit: false },
			             { field: 'jobInfo.division', width:'60', displayName: "Division", enableCellEdit: false },
			             { field: 'jobInfo.jobNo', width:'60', displayName: "Job", enableCellEdit: false},
			             { field: 'jobInfo.soloJV', width:'60', displayName: "Solo/JV", enableCellEdit: false},
			             { field: 'jobInfo.jvPercentage', width:'50', displayName: "JV%", enableCellEdit: false},
			             { field: 'jobInfo.employer', width:'60', displayName: "Client No", enableCellEdit: false},
			             { field: 'packageNo', width:'60', displayName: "Subcontract", enableCellEdit: false},
			             { field: 'vendorName', width:'180', displayName: "Subcontractor Name", enableCellEdit: false}, // TODO:ADL
			             { field: 'description', width:'180', displayName: "Description", enableCellEdit: false},
			             { field: 'paymentCurrency', width:'60', displayName: "Currency", enableCellEdit: false},
			             { field: 'splitTerminateStatusText', width:'110', displayName: "Split Terminate Status", enableCellEdit: false},
			             { field: 'paymentStatusText', width:'80', displayName: "Payment Status", enableCellEdit: false},
			             { field: 'paymentTerms', width:'80', displayName: "Payment Terms", enableCellEdit: false},
			             { field: 'subcontractTerm', width:'120', displayName: "Subcontract Terms", enableCellEdit: false},
			             { field: 'subcontractorNature', width:'100', displayName: "Subcontractor Nature", enableCellEdit: false},
			             { field: 'originalSubcontractSum', width:'120', displayName: "Original Subcontract Sum", cellClass: 'text-right', cellFilter: 'number:2', enableCellEdit: false},
			             { field: 'remeasuredSubcontractSum', width:'160', displayName: "Remeasured Subcontract Sum", cellClass: 'text-right', cellFilter: 'number:2', enableCellEdit: false}, 
			             { field: 'approvedVOAmount', width:'120', displayName: "Addendum", cellClass: 'text-right', cellFilter: 'number:2', enableCellEdit: false},
			             { field: 'subcontractSum', width:'160', displayName: "Revised Subcontract Sum", cellClass: 'text-right', cellFilter: 'number:2', enableCellEdit: false}, //TODO: on screen
			             { field: 'accumlatedRetention', width:'130', displayName: "Accumlated Retention (RT)", cellClass: 'text-right', cellFilter: 'number:2', enableCellEdit: false},
			             { field: 'retentionReleased', width:'130', displayName: "Retention Released (RR+RA)", cellClass: 'text-right', cellFilter: 'number:2', enableCellEdit: false},
			             { field: 'retentionBalance', width:'120', displayName: "Retention Balance", cellClass: 'text-right', cellFilter: 'number:2', enableCellEdit: false},	// TODO: on screen
			             { field: 'totalNetPostedCertifiedAmount', width:'120', displayName: "Net Certified (excl. RT, CC)", cellClass: 'text-right', cellFilter: 'number:2', enableCellEdit: false},	//TODO: on screen
			             { field: 'totalPostedCertifiedAmount', width:'120', displayName: "Posted Certified", cellClass: 'text-right', cellFilter: 'number:2', enableCellEdit: false},
			             { field: 'totalCumCertifiedAmount', width:'120', displayName: "Cumulative Certified", cellClass: 'text-right', cellFilter: 'number:2', enableCellEdit: false},
			             { field: 'totalCumWorkDoneAmount', width:'120', displayName: "Cumulative WorkDone", cellClass: 'text-right', cellFilter: 'number:2', enableCellEdit: false},
			             { field: 'totalProvisionAmount', width:'120', displayName: "Provision", cellClass: 'text-right', cellFilter: 'number:2', enableCellEdit: false},	//TODO: on screen
			             { field: 'balanceToCompleteAmount', width:'120', displayName: "Balance to Complete", cellClass: 'text-right', cellFilter: 'number:2', enableCellEdit: false}, // TODO: on screen
			             { field: 'totalCCPostedCertAmount', width:'120', displayName: "Contra Charge", cellClass: 'text-right', cellFilter: 'number:2', enableCellEdit: false},
			             { field: 'totalMOSPostedCertAmount', width:'120', displayName: "Material On Site", cellClass: 'text-right', cellFilter: 'number:2', enableCellEdit: false},
            			 ]
	};
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	$scope.loadGridData = function(){
//		$scope.blockEnquirySubcontract.start("Loading...")
		subcontractService.getSubcontractSnapshotList($scope.searchJobNo, $scope.searchYear, $scope.searchMonth, true, false)
		.then(function(data){
				$scope.gridOptions.data = data;
//				$scope.blockEnquirySubcontract.stop();
			}, function(data){
//				$scope.blockEnquirySubcontract.stop();
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data ); 
			})
	}
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	$scope.loadGridData();
	
}]);