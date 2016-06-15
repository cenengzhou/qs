mainApp.controller('SubcontractSelectCtrl', ['$scope', '$uibModal', 'modalService', '$animate', 'subcontractService', '$cookieStore', '$window',
                                             function($scope, $uibModal, modalService, $animate, subcontractService, $cookieStore, $window) {
	
	$scope.jobNo = $cookieStore.get("jobNo");
	$scope.jobDescription = $cookieStore.get("jobDescription");
	
	$scope.subcontractStatus = "";
	
    loadSubcontractList();
    
    function loadSubcontractList() {
    	subcontractService.obtainSubcontractList($scope.jobNo)
   	 .then(
			 function( data ) {
				 $scope.subcontracts= data;
				 
				 //Download file				 
				 //$window.open("gammonqs/scPaymentDownload.smvc?jobNumber=13362&packageNumber=1006&paymentCertNo=5", "_blank", "");
				//$window.open("gammonqs/subcontractReportExport.rpt?company=&division=&jobNumber=13362&subcontractNumber=&subcontractorNumber=&subcontractorNature=&paymentType=&workScope=&clientNo=&includeJobCompletionDate=false&splitTerminateStatus=&month=&year=", "_blank", "");

			 });
    }
    
   
    $scope.searchquery = '';
    
    $scope.removeDefaultAnimation = function (){
        $animate.enabled(false);
    };
    

    $scope.updateSubcontractNo = function (subcontractNo, subcontractDescription) {
    	if(subcontractNo){
    		$cookieStore.put('subcontractNo', subcontractNo);
    		$cookieStore.put('subcontractDescription', subcontractDescription);
    	}
    }
    
    $scope.openSubcontractCreateModal = function () {
    	modalService.open('lg', 'view/subcontract/modal/subcontract-create.html', 'SubcontractCreateModalCtrl');
    };

    
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

