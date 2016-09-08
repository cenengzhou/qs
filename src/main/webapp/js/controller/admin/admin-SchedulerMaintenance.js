mainApp.controller('AdminSchedulerMaintenanceCtrl', 
		['$scope', '$rootScope', '$http', 'SessionHelper', 'modalService', 'quartzService', '$sce', 'audithousekeepService', 'GlobalParameter',
		function($scope, $rootScope, $http, SessionHelper, modalService, quartzService, $sce, audithousekeepService, GlobalParameter) {
			$scope.GlobalParameter = GlobalParameter;
			$scope.onSubmit = function(){
				$scope.datetimeToTimestamp();
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
						$scope.addNextFireDatetime();
					};
				});
			};
			
			$scope.addNextFireDatetime = function(){
				angular.forEach($scope.triggers, function(trigger){
					trigger.nextFireDatetime = moment(trigger.nextFireTime).format('DD MMM YYYY HH:mm');
				});
			}
			
			$scope.datetimeToTimestamp = function(){
				angular.forEach($scope.triggers, function(trigger){
					trigger.nextFireTime = Date.parse(trigger.nextFireDatetime);
				});
			}
			$scope.auditTables = [];
			$scope.loadAuditTableMap = function(){
				audithousekeepService.getAuditTableMap()
				.then(function(data){
					$scope.scheduleTip = $sce.trustAsHtml('<table width=150 class="table table-bordered table-condensed">');
					$scope.scheduleTip+= $sce.trustAsHtml('<tr class="info"><th>Table Name</th><th>Housekeeping</th><th>Records&nbsp;old&nbsp;than</th></tr>');
					angular.forEach(data, function(value, key){
						  $scope.auditTables.push({
						    name: key,
						    auditInfo: value
						  });
						  var oday = $scope.recordsOldThan(value.period);
						  $scope.scheduleTip +=$sce.trustAsHtml('<tr><td nowrap>'+value.tableName+'</td><td>'+(value.housekeep?'Active':'Inactive')+'</td>');
						  $scope.scheduleTip +=$sce.trustAsHtml('<td nowrap><a title="' + oday.title + '">'+oday.msg+'</a></td></tr>');
					});
					$scope.scheduleTip +=$sce.trustAsHtml('</table>');
				});
			}
			$scope.loadAuditTableMap();
			
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
			
			$scope.recordsOldThan = function(p){
				var periods = p.split('-');
				var pyear = periods[0];
				var pmonth = periods[1];
				var pday = periods[2];
				var result = {};
				var msg = '';
				var today = new Date();
				var period = new Date();
				period.setFullYear(today.getFullYear() - pyear);
				period.setMonth(today.getMonth() - pmonth);
				period.setDate(today.getDate() - pday);
				result.title = moment(period).format('DD MMM YYYY');
				msg += pyear > 0 ? pyear + ' years ' : '';
				msg += pmonth > 0 ? pmonth + ' months ' : '';
				msg += pday > 0 ? pday + ' days' : '';
				result.msg = msg;
				return result;
			}

		}]);

