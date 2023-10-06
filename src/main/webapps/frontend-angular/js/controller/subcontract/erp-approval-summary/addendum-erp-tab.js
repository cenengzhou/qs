mainApp.controller('AddendumTabCtrl',
		['$scope' , '$state', '$stateParams', '$cookies', 'addendumService', 'modalService', 'confirmService', 'roundUtil', 'htmlService', 'GlobalHelper',
        function($scope , $state, $stateParams, $cookies, addendumService, modalService, confirmService, roundUtil, htmlService, GlobalHelper) {

	$scope.jobNo = $cookies.get("jobNo");
	$scope.subcontractNo = $cookies.get("subcontractNo");
	$scope.addendumNo = $cookies.get("addendumNo");
	$scope.tabDisableButtons = true;
	loadData();

	function loadData() {
		addendumService.getAddendum($scope.jobNo, $scope.subcontractNo, $scope.addendumNo)
			.then(function( data ) {
				$scope.addendum = data;

				if($scope.addendum.status == "PENDING")
					$scope.tabDisableButtons = false;
			});
	}


}]);