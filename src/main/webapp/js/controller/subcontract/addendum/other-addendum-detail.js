mainApp.controller('OtherAddendumDetailCtrl', ['$scope' , 'modalService', 'subcontractService', 'unitService', '$cookies', 'roundUtil', '$location',
                                              function($scope ,modalService, subcontractService, unitService, $cookies, roundUtil, $location) {

	var scDetailID = $cookies.get('scDetailID');
	
	
	$scope.subcontractDetail = [];
	$scope.units=[];
	$scope.units.selected = "AM";
	
	getUnitOfMeasurementList();
	getSubcontract();
	
	$scope.lineTypes = {
			"AP": "AP - Advanced Payment" , 
			"C1": "C1 - Contra Charges by GSL",
			"MS": "MS - Material On Site",
			"OA": "OA - Other Adjustment",
			"RA": "RA - Retention Adjustment",
			"RR": "RR - Release Retention"
	};
	$scope.lineType = "C1";


	//Save Function
	$scope.save = function () {
		$scope.disableButtons = true;
		if (false === $('form[name="form-validate"]').parsley().validate()) {
			event.preventDefault();  
			$scope.disableButtons = false;
			return;
		}

		if($scope.lineType == 'C1' && $scope.subcontractDetail.objectCode.substring(4, 6) !='88'){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Object code should be ended with '88'.");
			$scope.disableButtons = false;
		}
		
		if(scDetailID == null || scDetailID.length==0){
			var scDetailToAdd = {
					billItem:			$scope.subcontractDetail.billItem,
					objectCode: 		$scope.subcontractDetail.objectCode,	
					subsidiaryCode:		$scope.subcontractDetail.subsidiaryCode,
					description:		$scope.subcontractDetail.description,
					quantity:			$scope.subcontractDetail.quantity,
					scRate:				$scope.subcontractDetail.scRate, 
					amountSubcontract:	$scope.subcontractDetail.amountSubcontract, 
					lineType:			$scope.lineType,
					unit:				$scope.units.selected,
					remarks:			$scope.subcontractDetail.remarks,
			}

			addAddendumToSubcontractDetail(scDetailToAdd);
		}
		else {
			updateSubcontractDetailAddendum($scope.subcontractDetail);
		}
	};
	
	function addAddendumToSubcontractDetail(subcontractDetail){
		subcontractService.addAddendumToSubcontractDetail($scope.jobNo, $scope.subcontractNo, subcontractDetail)
		.then(
				function( data ) {
					if(data.length != 0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
						$scope.disableButtons = false;
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Addendum has been created.");
						$cookies.put('scDetailID', '');
						$location.path('/subcontract/otherAddendum/tab/list');
					}
				});
	}
	
	function updateSubcontractDetailAddendum(subcontractDetail){
		subcontractService.updateSubcontractDetailAddendum(subcontractDetail)
		.then(
				function( data ) {
					if(data.length != 0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
						$scope.disableButtons = false;
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Addendum has been created.");
						$cookies.put('scDetailID', '');
						$location.path('/subcontract/otherAddendum/tab/list');
					}
				});
	}
	
	function getSubcontract(){
		subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					if(data.scStatus < 500 || data.paymentStatus == 'F' || data.splitTerminateStatus ==1  || data.splitTerminateStatus ==2 || data.splitTerminateStatus ==4|| data.submittedAddendum ==1)
						$scope.disableButton = true;
					else 
						$scope.disableButton = false;
					
					if(data.scStatus < '500'){
						$scope.lineType = "RR";
						$scope.disableSelect = true;
					}
				});
	}
	
	
	function getUnitOfMeasurementList() {
		unitService.getUnitOfMeasurementList()
		.then(
				function( data ) {
					angular.forEach(data, function(value, key){
						$scope.units.push(value.unitCode.trim());
					});
				});
	}

	$scope.$watch('lineType', function(newValue, oldValue) {
		if($scope.paymentStatus == null || $scope.paymentStatus != 'F'){
			if(scDetailID !=null && scDetailID.length !=0){
				subcontractService.getSubcontractDetailByID(scDetailID)
				.then(
						function( data ) {
							if(data.length != 0){
								$scope.subcontractDetail = data;
								$scope.units.selected = data.unit;
								$scope.lineType = data.lineType;
								
								$scope.disableSelect = true;
								$scope.disableFields = true;
							}
						});
			}else {
				subcontractService.getDefaultValuesForSubcontractDetails($scope.jobNo, $scope.subcontractNo, newValue)
				.then(
						function( data ) {
							if(data.length != 0){
								$scope.subcontractDetail = data;
							}
						});
				
			}		
		}
		
	});

	
	

}]);

