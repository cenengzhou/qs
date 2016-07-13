mainApp.controller('PaymentCertCtrl', ['$scope' , '$http', '$stateParams', '$cookieStore', 'paymentService', 
                                          function($scope , $http, $stateParams, $cookieStore, paymentService) {
	$scope.disableButtons = true;

	$scope.mainCertNo = {
			options: [
			          "1",
			          "2",
			          "3",
			          "4",
			          "16"
			          ],
			          selected: "1"
	};

	console.log("$stateParams.paymentCertNo: "+$stateParams.paymentCertNo);
	if($stateParams.paymentCertNo){
		if($stateParams.paymentCertNo == '0'){
			$cookieStore.put('paymentCertNo', '');
			$cookieStore.put('paymentTerms', $stateParams.paymentTerms);
		}else{
			$cookieStore.put('paymentCertNo', $stateParams.paymentCertNo);
			$cookieStore.put('paymentTerms', $stateParams.paymentTerms);
		}
	}
	$scope.paymentCertNo = $cookieStore.get('paymentCertNo');
	$scope.paymentTerms = $cookieStore.get('paymentTerms');

	getPaymentCert();

	$scope.convertPaymentStatus = function(status){
		if(status!=null){
			if (status.localeCompare('PND') == 0) {
				return "Pending";
			}else if (status.localeCompare('SBM') == 0) {
				return "Submitted";
			}else if (status.localeCompare('UFR') == 0) {
				return { width: "Under Finance Review" }
			}else if (status.localeCompare('PCS') == 0) {
				return { width: "Waiting For Posting" }
			}else if (status.localeCompare('APR') == 0) {
				return "Posted To Finance";
			}
		}
	}
	


	function getPaymentCert() {
		if($scope.paymentCertNo != ""){
			paymentService.getPaymentCert($scope.jobNo, $scope.subcontractNo, $scope.paymentCertNo)
			.then(
					function( data ) {
						console.log(data);
						$scope.mainCertNo.selected = data.mainContractPaymentCertNo;
						$scope.payment = data;
						
						if($scope.payment.paymentStatus == "PND")
							$scope.disableButtons = false;
		
					});
		}
	}


}]);