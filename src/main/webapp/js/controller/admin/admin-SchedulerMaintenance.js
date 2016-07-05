mainApp.controller('AdminSchedulerMaintenanceCtrl', 
		
		function($scope, $rootScope, $http, SessionHelper, modalService) {
			$scope.onSubmit = function(){
				for(var i = 0; i<$scope.triggers.length;i++){
					if($scope.triggers[i].nextFireTime.toString().indexOf(':')> -1){
						$scope.triggers[i].nextFireTime = Date.parse($scope.triggers[i].nextFireTime);
					}
				};
				$http.post('service/UpdateQrtzTriggerList', $scope.triggers)
				.then(function(response){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Schedule updated.");;
				},function(response){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Status:" + response.statusText );
				});
			};
			$scope.getAllTriggers = function() {
				$http.post('service/GetAllTriggers').then(function(response) {
					if (response !== null) {
						$scope.triggers = response.data;
//						for(var i = 0; i<$scope.triggers.length;i++){
//							$scope.triggers[i].nextFireTime = moment($scope.triggers[i].nextFireTime).format('DD MMM YYYY HH:mm');
//						};
					};
				});
			};
			
			$scope.getDate = function(timestampValue){
				return new Date(timestampValue);
			};
						
			$scope.setHover = function(trigger){
				$scope.currentHover = trigger;
			};
			
			$scope.isHover = function(trigger){
				return $scope.currentHover === trigger;
			};
			
			$scope.getAllTriggers();
			$scope.currentHover = -1;

		} );

