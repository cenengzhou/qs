
mainApp.controller('EnquirySubcontractCtrl', ['$scope' , '$rootScope', '$http', '$cookieStore', 'modalService', 'blockUI', 'GlobalParameter', 'subcontractService', 
                                      function($scope , $rootScope, $http, $cookieStore, modalService, blockUI, GlobalParameter, subcontractService) {
	$scope.searchJobNo = $scope.jobNo;
	$scope.GlobalParameter = GlobalParameter;
	$scope.blockEnquirySubcontract = blockUI.instances.get('blockEnquirySubcontract');
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
			paginationPageSizes: [50],
			paginationPageSize: 50,
			allowCellFocus: false,
			enableCellSelection: false,
			isRowSelectable: function(row){
				if(row.entity.sessionId === $rootScope.sessionId){
					return false;
				}
				return true;
			},
			columnDefs: [
			             { field: 'company', width:'60', displayName: "Company", enableCellEdit: false },
			             { field: 'division', width:'60', displayName: "Division", enableCellEdit: false },
			             { field: 'jobNumber', width:'60', displayName: "Job Number", enableCellEdit: false},
			             { field: 'soloJV', width:'60', displayName: "Solo JV", enableCellEdit: false},
			             { field: 'jvPercentage', width:'50', displayName: "JV%", enableCellEdit: false},
			             { field: 'currency', width:'60', displayName: "Currency", enableCellEdit: false},
			             { field: 'clientNo', width:'60', displayName: "Client No", enableCellEdit: false},
			             { field: 'packageNo', width:'60', displayName: "SC No", enableCellEdit: false},
			             { field: 'vendorName', width:'180', displayName: "Subcontractor Name", enableCellEdit: false},
			             { field: 'description', width:'180', displayName: "Description", enableCellEdit: false},
			             { field: 'splitTerminateStatus', width:'110', displayName: "Split Terminate Status", enableCellEdit: false},
			             { field: 'paymentStatus', width:'80', displayName: "Payment Type", enableCellEdit: false},
			             { field: 'paymentTerms', width:'80', displayName: "Payment Terms", enableCellEdit: false},
			             { field: 'subcontractTerm', width:'120', displayName: "SC Term", enableCellEdit: false},
			             { field: 'subcontractorNature', width:'100', displayName: "Subcontractor Nature", enableCellEdit: false},
			             { field: 'originalSubcontractSum', width:'120', displayName: "Original SC Sum", enableCellEdit: false},
			             { field: 'remeasuredSubcontractSum', width:'160', displayName: "Remeasured Subcontract Sum", enableCellEdit: false},
			             { field: 'addendum', displayName: "Addendum", width:'120', enableCellEdit: false},
			             { field: 'subcontractSum', width:'160', displayName: "Revised Subcontract Sum", enableCellEdit: false},
			             { field: 'accumlatedRetentionAmt', width:'130', displayName: "Accumlated Retention(RT)", enableCellEdit: false},
			             { field: 'retentionReleasedAmt', width:'130', displayName: "Retention Released(RR)", enableCellEdit: false},
			             { field: 'retentionBalanceAmt', width:'120', displayName: "Retention Balance", enableCellEdit: false},
			             { field: 'netCertifiedAmount', width:'120', displayName: "Net Cert. Amt(excl. CC)", enableCellEdit: false},
			             { field: 'totalPostedCertAmt', width:'120', displayName: "Cert. Amount (Posted)", enableCellEdit: false},
			             { field: 'totalCumCertAmt', width:'120', displayName: "Cert Amount (Cum.)", enableCellEdit: false},
			             { field: 'totalLiabilities', width:'120', displayName: "Work Done Amount*(Cum.)", enableCellEdit: false},
			             { field: 'totalProvision', width:'120', displayName: "Provision", enableCellEdit: false},
			             { field: 'balanceToComplete', width:'120', displayName: "Balance to Complete", enableCellEdit: false},
			             { field: 'totalCCPostedAmt', width:'120', displayName: "Contra Charge", enableCellEdit: false},
			             { field: 'totalMOSPostedAmt', width:'120', displayName: "Material On Site", enableCellEdit: false},
            			 ]
	};
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	$scope.loadGridData = function(){
		$scope.blockEnquirySubcontract.start("Loading...")
		var subcontractListWrapper = {};
		subcontractListWrapper.jobNumber = $scope.searchJobNo;
		subcontractListWrapper.month = $scope.searchMonth;
		subcontractListWrapper.year = $scope.searchYear;
		subcontractService.obtainSubcontractListWithSCListWrapper(subcontractListWrapper)
		.then(function(data){
				$scope.gridOptions.data = data;
				$scope.blockEnquirySubcontract.stop();
			}, function(data){
				$scope.blockEnquirySubcontract.stop();
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data ); 
			})
	}
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	$scope.loadGridData();
	
}]);