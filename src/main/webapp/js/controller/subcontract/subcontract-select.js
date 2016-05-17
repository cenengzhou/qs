mainApp.controller('SubcontractSelectCtrl', ['$scope', '$uibModal', '$log', 'modalService', '$animate', 'subcontractService', 
                                             function($scope, $uibModal, $log, modalService, $animate, subcontractService) {
    
    loadSubcontractList();
    
    function loadSubcontractList() {
    	subcontractService.obtainSubcontractList()
   	 .then(
			 function( data ) {
				 $scope.subcontracts= data;
				 /*var file = new Blob([data], {type: 'application/pdf'});
				    var fileURL = URL.createObjectURL(file);
				    window.open(fileURL);*/
			 });
    }
    
   
    $scope.searchquery = '';
    
    $scope.removeDefaultAnimation = function (){
        $animate.enabled(false);
    };
    

    $scope.openSubcontractCreateModal = function () {
    	modalService.open('lg', 'view/subcontract/modal/subcontract-create.html', 'SubcontractCreateModalCtrl');
    };

    
}]);


//Customized Filter Function for selected columns
/*mainApp.filter('search', function($filter) {
  return function(subcontracts, searchquery) {
    if (!searchquery) return subcontracts;
    var arrSearch = searchquery.split(' '),
        lookup = '',
        result = [];
      
      arrSearch.forEach(function(item) {
          lookup = $filter('filter')(subcontracts, {'packageNo': item});
          console.log(lookup);
          if (lookup.length > 0) result = result.concat(lookup);
      });
    
    return result;
  };
});*/

