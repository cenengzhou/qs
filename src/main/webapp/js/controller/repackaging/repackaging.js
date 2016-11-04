mainApp.controller('RepackagingCtrl', ['$state', '$scope', '$location', '$cookies', '$uibModal', 'confirmService', 'repackagingService', 'resourceSummaryService', 'modalService', 'attachmentService', '$http', '$window', '$state', '$rootScope', 'rootscopeService', 'GlobalParameter', 'GlobalHelper', 'GlobalMessage',
                                       function($state, $scope, $location, $cookies, $uibModal, confirmService, repackagingService, resourceSummaryService, modalService, attachmentService, $http, $window, $state, $rootScope, rootscopeService, GlobalParameter, GlobalHelper, GlobalMessage) {
	rootscopeService.setSelectedTips('repackagingStatus');
	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");

	$scope.repackaging = "";
	$scope.sequenceNo = 0;
	$scope.imageServerAddress = GlobalParameter.imageServerAddress;
	$scope.selectedAttachement = false;
	$scope.isAddTextAttachment = false;
	
	$scope.latestVersion = false;
	getLatestRepackaging();

	$scope.click = function(view) {
		if(view=="generateResourceSummaries"){
			generateResourceSummaries();
		}
		else if(view=="unlock"){
			addRepackaging();
		}else if (view=="reset"){
			deleteRepackaging();
		}else if (view=="snapshot"){
			generateSnapshot();
		}else if (view=="email"){
			composeEmail();
		}else if (view=="confirm"){
			modalService.open('lg', 'view/repackaging/modal/repackaging-confirm.html', 'RepackagingConfirmModalCtrl',  $scope.latestVersion, $scope.repackaging.id);
		}
	};

	$scope.getHistory = function() {
		modalService.open('lg', 'view/repackaging/modal/repackaging-history.html', 'RepackagingHistoryModalCtrl');
	};

	$scope.updateRemarks = function(){
		updateRepackaging();
	}

	$rootScope.$on("GetSelectedRepackagingVersion", function(event, repackagingId){
		repackagingService.getRepackagingEntry(repackagingId)
		.then(
				function( data ) {
					$scope.repackaging = data;
					
					if($scope.repackaging.status != '900') $scope.isUpdatable = true;
					if($scope.repackaging.id != null){
						$scope.loadAttachment($scope.repackaging.id);
						
						if($scope.repackaging.id == $scope.latestID){
							$scope.latestVersion = true;
						}else 
							$scope.latestVersion = false;
					}
				});
      });
	
	function getLatestRepackaging() {
		repackagingService.getLatestRepackaging($scope.jobNo)
		.then(
				function( data ) {
					if(data != null){
						$scope.repackaging = data;
						$scope.latestVersion = true;
						$scope.latestID = $scope.repackaging.id;
						$cookies.put('repackagingId', $scope.repackaging.id);
						if($scope.repackaging.status != '900') $scope.isUpdatable = true;
						$scope.loadAttachment($scope.repackaging.id);
						
						console.log($scope.latestVersion);
						console.log($scope.repackaging.status);
						
						
					}
				});

	}

	$scope.loadAttachment = function(repackagingEntryID){
		attachmentService.getRepackagingAttachments(repackagingEntryID)
		.then(
				function(data){
					if(angular.isArray(data)){
						$scope.repackagingAttachments = data;
						$scope.addAttachmentsData($scope.repackagingAttachments);
					}
				})
	}

	$scope.addAttachmentsData = function(d){
		var index = 0;
		angular.forEach(d, function(att){
			att.selected = index;
			$scope.sequenceNo = att.sequenceNo;
			if(att.fileLink === null){
				att.fileIconClass = 'fa fa-2x fa-file-text-o'; 
			} else {
				var fileType = att.fileName.substring(att.fileName.length -4);
				att.fileIconClass = GlobalHelper.attachmentIconClass(fileType);
			}
			att.user = {};
			att.user.userIcon = 'resources/images/profile.png';
			$scope.getUserByUsername(att.createdUser)
			.then(function(response){
				if(response.data instanceof Object){
					att.user = response.data;
					if(att.user.StaffID !== null){
						att.user.userIcon = GlobalParameter.imageServerAddress+att.user.StaffID+'.jpg';
					} else {
						att.user.userIcon = 'resources/images/profile.png';
					}
				}
			});
			index++;
		});
	}

	$scope.attachmentClick = function(){
		$scope.isAddTextAttachment = false;
		if(this.attach.documentType === 5){
			url = 'service/attachment/downloadRepackagingAttachment?repackagingEntryID='+$scope.repackaging.id+'&sequenceNo='+this.attach.sequenceNo;
			var wnd = $window.open(url, 'Download Attachment', '_blank');
		} else {
			$scope.repackaging.attachment = this.attach;
			$scope.isTextUpdatable = $scope.isUpdatable; 
			modalService.open('lg', 'view/repackaging/modal/repackaging-textattachment.html', 'RepackagingTextAttachmentCtrl', 'Success', $scope);
		}
	}

	$scope.addTextAttachment = function(){
		$scope.isAddTextAttachment = true;
		$scope.repackaging.attachment = {};
		$scope.repackaging.attachment.sequenceNo = $scope.sequenceNo + 1;
		$scope.repackaging.attachment.fileName = "New Text";
		$scope.isTextUpdatable = true; 
		modalService.open('lg', 'view/repackaging/modal/repackaging-textattachment.html', 'RepackagingTextAttachmentCtrl', 'Success', $scope);
	}

	$scope.getUserByUsername = function(username){
		return $http.get('service/security/getUserByUsername?username='+username);
	}

	$scope.onSubmitAttachmentUpload = function(f){
		var formData = new FormData();
		angular.forEach(f.files, function(file){
			formData.append('files', file);
		});
		formData.append('repackagingEntryID', $scope.repackaging.id);
		formData.append('sequenceNo', $scope.sequenceNo+1);
		attachmentService.uploadRepackingAttachment(formData)
		.then(function(data){
			f.value = null;
			$scope.loadAttachment($scope.repackaging.id);
		});
	}

	$scope.checkSelected = function(){
		$scope.selectedAttachement = false;
		angular.forEach($scope.repackagingAttachments, function(att){
			if(att.selectedAttachement === true){
				$scope.selectedAttachement = true;
				return;
			}
		})
	}

	$scope.deleteAttachment = function(){
		confirmService.show({}, {bodyText:GlobalMessage.deleteAttachment})
		.then(function(response){
			if(response === 'Yes')
			angular.forEach($scope.repackagingAttachments, function(att){
				if(att.selectedAttachement === true){
					attachmentService.deleteRepackagingAttachment(parseInt($scope.repackaging.id), parseInt(att.sequenceNo))
					.then(function(data){
						$scope.loadAttachment($scope.repackaging.id);
						$scope.selectedAttachement = false;
					});
				}
			});
		});
	}

	function addRepackaging() {
		repackagingService.addRepackaging($scope.jobNo)
		.then(
				function( data ) {
					getLatestRepackaging();
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Repackaging has been unlocked.");
					}
				});
	}

	function updateRepackaging() {
		repackagingService.updateRepackaging($scope.repackaging)
		.then(
				function( data ) {
					getLatestRepackaging();
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Remarks has been updated.");
					}
				});
	}



	function deleteRepackaging() {
		repackagingService.deleteRepackaging($scope.repackaging.id)
		.then(
				function( data ) {
					getLatestRepackaging();
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Repackaging has been reset.");
					}
				});
	}

	function generateSnapshot() {
		repackagingService.generateSnapshot($scope.repackaging.id, $scope.jobNo)
		.then(
				function( data ) {
					getLatestRepackaging();
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Snapshot has been generated.");
					}
				});
	}

	function composeEmail(){
//		modalService.open('lg', 'view/repackaging/repackaging-email.html', 'RepackagingEmailCtrl', 'Success', $scope);
//		$uibModal.open({
//			  templateUrl: 'view/email-modal.html',
//			  scope: $scope
//			});
		$state.go('repackaging-email');
	}
	
	function generateResourceSummaries() {
		resourceSummaryService.generateResourceSummaries($scope.jobNo)
		.then(
				function( data ) {
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Resource summaries have been generated.");
						$state.reload();
					}
				});
	}

	

	
	//Attachment
	/*$scope.partialDownloadLink = 'http://localhost:8080/QSrevamp2/download?filename=';
    $scope.filename = '';

    $scope.uploadFile = function() {
    	console.log("Upload file");
        $scope.processDropzone();
    };

    $scope.reset = function() {
    	console.log("Reset file");
        $scope.resetDropzone();
    };*/




}]);