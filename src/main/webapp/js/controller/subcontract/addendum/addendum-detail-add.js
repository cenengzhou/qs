mainApp.controller('AddendumDetailsAddCtrl', ['$scope' , 'modalService', 'addendumService', 'subcontractService', 'unitService', '$stateParams', '$cookies', '$state', '$uibModalInstance', 'roundUtil',
                                              function($scope ,modalService, addendumService, subcontractService, unitService, $stateParams, $cookies, $state, $uibModalInstance, roundUtil) {

	var jobNo = $cookies.get('jobNo');
	var subcontractNo = $cookies.get('subcontractNo');
	var addendumNo = $cookies.get('addendumNo');
	
	var addendumDetailHeaderRef = $cookies.get('addendumDetailHeaderRef');
	
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
		if(field == "totalAmount" && $scope.addendumDetail.amtAddendumTba != null){
			$scope.addendumDetail.amtAddendumTba = roundUtil.round($scope.addendumDetail.amtAddendumTba, 2);
			$scope.addendumDetail.quantityTba = roundUtil.round($scope.addendumDetail.amtAddendumTba/$scope.addendumDetail.rateAddendumTba, 4);
			$scope.addendumDetail.rateAddendumTba = roundUtil.round($scope.addendumDetail.amtAddendumTba/$scope.addendumDetail.quantityTba, 2);
		}else if(field == "quantity" && $scope.addendumDetail.quantityTba != null){
			$scope.addendumDetail.quantityTba = roundUtil.round($scope.addendumDetail.quantityTba, 4);
			$scope.addendumDetail.amtAddendumTba = roundUtil.round($scope.addendumDetail.quantityTba*$scope.addendumDetail.rateAddendumTba, 2);
			$scope.addendumDetail.rateAddendumTba = roundUtil.round($scope.addendumDetail.amtAddendumTba/$scope.addendumDetail.quantityTba, 2);
		}else if (field == "rate" && $scope.addendumDetail.rateAddendumTba != null){
			$scope.addendumDetail.rateAddendumTba = roundUtil.round($scope.addendumDetail.rateAddendumTba, 2);
			$scope.addendumDetail.amtAddendumTba = roundUtil.round($scope.addendumDetail.quantityTba*$scope.addendumDetail.rateAddendumTba, 2);
			$scope.addendumDetail.quantityTba = roundUtil.round($scope.addendumDetail.amtAddendumTba/$scope.addendumDetail.rateAddendumTba, 4);
		}
	}

	//Save Function
	$scope.save = function () {
		if (false === $('form[name="form-validate"]').parsley().validate()) {
			event.preventDefault();  
			return;
		}

		//Addendum without budget
		$scope.addendumDetail.rateBudgetTba = 0;
		$scope.addendumDetail.amtBudgetTba = 0;
		$scope.addendumDetail.typeVo = $scope.subcontractorNature;
		
		//Set Header if exist
		if(addendumDetailHeaderRef!=null && addendumDetailHeaderRef.length !=0){
			$scope.addendumDetail.idHeaderRef = addendumDetailHeaderRef;
		}
		
		$scope.addendumDetailToAdd = {
				bpi:				$scope.addendumDetail.bpi,
				codeObject: 		$scope.addendumDetail.codeObject,	
				codeSubsidiary:		$scope.addendumDetail.codeSubsidiary,
				description:		$scope.addendumDetail.description,
				quantityTba:		$scope.addendumDetail.quantityTba,
				rateAddendumTba:	$scope.addendumDetail.rateAddendumTba, 
				amtAddendumTba:		$scope.addendumDetail.amtAddendumTba, 
				rateBudgetTba: 		$scope.addendumDetail.rateBudgetTba,
				amtBudgetTba: 		$scope.addendumDetail.amtBudgetTba,
				unit:				$scope.units.selected,
				remarks:			$scope.addendumDetail.remarks,
				idHeaderRef:		$scope.addendumDetail.idHeaderRef,
				typeVo : 			$scope.addendumDetail.typeVo,
		}

		console.log($scope.addendumDetailToAdd);
		addAddendumDetail($scope.addendumDetailToAdd);

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
		subcontractService.getDefaultValuesForAddendumDetails(jobNo, subcontractNo, newValue)
		.then(
				function( data ) {
					if(data.length != 0){
						$scope.addendumDetail = data;
						/*$scope.addendumDetail.bpi = data.billItem;
						$scope.addendumDetail.codeObject = data.objectCode;	
						$scope.addendumDetail.codeSubsidiary = data.codeSubsidiary;*/
					}
				});
	});
	

	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
	};

	//Listen for location changes and call the callback
	$scope.$on('$locationChangeStart', function(event){
		$uibModalInstance.close();
	});

}]);