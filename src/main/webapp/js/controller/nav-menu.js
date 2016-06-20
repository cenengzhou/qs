mainApp.controller('NavMenuCtrl', ['$http', '$scope', '$location', '$rootScope', '$cookieStore', '$stateParams', 
                                   function($http, $scope, $location, $rootScope, $cookieStore, $stateParams) {
	if($stateParams.jobNo){
		$cookieStore.put('jobNo', $stateParams.jobNo);
		$cookieStore.put('jobDescription', $stateParams.jobDescription);
	}

	$scope.jobNo = $cookieStore.get("jobNo");
	$scope.jobDescription = $cookieStore.get("jobDescription");

	
	$scope.subcontractNo = $cookieStore.get("subcontractNo");
	$scope.subcontractDescription = $cookieStore.get("subcontractDescription");

	$scope.currentPath = $location.path();


	//console.log("$scope.currentPath: "+$scope.currentPath);

	if($scope.currentPath.indexOf("/job")==0){	
		$scope.activeMenu = '';
		$scope.activeJobSideMenu = $scope.currentPath;
	}
	else if ($scope.currentPath.indexOf("/cert")==0){
		$scope.activeMenu = 'Certificate';

	}
	else if ($scope.currentPath.indexOf("/repackaging")==0){
		$scope.activeMenu = 'Repackaging';

	}
	else if ($scope.currentPath.indexOf("/subcontract")==0){
		$scope.activeMenu = 'Subcontract';
		$scope.activeSubcontractSideMenu = $scope.currentPath;
	}
	else if ($scope.currentPath.indexOf("/iv")==0){
		$scope.activeMenu = 'IV';
		$scope.activeIVSideMenu = $scope.currentPath;

	}else if ($scope.currentPath.indexOf("/transit")==0){
		$scope.activeMenu = 'Transit';
	}else if ($scope.currentPath.indexOf("/admin")==0){
		$scope.activeMenu = 'Admin';
		$scope.activeAdminSideMenu = $scope.currentPath;
	}



}]);