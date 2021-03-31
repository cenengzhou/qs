mainApp.controller('AdminRevisionsMainCertCtrl',
		['$scope', '$http', 'modalService', 'blockUI', 'GlobalHelper', 'GlobalParameter', 'mainCertService','rootscopeService',
		function($scope, $http, modalService, blockUI, GlobalHelper, GlobalParameter, mainCertService,rootscopeService) {
	$scope.GlobalParameter = GlobalParameter;
	$scope.MainCertSearch = {};
	$scope.onSubmitMainCertSearch = function() {
		var jobNo = $scope.MainCertSearch.jobNo;
		var certificateNumber = $scope.MainCertSearch.certificateNumber;
		cleanupMainCertRecord();
		if(GlobalHelper.checkNull([jobNo, certificateNumber])){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please enter job number and certificate number!");
		} else {
		mainCertService.getCertificate(jobNo, certificateNumber)
		.then( function(data) {
				if(data instanceof Object){
					$scope.MainCertRecord = data;
				} else {
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Main Cert not found!");
				}
			}, function(data){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Error");
			});
		}
	};

	$scope.onSubmitMainCertRecord = function() {
		if($scope.RevisionsMainCertRecord.$invalid) {
			return
		}
		if ($scope.MainCertRecord.jobNo !== undefined) {
			mainCertService.updateCertificateByAdmin($scope.MainCertRecord)
	        .then(function(data) {
				if(data === '') {
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "MainCert updated.");
				} else {
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
				}
			}, function(data){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
			});
		} else {
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please search MainCert first!");
		}
		cleanupMainCertRecord();
	};
	
	function cleanupMainCertRecord(){
		$scope.RevisionsMainCertRecord.$setPristine();
//		$scope.MainCertRecord = {};
	}
	
}]);
