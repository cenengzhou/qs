mainApp.controller("AttachmentSubcontractSelectModalCtrl", ['$rootScope', '$scope', 'subcontractDateService', '$uibModalInstance', 'modalService', '$cookies', '$state', 'roundUtil',
                                   function ($rootScope, $scope, subcontractDateService, $uibModalInstance, modalService, $cookies, $state, roundUtil) {

	$scope.jobNo = $cookies.get("jobNo");
	$scope.subcontractNo = $cookies.get("subcontractNo");
	$scope.dates = [];
	subcontractDateService.getScDateList($scope.jobNo, $scope.subcontractNo)
	.then(function(data){
		$scope.dates = data;
	});
	$scope.isUpdatable = true;
	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
	};

	//Listen for location changes and call the callback
	$scope.$on('$locationChangeStart', function(event){
		$uibModalInstance.close();
	});
	
	$rootScope.$on('upload-complete', $scope.cancel);
}]);

