
mainApp.controller('EnquiryJobInfoCtrl', ['$scope' , '$rootScope', '$http', 'modalService', 'blockUI', 'jobService', 
                                  function($scope , $rootScope, $http, modalService, blockUI, jobService) {
	
	$scope.blockEnquiryJobInfo = blockUI.instances.get('blockEnquiryJobInfo');
	
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
			paginationPageSizes : [ ],
			paginationPageSize : 100,
			allowCellFocus: false,
			enableCellSelection: false,
			columnDefs: [
			             { field: 'jobNo', width: '100', displayName: "Job Number", enableCellEdit: false },
			             { field: 'description', width: '200', displayName: "Description", enableCellEdit: false },
			             { field: 'company', width: '100', displayName: "Company", enableCellEdit: false },
			             { field: 'employer', width: '100', displayName: "Employer", enableCellEdit: false },
			             { field: 'contractType', width: '100', displayName: "Contract Type", enableCellEdit: false },
			             { field: 'division', width: '100', displayName: "Division", enableCellEdit: false },
			             { field: 'department', width: '100', displayName: "Department", enableCellEdit: false },
			             { field: 'internalJob', width: '100', displayName: "Internal Job", enableCellEdit: false },
			             { field: 'soloJV', width: '100', displayName: "Solo JV", enableCellEdit: false },
			             { field: 'completionStatus', width: '100', displayName: "Completion Status", enableCellEdit: false },
			             { field: 'insuranceCAR', width: '100', displayName: "Insurance CAR", enableCellEdit: false },
			             { field: 'insuranceECI', width: '100', displayName: "Insurance ECI", enableCellEdit: false },
			             { field: 'insuranceTPL', width: '100', displayName: "Insurance TPL", enableCellEdit: false },
			             { field: 'clientContractNo', width: '100', displayName: "Client Contract No", enableCellEdit: false },
			             { field: 'tenderGP', width: '100', displayName: "Tender GP", enableCellEdit: false },
			             { field: 'parentJobNo', width: '100', displayName: "Parent No", enableCellEdit: false },
			             { field: 'jvPartnerNo', width: '100', displayName: "JV Partner No", enableCellEdit: false },
			             { field: 'jvPercentage', width: '100', displayName: "JV %", enableCellEdit: false },
			             { field: 'originalContractValue', width: '100', displayName: "Original Contract Value", enableCellEdit: false },
			             { field: 'projectedContractValue', width: '100', displayName: "Projected Contract Value", enableCellEdit: false },
			             { field: 'orginalNominatedSCContractValue', width: '100', displayName: "Orginal Nominated SC Contract Value", enableCellEdit: false },
			             { field: 'forecastEndYear', width: '100', displayName: "Forecast End Year", enableCellEdit: false },
			             { field: 'forecastEndPeriod', width: '100', displayName: "Forecast End Period", enableCellEdit: false },
			             { field: 'maxRetentionPercentage', width: '100', displayName: "Max Retention %", enableCellEdit: false },
			             { field: 'interimRetentionPercentage', width: '100', displayName: "Interim Retention %", enableCellEdit: false },
			             { field: 'mosRetentionPercentage', width: '100', displayName: "MOS Retention %", enableCellEdit: false },
			             { field: 'valueOfBSWork', width: '100', displayName: "Value of BS Work", enableCellEdit: false },
			             { field: 'grossFloorArea', width: '100', displayName: "Gross Floor Area", enableCellEdit: false },
			             { field: 'grossFloorAreaUnit', width: '100', displayName: "Gross Floor Area Unit", enableCellEdit: false },
			             { field: 'billingCurrency', width: '100', displayName: "Billing Currency", enableCellEdit: false },
			             { field: 'paymentTermsForNominatedSC', width: '100', displayName: "Payment Terms for Nominated SC", enableCellEdit: false },
			             { field: 'defectProvisionPercentage', width: '100', displayName: "Defect Provision %", enableCellEdit: false },
			             { field: 'cpfApplicable', width: '100', displayName: "CPF Applicable", enableCellEdit: false },
			             { field: 'cpfIndexName', width: '100', displayName: "CPF Applicable", enableCellEdit: false },
			             { field: 'cpfBaseYear', width: '100', displayName: "CPF Base Year", enableCellEdit: false },
			             { field: 'cpfBasePeriod', width: '100', displayName: "CPF Base Period", enableCellEdit: false },
			             { field: 'levyApplicable', width: '100', displayName: "Levy Applicable", enableCellEdit: false },
			             { field: 'levyCITAPercentage', width: '100', displayName: "Levy CITA %", enableCellEdit: false },
			             { field: 'levyPCFBPercentage', width: '100', displayName: "Levy PCFB %", enableCellEdit: false },
			             { field: 'expectedPCCDate', width: '100', displayName: "Expected PCC Date", cellFilter: 'date:"MM/dd/yyyy"', enableCellEdit: false },
			             { field: 'actualPCCDate', width: '100', displayName: "Actual PCC Date", cellFilter: 'date:"MM/dd/yyyy"', enableCellEdit: false },
			             { field: 'expectedMakingGoodDate', width: '100', displayName: "Expected making Good Date", cellFilter: 'date:"MM/dd/yyyy"', enableCellEdit: false },
			             { field: 'actualMakingGoodDate', width: '100', displayName: "Actual Making Good Date", cellFilter: 'date:"MM/dd/yyyy"', enableCellEdit: false },
			             { field: 'defectLiabilityPeriod', width: '100', displayName: "Defect Liability Period", enableCellEdit: false },
			             { field: 'defectListIssuedDate', width: '100', displayName: "DefectList Issue Date", cellFilter: 'date:"MM/dd/yyyy"', enableCellEdit: false },
			             { field: 'financialEndDate', width: '100', displayName: "Financial End Date", cellFilter: 'date:"MM/dd/yyyy"', enableCellEdit: false },
			             { field: 'dateFinalACSettlement', width: '100', displayName: "Final AC Settlement Date", cellFilter: 'date:"MM/dd/yyyy"', enableCellEdit: false },
			             { field: 'yearOfCompletion', width: '100', displayName: "Year of Completion", enableCellEdit: false },
			             { field: 'bqFinalizedFlag', width: '100', displayName: "BQ Finalized Flag", enableCellEdit: false },
			             { field: 'allowManualInputSCWorkDone', width: '100', displayName: "Allow Manual Input SC Work Done", enableCellEdit: false },
			             { field: 'legacyJob', width: '100', displayName: "Legacy Job", enableCellEdit: false },
			             { field: 'conversionStatus', width: '100', displayName: "Conversion Status", enableCellEdit: false },
			             { field: 'repackagingType', width: '100', displayName: "Repackaging Type", enableCellEdit: false },
			             { field: 'budgetPosted', width: '100', displayName: "Budget Posted", enableCellEdit: false },
			             { field: 'finQS0Review', width: '100', displayName: "Finance QS Review", enableCellEdit: false }
            			 ]
	};
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	$scope.loadGridData = function(){
		$scope.blockEnquiryJobInfo.start('Loading...')
		jobService.getJobDetailList()
		    .then(function(data) {
				if(angular.isArray(data)){
					$scope.gridOptions.data = data;
				} 
				$scope.blockEnquiryJobInfo.stop();
			}, function(data){
				$scope.blockEnquiryJobInfo.stop();
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data ); 
			});
	}
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	$scope.loadGridData();
	
}]);