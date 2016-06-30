mainApp.controller("RepackagingAddModalCtrl", ['$scope', '$uibModalInstance', 'modalParam', 'resourceSummaryService', 'modalService', '$cookieStore', '$state',
                                               function ($scope, $uibModalInstance, modalParam, resourceSummaryService, modalService, $cookieStore, $state) {

	$scope.repackagingEntryId = modalParam;

	$scope.resourceSummary = {
			subsidiaryCode : "29999999",
			objectCode : "140299",
			resourceDescription : "Testing",
			unit : "AM",
			quantity : 100,
			rate : 1,

			resourceType : "OI",
			//excludeLevy : false,
			//excludeDefect : false,


			//amount : "",
	}



	//Save Function
	$scope.save = function () {

		if (false === $('form[name="form-wizard-step-1"]').parsley().validate()) {
			event.preventDefault();  
			return;
		}

		addResourceSummary();
	};


	function addResourceSummary() {
		resourceSummaryService.addResourceSummary($cookieStore.get("jobNo"), $scope.repackagingEntryId, $scope.resourceSummary)
		.then(
				function( data ) {
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "New Resource has been added.");
						$uibModalInstance.close();
						$state.reload();
					}
				});
	}


	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
	};

	//Listen for location changes and call the callback
	$scope.$on('$locationChangeStart', function(event){
		$uibModalInstance.close();
	});

}]);

