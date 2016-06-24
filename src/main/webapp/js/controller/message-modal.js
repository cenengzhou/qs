mainApp.controller("MessageModalCtrl", ['$scope', '$uibModalInstance', 'modalType', 'modalMessage',  function ($scope, $uibModalInstance, modalType, modalMessage) {
	$scope.header = "";
	$scope.type = modalType;
	
	$scope.message = modalMessage;

	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
	};
	
    
}]);

