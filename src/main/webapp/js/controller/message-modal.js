mainApp.controller("MessageModalCtrl", ['$scope', '$uibModalInstance', 'items',  function ($scope, $uibModalInstance, items) {
	$scope.header = "";
	$scope.message = items;

	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
	};
	
    
}]);

