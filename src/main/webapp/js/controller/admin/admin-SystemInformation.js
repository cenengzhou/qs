
mainApp.controller('AdminSystemInformationCtrl', ['$scope', '$stateParams', function($scope, $stateParams) {
	$scope.activeTab = $stateParams.activeTab;
	$scope.selectTab = function(setTab){
		$scope.activeTab = setTab;
	};
	$scope.isSelected = function(checkTab){
		return $scope.activeTab === checkTab;
	};
	
}]);
