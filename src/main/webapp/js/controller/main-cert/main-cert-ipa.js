mainApp.controller('IPACtrl', ['$scope', 'mainCertService', '$cookies', '$stateParams', '$location', 'modalService', 'confirmService', '$state', 'GlobalParameter', '$q',
                                       function ($scope,  mainCertService, $cookies, $stateParams, $location, modalService, confirmService, $state, GlobalParameter, $q) {
	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");
	$scope.GlobalParameter = GlobalParameter;
	$scope.disableButtons = true;

	$scope.previousCertNetAmount = 0;
	$scope.previousGSTReceivable = 0;
	$scope.previousGSTPayable = 0;

	$scope.prevCert = null;
	$scope.certMovement = {
		appliedMainContractorAmount : 0,
		appliedNSCNDSCAmount : 0,
		appliedClaimsAmount : 0,
		appliedVariationAmount : 0,
		appliedMOSAmount : 0,
		appliedMainContractorRetentionReleased : 0,
		appliedRetentionforNSCNDSCReleased : 0,
		appliedMOSRetentionReleased : 0,
		appliedAdjustmentAmount : 0,
		appliedAdvancePayment : 0,
		appliedCPFAmount : 0,
		appliedMainContractorRetention : 0,
		appliedRetentionforNSCNDSC : 0,
		appliedMOSRetention : 0,
		appliedContraChargeAmount : 0
	};
	$scope.isMovementColumnShown = false;

	$scope.getLabelCss = isMovementColumnShown => mainCertService.getLabelCss(isMovementColumnShown);
	$scope.getCertColCss = isMovementColumnShown => mainCertService.getCertColCss(isMovementColumnShown);
	$scope.getMvmtColCss = isMovementColumnShown => mainCertService.getMvmtColCss(isMovementColumnShown);

	$scope.mainCertNo = $cookies.get("mainCertNo");

	if($scope.mainCertNo != null && $scope.mainCertNo.length > 0){
		// View previous certificate, or new IPA is saved
		handleGetCertificateAndCalculateDiff($scope.mainCertNo);
	}
	else {
		// Create new main certificate
		handleGetLatestMainCertAndPrevCert();
	}

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

	$scope.insertIPA = function() {
		if(!$scope.fieldChanged){

			var modalOptions = {
					bodyText: "You WON'T be able to edit IPA after sending it to JDE finance, confirm?"
			};
			confirmService.showModal({}, modalOptions)
			.then(function (result) {
				if(result == "Yes"){

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

				}
			});


		}else{
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Certificate has been modified, please save it first.");
		}
	}

	$scope.$watch('cert', function(newValue, oldValue) {
		if(oldValue != null){
			$scope.fieldChanged = true;
			if ($scope.isMovementColumnShown)
				$scope.certMovement = mainCertService.calculateMovement($scope.certMovement, $scope.cert, $scope.prevCert, 'applied')
		}

	}, true);

	$scope.$watch('certMovement', function(newValue, oldValue) {
		if (oldValue != null && $scope.isMovementColumnShown)
			$scope.cert = mainCertService.calculateCertFromMovement($scope.cert, $scope.prevCert, $scope.certMovement)
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

	function handleGetCertificateAndCalculateDiff(mainCertNo){
		var getCert = mainCertService.getCertificate($scope.jobNo, mainCertNo);
		var getPrevCert = mainCertService.getCertificate($scope.jobNo, mainCertNo - 1);

		var promises = [getCert];
		if (mainCertNo > 1)
			promises.push(getPrevCert)

		$q.all(promises).then(function(result) {
			var certData = result[0]
			if (certData) {
				$scope.cert = certData;
				if ($scope.cert.certificateStatus < 200)
					$scope.disableButtons = false;
				else
					$scope.disableButtons = true;
			}
			if (result.length > 1 && result[1]) {
				var prevCertData = result[1]
				$scope.prevCert = prevCertData
				$scope.certMovement = mainCertService.calculateMovement($scope.certMovement, result[0], result[1], 'applied')
				$scope.isMovementColumnShown = true
			}
		})
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

	function createMainCert(){
		mainCertService.createMainCert($scope.cert)
		.then(
				function( data ) {
					if(data != null && data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "New Main Contract Certificate has been added.");
						$scope.fieldChanged = false;
						$location.path("/main-cert-select");
					}
				});
	}



	function handleGetLatestMainCertAndPrevCert() {
		mainCertService.getLatestMainCert($scope.jobNo)
			.then(function (data) {
				$scope.disableButtons = false;
				if (data.length != 0) {
					$scope.cert = {
						id: '',
						jobNo: data.jobNo,
						certificateNumber: data.certificateNumber + 1,
						appliedMainContractorAmount: data.appliedMainContractorAmount,
						appliedNSCNDSCAmount: data.appliedNSCNDSCAmount,
						appliedClaimsAmount: data.appliedClaimsAmount,
						appliedVariationAmount: data.appliedVariationAmount,
						appliedMOSAmount: data.appliedMOSAmount,
						appliedMainContractorRetentionReleased: data.appliedMainContractorRetentionReleased,
						appliedRetentionforNSCNDSCReleased: data.appliedRetentionforNSCNDSCReleased,
						appliedMOSRetentionReleased: data.appliedMOSRetentionReleased,
						appliedAdvancePayment: data.appliedAdvancePayment,
						appliedAdjustmentAmount: data.appliedAdjustmentAmount,
						appliedCPFAmount: data.appliedCPFAmount,
						appliedMainContractorRetention: data.appliedMainContractorRetention,
						appliedRetentionforNSCNDSC: data.appliedRetentionforNSCNDSC,
						appliedMOSRetention: data.appliedMOSRetention,
						appliedContraChargeAmount: data.appliedContraChargeAmount,

						certifiedMainContractorAmount: data.certifiedMainContractorAmount,
						certifiedNSCNDSCAmount: data.certifiedNSCNDSCAmount,
						certifiedClaimsAmount: data.certifiedClaimsAmount,
						certifiedVariationAmount: data.certifiedVariationAmount,
						certifiedMOSAmount: data.certifiedMOSAmount,
						certifiedMainContractorRetentionReleased: data.certifiedMainContractorRetentionReleased,
						certifiedRetentionforNSCNDSCReleased: data.certifiedRetentionforNSCNDSCReleased,
						certifiedMOSRetentionReleased: data.certifiedMOSRetentionReleased,
						certifiedAdvancePayment: data.certifiedAdvancePayment,
						certifiedAdjustmentAmount: data.certifiedAdjustmentAmount,
						certifiedCPFAmount: data.certifiedCPFAmount,
						certifiedMainContractorRetention: data.certifiedMainContractorRetention,
						certifiedRetentionforNSCNDSC: data.certifiedRetentionforNSCNDSC,
						certifiedMOSRetention: data.certifiedMOSRetention,
						certifiedContraChargeAmount: data.certifiedContraChargeAmount,

						gstReceivable: data.gstReceivable,
						gstPayable: data.gstPayable
					}

					$scope.previousCertNetAmount = data.certNetAmount;
					$scope.previousGSTReceivable = data.gstReceivable;
					$scope.previousGSTPayable = data.gstPayable;
				} else {
					$scope.cert = {
						id: '',
						jobNo: $scope.jobNo,
						certificateNumber: 1,
						appliedMainContractorAmount: 0,
						appliedClaimsAmount: 0,
						appliedVariationAmount: 0,
						appliedNSCNDSCAmount: 0,
						appliedMOSAmount: 0,
						appliedMainContractorRetentionReleased: 0,
						appliedRetentionforNSCNDSCReleased: 0,
						appliedMOSRetentionReleased: 0,
						appliedAdvancePayment: 0,
						appliedAdjustmentAmount: 0,
						appliedCPFAmount: 0,
						appliedMainContractorRetention: 0,
						appliedRetentionforNSCNDSC: 0,
						appliedMOSRetention: 0,
						appliedContraChargeAmount: 0,

						certifiedMainContractorAmount: 0,
						certifiedNSCNDSCAmount: 0,
						certifiedClaimsAmount: 0,
						certifiedVariationAmount: 0,
						certifiedMOSAmount: 0,
						certifiedMainContractorRetentionReleased: 0,
						certifiedRetentionforNSCNDSCReleased: 0,
						certifiedMOSRetentionReleased: 0,
						certifiedAdvancePayment: 0,
						certifiedAdjustmentAmount: 0,
						certifiedCPFAmount: 0,
						certifiedMainContractorRetention: 0,
						certifiedRetentionforNSCNDSC: 0,
						certifiedMOSRetention: 0,
						certifiedContraChargeAmount: 0,

						gstReceivable: 0,
						gstPayable: 0
					}

				}
				return $scope.cert.certificateNumber
			})
			.then(function (certNo) {
				if (certNo > 1) {
					mainCertService.getCertificate($scope.jobNo, certNo - 1)
						.then(function (data) {
							$scope.prevCert = data;
						})
						.then(function() {
							if ($scope.cert && $scope.prevCert) {
								$scope.certMovement = mainCertService.calculateMovement($scope.certMovement, $scope.cert, $scope.prevCert, 'applied')
								$scope.isMovementColumnShown = true
							}
						})
				}
			});

	}
	
	
	
	/*document.getElementById("number").onblur =function (){    
		this.value = parseFloat(this.value.replace(/,/g, ""))
		.toFixed(2)
		.toString()
		.replace(/\B(?=(\d{3})+(?!\d))/g, ",");

		//document.getElementById("display").value = this.value.replace(/,/g, "")

	}*/

}]);

