mainApp.controller('AddendumForm2Ctrl', ['$scope' , 'modalService', 'addendumService', 'subcontractService', '$stateParams', '$cookies', '$state', 
                                           function($scope ,modalService, addendumService, subcontractService, $stateParams, $cookies, $state) {

	$scope.addendumNo = $cookies.get('addendumNo');
	var addendumDetailHeaderRef = $cookies.get('addendumDetailHeaderRef');

	
	getAddendum();
	getAllAddendumDetails();
	
	$scope.save = function(){
		$scope.disableButtons = true;
		if (false === $('form[name="form-validate"]').parsley().validate()) {
			event.preventDefault();  
			$scope.disableButtons = false;
			return;
		}
		
		if($scope.addendum.id == null){
			$scope.addendumToInsert = {
					id:  null,
					noJob : $scope.jobNo,
					noSubcontract: $scope.subcontractNo,
					no: $scope.addendum.no,
					title: $scope.addendum.title,
					remarks: $scope.addendum.remarks,
			}
			createAddendum($scope.addendumToInsert);
		}
		else
			updateAddendum($scope.addendum);
	}
	
	/*function getSubcontract(){
		subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					console.log("Subcontract");
					console.log(data);
					var subcontract = data;
					if(subcontract.subcontractStatus < 500){
						//Subcontract not awarded
						$scope.disableButtons = true;
					}
					if(subcontract.submittedAddendum == '1' || subcontract.splitTerminateStatus =='1' || subcontract.splitTerminateStatus =='2'){
						//Addendum/Split/Terminate submitted
						$scope.disableButtons = true;
					}
				});
	}*/
	
	
	function getAddendum(){
		addendumService.getAddendum($scope.jobNo, $scope.subcontractNo, $scope.addendumNo)
		.then(
				function( data ) {
					$scope.addendum = data;
					if($scope.addendum.status == "PENDING")
						$scope.disableButtons = false;
					else
						$scope.disableButtons = true;
				});
	}
	
	function getAllAddendumDetails(){
		addendumService.getAllAddendumDetails($scope.jobNo, $scope.subcontractNo, $scope.addendumNo)
		.then(
				function( data ) {
					$scope.addendumDetailList = data;
				});
	}
	


}]);