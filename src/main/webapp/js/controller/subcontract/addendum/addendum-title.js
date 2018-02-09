mainApp.controller('AddendumTitleCtrl', ['$rootScope', '$scope' , 'modalService', 'addendumService', 'subcontractService', '$stateParams', '$cookies', '$state', '$location', 
                                           function($rootScope, $scope ,modalService, addendumService, subcontractService, $stateParams, $cookies, $state, $location) {
	$scope.addendum = [];
	if($stateParams.addendumNo != undefined){
		if($stateParams.addendumNo == 'New') {
			$cookies.put('addendumNo', '');
			$scope.addendum.no = $scope.addendumNo = '';
			$rootScope.addendumNo = '';
		} else{
			$cookies.put('addendumNo', $stateParams.addendumNo);
			$scope.addendum.no = $scope.addendumNo = $stateParams.addendumNo;
			$rootScope.addendumNo = $stateParams.addendumNo;
		}
	} else {
		$scope.addendum.no = $scope.addendumNo;
	}
	
	loadData();
	
	
	function loadData(){
		if($scope.addendum.no.length == 0)
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
	
	
	function getAddendum(){
		addendumService.getAddendum($scope.jobNo, $scope.subcontractNo, $scope.addendum.no)
		.then(
				function( data ) {
					$scope.addendum = data;
					if($scope.addendum ==null || $scope.addendum.length==0 || $scope.addendum.status == "PENDING")
						$scope.disableButtons = false;
					else
						$scope.disableButtons = true;
				});
	}
	
	function getLatestAddendum(){
		addendumService.getLatestAddendum($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					if(data.length ==0){
						$scope.addendum.no = 1;
					}else{
						if(data.status == 'APPROVED'){
							$scope.addendum.no = data.no + 1;
						}else{
							$location.path('/subcontract/addendum-select');
							modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "A pending addendum already exists.");
						}
					}
				});
	}
	
	
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