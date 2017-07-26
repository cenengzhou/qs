mainApp.controller('AdminRevisionsSubcontractCtrl',
		['$scope', '$http', 'modalService', 'blockUI', 'rootscopeService', 'GlobalHelper', 'GlobalParameter', 'subcontractService', 
		function($scope, $http, modalService, blockUI, rootscopeService, GlobalHelper, GlobalParameter, subcontractService) {
	$scope.GlobalParameter = GlobalParameter;
	rootscopeService.gettingWorkScopes()
	.then(function(response){
		$scope.allWorkScopes = response.workScopes;
	});
	$scope.SubcontractSearch = {};
	$scope.onSubmitSubcontractSearch = function() {
		var jobNo = $scope.SubcontractSearch.jobNo;
		var packageNo = $scope.SubcontractSearch.packageNo;
		cleanupSubcontractRecord();
		if(GlobalHelper.checkNull([jobNo, packageNo])){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please enter job number and subcontract number!");
		} else {
		subcontractService.getSubcontract(jobNo, packageNo).then(
			function(data) {
				if(data instanceof Object){
					if(data.workscope) data.workscope = '' + data.workscope;
					$scope.SubcontractRecord = data;
				} else {
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Subcontract not found");
				}
			}, function(data){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
			});
		}
	};

	$scope.onSubmitSubcontractRecord = function() {
		if($scope.RevisionsSubcontractRecord.$invalid) {
			return
		}
		
		if ($scope.SubcontractRecord.jobInfo !== undefined) {
			subcontractService.updateSubcontractAdmin($scope.SubcontractRecord)
	        .then(
			function(data) {
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Subcontract updated.");
			}, function(message){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', message);
			});
		} else {
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please search subcontract first!");
		}
		cleanupSubcontractRecord();
	};
	
	function cleanupSubcontractRecord(){
		$scope.RevisionsSubcontractRecord.$setPristine();
//		$scope.SubcontractRecord = {};
	}
	
}]);