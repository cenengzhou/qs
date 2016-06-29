mainApp.controller("MessageModalCtrl", ['$scope', '$uibModalInstance', 'modalStaus', 'modalParam',  function ($scope, $uibModalInstance, modalStaus, modalParam) {
	$scope.header = "";
	$scope.status = modalStaus;

	$scope.message = modalParam;

	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
	};


	//Listen for location changes and call the callback
	$scope.$on('$locationChangeStart', function(event){
		$uibModalInstance.close();
	});	

}]);

