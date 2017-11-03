mainApp.controller('AttachmentTextEditorCtrl', ['$scope', 'modalStatus', 'modalParam', '$uibModalInstance', 'attachmentService', 'modalService', 'GlobalMessage', 'GlobalParameter',
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
		var nameObject = $scope.parentScope.nameObject;
		var textKey = $scope.parentScope.textKey;
		var noSequence = $scope.parentScope.textAttachment.noSequence;
		var nameFile = $scope.parentScope.textAttachment.nameFile;
		var textValue = $scope.parentScope.textAttachment.text;
		if(nameFile == null || nameFile.length==0){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please input file name");
			return;
		}
		$scope.parentScope.saveTextAttachmentFacade(nameObject, textKey, noSequence, nameFile, textValue)
		.then(function(data){
			$scope.parentScope.loadAttachment(nameObject, textKey);
		});
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