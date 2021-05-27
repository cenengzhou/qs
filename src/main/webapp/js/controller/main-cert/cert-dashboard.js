mainApp.controller('CertCtrl', ['$scope', 'mainCertService', 'colorCode', '$cookies', '$q','uiGridConstants', 'roundUtil', 'GlobalParameter', 'rootscopeService', 'jobService', '$uibModal', 'dashboardHelper',
                                function($scope, mainCertService, colorCode, $cookies, $q, uiGridConstants, roundUtil, GlobalParameter, rootscopeService, jobService, $uibModal, dashboardHelper) {
	rootscopeService.setSelectedTips('mainContractCertificateStatus');
	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");
	$cookies.put("mainCertNo", undefined);
	
	Chart.defaults.global.colours = [colorCode.green, colorCode.blue, colorCode.purple];


	var year =  new Date().getFullYear();
	$scope.yearList = dashboardHelper.getDashboardDropdown();
	$scope.selectedYear = $scope.yearList[0];
	
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
		getMainCertData(year);
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
		getMainCertData($scope.selectedYear);
		getLatestMainCert();
	}

	function getMainCertData(year) {
		var getCertificateDashboardData = mainCertService.getCertificateDashboardData($scope.jobNo, year);

		$q.all([getCertificateDashboardData]).then(function (data){setDashboardData(data[0]);});
	}


	function setDashboardData(data) {
		var crData = data.find(x => x.category == 'CR').detailList
		var ipaData = data.find(x => x.category == 'IPA').detailList
		var ipcData = data.find(x => x.category == 'IPC').detailList
		var chartLabels = data[0].monthList

		$scope.contractReceivable = crData[crData.length-1];
		$scope.ipa = ipaData[ipaData.length-1];
		$scope.ipc = ipcData[ipcData.length-1];

		$scope.startYear = data[0].startYear;
		$scope.endYear = data[0].endYear;
		$scope.startMonth = chartLabels[0];
		$scope.endMonth = chartLabels[chartLabels.length-1];

		$scope.lineChartParameters = {
				labels : chartLabels,
				series : ['IPA', 'IPC', 'Contract Receivable'],
				data: [ipaData, ipcData, crData],
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
