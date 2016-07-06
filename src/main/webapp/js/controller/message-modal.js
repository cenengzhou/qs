mainApp.controller("MessageModalCtrl", ['$scope', '$uibModalInstance', 'modalStatus', 'modalParam',  function ($scope, $uibModalInstance, modalStatus, modalParam) {
	$scope.header = "";
	$scope.status = modalStatus;

	$scope.message = modalParam;

	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
	};


	//Listen for location changes and call the callback
	$scope.$on('$locationChangeStart', function(event){
		$uibModalInstance.close();
	});	

}]);

