mainApp.controller('JobForecastAddDateCtrl', ['$scope','forecastService', '$uibModalInstance', '$cookies', 'modalService', '$state', 'GlobalParameter', '$q',
                                   function($scope, forecastService, $uibModalInstance, $cookies, modalService, $state, GlobalParameter, $q) {
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
						$scope.cancel();
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Records have been updated.");
						$state.reload();	
					}
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
						$scope.cancel();
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Records have been updated.");
						$state.reload();
					}
				});
		
	}
	
	

	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
	};
	
}]);
