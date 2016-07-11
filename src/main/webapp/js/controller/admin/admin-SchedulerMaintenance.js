mainApp.controller('AdminSchedulerMaintenanceCtrl', 
		['$scope', '$rootScope', '$http', 'SessionHelper', 'modalService', 'quartzService',
		function($scope, $rootScope, $http, SessionHelper, modalService, quartzService) {
	
			$scope.onSubmit = function(){
				for(var i = 0; i<$scope.triggers.length;i++){
					if($scope.triggers[i].nextFireTime.toString().indexOf(':')> -1){
						$scope.triggers[i].nextFireTime = Date.parse($scope.triggers[i].nextFireTime);
					}
				};
				quartzService.updateQrtzTriggerList($scope.triggers)
				.then(function(data){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Schedule updated.");;
				},function(data){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', data );
				});
			};
			$scope.getAllTriggers = function() {
				quartzService.getAllTriggers().then(function(data) {
					if (data instanceof Object) {
						$scope.triggers = data;
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

		}]);

