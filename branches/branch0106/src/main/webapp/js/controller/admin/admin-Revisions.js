
mainApp.controller('AdminRevisionsCtrl', 
		['$scope', 
		 function($scope) {
	$scope.tab = 'Subcontract';
	$scope.selectTab = function(setTab){
		$scope.tab = setTab;
	};
	$scope.isSelected = function(checkTab){
		return $scope.tab === checkTab;
	};
	
	
}]);
