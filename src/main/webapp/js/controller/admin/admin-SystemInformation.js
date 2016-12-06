
mainApp.controller('AdminSystemInformationCtrl', 
		['$scope',  
		 function($scope) {
	$scope.tab = 'System Information';
	$scope.selectTab = function(setTab){
		$scope.tab = setTab;
	};
	$scope.isSelected = function(checkTab){
		return $scope.tab === checkTab;
	};
	
	
}]);
