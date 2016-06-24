
mainApp.controller('AdminRevisionsCtrl', ['$scope', '$rootScope', function($scope, $rootScope) {
	this.tab = 1;
	this.selectTab = function(setTab){
		this.tab = setTab;
	};
	this.isSelected = function(checkTab){
		return this.tab === checkTab;
	};
}]);
