mainApp.controller('JobInfoCtrl', ['$scope','jobService', '$log', 
                                   function($scope, jobService, $log) {

	$(".date").datepicker({
		format: "dd-mm-yyyy",
		todayHighlight: true,
		autoclose: true,
		todayBtn: "linked"
	});
	
	$scope.plannedStartDate = "";
	$scope.actualStartDate  = "";
	$scope.plannedEndDate  = "";
	$scope.actualEndDate  = "";
	$scope.anticipatedCompletionDate= "";  
	$scope.revisedCompletionDate  = "";

	$scope.actualPCCDate="";
	$scope.actualMakingGoodDate="";
	$scope.defectLiabilityDate="";
	$scope.financialEndDate="";
	$scope.finalACSettlementDate="";

	$scope.company ="00001";
	$scope.customerNo ="286237";
	$scope.contactType ="RMC";
	$scope.division ="CVL";
	$scope.department ="CVL";
	$scope.soloJV ="S";
	$scope.completionStatus ="1";
	$scope.insuranceCAR ="";
	$scope.insuranceECI ="";
	$scope.insuranceTPL ="";
	/*$scope. ="";
	$scope. ="";
	$scope. ="";
	$scope. ="";
	$scope. ="";
	$scope. ="";
	$scope. ="";
	$scope. ="";
	$scope. ="";*/

	/*$scope.paymentTerms = {
	options: [
      "Yes",
      "No"
      ],
      selected: "No"
	};*/
	
	$scope.selectedLevy = true;
	$scope.levyPeriod = "";
	$scope.levyYear = "";
	
	//CPF Calculation
	$scope.selectedCPF = false;
	$scope.cpfPeriod = "";
	
	//Save Function
	$scope.saveJobInfo = function () {
		$log.info("levyPeriod: " + $scope.levyPeriod);
		$log.info("cpfPeriod: " + $scope.cpfPeriod);

	};
	
	$scope.saveDates = function () {
		$log.info("plannedStartDate: " + $scope.plannedStartDate);


	};
	
}]);