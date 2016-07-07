mainApp.controller("SubcontractCreateCtrl", ['$scope', 'subcontractService', '$cookieStore', 'modalService', 'subcontractRetentionTerms',
                                                  function ($scope, subcontractService, $cookieStore, modalService, subcontractRetentionTerms) {

	getSubcontract();
	
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
			selectedCPF : false,//
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
		if ($scope.subcontract.labourIncludedContract == true){
			$scope.subcontract.labourIncludedContract = false;
		}else
			$scope.subcontract.labourIncludedContract = true;
	}

	$scope.updatePlant = function (){
		if ($scope.subcontract.plantIncludedContract == true){
			$scope.subcontract.plantIncludedContract = false;
		}else
			$scope.subcontract.plantIncludedContract = true;
	}

	$scope.updateMaterial = function (){
		if ($scope.subcontract.materialIncludedContract == true){
			$scope.subcontract.materialIncludedContract = false;
		}else
			$scope.subcontract.materialIncludedContract = true;
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
	
	$scope.saveBoolean = false;

	//Save Function
	$scope.save = function () {
		$scope.saveBoolean = true;
		
		if (false === $('form[name="form-wizard-step-1"]').parsley().validate()) {
			event.preventDefault();  
			return;
		}
		
		$scope.newSubcontract = {
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
				//approvalSubType: $scope.subcontract.approvalSubType
				/*selectedCPF : $scope.subcontract.selectedCPF,
				cpfBasePeriod : $scope.subcontract.cpfBasePeriod,
				cpfBaseYear: $scope.subcontract.cpfBaseYear,
				*/

		}
		
		if($scope.subcontract.retentionTerms == "Lump Sum"){
			$scope.newSubcontract.retentionTerms = subcontractRetentionTerms.RETENTION_LUMPSUM;
		}
		else if($scope.subcontract.retentionTerms == "Percentage" && $scope.percentageOption == "Original"){
			$scope.newSubcontract.retentionTerms = subcontractRetentionTerms.RETENTION_ORIGINAL;
		}
		else if($scope.subcontract.retentionTerms == "Percentage" && $scope.percentageOption == "Revised"){
			$scope.newSubcontract.retentionTerms = subcontractRetentionTerms.RETENTION_REVISED;
		} 

		
		console.log($scope.newSubcontract);


		getWorkScope($scope.subcontract.workscope);
	};

	function getSubcontract(){
		subcontractService.getSubcontract($cookieStore.get("jobNo"), $cookieStore.get("subcontractNo"))
		.then(
				function( data ) {
					//console.log(data);
					$scope.subcontract = data;
					if($scope.subcontract.retentionTerms == subcontractRetentionTerms.RETENTION_LUMPSUM){
						$scope.subcontract.retentionTerms = "Lump Sum";
					}else if($scope.subcontract.retentionTerms == subcontractRetentionTerms.RETENTION_ORIGINAL){
						$scope.subcontract.retentionTerms = "Percentage";
						$scope.percentageOption= "Original";
					}else if($scope.subcontract.retentionTerms == subcontractRetentionTerms.RETENTION_REVISED){
						$scope.subcontract.retentionTerms = "Percentage";
						$scope.percentageOption= "Revised";
					} 

				});
	}
	
	function getWorkScope(workScopeCode){
		subcontractService.getWorkScope(workScopeCode)
		.then(
				function( data ) {
					if(data.length==0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Alert', "Work Scope "+workScopeCode+" does not exist.");
					}else{
						addSubcontract();
					}
				});
	}

	function addSubcontract(){
	subcontractService.addSubcontract($cookieStore.get("jobNo"), $scope.newSubcontract)
	.then(
			function( data ) {
				if(data.length>0){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Alert', data);
				}else{
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Subcontract has been saved successfully.");
				}

				//Download file				 
				//$window.open("gammonqs/scPaymentDownload.smvc?jobNumber=13362&packageNumber=1006&paymentCertNo=5", "_blank", "");
				//$window.open("gammonqs/subcontractReportExport.rpt?company=&division=&jobNumber=13362&subcontractNumber=&subcontractorNumber=&subcontractorNature=&paymentType=&workScope=&clientNo=&includeJobCompletionDate=false&splitTerminateStatus=&month=&year=", "_blank", "");

			});
	}

}]);

