mainApp.controller('PaymentInvoiceCtrl', ['$scope' , '$http', '$stateParams', '$cookieStore', 'paymentService', 
                                          function($scope , $http, $stateParams, $cookieStore, paymentService) {
	
	$scope.disableButtons = true;
	loadData();

	function loadData() {
		getPaymentCert();
		loadPaymentCertSummary();
	}

	function getPaymentCert() {
		if($cookieStore.get('paymentCertNo') != ""){
			paymentService.getPaymentCert($scope.jobNo, $scope.subcontractNo, $cookieStore.get('paymentCertNo'))
			.then(
					function( data ) {
						console.log(data);
						$scope.mainCertNo.selected = data.mainContractPaymentCertNo;
						$scope.payment = data;
	
						if($scope.payment.paymentStatus == "PND")
							$scope.disableButtons = false;
					});
		}
		
	}
	
	
	
	function loadPaymentCertSummary() {
		if($cookieStore.get('paymentCertNo') != ""){
			paymentService.getPaymentCertSummary($scope.jobNo, $scope.subcontractNo, $cookieStore.get('paymentCertNo'))
			.then(
					function( data ) {
						console.log(data);
						$scope.paymentCertSummary = data;
					});
		}
	}

}]);