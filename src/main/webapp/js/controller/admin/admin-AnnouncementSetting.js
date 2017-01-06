mainApp.controller('AnnouncementSettingCtrl', ['$scope', 'userpreferenceService', 'modalService',
							function($scope, userpreferenceService, modalService ) { 

	$scope.updateAnnouncentSetting = function (setting){
		userpreferenceService.updateAnnouncentSetting(setting)
		.then(function( result ) {
			if(result){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', result);				
			}else{
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Announcement Setting has been updated.");
			}
		});
	}
	
}]);