mainApp.controller('PaymentInvoiceCtrl', 
		['$scope' , '$state', '$stateParams', '$cookies', 'paymentService', 'modalService', 'confirmService', 'roundUtil', 'htmlService', 'GlobalHelper',
        function($scope , $state, $stateParams, $cookies, paymentService, modalService, confirmService, roundUtil, htmlService, GlobalHelper) {
	
	//only for init page load, reassign value when paymentCert loaded from backend
	$scope.paymentCertNo = $cookies.get('paymentCertNo');
	$scope.disableButtons = true;
	loadData();
	
	$scope.submit =  function(){
		if($scope.payment.paymentStatus == "PND"){
		
			if(roundUtil.round($scope.paymentCertSummary.subMovement5, 2) !=  roundUtil.round($scope.paymentCertSummary.amountDueMovement, 2)){
				var message = "Total Movement Amount: "+roundUtil.round($scope.paymentCertSummary.subMovement5, 2)+"\n"+
								"Total Amount Due: "+roundUtil.round($scope.paymentCertSummary.amountDueMovement, 2)+"\n"+
								"Total Movement Amount and Total Amount Due do not match in the payment certificate.\n" +
								"Please verify the figures before submitting the payment.";
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', message);
			}
			else{
				
				if($scope.payment.bypassPaymentTerms == "Y"){
					var modalOptions = {
							bodyText: "Please attach a summary sheet of Early Release Payment for the whole project.<br/>"
								+"1.	All related subcontracts and their respective early release amounts<br/>"
								+"2.	Total early release amount for the project<br/>"
								+"3.	Project cashflow<br/>"
								+"4.	Justification"
					};

					confirmService.showModal({}, modalOptions).then(function (result) {
						if(result == "Yes"){
							alertForZeroAmountPayment();
						}
					});
					
				}else
					alertForZeroAmountPayment();
				
			}
		}
	}

	function alertForZeroAmountPayment() {
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
	
	
	function loadData() {
		if($scope.paymentCertNo != ""){
			getPaymentCert();
			getPaymentCertSummary();
		}else
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please create a payment certificate.");
	}

	function getPaymentCert() {
		paymentService.getPaymentCert($scope.jobNo, $scope.subcontractNo, $scope.paymentCertNo)
		.then(
				function( data ) {
					$scope.payment = data;

					if($scope.payment.paymentStatus == "PND")
						$scope.disableButtons = false;
				});

	}

	function getPaymentCertSummary() {
		paymentService.getPaymentCertSummary($scope.jobNo, $scope.subcontractNo, $scope.paymentCertNo)
		.then(
				function( data ) {
					$scope.paymentCertSummary = data;
				});
		
		// htmlService.makeHTMLStringForSCPaymentCert({jobNumber: $scope.jobNo, packageNo: $scope.subcontractNo, paymentNo: $cookies.get('paymentCertNo'), htmlVersion: 'W'})
		// .then(function(data){
		// 	$scope.invoiceHtml = GlobalHelper.formTemplate(data);
		// });
	}
	
	function submitPayment() {
		paymentService.submitPayment($scope.jobNo, $scope.subcontractNo, $scope.paymentCertNo)
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