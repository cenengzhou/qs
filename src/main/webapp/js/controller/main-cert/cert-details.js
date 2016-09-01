mainApp.controller('CertDetailsCtrl', ['$scope', 'mainCertService', '$cookies', '$stateParams',
                                       function ($scope,  mainCertService, $cookies, $stateParams) {

	$scope.disableButtons = true;

	if($stateParams.mainCertNo){
		if($stateParams.mainCertNo == '0'){
			$cookies.put('mainCertNo', '');
		}else{
			$cookies.put('mainCertNo', $stateParams.mainCertNo);
		}
	}

	$scope.mainCertNo = $cookies.get("mainCertNo");
		
	
	if($scope.mainCertNo != null && $scope.mainCertNo.length > 0)
		getCertificate();
	else
		getLatestMainCert();		
	
	//Save Function
	$scope.save = function () {
		console.log("ipaSubmissionDate: " + $scope.ipaSubmissionDate);

	};

	function getLatestMainCert() {
		mainCertService.getLatestMainCert($scope.jobNo)
		.then(
				function( data ) {
					console.log(data);
					$scope.disableButtons = false;
					if(data.length !=0){
						$scope.cert = {
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
								appliedMOSRetentionReleased: 				data.appliedMOSRetentionReleased,
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
								certifiedMOSRetentionReleased:				data.certifiedMOSRetentionReleased,
								certifiedContraChargeAmount:				data.certifiedContraChargeAmount,
								
								gstReceivable: 								data.gstReceivable,
								gstPayable: 								data.gstPayable
						}
					}
					
				});
	}

	function getCertificate(){
		mainCertService.getCertificate($scope.jobNo, $scope.mainCertNo)
		.then(
				function( data ) {
					console.log(data);
					$scope.cert = data;
					if($scope.cert.length==0 || $scope.cert.status < 300)
						$scope.disableButtons = false;
					else
						$scope.disableButtons = true;
				});
	}


}]);

