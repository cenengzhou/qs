mainApp.controller('AttachmentMainCtrl', ['$scope', '$location','attachmentService', 'modalService', 'confirmService', '$cookies', '$http', '$window', '$stateParams', 'GlobalParameter', 'GlobalHelper', 'GlobalMessage', 'jobService', 'paymentService', 'addendumService', 'subcontractService', 'transitService', 'mainCertService',
                                         function($scope, $location, attachmentService, modalService, confirmService, $cookies, $http, $window, $stateParams, GlobalParameter, GlobalHelper, GlobalMessage, jobService, paymentService, addendumService, subcontractService, transitService, mainCertService) {
	/*
	 * attachment within ADDRESS BOOK modal is not control by this controller
	 * because the modal is not manage by state of ui-router so that cannot inject with ui-view
	 */
	
	$scope.GlobalParameter = GlobalParameter;
	if(!$scope.nameObject) {
		$scope.inBox = false;
		$scope.nameObject = $stateParams.nameObject;
	} else {		
		$scope.inBox = true;
		$scope.cancel = function () {
			$scope.uibModalInstance.close();
		};
	}
	$scope.jobNo = $cookies.get('jobNo');
	$scope.mainCertNo = $cookies.get("mainCertNo");
	
	$scope.subcontractNo = $cookies.get('subcontractNo');
	$scope.addendumNo = $cookies.get('addendumNo');
	$scope.paymentCertNo = $cookies.get('paymentCertNo');
	$scope.mainCertNo = $cookies.get('mainCertNo');
	$scope.offsetTop = $stateParams.offsetTop;
	$scope.insideContent = false;
	$scope.sequenceNo = 0;
	$scope.textKey = $scope.jobNo + '|' + $scope.subcontractNo + '|';
	$scope.hideButton = false;
//	$scope.isUpdatable = false;
	$scope.loadAttachmentFacade = attachmentService.obtainAttachmentList;
	$scope.uploadAttachmentFacade = attachmentService.uploadAttachment;
	$scope.deleteAttachmentFacade = attachmentService.deleteAttachment;
	$scope.saveTextAttachmentFacade = attachmentService.uploadTextAttachment;
	$scope.loadAttachment = loadAttachment;
	$scope.hideButton = false;
	switch($scope.nameObject){
		case GlobalParameter['AbstractAttachment'].JobInfoNameObject:
			$scope.textKey = $scope.jobNo;
			checkJobInfoUpdatable();
			loadAttachment($scope.nameObject, $scope.textKey);
			break;
		case GlobalParameter['AbstractAttachment'].AddendumNameObject:
			if($scope.addendumNo){
				$scope.textKey += $scope.addendumNo;
			} else {
				$scope.textKey += '-1';
				$scope.error_msg = 'Addendum does not exist. Please create addendum title first.';
				$scope.hideButton = true;
			}
			$scope.insideContent = true;
			checkAddendumUpdatable();
			loadAttachment($scope.nameObject, $scope.textKey);
			break;
		case GlobalParameter['AbstractAttachment'].SCPaymentNameObject:
			$scope.textKey += $scope.paymentCertNo;
			$scope.insideContent = true;
			checkPaymentCertUpdatable();
			loadAttachment($scope.nameObject, $scope.textKey);
			break;
		case GlobalParameter['AbstractAttachment'].SplitNameObject:
			$scope.isUpdatable = !$scope.disableButtons;
			loadAttachment($scope.nameObject, $scope.textKey);
			break;
		case GlobalParameter['AbstractAttachment'].TerminateNameObject:
			$scope.isUpdatable = !$scope.disableButtons;
			loadAttachment($scope.nameObject, $scope.textKey);
			break;
		case GlobalParameter['AbstractAttachment'].MainCertNameObject:
			$scope.textKey += $scope.mainCertNo;
			checkMainCertUpdatable();
			loadAttachment($scope.nameObject, $scope.textKey);
			break;
		case GlobalParameter['AbstractAttachment'].RepackagingNameObject:
			$scope.$parent.getLatestRepackaging()
			.then(function(response){
				if(response.repackaging){
					$scope.textKey += response.repackaging.id;
					loadAttachment($scope.nameObject, $scope.textKey);
				} else {
					$scope.textKey += '-1';
					$scope.error_msg = 'Please generate resource summary first.';
					$scope.hideButton = true;
				}
			});
			break;
		case GlobalParameter['AbstractAttachment'].TransitNameObject:
			checkTransitStatus();
			loadAttachment($scope.nameObject, $scope.textKey);
			break;
		default:
			checkSubcontractUpdatable();
			loadAttachment($scope.nameObject, $scope.textKey);
			break;
	}

    function loadAttachment(nameObject, textKey){
    	if(textKey.split('|')[2] === '-1'){
    		$scope.hideButton = true;
    		modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', $scope.error_msg);
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
		confirmService.show({}, {bodyText:GlobalMessage.deleteAttachment})
		.then(function(response){
			if(response === 'Yes')
			angular.forEach($scope.attachments, function(att){
				if(att.selectedAttachement === true){
					$scope.deleteAttachmentFacade($scope.nameObject,$scope.textKey, parseInt(att.noSequence))
					.then(function(data){
						$scope.loadAttachment($scope.nameObject, $scope.textKey);
						$scope.selectedAttachement = false;
					});
				}
			});
		});
	}
	
    $scope.addTextAttachment = function(){
    	$scope.isAddTextAttachment = true;
    	$scope.textAttachment = {};
    	$scope.textAttachment.noSequence = $scope.sequenceNo + 1;
    	$scope.textAttachment.nameFile = "New Text";
    	$scope.isTextUpdatable = true;
		modalService.open('lg', 'view/attachment/attachment-text-editor.html', 'AttachmentTextEditorCtrl', 'Success', $scope);
    }
    
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
	    	url = 'service/attachment/obtainFileAttachment?nameObject='+$scope.nameObject+'&textKey='+$scope.textKey+'&sequenceNo='+this.attach.noSequence;
	    	GlobalHelper.downloadFile(encodeURI(url));
    	} else {
    		$scope.textAttachment = this.attach;
    		$scope.isTextUpdatable = $scope.isUpdatable; 
    		modalService.open('lg', 'view/attachment/attachment-text-editor.html', 'AttachmentTextEditorCtrl', 'Success', $scope);
    	}
    }
    
    function checkJobInfoUpdatable(){
//    	jobService.getJob($scope.jobNo)
//    	.then(function(data){
//    		var jobInfo = jobInfo ? jobInfo : data;
//    		$scope.isUpdatable = jobInfo.completionStatus !='1' ? false : true;
//    	});
    	$scope.isUpdatable = true;
    }
    
    function checkMainCertUpdatable(){
    	if($scope.mainCertNo != null && $scope.mainCertNo.length > 0){
    		mainCertService.getCertificate($scope.jobNo, $scope.mainCertNo)
    		.then(
    				function( data ) {
    					$scope.cert = data;
    					if($scope.cert.certificateStatus < 200){
    						$scope.disableButtons = false;
    					} else {
    						$scope.disableButtons = true;
    					}
    					$scope.isUpdatable = !$scope.disableButtons;
    				});
    	} else {
    		$scope.disableButtons = false;
    		$scope.isUpdatable = !$scope.disableButtons;
    	}
    }
    
	function checkAddendumUpdatable(){
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
	} 

    function checkSubcontractUpdatable(){
    	subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
		.then(function( data ) {
			if(angular.isObject(data)){
				$scope.subcontract = data;
				if($scope.subcontract.scStatus !="330" && $scope.subcontract.scStatus !="500")
					$scope.isUpdatable = true;
			}
		})
    }
    
   function checkPaymentCertUpdatable(){
    	paymentService.getPaymentCert($scope.jobNo, $scope.subcontractNo, $scope.paymentCertNo)
		.then(function( data ) {
			if(angular.isObject(data)){
				$scope.payment = data;
				if($scope.payment.paymentStatus == "PND")
					$scope.isUpdatable = true;
			}		
		})
    }

   function checkTransitStatus(){
   	transitService.getTransit($scope.jobNo)
		.then(
				function(transit){
					if(transit){
						if(transit.status === 'Transit Completed') {
							$scope.isUpdatable = false;
			    		}else
			    			$scope.isUpdatable = true;
						
					}

				});
   }
   
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
    		att.user = {fullname: att.usernameCreated};
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
    				if(!att.user.fullname) att.user.fullname = att.usernameCreated;
    			}
    		});
    		index++;
    	});
    }
	
}]);