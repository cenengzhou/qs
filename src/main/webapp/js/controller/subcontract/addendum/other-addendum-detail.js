mainApp.controller('OtherAddendumDetailCtrl', ['$scope' , 'modalService', 'subcontractService', 'unitService', '$cookies', 'roundUtil', '$location',
                                              function($scope ,modalService, subcontractService, unitService, $cookies, roundUtil, $location) {

	var scDetailID = $cookies.get('scDetailID');
	
	
	$scope.subcontractDetail = [];
	$scope.units=[];
	$scope.units.selected = "AM";
	
	getUnitOfMeasurementList();
	
	$scope.lineTypes = {
			"AP": "AP - Advanced Payment" , 
			"C1": "C1 - Contra Charges by GSL",
			"MS": "MS - Material On Site",
			"OA": "OA - Other Adjustment",
			"RA": "RA - Retention Adjustment",
			"RR": "RR - Release Retention"
	};
	$scope.lineType = "C1";

	

	$scope.calculate = function(field){
		if(field == "totalAmount" && $scope.subcontractDetail.amountSubcontract != null){
			$scope.subcontractDetail.amountSubcontract = roundUtil.round($scope.subcontractDetail.amountSubcontract, 2);
			$scope.subcontractDetail.quantity = roundUtil.round($scope.subcontractDetail.amountSubcontract/$scope.subcontractDetail.scRate, 4);
			$scope.subcontractDetail.scRate = roundUtil.round($scope.subcontractDetail.amountSubcontract/$scope.subcontractDetail.quantity, 2);
		}else if(field == "quantity" && $scope.subcontractDetail.quantity != null){
			$scope.subcontractDetail.quantity = roundUtil.round($scope.subcontractDetail.quantity, 4);
			$scope.subcontractDetail.amountSubcontract = roundUtil.round($scope.subcontractDetail.quantity*$scope.subcontractDetail.scRate, 2);
			$scope.subcontractDetail.scRate = roundUtil.round($scope.subcontractDetail.amountSubcontract/$scope.subcontractDetail.quantity, 2);
		}else if (field == "rate" && $scope.subcontractDetail.scRate != null){
			$scope.subcontractDetail.scRate = roundUtil.round($scope.subcontractDetail.scRate, 2);
			$scope.subcontractDetail.amountSubcontract = roundUtil.round($scope.subcontractDetail.quantity*$scope.subcontractDetail.scRate, 2);
			$scope.subcontractDetail.quantity = roundUtil.round($scope.subcontractDetail.amountSubcontract/$scope.subcontractDetail.scRate, 4);
		}
	}

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
		
		if(scDetailID.length==0){
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
			console.log(scDetailToAdd);
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
		console.log("scDetailID: "+scDetailID);
		if(scDetailID.length !=0){
			subcontractService.getSubcontractDetailByID(scDetailID)
			.then(
					function( data ) {
						if(data.length != 0){
							console.log(data);
							$scope.subcontractDetail = data;
							$scope.units.selected = data.unit;
							$scope.lineType = data.lineType;
							
							$scope.disableSelect = true;
							$scope.disableFields = true;
						}
					});
		}else {console.log("get Default Value");
			subcontractService.getDefaultValuesForSubcontractDetails($scope.jobNo, $scope.subcontractNo, newValue)
			.then(
					function( data ) {
						if(data.length != 0){
							$scope.subcontractDetail = data;
						}
					});
			
		}
		
	});
	

}]);
