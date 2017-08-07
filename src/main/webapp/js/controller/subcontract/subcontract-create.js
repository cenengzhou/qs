mainApp.controller("SubcontractCreateCtrl", ['$scope', 'jobService', 'subcontractService', '$cookies', '$timeout', 'modalService', 'subcontractRetentionTerms', '$state', 'GlobalParameter', 'paymentService', 'confirmService', 'rootscopeService',
                                                  function ($scope, jobService, subcontractService, $cookies, $timeout, modalService, subcontractRetentionTerms, $state, GlobalParameter, paymentService, confirmService, rootscopeService) {
	$scope.GlobalParameter = GlobalParameter;
	getSubcontract();
	getJob();
	rootscopeService.gettingWorkScopes()
	.then(function(response){
		$scope.allWorkScopes = response.workScopes;
	});
	$scope.subcontract = {
			id:"",
			packageNo : "",
			description : "",
			workscope : "",
			approvalSubType : "",

			//Subcontractor Nature
			subcontractorNature : "DSC",

			//Subcontract Term
			subcontractTerm : "Lump Sum",

			labourIncludedContract : false,
			plantIncludedContract : false,
			materialIncludedContract : false,


			//Form of Subcontract
			formOfSubcontract : "Major",
			internalJobNo : "",

			//Payment Terms
			paymentTerms : "QS2",
			paymentTermsDescription: "",

			//CPF Calculation
			cpfCalculation : false,//
			cpfBasePeriod : "",
			cpfBaseYear: "",

			//Rentention
			retentionTerms: "Percentage",

			retentionAmount : 0,
			maxRetentionPercentage : 5,
			interimRentionPercentage : 10,
			mosRetentionPercentage : 10
	};

	//Rentention
	$scope.percentageOption= "Revised";
	
	$scope.updateLabour = function (){
		if(!$scope.disableButtons){
			if ($scope.subcontract.labourIncludedContract == true){
				$scope.subcontract.labourIncludedContract = false;
			}else
				$scope.subcontract.labourIncludedContract = true;
		}
	}

	$scope.updatePlant = function (){
		if(!$scope.disableButtons){
			if ($scope.subcontract.plantIncludedContract == true){
				$scope.subcontract.plantIncludedContract = false;
			}else
				$scope.subcontract.plantIncludedContract = true;
		}
	}

	$scope.updateMaterial = function (){
		if(!$scope.disableButtons){
			if ($scope.subcontract.materialIncludedContract == true){
				$scope.subcontract.materialIncludedContract = false;
			}else
				$scope.subcontract.materialIncludedContract = true;
		}
	}

	$scope.resetRetentionOptions = function (){
		if ($scope.subcontract.retentionTerms == "No Retention"){
			$scope.subcontract.retentionAmount = 0;
			$scope.subcontract.maxRetentionPercentage = 0;
			$scope.subcontract.interimRentionPercentage = 0;
			$scope.subcontract.mosRetentionPercentage = 0;
		}
		else if ($scope.subcontract.retentionTerms == "Lump Sum"){
			$scope.subcontract.retentionAmount = 0;
			$scope.subcontract.maxRetentionPercentage = 0;
			$scope.subcontract.interimRentionPercentage = 0;
			$scope.subcontract.mosRetentionPercentage = 0;
		}
		else if ($scope.subcontract.retentionTerms == "Percentage"){
			$scope.subcontract.retentionAmount = 0;
			$scope.subcontract.maxRetentionPercentage = 5;
			$scope.subcontract.interimRentionPercentage = 10;
			$scope.subcontract.mosRetentionPercentage = 10;
			
			
		}
		
	}
	
	$scope.resetInternalJob = function (){
		$scope.subcontract.internalJobNo = "";
	}
	$scope.paymentTermsHints;
	$scope.setPaymentTermsDescription = function(terms){
		var paymentTerms = GlobalParameter.getObjectById(GlobalParameter.paymentTerms, terms);
		$scope.paymentTermsHints = paymentTerms;
//		if(paymentTerms) angular.element('#paymentTermsDescriptionTextarea').val(paymentTerms.id + ' - ' + paymentTerms.value);
	}

	$scope.updateApprovalSubType = function (approvalSubType){
		 $scope.subcontract.approvalRoute = approvalSubType;
	}
	
	$scope.updateStandardTerms = function (formOfSubcontract){
		if(formOfSubcontract == 'Major'){
			$scope.subcontract.retentionTerms = "Percentage";
			$scope.subcontract.paymentTerms = "QS2";
		}
		else if(formOfSubcontract == 'Minor'){
			$scope.subcontract.retentionTerms = "Percentage";
			$scope.subcontract.paymentTerms = "QS2";
		}
		else if(formOfSubcontract == 'Consultancy Agreement'){
			$scope.subcontract.retentionTerms = "No Retention";
			$scope.subcontract.paymentTerms = "QS4";
		}
		else if(formOfSubcontract == 'Internal Trading'){
			$scope.subcontract.retentionTerms = "No Retention";
			$scope.subcontract.paymentTerms = "QS1";
		}
		$scope.resetRetentionOptions();
		$scope.setPaymentTermsDescription($scope.subcontract.paymentTerms);
	}
	
	//Save Function
	$scope.save = function () {
		
		if (false === $('form[name="form-validate"]').parsley().validate()) {
			event.preventDefault();  
			return;
		}
		
		if(!$scope.subcontract.labourIncludedContract && !$scope.subcontract.plantIncludedContract && !$scope.subcontract.materialIncludedContract){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select subcontract type.");
			return;
		}
			
		if($scope.subcontract.retentionTerms == null || $scope.subcontract.retentionTerms.trim().length == 0){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select Retention.");
			return;
		}

		
		$scope.subcontractToUpdate = {
				id:  $scope.subcontract.id,
				packageNo : $scope.subcontract.packageNo,
				description: $scope.subcontract.description,
				subcontractorNature: $scope.subcontract.subcontractorNature,
				subcontractTerm: $scope.subcontract.subcontractTerm,
				formOfSubcontract: $scope.subcontract.formOfSubcontract,
				internalJobNo: $scope.subcontract.internalJobNo,
				retentionTerms: $scope.subcontract.retentionTerms,
				paymentTerms: $scope.subcontract.paymentTerms,
				paymentTermsDescription: $scope.subcontract.paymentTermsDescription,
				retentionAmount : $scope.subcontract.retentionAmount,
				maxRetentionPercentage: $scope.subcontract.maxRetentionPercentage,
				interimRentionPercentage: $scope.subcontract.interimRentionPercentage,
				mosRetentionPercentage: $scope.subcontract.mosRetentionPercentage,
				labourIncludedContract : $scope.subcontract.labourIncludedContract,
				plantIncludedContract : $scope.subcontract.plantIncludedContract,
				materialIncludedContract : $scope.subcontract.materialIncludedContract,
				notes: $scope.subcontract.notes,
				approvalRoute: $scope.subcontract.approvalRoute,
				cpfCalculation : $scope.subcontract.cpfCalculation,
				cpfBasePeriod : $scope.subcontract.cpfBasePeriod,
				cpfBaseYear: $scope.subcontract.cpfBaseYear,
				workscope: $scope.subcontract.workscope
		}
		
		
		if($scope.subcontract.retentionTerms == "Lump Sum"){
			$scope.subcontractToUpdate.retentionTerms = subcontractRetentionTerms.RETENTION_LUMPSUM;
		}
		else if($scope.subcontract.retentionTerms == "Percentage" && $scope.percentageOption == "Original"){
			$scope.subcontractToUpdate.retentionTerms = subcontractRetentionTerms.RETENTION_ORIGINAL;
		}
		else if($scope.subcontract.retentionTerms == "Percentage" && $scope.percentageOption == "Revised"){
			$scope.subcontractToUpdate.retentionTerms = subcontractRetentionTerms.RETENTION_REVISED;
		} 
		if($scope.subcontract.cpfCalculation=="No CPF"){
			$scope.subcontractToUpdate.cpfCalculation = "Not Subject to CPF";
		}
		
		if($scope.subcontract.cpfCalculation == true){
			$scope.subcontractToUpdate.cpfCalculation = "1";
		}else{
			$scope.subcontractToUpdate.cpfCalculation = "0";
			$scope.subcontractToUpdate.cpfBasePeriod = "";
			$scope.subcontractToUpdate.cpfBaseYear = "";
		}

		getWorkScope($scope.subcontract.workscope);
	};

	
	function getSubcontract(){
		subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					if(data.length != 0){
						$scope.subcontract = data;
						$scope.subcontract.workscope = '' + $scope.subcontract.workscope;//convert to str for ng-options
						$scope.subcontractStatus = data.scStatus + ' - ' + GlobalParameter.getValueById(GlobalParameter.subcontractStatus, data.scStatus);
						
						if($scope.subcontract.retentionTerms == subcontractRetentionTerms.RETENTION_LUMPSUM){
							$scope.subcontract.retentionTerms = "Lump Sum";
						}else if($scope.subcontract.retentionTerms == subcontractRetentionTerms.RETENTION_ORIGINAL){
							$scope.subcontract.retentionTerms = "Percentage";
							$scope.percentageOption= "Original";
							$scope.retentionAmount = 0;
						}else if($scope.subcontract.retentionTerms == subcontractRetentionTerms.RETENTION_REVISED){
							$scope.subcontract.retentionTerms = "Percentage";
							$scope.percentageOption= "Revised";
							$scope.retentionAmount = 0;
						}else{
							$scope.retentionAmount = 0;
						}
						
						if($scope.subcontract.cpfCalculation == "1"){
							$scope.subcontract.cpfCalculation = true;
						}else
							$scope.subcontract.cpfCalculation = false;
						
						if($scope.subcontract.scStatus =="330" || $scope.subcontract.scStatus =="500")
							$scope.disableButtons = true;
						else
							$scope.disableButtons = false;
						
						$scope.disableSubcontactNo = true;
					}else
						$scope.disableSubcontactNo = false;
					
					if(!$scope.subcontract.paymentTermsDescription){
						$timeout(function(){$scope.setPaymentTermsDescription($scope.subcontract.paymentTerms);}, 500);
					}
				});
	}
	
	function getJob(){
		jobService.getJob($scope.jobNo)
		.then(
				function( data ) {
					if(data.cpfApplicable=="1")
						$scope.disableCPFButton = false;
					else
						$scope.disableCPFButton = true;
				});
	}
	
	
	function getWorkScope(workScopeCode){
//		subcontractService.getWorkScope(workScopeCode)
//		.then(
//				function( data ) {
//					if(data.length==0){
//						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Work Scope "+workScopeCode+" does not exist.");
//					}else{
//						if($scope.subcontractToUpdate.id == null){
//							upateSubcontract();
//						} else {
//							getLatestPaymentCert();
//						}
//					}
//				});
		var wsFound = $scope.allWorkScopes.some(function(ws){
			return ws.codeWorkscope.indexOf(workScopeCode) > -1;
		});
		
		if(wsFound){
			if(!$scope.subcontractToUpdate.id){
				upateSubcontract();
			} else {
				getLatestPaymentCert();
			}
		} else {
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Work Scope "+workScopeCode+" does not exist.");
		}
	}

	function upateSubcontract(){
	subcontractService.upateSubcontract($cookies.get("jobNo"), $scope.subcontractToUpdate)
	.then(
			function( data ) {
				if(data.length>0){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
				}else{
			    	modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Subcontract has been saved successfully.");
					$cookies.put('subcontractNo', $scope.subcontractToUpdate.packageNo);
			    	$cookies.put('subcontractDescription', $scope.subcontractToUpdate.description);
			    	$cookies.put('paymentStatus', '');
			    	$state.reload();
				}

				//Download file				 
				//$window.open("gammonqs/scPaymentDownload.smvc?jobNumber=13362&packageNumber=1006&paymentCertNo=5", "_blank", "");
				//$window.open("gammonqs/subcontractReportExport.rpt?company=&division=&jobNumber=13362&subcontractNumber=&subcontractorNumber=&subcontractorNature=&paymentType=&workScope=&clientNo=&includeJobCompletionDate=false&splitTerminateStatus=&month=&year=", "_blank", "");

			});
	}

	
	function getLatestPaymentCert() {
		paymentService.getLatestPaymentCert($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					if(data){
						if(data.paymentStatus == 'PND'){
							var modalOptions = {
									bodyText: "Payment Requisition with status 'Pending' will be deleted. Proceed?"
							};


							confirmService.showModal({}, modalOptions).then(function (result) {
								if(result == "Yes"){
									upateSubcontract();
								}
							});
						}else if(data.paymentStatus == 'APR'){
							upateSubcontract();
						}else{
							modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Payment Requisition is being submitted. No amendment is allowed.");
						}
					}else{
						upateSubcontract();
					}
				});
	}
	
}]);

