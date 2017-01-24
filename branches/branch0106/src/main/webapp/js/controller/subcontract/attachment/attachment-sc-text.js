mainApp.controller('AttachmentSCTextCtrl', ['$scope', 'modalStatus', 'modalParam', '$uibModalInstance', 'attachmentService', 'modalService', 'GlobalMessage', 'GlobalParameter',
                                            function($scope, modalStatus, modalParam, $uibModalInstance, attachmentService, modalService, GlobalMessage, GlobalParameter){
	$scope.status = modalStatus;
	$scope.parentScope = modalParam;
	
	$scope.cancel = function () {
		$uibModalInstance.close();
	};
	
	$scope.saveTextAttachment = function(){
		if(tinyMceCharLimitReached){ 
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', GlobalMessage.maxCharLimitReached);
			return;
		}
		if($scope.parentScope.isAddTextAttachment === false){
			$scope.parentScope.saveTextAttachmentFacade(
					$scope.parentScope.nameObject, 
					$scope.parentScope.textKey, 
					$scope.parentScope.textAttachment.sequenceNo, 
					$scope.parentScope.textAttachment.fileName, 
					$scope.parentScope.textAttachment.textAttachment)
			.then(function(data){
				$scope.parentScope.loadAttachment($scope.parentScope.nameObject, $scope.parentScope.textKey);
			});
		} else {
			$scope.parentScope.saveTextAttachmentFacade(
					$scope.parentScope.nameObject, 
					$scope.parentScope.textKey, 
					$scope.parentScope.textAttachment.sequenceNo, 
					$scope.parentScope.textAttachment.fileName, 
					$scope.parentScope.textAttachment.textAttachment)
			.then(function(data){
				$scope.parentScope.loadAttachment($scope.parentScope.nameObject, $scope.parentScope.textKey);
			});
		}
		$scope.cancel();
	}
		
	function getOptions(){
		if($scope.parentScope.disableRichEditor){
			return tinySimpleEditor;
		} else {
			return tinyWithRichEditor;
		}
	}
	var tinyWithRichEditor = {
		    plugins: [
		              'advlist autolink lists link textcolor colorpicker charmap', // code
		              'print preview hr searchreplace wordcount-maxlength insertdatetime ',
		              'nonbreaking save table contextmenu directionality paste textpattern '
		              ],
              removed_menuitems:'newdocument visualaid ',
              toolbar: 'save | print preview | undo redo | bold italic | forecolor backcolor | alignleft aligncenter alignright | bullist numlist outdent indent | link ', 
              save_onsavecallback: $scope.saveTextAttachment,
              save_enablewhendirty: false,
              statusbar: true,
              height: 350,
              skin: 'tinymce_charcoal',
              readonly: !$scope.parentScope.isTextUpdatable,
              maxLength : GlobalParameter.tinyMceMaxCharLength,
	};
	
	var tinySimpleEditor = {
		    plugins: [
	              'charmap', // code
	              'print preview searchreplace wordcount-maxlength insertdatetime ',
	              'nonbreaking save contextmenu directionality paste textpattern '
		              ],
              removed_menuitems:'newdocument visualaid',
              toolbar: 'save | print preview | undo redo |', 
              menubar : '',
              save_onsavecallback: $scope.saveTextAttachment,
              save_enablewhendirty: false,
              statusbar: true,
              height: 350,
              skin: 'tinymce_charcoal',
              readonly: !$scope.parentScope.isTextUpdatable,
              maxLength : GlobalParameter.tinyMceMaxCharLength,
	};
	$scope.tinymceOptions = getOptions();

}]);