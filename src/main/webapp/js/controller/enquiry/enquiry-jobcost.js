
mainApp.controller('EnquiryJobCostCtrl', 
		['$scope','$window',
		 function($scope, $window) {
	if($window.location.href.indexOf('jde')>0){
		$scope.tab = 'JDE';
	} else {
		$scope.tab = 'ADL';
	}
	$scope.selectTab = function(setTab){
		$scope.tab = setTab;
	};
	$scope.isSelected = function(checkTab){
		return $scope.tab === checkTab;
	};
	
	
}]);
