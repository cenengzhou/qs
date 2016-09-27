mainApp.controller('AttachmentAddendumTextCtrl', ['$scope', 'modalStatus', 'modalParam', '$uibModalInstance', 'attachmentService',
                                            function($scope, modalStatus, modalParam, $uibModalInstance, attachmentService){
	$scope.status = modalStatus;
	$scope.parentScope = modalParam;
	
	$scope.cancel = function () {
		$uibModalInstance.close();
	};
	
	$scope.saveTextAttachment = function(){
		if($scope.parentScope.isAddTextAttachment === false){
			$scope.parentScope.saveTextAttachmentFacade(
					$scope.parentScope.nameObject, 
					$scope.parentScope.textKey, 
					$scope.parentScope.textAttachment.noSequence, 
					$scope.parentScope.textAttachment.nameFile, 
					$scope.parentScope.textAttachment.text)
			.then(function(data){
				$scope.parentScope.loadAttachment($scope.parentScope.nameObject, $scope.parentScope.textKey);
			});
		} else {
			$scope.parentScope.saveTextAttachmentFacade(
					$scope.parentScope.nameObject, 
					$scope.parentScope.textKey, 
					$scope.parentScope.textAttachment.noSequence, 
					$scope.parentScope.textAttachment.nameFile, 
					$scope.parentScope.textAttachment.text)
			.then(function(data){
				$scope.parentScope.loadAttachment($scope.parentScope.nameObject, $scope.parentScope.textKey);
			});
		}
	}
	
	$scope.tinymceOptions = {
		    plugins: [
		              'advlist autolink lists link textcolor colorpicker charmap',
		              'print preview hr searchreplace wordcount insertdatetime ',
		              'nonbreaking save table contextmenu directionality paste textpattern '
		              ],
              removed_menuitems:'newdocument visualaid ',
              toolbar: 'save | print preview | undo redo | bold italic | forecolor backcolor | alignleft aligncenter alignright | bullist numlist outdent indent | link ', 
              save_onsavecallback: $scope.saveTextAttachment,
              save_enablewhendirty: false,
              statusbar: true,
              height: 350,
              skin: 'tinymce_charcoal'
};
	
}]);