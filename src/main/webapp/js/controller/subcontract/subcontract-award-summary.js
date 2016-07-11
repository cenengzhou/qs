mainApp.controller('SubcontractAwardSummaryCtrl', ['$scope', 'tenderVarianceService', 'tenderService', 'subcontractService', 'modalService',
                                            function($scope, tenderVarianceService, tenderService, subcontractService, modalService) {
	loadData();
	
    //Save Function
    $scope.submit = function () {
    };

    
    function loadData(){
		if($scope.subcontractNo!="" && $scope.subcontractNo!=null){
			getSubcontract();
			getTender();
			getTenderList();
			getRecommendedTender();
		}
	}
 
    
    function getSubcontract(){
		subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					$scope.subcontract = data;
					
					if($scope.subcontract.scStatus =="330" || $scope.subcontract.scStatus =="500")
						$scope.disableButtons = true;
					else
						$scope.disableButtons = false;
				});
	}
	
    function getTender() {
		tenderService.getTender($scope.jobNo, $scope.subcontractNo, 0)
		.then(
				function( data ) {
					$scope.budgetTender = data;
				});
	}
    
    function getTenderList() {
		tenderService.getTenderList($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					$scope.tenderList = data;
				});
	}

	function getRecommendedTender() {
		tenderService.getRecommendedTender($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					if(data.length==0){
						//modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select a tenderer before doing tender variance.");
					}else{
						$scope.rcmTenderer = data;
						getTenderVarianceList($scope.rcmTenderer.vendorNo);
					}
				});
	}

	function getTenderVarianceList(tenderNo) {
		tenderVarianceService.getTenderVarianceList($scope.jobNo, $scope.subcontractNo, tenderNo)
		.then(
				function( data ) {
					$scope.tenderVarianceList = data;
				});
	}
}]);