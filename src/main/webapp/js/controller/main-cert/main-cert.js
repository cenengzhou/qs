mainApp.controller('MainCertCtrl', ['$scope', 'modalService', 'mainCertService', '$cookies', '$rootScope', '$stateParams',
	function($scope, modalService, mainCertService, $cookies, $rootScope, $stateParams) {
	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");
	$scope.mainCertStatus = '';	

	if($stateParams.mainCertNo){
		if($stateParams.mainCertNo == '0'){
			$cookies.put('mainCertNo', '');
		}else{
			$cookies.put('mainCertNo', $stateParams.mainCertNo);
		}
	}
	
	$scope.mainCertNo = $cookies.get("mainCertNo");
	getCertificate($scope.mainCertNo);

	
	function getCertificate(mainCertNo){
		mainCertService.getCertificate($scope.jobNo, mainCertNo)
		.then(
				function( data ) {
					if(data)
						$scope.mainCertStatus = data.certificateStatus;
				});
	}

	$scope.convertCertStatus = function(status){
		if(status!=null){
			if (status.localeCompare('100') == 0) {
				return "Certificate Created";
			}else if (status.localeCompare('120') == 0) {
				return "IPA Sent";
			}else if (status.localeCompare('150') == 0) {
				return "Certificate(IPC) Confirmed";
			}else if (status.localeCompare('200') == 0) {
				return "Certificate(IPC) Waiting for special approval";
			}else if (status.localeCompare('300') == 0) {
				return "Certificate Posted to Finance's AR";
			}else if (status.localeCompare('400') == 0) {
				return "Certifited Amount Received";
			}
		}
	}
	
	$rootScope.$on("UpdateMainCertStatus", function(event, transitStatus){
		$scope.mainCertStatus = mainCertStatus;
		
	});
	
}]);