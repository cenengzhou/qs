mainApp.controller('JobInfoCtrl', ['$scope','jobService', '$cookieStore',
                                   function($scope, jobService, $cookieStore) {

	$(".date").datepicker({
		format: "dd-mm-yyyy",
		//todayHighlight: true,
		autoclose: true,
		//todayBtn: "linked"
	});

	loadJobInfo();

	function loadJobInfo() {
		jobService.getJob($cookieStore.get("jobNo"))
		.then(
				function( data ) {
					$scope.plannedStartDate = "";
					$scope.actualStartDate  = data.actualPCCDate;
					$scope.plannedEndDate  = "";
					$scope.actualEndDate  = "";
					$scope.anticipatedCompletionDate= "";  
					$scope.revisedCompletionDate  = "";

					$scope.actualPCCDate="";
					$scope.actualMakingGoodDate="";
					$scope.defectLiabilityDate="";
					$scope.financialEndDate="";
					$scope.finalACSettlementDate="";

					$scope.company = data.company;
					$scope.customerNo = data.employer;
					$scope.contactType =data.contractType;
					$scope.division = data.division;
					$scope.department = data.department;
					$scope.soloJV = data.soloJV;
					$scope.completionStatus = data.completionStatus;
					$scope.insuranceCAR = data.insuranceCAR;
					$scope.insuranceECI = data.insuranceECI;
					$scope.insuranceTPL = data.insuranceTPL;

					$scope.originalContractValue = data.originalContractValue;
					$scope.projectedContractValue = data.projectedContractValue;
					$scope.orginalNominatedSCContractValue = data.orginalNominatedSCContractValue;
					$scope.tenderGP = data.tenderGP;
					$scope.clientContractNo = data.clientContractNo;
					$scope.parentJobNo = data.parentJobNo;
					$scope.jvPartnerNo= data.jvPartnerNo;
					$scope.jvPercentage= data.jvPercentage;

					$scope.maxRetPercent = data.maxRetPercent;
					$scope.interimRetPercent = data.interimRetPercent;
					$scope.mosRetPercent = data.mosRetPercent;

					$scope.valueOfBSWork = data.valueOfBSWork;
					$scope.grossFloorArea = data.grossFloorArea;
					$scope.grossFloorAreaUnit = data.grossFloorAreaUnit;
					$scope.billingCurrency = data.billingCurrency;
					$scope.paymentTermsForNominatedSC = data.paymentTermsForNominatedSC;
					$scope.defectProvisionPercentage = data.defectProvisionPercentage;
					$scope.forecastEndYear = data.forecastEndYear;
					$scope.forecastEndPeriod = data.forecastEndPeriod;

					$scope.levyApplicable = false;
					if(data.levyApplicable == "1")
						$scope.levyApplicable = true;

					$scope.levyCITAPercentage = data.levyCITAPercentage;
					$scope.levyPCFBPercentage = data.levyPCFBPercentage;

					//CPF Calculation
					$scope.cpfApplicable = false;
					if(data.cpfApplicable == "1")
						$scope.cpfApplicable = true;

					$scope.cpfBaseYear = data.cpfBaseYear;
					$scope.cpfBasePeriod = data.cpfBasePeriod;
					$scope.cpfIndexName = data.cpfIndexName;

					console.log("data.actualPCCDate: "+data.actualPCCDate);
					$scope.actualPCCDate = data.actualPCCDate;
					//$scope.actualPCCDate = new Date("12-02-2012");
					$scope.actualMakingGoodDate = data.actualMakingGoodDate;
					$scope.dateFinalACSettlement = data.dateFinalACSettlement;
					$scope.defectListIssuedDate = data.defectListIssuedDate;
					$scope.financialEndDate = data.financialEndDate;

				});
	}



	/*$scope.paymentTerms = {
	options: [
      "Yes",
      "No"
      ],
      selected: "No"
	};*/



	//Save Function
	$scope.saveJobInfo = function () {
		console.log("levyCITAPercentage: " + $scope.levyCITAPercentage);
		console.log("cpfPeriod: " + $scope.cpfPeriod);

	};

	$scope.saveDates = function () {
		console.log("plannedStartDate: " + $scope.plannedStartDate);


	};

}]);
/*.filter("jsonDate", function() {
    var re = /\\\/Date\(([0-9]*)\)\\\//;
    return function(x) {
        var m = x.match(re);
        if( m ) return new Date(parseInt(m[1]));
        else return null;
    };
});*/
