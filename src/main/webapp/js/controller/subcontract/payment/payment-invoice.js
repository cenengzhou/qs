mainApp.controller('PaymentInvoiceCtrl', 
		['$scope' , '$state', '$stateParams', '$cookies', 'paymentService', 'modalService', 'confirmService', 'roundUtil', 'htmlService', '$sce',
        function($scope , $state, $stateParams, $cookies, paymentService, modalService, confirmService, roundUtil, htmlService, $sce) {
	
	$scope.disableButtons = true;
	loadData();
	
	$scope.submit =  function(){
		if($scope.payment.paymentStatus == "PND"){
			
			if(roundUtil.round($scope.paymentCertSummary.subMovement5, 2) !=  roundUtil.round($scope.paymentCertSummary.amountDueMovement, 2)){
				var message = "Total Movement Amount: "+roundUtil.round($scope.paymentCertSummary.subMovement5, 2)+"</br>"+
								"Total Amount Due: "+roundUtil.round($scope.paymentCertSummary.amountDueMovement, 2)+"</br>"+
								"Total Movement Amount and Total Amount Due do not match in the payment certificate.</br>" +
								"Please verify the figures before submitting the payment.";
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', message);
			}
			else{
				if($scope.payment.certAmount < 0 || $scope.payment.certAmount == 0){
					var modalOptions = {
							bodyText: 'The Certificate Amount is less than or equal to $0.00.<br/>Do you still wish to submit the payment?'
					};

					confirmService.showModal({}, modalOptions).then(function (result) {
						if(result == "Yes"){
							submitPayment();
						}
					});
				}else
					submitPayment();
			}
		}
	}

	function loadData() {
		if($cookies.get('paymentCertNo') != ""){
			getPaymentCert();
			getPaymentCertSummary();
		}else
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please create a payment certificate.");
	}

	function getPaymentCert() {
		paymentService.getPaymentCert($scope.jobNo, $scope.subcontractNo, $cookies.get('paymentCertNo'))
		.then(
				function( data ) {
					//console.log(data);
					$scope.payment = data;

					if($scope.payment.paymentStatus == "PND")
						$scope.disableButtons = false;
				});

	}



	function getPaymentCertSummary() {
//		paymentService.getPaymentCertSummary($scope.jobNo, $scope.subcontractNo, $cookies.get('paymentCertNo'))
//		.then(
//				function( data ) {
//					//console.log(data);
//					$scope.paymentCertSummary = data;
//				});
		htmlService.makeHTMLStringForSCPaymentCert($scope.jobNo, $scope.subcontractNo, $cookies.get('paymentCertNo'), 'A')
		.then(function(data){
			$scope.invoiceHtml = $sce.trustAsHtml(data);
		});
	}
	
	function submitPayment() {
		paymentService.submitPayment($scope.jobNo, $scope.subcontractNo, $cookies.get('paymentCertNo'))
		.then(
				function( data ) {
					if(data.length != 0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else {
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Payment has been submitted.");
						$state.reload();
					}
				});
	}

}]);