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
	
	$scope.userIcon = 'resources/images/profile.png';
	$scope.imageServerAddress = 'http://gammon/PeopleDirectory_Picture/'
	$scope.user = {name:'No one'};
	

	$scope.currentPath = $location.path();

	$scope.getCurrentUser = function(){
		$http.get('service/security/getCurrentUser')
		.then(function(response){
			if(response.data instanceof Object){
				$scope.user = response.data;
				$scope.userIcon = $scope.imageServerAddress + $scope.user.StaffID + '.jpg';
			}
		})
	}
	$scope.getCurrentUser();

	if($scope.currentPath.indexOf("/job")==0){	
		$scope.activeMenu = '';
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

	}else if ($scope.currentPath.indexOf("/transit")==0){
		$scope.activeMenu = 'Transit';
	}else if ($scope.currentPath.indexOf("/admin")==0){
		$scope.activeMenu = 'Admin';
		$scope.activeAdminSideMenu = $scope.currentPath;
	}



}]);