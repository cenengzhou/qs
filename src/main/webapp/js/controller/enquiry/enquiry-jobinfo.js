
mainApp.controller('EnquiryJobInfoCtrl', ['$scope' , '$rootScope', '$http', 'modalService', 'blockUI', 'jobService', 
                                  function($scope , $rootScope, $http, modalService, blockUI, jobService) {
	
	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			enableFullRowSelection: false,
			multiSelect: true,
			showGridFooter : true,
			enableCellEditOnFocus : false,
			enableCellEdit: false,
			exporterMenuPdf: false,
			columnDefs: [
			             { field: 'jobNo', width: '100', displayName: "Job No."},
			             { field: 'description', width: '250', displayName: "Description" },
			             { field: 'company', width: '80', displayName: "Company"},
			             { field: 'employer', width: '80', displayName: "Employer"},
			             { field: 'contractType', width: '120', displayName: "Contract Type", visible: false},
			             { field: 'division', width: '80', displayName: "Division"},
			             { field: 'department', width: '100', displayName: "Department"},
			             { field: 'internalJob', width: '100', displayName: "Internal Job", visible: false},
			             { field: 'soloJV', width: '80', displayName: "Solo JV"},
			             { field: 'completionStatus', width: '150', displayName: "Completion Status"},
			             { field: 'insuranceCAR', width: '130', displayName: "Insurance CAR %", cellClass: 'text-right', cellFilter: 'number:2'},
			             { field: 'insuranceECI', width: '130', displayName: "Insurance ECI %", cellClass: 'text-right', cellFilter: 'number:2'},
			             { field: 'insuranceTPL', width: '130', displayName: "Insurance TPL %", cellClass: 'text-right', cellFilter: 'number:2'},
			             { field: 'clientContractNo', width: '200', displayName: "Client Contract No"},
			             { field: 'tenderGP', width: '120', displayName: "Tender GP", cellClass: 'text-right', cellFilter: 'number:2'},
			             { field: 'parentJobNo', width: '100', displayName: "Parent No", cellClass: 'text-right'},
			             { field: 'jvPartnerNo', width: '130', displayName: "JV Partner No", cellClass: 'text-right'},
			             { field: 'jvPercentage', width: '50', displayName: "JV %", cellClass: 'text-right', cellFilter: 'number:2'},
			             { field: 'originalContractValue', width: '180', displayName: "Original Contract Value", cellClass: 'text-right', cellFilter: 'number:2'},
			             { field: 'projectedContractValue', width: '180', displayName: "Projected Contract Value", cellClass: 'text-right', cellFilter: 'number:2'},
			             { field: 'orginalNominatedSCContractValue', width: '200', displayName: "Orginal NSC Contract Value", cellClass: 'text-right', cellFilter: 'number:2'},
			             { field: 'forecastEndYear', width: '100', displayName: "Forecast End Year", visible: false},
			             { field: 'forecastEndPeriod', width: '100', displayName: "Forecast End Period", visible: false},
			             { field: 'maxRetentionPercentage', width: '150', displayName: "Max Retention %", cellClass: 'text-right', cellFilter: 'number:2'},
			             { field: 'interimRetentionPercentage', width: '150', displayName: "Interim Retention %", cellClass: 'text-right', cellFilter: 'number:2'},
			             { field: 'mosRetentionPercentage', width: '150', displayName: "MOS Retention %", cellClass: 'text-right', cellFilter: 'number:2'},
			             { field: 'valueOfBSWork', width: '100', displayName: "Value of BS Work", visible: false},
			             { field: 'grossFloorArea', width: '100', displayName: "Gross Floor Area", visible: false},
			             { field: 'grossFloorAreaUnit', width: '100', displayName: "Gross Floor Area Unit", visible: false},
			             { field: 'billingCurrency', width: '120', displayName: "Billing Currency"},
			             { field: 'paymentTermsForNominatedSC', width: '150', displayName: "Payment Terms for NSC"},
			             { field: 'defectProvisionPercentage', width: '150', displayName: "Defect Provision %", cellClass: 'text-right', cellFilter: 'number:2'},
			             { field: 'cpfApplicable', width: '130', displayName: "CPF Applicable"},
			             { field: 'cpfIndexName', width: '150', displayName: "CPF Index Name", visible: false},
			             { field: 'cpfBaseYear', width: '120', displayName: "CPF Base Year", visible: false},
			             { field: 'cpfBasePeriod', width: '100', displayName: "CPF Base Period", visible: false},
			             { field: 'levyApplicable', width: '130', displayName: "Levy Applicable"},
			             { field: 'levyCITAPercentage', width: '100', displayName: "Levy CITA %", cellFilter: 'number:2'},
			             { field: 'levyPCFBPercentage', width: '120', displayName: "Levy PCFB %", cellFilter: 'number:2'},
			             { field: 'expectedPCCDate', width: '150', displayName: "Expected PCC Date", cellFilter: 'date:"yyyy/MM/dd"', cellClass: 'text-right'},
			             { field: 'actualPCCDate', width: '150', displayName: "Actual PCC Date", cellFilter: 'date:"yyyy/MM/dd"', cellClass: 'text-right'},
			             { field: 'expectedMakingGoodDate', width: '200', displayName: "Expected making Good Date", cellFilter: 'date:"yyyy/MM/dd"', cellClass: 'text-right'},
			             { field: 'actualMakingGoodDate', width: '180', displayName: "Actual Making Good Date", cellFilter: 'date:"yyyy/MM/dd"', cellClass: 'text-right'},
			             { field: 'defectLiabilityPeriod', width: '180', displayName: "Defect Liability Period"},
			             { field: 'defectListIssuedDate', width: '150', displayName: "DefectList Issue Date", cellFilter: 'date:"yyyy/MM/dd"', cellClass: 'text-right'},
			             { field: 'financialEndDate', width: '150', displayName: "Financial End Date", cellFilter: 'date:"yyyy/MM/dd"', cellClass: 'text-right'},
			             { field: 'dateFinalACSettlement', width: '180', displayName: "Final AC Settlement Date", cellFilter: 'date:"yyyy/MM/dd"', cellClass: 'text-right'},
			             { field: 'yearOfCompletion', width: '150', displayName: "Year of Completion", cellClass: 'text-right'},
//			             { field: 'bqFinalizedFlag', width: '100', displayName: "BQ Finalized Flag", visible: false},
//			             { field: 'allowManualInputSCWorkDone', width: '100', displayName: "Allow Manual Input SC Work Done", visible: false},
//			             { field: 'legacyJob', width: '100', displayName: "Legacy Job", visible: false},
//			             { field: 'conversionStatus', width: '100', displayName: "Conversion Status", visible: false},
			             { field: 'repackagingType', width: '150', displayName: "Repackaging Type"},
			             { field: 'budgetPosted', width: '100', displayName: "Budget Posted", visible: false},
			             { field: 'finQS0Review', width: '100', displayName: "Finance QS Review", visible: false}
            			 ]
	};
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	$scope.loadGridData = function(){
		jobService.getJobDetailList()
		    .then(function(data) {
				if(angular.isArray(data)){
					$scope.gridOptions.data = data;
				} 
			}, function(data){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data ); 
			});
	}
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	$scope.loadGridData();
	
}]);