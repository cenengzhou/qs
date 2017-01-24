mainApp.controller('ExcelUploadModalCtrl', 
			['$scope', 'modalStatus', 'modalParam', '$uibModalInstance', 
	function($scope, modalStatus, modalParam, $uibModalInstance){
	$scope.status = modalStatus;
	$scope.statusArray = modalParam;
	$scope.cancel = function () {
		$uibModalInstance.close();
	};
		
	$scope.importedBody = {name:'imported', show:true};
	$scope.ignoredBody = {name:'ignored', show:true};
	$scope.nochangeBody = {name:'nochange', show:true};
	
	$scope.panelList = [$scope.importedBody, $scope.ignoredBody, $scope.nochangeBody];
	$scope.showPanel = function(panel){
		angular.forEach($scope.panelList, function(p){
			if(p.name !== panel && p.show === true){
				$scope.flipVisiblity(p);
			} else if(p.name === panel && $scope.currentPanel !== undefined){
				p.show = !p.show;
			}
		})
		$scope.currentPanel = panel;
	}
	$scope.flipVisiblity = function(s){
		s.show = false;
		angular.element('#'+s.name+'Body').slideToggle();
	}

}]);