mainApp.controller('AdminSchedulerMaintenanceCtrl', 
		[ '$scope', '$rootScope', '$http', 'SessionHelper',
		function($scope, $rootScope, $http, SessionHelper) {
			
			$scope.getAllTriggers = function() {
				$http.post('service/GetAllTriggers').then(function(data) {
					if (data !== null) {
						$scope.triggers = data.data;
						$scope.minDate = moment().format('DD MMM YYYY HH:mm');
						for(var i = 0; i<$scope.triggers.length;i++){							
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

