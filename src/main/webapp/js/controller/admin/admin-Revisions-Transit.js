mainApp.controller('AdminRevisionsTransitCtrl',
		['$scope', '$http', 'modalService', 'blockUI', 'rootscopeService', 'GlobalHelper', 'GlobalParameter', 'transitService', 
		function($scope, $http, modalService, blockUI, rootscopeService, GlobalHelper, GlobalParameter, transitService) {

	$scope.onUnlockTransit = function() {
		if ($scope.jobNo > 10000 && $scope.jobNo < 99999) {
			transitService.unlockTransitAdmin($scope.jobNo).then(
			function(data) {
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Transit updated.");
			}, function(message){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', message);
			});
		} else {
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Job number must be 5 digit");
		}
	};
	
}]);