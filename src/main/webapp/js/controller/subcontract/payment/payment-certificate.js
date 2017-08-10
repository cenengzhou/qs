mainApp.controller('PaymentCertCtrl', ['$scope' , '$stateParams', '$cookies', 'paymentService', 'mainCertService', 'modalService', 'GlobalParameter',
                                       function($scope, $stateParams, $cookies, paymentService, mainCertService, modalService, GlobalParameter) {
	$scope.GlobalParameter = GlobalParameter;
	$scope.disableButtons = true;

	$scope.mainCertNo = {
			options: [],
			selected: ""
	};

	
	if($stateParams.paymentCertNo){
		if($stateParams.paymentCertNo == '0'){
			$cookies.put('paymentCertNo', '');
			$cookies.put('paymentTermsDesc', $stateParams.paymentTermsDesc);
			createPayment();
		}else{
			$cookies.put('paymentCertNo', $stateParams.paymentCertNo);
			$cookies.put('paymentTermsDesc', $stateParams.paymentTermsDesc);
		}
	}
	$scope.paymentCertNo = $cookies.get('paymentCertNo');
	$scope.paymentTermsDesc = $cookies.get('paymentTermsDesc');
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
				return "Under Finance Review";
			}else if (status.localeCompare('PCS') == 0) {
				return "Waiting For Posting";
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
						if(data != null){
							if(data.length == 0){
								modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Payment has been updated successfully.");
								loadData();
							}else
								modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
						}
					});
		}else{
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Payment is not in Pending status.");
		}

	}

	$scope.calculatePaymentDueDate = function() {
		if($scope.paymentTerms != "QS0"){
			paymentService.calculatePaymentDueDate($scope.jobNo,  $scope.subcontractNo, $scope.mainCertNo.selected, $scope.payment.asAtDate, $scope.payment.invoiceReceivedDate, $scope.payment.dueDate)
			.then(
					function( data ) {
						console.log(data);
						if(data!=null){
							if(data.valid ==true){
								if(data.dueDate !=null)
									$scope.payment.dueDate = (new Date(data.dueDate)).yyyymmdd();
								else
									$scope.payment.dueDate = null;
							}
							else
								modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data.errorMsg);
						}
					});
		}
	}

	$scope.updatePaymentType = function(paymentType){
		if(!$scope.disableButtons)
			paymentService.updatePaymentDetails($scope.jobNo, $scope.subcontractNo, $cookies.get('paymentCertNo'), paymentType)
			.then(
					function( data ) {
						if(data.length != 0){
							modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
						}else{
							modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Payment Certificate has been updated.");
							loadData();
						}
					});
	}
	
	function loadData(){
		if($scope.paymentCertNo != ""){
			getPaymentCert();
			getMainCertNoList();
		}
	}

	function getPaymentCert() {
		paymentService.getPaymentCert($scope.jobNo, $scope.subcontractNo, $scope.paymentCertNo)
		.then(
				function( data ) {
					$scope.payment = data;
					$scope.mainCertNo.selected = data.mainContractPaymentCertNo;
					
					//Get GST value for Singapore jobs
					if($scope.jobNo.indexOf("14") > -1){
						getGSTPayble();
					}else
						$scope.totalCertAmount = data.certAmount;
					
					if($scope.payment.paymentStatus == "PND")
						$scope.disableButtons = false;

				});

	}

	function getGSTPayble() {
		paymentService.getGSTAmount($scope.jobNo, $scope.subcontractNo, $scope.paymentCertNo, "GP")
		.then(
				function( data ) {
					$scope.gstPayable = data;
					getGSTReceivable();
				});
	}

	function getGSTReceivable() {
		paymentService.getGSTAmount($scope.jobNo, $scope.subcontractNo, $scope.paymentCertNo, "GR")
		.then(
				function( data ) {
					$scope.gstReceivable = data;
					$scope.totalCertAmount = $scope.payment.certAmount + $scope.gstPayable - $scope.gstReceivable;
				});
	}


	/*function getPaidMainCertList() {
		mainCertService.getPaidMainCertList($scope.jobNo)
		.then(
				function( data ) {
					$scope.mainCertNo.options = data;
				});
	}*/

	
	function getMainCertNoList() {
	mainCertService.getMainCertNoList($scope.jobNo)
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

					$cookies.put('paymentCertNo', $scope.payment.paymentCertNo);
					$scope.paymentCertNo = $cookies.get('paymentCertNo');
					
					
					getMainCertNoList();

					if($scope.payment.paymentStatus == "PND")
						$scope.disableButtons = false;

				});

	}
	
	Date.prototype.yyyymmdd = function() {
		var yyyy = this.getFullYear().toString();
		var mm = (this.getMonth()+1).toString(); // getMonth() is zero-based
		var dd  = this.getDate().toString();

		return  yyyy+"-"+(mm.length===2?mm:"0"+mm) +"-"+ (dd.length===2?dd:"0"+dd); // padding
	};
	

}]);