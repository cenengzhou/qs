mainApp.controller('PaymentCertCtrl', ['$scope' , '$http', '$stateParams', '$cookieStore', 'paymentService', 'mainCertService', 'modalService', 
                                          function($scope , $http, $stateParams, $cookieStore, paymentService, mainCertService, modalService) {
	getPaymentCert();
	
	$scope.disableButtons = true;

	$scope.mainCertNo = {
			options: [],
			selected: ""
	};

	if($stateParams.paymentCertNo){
		if($stateParams.paymentCertNo == '0'){
			$cookieStore.put('paymentCertNo', '');
			$cookieStore.put('paymentTerms', $stateParams.paymentTerms);
			createPayment();
		}else{
			$cookieStore.put('paymentCertNo', $stateParams.paymentCertNo);
			$cookieStore.put('paymentTerms', $stateParams.paymentTerms);
		}
	}
	$scope.paymentCertNo = $cookieStore.get('paymentCertNo');
	$scope.paymentTerms = $cookieStore.get('paymentTerms');

	

	$scope.convertPaymentStatus = function(status){
		if(status!=null){
			if (status.localeCompare('PND') == 0) {
				return "Pending";
			}else if (status.localeCompare('SBM') == 0) {
				return "Submitted";
			}else if (status.localeCompare('UFR') == 0) {
				return { width: "Under Finance Review" }
			}else if (status.localeCompare('PCS') == 0) {
				return { width: "Waiting For Posting" }
			}else if (status.localeCompare('APR') == 0) {
				return "Posted To Finance";
			}
		}
	}
	
	$scope.update = function(){
		modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please create a payment certificate.");
	}

	$scope.calculatePaymentDueDate = function() {
		console.log("$scope.paymentTerms.substring(0, 3):"+ $scope.paymentTerms.substring(0, 3));
		if($scope.paymentTerms.substring(0, 3) != "QS0"){
			paymentService.calculatePaymentDueDate($scope.jobNo,  $scope.subcontractNo, $scope.mainCertNo.selected, $scope.payment.asAtDate, $scope.payment.invoiceReceivedDate, $scope.payment.dueDate)
			.then(
					function( data ) {
						console.log(data);
						if(data!=null){
							if(data.valid ==true)
								$scope.payment.dueDate = data.dueDate;
							else
								modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data.errorMsg);
						}
					});
		}
	}
	
	
	function getPaymentCert() {
		if($scope.paymentCertNo != ""){
			paymentService.getPaymentCert($scope.jobNo, $scope.subcontractNo, $scope.paymentCertNo)
			.then(
					function( data ) {
						console.log(data);
						$scope.payment = data;
						
						getPaidMainCertList();
						
						if($scope.payment.paymentStatus == "PND")
							$scope.disableButtons = false;
		
					});
		}else
			$scope.disableButtons = false;
	}
	

	function getPaidMainCertList() {
		mainCertService.getPaidMainCertList($scope.jobNo)
		.then(
				function( data ) {
					console.log(data);
					$scope.mainCertNo.options = data;
					$scope.mainCertNo.selected = data.mainContractPaymentCertNo;

				});
	}
	
	
	
	function createPayment() {
		paymentService.createPayment($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					console.log(data);
					
					getPaymentCert();

				});
	}

}]);