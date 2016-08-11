mainApp.controller('AddendumSummaryCtrl', ['$scope' , 'modalService', 'addendumService', 'subcontractService', 'paymentService', '$stateParams', '$cookies', '$state', 
                                           function($scope ,modalService, addendumService, subcontractService, paymentService, $stateParams, $cookies, $state) {

	if($stateParams.addendumNo){
		$cookies.put('addendumNo', $stateParams.addendumNo);
	}

	$scope.addendum = [];
	$scope.addendum.no = $cookies.get('addendumNo');

	loadData();
	
	
	function loadData(){
		if($scope.addendum.no==null)
			getLatestAddendum();
		else
			getAddendum();
	}
	
	
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
		addendumService.getAddendum($scope.jobNo, $scope.subcontractNo, $scope.addendum.no)
		.then(
				function( data ) {
					console.log(data);
					$scope.addendum = data;
					if($scope.addendum.status == "PENDING")
						$scope.disableButtons = false;
					else
						$scope.disableButtons = true;
				});
	}
	
	function getLatestAddendum(){
		addendumService.getLatestAddendum($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					console.log(data);
					if(data.length ==0){
						$scope.addendum.no = 1;
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "A pending addendum already exists.");
					}
				});
	}
	
	/*function getLatestPaymentCert(){
		paymentService.getLatestPaymentCert($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					console.log(data);
					if(data.length !=0){
						if(data.paymentStatus != 'APR'){
							$scope.disableButtons = true;
						}
					}
				});
	}*/
	
	function createAddendum(addendum){
		addendumService.createAddendum(addendum)
		.then(
				function( data ) {
					if(data.length != 0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
						$scope.disableButtons = false;
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Addendum has been created.");
						$state.reload();
					}
				});
	}
	
	function updateAddendum(addendum){
		addendumService.updateAddendum(addendum)
		.then(
				function( data ) {
					if(data.length != 0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
						$scope.disableButtons = false;
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Addendum has been updated.");
						$state.reload();
					}
				});
	}

}]);