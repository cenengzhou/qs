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
		.then(function(status){
			if(status == 'Y')
				$scope.readStatus = "Read";
			else
				$scope.readStatus = "Not Read";
		});
	}
	
	
	function checkUserNotiifcationSetting(){
		userpreferenceService.getNotificationReadStatusByCurrentUser()
		.then(function(status){
			$scope.status = status;
		//	console.log(status);
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
				title: 'There is announcement about Year End Procedure!',
				text: 'Please view the details in the announcement board.',
				image: 'image/new2.png',
				sticky: false,
				time: '5000'
			});
			return false;

		}, 1000);

	}
	
}]);