mainApp.controller('SubcontractFlowCtrl', ['$scope', '$location', 'colorCode', 'modalService', '$cookies', 
                                           function($scope, $location, colorCode, modalService, $cookies) {

	$scope.subcontractNo = $cookies.get("subcontractNo");
	$scope.subcontractDescription = $cookies.get("subcontractDescription");
	
	$scope.budget = 226789000;
	$scope.taBudget = 244789000;
	$scope.revenue = $scope.budget - $scope.taBudget;
	
	$scope.progress = 80;
	
	$scope.display = {
			scpackage : false,
			ta : false,
			vendor : false,
			compare : false,
			requisition: false,
			award: false
	};

	$scope.click = function(view) {
		$scope.display.scpackage = (view == "scpackage");
		$scope.display.ta = (view == "ta");
		$scope.display.vendor = (view == "vendor");
		$scope.display.compare = (view == "compare");
		$scope.display.requisition = (view == "requisition");
		$scope.display.award = (view == "award");

		if(view=="scpackage"){
			modalService.open('lg', 'view/subcontract/modal/subcontract-create.html', 'SubcontractCreateModalCtrl');
		}else if (view=="assignResources"){
			modalService.open('lg', 'view/repackaging/modal/repackaging-assign-resources.html', 'RepackagingAssignResourcesCtrl');
		}else if (view=="ta"){
			modalService.open('lg', 'view/subcontract/subcontract-ta.html', 'SubcontractTACtrl');
		}else if (view=="vendor"){
			modalService.open('lg', 'view/subcontract/modal/subcontract-vendor.html', 'SubcontractVendorModalCtrl');
		}else if (view=="compare"){
			modalService.open('lg', 'view/subcontract/modal/subcontract-vendor-compare.html', 'SubcontractVendorCompareModalCtrl');
		}else if (view=="requisition"){
			$location.path("/subcontract/dashboard");
		}else if (view=="award"){
			modalService.open('md', 'view/subcontract/modal/subcontract-award.html', 'SubcontractAwardeModalCtrl');
		}
	};


	
}]);