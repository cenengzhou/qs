mainApp.controller('SubcontractWorkdoneCtrl', ['$scope', 'subcontractService', 'resourceSummaryService', 'uiGridConstants', 'modalService', '$state',
                                               function ($scope, subcontractService, resourceSummaryService, uiGridConstants, modalService, $state) {

	$scope.percent = "";
	$scope.lastID ="";
	
	getSubcontractDetailForWD();
	getResourceSummariesBySC();
	
	$scope.gridOptions = {
			enableSorting: true,
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableColumnMoving: true,
			showGridFooter : false,
			showColumnFooter : true,
			//fastWatch : true,
			
			exporterMenuPdf: false,
			
			enableCellEditOnFocus : true,

			//rowEditWaitInterval :-1,
			
			columnDefs: [
		             	{ field: 'id', width: 20, enableCellEdit: false, visible:false},
			             { field: 'lineType', width: 40, enableCellEdit: false, pinnedLeft:true},
			             { field: 'description', width: 100, enableCellEdit: false, pinnedLeft:true},

			             {field: 'amountBudget', displayName: "Budget Amount", width: 120, visible:false, enableCellEdit: false,enableFiltering: false ,
			            	 cellClass: 'text-right', cellFilter: 'number:2'},
			             {field: 'amountSubcontract', displayName: "SC Amount", width: 120, enableCellEdit: false, enableFiltering: false ,
			            		 cellClass: 'text-right', cellFilter: 'number:2'},
			             { field: 'amountCumulativeWD', displayName: "Cum WD Amount", width: 120, cellClass: 'text-right blue', cellFilter: 'number:2', enableFiltering: false ,
			            	aggregationType: uiGridConstants.aggregationTypes.sum,
			            	footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'},
			             { field: 'amountPostedWD', displayName: "Posted WD Amount", enableCellEdit: false, enableFiltering: false ,
		            		cellClass: 'text-right', cellFilter: 'number:2',
	            			width: 120, aggregationType: uiGridConstants.aggregationTypes.sum,
	            		 	footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'},
			             { field: 'amountCumulativeCert', displayName: "Cum Certified Amount", enableCellEdit: false,enableFiltering: false ,
	            		 		cellClass: 'text-right', cellFilter: 'number:2',
	            		 	width: 120, aggregationType: uiGridConstants.aggregationTypes.sum,
        		 			footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'},
			             { field: 'amountPostedCert', displayName: "Posted Certified Amount", width: 120, visible:false, enableCellEdit: false, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2'},
			            { field: 'quantity', width: 100, enableCellEdit: false, enableFiltering: false ,cellClass: 'text-right', cellFilter: 'number:4'},

	            		 {field: 'costRate', width: 60, enableCellEdit: false, enableFiltering: false},
	            		 {field: 'scRate', width: 60, enableCellEdit: false, enableFiltering: false},
		            	 {field: 'billItem', width: 80, enableCellEdit: false},
			             {field: 'objectCode', width: 70, enableCellEdit: false},
			             {field: 'subsidiaryCode', width: 70, enableCellEdit: false},
			             
			             {field: 'projectedProvision', width: 120, visible:false, enableCellEdit: false, enableFiltering: false},
			             {field: 'provision', width: 120, visible:false, enableCellEdit: false, enableFiltering: false},

			             {field: 'altObjectCode', width: 100, visible:false, enableCellEdit: false},

			             {field: 'approved', width: 80, enableCellEdit: false},
			             {field: 'unit', width: 80, visible:false, enableCellEdit: false},

			             {field: 'remark', width: 80, visible:false, enableCellEdit: false},
			             {field: 'contraChargeSCNo', width: 80, visible:false, enableCellEdit: false},
			             {field: 'sequenceNo', width: 80, visible:false, enableCellEdit: false},
			             {field: 'resourceNo', width: 80, visible:false, enableCellEdit: false},
			             {field: 'balanceType', width: 80, visible:false, enableCellEdit: false}
			             ],

	
	};

	$scope.gridOptions.onRegisterApi= function(gridApi){
		$scope.gridApi = gridApi;

		 gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			 if($scope.lastID != rowEntity.id){
				$scope.lastID = rowEntity.id;
					
				if(rowEntity.costRate != 0) {
	            	getResourceSummariesByLineType(rowEntity.objectCode, rowEntity.subsidiaryCode, rowEntity.lineType, rowEntity.resourceNo);
				} else {
	            	$scope.gridOptionsIV.data.splice (0, $scope.gridOptionsIV.data.length);
	            }
			}
        });
		
		gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			if(newValue != oldValue )
				updateWDandIV(rowEntity);
		});

	}
	

	$scope.gridOptionsIV = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			showGridFooter : false,
			showColumnFooter : true,
			enableCellEditOnFocus : true,
			exporterMenuPdf: false,
			//rowEditWaitInterval :-1,


			columnDefs: [
			             { field: 'id', width:50 , visible:false, enableCellEdit: false},
			             { field: 'packageNo', width:70, displayName:"Subcontract No.", visible:false, enableCellEdit: false},
			             { field: 'objectCode', enableCellEdit: false , width:70, pinnedLeft:true},
			             { field: 'subsidiaryCode',width:70, enableCellEdit: false, pinnedLeft:true},
			             { field: 'resourceDescription', width:80, displayName: "Description", enableCellEdit: false },
			             {field: 'currIVAmount', displayName: "Cum. IV Amount", width:130, enableFiltering: false, cellClass: 'text-right blue', cellFilter: 'number:2', 
			            	 aggregationType: uiGridConstants.aggregationTypes.sum,
			            	footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'},
	            		 {field: 'postedIVAmount', width:130,  enableCellEdit: false, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2',  
	            			aggregationType: uiGridConstants.aggregationTypes.sum,
			            	footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'},
            			 {field: 'amountBudget',width:100,  displayName:"Budget Amount", cellClass: 'text-right', cellFilter: 'number:2', enableCellEdit: false, enableFiltering: false },
            			 { field: 'unit', width:50, enableCellEdit: false, enableFiltering: false},
			             { field: 'quantity', width:100, enableCellEdit: false ,enableFiltering: false},
			             { field: 'rate', width:100, enableCellEdit: false, enableFiltering: false }
            			 ]

	};
	
	$scope.gridOptionsIV.onRegisterApi= function(gridApi){
		$scope.gridApiIV = gridApi;

		 gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
	           
	        });
		
		gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			
			$scope.$apply();
		});

	}
	

	$scope.save = function () {

	};

	$scope.applyPercent = function (){
		if($scope.percent != null)
			updateWDandIVByPercent($scope.percent);
		else
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please input %");
	}
	
	$scope.recalculateIV = function recalculateResourceSummaryIV() {
		subcontractService.recalculateResourceSummaryIV($scope.jobNo, $scope.subcontractNo, false)
	   	 .then(
				 function( data ) {
					 if(data == false)
						 modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "IV cannot be recalculated.");
					 else
						 modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "IV has been recalculated.");
				 });
    }

	function getSubcontractDetailForWD() {
		subcontractService.getSubcontractDetailForWD($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					//console.log(data);
					$scope.gridOptions.data = data;
				});
	}
	
	function getResourceSummariesByLineType(objectCode, subsidiaryCode ,lineType, resourceNo) {console.log(objectCode, subsidiaryCode ,lineType, resourceNo);
		resourceSummaryService.getResourceSummariesByLineType($scope.jobNo, $scope.subcontractNo, objectCode, subsidiaryCode, lineType, resourceNo)
	   	 .then(
				 function( data ) {
					 console.log(data);
					 $scope.IV  =data
					 $scope.gridOptionsIV.data=  $scope.IV;
				 });
	    }
	
	function getResourceSummariesBySC() {
		resourceSummaryService.getResourceSummariesBySC($scope.jobNo, $scope.subcontractNo)
	   	 .then(
				 function( data ) {
					 //console.log(data);
					 $scope.gridOptionsIV.data= data;
				 });
	    }
	
	function updateWDandIV(scDetail) {
		subcontractService.updateWDandIV($scope.jobNo, $scope.subcontractNo, scDetail)
	   	 .then(
				 function( data ) {
					 if(data.length!=0){
						 modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', data);
						 $state.reload();
						}
					 else
						 getResourceSummariesByLineType(scDetail.objectCode, scDetail.subsidiaryCode, scDetail.lineType, scDetail.resourceNo);
				 });
	    }
	
	function updateWDandIVByPercent(percent) {
		subcontractService.updateWDandIVByPercent($scope.jobNo, $scope.subcontractNo, percent)
	   	 .then(
				 function( data ) {
					 if(data.length!=0)
						 modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', data);
					 $state.reload();
				 });
	    }
	
}]);

