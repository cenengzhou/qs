mainApp.controller('SubcontractMenuCtrl', ['$scope', '$location', '$cookies', 'subcontractService', 'modalService',
                                           function($scope, $location, $cookies, subcontractService, modalService) {

	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");
	$scope.subcontractNo = $cookies.get("subcontractNo");
	$scope.subcontractDescription = $cookies.get("subcontractDescription");


	getSubcontract();

	
	$scope.paymentRequisition = function (){
		subcontractService.generateSCDetailsForPaymentRequisition($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					if(data.length != 0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						$location.path("/subcontract/dashboard");
					}
				});
	}
	
	function getSubcontract(){
		subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					$scope.subcontract = data;

					if($scope.subcontract.scStatus =="330" || $scope.subcontract.scStatus =="500")
						$scope.hideItem = false;
					else
						$scope.hideItem = true;
				});
	}

}]);