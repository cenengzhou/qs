mainApp.controller('CertCtrl', ['$scope', 'mainCertService', 'colorCode', '$cookies', '$q','uiGridConstants', 'roundUtil', 'GlobalParameter', 'rootscopeService', 'modalService', 'jobService', '$uibModal',
                                function($scope, mainCertService, colorCode, $cookies, $q, uiGridConstants, roundUtil, GlobalParameter, rootscopeService, modalService, jobService, $uibModal) {
	rootscopeService.setSelectedTips('mainContractCertificateStatus');
	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");
	$cookies.put("mainCertNo", undefined);
	
	Chart.defaults.global.colours = [colorCode.green, colorCode.blue, colorCode.purple];


	var year =  new Date().getFullYear();
	$scope.selectedYear = year;
	$scope.yearList = [year];

	for(var i=0; i < 20; i++){
		var yearToAdd = year - i;		
		if(i>0 && yearToAdd > 2002){
			$scope.yearList.push(yearToAdd);
		}
	} 
	
	loadData();

	$scope.gridOptions = {
			enableFiltering: false,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			multiSelect: true,
			enableCellEditOnFocus : true,
			showColumnFooter : true,
			showGridFooter : false,
			//showColumnFooter : true,
			exporterMenuPdf: false,

			columnDefs: [
			             { field: 'mainCertNo', displayName: "Cert No.", width: 65},
			             { field: 'contractualDueDate', cellFilter:'date:"' + GlobalParameter.DATE_FORMAT +'"', width: 100},
			             { field: 'dueDate', displayName: "Forecast/Actual Due Date", cellFilter:'date:"' + GlobalParameter.DATE_FORMAT +'"', width: 100},
			             { field: 'percent', displayName: "Percent", width: 65, 
			            	 cellClass: 'text-right', cellFilter: 'number:2', 
			            	 aggregationType: uiGridConstants.aggregationTypes.sum,
			            	 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'},
		            	 { field: 'amount',  displayName: "Amount", width: 120, 
		            		 cellClass: 'text-right', cellFilter: 'number:2', 
		            		 aggregationType: uiGridConstants.aggregationTypes.sum,
		            		 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'},
	            		 { field: 'status', cellFilter: 'mapStatus', width: 65}	
	            		 ]
	};

	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;
	}

	$scope.getMainCertDashboardData = function (year){
		$scope.selectedYear = year;
		getMainCertData(year.toString().substring(2, 4));
	}
	
	$scope.openRetentionReleaseSchedule = function() {
		var modalScope = $scope.$new();
	    
        var modalInstance = $uibModal.open({
            templateUrl: 'view/main-cert/modal/retention-release-schedule.html',
            controller: 'RetentionReleaseScheduleCtrl',
            size: 'lg',
            keyboard: false,
			backdrop: 'static',
            scope: modalScope
        });
        
        modalScope.modalInstance = modalInstance;
        
        modalInstance.result.then(function (result) {
        }, null);
	}

	function loadData(){
		getJob();
		getMainCertData(year.toString().substring(2, 4));	
		getLatestMainCert();
	}

	function getMainCertData(year) {
		var contractReceivableList = mainCertService.getCertificateDashboardData($scope.jobNo, 'ContractReceivable', year);
		var ipaList = mainCertService.getCertificateDashboardData($scope.jobNo,  'IPA', year);
		var ipcList = mainCertService.getCertificateDashboardData($scope.jobNo, 'IPC', year);


		$q.all([contractReceivableList, ipaList, ipcList])
		.then(function (data){
			setDashboardData(data[0], data[1], data[2]);
		});
	}


	function setDashboardData(contractReceivableList, ipaList, ipcList) {
		$scope.contractReceivable = contractReceivableList[11];
		$scope.ipa = ipaList[11];
		$scope.ipc = ipcList[11];

		$scope.linChartParameters = {
				labels : ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
				series : ['IPA', 'IPC', 'Contract Receivable'],
				data: [ipaList, ipcList, contractReceivableList],
				options : {
					showScale : true,
					showTooltips : true,
					responsive : true,
					maintainAspectRatio : true,
					pointDot : true,
					bezierCurve : true,
					datasetFill : false,
					animation : true,
					//scaleLabel: " <%= Number(value / 1000000).toFixed(2) + ' M'%>"
					scaleLabel: " <%= Number(value / 1000000).toFixed(2) + ' M'%>"
				}
		};
	}

	function getJob(){
		jobService.getJob($scope.jobNo)
		.then(
				function( data ) {
					$scope.projectedContractValue = data.projectedContractValue;
					$scope.maxRetPercent = data.maxRetPercent;
				});
	}
	
	function getLatestMainCert(){
		mainCertService.getLatestMainCert($scope.jobNo)
		.then(
				function( data ) {
					$scope.cert = data;
					getRetentionReleaseList();
				});
	}
	
	function getRetentionReleaseList(){
		mainCertService.getRetentionReleaseList($scope.jobNo)
		.then( function (data){
			$scope.gridOptions.data= data;

			angular.forEach(data, function(value, key){
				angular.forEach(data, function(value, key){
					
					if(value.status == 'F')
						value.amount = value.forecastReleaseAmt;
					else
						value.amount = value.actualReleaseAmt;
					
					value.percent = roundUtil.round(value.amount / $scope.cert.amount_cumulativeRetention * 100, 2);

				});
				
			});
		});

	}

}])
.filter('mapStatus', function() {
	var excludeHash = {
			'A': 'Actual',
			'F': 'Forecast'
			
	};

	return function(input) {
		return excludeHash[input];
	};
});
