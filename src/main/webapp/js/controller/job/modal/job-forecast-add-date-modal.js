mainApp.controller('JobForecastAddDateCtrl', ['$scope','forecastService', '$uibModalInstance', '$cookies', 'modalService', 'confirmService', '$state', 'GlobalParameter', 'GlobalMessage', '$q',
                                   function($scope, forecastService, $uibModalInstance, $cookies, modalService, confirmService, $state, GlobalParameter, GlobalMessage, $q) {
	$scope.GlobalParameter = GlobalParameter;
	
	$scope.jobNo = $cookies.get("jobNo");

	getLatestCriticalProgramList();
	
	function getLatestCriticalProgramList(){
		forecastService.getLatestCriticalProgramList ($scope.jobNo)
		.then(
				function( data ) {
					//console.log(data);
					$scope.cpList = data;
				});
		
		
	}
	
	$scope.delete = function(jobNo, forecastDesc){
		confirmService.show({}, {bodyText:GlobalMessage.deleteForecastCP})
		.then(function(response){
			if(response === 'Yes'){
				var param = {noJob: jobNo, forecastDesc: forecastDesc}
				forecastService.deleteBy('FORECAST_DESC', param)
				.then(function(){
					Success();
				}, function(error){
					Error(error);
				})
			}
		});
		
	}

	$scope.update = function (program){
		if (program.forecastDesc == undefined) {
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please input description.");
            return;
        }
		
		
		forecastService.updateCriticalProgramDesc (program)
		.then(
				function( result ) {
					
					if(result.data != "")
					{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', result.data);
					}
					else
					{
						Success();	
					}
				}, function(error){
					Error(error);
			});
	}
	
	$scope.add = function(){
		if ($scope.forecast.forecastDesc == undefined) {
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please input description.");
            return;
        }
		
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
						Success();
					}
				}, function(error){
					Error(error)
			});
		
	}
	
	

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
