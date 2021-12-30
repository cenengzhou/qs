mainApp.controller('ErpCommentFormCtrl',
		['$scope' , '$state', '$stateParams', '$cookies', 'paymentService', 'modalService', 'confirmService', 'roundUtil', 'htmlService', 'GlobalHelper', 'approvalSummaryService',
        function($scope , $state, $stateParams, $cookies, paymentService, modalService, confirmService, roundUtil, htmlService, GlobalHelper, approvalSummaryService) {
	
	$scope.paymentCertNo = $cookies.get('paymentCertNo');
	$scope.disableButtons = true;
	$scope.jobNo = $cookies.get('jobNo');

	$scope.disableButtons = true;
	loadData();

	$scope.update =  function(){
		approvalSummaryService.updateApprovalSummary($scope.jobNo, $scope.nameObject, $scope.textKey, $scope.summary)
			.then(function(data) {
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "ERP Approval Summary Comment has been updated.");

		})
	}

	function loadData() {
		if($scope.paymentCertNo != ""){
			getPaymentCert();
		}else
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please create a payment certificate.");
	}

	function getPaymentCert() {
		paymentService.getPaymentCert($scope.jobNo, $scope.subcontractNo, $scope.paymentCertNo)
		.then(
				function( data ) {
					$scope.payment = data;

					$scope.textKey = data.id;
					$scope.nameObject = 'PAYMENT_CERT';

					approvalSummaryService.obtainApprovalSummary($scope.nameObject, $scope.textKey)
						.then(function(data) {
							$scope.summary = data;
						})

					if($scope.payment.paymentStatus == "PND")
						$scope.disableButtons = false;
				});

	}



}]);