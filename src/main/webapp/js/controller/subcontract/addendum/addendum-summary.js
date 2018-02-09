mainApp.controller('AddendumSummaryCtrl', ['$scope' , 'modalService', 'addendumService', 'subcontractService', '$stateParams', '$cookies', '$state', 
                                           function($scope ,modalService, addendumService, subcontractService, $stateParams, $cookies, $state) {

	$scope.addendumNo = $stateParams.addendumNo;

	loadData();
	
	function loadData(){
		if($scope.addendumNo != null && $scope.addendumNo.length != 0){
			getAddendum();
			getAllAddendumDetails();
		}else
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Addendum does not exist. Please create addendum title first.");
	}
	
	function getAddendum(){
		addendumService.getAddendum($scope.jobNo, $scope.subcontractNo, $scope.addendumNo)
		.then(
				function( data ) {
					$scope.addendum = data;
				});
	}
	
	function getAllAddendumDetails(){
		addendumService.getAllAddendumDetails($scope.jobNo, $scope.subcontractNo, $scope.addendumNo)
		.then(
				function( data ) {
					$scope.addendumDetailList = data;
				});
	}
	


}]);