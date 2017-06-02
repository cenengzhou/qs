mainApp.controller('RepackagingCtrl', ['$q', '$state', '$scope', '$location', '$cookies', '$uibModal', 'confirmService', 'repackagingService', 'resourceSummaryService', 'modalService', '$http', '$window', '$state', '$rootScope', 'rootscopeService', 'GlobalParameter', 'GlobalHelper', 'GlobalMessage',
                                       function($q, $state, $scope, $location, $cookies, $uibModal, confirmService, repackagingService, resourceSummaryService, modalService, $http, $window, $state, $rootScope, rootscopeService, GlobalParameter, GlobalHelper, GlobalMessage) {
	rootscopeService.setSelectedTips('repackagingStatus');
	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");
	$scope.GlobalParameter = GlobalParameter;
	$scope.repackaging = "";
	$scope.sequenceNo = 0;
	$scope.imageServerAddress = GlobalParameter.imageServerAddress;
	$scope.selectedAttachement = false;
	$scope.isAddTextAttachment = false;
	$scope.getLatestRepackaging = getLatestRepackaging;
	$scope.latestVersion = false;
	$scope.isResourceGenerated = false;
	getLatestRepackaging();
	
	$scope.click = function(view) {
		if(view=="generateResourceSummaries"){
			generateResourceSummaries();
		}
		else if(view=="unlock"){
			addRepackaging();
		}else if (view=="reset"){
			deleteRepackaging();
		}else if (view=="snapshot"){
			generateSnapshot();
		}else if (view=="email"){
			composeEmail();
		}else if (view=="confirm"){
			modalService.open('lg', 'view/repackaging/modal/repackaging-confirm.html', 'RepackagingConfirmModalCtrl',  $scope.latestVersion, $scope.repackaging.id);
		}
	};

	$scope.getHistory = function() {
		modalService.open('lg', 'view/repackaging/modal/repackaging-history.html', 'RepackagingHistoryModalCtrl');
	};

	$scope.updateRemarks = function(){
		updateRepackaging();
	}

	$rootScope.$on("GetSelectedRepackagingVersion", function(event, repackagingId){
		repackagingService.getRepackagingEntry(repackagingId)
		.then(
				function( data ) {
					$scope.repackaging = data;
					
					if($scope.repackaging.status != '900') $scope.isUpdatable = true;
					if($scope.repackaging.id != null){
						if($scope.repackaging.id == $scope.latestID){
							$scope.latestVersion = true;
						}else 
							$scope.latestVersion = false;
					}
				});
      });
	
	function getLatestRepackaging() {
		var deferral = $q.defer();
		repackagingService.getLatestRepackaging($scope.jobNo)
		.then(
				function( data ) {
					if(data != null && data != ''){
						$scope.repackaging = data;
						$scope.latestVersion = true;
						$scope.latestID = $scope.repackaging.id;
						$cookies.put('repackagingId', $scope.repackaging.id);
						if($scope.repackaging.status != '900') $scope.isUpdatable = true;
						$scope.isResourceGenerated = true;
					} else {
						$scope.isResourceGenerated = false;
					}
					deferral.resolve({
						repackaging: $scope.repackaging,
						isUpdatable: $scope.isUpdatable,
						isResourceGenerated: false
					})
				});
		return deferral.promise;
	}

	function addRepackaging() {
		repackagingService.addRepackaging($scope.jobNo)
		.then(
				function( data ) {
					getLatestRepackaging();
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Repackaging has been unlocked.");
					}
				});
	}

	function updateRepackaging() {
		repackagingService.updateRepackaging($scope.repackaging)
		.then(
				function( data ) {
					getLatestRepackaging();
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Remarks has been updated.");
					}
				});
	}



	function deleteRepackaging() {
		repackagingService.deleteRepackaging($scope.repackaging.id)
		.then(
				function( data ) {
					getLatestRepackaging();
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Repackaging has been reset.");
					}
				});
	}

	function generateSnapshot() {
		repackagingService.generateSnapshot($scope.repackaging.id, $scope.jobNo)
		.then(
				function( data ) {
					getLatestRepackaging();
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Snapshot has been generated.");
					}
				});
	}

	function composeEmail(){
		$state.go('repackaging-email');
	}
	
	function generateResourceSummaries() {
		resourceSummaryService.generateResourceSummaries($scope.jobNo)
		.then(
				function( data ) {
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Resource summaries have been generated.");
						$state.reload();
					}
				});
	}

}]);