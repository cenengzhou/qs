mainApp.controller('JobForecastCtrl', ['$scope','forecastService', 'rocService', '$uibModal', '$cookies', 'modalService', '$sce','$state', 'GlobalParameter', 'rootscopeService', '$timeout', '$q',
                                   function($scope, forecastService, rocService, $uibModal, $cookies, modalService, $sce,$state, GlobalParameter, rootscopeService, $timeout, $q) {
	$scope.GlobalParameter = GlobalParameter;
	$scope.editable = false;
	$scope.rocEditable = false;
	$scope.excludeJobList = [];
	
	$scope.jobNo = $cookies.get("jobNo");

	rocService.getCutoffPeriod().then(function(data) {
		$scope.cutoffDate = data;
		$scope.cutoffDate.periodInFormat = moment(data.period, 'YYYY-MM').format('MMM, YYYY');
		$scope.monthYear = $scope.cutoffDate.period;

		if(data.excludeJobList != null){
			$scope.excludeJobList = data.excludeJobList.split(",").map(function(item) {
				return item.trim();
			});
		}
	});
	
	function getData(year, month) {
    	var turnover = forecastService.getByTypeDesc($scope.jobNo, year, month, GlobalParameter.forecast.EoJ, GlobalParameter.forecast.Turnover);
    	var cost = forecastService.getByTypeDesc($scope.jobNo, year, month, GlobalParameter.forecast.EoJ, GlobalParameter.forecast.Cost);
    	var tenderRisk = forecastService.getByTypeDesc($scope.jobNo, year, month, GlobalParameter.forecast.Contingency, GlobalParameter.forecast.TenderRisks);
    	var tenderOpps = forecastService.getByTypeDesc($scope.jobNo, year, month, GlobalParameter.forecast.Contingency, GlobalParameter.forecast.TenderOpps);
    	var others = forecastService.getByTypeDesc($scope.jobNo, year, month, GlobalParameter.forecast.Contingency, GlobalParameter.forecast.Others);
    	var risk = forecastService.getByTypeDesc($scope.jobNo, year, month, GlobalParameter.forecast.Risks, GlobalParameter.forecast.Risks);
    	var opps = forecastService.getByTypeDesc($scope.jobNo, year, month, GlobalParameter.forecast.Risks, GlobalParameter.forecast.Opps);
    	var unTurnover = forecastService.getByTypeDesc($scope.jobNo, year, month, GlobalParameter.forecast.UnsecuredEoJ, GlobalParameter.forecast.UnsecuredTurnover);
    	var unCost = forecastService.getByTypeDesc($scope.jobNo, year, month, GlobalParameter.forecast.UnsecuredEoJ, GlobalParameter.forecast.UnsecuredCost);
    	var actualTurnover = forecastService.getByTypeDesc($scope.jobNo, year, month, GlobalParameter.forecast.Actual, GlobalParameter.forecast.InternalValuation);
    	var actualCost = forecastService.getByTypeDesc($scope.jobNo, year, month, GlobalParameter.forecast.Actual, GlobalParameter.forecast.Cost);
    	var siteProfit = forecastService.getByTypeDesc($scope.jobNo, year, month, GlobalParameter.forecast.EoJ, GlobalParameter.forecast.SiteProfit);
    	var totalContingency = forecastService.getByTypeDesc($scope.jobNo, year, month, GlobalParameter.forecast.Contingency, GlobalParameter.forecast.TotalContingency);
    	
    	$q.all([turnover, cost, tenderRisk, tenderOpps, others, risk, opps, unTurnover, unCost, actualTurnover, actualCost, siteProfit, totalContingency])
    		.then(function (data){
    			$scope.data = {};
    			$scope.data.turnover = data[0];
    			$scope.data.cost = data[1];
    			$scope.data.tenderRisk = data[2];
    			$scope.data.tenderOpps = data[3];
    			$scope.data.others = data[4];
    			$scope.data.risk = data[5];
    			$scope.data.opps = data[6];
    			$scope.data.unTurnover = data[7];
    			$scope.data.unCost = data[8];
    			$scope.data.actualTurnover = data[9];
    			$scope.data.actualCost = data[10];
    			$scope.data.siteProfit = data[11];
    			$scope.data.totalContingency = data[12];
    			
    			getCriticalProgramRFList(year, month);
    			getLatestForecastPeriod();
    	});
    }
	
	
	function getCriticalProgramRFList(year, month) {
		forecastService.getCriticalProgramRFList ($scope.jobNo, year, month)
		.then(
				function( data ) {
					$scope.data.criticalProgrammeList = data.criticalProgrammeList;
				});
    	
    }
	
	function getLatestForecastPeriod(){
		forecastService.getLatestForecastPeriod ($scope.jobNo)
		.then(
				function( data ) {
					$scope.latestForecastPeriod = data;
				});
		
	}
	
	$scope.dateDiff = function (date1, date2) {
		var dateDiff = "";
		if(date1 != undefined && date2 != undefined){
			dateDiff = moment(date1).diff(moment(date2), 'days');
		}
		return dateDiff;
	}
	
	$scope.addForecast = function() {
		modalService.open('lg', 'view/job/modal/job-forecast-add.html', 'JobForecastAddCtrl');
	}
	
	$scope.addCriticalProgram = function() {
		modalService.open('lg', 'view/job/modal/job-forecast-add-date.html', 'JobForecastAddDateCtrl');
	}
	
	$scope.edit = function(){
		$scope.editable = true;
	}
	
	$scope.update = function(){
		$scope.editable = false;
		forecastService.saveByJobNo($scope.jobNo, $scope.data)
		.then(
				function( data ) {
					if(data = 'true'){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Records have been updated.");
						getData($scope.year, $scope.month);
					}
				}, function(error){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', error.data.message )
					.closed.then(function(){
						$state.reload();
					});
				});
		
	}
	
	$scope.$watch('monthYear', function(newValue, oldValue) {
		if(oldValue != newValue){
			var period = $scope.monthYear.split("-");
			$scope.year = period[0];
			$scope.month = period[1];
			getData($scope.year, $scope.month);
			
			///console.log('year: '+$scope.year + ' - month: '+$scope.month);
			
			$scope.period = moment().month($scope.month-1).format('MMM');
			$scope.lastPeriod = moment().month($scope.month-2).format('MMM');

			$scope.rocEditable = ($scope.monthYear == $scope.cutoffDate.period);

			//exclude jobs from period cutoff control from specified list
			if ($scope.excludeJobList.indexOf($scope.jobNo) > -1){
				$scope.rocEditable = true;
			}
			
			
		}

	}, true);

	
}]);
