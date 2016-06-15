mainApp.controller('PaymentDetailsCtrl', ['$scope' , '$http',  function($scope , $http) {


	$scope.status = "yes";


	$scope.interimFinal = "Interim";

	$scope.mainCertNo = {
			options: [
			          "1",
			          "2",
			          "3",
			          "4"
			          ],
			          selected: "1"
	};



	/*
	 //By using jquery json parser
	 var obj = $.parseJSON('{"name": "", "skills": "", "jobtitel": "Entwickler", "res_linkedin": "GwebSearch"}');
	alert(obj['jobtitel']);

	//By using javasript json parser
	var t = JSON.parse('{"name": "", "skills": "", "jobtitel": "Entwickler", "res_linkedin": "GwebSearch"}');
	alert(t['jobtitel'])
	 */

	$scope.payment = {
			dueDate: new Date("05/06/2016"),
			asAtDate: "",
			invoiceReceivedDate: "",
			gstPayable: 0,
			gstReceivable: 0,
			certificateAmount: 1000000000,
			paymentTerms: "QS2",
			paymentRequisition: "No",
			paymentStatus:"Pending",
			certIssueDate: ""
	};


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
			//enableSelectAll: true,
			//enableFullRowSelection: true,
			multiSelect: false,
			showGridFooter : true,
			//showColumnFooter : true,
			//fastWatch : true,


			columnDefs: [
			             { field: 'lineType', width:80},
			             { field: 'bqItem',  width:100 },
			             { field: 'costRate',  width:100 },
			             { field: 'ScRate' ,  width:100 },
			             { field: 'Quantity' ,  width:100 },
			             { field: 'BudgetAmount' ,  width:100 },
			             { field: 'toBeApprovedCostRate' ,  width:100 },
			             {field: 'toBeApprovedScRate' ,  width:100 },
			             {field: 'toBeApprovedQuantity' ,  width:100 },
			             {field: 'toBeApprovedBudgetAmount' ,  width:100 },
			             {field: 'MovementAmount' ,  width:100 },
			             {field: 'ObjectCode' ,  width:100 },
			             {field: 'SubsidiaryCode' ,  width:100 },
			             {field: 'Description' ,  width:100 },
			             {field: 'Unit' ,  width:100 },
			             {field: 'Remarks' ,  width:100 },
			             ]
	};

	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;

		gridApi.selection.on.rowSelectionChanged($scope,function(row){
			var msg = 'row selected ' + row.isSelected;
			console.log(row.entity.lineType);
		});


		/*gridApi.selection.on.rowSelectionChanged($scope,function(row){
			  var removeRowIndex = $scope.grid_Options.data.indexOf(row.entity);
         });*/
	}

	
	$http.get('http://localhost:8080/pcms/data/payment.json')
	.success(function(data) {
		$scope.gridOptions.data = data;
	});

	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};


	$scope.edit = function(){
		$scope.selectedRows  = $scope.gridApi.selection.getSelectedRows();

		if($scope.selectedRows.length == 0){
			modalMessageService.open('view/message-modal.html', 'MessageModalCtrl', "Please select a row to edit.");
			return false;
		}


		$scope.payment.description = $scope.selectedRows[0]['Description'];
		$scope.payment.objectCode = $scope.selectedRows[0]['ObjectCode'];
		$scope.payment.subsidiaryCode = $scope.selectedRows[0]['SubsidiaryCode'];
		$scope.payment.unit = $scope.selectedRows[0]['Unit'];
		//$scope.payment.quantity = $scope.selectedRows[0]['toBeApprovedQuantity'];
		//$scope.payment.scRate = $scope.selectedRows[0]['toBeApprovedScRate'];
		//s$scope.payment.totalAmount = $scope.selectedRows[0]['MovementAmount'];
		//$scope.payment.invoiceReceivedDate = $scope.selectedRows[0]['Description'];
		//$scope.payment.altObjectCode = $scope.selectedRows[0]['Unit'];
		$scope.payment.remark = $scope.selectedRows[0]['Remarks'];


		/*var arrayLength = $scope.selectedRows.length;
		for (var i = 0; i < arrayLength; i++) {
			console.log($scope.selectedRows[i]['lineType']);
		}*/
		$('ul.setup-panel li a[href="#step2"]').trigger('click');	    
	}


	//Form validation
	$(document).ready(function() {

		var navListItems = $('ul.setup-panel li a'),
		allWells = $('.setup-content');

		allWells.hide();

		navListItems.click(function(e)
				{
			e.preventDefault();
			var $target = $($(this).attr('href')),
			$item = $(this).closest('li');

			if (!$item.hasClass('disabled')) {
				navListItems.closest('li').removeClass('active');
				$item.addClass('active');
				allWells.hide();
				$target.show();
			}
				});


		$('ul.setup-panel li.active a').trigger('click');

		if($scope.status == "yes"){
			$('ul.setup-panel li:eq(1)').removeClass('disabled');
			$('ul.setup-panel li:eq(2)').removeClass('disabled');
			$('ul.setup-panel li:eq(3)').removeClass('disabled');
			$('ul.setup-panel li a[href="#step2"]').trigger('click');	    
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



	/* $scope.gridOptions = {
    	      enableFiltering: true,
    	      enableColumnResizing : true,
    	      enableGridMenu : true,
    	      enableRowSelection: true,
    	      enableFullRowSelection: true,
    	      multiSelect: false,
    	      //showGridFooter : true,
    	      //showColumnFooter : true,
    	      //fastWatch : true,

    	     enableCellEditOnFocus : true,

    	     paginationPageSizes: [50],
    	      paginationPageSize: 50,


    	      //Single Filter
    	      onRegisterApi: function(gridApi){
    	        $scope.gridApi = gridApi;
    	      },
    	      columnDefs: [
    	        { field: 'certNo', width:80, displayName:"Cert No.",
    	         cellTemplate: '<div style="text-decoration:underline;color:blue;text-align:right;cursor:pointer" ng-click="grid.appScope.rowClick(row)">{{COL_FIELD}}</div>'},
    	        //cellTemplate: '<div class="ui-grid-cell-contents"><span><a ng-click="clicker(row)">{{COL_FIELD}}</a></span></div>' },
    	         { field: 'clientCertNo', enableCellEdit: false , width:100 },
    	        { field: 'mainContractorAmt', cellFilter: 'mapGender' },
    	        { field: 'nscAmt' },
    	        { field: 'mosAmt' },
    	        { field: 'retention' },
    	        { field: 'mosRetention' },
    	        {field: 'contraChargeAmt' },
    	        {field: 'adjustmentAmt' },
    	        {field: 'advancedPayment' },
    	        {field: 'cpfAmt' },
    	      ]

    	    };

    	    $http.get('http://localhost:8080/QSrevamp2/data/cert-data.json')
    	      .success(function(data) {
    	        $scope.gridOptions.data = data;
    	      });

    	    $scope.filter = function() {
    	      $scope.gridApi.grid.refresh();
    	    };*/


}]);