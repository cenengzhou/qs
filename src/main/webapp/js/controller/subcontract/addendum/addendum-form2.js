mainApp.controller('AddendumForm2Ctrl', ['$scope' , 'modalService', 'addendumService', 'subcontractService', '$stateParams', '$cookies', '$state', 
                                           function($scope ,modalService, addendumService, subcontractService, $stateParams, $cookies, $state) {

	$scope.addendumNo = $cookies.get('addendumNo');
	var addendumDetailHeaderRef = $cookies.get('addendumDetailHeaderRef');

	
	getAddendum();
	getAllAddendumDetails();
	
	$scope.submit = function(){
		$scope.disableButtons = true;
		submitAddendumApproval();
	}
	
	function getAddendum(){
		addendumService.getAddendum($scope.jobNo, $scope.subcontractNo, $scope.addendumNo)
		.then(
				function( data ) {
					$scope.addendum = data;
					if($scope.addendum.length==0 || $scope.addendum.status == "PENDING")
						$scope.disableButtons = false;
					else
						$scope.disableButtons = true;
				});
	}
	
	function getAllAddendumDetails(){
		addendumService.getAllAddendumDetails($scope.jobNo, $scope.subcontractNo, $scope.addendumNo)
		.then(
				function( data ) {
					$scope.addendumDetailList = data;
				});
	}
	
	function submitAddendumApproval(){
		addendumService.submitAddendumApproval($scope.jobNo, $scope.subcontractNo, $scope.addendumNo)
		.then(
				function( data ) {
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
						$scope.disableButtons = false;
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Addendum has been submitted for approval.");
						$state.reload();
					}
				});
	}


}]);