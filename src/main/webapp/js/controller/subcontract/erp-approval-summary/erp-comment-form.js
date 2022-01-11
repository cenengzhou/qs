mainApp.controller('ErpCommentFormCtrl',
		['$scope' , '$state', '$stateParams', '$cookies', 'paymentService', 'modalService', 'confirmService', 'roundUtil', 'htmlService', 'GlobalHelper', 'approvalSummaryService', 'addendumService', 'subcontractService',
        function($scope , $state, $stateParams, $cookies, paymentService, modalService, confirmService, roundUtil, htmlService, GlobalHelper, approvalSummaryService, addendumService, subcontractService) {
	
	$scope.paymentCertNo = $cookies.get('paymentCertNo');
	$scope.disableButtons = true;
	$scope.jobNo = $cookies.get('jobNo');
	$scope.subcontractNo = $cookies.get('subcontractNo');
	$scope.addendumNo =  $cookies.get('addendumNo');

	$scope.disableButtons = true;
	loadData();

	$scope.update =  function(){
		approvalSummaryService.updateApprovalSummary($scope.jobNo, $scope.nameObject, $scope.textKey, $scope.summary)
			.then(function(data) {
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "ERP Approval Summary Comment has been updated.");

		})
	}

	function loadData() {
		$scope.nameObject = $stateParams.nameObject;
		if ($scope.nameObject === 'SUBCONTRACT') {
			getSubcontract();
		} else if ($scope.nameObject === 'ADDENDUM') {
			getAddendum();
		} else {
			if($scope.paymentCertNo != ""){
				getPaymentCert();
			}else
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please create a payment certificate.");
		}

	}

	function getSubcontract() {
		subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
			.then(function(data) {
				$scope.textKey = data.id;
				approvalSummaryService.obtainApprovalSummary($scope.nameObject, $scope.textKey)
					.then(function(data) {
						$scope.summary = data;
					});
				if(data.scStatus =="330" || data.scStatus =="500")
					$scope.disableButtons = true;
				else
					$scope.disableButtons = false;
			});

	}

	function getAddendum() {
		addendumService.getAddendum($scope.jobNo, $scope.subcontractNo, $scope.addendumNo)
			.then(function (data) {
				$scope.textKey = data.id;
				approvalSummaryService.obtainApprovalSummary($scope.nameObject, $scope.textKey)
					.then(function(data) {
						$scope.summary = data;
					});
				if (data.status == "PENDING")
					$scope.disableButtons = false;
				else
					$scope.disableButtons = true;
			});
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