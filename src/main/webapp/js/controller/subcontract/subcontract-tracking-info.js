mainApp.controller('SubcontractTrackingInfoCtrl', ['$scope', 'subcontractService', 'modalService', '$state', 'GlobalParameter', 
                                            function($scope, subcontractService, modalService, $state, GlobalParameter ) {
	$scope.GlobalParameter = GlobalParameter;

	$scope.dates = subcontractService.dates;
}]);