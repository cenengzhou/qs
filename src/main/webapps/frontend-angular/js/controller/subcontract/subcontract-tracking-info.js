mainApp.controller('SubcontractTrackingInfoCtrl', ['$scope', 'subcontractDateService', 'subcontractService', 'modalService', '$state', 'GlobalParameter', 
                                            function($scope, subcontractDateService, subcontractService, modalService, $state, GlobalParameter ) {
	$scope.GlobalParameter = GlobalParameter;

	$scope.dates = [];
	subcontractDateService.getScDateList($scope.jobNo, $scope.subcontractNo)
	.then(function(data){
		$scope.dates = data;
	});
}]);