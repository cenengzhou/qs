mainApp.controller('SubcontractMenuCtrl', ['$scope', '$location', '$cookies', 'subcontractService', 'modalService', 'paymentService', 'GlobalParameter',
                                           function($scope, $location, $cookies, subcontractService, modalService, paymentService, GlobalParameter) {

	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");
	$scope.subcontractNo = $cookies.get("subcontractNo");
	$scope.subcontractDescription = $cookies.get("subcontractDescription");
	$scope.paymentStatus = $cookies.get("paymentStatus");
	
	getSubcontract();
	
	$scope.paymentRequisition = function (){
		if($scope.subcontract.scStatus !="500"){
			if($scope.subcontract.scStatus >="160"){
				if($scope.subcontract.vendorNo != null && $scope.subcontract.vendorNo.length >0){
					subcontractService.generateSCDetailsForPaymentRequisition($scope.jobNo, $scope.subcontractNo)
					.then(
							function( data ) {
								if(data.length != 0){
									modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
								}else{
									$location.path("/subcontract/payment-select");
								}
							});
					
				}else
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn',  "Please select a recommended tenerer.");
				
			}else
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn',  "Please prepare Tender Analysis.");
			
		}
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
					
					if($scope.subcontract.scStatus < "500"){
						paymentService.getLatestPaymentCert($scope.jobNo, $scope.subcontractNo)
						.then(
								function( data ) {
									var latestPayment = data;
									if(latestPayment){
										paymentService.getPaymentCertList($scope.jobNo, $scope.subcontractNo)
										.then(
												function( data ) {
													var paymentList = data;
													if((paymentList != null && paymentList.length > 1) || latestPayment.paymentStatus == 'APR')
														$scope.hideItemForPayReq = false;
													else
														$scope.hideItemForPayReq = true;
												});
										
										if(latestPayment.paymentStatus.length >0)
											$scope.requisitionStatus = "Payment Requisition Status: "+GlobalParameter.getValueById(GlobalParameter.paymentStatus, data.paymentStatus);
									}else
										$scope.hideItemForPayReq = true;
								});
						
					}
				});
	}
	

}]);