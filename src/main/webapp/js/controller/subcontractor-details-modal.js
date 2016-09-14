mainApp.controller('SubcontractorDetailsModalCtrl', ['$scope', '$uibModalInstance', '$interval', 'modalService', 'modalStatus', 'modalParam', 'masterListService', 'subcontractorService', 'GlobalParameter', 'GlobalHelper', 'roundUtil',
                                            function ($scope, $uibModalInstance, $interval, modalService, modalStatus, modalParam, masterListService, subcontractorService, GlobalParameter, GlobalHelper, roundUtil) {
	$scope.status = modalStatus;

	$scope.vendorNo = modalParam;

	$scope.cancel = function () {
		$uibModalInstance.close();
	};

	//Listen for location changes and call the callback
	$scope.$on('$locationChangeStart', function(event){
		$uibModalInstance.close();
	});	
	
	$scope.tab = 1;
	$scope.selectTab = function(setTab){
		$scope.tab = setTab;
	};
	$scope.isSelected = function(checkTab){
		return $scope.tab === checkTab;
	};

	$scope.awardedGridOptions = {};
	$scope.tenderAnalysisGridOptions = {};
	$scope.workscopeGridOptions = {};
	
	$scope.loadVendor = function(){
		masterListService.searchVendorAddressDetails($scope.vendorNo)
		.then(function(data){
			if(angular.isObject(data)){
				$scope.vendor = data;
				loadStatistics();
				loadSubcontract();
				loadTenderAnalysis();
				loadWorkScope();
			} else {
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'No subcontractor found:' + $scope.vendorNo);
			}
			
		}, function(error){
			console.log(error);
		});
	}
	$scope.loadVendor();
	$scope.startYear = '01Jan' + (new Date().getFullYear() - 1);
	$scope.addressLineField = function(address){
		var result = {};
		angular.forEach(address, function(value, key) {
	        if (key.startsWith('addressLine')) {
	        	result[key] = value;
	        }
	    });
		return result;
	}
	
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
			allowCellFocus: false,
			enableCellSelection: false,
			exporterMenuPdf: false
	};
	
	$scope.awardedGridOptions = angular.copy($scope.gridOptions);
	$scope.tenderAnalysisGridOptions = angular.copy($scope.gridOptions);
	$scope.workscopeGridOptions = angular.copy($scope.gridOptions);

	$scope.awardedColumnDefs = [
	             { field: 'jobInfo.jobNo', width: 60, displayName: 'Job No', enableCellEdit: false },
	             { field: 'jobInfo.description', width: 180, displayName: 'Job Description', enableCellEdit: false },
	             { field: 'jobInfo.division', width: 80, displayName: 'Job Division', enableCellEdit: false },
	             { field: 'packageNo', width: 65, displayName: 'Package No', enableCellEdit: false },
	             { field: 'description', width: 180, displayName: 'Package Description', enableCellEdit: false },
	             { field: 'paymentTerms', width: 85, displayName: 'Payment Terms', enableCellEdit: false,
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 if(row.entity.paymentTerms === 'QS0'){
	            			 return 'text-warning';
	            		 }
	            	 }
	             },
	             { field: 'paymentStatusText', width: 85, displayName: 'Payment Type', enableCellEdit: false,
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 if(row.entity.paymentStatus === 'F'){
	            			 return 'text-success';
	            		 }
	            	 }
	             },
	             { field: 'remeasuredSubcontractSum', width: 110, cellFilter: 'number:2', displayName: 'Remeasured SC Sum', enableCellEdit: false,
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 return GlobalHelper.numberClass(row.entity.remeasuredSubcontractSum);
	            	 }
	             },
	             { field: 'approvedVOAmount', width: 110, cellFilter: 'number:2', displayName: 'Approved VO Amount', enableCellEdit: false,
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 return GlobalHelper.numberClass(row.entity.approvedVOAmount);
	            	 }
	             },
	             { field: 'revisedSCSum', width: 120, cellFilter: 'number:2', displayName: 'Revised SC Sum (Remeasured SC Sum + Approved VO Amount)', enableCellEdit: false, 
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 return GlobalHelper.numberClass(row.entity.revisedSCSum);
	            	 }
	             },	             
	             { field: 'balanceToComplete', width: 110, cellFilter: 'number:2', displayName: 'Balance To Complete (Revised SC Sum - Total CumWork Done Amount)', enableCellEdit: false,
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 return GlobalHelper.numberClass(row.entity.balanceToComplete);
	            	 }
	             }
   			 ];

	$scope.tenderAnalysisColumnDefs = [
	      	           	             { field: 'jobNo', width: 80, displayName: "Job No", enableCellEdit: false },
	    	           	             { field: 'division', width: 80, displayName: "Job Division", enableCellEdit: false },
	    	           	             { field: 'packageNo', width: 80, displayName: "Package No", enableCellEdit: false },
	    	           	             { field: 'budgetAmount', width: 120, displayName: "Budget Amount", cellFilter: 'number:2', enableCellEdit: false,
	    	        	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	    	        	            		 return GlobalHelper.numberClass(row.entity.budgetAmount);
	    	        	            	 }
	    	           	             },
	    	           	             { field: 'quotedAmount', width: 120,  displayName: "Quoted. Amount", cellFilter: 'number:2', enableCellEdit: false,
	    	        	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	    	        	            		 return GlobalHelper.numberClass(row.entity.quotedAmount);
	    	        	            	 }
	    	           	             },
	    	           	             { field: 'status', width: 110, displayName: "Tender Status", enableCellEdit: false,
	    	        	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	    	        	            		 if(row.entity.status === 'AWD'){
	    	        	            			 return 'text-success';
	    	        	            		 }
	    	        	            	 }
	    	           	             },
	    	           	             { field: 'awardStatusText', width: 200, displayName: "Tender Status Description", enableCellEdit: false }
	               			 ];

	$scope.workscopeColumnDefs = [
	           	             { field: 'workScopeCode', displayName: "Work Scope", enableCellEdit: false },
	           	             { field: 'description', displayName: "Description", enableCellEdit: false },
	           	             { field: 'isApprovedText', displayName: "Status", enableCellEdit: false }
	               			 ];

	$scope.awardedGridOptions.columnDefs = $scope.awardedColumnDefs;
	$scope.tenderAnalysisGridOptions.columnDefs = $scope.tenderAnalysisColumnDefs;
	$scope.workscopeGridOptions.columnDefs = $scope.workscopeColumnDefs;
	
	$scope.awardedGridOptions.onRegisterApi = function (gridApi) {
		  $scope.awardedGridApi = gridApi;
		     $interval( function() {
		    	 $scope.awardedGridApi.grid.refresh();
		       }, 500, 20);
	}

	$scope.tenderAnalysisGridOptions.onRegisterApi = function (gridApi) {
		  $scope.tenderAnalysisGridApi = gridApi;
		     $interval( function() {
		    	 $scope.tenderAnalysisGridApi.grid.refresh();
		       }, 500, 20);
	}

	$scope.workscopeGridOptions.onRegisterApi = function (gridApi) {
		  $scope.workscopeGridApi = gridApi;
		     $interval( function() {
		    	 $scope.workscopeGridApi.grid.refresh();
		       }, 500, 20);
	}

	function loadStatistics(){
		subcontractorService.obtainSubconctractorStatistics($scope.vendorNo)
		.then(function(data){
			if(angular.isObject(data)){
				$scope.statistics = data;
			}
		});
	}
	
	function loadSubcontract(){
		subcontractorService.obtainPackagesByVendorNo($scope.vendorNo)
		.then(function(data){
			if(angular.isObject(data)){
				$scope.subcontracts = data;
				$scope.subcontracts.forEach(function(subcontract){
					subcontract.revisedSCSum = roundUtil.round(subcontract.remeasuredSubcontractSum + subcontract.approvedVOAmount, 2);
					subcontract.balanceToComplete = roundUtil.round(subcontract.revisedSCSum - subcontract.totalCumWorkDoneAmount, 2);
					subcontract.paymentStatusText = GlobalParameter.getValueById(GlobalParameter.intermFinalPayment, subcontract.paymentStatus); 
				})
				$scope.awardedGridOptions.data = $scope.subcontracts;
			}
		})
	}
	
	function loadTenderAnalysis(){
		subcontractorService.obtainTenderAnalysisWrapperByVendorNo($scope.vendorNo)
		.then(function(data){
			if(angular.isObject(data)){
				$scope.tenderAnalysis = data;
				$scope.tenderAnalysis.forEach(function(tenderAnalysis){
					tenderAnalysis.awardStatusText = GlobalParameter.getValueById(GlobalParameter.awardStatus, tenderAnalysis.status != undefined ? tenderAnalysis.status : ' ');
				});				
				$scope.tenderAnalysisGridOptions.data = $scope.tenderAnalysis;
			}
		})
	}
	
	function loadWorkScope(){
		masterListService.getSubcontractorWorkScope($scope.vendorNo)
		.then(function(data){
			if(angular.isObject(data)){
				$scope.workScopes = data;
				$scope.workScopes.forEach(function(workScope){
					workScope.isApprovedText = GlobalParameter.getValueById(GlobalParameter.ApprovalStatus, workScope.isApproved.trim());
				});
				$scope.workscopeGridOptions.data = $scope.workScopes;
			}
		})
	}
}]);

