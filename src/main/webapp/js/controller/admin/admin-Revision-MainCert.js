mainApp.controller('AdminRevisionMainCertCtrl',
		function($scope, $http, modalService, GlobalParameter) {
	$scope.GlobalParameter = GlobalParameter;
	$scope.MainCertSearch = {};

	$scope.onSubmitMainCertSearch = function() {
		var jobNo = $scope.MainCertSearch.jobNo;
		var certificateNumber = $scope.MainCertSearch.certificateNumber;
		$http.get('service/mainCert/GetCertificate?jobNo='+ jobNo + '&certificateNumber='+ certificateNumber).then(
			function(response) {
				if(response.data instanceof Object){
					$scope.MainCertRecord = response.data;
				} else {
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please enter job number and certificate number!");
				}
			}, function(response){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Please search MainCert first!");
			});
	};

	$scope.onSubmitMainCertRecord = function() {
		if ($scope.MainCertRecord.jobNo !== undefined) {
			$http.post('service/mainCert/UpdateCertificate', $scope.MainCertRecord)
	        .then(
			function(response) {
				if(response.data === '') {
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "MainCert updated.");
				} else {
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', response.data);
				}
			}, function(response){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Status: " + response.statusText);
			});
			$scope.RevisionsMainCertRecord.$setPristine();
			$scope.MainCertRecord = {};
		} else {
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please search MainCert first!");
		}
	};
	
});