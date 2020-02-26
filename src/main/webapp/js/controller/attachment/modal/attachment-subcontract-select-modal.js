mainApp.controller("AttachmentSubcontractSelectModalCtrl", ['$rootScope', '$scope', '$uibModalInstance', 'subcontractService', 'modalService', '$cookies', '$state', 'roundUtil',
                                   function ($rootScope, $scope, $uibModalInstance, subcontractService, modalService, $cookies, $state, roundUtil) {

	$scope.dates = subcontractService.dates;
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

