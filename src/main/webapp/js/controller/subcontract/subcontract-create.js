mainApp.controller("SubcontractCreateModalCtrl", ['$scope', '$uibModalInstance', '$location', 'subcontractService', '$cookieStore',
                                                  function ($scope, $uibModalInstance, $location, subcontractService, $cookieStore) {
	$scope.subcontract = {
			subcontractNumber : 1010,
			description : "Testing SC1010",
			workscope : 206,
			approvalSubType : "",
			
			//Subcontractor Nature
			subcontractorNature : "DSC",
			
		    
		    //Subcontract Term
		    subcontractTerm : "Re-measurement",

		    //Form of Subcontract
		    formOfSubcontract : "Major",
			internalJobNo : "",
		    
			//Rentention
			retentionOption : "Percentage",
			
			
			//Payment Terms
			paymentTerms : "QS2",
			paymentTermsDesc: "",
			 
			//CPF Calculation
			selectedCPF : false,
			cpfPeriod : "",
			cpfYear: "",

			percentageOption: "Revised",
			
			lumpSumRetention : 5000,
			maxRetention : 5,
			interimRetention : 10,
			mosRetention : 10
	};
	
	
	
	$scope.updateLabour = function (){
		if ($scope.subcontract.checkedLabour == "Labour"){
			$scope.subcontract.checkedLabour = "";
		}else
			$scope.subcontract.checkedLabour = "Labour";
	}
	
	$scope.updatePlant = function (){
		if ($scope.subcontract.checkedPlant == "Plant"){
			$scope.subcontract.checkedPlant = "";
		}else
			$scope.subcontract.checkedPlant = "Plant";
	}
	
	$scope.updateMaterial = function (){
		if ($scope.subcontract.checkedMaterial == "Material"){
			$scope.subcontract.checkedMaterial = "";
		}else
			$scope.subcontract.checkedMaterial = "Material";
	}
	
	$scope.saveBoolean = false;
	
    //Save Function
	$scope.save = function () {
		$scope.saveBoolean = true;
		console.log($scope.subcontract);
		
		$scope.newSubcontract = {
				packageNo : $scope.subcontract.subcontractNumber,
				description: $scope.subcontract.description,
				subcontractorNature: $scope.subcontract.subcontractorNature,
				subcontractTerm: $scope.subcontract.subcontractTerm,
				formOfSubcontract: $scope.subcontract.formOfSubcontract,
				internalJobNo: $scope.subcontract.internalJobNo,
				retentionTerms: $scope.subcontract.retentionOption,
				paymentTerms: $scope.subcontract.paymentTerms,
				retentionAmount: $scope.subcontract.lumpSumRetention,
				paymentTermsDescription: $scope.subcontract.paymentTermsDesc,
				maxRetentionPercentage: $scope.subcontract.maxRetention,
				interimRentionPercentage: $scope.subcontract.interimRetention,
				mosRetentionPercentage: $scope.subcontract.mosRetention
		}
		console.log($scope.newSubcontract);
		
		
		subcontractService.addSubcontract($cookieStore.get("jobNo"), $scope.newSubcontract)
		.then(
				 function( data ) {
					 console.log(data);
					 //Download file				 
					 //$window.open("gammonqs/scPaymentDownload.smvc?jobNumber=13362&packageNumber=1006&paymentCertNo=5", "_blank", "");
					//$window.open("gammonqs/subcontractReportExport.rpt?company=&division=&jobNumber=13362&subcontractNumber=&subcontractorNumber=&subcontractorNature=&paymentType=&workScope=&clientNo=&includeJobCompletionDate=false&splitTerminateStatus=&month=&year=", "_blank", "");

				 });
		
		//$location.path("/subcontract-flow");
		//$uibModalInstance.close();//($scope.selected.item);


	};

	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
		
	};
	
	//Listen for location changes and call the callback
    $scope.$on('$locationChangeStart', function(event){
    	//console.log("Location changed");
    	 if(!$scope.saveBoolean){
    		 var confirmed = window.confirm("Are you sure to exit this page?");
	    	 if(confirmed)
	    		 $uibModalInstance.close();
	    	 else{
	    		 // Prevent the browser default action (Going back):
	 		    event.preventDefault();            
	    	 }
    	 }
    });
    
}]);

