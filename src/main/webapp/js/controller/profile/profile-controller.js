mainApp.controller('ProfileCtrl', ['$scope', '$timeout', 'rootscopeService', 'userpreferenceService', 'blockUI',
							function($scope, $timeout, rootscopeService, userpreferenceService, blockUI) { 

	$scope.userIcon = 'resources/images/profile.png';
	rootscopeService.gettingUser()
	.then(function(response){
		$scope.user = response.user;
		if($scope.user.authType === 'Kerberos'){
			$scope.userIcon = iconPath;
		}
	});
	
	var iconArray = {} ;
	iconArray['ENQ'] = 'icofont-look'; //'icofont-safety-hat';
	iconArray['REVIEWER'] = 'icofont-job-search';//'icofont-worker-group';
	iconArray['QS'] = 'icofont-worker';
	iconArray['ADMIN'] = 'icofont-tools-bag';//'icofont-safety-hat-light';
	iconArray['IMS'] = 'icofont-engineer';
	iconArray['JOB_ALL'] = 'icofont-industries-alt-3';
	iconArray['JOB'] = 'icofont-industries-alt-4';
	var iconObjArray = {};
	for(key in iconArray){
		iconObjArray[key] = new RoleIcon(key);
	};
	function loadJobs(){
		userpreferenceService.gettingUserPreference()
		.then(function(response){
			$scope.defaultJobNo = response.userPreference.DEFAULT_JOB_NO;
		});
		
		if(!$scope.jobs){
			blockUI.start('Loading...');
			$timeout(function(){
			rootscopeService.gettingJobList('ONGOING_JOB_LIST')
			.then(function( response ) {
				$scope.jobs = response.jobs;
					$timeout(function(){
					rootscopeService.gettingJobList('COMPLETED_JOB_LIST')
					.then(function( response ) {
						$scope.jobs = $scope.jobs.concat(response.jobs);
						$scope.jobs.sort(function(a,b){return a.jobNo - b.jobNo;});
						blockUI.stop();
					});
					}, 150);
			});
			}, 150);
		}
		
	} loadJobs();
	
	$scope.settingDefaultJobNo = function(){
		userpreferenceService.settingDefaultJobNo($scope.defaultJobNo)
		.then(function(response){
			showUpdateDefaultJobStatus();
		})
	}
	
	function showUpdateDefaultJobStatus(){
		$scope.showDefaultJobStatus = true;
		$timeout(function(){
			$scope.showDefaultJobStatus = false;
		}, 5000);
	}
	
	function RoleIcon(type){
		this.type = type;
		this.iconClass = iconArray[type];
		if(!this.iconClass) this.iconClass = 'icofont-help-robot';
	}

	$scope.getRoleIcon = function(key){
		switch(key){
		case 'ROLE_QS_ENQ':
		case 'ROLE_PCMS_ENQ':
			return [iconObjArray['ENQ']];
			break;
		case 'ROLE_QS_REVIEWER':
		case 'ROLE_PCMS_QS_REVIEWER':
			return [iconObjArray['REVIEWER']];
			break;
		case 'ROLE_QS_QS':
		case 'ROLE_PCMS_QS':
			return [iconObjArray['QS']];
			break;
		case 'ROLE_QS_QS_ENQ':
		case 'ROLE_PCMS_QS_ENQ':
			return [iconObjArray['ENQ'], iconObjArray['QS']];
			break;
		case 'ROLE_QS_QS_ADM':
		case 'ROLE_PCMS_QS_ADMIN':
			return [iconObjArray['ADMIN'], iconObjArray['QS']];
			break;
		case 'ROLE_QS_IMS_ENQ':
		case 'ROLE_PCMS_IMS_ENQ':
			return [iconObjArray['ENQ'], iconObjArray['IMS']];
			break;
		case 'ROLE_QS_IMS_ADM':
		case 'ROLE_PCMS_IMS_ADMIN':
			return [iconObjArray['ADMIN'], iconObjArray['IMS']];
			break;
		case 'JOB_ALL':
			return [iconObjArray['JOB_ALL']];
			break;
		default:
			return [iconObjArray['JOB']];
			break;
		}
	}
	
}]);