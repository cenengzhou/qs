mainApp.controller('SubcontractDatesCtrl', ['$scope', 'subcontractService', 'modalService', '$state',
                                            function($scope, subcontractService, modalService, $state) {

	getSubcontract();
	

//	Save Function
	$scope.save = function () {
		console.log($scope.subcontract);
		
		var subcontractToUpdate = {
				id: $scope.subcontract.id,
				packageNo : $scope.subcontract.packageNo,
				requisitionApprovedDate:  $scope.subcontract.requisitionApprovedDate,
				tenderAnalysisApprovedDate : $scope.subcontract.tenderAnalysisApprovedDate,
				preAwardMeetingDate: $scope.subcontract.preAwardMeetingDate,
				loaSignedDate: $scope.subcontract.loaSignedDate,
				scDocScrDate: $scope.subcontract.scDocScrDate,
				scDocLegalDate: $scope.subcontract.scDocLegalDate,
				workCommenceDate: $scope.subcontract.workCommenceDate,
				onSiteStartDate: $scope.subcontract.onSiteStartDate
		}
		
		console.log(subcontractToUpdate);
		
		upateSubcontractDates(subcontractToUpdate);
	};
	
	function getSubcontract(){
		subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					console.log(data);
					$scope.subcontract = data;
				});
	}

	function upateSubcontractDates(subcontractToUpdate){
		subcontractService.upateSubcontractDates($scope.jobNo, subcontractToUpdate)
		.then(
				function( data ) {
					if(data.length>0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Alert', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Subcontract has been updated successfully.");
						$state.reload();
					}
				});
		}
	
}]);