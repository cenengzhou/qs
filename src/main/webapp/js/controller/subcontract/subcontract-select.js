mainApp.controller('SubcontractSelectCtrl', ['$scope', '$uibModal', 'modalService', '$animate', 'subcontractService', '$cookies', '$window', 'rootscopeService',
                                             function($scope, $uibModal, modalService, $animate, subcontractService, $cookies, $window, rootscopeService) {
	rootscopeService.setSelectedTips('subcontractStatus');
	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");
	
	$scope.subcontractStatus = "";
	$scope.finalPaymentStatus ="";
	
    loadSubcontractList();
    
    function loadSubcontractList() {
    	subcontractService.getSubcontractList($scope.jobNo, false)
   	 .then(
			 function( data ) {
				 $scope.subcontracts= data;
			 });
    }
    
   
    $scope.searchquery = '';
    
    $scope.removeDefaultAnimation = function (){
        $animate.enabled(false);
    };
    
    $scope.updateFinalOption = function (){
    	console.log($scope.checkedFinal);
    	if($scope.checkedFinal == true)
        	$scope.finalPaymentStatus ="F";    		
    	else
        	$scope.finalPaymentStatus ="";	

    }

    $scope.updateSubcontract = function (subcontractNo, subcontractDescription, paymentStatus) {
    	$cookies.put('subcontractNo', subcontractNo);
    	$cookies.put('subcontractDescription', subcontractDescription);
    	$cookies.put('paymentStatus', paymentStatus);
    }
    
    $scope.openSubcontractCreateModal = function () {
    	modalService.open('lg', 'view/subcontract/modal/subcontract-create.html', 'SubcontractCreateModalCtrl', 'Create');
    };



    //Get filtered list
    /*$scope.filter =  function () {
    	for (var i = 0; i < $scope.filteredItems.length; i++) {
    		console.log($scope.filteredItems[i].createdDate);
		}
    };*/

    
}]);



//Customized Filter Function for selected columns
/*mainApp.filter('search', function($filter) {console.log("In Search");
  return function(subcontracts, searchquery) {
    if (!searchquery) return subcontracts;
    var arrSearch = searchquery.split(' '),
        lookup = '',
        result = [];
      
      arrSearch.forEach(function(item) {
          lookup = $filter('filter')(subcontracts, {'scStatus': item});
          console.log(lookup);
          if (lookup.length > 0) result = result.concat(lookup);
      });
    
    return result;
  };
});*/

