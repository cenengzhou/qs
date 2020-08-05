mainApp.controller('JobForecastAddDateCtrl', ['$scope','forecastService', '$uibModalInstance', '$cookies', 'modalService', '$state', 'GlobalParameter', '$q',
                                   function($scope, forecastService, $uibModalInstance, $cookies, modalService, $state, GlobalParameter, $q) {
	$scope.GlobalParameter = GlobalParameter;
	
	$scope.jobNo = $cookies.get("jobNo");

	
	$scope.update = function(){
		if (false === $('form[name="form-validate"]').parsley().validate()) {
            event.preventDefault();
            return;
        }
		
		//console.log($scope.forecast);
		forecastService.addCriticalProgram ($scope.jobNo, $scope.forecast)
		.then(
				function( result ) {
					//console.log(result);
					if(result.data != "")
					{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', result.data);
					}
					else
					{
						$scope.cancel();
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Records have been updated.");
						$state.reload();
					}
				});
		
	}
	
	$scope.$watch('monthYear', function(newValue, oldValue) {
		if(oldValue != newValue){
			if( newValue != undefined){
				var period = $scope.monthYear.split("-");
				$scope.year = period[0];
				$scope.month = period[1];
			}
			
		}

	}, true);

	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
	};
	
}]);
