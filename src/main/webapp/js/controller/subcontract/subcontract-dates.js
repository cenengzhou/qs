mainApp.controller('SubcontractDatesCtrl', ['$scope', '$timeout', 'subcontractService', 'subcontractDateService', 'commentService', 'modalService', '$state', 'GlobalHelper', 'GlobalParameter', 
                                            function($scope, $timeout, subcontractService, subcontractDateService, commentService, modalService, $state, GlobalHelper, GlobalParameter ) {
	$scope.GlobalParameter = GlobalParameter;
	getSubcontract();
	
	$scope.dates = [];
	subcontractDateService.getScDateList($scope.jobNo, $scope.subcontractNo, true)
	.then(function(data){
		$scope.dates = data;
	});
//	Save Function
	$scope.save = function () {
		if($scope.subcontractNo!="" && $scope.subcontractNo!=null){

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
					onSiteStartDate: $scope.subcontract.onSiteStartDate,
					scFinalAccDraftDate: $scope.subcontract.scFinalAccDraftDate,
					scFinalAccSignoffDate: $scope.subcontract.scFinalAccSignoffDate
			}

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
	
	angular.element('input[name$=".singleDate"').daterangepicker({
	    singleDatePicker: true,
	    showDropdowns: true,
	    autoApply: true,
		locale: {
		      format: GlobalParameter.MOMENT_DATE_FORMAT
		    },

	})
	$scope.openDropdown = function( $event){
		angular.element('input[name="' + $event.currentTarget.nextElementSibling.name + '"').click();
	}
	
	$scope.downloadAttachment = function(attach) {
		const textKey = $scope.jobNo + '|' + $scope.subcontractNo + '|';
		const nameObject = GlobalParameter['AbstractAttachment'].SCPackageNameObject;
    	url = 'service/attachment/obtainFileAttachment?nameObject='+nameObject+'&textKey='+textKey+'&sequenceNo='+attach.noSequence;
    	GlobalHelper.downloadFile(encodeURI(url));
	}
	
	$scope.addComment = function(event, obj) {
		if(obj.comment && (event.type == 'click' || event.key == 'Enter')){
			const comment = {
					field: obj.field,
					idTable: $scope.subcontract.id,
					message: obj.comment,
					nameTable: 'SUBCONTRACT'
			}
			commentService.save(comment)
			.then(function(data) {
				commentService.find('SUBCONTRACT', $scope.subcontract.id, obj.field)
				.then(function(newData){
					$scope.dates.forEach(d => {
						if(d.field == obj.field) d.commentList = newData;
						obj.comment = '';
					});
				});
			});
		}
	}
	
}]);