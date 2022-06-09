mainApp.controller('CaMenuCtrl', ['$scope',  '$cookies', 'consultancyAgreementService', 
	function($scope, $cookies,  consultancyAgreementService) {

	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");
	//only for init page load, reassign value when subcontract loaded from backend
	$scope.subcontractNo = $cookies.get("subcontractNo");
	$scope.subcontractDescription = $cookies.get("subcontractDescription");

	getMemo();
	
	function getMemo(){
        consultancyAgreementService.getMemo($scope.jobNo, $scope.subcontractNo).then(function (data) {
            $scope.memo = data;
        });
	}
	

}]);