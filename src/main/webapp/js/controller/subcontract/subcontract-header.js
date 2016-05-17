mainApp.controller("SubcontractHeaderCtrl", ['$scope', '$log', function ($scope, $log) {
	/*	$http.get("json/questions.json").success(function (data) {
    //all questions
    $scope.questions = data;

    //filter for getting answers / question
    $scope.ids = function (question) {
        return question.id == number;
    }

    $scope.buttonText = "Next question";

    $scope.next = function () {
        if (!(number == (data.length))) {
            if(number+1==(data.length)){
                $scope.buttonText = "Get results";
            }
            number++;
        }
    }*/
	
	
	
	//Subcontractor Nature
	$scope.subcontractorNature = "DSC";
	/*$scope.subcontractorNature = {
		options: [
	      "DSC",
	      "NDSC",
	      "NSC"
	      ],
	      selected: "DSC"
	};*/
		
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
	/*$scope.paymentTerms = {
			options: [
	          "Qs0",
	          "QS1",
	          "QS2",
	          "QS3",
	          "QS4",
	          "QS5",
	          "QS6",
	          "QS7",
	          ],
	          selected: "QS2"
		};*/
	
	 
	//CPF Calculation
	$scope.selectedCPF = false;
	$scope.cpfPeriod = "";
	

	$scope.percentageOption= "Revised";
	
	$scope.lumpSumRetention = 5000;
	$scope.maxRetention = 5;
	$scope.interimRetention = 10;
	$scope.mosRetention = 10;
	
	
}]);

