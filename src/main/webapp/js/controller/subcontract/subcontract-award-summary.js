mainApp.controller('SubcontractAwardSummaryCtrl', ['$scope', 'tenderVarianceService', 'tenderService', 'subcontractService', 'masterListService', 'modalService', 'confirmService','GlobalMessage', '$state', 'htmlService', 'GlobalHelper', 'jobService',
                                            function($scope, tenderVarianceService, tenderService, subcontractService, masterListService, modalService, confirmService, GlobalMessage, $state, htmlService, GlobalHelper, jobService) {
	loadData();
	
    $scope.submit = function () {
    	if($scope.rcmTenderer!=null){
    		if($scope.subcontract.preAwardMeetingDate!=null){
		    	masterListService.getSubcontractor($scope.rcmTenderer.vendorNo)
		    	.then(
		    			function( data ) {
		    				if(data.length!=0){
		    					var message = "";
		    					if((data.scFinancialAlert !="" && data.scFinancialAlert !=null)){
		    						message = "1. "+GlobalMessage.subcontractorHoldMessage+"<br/>";
		    					}
		    					if($scope.rcmTenderer.currencyCode !="HKD"){
		    						if(message!="")
		    							message = message + "2. "
		    						message = message + "Currency   Code: "+$scope.rcmTenderer.currencyCode;
		    					}
		    					if(message.length>0){
		    						message = message + "<br/>Are you sure to continue?";
		    						var modalOptions = {
		    								bodyText: message
		    						};
		    						confirmService.showModal({}, modalOptions).then(function (result) {
		    							if(result == "Yes"){
		    								submitAwardApproval();
		    							}
		    						});
		    					}
		    					else
		    						submitAwardApproval();
		    				}
		    			});
	    	}else
	    		modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please input Pre-Award Finalization Meeting Date.");
    	}else
    		modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select a tenderer.");
    };

    
    function loadData(){
    	getCompanyName();
		if($scope.subcontractNo!="" && $scope.subcontractNo!=null){
			getSubcontract();
			htmlService.makeHTMLStringForTenderAnalysis({jobNumber: $scope.jobNo, packageNo: $scope.subcontractNo, htmlVersion:'A'})
			.then(function(data){
				$scope.awardHtml = GlobalHelper.formTemplate(data);
			});
			getTender();
			getTenderList();
			getRecommendedTender();
		}
	}
 
    function getCompanyName() {
		jobService.getCompanyName($scope.jobNo)
		.then(
				function( data ) {console.log(data);
					$scope.companyName = data;
				});
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
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select a tenderer before doing tender variance.");
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
	
	function submitAwardApproval(){
		subcontractService.submitAwardApproval($scope.jobNo, $scope.subcontractNo, $scope.rcmTenderer.vendorNo)
    	.then(
    			function( data ) {
    				if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Subcontract has been submitted.");
						$state.reload();
					}
    			});
	}
	
}]);