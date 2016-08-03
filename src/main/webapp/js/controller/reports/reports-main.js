
mainApp.controller('ReportMainCtrl', ['$scope' , '$rootScope', '$http', 'modalService', 'blockUI', '$window', '$cookies', 'GlobalParameter',
                                function($scope , $rootScope, $http, modalService, blockUI, $window, $cookies, GlobalParameter) {
	
	$scope.GlobalParameter = GlobalParameter;
	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");

	$scope.printUnpaidPaymentCertDueDateType = 'onOrBefore';
	$scope.printUnpaidPaymentCertJobNumber = $scope.jobNo;
	
	$scope.onPrintUnPaidPaymentCert = function(type){
		var baseUrl = $window.location.href.split('home.html')[0];
		if($scope.printUnPaidPaymentCertForm.$valid){
			var url = baseUrl + 'gammonqs/';
			if(type === 'pdf'){
				url += 'printUnpaidPaymentCertificateReportPdf.rpt';
			} else {
				return;
			}
			url += '?dueDateType=';
			url += $scope.printUnpaidPaymentCertDueDateType !== undefined ? $scope.printUnpaidPaymentCertDueDateType : '';
			url += '&dueDate=';
			url += $scope.printUnpaidPaymentCertDueDate !== undefined ? $scope.printUnpaidPaymentCertDueDate : '';
			url += '&company=';
			url += $scope.printUnpaidPaymentCertCompany !== undefined ? $scope.printUnpaidPaymentCertCompany : '';
			url += '&jobNumber=';
			url += $scope.printUnpaidPaymentCertJobNumber !== undefined ? $scope.printUnpaidPaymentCertJobNumber : '';
			url += '&supplierNumber=';
			url += $scope.printUnpaidPaymentCertSupplierNumber !== undefined ? $scope.printUnpaidPaymentCertSupplierNumber : '';
			var wnd = $window.open(url, 'Print Report', '_blank');
		}
	}

	$scope.printPaymentCertDueDateType = 'onOrBefore';
	$scope.printPaymentCertJobNumber = $scope.jobNo;
		
	$scope.onPrintPaymentCert = function(type){
		var baseUrl = $window.location.href.split('home.html')[0];
		if($scope.printPaymentCertForm.$valid){
			var url = baseUrl + 'gammonqs/';
			if(type === 'pdf'){
				url += 'printPaymentCertificateReportPdf.rpt';
			} else {
				return;
			}
			url += '?dueDateType=';
			url += $scope.printPaymentCertDueDateType !== undefined ? $scope.printPaymentCertDueDateType : '';
			url += '&dueDate=';
			url += $scope.printPaymentCertDueDate !== undefined ? $scope.printPaymentCertDueDate : '';
			url += '&company=';
			url += $scope.printPaymentCertCompany !== undefined ? $scope.printPaymentCertCompany : '';
			url += '&jobNumber=';
			url += $scope.printPaymentCertJobNumber !== undefined ? $scope.printPaymentCertJobNumber : '';
			var wnd = $window.open(url, 'Print Report', '_blank');
		}
	}

	$scope.printSubcontractLiabilityJobNumber = $scope.jobNo;
	$scope.onPrintSubcontract = function(report, type){
		var baseUrl = $window.location.href.split('home.html')[0];
		if($scope.printSubcontractLiabilityForm.$valid){
			var url = baseUrl + 'gammonqs/';
			if(report === 'liability') {
				if(type === 'pdf'){
					url += 'subcontractLiabilityReportExport.rpt';
				} else {
					url += 'subcontractLiabilityExcelExport.rpt';
				}
			} else if(report === 'analysis') {
				if(type === 'pdf'){
					url += 'subcontractorAnalysisReportExport.rpt';
				} else {
					url += 'subcontractorAnalysisExcelExport.rpt';
				}				
			}
			url += '?company=';
			url += $scope.printSubcontractLiabilityCompany !== undefined ? $scope.printSubcontractLiabilityCompany : '';
			url += '&division=';
			url += $scope.printSubcontractLiabilityDivision !== undefined ? $scope.printSubcontractLiabilityDivision : '';
			url += '&jobNumber=';
			url += $scope.printSubcontractLiabilityJobNumber !== undefined ? $scope.printSubcontractLiabilityJobNumber : '';
			url += '&subcontractNumber=';
			url += $scope.printSubcontractLiabilitySubcontractNumber !== undefined ? $scope.printSubcontractLiabilitySubcontractNumber : '';
			url += '&subcontractorNumber=';
			url += $scope.printSubcontractLiabilitySubcontractorNumber !== undefined ? $scope.printSubcontractLiabilitySubcontractorNumber : '';
			url += '&subcontractorNature=';
			url += $scope.printSubcontractLiabilitySubcontractorNature !== undefined ? $scope.printSubcontractLiabilitySubcontractorNature : '';
			url += '&paymentType=';
			url += $scope.printSubcontractLiabilityPaymentType !== undefined ? $scope.printSubcontractLiabilityPaymentType : '';
			url += '&workScope=';
			url += $scope.printSubcontractLiabilityWorkScope !== undefined ? $scope.printSubcontractLiabilityWorkScope : '';
			url += '&clientNo=';
			url += $scope.printSubcontractLiabilityClientNumber !== undefined ? $scope.printSubcontractLiabilityClientNumber : '';
			url += '&includeJobCompletionDate=';
			url += $scope.printSubcontractLiabilityIncludeJobCompletionDate !== undefined ? $scope.printSubcontractLiabilityIncludeJobCompletionDate : '';
			url += '&splitTerminateStatus=';
			url += $scope.printSubcontractLiabilitySplitTerminateStatus !== undefined ? $scope.printSubcontractLiabilitySplitTerminateStatus : '';
			url += '&month=';
			url += $scope.printSubcontractLiabilityMonth !== undefined ? $scope.printSubcontractLiabilityMonth : '';
			url += '&year=';
			url += $scope.printSubcontractLiabilityYear !== undefined ? $scope.printSubcontractLiabilityYear : '';
			var wnd = $window.open(url, 'Print Report', '_blank');
		}
	}

}]);