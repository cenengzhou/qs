mainApp.controller('SubcontractDatesCtrl', ['$scope',  function($scope) {

	$(".date").datepicker({
		format: "dd-mm-yyyy",
		todayHighlight: true,
		autoclose: true,
		todayBtn: "linked"
	});

	$scope.subcontractRequisitionApprovedDate  = "01-02-2016";
	$scope.subcontractTaApprovedDate  = "";
	$scope.preAwardFinalizationMeetingDate = ""; 
	$scope.loaSignedBySubcontractorDate  = "";
	$scope.subcontractDocumentExecutedBySubcontractorDate= "";  
	$scope.subcontractDocumentExecutedByLegalDate  = "";
	$scope.worksCommencementDate = "";
	$scope.subcontractorStartOnSiteDate= ""; 

	$scope.subcontractCreatedDate= "-";
	$scope.subcontractAwardApprovalRequestSentOutDate= "01-02-2016";
	$scope.subcontractAwardApprovalDate= "01-02-2016";
	$scope.latestAddendumApprovalDate= "01-02-2016";
	$scope.firstPaymentCertificateIssuedDate= "01-02-2016";
	$scope.latestPaymentCertificateIssuedDate= "01-02-2016";
	$scope.finalPaymentCertificateIssuedDate= "01-02-2016";


//	Save Function
	$scope.save = function () {
		console.log("subcontractRequisitionApprovedDate: " + $scope.subcontractRequisitionApprovedDate);

	};

}]);