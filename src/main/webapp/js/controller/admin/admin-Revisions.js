
mainApp.controller('AdminRevisionsCtrl', ['$scope', '$rootScope', function($scope, $rootScope) {
	$scope.tab = 1;
	$scope.selectTab = function(setTab){
		$scope.tab = setTab;
	};
	$scope.isSelected = function(checkTab){
		return $scope.tab === checkTab;
	};
	
	
}]);
