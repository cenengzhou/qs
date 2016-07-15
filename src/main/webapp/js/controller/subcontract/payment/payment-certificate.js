mainApp.controller('PaymentCertCtrl', ['$scope' , '$http', '$stateParams', '$cookieStore', 'paymentService', 'mainCertService', 'modalService', 
                                          function($scope , $http, $stateParams, $cookieStore, paymentService, mainCertService, modalService) {
	
	$scope.disableButtons = true;

	$scope.mainCertNo = {
			options: [],
			selected: ""
	};

	if($stateParams.paymentCertNo){
		if($stateParams.paymentCertNo == '0'){
			$cookieStore.put('paymentCertNo', '');
			$cookieStore.put('paymentTermsDesc', $stateParams.paymentTermsDesc);
			createPayment();
		}else{
			$cookieStore.put('paymentCertNo', $stateParams.paymentCertNo);
			$cookieStore.put('paymentTermsDesc', $stateParams.paymentTermsDesc);
		}
	}
	$scope.paymentCertNo = $cookieStore.get('paymentCertNo');
	$scope.paymentTermsDesc = $cookieStore.get('paymentTermsDesc');
	console.log($cookieStore.get('paymentTermsDesc'));
	$scope.paymentTerms = $scope.paymentTermsDesc.substring(0, 3);
	
	getPaymentCert();

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
		if($scope.paymentTerms != "QS0"){
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
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else
						getLatestPaymentCert();

				});
	}

	
	function getLatestPaymentCert() {
		paymentService.getLatestPaymentCert($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					$scope.payment = data;
					
					$cookieStore.put('paymentCertNo', $scope.payment.paymentCertNo);
					$scope.paymentCertNo = $cookieStore.get('paymentCertNo');
					
					getPaidMainCertList();

					if($scope.payment.paymentStatus == "PND")
						$scope.disableButtons = false;

				});
		
	}
	
	$scope.$watch('payment.dueDate', function(newValue, oldValue) {
		console.log("newValue: "+newValue);
		console.log("oldValue: "+oldValue);
        
		if(oldValue != null)
			$scope.calculatePaymentDueDate();
    });
	
}]);