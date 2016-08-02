mainApp.controller('AdminSchedulerMaintenanceCtrl', 
		['$scope', '$rootScope', '$http', 'SessionHelper', 'modalService', 'quartzService', '$sce',
		function($scope, $rootScope, $http, SessionHelper, modalService, quartzService, $sce) {
	
			$scope.onSubmit = function(){
				for(var i = 0; i<$scope.triggers.length;i++){
					if($scope.triggers[i].nextFireTime.toString().indexOf(':')> -1){
						$scope.triggers[i].nextFireTime = Date.parse($scope.triggers[i].nextFireTime);
					}
				};
				quartzService.updateQrtzTriggerList($scope.triggers)
				.then(function(data){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Schedule updated.");
				},function(data){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data );
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
			
			$scope.scheduleTip = $sce.trustAsHtml('<table width=150 class="table table-bordered table-condensed"><tr class="info"><th>Table Name</th><th>Housekeeping</th><th>Records&nbsp;old&nbsp;than</th></tr>');
			$scope.scheduleTip +=$sce.trustAsHtml('<tr><td>ADDENDUM</td><td>Active</td><td>1 year</td></tr>');
			$scope.scheduleTip +=$sce.trustAsHtml('<tr><td>ADDENDUM_DETAIL</td><td>Active</td><td>6 months</td></tr>');
			$scope.scheduleTip +=$sce.trustAsHtml('<tr><td>JOB_INFO</td><td>Inactive</td><td>1 year</td></tr>');
			$scope.scheduleTip +=$sce.trustAsHtml('<tr><td>PAYMENT_CERT</td><td>Active</td><td>1 year</td></tr>');
			$scope.scheduleTip +=$sce.trustAsHtml('<tr><td>PAYMENT_CERT_DETAIL</td><td>Active</td><td>6 months</td></tr>');
			$scope.scheduleTip +=$sce.trustAsHtml('<tr><td>RESOURCE_SUMMARY</td><td>Active</td><td>6 months</td></tr>');
			$scope.scheduleTip +=$sce.trustAsHtml('<tr><td>SUBCONTRACT</td><td>inactive</td><td>1 year</td></tr>');
			$scope.scheduleTip +=$sce.trustAsHtml('<tr><td>SUBCONTRACT_DETAIL</td><td>Active</td><td>6 months</td></tr></table>');

		}]);

