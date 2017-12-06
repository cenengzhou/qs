mainApp.controller('NavigationCtrl', ['$scope', '$timeout', '$window', 'userpreferenceService', 'transitService', '$cookies', '$location',
	function($scope, $timeout, $window, userpreferenceService, transitService, $cookies, $location) {

	$scope.jobNo = $cookies.get("jobNo");
	checkUserNotiifcationSetting();
	detectBrowser();	

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
	
	$scope.navigateToTransit = function(){
		transitService.getTransit($cookies.get("jobNo"))
		.then(
				function(transit){
						if(transit){
							if(transit.status === 'Header Created'){
								$location.path('/transit/userGuide');
				    		} else if(transit.status === 'BQ Items Imported'){
				    			$location.path('/transit/BQ');
				    		} else if(transit.status === 'Resources Imported' || transit.status === 'Resources Updated') {
				    			$location.path('/transit/resources');
				    		} else if(transit.status === 'Resources Confirmed') {
				    			$location.path('/transit/confirm');
				    		} else if(transit.status === 'Report Printed') {
				    			$location.path('/transit/report');
				    		}else if(transit.status === 'Transit Completed') {
				    			$location.path('/transit/complete');
				    		}else
								$location.path('/transit/userGuide');
						}else
							$location.path('/transit/userGuide');
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
				title: 'Announcement',
				/*text: 'Do you know we have just updated the system? You can find out more in the Annoucement Board!',*/
				text: 'YEAR END CUTOFF 2017 has been released. You can find out more in the Annoucement Board!',
				image: 'image/new2.png',
				sticky: false,
				time: '8000'
			});
			return false;

		}, 1000);

	}
	
	function detectBrowser(){
		//var isChrome = !!window.chrome && !!window.chrome.webstore;
		// Internet Explorer 6-11
		var isIE = /*@cc_on!@*/false || !!document.documentMode;
		// Edge 20+
		var isEdge = !isIE && !!window.StyleMedia;
		if(isIE || isEdge){
			$timeout(function(){
				$.gritter.add({
					title: 'Alert',
					text: 'IE may slow down the performance. Please use Chrome!',
					image: 'image/info.png',
					sticky: false,
					time: '15000'
				});
				return false;

			}, 500);
		}
	}
	
}]);