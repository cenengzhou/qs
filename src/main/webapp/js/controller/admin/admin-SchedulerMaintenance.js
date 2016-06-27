mainApp.controller('AdminSchedulerMaintenanceCtrl', 
		[ '$scope', '$rootScope', '$http', 'SessionHelper',
		function($scope, $rootScope, $http, SessionHelper) {
			
			$scope.getAllTriggers = function() {
				$http.post('service/GetAllTriggers').then(function(response) {
					if (response !== null) {
						$scope.triggers = response.data;
						$scope.minDate = moment().format('DD MMM YYYY HH:mm');
						for(var i = 0; i<$scope.triggers.length;i++){
							console.log(triggers[i].triggerName + " nextFireTime:" + $scope.triggers[i].nextFireTime)
							$scope.triggers[i].nextFireTime_Datetime = moment($scope.triggers[i].nextFireTime).format('DD MMM YYYY HH:mm');
						}
					}
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

		} ]);

