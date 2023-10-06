mainApp.controller('JobForecastAddCtrl', ['$scope','forecastService', '$uibModalInstance', '$cookies', 'modalService', '$state', 'GlobalParameter', '$q',
                                   function($scope, forecastService, $uibModalInstance, $cookies, modalService, $state, GlobalParameter, $q) {
	$scope.GlobalParameter = GlobalParameter;
	$scope.editable = false;
	
	$scope.jobNo = $cookies.get("jobNo");
	
	getLatestForecast();
	
	function getLatestForecast() {
		forecastService.getForecastByJobNo ($scope.jobNo, 0, 0)
		.then(
				function( data ) {
					if(data != null && data.tenderRisk != null){
						$scope.data = data;
						$scope.monthYear = $scope.data.tenderRisk.year+'-'+$scope.data.tenderRisk.month.toString().padStart(2, '0');
						//console.log('$scope.monthYear: '+$scope.monthYear);
						
						
						if($scope.data.tenderRisk.forecastPeriod != null)
							$scope.forecastPeriod =  $scope.data.tenderRisk.forecastPeriod;
						else
							$scope.forecastPeriod =  GlobalParameter.ForecastStatus[0].value;

					}
				});
		
    	
    }
	
	function getData(year, month) {
		forecastService.getForecastByJobNo ($scope.jobNo, year, month)
		.then(
				function( data ) {
					//console.log(data);
					$scope.data = data;
				});
    	
    }
	
	
	
	
	$scope.edit = function(){
		$scope.editable = true;
	}
	
	$scope.delete = function(jobNo, year, month){
		var param = {noJob: jobNo, year: year, month: month, forecastFlag: 'F'};
		forecastService.deleteBy('YEAR_MONTH_FLAG', param)
		.then(function(){
			Success();
		}, function(error){
			Error(error);
		});
	}

	$scope.update = function(){
		if (false === $('form[name="form-validate"]').parsley().validate()) {
            event.preventDefault();
            return;
        }
		
		$scope.editable = false;
		forecastService.saveForecastByJobNo ($scope.jobNo, $scope.data)
		.then(
			function( data ) {
				if(data = 'true'){
					Success();
				}
			}, function(error){
				Error(error);
			}
		);
		
	}
	
	$scope.$watch('monthYear', function(newValue, oldValue) {
		if( oldValue != newValue){
			
			var period = $scope.monthYear.split("-");
			$scope.year = period[0];
			$scope.month = period[1];
			getData($scope.year, $scope.month);
			
			//console.log('year: '+$scope.year + ' - month: '+$scope.month);
			
			$scope.period = moment().month($scope.month-1).format('MMM');
			$scope.lastPeriod = moment().month($scope.month-2).format('MMM');
			
			//console.log('period:'+ $scope.period);
			//console.log('prePeriod: '+$scope.lastPeriod);
		}

	}, true);

	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
	};
	
	function Success(){
		$scope.cancel();
		modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Records have been updated.");
		$state.reload();
	}

	function Error(error){
		modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', error.data.message )
		.closed.then(function(){
			$state.reload();
		});
	}

}]);
