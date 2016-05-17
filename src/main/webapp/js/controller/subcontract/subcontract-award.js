mainApp.controller('SubcontractAwardeModalCtrl', ['$scope', '$log', '$uibModalInstance', '$location', 'modalService', function($scope, $log, $uibModalInstance, $location, modalService) {

	$scope.packageNo = 1004;
	$scope.budget = 226789000;
	
	$scope.subcontractSum = 226789000;
	$scope.vendorNo = 31347;
	$scope.vendorName = "VSL Hong Kong Ltd.";

    //Save Function
    $scope.submit = function () {
    	$location.path("/subcontract-flow");
    	$uibModalInstance.close();
    };

    $scope.cancel = function () {
    	$uibModalInstance.dismiss("cancel");
    };
    
  //Listen for location changes and call the callback
    $scope.$on('$locationChangeStart', function(event){
   		 $uibModalInstance.close();
    });	
}]);