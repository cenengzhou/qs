
mainApp.controller('EnquiryPaymentCtrl', ['$scope' , '$rootScope', '$http', 'modalService', 'blockUI', 'GlobalParameter', 'paymentService', 'GlobalParameter', 
                                function($scope , $rootScope, $http, modalService, blockUI, GlobalParameter, paymentService, GlobalParameter) {
	$scope.GlobalParameter = GlobalParameter;
	$scope.searchDueDateType = 'onOrBefore';
//	$scope.blockEnquiryPayment = blockUI.instances.get('blockEnquiryPayment');
	$scope.searchJobNo = $scope.jobNo;
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
			             { field: 'jobInfo.company', displayName: 'Company', enableCellEdit: false },
			             { field: 'jobNo', displayName: 'Job Number', enableCellEdit: false },
			             { field: 'packageNo', displayName: 'Subcontract Number', enableCellEdit: false},
			             { field: 'subcontract.vendorNo', displayName: 'Subcontractor Number', enableCellEdit: false},
			             { field: 'paymentCertNo', displayName: 'Payment Number', enableCellEdit: false},
			             { field: 'mainContractPaymentCertNo', displayName: 'Main Certificate Number', enableCellEdit: false},
			             { field: 'subcontract.paymentTerms', displayName: 'Payment Terms', enableCellEdit: false},
			             { field: 'getValueById("paymentStatus","paymentStatus")', displayName: 'Status', enableCellEdit: false},
			             { field: 'getValueById("directPayment","directPayment")', displayName: 'Direct Payment', enableCellEdit: false},
			             { field: 'getValueById("intermFinalPayment","intermFinalPayment")', displayName: 'Interim / Final Payment', enableCellEdit: false},
			             { field: 'certAmount', displayName: 'Certificate Amount', cellFilter: 'number:2', cellClass: 'text-right', enableCellEdit: false},
			             { field: 'dueDate', displayName: 'Due Date', cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"', enableCellEdit: false},
			             { field: 'asAtDate', cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"', displayName: 'As at Date', enableCellEdit: false},
			             { field: 'scIpaReceivedDate', displayName: 'SC IPA Received Date', cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"', enableCellEdit: false},
			             { field: 'certIssueDate', displayName: 'Certificate Issue Date', cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"', enableCellEdit: false}	
            			 ]
	};
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	$scope.loadGridData = function(){
		var paymentCertWrapper = {};
		paymentCertWrapper.jobInfo = {}
		console.log($scope.searchJobNo);
		paymentCertWrapper.jobInfo.jobNumber = $scope.searchJobNo;
		paymentCertWrapper.jobNo = $scope.searchJobNo;
		paymentCertWrapper.jobInfo.setCompany = $scope.searchCompany;
		paymentCertWrapper.subcontract = {};
		paymentCertWrapper.subcontract.packageNo = $scope.searchPackage;
		paymentCertWrapper.subcontract.venderNo = $scope.searchVenderNo;
		paymentCertWrapper.subcontract.paymentTerms = $scope.searchPaymentTerms;
		paymentCertWrapper.paymentStatus = $scope.searchPaymentStatus;
		paymentCertWrapper.intermFinalPayment = $scope.searchIntermFinalPayment;
		paymentCertWrapper.directPayment = $scope.searchDirectPayment;
		paymentCertWrapper.dueDate = $scope.searchDueDate !== undefined ? new moment($scope.searchDueDate) : null;
		paymentCertWrapper.certIssueDate = $scope.searchCertIssueDate;
		
		var dueDateType = $scope.searchDueDateType !== undefined ? $scope.searchDueDateType : 'ignore';
//		$scope.blockEnquiryPayment.start('Loading...')
		paymentService.obtainPaymentCertificateList(paymentCertWrapper, dueDateType)
		.then(function(data){
				if(angular.isArray(data)){
					$scope.convertAbbr(data);
					$scope.gridOptions.data = data;
				} 
//				$scope.blockEnquiryPayment.stop();
		}, function(data){
//			$scope.blockEnquiryPayment.stop();
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data ); 
		});

	}
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	$scope.loadGridData();
	
	$scope.convertAbbr = function(data){
    	data.forEach(function(d){
    		d.getValueById = $scope.getValueById;
    	})
    }
	
	$scope.getValueById = function(arr, id){
		var obj = this;
		return GlobalParameter.getValueById(GlobalParameter[arr], obj[id]);
	}
	
}]);