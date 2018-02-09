mainApp.controller('AddendumTabCtrl', ['$scope', '$state', '$stateParams', '$cookies',
                                    function($scope, $state, $stateParams, $cookies) {
	if($stateParams.addendumNo == null){
		$scope.addendumNo =  $cookies.get('addendumNo');
	} else {
		$scope.addendumNo = $stateParams.addendumNo;
	}
	$scope.jobNo = $stateParams.jobNo || $cookies.get('jobNo');
	$scope.subcontractNo = $stateParams.subcontractNo || $cookies.get('subcontractNo');
	$scope.addendumDetailHeaderRef=$stateParams.addendumDetailHeaderRef || $cookies.get('addendumDetailHeaderRef');
}]);