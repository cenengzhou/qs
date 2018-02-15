mainApp.controller('TransitCtrl', ['$scope', 'modalService', 'transitService', '$cookies', '$rootScope',
	function($scope, modalService, transitService, $cookies, $rootScope) {
	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");

	$scope.causewayGuide = 'image/transit.bmp';
	$scope.candyGuide = 'image/candy.bmp';
	
	getTransit();

	
	function getTransit(){
		transitService.getTransit($scope.jobNo)
		.then(
				function(data){
					if(data){
						$scope.transitStatus = data.status;
						$rootScope.transitLocked = data.systemStatus == 'LOCKED';
					}

				});
	}

	$scope.openWindow = function (type){
		//if(type == 'Resource'){
			modalService.open('lg', 'view/transit/modal/code-match-enquiry-modal.html', 'CodeMatchEnquiryModalCtrl', type);
		//}else if(type == 'Unit'){
		//	modalService.open('lg', 'view/transit/modal/code-match-enquiry-modal.html', 'CodeMatchEnquiryModalCtrl', type);
		//}
	}
	
	$scope.editTransitHeader = function(){
		modalService.open('md', 'view/transit/modal/transit-header-modal.html', 'TransitHeaderModalCtrl');
	}
	
	$rootScope.$on("UpdateTransitStatus", function(event, transitStatus){
		$scope.transitStatus = transitStatus;
	});
	
}]);