mainApp.controller('JobAccountMasterCtrl', ['$scope','jdeService', 'jdeService', 'modalService',
                                            function($scope, jdeService, jdeService, modalService) {

	$scope.createAccountCode = function () {
		if (false === $('form[name="form-validate"]').parsley().validate()) {
			event.preventDefault();  
			return;
		}

		validateAndCreateAccountCode();
	};

	function validateAndCreateAccountCode(){
		jdeService.validateAndCreateAccountCode($scope.jobNo, $scope.objectCode, $scope.subsidiaryCode)
		.then(
				function( data ) {
					if(data.length>0)
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					else
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Account code has been created successfully.");

				});
	}

	$scope.createAccountCodeByJob = function (){
		jdeService.createAccountMasterByGroup($scope.jobNo, $scope.checkedResource, $scope.checkedResourceSummary, $scope.checkedSCDetails)
		.then(
				function( data ) {
					if(data.length>0)
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', data);
					else
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Account codes cannot be created.");

				});
	}


}]);

