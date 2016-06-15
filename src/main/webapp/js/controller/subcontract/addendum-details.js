mainApp.controller('AddendumDetailsCtrl', ['$scope' , '$http', 'modalMessageService', function($scope , $http, modalMessageService) {

	$scope.status = "yes";
	
	$scope.addendumTitle = "Addendum 1";
	
	$scope.subcontractorNature = "V2";
	
	$scope.lineTypes = {
			"V1": "V1 - External VO - No Budget" , 
			"V2": "V2 - Internal VO - No Budget" ,
			"V3": "V3 - " ,
			"L1": "L1 - Claims vs GSL" ,
			"L2": "L2 - Claims vs other Subcontract" ,
			"D1": "D1 - Day Work for GCL",
			"D2": "D2 - Day Work for other Subcontract" ,
			"CF": "CPF" 
    };
	
	/*
	 //By using jquery json parser
	 var obj = $.parseJSON('{"name": "", "skills": "", "jobtitel": "Entwickler", "res_linkedin": "GwebSearch"}');
	alert(obj['jobtitel']);

	//By using javasript json parser
	var t = JSON.parse('{"name": "", "skills": "", "jobtitel": "Entwickler", "res_linkedin": "GwebSearch"}');
	alert(t['jobtitel'])
	*/
	
	$scope.addendum = {
			description: "",
			objectCode: "",
			subsidiaryCode: "",
			unit: "",
			quantity: 0,
			scRate: 0,
			totalAmount: 0,
			corrSCNo: "",
			altObjectCode: "",
			remark: ""
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
			//showGridFooter : true,
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
	
	
	$http.get('http://localhost:8080/pcms/data/addendum.json')
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
			
		
		$scope.addendum.description = $scope.selectedRows[0]['Description'];
		$scope.addendum.objectCode = $scope.selectedRows[0]['ObjectCode'];
		$scope.addendum.subsidiaryCode = $scope.selectedRows[0]['SubsidiaryCode'];
		$scope.addendum.unit = $scope.selectedRows[0]['Unit'];
		//$scope.addendum.quantity = $scope.selectedRows[0]['toBeApprovedQuantity'];
		//$scope.addendum.scRate = $scope.selectedRows[0]['toBeApprovedScRate'];
		//s$scope.addendum.totalAmount = $scope.selectedRows[0]['MovementAmount'];
		//$scope.addendum.corrSCNo = $scope.selectedRows[0]['Description'];
		//$scope.addendum.altObjectCode = $scope.selectedRows[0]['Unit'];
		$scope.addendum.remark = $scope.selectedRows[0]['Remarks'];
		
		
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
	    	$('ul.setup-panel li a[href="#step3"]').trigger('click');	    
	    }
	    	
	    
	    
	    
	    $('#activate-step-2').on('click', function(e) {
	    	/*console.log("Line types: "+$scope.addendum.selected);
	    	console.log("Description: "+$scope.addendum.description);*/
	    	
	                // step-1 validation
            if (false === $('form[name="form-wizard-step-1"]').parsley().validate('wizard-step-1')) {
                return true;
            }
	    	
	        $('ul.setup-panel li:eq(1)').removeClass('disabled');
	        $('ul.setup-panel li a[href="#step2"]').trigger('click');
	    })    
	    $('#activate-step-3').on('click', function(e) {
	    	
	    	if (false === $('form[name="form-wizard-step-2"]').parsley().validate('wizard-step-2')) {
                return false;
            }
	    	
	        $('ul.setup-panel li:eq(2)').removeClass('disabled');
	        $('ul.setup-panel li a[href="#step3"]').trigger('click');
	    })  
	    $('#activate-step-4').on('click', function(e) {
	        $('ul.setup-panel li:eq(3)').removeClass('disabled');
	        $('ul.setup-panel li a[href="#step4"]').trigger('click');
	    })  
	    $('#activate-step-5').on('click', function(e) {
	        $('ul.setup-panel li:eq(4)').removeClass('disabled');
	        $('ul.setup-panel li a[href="#step5"]').trigger('click');
	    }) 
	    $('#activate-step-6').on('click', function(e) {
	        $('ul.setup-panel li:eq(5)').removeClass('disabled');
	        $('ul.setup-panel li a[href="#step6"]').trigger('click');
	    }) 
	});


}]);