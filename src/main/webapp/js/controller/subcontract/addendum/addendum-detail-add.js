mainApp.controller('AddendumDetailsAddCtrl', ['$scope' , 'modalService', 'addendumService', 'subcontractService', 'unitService', '$cookies', '$state', '$uibModalInstance', 'roundUtil', 'modalStatus', 'modalParam', '$location', 
                                              function($scope ,modalService, addendumService, subcontractService, unitService, $cookies, $state, $uibModalInstance, roundUtil, modalStatus, modalParam, $location) {

	var jobNo = $cookies.get('jobNo');
	var subcontractNo = $cookies.get('subcontractNo');
	var addendumNo = $cookies.get('addendumNo');
	
	var addendumDetailHeaderRef = $cookies.get('addendumDetailHeaderRef');
	if(addendumDetailHeaderRef=='Empty')
		addendumDetailHeaderRef = '';
	
	$scope.addendumDetail = [];
	$scope.units=[];
	$scope.units.selected = "AM";
	$scope.regex = "[0-9]";
	
	getUnitOfMeasurementList();
	
	$scope.lineTypes = {
			"V1": "V1 - External VO" , 
			"V2": "V2 - Internal VO - No Budget" ,
			"V3": "V3 - Internal VO - Budget" ,
			"L1": "L1 - Claims vs GSL" ,
			"L2": "L2 - Claims vs other Subcontract" ,
			"D1": "D1 - Day Work for GCL",
			"D2": "D2 - Day Work for other Subcontract" ,
			"CF": "CPF" 
	};
	$scope.lineType = "V2";

	

	$scope.calculate = function(field){
		if(field == "totalAmount" && $scope.addendumDetail.amtAddendum != null){
			if($scope.addendumDetail.amtAddendum.indexOf('.') != $scope.addendumDetail.amtAddendum.length -1){
				$scope.addendumDetail.amtAddendum = roundUtil.round($scope.addendumDetail.amtAddendum, 2);
				$scope.addendumDetail.quantity = roundUtil.round($scope.addendumDetail.amtAddendum/$scope.addendumDetail.rateAddendum, 4);
			}
		}else if(field == "quantity" && $scope.addendumDetail.quantity != null){
			if($scope.addendumDetail.quantity.indexOf('.') != $scope.addendumDetail.quantity.length -1){
				$scope.addendumDetail.quantity = roundUtil.round($scope.addendumDetail.quantity, 4);
				$scope.addendumDetail.amtAddendum = roundUtil.round($scope.addendumDetail.quantity*$scope.addendumDetail.rateAddendum, 2);
			}
		}else if (field == "rate" && $scope.addendumDetail.rateAddendum != null){
			if($scope.addendumDetail.rateAddendum.indexOf('.') != $scope.addendumDetail.rateAddendum.length -1){
				$scope.addendumDetail.rateAddendum = roundUtil.round($scope.addendumDetail.rateAddendum, 2);
				$scope.addendumDetail.amtAddendum = roundUtil.round($scope.addendumDetail.quantity*$scope.addendumDetail.rateAddendum, 2);
			}
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

		
		if(modalStatus == 'ADD'){
			if($scope.addendumDetail.idResourceSummary == null ){//&& idSubcontractDetail ==null
				//Addendum without budget
				$scope.addendumDetail.rateBudget = 0;
				$scope.addendumDetail.amtBudget = 0;
			}
			
			//Set line type
			$scope.addendumDetail.typeVo = $scope.lineType;
			
			//Set Header if exist
			if(addendumDetailHeaderRef!=null && addendumDetailHeaderRef.length !=0){
				$scope.addendumDetail.idHeaderRef = addendumDetailHeaderRef;
			}
			
			var addendumDetailToAdd = {
					bpi:				$scope.addendumDetail.bpi,
					codeObject: 		$scope.addendumDetail.codeObject,	
					codeSubsidiary:		$scope.addendumDetail.codeSubsidiary,
					description:		$scope.addendumDetail.description,
					quantity:			$scope.addendumDetail.quantity,
					rateAddendum:		$scope.addendumDetail.rateAddendum, 
					amtAddendum:		$scope.addendumDetail.amtAddendum, 
					rateBudget: 		$scope.addendumDetail.rateBudget,
					amtBudget: 			$scope.addendumDetail.amtBudget,
					unit:				$scope.units.selected,
					noSubcontractChargedRef: $scope.addendumDetail.noSubcontractChargedRef,
					codeObjectForDaywork: $scope.addendumDetail.codeObjectForDaywork,
					remarks:			$scope.addendumDetail.remarks,
					idHeaderRef:		$scope.addendumDetail.idHeaderRef,
					typeVo: 			$scope.addendumDetail.typeVo,
					idSubcontractDetail: $scope.addendumDetail.idSubcontractDetail
			}
			console.log(addendumDetailToAdd);
			addAddendumDetail(addendumDetailToAdd);
		}
		else if(modalStatus == 'UPDATE'){
			updateAddendumDetail($scope.addendumDetail);
		}
	};
	
	function addAddendumDetail(addendumDetail){
		addendumService.addAddendumDetail(jobNo, subcontractNo, addendumNo, addendumDetail)
		.then(
				function( data ) {
					if(data.length != 0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
						$scope.disableButtons = false;
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Addendum has been created.");
						$uibModalInstance.close();
						
						if(addendumDetailHeaderRef == null || addendumDetailHeaderRef.trim().length ==0){
							$location.path("/subcontract/addendum/tab/detail-list");
						}else
							$state.reload();
					}
				});
	}
	
	function updateAddendumDetail(addendumDetail){
		addendumService.updateAddendumDetail(jobNo, subcontractNo, addendumNo, addendumDetail)
		.then(
				function( data ) {
					if(data.length != 0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
						$scope.disableButtons = false;
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Addendum has been created.");
						$uibModalInstance.close();
						$state.reload();
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
		if(modalStatus == 'ADD' && modalParam == null){
			//1. Add V1/V2 (no budget)
			addendumService.getDefaultValuesForAddendumDetails(jobNo, subcontractNo, addendumNo, newValue)
			.then(
					function( data ) {
						if(data.length != 0){
							$scope.addendumDetail = data;
						}
					});
		}else if(modalStatus == 'ADD' && modalParam != null){
			//Add from Approved Addendum
			$scope.addendumDetail = modalParam;
			$scope.lineType = modalParam.typeVo;

			$scope.disableSelect = true;
			
			$scope.disableFields = true;
		}
		else if(modalStatus == 'UPDATE'){
			//2. Add new VO from SC Detail (no budget) OR Update VO (no budget)
			console.log(modalParam);
			$scope.addendumDetail = modalParam;
			
			$scope.lineType = modalParam.typeVo;
			$scope.disableSelect = true;
			
			//Update from APPROVED SC Details (V1, V2, V3)
			if($scope.addendumDetail.idSubcontractDetail != null){
				$scope.disableFields = true;
			}
			
			//Update from V3 (Non-APPROVED)
			if($scope.addendumDetail.idResourceSummary != null){
				$scope.disableFields = true;
			}
		}
		
	});
	

	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
	};

	//Listen for location changes and call the callback
	$scope.$on('$locationChangeStart', function(event){
		$uibModalInstance.close();
	});

}]);