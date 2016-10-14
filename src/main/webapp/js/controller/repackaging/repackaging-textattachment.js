mainApp.controller('RepackagingTextAttachmentCtrl', ['$scope', 'modalService', 'modalStatus', 'modalParam', '$uibModalInstance', 'attachmentService', 'GlobalMessage', 'GlobalParameter',
                                            function($scope, modalService, modalStatus, modalParam, $uibModalInstance, attachmentService, GlobalMessage, GlobalParameter){
	$scope.status = modalStatus;
	$scope.parentScope = modalParam;
	
	$scope.cancel = function () {
		$uibModalInstance.close();
	};
	
	$scope.saveRepackagingTextAttachment = function(){
		if(tinyMceCharLimitReached){ 
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', GlobalMessage.maxCharLimitReached);
			return;
		}
		if($scope.parentScope.isAddTextAttachment === false){
			attachmentService.saveRepackagingTextAttachment(
					$scope.parentScope.repackaging.id, 
					$scope.parentScope.repackaging.attachment.sequenceNo, 
					$scope.parentScope.repackaging.attachment.fileName, 
					$scope.parentScope.repackaging.attachment.textAttachment)
			.then(function(data){
				$scope.parentScope.loadAttachment($scope.parentScope.repackaging.id);
			});
		} else {
			attachmentService.addRepackagingTextAttachment(
					$scope.parentScope.repackaging.id, 
					$scope.parentScope.repackaging.attachment.sequenceNo, 
					$scope.parentScope.repackaging.attachment.fileName, 
					$scope.parentScope.repackaging.attachment.textAttachment)
			.then(function(data){
				$scope.parentScope.loadAttachment($scope.parentScope.repackaging.id);
			});
		}
		$scope.cancel();
	}
	
	$scope.tinymceOptions = {
		    plugins: [
		              'advlist autolink lists link textcolor colorpicker charmap wordcount-maxlength', //wordcount code
		              'print preview hr searchreplace insertdatetime ', 
		              'nonbreaking save table contextmenu directionality paste textpattern '
		              ],
              removed_menuitems:'newdocument visualaid ',
              toolbar: 'save | print preview | undo redo | bold italic | forecolor backcolor | alignleft aligncenter alignright | bullist numlist outdent indent | link ', 
              save_onsavecallback: $scope.saveRepackagingTextAttachment,
              save_enablewhendirty: false,
              statusbar: true,
              height: 350,
              skin: 'tinymce_charcoal',
              readonly: !$scope.parentScope.isUpdatable,
              maxLength : GlobalParameter.tinyMceMaxCharLength,
	};
	
}]);