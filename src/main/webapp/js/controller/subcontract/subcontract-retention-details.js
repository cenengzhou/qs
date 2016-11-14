mainApp.controller('RetentionDetailsModalCtrl', ['$scope',  'modalService', 'subcontractService',   '$cookies',  '$uibModalInstance', 'roundUtil',
                                                 function($scope, modalService, subcontractService , $cookies, $uibModalInstance, roundUtil) {

	$scope.jobNo = $cookies.get("jobNo");
	$scope.subcontractNo = $cookies.get("subcontractNo");

	getSubcontract();
	
	function getSubcontract(){
		subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					$scope.subcontract = data;
				});
	}

	
	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
	};

	//Listen for location changes and call the callback
	$scope.$on('$locationChangeStart', function(event){
		$uibModalInstance.close();
	});

}]);