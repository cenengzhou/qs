mainApp.controller('AdminRevisionsFinalAccountCtrl',
		['$scope', '$http', 'modalService', 'GlobalParameter', 'blockUI', 'GlobalHelper', 'finalAccountService','rootscopeService',
		function($scope, $http, modalService, GlobalParameter, blockUI, GlobalHelper, finalAccountService,rootscopeService) {
	$scope.GlobalParameter = GlobalParameter;
	$scope.FinalAccountSearch = {};
	$scope.onSubmitFinalAccountSearch = function() {
		var jobNo = $scope.FinalAccountSearch.jobNo;
		var subcontractNo = $scope.FinalAccountSearch.subcontractNo;
		var addendumNo = $scope.FinalAccountSearch.addendumNo;
		cleanupFinalAccountRecord();
		if(GlobalHelper.checkNull([jobNo, subcontractNo, addendumNo])){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please enter job number and subcontract number!");
		} else {
		finalAccountService.getFinalAccountAdmin(jobNo, subcontractNo, addendumNo)
		.then(function(data) {
				if(data instanceof Object){
					$scope.FinalAccountRecord = data;
				} else {
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "FinalAccount not found!");
				}
			}, function(data){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Please search finalAccount first!");
			});
		}
	};

	$scope.onSubmitFinalAccountRecord = function() {
		if($scope.RevisionsFinalAccountRecord.$invalid) {
			return
		}
		if ($scope.FinalAccountRecord.jobNo !== undefined) {
			finalAccountService.updateFinalAccountAdmin($scope.FinalAccountRecord.jobNo, $scope.FinalAccountSearch.subcontractNo, $scope.FinalAccountRecord.addendumNo, $scope.FinalAccountRecord)
	        .then(function(data) {
				if(data === ''){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "FinalAccount updated.");
				} else {
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
				}
			}, function(data){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
			});
		} else {
			$scope.blockFinalAccount.stop();
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please search finalAccount first!");
		}
		cleanupFinalAccountRecord();
	};

	$scope.onDeleteFinalAccount = function(){
		finalAccountService.deleteFinalAccountAdmin($scope.FinalAccountRecord.jobNo, $scope.FinalAccountSearch.subcontractNo, $scope.FinalAccountRecord.addendumNo, $scope.FinalAccountRecord)
		.then(function(data){
			if(data === ''){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "FinalAccount deleted.");
				$scope.FinalAccountRecord = null;
			}
		})
	}

	function cleanupFinalAccountRecord(){
		$scope.RevisionsFinalAccountRecord.$setPristine();
//		$scope.FinalAccountRecord = {};
	}
	
}]);
