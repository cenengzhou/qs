mainApp.controller('RetentionReleaseModalCtrl', ['$scope',  'modalService', 'jobService',  'mainCertService', '$cookies', 'uiGridConstants', '$uibModalInstance', 'roundUtil', 'GlobalParameter', '$state',
                                                 function($scope, modalService, jobService, mainCertService, $cookies, uiGridConstants, $uibModalInstance, roundUtil, GlobalParameter, $state) {

	$scope.jobNo = $cookies.get("jobNo");
	$scope.mainCertNo = $cookies.get("mainCertNo");

	loadData();

	$scope.gridOptions = {
			enableFiltering: false,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			multiSelect: false,
			enableCellEditOnFocus : true,
			showColumnFooter : true,
			showGridFooter : false,
			//showColumnFooter : true,
			exporterMenuPdf: false,

			rowEditWaitInterval :-1,

			columnDefs: [
			             { field: 'id', visible: false},
			             { field: 'jobNo', visible: false},
			             { field: 'mainCertNo', displayName: "Cert No.", enableCellEdit: false},
			             { field: 'contractualDueDate', type: 'date', cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"'},
			             { field: 'dueDate', displayName: "Forecast/Actual Due Date", type: 'date', cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"'},
			             { field: 'percent', displayName: "Release Percentage", 
			            	 cellClass: 'text-right', cellFilter: 'number:2', 
			            	 aggregationType: uiGridConstants.aggregationTypes.sum,
			            	 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'},
		            	 { field: 'amount',  displayName: "Amount",
		            		 cellClass: 'text-right', cellFilter: 'number:2', 
		            		 aggregationType: uiGridConstants.aggregationTypes.sum,
		            		 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'},
	            		 { field: 'status', cellFilter: 'mapStatus', enableCellEdit: false}	
	            		 ]
	};

	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;
		
		
		gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			if(rowEntity.status == "A"){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Actual Retention Release cannot be edited.");
			}
		});
		
		
		gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			if(rowEntity.status == "F"){
				if(colDef.name == "amount"){
					if(newValue.length ==0)
						rowEntity.amount = oldValue;
					else{
						rowEntity.amount = roundUtil.round(newValue, 2);
						rowEntity.percent = roundUtil.round(rowEntity.amount / $scope.cumRetentionAmount * 100, 2);
					}
				}
				else if(colDef.name == "percent"){
					if(newValue.length ==0)
						rowEntity.percent = oldValue;
					else{
						rowEntity.percent = roundUtil.round(newValue, 2);
						rowEntity.amount = roundUtil.round($scope.cumRetentionAmount * rowEntity.percent / 100, 2);
					}
				}
				else if(colDef.name == "contractualDueDate"){
					if(newValue == null)
						rowEntity.contractualDueDate = oldValue;
				}
				else if(colDef.name == "dueDate"){
					if(newValue == null)
						rowEntity.dueDate = oldValue;
				}
			}
		});
		
		gridApi.selection.on.rowSelectionChanged($scope,function(row){
			$scope.removeRowIndex = $scope.gridOptions.data.indexOf(row.entity);
		});
	}


	$scope.addData = function() {
		$scope.gridOptions.data.push({
			"id":0,
			"jobNo": $scope.jobNo,
			"mainCertNo": "",
			"contractualDueDate": new Date(),
			"dueDate": new Date(),
			"percent": 0,
			"amount": 0,
			"status": "F",
		});
	};

	$scope.deleteData = function() {
		var selectedRows = $scope.gridApi.selection.getSelectedRows();
		if(selectedRows.length == 0){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select row to delete.");
			return;
		}else{
			if(selectedRows[0]['status'] != 'F'){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Only forecast retention release can be deleted.");
				return;			
			}
			
			selectedRows[0]['status'] = 'D';
			$scope.gridApi.rowEdit.setRowsDirty(selectedRows);
			$scope.gridOptions.data = $scope.gridOptions.data.filter(function(row){
				return row.status != 'D';
			});
		}

	};

	$scope.save = function() {
		var totalRR = roundUtil.round($scope.gridApi.grid.columns[7].getAggregationValue(), 2);
		if(totalRR != $scope.cumRetentionAmount){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 
					"Total Release Amount must be equal to Cumulative Retention Amount. \r\n"
					+"Total Release Amount: "+totalRR+" \r\n"
					+"Cumulative Retention Amount: "+$scope.cumRetentionAmount );
		}else{
			var gridRows = $scope.gridApi.rowEdit.getDirtyRows();
			var dataRows = gridRows.map( function( gridRow ) { 
					if(gridRow.id!=0 && gridRow.status != 'D'){
						return gridRow.entity;
					}
				});
			
			if(dataRows.length==0){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "No record has been modified");
				return;
			}
			
			angular.forEach(dataRows, function(value, key){
				if(value.status == 'F')
					value.forecastReleaseAmt = value.amount;
				else
					value.actualReleaseAmt = value.amount;
			});
			updateRetentionRelease(dataRows);
		}
	};
	
	function loadData(){
		getJob();
		if($scope.mainCertNo == null || $scope.mainCertNo == undefined || $scope.mainCertNo.length==0){
			getLatestMainCert();
		}else
			getCertificate();
		
	}

	function getCalculatedRetentionRelease(certNo){
		mainCertService.getCalculatedRetentionRelease($scope.jobNo, certNo)
		.then(
				function( data ) {
					$scope.gridOptions.data = data;

					angular.forEach(data, function(value, key){
						if(value.contractualDueDate != null)
							value.contractualDueDate = new Date(value.contractualDueDate);
						if(value.dueDate != null)
							value.dueDate = new Date(value.dueDate);
						
						
						if(value.status == 'F')
							value.amount = value.forecastReleaseAmt;
						else
							value.amount = value.actualReleaseAmt;
						
						value.percent = roundUtil.round(value.amount / $scope.cumRetentionAmount * 100, 2);

					});
				});
	}

	function getJob(){
		jobService.getJob($scope.jobNo)
		.then(
				function( data ) {
					$scope.projectedContractValue = data.projectedContractValue;
					$scope.maxRetPercent = data.maxRetPercent;
				});
	}

	function getCertificate(){
		mainCertService.getCertificate($scope.jobNo, $scope.mainCertNo)
		.then(
				function( data ) {
					$scope.cumRetentionAmount = data.amount_cumulativeRetention;
//						(data.certifiedRetentionforNSCNDSC==null?0:data.certifiedRetentionforNSCNDSC)
//						+ (data.certifiedMainContractorRetention==null?0:data.certifiedMainContractorRetention)
//						+ (data.certifiedMOSRetention==null?0:data.certifiedMOSRetention);
					
					getCalculatedRetentionRelease($scope.mainCertNo);
				});
	}

	function getLatestMainCert() {
		mainCertService.getLatestMainCert($scope.jobNo)
		.then(
				function( data ) {
					if(data){
						$scope.cumRetentionAmount = data.amount_cumulativeRetention;
//							(data.certifiedRetentionforNSCNDSC==null?0:data.certifiedRetentionforNSCNDSC)
//							+ (data.certifiedMainContractorRetention==null?0:data.certifiedMainContractorRetention)
//							+ (data.certifiedMOSRetention==null?0:data.certifiedMOSRetention);
						
						getCalculatedRetentionRelease(data.certificateNumber);
					}

				});
	}
	
	function updateRetentionRelease(rrList){
		mainCertService.updateRetentionRelease($scope.jobNo, rrList)
		.then(
				function( data ) {
					if(data.status == 'FAIL'){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', data.message);
						return;
					}else{
						$uibModalInstance.close();
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', 'Retention Release Schedule has been updated.');
						$state.reload();
					}
				});
	}
	
	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
	};

	//Listen for location changes and call the callback
	$scope.$on('$locationChangeStart', function(event){
		$uibModalInstance.close();
	});

}])
.filter('mapStatus', function() {
	var excludeHash = {
			'F': 'Forecast',
			'A': 'Actual'
	};

	return function(input) {
		return excludeHash[input];
	};
});