mainApp.controller("SubcontractHeaderCtrl", ['$scope', 'subcontractService', '$cookies', 'modalService', 'subcontractRetentionTerms', '$state', 'GlobalParameter',
                                                  function ($scope,  subcontractService, $cookies, modalService, subcontractRetentionTerms, $state, GlobalParameter) {
	getSubcontract();
	

	//Rentention
	$scope.percentageOption= "Revised";
	
	
	
	function getSubcontract(){
		subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					if(data.length != 0){
						$scope.subcontract = data;
						$scope.subcontractStatus = data.scStatus + ' - ' + GlobalParameter.getValueById(GlobalParameter.subcontractStatus, data.scStatus);
						
						if($scope.subcontract.retentionTerms == subcontractRetentionTerms.RETENTION_LUMPSUM){
							$scope.subcontract.retentionTerms = "Lump Sum";
						}else if($scope.subcontract.retentionTerms == subcontractRetentionTerms.RETENTION_ORIGINAL){
							$scope.subcontract.retentionTerms = "Percentage";
							$scope.percentageOption= "Original";
						}else if($scope.subcontract.retentionTerms == subcontractRetentionTerms.RETENTION_REVISED){
							$scope.subcontract.retentionTerms = "Percentage";
							$scope.percentageOption= "Revised";
						} 
						
						if($scope.subcontract.cpfCalculation == "1"){
							$scope.subcontract.cpfCalculation = true;
						}else
							$scope.subcontract.cpfCalculation = false;
						
						if($scope.subcontract.scStatus =="330" || $scope.subcontract.scStatus =="500")
							$scope.disableButtons = true;
						else
							$scope.disableButtons = false;
						
						$scope.disableSubcontactNo = true;
					}else
						$scope.disableSubcontactNo = false;
				});
	}
	
	
	
	/*function getWorkScope(workScopeCode){
		subcontractService.getWorkScope(workScopeCode)
		.then(
				function( data ) {
					if(data.length==0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Work Scope "+workScopeCode+" does not exist.");
					}else{
						upateSubcontract();
					}
				});
	}*/

}]);

