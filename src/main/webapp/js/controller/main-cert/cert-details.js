mainApp.controller('CertDetailsCtrl', ['$scope', 'mainCertService', '$cookies', '$stateParams',
                                       function ($scope,  mainCertService, $cookies, $stateParams) {

	if($stateParams.mainCertNo){
		$cookies.put('mainCertNo', $stateParams.mainCertNo);
	}else
		$cookies.put('mainCertNo', '');
	
	$scope.mainCertNo = $cookies.get("mainCertNo");
	
	if($scope.mainCertNo != null && $scope.mainCertNo.length > 0)
		getCertificate();
   
 //Save Function
	$scope.save = function () {
		console.log("ipaSubmissionDate: " + $scope.ipaSubmissionDate);

	};
	
	
	function getCertificate(){
		mainCertService.getCertificate($scope.jobNo, $scope.mainCertNo)
		.then(
				function( data ) {
					console.log(data);
					$scope.cert = data;
					if($scope.cert.length==0 || $scope.cert.status < 300)
						$scope.disableButtons = false;
					else
						$scope.disableButtons = true;
				});
	}
	
	
}]);

