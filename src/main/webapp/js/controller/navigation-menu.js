mainApp.controller('NavigationCtrl', ['$scope', '$timeout', '$window', 'userpreferenceService',
	function($scope, $timeout, $window, userpreferenceService) {

	checkUserNotiifcationSetting();
	

	$scope.openAnnouncement = function(){
		//$window.location.href = 'ad';
		 $window.open('ad/announcement.html', '_blank');
		 $scope.updateNotificationReadStatus();
		 
	}

	$scope.updateNotificationReadStatus = function(){
		userpreferenceService.updateNotificationReadStatusByCurrentUser('Y')
		.then(function(msg){
			$scope.readStatus = "Read";
		});
	}
	
	
	function checkUserNotiifcationSetting(){
		userpreferenceService.getNotificationReadStatusByCurrentUser()
		.then(function(status){
			$scope.status = status;
			if(status == 'Y')
				$scope.readStatus = "Read";
			else
				$scope.readStatus = "Not Read";
			
			if(status == null || status.length ==0){
				userpreferenceService.insertNotificationReadStatusByCurrentUser()
				.then(function(msg){
					showNotiifcation();
				});
				
			}else if(status == 'N')
				showNotiifcation();
		});
	}
	
	function showNotiifcation(){
		$timeout(function(){
			$.gritter.add({
				title: 'Announcement: Year End Cutoff!',
				text: 'Please view the details in the announcement board.',
				image: 'image/new2.png',
				sticky: false,
				time: '8000'
			});
			return false;

		}, 1000);

	}
	
}]);