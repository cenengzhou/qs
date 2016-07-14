mainApp.controller('PaymentInvoiceCtrl', ['$scope' , '$http', '$stateParams', '$cookieStore', 'paymentService', 'modalService',
                                          function($scope , $http, $stateParams, $cookieStore, paymentService, modalService) {
	
	$scope.disableButtons = true;
	loadData();

	function loadData() {
		if($cookieStore.get('paymentCertNo') != ""){
			getPaymentCert();
			loadPaymentCertSummary();
		}else
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please create a payment certificate.");
	}

	function getPaymentCert() {
		paymentService.getPaymentCert($scope.jobNo, $scope.subcontractNo, $cookieStore.get('paymentCertNo'))
		.then(
				function( data ) {
					console.log(data);
					$scope.payment = data;

					if($scope.payment.paymentStatus == "PND")
						$scope.disableButtons = false;
				});

	}



	function loadPaymentCertSummary() {
		paymentService.getPaymentCertSummary($scope.jobNo, $scope.subcontractNo, $cookieStore.get('paymentCertNo'))
		.then(
				function( data ) {
					console.log(data);
					$scope.paymentCertSummary = data;
				});

	}

}]);