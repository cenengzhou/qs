mainApp.controller('SubcontractAwardCtrl', ['$scope', '$location', 'modalService',
                                            function($scope, $location, modalService) {

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

 
}]);