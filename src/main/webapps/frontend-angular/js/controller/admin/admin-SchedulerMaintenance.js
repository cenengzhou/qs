mainApp.controller('AdminSchedulerMaintenanceCtrl', 
		['$scope', '$http', 'SessionHelper', 'modalService', '$sce', 'systemService', 'GlobalParameter',
		function($scope, $http, SessionHelper, modalService, $sce, systemService, GlobalParameter) {
			$scope.GlobalParameter = GlobalParameter;
			$scope.onSubmit = function(){
				$scope.datetimeToTimestamp();
				var triggerMap = {}
				var qrtzList = [];
				var cronList = [];
				$scope.triggers.forEach(function(trigger){
					qrtzList.push($scope.allTriggers[trigger.triggerName]['qrtz']);
					cronList.push($scope.allTriggers[trigger.triggerName]['cron']);
				})
				triggerMap['qrtz'] = qrtzList;
				triggerMap['cron'] = cronList;
				systemService.updateQrtzTriggerList(triggerMap)
				.then(function(data){
					if(data == ''){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Schedule updated.");
					} else {
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data.replace(/\n/g, '<br>'));
					}
				});
			};
			$scope.getAllTriggers = function() {
				systemService.getAllTriggers().then(function(data) {
					if (data instanceof Object) {
						$scope.allTriggers = {}
						$scope.triggers = data['quartzTrigger'];
						$scope.crons = data['cronTrigger'];
						$scope.addNextFireDatetime();
						$scope.triggers.forEach(function(trigger){
							if(!$scope.allTriggers[trigger.triggerName])  $scope.allTriggers[trigger.triggerName] = {};
							$scope.allTriggers[trigger.triggerName]['qrtz'] = trigger;
						});
						$scope.crons.forEach(function(cron){
							if(!$scope.allTriggers[cron.triggerName])  $scope.allTriggers[cron.triggerName] = {};
							$scope.allTriggers[cron.triggerName]['cron'] = cron;
						});
					};
				});
			};
			
			$scope.addNextFireDatetime = function(){
				angular.forEach($scope.triggers, function(trigger){
					trigger.nextFireDatetime = moment(trigger.nextFireTime).format(GlobalParameter.MOMENT_DATETIME_FORMAT);
				});
			}
			
			$scope.datetimeToTimestamp = function(){
				angular.forEach($scope.allTriggers, function(trigger){
					trigger.qrtz.nextFireTime = Date.parse(trigger.qrtz.nextFireDatetime);
					if(isNaN(trigger.qrtz.nextFireTime)) trigger.qrtz.nextFireTime = Date.parse(trigger.qrtz.nextFireDatetime.replace(' ','T'));
				});
			}

			$scope.auditTables = [];
			$scope.loadAuditTableMap = function(){
				systemService.getAuditTableMap()
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
			
		$scope.loadDatePickerFn = function(){
			angular.element('input[name$=".singleDate"').daterangepicker({
			    singleDatePicker: true,
			    showDropdowns: true,
			    timePicker: true,
			    timePicker24Hour: true,
			    autoApply: true,
				locale: {
				      format: GlobalParameter.MOMENT_DATETIME_FORMAT
				    },

			});
		}
		
		$scope.openDropdown = function( $event){
			angular.element('input[name="' + $event.currentTarget.nextElementSibling.name + '"').click();
		}



}]);

