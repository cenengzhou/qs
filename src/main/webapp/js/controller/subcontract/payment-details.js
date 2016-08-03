mainApp.controller('PaymentDetailsCtrl', ['$scope' , '$http', '$stateParams', '$cookies', 'paymentService', 
                                          function($scope , $http, $stateParams, $cookies, paymentService) {
	$scope.disableButtons = true;

	$scope.mainCertNo = {
			options: [
			          "1",
			          "2",
			          "3",
			          "4"
			          ],
			          selected: "1"
	};

	if($stateParams.payment){
		$cookies.put('paymentCertNo', $stateParams.payment.paymentCertNo);
		$cookies.put('paymentTerms', $stateParams.paymentTerms);

	}
	$scope.paymentCertNo = $cookies.get('paymentCertNo');
	$scope.paymentTerms = $cookies.get('paymentTerms');


	$scope.payment = $stateParams.payment;
	if($scope.payment==null){
		loadPaymentCert();

	}else{
		initTab();
	}

	function loadPaymentCert() {
		paymentService.getPaymentCert($scope.jobNo, $scope.subcontractNo, $scope.paymentCertNo)
		.then(
				function( data ) {
					console.log(data);
					$scope.mainCertNo.selected = data.mainContractPaymentCertNo;
					$scope.payment = data;

					initTab();
				});
	}

	function loadPaymentDetails() {
		paymentService.getPaymentDetailList($scope.jobNo, $scope.subcontractNo, $scope.paymentCertNo)
		.then(
				function( data ) {
					$scope.gridOptions.data = data;
					$scope.gridApi.grid.refresh();
				});
	}

	function loadPaymentCertSummary() {
		paymentService.getPaymentCertSummary($scope.jobNo, $scope.subcontractNo, $scope.paymentCertNo)
		.then(
				function( data ) {
					console.log(data);
					$scope.paymentCertSummary = data;
				});
	}

	$scope.units = {
			"V1": "V1 - External VO - No Budget" , 
			"V2": "V2 - Internal VO - No Budget" ,
			"V3": "V3 - " ,
			"L1": "L1 - Claims vs GSL" ,
			"L2": "L2 - Claims vs other Subcontract" ,
			"D1": "D1 - Day Work for GCL",
			"D2": "D2 - Day Work for other Subcontract" ,
			"CF": "CPF" 
	};


	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			//enableFullRowSelection: true,
			multiSelect: true,
			enableCellEditOnFocus : true,
			showGridFooter : false,
			//showColumnFooter : true,
			exporterMenuPdf: false,

			columnDefs: [
			             { field: 'lineType', enableCellEdit: false},
			             { field: 'billItem', enableCellEdit: false},
			             { field: 'movementAmount', cellClass: "grid-theme-blue"},
			             { field: 'cumAmount', displayName: "Cumulative Certified Amount", cellClass: "grid-theme-blue"},
			             { field: 'postedAmount', displayName: "Posted Certified Amount", enableCellEdit: false},
			             { field: 'description', enableCellEdit: false},
			             { field: 'scSeqNo', displayName: "Sequence No", enableCellEdit: false},
			             { field: 'objectCode', enableCellEdit: false},
			             {field: 'subsidiaryCode', enableCellEdit: false}
			             ]
	};

	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;

		gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {

			//Alert to show what info about the edit is available
			//alert('Column: ' + colDef.name + ' feedbackRate: ' + rowEntity.feedbackRate);
			//rowEntity.feedbackRateHK = rowEntity.feedbackRate * $scope.exchangeRate;
		});

	}

	function initTab (){
		$(document).ready(function() {console.log("Init Tab");

		var navListItems = $('ul.setup-panel li a'),
		allWells = $('.setup-content');

		allWells.hide();

		navListItems.click(function(e){
			e.preventDefault();
			var $target = $($(this).attr('href')),
			$item = $(this).closest('li');


			if (!$item.hasClass('disabled')) {
				navListItems.closest('li').removeClass('active');
				$item.addClass('active');
				allWells.hide();
				$target.show();
			}

			if($item.context.hash == "#step2"){
				loadPaymentDetails();
			}else if($item.context.hash == "#step3"){

			}else if($item.context.hash == "#step4"){
				loadPaymentCertSummary();
			}


		});


		$('ul.setup-panel li.active a').trigger('click');


		if($scope.payment.paymentStatus != "PND"){
			$('ul.setup-panel li:eq(1)').removeClass('disabled');
			$('ul.setup-panel li:eq(2)').removeClass('disabled');
			$('ul.setup-panel li:eq(3)').removeClass('disabled');

			//$('ul.setup-panel li a[href="#step2-details"]').trigger('click');

			$scope.disableButtons = true;
		}else{
			$scope.disableButtons = false;
		}

		$('#activate-step-2').on('click', function(e) {
			// step-1 validation
			if (false === $('form[name="form-wizard-step-1"]').parsley().validate('wizard-step-1')) {
				return true;
			}

			$('ul.setup-panel li:eq(1)').removeClass('disabled');
			$('ul.setup-panel li a[href="#step2"]').trigger('click');
		})    
		$('#activate-step-3').on('click', function(e) {

			$('ul.setup-panel li:eq(2)').removeClass('disabled');
			$('ul.setup-panel li a[href="#step3"]').trigger('click');
		})  
		$('#activate-step-4').on('click', function(e) {
			$('ul.setup-panel li:eq(3)').removeClass('disabled');
			$('ul.setup-panel li a[href="#step4"]').trigger('click');
		})  

		});
	};

	$scope.convertPaymentStatus = function(status){
		if(status!=null){
			if (status.localeCompare('PND') == 0) {
				return "Pending";
			}else if (status.localeCompare('SBM') == 0) {
				return "Submitted";
			}else if (status.localeCompare('UFR') == 0) {
				return { width: "Under Finance Review" }
			}else if (status.localeCompare('PCS') == 0) {
				return { width: "Waiting For Posting" }
			}else if (status.localeCompare('APR') == 0) {
				return "Posted To Finance";
			}
		}
	}

}]);