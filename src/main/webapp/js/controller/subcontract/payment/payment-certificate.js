mainApp.controller('PaymentCertCtrl', ['$scope' , '$state', '$stateParams', '$cookieStore', 'paymentService', 'mainCertService', 'modalService', 
                                       function($scope , $state, $stateParams, $cookieStore, paymentService, mainCertService, modalService) {

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
	$scope.paymentTerms = $scope.paymentTermsDesc.substring(0, 3);

	//Initiation
	loadData();

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
		if($scope.payment.paymentStatus == "PND"){
			
			if (false === $('form[name="form-validate"]').parsley().validate()) {
				event.preventDefault();  
				return;
			}
			
			
			$scope.payment.mainContractPaymentCertNo =  $scope.mainCertNo.selected;
			paymentService.updatePaymentCertificate($scope.jobNo, $scope.subcontractNo, $scope.paymentCertNo, $scope.paymentTerms, $scope.gstPayable, $scope.gstReceivable, $scope.payment)
			.then(
					function( data ) {
						console.log(data);
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Payment has been updated successfully.");
						$state.reload();
					});
		}else{
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Payment is not in Pending status.");
		}

	}

	$scope.calculatePaymentDueDate = function() {console.log('calculatePaymentDueDate');
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

	$scope.updatePaymentType = function(paymentType){
		paymentService.updatePaymentDetails($scope.jobNo, $scope.subcontractNo, $cookieStore.get('paymentCertNo'), paymentType)
		.then(
				function( data ) {
					if(data.length != 0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Payment Certificate has been updated.");
						$state.reload();
					}
				});
	}
	
	function loadData(){
		if($scope.paymentCertNo != ""){
			getPaymentCert();
			getGSTPayble();
			getGSTReceivable();
			getPaidMainCertList();
		}
	}

	function getPaymentCert() {
		paymentService.getPaymentCert($scope.jobNo, $scope.subcontractNo, $scope.paymentCertNo)
		.then(
				function( data ) {
					console.log(data);
					$scope.payment = data;
					$scope.mainCertNo.selected = data.mainContractPaymentCertNo;

					if($scope.payment.paymentStatus == "PND")
						$scope.disableButtons = false;

				});

	}

	function getGSTPayble() {
		paymentService.getGSTAmount($scope.jobNo, $scope.subcontractNo, $scope.paymentCertNo, "GP")
		.then(
				function( data ) {
					$scope.gstPayable = data;
				});
	}

	function getGSTReceivable() {
		paymentService.getGSTAmount($scope.jobNo, $scope.subcontractNo, $scope.paymentCertNo, "GR")
		.then(
				function( data ) {
					$scope.gstReceivable = data;
				});
	}


	function getPaidMainCertList() {
		mainCertService.getPaidMainCertList($scope.jobNo)
		.then(
				function( data ) {
					$scope.mainCertNo.options = data;
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
	
	/*$scope.$watch('payment.asAtDate', function(newValue, oldValue) {
		console.log("newValue: "+newValue);
		console.log("oldValue: "+oldValue);

		if (false === $('form[name="form-validate"]').parsley().validate("validate-asAtDate")) {
			event.preventDefault();  
			return;
		}
		
		if(oldValue != null)
			$scope.calculatePaymentDueDate();
	});*/

}]);