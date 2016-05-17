mainApp.controller("SubcontractCreateModalCtrl", ['$scope', '$uibModalInstance', '$location', '$log', function ($scope, $uibModalInstance, $location, $log) {
	$scope.packageNo = "";
	$scope.description = "";
	$scope.workscope = "";
	$scope.approvalSubType = "";
	
	//Subcontractor Nature
	$scope.subcontractorNature = "DSC";
	
	//Subcontract Type
	$scope.checkedLabour = false;
    $scope.checkedPlant = false;
    $scope.checkedMaterial = false;
    
    //Subcontract Term
    $scope.subcontractTerm = "Re-measurement";

    //Form of Subcontract
    $scope.formOfSubcontract = "Major";
	$scope.internalJobNo = "";
    
	//Rentention
	 $scope.retentionOption = "Percentage";
	
	
	//Payment Terms
	 $scope.paymentTerms = "QS2";
	 
	//CPF Calculation
	$scope.selectedCPF = false;
	$scope.cpfPeriod = "";
	

	$scope.percentageOption= "Revised";
	
	$scope.lumpSumRetention = 5000;
	$scope.maxRetention = 5;
	$scope.interimRetention = 10;
	$scope.mosRetention = 10;
	
	$scope.saveBoolean = false;
	
    //Save Function
	$scope.save = function () {
		$scope.saveBoolean = true;
		$log.info("packageNo: " + $scope.packageNo);
		$log.info("description: " + $scope.description);
		$log.info("workscope: " + $scope.workscope);
		$log.info("approvalSubType: " + $scope.approvalSubType);
		$log.info("subcontractorNature: " + $scope.subcontractorNature);
		$log.info("checkedLabour: " + $scope.checkedLabour);
		$log.info("checkedPlant: " + $scope.checkedPlant);
		$log.info("checkedMaterial: " + $scope.checkedMaterial);
		$log.info("subcontractTerm: " + $scope.subcontractTerm);
		$log.info("formOfSubcontract: " + $scope.formOfSubcontract);
		$log.info("internalJobNo: " + $scope.internalJobNo);
		$log.info("selectedCPF: " + $scope.selectedCPF);
		$log.info("cpfPeriod: " + $scope.cpfPeriod);
		$log.info("scope.saveBoolean:" + $scope.saveBoolean);
		$location.path("/subcontract-flow");
		$uibModalInstance.close();//($scope.selected.item);


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

