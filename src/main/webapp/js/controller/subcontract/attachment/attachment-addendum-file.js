mainApp.controller('AttachmentAddendumFileCtrl', ['$scope', '$location','attachmentService', 'modalService', '$cookies', '$http', '$window', '$stateParams', 'GlobalParameter', 'GlobalHelper', 'addendumService',
                                         function($scope, $location, attachmentService, modalService, $cookies, $http, $window, $stateParams, GlobalParameter, GlobalHelper, addendumService) {
	
	$scope.jobNo = $cookies.get('jobNo');
	$scope.subcontractNo = $cookies.get('subcontractNo');
	$scope.addendumNo = $cookies.get('addendumNo');
	$scope.paymentCertNo = $cookies.get('paymentCertNo');
	$scope.nameObject = $stateParams.nameObject;
	$scope.offsetTop = $stateParams.offsetTop;
	$scope.insideContent = false;
	$scope.sequenceNo = 0;
	$scope.textKey = $scope.jobNo + '|' + $scope.subcontractNo + '|';
	$scope.hideButton = false;
	$scope.isUpdatable = false;
	$scope.loadAttachmentFacade = attachmentService.getAttachmentListForPCMS;
	$scope.uploadAttachmentFacade = attachmentService.uploadSCAttachment;
	$scope.deleteAttachmentFacade = attachmentService.deleteAttachment;
	$scope.saveTextAttachmentFacade = attachmentService.uploadTextAttachment;
	
	if($scope.nameObject === GlobalParameter['AbstractAttachment'].SCDetailsNameObject){
		$scope.textKey += $scope.addendumNo ? $scope.addendumNo : '-1';
		$scope.insideContent = true;
		$scope.loadAttachmentFacade = attachmentService.obtainAttachmentList;
		$scope.uploadAttachmentFacade = attachmentService.uploadAddendumAttachment;
		$scope.deleteAttachmentFacade = attachmentService.deleteAddendumAttachment;
		$scope.saveTextAttachmentFacade = attachmentService.uploadAddendumTextAttachment;
	} else if($scope.nameObject === GlobalParameter['AbstractAttachment'].SCPaymentNameObject){
		$scope.textKey += $scope.paymentCertNo;
		$scope.insideContent = true;
	}
	
    $scope.loadAttachment = function(nameObject, textKey){
    	if(textKey.split('|')[2] === '-1'){
    		$scope.hideButton = true;
    		modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'Addendum does not exist. Please create addendum title first.');
    		return;
    	}
    	$scope.loadAttachmentFacade(nameObject, textKey)
    	.then(function(data){
    				if(angular.isArray(data)){
    					processData(data);    					
    				}
    			});
    }
    
	$scope.onSubmitAttachmentUpload = function(f){
		var formData = new FormData();
		angular.forEach(f.files, function(file){
			formData.append('files', file);
		});
		formData.append('nameObject', $scope.nameObject);
		formData.append('textKey', $scope.textKey)
		formData.append('sequenceNo', $scope.sequenceNo+1);
		$scope.uploadAttachmentFacade(formData)
		.then(function(data){
			f.value = null;
			$scope.loadAttachment($scope.nameObject, $scope.textKey);
		});
	}
	
	$scope.deleteAttachment = function(){
		angular.forEach($scope.attachments, function(att){
			if(att.selectedAttachement === true){
				$scope.deleteAttachmentFacade($scope.nameObject,$scope.textKey, parseInt(att.noSequence))
				.then(function(data){
					$scope.loadAttachment($scope.nameObject, $scope.textKey);
					$scope.selectedAttachement = false;
				});
			}
		})
	}
	
    $scope.addTextAttachment = function(){
    	$scope.isAddTextAttachment = true;
    	$scope.textAttachment = {};
    	$scope.textAttachment.noSequence = $scope.sequenceNo + 1;
    	$scope.textAttachment.nameFile = "New Text";
		modalService.open('lg', 'view/subcontract/attachment/attachment-addendum-text.html', 'AttachmentAddendumTextCtrl', 'Success', $scope);
    }
    
    $scope.loadAttachment($scope.nameObject, $scope.textKey);
    
	$scope.checkSelected = function(){
		$scope.selectedAttachement = false;
		angular.forEach($scope.attachments, function(att){
			if(att.selectedAttachement === true){
				$scope.selectedAttachement = true;
				return;
			}
		})
	}
	
    $scope.attachmentClick = function(){
    	$scope.isAddTextAttachment = false;
    	if(this.attach.typeDocument === '5'){
//	    	console.log('file:'+$scope.attachServerPath+this.attach.fileLink);
	    	url = 'service/attachment/obtainAddendumFileAttachment?nameObject='+$scope.nameObject+'&textKey='+$scope.textKey+'&sequenceNo='+this.attach.noSequence;
	    	var wnd = $window.open(url, 'Download Attachment', '_blank');
    	} else {
    		$scope.textAttachment = this.attach;
    		modalService.open('lg', 'view/subcontract/attachment/attachment-addendum-text.html', 'AttachmentAddendumTextCtrl', 'Success', $scope);
    	}
    }
    
	function getAddendum(){
		if($scope.addendumNo)
		addendumService.getAddendum($scope.jobNo, $scope.subcontractNo, $scope.addendumNo)
		.then(
				function( data ) {
					$scope.addendum = data;
					if($scope.addendum && $scope.addendum.status == "PENDING")
						$scope.isUpdatable = true;
					else
						$scope.isUpdatable = false;
				});
	} getAddendum()
	
	function getUserByUsername (username){
		return $http.get('service/security/getUserByUsername?username='+username);
	}
    
    function processData(data){
    	$scope.attachments = data;
    	addAttachmentsData($scope.attachments);
    }
   
    function addAttachmentsData (d){
    	var index = 0;
       	angular.forEach(d, function(att){
       		att.selected = index;
       		$scope.sequenceNo = att.noSequence;
       		if(att.pathFile === null || att.pathFile === ' '){
       			att.fileIconClass = 'fa fa-2x fa-file-text-o'; 
       		} else {
       			var fileType = att.nameFile.substring(att.nameFile.length -4);
       			att.fileIconClass = GlobalHelper.attachmentIconClass(fileType);
       		}
    		att.user = {UserName: att.usernameCreated};
    		att.user.userIcon = 'resources/images/profile.png';
    		getUserByUsername(att.usernameCreated)
    		.then(function(response){
    			if(response.data instanceof Object){
    				att.user = response.data;
    				if(att.user.StaffID !== null){
    					att.user.userIcon = GlobalParameter.imageServerAddress+att.user.StaffID+'.jpg';
    				} else {
    					att.user.userIcon = 'resources/images/profile.png';
    				}
    				if(!att.user.UserName) att.user.UserName = att.usernameCreated;
    			}
    		});
    		index++;
    	});
    }
	
}]);