mainApp.controller('AdminRevisionSubcontractCtrl',
		['$scope', '$http', 'modalService', 'blockUI', 'GlobalHelper', 'GlobalParameter', 'subcontractService',
		function($scope, $http, modalService, blockUI, GlobalHelper, GlobalParameter, subcontractService) {
	$scope.GlobalParameter = GlobalParameter;
	$scope.SubcontractSearch = {};

	$scope.blockSbucontract = blockUI.instances.get('blockSbucontract');
	$scope.blockSbucontract.start({hideMessage: true, hideAnimate:true});
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
					$scope.SubcontractRecord = data;
					$scope.blockSbucontract.stop();
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
		$scope.SubcontractRecord = {};
		if(!$scope.blockSbucontract.isBlocking()){
			$scope.blockSbucontract.start({hideMessage: true, hideAnimate:true});
		}
	}
	
}]);