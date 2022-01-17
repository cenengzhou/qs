mainApp.controller('ErpCommentFormCtrl',
		['$scope' , '$state', '$stateParams', '$cookies', 'paymentService', 'modalService', 'confirmService', 'roundUtil', 'htmlService', 'GlobalHelper', 'approvalSummaryService', 'addendumService', 'subcontractService', '$timeout',
        function($scope , $state, $stateParams, $cookies, paymentService, modalService, confirmService, roundUtil, htmlService, GlobalHelper, approvalSummaryService, addendumService, subcontractService, $timeout) {
	
	$scope.paymentCertNo = $cookies.get('paymentCertNo');
	$scope.disableButtons = true;
	$scope.jobNo = $cookies.get('jobNo');
	$scope.subcontractNo = $cookies.get('subcontractNo');
	$scope.addendumNo =  $cookies.get('addendumNo');

	$scope.disableButtons = true;
	loadData();

	$scope.update =  function(){
		if (false === $('form[name="form-validate"]').parsley().validate()) {
			event.preventDefault();
			return;
		}

		var MAX_TEXTAREA_LENGTH = 1500;
		if ($scope.summary.summary.length > MAX_TEXTAREA_LENGTH
			|| $scope.summary.eoj.length > MAX_TEXTAREA_LENGTH
			|| $scope.summary.contingencies.length > MAX_TEXTAREA_LENGTH
			|| $scope.summary.riskOpps.length > MAX_TEXTAREA_LENGTH
			|| $scope.summary.others.length > MAX_TEXTAREA_LENGTH
		) {
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'Exceed maximum characters: ' + MAX_TEXTAREA_LENGTH);
			return;
		}

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
			.then(function(subcontract) {
				$scope.textKey = subcontract.id;
				approvalSummaryService.obtainApprovalSummary($scope.nameObject, $scope.textKey)
					.then(function(data) {
						$scope.summary = data;
						if(subcontract.scStatus =="330" || subcontract.scStatus =="500")
							$scope.disableButtons = true;
						else {
							$scope.disableButtons = false;
							redirectIfNotComplete();
						}
					});

			});

	}

	function getAddendum() {
		addendumService.getAddendum($scope.jobNo, $scope.subcontractNo, $scope.addendumNo)
			.then(function (addendum) {
				$scope.textKey = addendum.id;
				approvalSummaryService.obtainApprovalSummary($scope.nameObject, $scope.textKey)
					.then(function(data) {
						$scope.summary = data;
						if (addendum.status == "PENDING") {
							$scope.disableButtons = false;
							redirectIfNotComplete();
						}
						else
							$scope.disableButtons = true;
					});
			});
	}

	function getPaymentCert() {
		paymentService.getPaymentCert($scope.jobNo, $scope.subcontractNo, $scope.paymentCertNo)
		.then(
				function( pay ) {
					$scope.payment = pay;

					$scope.textKey = pay.id;
					$scope.nameObject = 'PAYMENT_CERT';

					approvalSummaryService.obtainApprovalSummary($scope.nameObject, $scope.textKey)
						.then(function(data) {
							$scope.summary = data;

							if(pay.paymentStatus == "PND") {
								$scope.disableButtons = false;
								redirectIfNotComplete();
							}
						})
				});

	}

	function redirectIfNotComplete() {
		if ($stateParams.redirectTo) {
			if (!$scope.summary.summary || !$scope.summary.eoj || !$scope.summary.contingencies || !$scope.summary.riskOpps || !$scope.summary.others) {
				$state.go($stateParams.redirectTo);
				$timeout(function() {
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "ERP Approval Summary is not complete.");
				}, 1000);
			}
		}
	}

}]);