mainApp.controller('RepackagingTextAttachmentCtrl', ['$scope', 'modalStatus', 'modalParam', '$uibModalInstance', 'repackagingService',
                                            function($scope, modalStatus, modalParam, $uibModalInstance, repackagingService){
	$scope.status = modalStatus;
	$scope.parentScope = modalParam;
	
	$scope.cancel = function () {
		$uibModalInstance.close();
	};
	
	$scope.saveRepackagingTextAttachment = function(){
		if($scope.parentScope.isAddTextAttachment === false){
			repackagingService.saveRepackagingTextAttachment(
					$scope.parentScope.repackaging.id, 
					$scope.parentScope.repackaging.attachment.sequenceNo, 
					$scope.parentScope.repackaging.attachment.fileName, 
					$scope.parentScope.repackaging.attachment.textAttachment)
			.then(function(data){
				$scope.parentScope.loadAttachment($scope.parentScope.repackaging.id);
			});
		} else {
			repackagingService.addRepackagingTextAttachment(
					$scope.parentScope.repackaging.id, 
					$scope.parentScope.repackaging.attachment.sequenceNo, 
					$scope.parentScope.repackaging.attachment.fileName, 
					$scope.parentScope.repackaging.attachment.textAttachment)
			.then(function(data){
				$scope.parentScope.loadAttachment($scope.parentScope.repackaging.id);
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
              save_onsavecallback: $scope.saveRepackagingTextAttachment,
              save_enablewhendirty: false,
              statusbar: true,
              height: 350,
              skin: 'tinymce_primary'
};
	
}]);