
mainApp.controller('ReportMainCtrl', ['$scope' , '$rootScope', '$http', 'modalService', 'blockUI', '$window', '$cookies', 'GlobalParameter',
                                function($scope , $rootScope, $http, modalService, blockUI, $window, $cookies, GlobalParameter) {
	
	$scope.GlobalParameter = GlobalParameter;
	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");
	
	$scope.paymentCertBody = {name:'paymentCert', show:true};
	$scope.subcontractBody = {name:'subcontract', show:true};
	$scope.subcontractLiabilityBody = {name:'subcontractLiability', show:true};
	$scope.subcontractAnalysisBody = {name:'subcontractAnalysis', show:true};
	
	$scope.panelList = [$scope.paymentCertBody, $scope.subcontractBody, $scope.subcontractLiabilityBody, $scope.subcontractAnalysisBody];
	$scope.showPanel = function(panel){
		angular.forEach($scope.panelList, function(p){
			if(p.name !== panel && p.show === true){
				$scope.flipVisiblity(p);
			} else if(p.name === panel && $scope.currentPanel !== undefined){
				p.show = !p.show;
			}
		})
		$scope.currentPanel = panel;
	}
	$scope.flipVisiblity = function(s){
		s.show = false;
		angular.element('#'+s.name+'Body').slideToggle();
	}
	$scope.showPanel('paymentCert');
	
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

	$scope.printPaymentCertDueDateType = true;
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
			url += $scope.printPaymentCertDueDateType? 'onOrBefore': 'exactDate';
			url += '&dueDate=';
			url += $scope.printPaymentCertDueDate !== undefined ? $scope.printPaymentCertDueDate : '';
			url += '&company=';
			url += $scope.printPaymentCertCompany !== undefined ? $scope.printPaymentCertCompany : '';
			url += '&jobNumber=';
			url += $scope.printPaymentCertJobNumber !== undefined ? $scope.printPaymentCertJobNumber : '';
			var wnd = $window.open(url, 'Print Report', '_blank');
		}
	}

	$scope.printSubcontractJobNumber = $scope.jobNo;
	$scope.onPrintSubcontract = function(report, type){
		var baseUrl = $window.location.href.split('home.html')[0];
		if($scope.printSubcontractForm.$valid){
			var url = baseUrl + 'gammonqs/';
				if(type === 'pdf'){
					url += 'subcontractReportExport.rpt';
				} else {
					url += 'financeSubcontractListDownload.smvc';
				}
			url += '?company=';
			url += $scope.printSubcontractCompany !== undefined ? $scope.printSubcontractCompany : '';
			url += '&division=';
			url += $scope.printSubcontractDivision !== undefined ? $scope.printSubcontractDivision : '';
			url += '&jobNumber=';
			url += $scope.printSubcontractJobNumber !== undefined ? $scope.printSubcontractJobNumber : '';
			url += '&subcontractNumber=';
			url += $scope.printSubcontractSubcontractNumber !== undefined ? $scope.printSubcontractSubcontractNumber : '';
			url += '&subcontractorNumber=';
			url += $scope.printSubcontractSubcontractorNumber !== undefined ? $scope.printSubcontractSubcontractorNumber : '';
			url += '&subcontractorNature=';
			url += $scope.printSubcontractSubcontractorNature !== undefined ? $scope.printSubcontractSubcontractorNature : '';
			url += '&paymentType=';
			url += $scope.printSubcontractPaymentType !== undefined ? $scope.printSubcontractPaymentType : '';
			url += '&workScope=';
			url += $scope.printSubcontractLiabilityWorkScope !== undefined ? $scope.printSubcontractLiabilityWorkScope : '';
			url += '&clientNo=';
			url += $scope.printSubcontractClientNumber !== undefined ? $scope.printSubcontractClientNumber : '';
			url += '&includeJobCompletionDate=';
			url += $scope.printSubcontractIncludeJobCompletionDate !== undefined ? $scope.printSubcontractncludeJobCompletionDate : '';
			url += '&splitTerminateStatus=';
			url += $scope.printSubcontractSplitTerminateStatus !== undefined ? $scope.printSubcontractSplitTerminateStatus : '';
			url += '&month=';
			url += $scope.printSubcontractMonth !== undefined ? $scope.printSubcontractMonth : '';
			url += '&year=';
			url += $scope.printSubcontractYear !== undefined ? $scope.printSubcontractYear : '';
			var wnd = $window.open(url, 'Print Report', '_blank');
		}
	}

	$scope.printSubcontractLiabilityJobNumber = $scope.jobNo;
	$scope.onPrintSubcontractLiability = function(report, type){
		var baseUrl = $window.location.href.split('home.html')[0];
		if($scope.printSubcontractLiabilityForm.$valid){
			var url = baseUrl + 'gammonqs/';
				if(type === 'pdf'){
					url += 'subcontractLiabilityReportExport.rpt';
				} else {
					url += 'subcontractLiabilityExcelExport.rpt';
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

	$scope.printSubcontractAnalysisJobNumber = $scope.jobNo;
	$scope.onPrintSubcontractAnalysis = function(report, type){
		var baseUrl = $window.location.href.split('home.html')[0];
		if($scope.printSubcontractAnalysisForm.$valid){
			var url = baseUrl + 'gammonqs/';
				if(type === 'pdf'){
					url += 'subcontractorAnalysisReportExport.rpt';
				} else {
					url += 'subcontractorAnalysisExcelExport.rpt';
				}				

			url += '?company=';
			url += $scope.printSubcontractAnalysisCompany !== undefined ? $scope.printSubcontractAnalysisCompany : '';
			url += '&division=';
			url += $scope.printSubcontractAnalysisDivision !== undefined ? $scope.printSubcontractAnalysisDivision : '';
			url += '&jobNumber=';
			url += $scope.printSubcontractAnalysisJobNumber !== undefined ? $scope.printSubcontractAnalysisJobNumber : '';
			url += '&subcontractNumber=';
			url += $scope.printSubcontractAnalysisSubcontractNumber !== undefined ? $scope.printSubcontractAnalysisSubcontractNumber : '';
			url += '&subcontractorNumber=';
			url += $scope.printSubcontractAnalysisSubcontractorNumber !== undefined ? $scope.printSubcontractAnalysisSubcontractorNumber : '';
			url += '&subcontractorNature=';
			url += $scope.printSubcontractAnalysisSubcontractorNature !== undefined ? $scope.printSubcontractAnalysisSubcontractorNature : '';
			url += '&paymentType=';
			url += $scope.printSubcontractAnalysisPaymentType !== undefined ? $scope.printSubcontractAnalysisPaymentType : '';
			url += '&workScope=';
			url += $scope.printSubcontractAnalysisWorkScope !== undefined ? $scope.printSubcontractAnalysisWorkScope : '';
			url += '&clientNo=';
			url += $scope.printSubcontractAnalysisClientNumber !== undefined ? $scope.printSubcontractAnalysisClientNumber : '';
			url += '&includeJobCompletionDate=';
			url += $scope.printSubcontractAnalysisIncludeJobCompletionDate !== undefined ? $scope.printSubcontractAnalysisIncludeJobCompletionDate : '';
			url += '&splitTerminateStatus=';
			url += $scope.printSubcontractAnalysisSplitTerminateStatus !== undefined ? $scope.printSubcontractAnalysisSplitTerminateStatus : '';
			url += '&month=';
			url += $scope.printSubcontractAnalysisMonth !== undefined ? $scope.printSubcontractAnalysisMonth : '';
			url += '&year=';
			url += $scope.printSubcontractAnalysisYear !== undefined ? $scope.printSubcontractAnalysisYear : '';
			var wnd = $window.open(url, 'Print Report', '_blank');
		}
	}

}]);