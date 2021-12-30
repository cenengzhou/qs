mainApp.controller('ErpApprovalTabCtrl',
		['$scope' , '$state', '$stateParams', '$cookies', 'paymentService', 'modalService', 'confirmService', 'roundUtil', 'htmlService', 'GlobalHelper',
        function($scope , $state, $stateParams, $cookies, paymentService, modalService, confirmService, roundUtil, htmlService, GlobalHelper) {
	
	$scope.paymentCertNo = $cookies.get('paymentCertNo');
	$scope.tabDisableButtons = true;
	loadData();

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

					if($scope.payment.paymentStatus == "PND")
						$scope.tabDisableButtons = false;
				});

	}


}]);