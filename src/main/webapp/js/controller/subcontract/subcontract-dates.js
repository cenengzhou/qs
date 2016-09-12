mainApp.controller('SubcontractDatesCtrl', ['$scope', 'subcontractService', 'modalService', '$state', 'GlobalParameter',
                                            function($scope, subcontractService, modalService, $state, GlobalParameter) {
	$scope.GlobalParameter = GlobalParameter;
	getSubcontract();
	

//	Save Function
	$scope.save = function () {
		if($scope.subcontractNo!="" && $scope.subcontractNo!=null){
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

			//console.log(subcontractToUpdate);

			upateSubcontractDates(subcontractToUpdate);
		}else{
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Subcontract does not exist.");
		}
	};
	
	function getSubcontract(){
		if($scope.subcontractNo!="" && $scope.subcontractNo!=null){
			subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
			.then(
					function( data ) {
						$scope.subcontract = data;
					});
		}
	}

	function upateSubcontractDates(subcontractToUpdate){
		subcontractService.upateSubcontractDates($scope.jobNo, subcontractToUpdate)
		.then(
				function( data ) {
					if(data.length>0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Subcontract has been updated successfully.");
						$state.reload();
					}
				});
		}
	
}]);