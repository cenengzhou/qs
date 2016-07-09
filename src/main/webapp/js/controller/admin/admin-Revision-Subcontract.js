mainApp.controller('AdminRevisionSubcontractCtrl',
		function($scope, $http, modalService, GlobalParameter) {
	$scope.GlobalParameter = GlobalParameter;
	$scope.SubcontractSearch = {};

	$scope.onSubmitSubcontractSearch = function() {
		var jobNo = $scope.SubcontractSearch.jobNo;
		var packageNo = $scope.SubcontractSearch.packageNo;
		$http.get('service/subcontract/getSubcontract?jobNo='+ jobNo + '&subcontractNo='+ packageNo).then(
			function(response) {
				if(response.data instanceof Object){
					$scope.SubcontractRecord = response.data;
				} else {
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please enter job number and package number!");
				}
			}, function(response){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Please search subcontract first!");
			});
	};

	$scope.onSubmitSubcontractRecord = function() {
		if ($scope.SubcontractRecord.jobInfo !== undefined) {
			$http.post('service/subcontract/UpdateSubcontract', $scope.SubcontractRecord)
	        .then(
			function(response) {
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Subcontract updated.");
			}, function(response){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Status: " + response.statusText);
			});
			$scope.RevisionsSubcontractRecord.$setPristine();
			$scope.SubcontractRecord = {};
		} else {
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please search subcontract first!");
		}
	};
	
});