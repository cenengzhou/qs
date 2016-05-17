mainApp.controller('RepackagingCtrl', ['$scope', '$location', function($scope, $location) {

	$scope.totalBudget = 226789000;
	$scope.taBudget = 244789000;

	$scope.enableEditButton = function() {
		return false;
		 /*if (model.questions.length > 1) { // your question said "more than one element"
		   return true;
		  }
		  else {
		   return false;
		  }*/
		};
	
	$scope.display = {
			unlock : true,
			reset : false,
			update : false,
			snapshot : false,
			confirm: false
	};

	$scope.click = function(view) {
		//console.log("view: "+view);
		$scope.display.unlock = (view == "unlock");
		$scope.display.reset = (view == "reset");
		$scope.display.update = (view == "update");
		$scope.display.snapshot = (view == "snapshot");
		$scope.display.confirm = (view == "confirm");


		if(view=="unlock"){
			$scope.enableEditButton = true;
		//	modalService.open('lg', 'view/subcontract/modal/subcontract-create.html', 'SubcontractCreateModalCtrl');
		}else if (view=="reset"){
		//	modalService.open('lg', 'view/subcontract/subcontract-ta.html', 'SubcontractTACtrl');
		}else if (view=="update"){
			$location.path("/repackaging-update");
		}else if (view=="snapshot"){
		//	modalService.open('lg', 'view/subcontract/modal/subcontract-vendor-compare.html', 'SubcontractVendorCompareModalCtrl');
		}else if (view=="confirm"){
		//	$location.path("/subcontract/dashboard");
		}
	};


	$scope.partialDownloadLink = 'http://localhost:8080/QSrevamp2/download?filename=';
    $scope.filename = '';

    $scope.uploadFile = function() {
    	console.log("Upload file");
        $scope.processDropzone();
    };

    $scope.reset = function() {
    	console.log("Reset file");
        $scope.resetDropzone();
    };
	
	
	
	
}]);