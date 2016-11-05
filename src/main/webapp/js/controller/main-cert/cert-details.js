mainApp.controller('CertDetailsCtrl', ['$scope', 'mainCertService', '$cookies', '$stateParams', '$location', 'modalService', 'confirmService', '$state', 'roundUtil', 'GlobalParameter',
                                       function ($scope,  mainCertService, $cookies, $stateParams, $location, modalService, confirmService, $state, roundUtil, GlobalParameter) {
	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");
	$scope.GlobalParameter = GlobalParameter;
	$scope.disableButtons = true;

	$scope.previousCertNetAmount = 0;
	$scope.previousGSTReceivable = 0;
	$scope.previousGSTPayable = 0;

	if($stateParams.mainCertNo){
		if($stateParams.mainCertNo == '0'){
			$cookies.put('mainCertNo', '');
		}else{
			$cookies.put('mainCertNo', $stateParams.mainCertNo);
		}
	}

	$scope.mainCertNo = $cookies.get("mainCertNo");


	if($scope.mainCertNo != null && $scope.mainCertNo.length > 0){
		getCertificate($scope.mainCertNo);
	}
	else
		getLatestMainCert();		

	//Save Function
	$scope.save = function () {
		$scope.disableButtons = true;

		if (false === $('form[name="form-validate"]').parsley().validate()) {
			event.preventDefault();  
			$scope.disableButtons = false;
			return;
		}


		if($scope.cert.id == null || $scope.cert.id.length == 0)	
			createMainCert();
		else
			updateCertificate();
	};

	$scope.editContraCharge = function() {
		if(!$scope.fieldChanged){
			
			if($scope.cert.id == null || $scope.cert.id.length == 0){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please save the current main contract certificate first.");
				return;
			}
			
			modalService.open('lg', 'view/main-cert/modal/contra-charge-modal.html', 'ContraChargeModalCtrl');
		}else{
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Certificate has been modified, please save it first.");
		}
	}
	
	$scope.$watch('cert', function(newValue, oldValue) {
		if(oldValue != null){
			$scope.fieldChanged = true;
		}

	}, true);


	$scope.openRetentionReleaseSchedule = function() {
		if(!$scope.fieldChanged){
			modalService.open('lg', 'view/main-cert/modal/retention-release-modal.html', 'RetentionReleaseModalCtrl');
		}else{
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Certificate has been modified, please save it first.");
		}
	}

	function getCertificate(mainCertNo){
		mainCertService.getCertificate($scope.jobNo, mainCertNo)
		.then(
				function( data ) {
					$scope.cert = data;
					if($scope.cert.length==0 || $scope.cert.certificateStatus < 200)
						$scope.disableButtons = false;
					else
						$scope.disableButtons = true;

					if(mainCertNo > 1){
						mainCertService.getCertificate($scope.jobNo, mainCertNo - 1)
						.then(
								function( data ) {
									$scope.previousCertNetAmount = data.certNetAmount;
									//console.log("$scope.previousCertNetAmount: "+$scope.previousCertNetAmount);
									$scope.previousGSTReceivable = data.gstReceivable;
									$scope.previousGSTPayable = data.gstPayable;
								});
					}
				});
	}


	function updateCertificate(){
		mainCertService.updateCertificate($scope.cert)
		.then(
				function( data ) {
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Main Contract Certificate has been updated.");
						$state.reload();
					}
				});
	}

	function createMainCert(){
		mainCertService.createMainCert($scope.cert)
		.then(
				function( data ) {
					if(data != null && data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "New Main Contract Certificate has been added.");
						$location.path("/main-cert-select");
					}
				});
	}


	$scope.insertIPA = function() {
		if(!$scope.fieldChanged){
			mainCertService.insertIPA($scope.cert)
			.then(
					function( data ) {
						if(data.length!=0){
							modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
						}else{
							modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "IPA has been sent out.");
							$state.reload();
						}
					});
		}else{
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Certificate has been modified, please save it first.");
		}
	}


	$scope.confirmIPC = function() {
		if(!$scope.fieldChanged){
			mainCertService.getCumulativeRetentionReleaseByJob($scope.jobNo, $scope.cert.certificateNumber)
			.then(
					function (data) {
						if(data.status == 'FAIL'){
							modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', data.message);
							return;
						}else{
							mainCertService.confirmIPC($scope.cert)
							.then(
									function( data ) {
										if(data !=null && data.length!=0){
											modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
										}else{
											modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "IPC has been confirmed.");
											$state.reload();
										}
									});
						}

					});
		}else{
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Certificate has been modified, please save it first.");
		}

	}

	$scope.resetIPC = function() {
		if(!$scope.fieldChanged){
			mainCertService.resetIPC($scope.cert)
			.then(
					function( data ) {
						if(data.length!=0){
							modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
						}else{
							modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "IPC has been reset.");
							$state.reload();
						}
					});
		}else{
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Certificate has been modified, please save it first.");
		}
	}

	$scope.postIPC = function() {
		if(!$scope.fieldChanged){
			if($scope.postingAmount < 0){
				var modalOptions = {
						bodyText: "Main Contract Certificate Net Movement Amount (without GST):  $"+roundUtil.round($scope.postingAmount, 2)+"<br/>" 
						+" Net GST Receivable Movement Amount: $" + roundUtil.round($scope.cert.gstReceivable - $scope.previousGSTReceivable, 2) + "<br/>" 
						+ "Net GST Payable Movement Amount: $" + roundUtil.round($scope.cert.gstPayable - $scope.previousGSTPayable, 2) + "<br/><br/>"
						+ "Are you sure you want to post this Main Certificate?<br/>" 
						+ "Approval with approval route(RM) is required."
				};

				confirmService.showModal({}, modalOptions).then(function (result) {
					if(result == "Yes"){
						submitNegativeMainCertForApproval();
					}
				});

			}else{
				var modalOptions = {
						bodyText: "Main Contract Certificate Net Movement Amount (without GST):  $"+roundUtil.round($scope.postingAmount, 2)+"<br/>"
						+ "Net GST Receivable Movement Amount: $" + roundUtil.round($scope.cert.gstReceivable - $scope.previousGSTReceivable, 2) + "<br/>"
						+ "Net GST Payable Movement Amount: $" + roundUtil.round($scope.cert.gstPayable - $scope.previousGSTPayable, 2) + "<br/>"
						+ "Are you sure you want to post this Main Certificate?"
				};

				confirmService.showModal({}, modalOptions).then(function (result) {
					if(result == "Yes"){
						mainCertService.postIPC($scope.jobNo, $scope.cert.certificateNumber)
						.then(
								function( data ) {
									if(data !=null && data.length!=0){
										modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
									}else{
										modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "IPC has been posted to JDE Finance.");
										$state.reload();
									}
								});
					}
				});
			}
		}else{
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Certificate has been modified, please save it first.");
		}
	}

	function submitNegativeMainCertForApproval(){
		mainCertService.submitNegativeMainCertForApproval($scope.jobNo, $scope.mainCertNo, $scope.postingAmount)
		.then(
				function( data ) {
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Main Contract Certificate has been submitted for Approval.");
						$location.path("/main-cert-select");
					}
				});
	}



	function getLatestMainCert() {
		mainCertService.getLatestMainCert($scope.jobNo)
		.then(
				function( data ) {
					$scope.disableButtons = false;
					if(data.length !=0){
						$scope.cert = {
								id:											'',
								jobNo:										data.jobNo,	
								certificateNumber: 							data.certificateNumber + 1,
								appliedMainContractorAmount: 				data.appliedMainContractorAmount,
								appliedNSCNDSCAmount: 						data.appliedNSCNDSCAmount,
								appliedMOSAmount: 							data.appliedMOSAmount,
								appliedMainContractorRetentionReleased: 	data.appliedMainContractorRetentionReleased ,
								appliedRetentionforNSCNDSCReleased: 		data.appliedRetentionforNSCNDSCReleased,
								appliedMOSRetentionReleased: 				data.appliedMOSRetentionReleased,
								appliedAdvancePayment:						data.appliedAdvancePayment,
								appliedAdjustmentAmount: 					data.appliedAdjustmentAmount,
								appliedCPFAmount: 							data.appliedCPFAmount,
								appliedMainContractorRetention: 			data.appliedMainContractorRetention,
								appliedRetentionforNSCNDSC: 				data.appliedRetentionforNSCNDSC,
								appliedMOSRetention: 						data.appliedMOSRetention,
								appliedContraChargeAmount:					data.appliedContraChargeAmount,

								certifiedMainContractorAmount:				data.certifiedMainContractorAmount, 
								certifiedNSCNDSCAmount:						data.certifiedNSCNDSCAmount,
								certifiedMOSAmount:							data.certifiedMOSAmount,
								certifiedMainContractorRetentionReleased:	data.certifiedMainContractorRetentionReleased,
								certifiedRetentionforNSCNDSCReleased:		data.certifiedRetentionforNSCNDSCReleased,
								certifiedMOSRetentionReleased:				data.certifiedMOSRetentionReleased,
								certifiedAdvancePayment: 					data.certifiedAdvancePayment,
								certifiedAdjustmentAmount:					data.certifiedAdjustmentAmount,
								certifiedCPFAmount:							data.certifiedCPFAmount,
								certifiedMainContractorRetention:			data.certifiedMainContractorRetention,
								certifiedRetentionforNSCNDSC:				data.certifiedRetentionforNSCNDSC,
								certifiedMOSRetention:						data.certifiedMOSRetention,
								certifiedContraChargeAmount:				data.certifiedContraChargeAmount,

								gstReceivable: 								data.gstReceivable,
								gstPayable: 								data.gstPayable
						}
						
						$scope.previousCertNetAmount = data.certNetAmount;
						$scope.previousGSTReceivable = data.gstReceivable;
						$scope.previousGSTPayable = data.gstPayable;
					}else{
						$scope.cert = {
								id:											'',
								jobNo:										$scope.jobNo,	
								certificateNumber: 							1,
								appliedMainContractorAmount: 				0,
								appliedNSCNDSCAmount: 						0,
								appliedMOSAmount: 							0,
								appliedMainContractorRetentionReleased: 	0,
								appliedRetentionforNSCNDSCReleased: 		0,
								appliedMOSRetentionReleased: 				0,
								appliedAdvancePayment:						0,
								appliedAdjustmentAmount: 					0,
								appliedCPFAmount: 							0,
								appliedMainContractorRetention: 			0,
								appliedRetentionforNSCNDSC: 				0,
								appliedMOSRetention:		 				0,
								appliedContraChargeAmount:					0,

								certifiedMainContractorAmount:				0, 
								certifiedNSCNDSCAmount:						0,
								certifiedMOSAmount:							0,
								certifiedMainContractorRetentionReleased:	0,
								certifiedRetentionforNSCNDSCReleased:		0,
								certifiedMOSRetentionReleased:				0,
								certifiedAdvancePayment: 					0,
								certifiedAdjustmentAmount:					0,
								certifiedCPFAmount:							0,
								certifiedMainContractorRetention:			0,
								certifiedRetentionforNSCNDSC:				0,
								certifiedMOSRetention:						0,
								certifiedContraChargeAmount:				0,

								gstReceivable: 								0,
								gstPayable: 								0
						}
						
					}

				});
	}
	
	$scope.openAttachment = function(){
		modalService.open('lg', 'view/main-cert/modal/main-cert-attachment-file.html', 'AttachmentMainCertFileCtrl', 'Success', $scope);
	}

	/*document.getElementById("number").onblur =function (){    
		this.value = parseFloat(this.value.replace(/,/g, ""))
		.toFixed(2)
		.toString()
		.replace(/\B(?=(\d{3})+(?!\d))/g, ",");

		//document.getElementById("display").value = this.value.replace(/,/g, "")

	}*/

}]);

