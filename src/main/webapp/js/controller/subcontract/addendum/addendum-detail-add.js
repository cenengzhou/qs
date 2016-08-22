mainApp.controller('AddendumDetailsAddCtrl', ['$scope' , 'modalService', 'addendumService', 'subcontractService', 'unitService', '$cookies', '$state', '$uibModalInstance', 'roundUtil', 'modalStatus', 'modalParam', 
                                              function($scope ,modalService, addendumService, subcontractService, unitService, $cookies, $state, $uibModalInstance, roundUtil, modalStatus, modalParam) {

	var jobNo = $cookies.get('jobNo');
	var subcontractNo = $cookies.get('subcontractNo');
	var addendumNo = $cookies.get('addendumNo');
	
	var addendumDetailHeaderRef = $cookies.get('addendumDetailHeaderRef');
	if(addendumDetailHeaderRef=='Empty')
		addendumDetailHeaderRef = '';
	
	$scope.addendumDetail = [];
	$scope.units=[];
	$scope.units.selected = "AM";
	
	getUnitOfMeasurementList();
	
	$scope.lineTypes = {
			"V1": "V1 - External VO - No Budget" , 
			"V2": "V2 - Internal VO - No Budget" ,
			"L1": "L1 - Claims vs GSL" ,
			"L2": "L2 - Claims vs other Subcontract" ,
			"D1": "D1 - Day Work for GCL",
			"D2": "D2 - Day Work for other Subcontract" ,
			"CF": "CPF" 
	};
	$scope.subcontractorNature = "V2";

	

	$scope.calculate = function(field){
		if(field == "totalAmount" && $scope.addendumDetail.amtAddendum != null){
			$scope.addendumDetail.amtAddendum = roundUtil.round($scope.addendumDetail.amtAddendum, 2);
			$scope.addendumDetail.quantity = roundUtil.round($scope.addendumDetail.amtAddendum/$scope.addendumDetail.rateAddendum, 4);
			$scope.addendumDetail.rateAddendum = roundUtil.round($scope.addendumDetail.amtAddendum/$scope.addendumDetail.quantity, 2);
		}else if(field == "quantity" && $scope.addendumDetail.quantity != null){
			$scope.addendumDetail.quantity = roundUtil.round($scope.addendumDetail.quantity, 4);
			$scope.addendumDetail.amtAddendum = roundUtil.round($scope.addendumDetail.quantity*$scope.addendumDetail.rateAddendum, 2);
			$scope.addendumDetail.rateAddendum = roundUtil.round($scope.addendumDetail.amtAddendum/$scope.addendumDetail.quantity, 2);
		}else if (field == "rate" && $scope.addendumDetail.rateAddendum != null){
			$scope.addendumDetail.rateAddendum = roundUtil.round($scope.addendumDetail.rateAddendum, 2);
			$scope.addendumDetail.amtAddendum = roundUtil.round($scope.addendumDetail.quantity*$scope.addendumDetail.rateAddendum, 2);
			$scope.addendumDetail.quantity = roundUtil.round($scope.addendumDetail.amtAddendum/$scope.addendumDetail.rateAddendum, 4);
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
			$scope.addendumDetail.typeVo = $scope.subcontractorNature;
			
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

	$scope.$watch('subcontractorNature', function(newValue, oldValue) {
		if(modalStatus == 'ADD' && modalParam == null){console.log("ADD");
			//1. Add new VO (no budget)
			addendumService.getDefaultValuesForAddendumDetails(jobNo, subcontractNo, addendumNo, newValue)
			.then(
					function( data ) {
						if(data.length != 0){console.log(data);
							$scope.addendumDetail = data;
						}
					});
		}
		else {
			//2. Add new VO from SC Detail (no budget) OR Update VO (no budget)
			$scope.addendumDetail = modalParam;
			$scope.subcontractorNature = modalParam.typeVo;
			$scope.disableSelect = true;
			
			if(modalParam.idResourceSummary != null){
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