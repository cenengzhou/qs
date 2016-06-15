mainApp.controller('SubcontractVendorModalCtrl', ['$scope', '$uibModalInstance', '$location', 'modalService', function($scope, $uibModalInstance, $location, modalService) {
	var vendorJson = [
		    {
		        "vendorNo": "31347",
		        "vendorName": "VSL Hong Kong Ltd.",
		        "subcontractSum": 782941845.64,
		        "status": "500"	
		    },
		    {
		        "vendorNo": "34301",
		        "vendorName": "Welcome Construction Company Limited",
		        "subcontractSum": 782941845.64,
		        "status": "500"	
		    },
		    {
		        "vendorNo": "34301",
		        "vendorName": "Welcome Construction Company Limited",
		        "subcontractSum": 782941845.64,
		        "status": "500"	
		    },
		    {
		        "vendorNo": "NaN"
		    }
    ];
    $scope.vendors = vendorJson;
    
   
    $scope.searchquery = '';
    
    $scope.openVendorFeedbackModal = function(){
    	modalService.open('lg', 'view/subcontract/modal/subcontract-vendor-feedback.html', 'SubcontractVendorFeedbackModalCtrl');
    };
    

    //Save Function
    $scope.save = function () {
    	$location.path("/subcontract-flow");
    	$uibModalInstance.close();
    };

    $scope.cancel = function () {
    	$uibModalInstance.dismiss("cancel");
    };
    
  //Listen for location changes and call the callback
    $scope.$on('$locationChangeStart', function(event){
   		 $uibModalInstance.close();
    });
	
}]);



