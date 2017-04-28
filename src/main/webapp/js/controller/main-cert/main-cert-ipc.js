mainApp.controller('IPCCtrl', ['$q', '$scope', 'mainCertService', '$cookies', '$location', 'modalService', 'confirmService', '$state', 'roundUtil', 'GlobalParameter', 'attachmentService',
                                       function ($q, $scope,  mainCertService, $cookies,  $location, modalService, confirmService, $state, roundUtil, GlobalParameter, attachmentService) {
	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");
	$scope.GlobalParameter = GlobalParameter;
	$scope.disableButtons = true;

	$scope.previousCertNetAmount = 0;
	$scope.previousGSTReceivable = 0;
	$scope.previousGSTPayable = 0;

	
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


	$scope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams){ 
		if($scope.fieldChanged){
			event.preventDefault();
			var modalOptions = {
					bodyText: "There are unsaved data, do you want to leave without saving?"
			};
			confirmService.showModal({}, modalOptions)
			.then(function (result) {
				if(result == "Yes"){
					$scope.fieldChanged = false;
					$state.go(toState.name);
				}
			});
			
		}
	});
	
	function getCertificate(mainCertNo){
		mainCertService.getCertificate($scope.jobNo, mainCertNo)
		.then(
				function( data ) {
					$scope.cert = data;
					if($scope.cert.certificateStatus < 200)
						$scope.disableButtons = false;
					else
						$scope.disableButtons = true;

					$scope.currentCertNetAmount = data.certNetAmount;
					
					if(mainCertNo > 1){
						mainCertService.getCertificate($scope.jobNo, mainCertNo - 1)
						.then(
								function( data ) {
									$scope.previousCertNetAmount = data.certNetAmount;
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
						$scope.fieldChanged = false;
						$state.reload();
					}
				});
	}





	$scope.confirmIPC = function() {
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

	}

	$scope.resetIPC = function() {
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
	}

	function checkAttachment(){
		var deferral = $q.defer();
		var nameObject = GlobalParameter['AbstractAttachment'].MainCertNameObject;
		var textKey = $scope.jobNo + '|' + $scope.mainCertNo + '|';
    	attachmentService.getAttachmentListForPCMS(nameObject, textKey)
    	.then(function(data){
    		deferral.resolve({
    			attachmentSize: data.length
    		});
		});
    	return deferral.promise;
	}

	$scope.postIPC = function() {
		checkAttachment()
		.then(function(response){
			if(response.attachmentSize > 0) {
				confirmPostIPC();
			} else {
				var modalOptions = {
						bodyText: "There is no attachment, do you want to continue?"
				};
				confirmService.showModal({}, modalOptions)
				.then(function (result) {
					if(result == "Yes"){
						confirmPostIPC();
					}
				});
			}
			
		})

	}
	
	function confirmPostIPC(){
		$scope.postingAmount = roundUtil.round($scope.currentCertNetAmount - $scope.previousCertNetAmount, 2);
		var message = "Main Contract Certificate Net Movement Amount (without GST):  $"+$scope.postingAmount+"<br/>" 
					+" Net GST Receivable Movement Amount: $" + roundUtil.round($scope.cert.gstReceivable - $scope.previousGSTReceivable, 2) + "<br/>" 
					+ "Net GST Payable Movement Amount: $" + roundUtil.round($scope.cert.gstPayable - $scope.previousGSTPayable, 2) + "<br/><br/>"
					+ "Are you sure you want to post this Main Certificate?<br/>" ;
		
		if($scope.postingAmount < 0){
			var modalOptions = {
					bodyText: message + "Approval with approval route(RM) is required."
			};

			confirmService.showModal({}, modalOptions).then(function (result) {
				if(result == "Yes"){
					submitNegativeMainCertForApproval();
				}
			});

		}else{
			var modalOptions = {
					bodyText: message
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
	
	$scope.convertCertStatus = function(status){
		if(status!=null){
			if (status.localeCompare('100') == 0) {
				return "Certificate Created";
			}else if (status.localeCompare('120') == 0) {
				return "IPA Sent";
			}else if (status.localeCompare('150') == 0) {
				return "Certificate(IPC) Confirmed";
			}else if (status.localeCompare('200') == 0) {
				return "Certificate(IPC) Waiting for special approval";
			}else if (status.localeCompare('300') == 0) {
				return "Certificate Posted to Finance's AR";
			}else if (status.localeCompare('400') == 0) {
				return "Certifited Amount Received";
			}
		}
	}

}]);

