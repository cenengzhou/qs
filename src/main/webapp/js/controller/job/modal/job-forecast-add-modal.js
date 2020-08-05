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
					//console.log(data);
						$scope.data = data;
						$scope.monthYear = $scope.data.tenderRisk.year+'-'+$scope.data.tenderRisk.month;
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
	
	$scope.update = function(){
		if (false === $('form[name="form-validate"]').parsley().validate()) {
            event.preventDefault();
            return;
        }
		
		$scope.editable = false;
		//console.log($scope.data);
		forecastService.saveForecastByJobNo ($scope.jobNo, $scope.data)
		.then(
				function( data ) {
					if(data = 'true'){
						$scope.cancel();
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Records have been updated.");
						$state.reload();
					}
				});
		
	}
	
	
	
	$scope.$watch('monthYear', function(newValue, oldValue) {
		if(oldValue != newValue){
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
	
}]);
