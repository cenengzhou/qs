mainApp.controller("SubcontractCreateModalCtrl", ['$scope', '$uibModalInstance', '$location',  function ($scope, $uibModalInstance, $location) {
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
		console.log("packageNo: " + $scope.packageNo);
		console.log("description: " + $scope.description);
		console.log("workscope: " + $scope.workscope);
		console.log("approvalSubType: " + $scope.approvalSubType);
		console.log("subcontractorNature: " + $scope.subcontractorNature);
		console.log("checkedLabour: " + $scope.checkedLabour);
		console.log("checkedPlant: " + $scope.checkedPlant);
		console.log("checkedMaterial: " + $scope.checkedMaterial);
		console.log("subcontractTerm: " + $scope.subcontractTerm);
		console.log("formOfSubcontract: " + $scope.formOfSubcontract);
		console.log("internalJobNo: " + $scope.internalJobNo);
		console.log("selectedCPF: " + $scope.selectedCPF);
		console.log("cpfPeriod: " + $scope.cpfPeriod);
		console.log("scope.saveBoolean:" + $scope.saveBoolean);
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

