mainApp.controller('SubcontractWorkdoneCtrl', ['$scope', 'subcontractService', 'resourceSummaryService', 'uiGridConstants', 'modalService', '$state', '$timeout',
                                               function ($scope, subcontractService, resourceSummaryService, uiGridConstants, modalService, $state, $timeout) {

	$scope.percent = "";
	$scope.lastID ="";
	
	getSubcontractDetailForWD();
	getResourceSummariesBySC();
 
	$scope.checkEditable = function($scope){
//		subcontract.paymentStatus === 'F' [modal] "SC Package was final paid."
		if($scope.row.entity.subcontract.paymentStatus === 'F'){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'Subcontract was final paid');
			return;
		}
//		SubcontractDetail.lineType === BQ && (&& SubcontractDetail.manualInputSCWD === Y || SubcontractDetail.LEGACYJOB === Y)
//		[modal] "Workdone cannot be updated in BQ Line"
		/*if($scope.row.entity.lineType === 'BQ' && ($scope.row.entity.subcontract.jobInfo.manualInputSCWD === 'Y' || $scope.row.entity.subcontract.jobInfo.legacyJob === 'Y')){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'Workdone cannot be updated in BQ Line');
			return;
		}*/
//		SubcontractDetail.sourceType === D && SubcontractDetail.lineType !== OA
//		[modal] "Workdone cannot be updated in "+SubcontractDetail.lineType+" Line"
		if($scope.row.entity.sourceType === 'D' && $scope.row.entity.lineType !== 'OA') {
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'Workdone cannot be updated in '+$scope.row.entity.lineType+' Line');
			return;
		}
		
//		jobinfo.repackagingType = 3
//		&& SubcontractDetail.lineType in (BQ, V3, V1)
//		&& SubcontractDetail.costRate!=0
//		[modal] "Please update the workdone of BQ/V1/V3 lines from 'IV Update by Resouce' Window or 'IV Update by BQItem' Window."
		if($scope.row.entity.subcontract.jobInfo.repackaging === 3
			&& ['BQ', 'V3', 'V1'].indexOf(rowEntity.lineType) >=0 
			&& $scope.row.entity.costRate != 0) {
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'Please update the workdone of BQ/V1/V3 lines from \'IV Update by Resouce\' Window or \'IV Update by BQItem\' Window');
				return;
		}
//		SubcontractDetail.lineType in (C1, C2, RR, RA, AP)
//		&& SubcontractDetail.amountCumulativeWD != 0
//		[modal] "Invalid inputed value. Cum Work Done Qty must be zero."
		if(['C1', 'C2', 'RR', 'RA', 'AP'].indexOf($scope.row.entity.lineType) >= 0
			&& $scope.row.entity.amountCumulativeWD !== 0){
	 			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'Invalid inputed value. Cum Work Done Qty must be zero');
	 			return;
 		}
//		SubcontractDetail.amountCumulativeWD > SubcontractDetail.quantity
//		&& (!SubcontractDetail.subsidiaryCode.startsWith("4") && SubcontractDetail.subsidiaryCode.substring(2, 4) !== 80)
//		&& SubcontractDetail.approved === A
//		&& SubcontractDetail.lineType in (BQ, V3, V1)
//		&& SubcontractDetail.costRate != 0
//		[modal] "Invalid inputed value. Cum Work Done Qty cannot be larger than BQ Qty."
		var t = $scope.row.entity.subsidiaryCode.startsWith('4');
		if($scope.row.entity.amountCumulativeWD > $scope.row.entity.quantity
			&& (!$scope.row.entity.subsidiaryCode.toString().startsWith('4') && $scope.row.entity.subsidiaryCode.substring(2, 4) !== 80)
			&& $scope.row.entity.approved === 'A'
			&& ['BQ', 'V3', 'V1'].indexOf($scope.row.entity.lineType) >= 0
			&& $scope.row.entity.costRate != 0){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'Invalid inputed value. Cum Work Done Qty cannot be larger than BQ Qty');
				return;
		}
		return true;
	}
	
	$scope.gridOptions = {
			enableSorting: true,
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableColumnMoving: true,
			//showGridFooter : true,
			showColumnFooter : true,
			//fastWatch : true,
			importerDataAddCallback: $scope.importWDData,
			exporterMenuPdf: false,
			
			enableCellEditOnFocus : true,

			rowEditWaitInterval :-1,
			
			columnDefs: [
		             	{ field: 'id', width: 20, enableCellEdit: false, visible:false},
			             { field: 'lineType', width: 40, enableCellEdit: false, pinnedLeft:true},
			             { field: 'description', width: 100, enableCellEdit: false, pinnedLeft:true},

			             {field: 'amountBudget', displayName: "Budget Amount", width: 120, visible:false, enableCellEdit: false,enableFiltering: false ,
			            	 cellClass: 'text-right', cellFilter: 'number:2'},
			             {field: 'amountSubcontract', displayName: "SC Amount", width: 120, enableCellEdit: false, enableFiltering: false ,
			            	 cellClass: 'text-right', cellFilter: 'number:2'},
		            	 {field: 'amountSubcontractTBA', displayName: "TBA Amount", width: 120, visible:false, enableCellEdit: false, enableFiltering: false ,
		            		 cellClass: 'text-right', cellFilter: 'number:2'},
			             { field: 'amountCumulativeWD', displayName: "Cum WD Amount", width: 120, enableFiltering: false ,
			            	aggregationType: uiGridConstants.aggregationTypes.sum, cellEditableCondition: $scope.checkEditable,
			            	footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>',
			            	cellClass: 'blue text-right', cellFilter: 'number:2'},
			             { field: 'amountPostedWD', displayName: "Posted WD Amount", enableCellEdit: false, enableFiltering: false ,
	            			width: 120, aggregationType: uiGridConstants.aggregationTypes.sum,
	            		 	footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>',
	            		 	cellClass: 'text-right', cellFilter: 'number:2'},
			             { field: 'amountCumulativeCert', displayName: "Cum Certified Amount", enableCellEdit: false,enableFiltering: false ,
	            		 	width: 120, aggregationType: uiGridConstants.aggregationTypes.sum,
        		 			footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>',
		            		cellClass: 'text-right', cellFilter: 'number:2'},
			             { field: 'amountPostedCert', displayName: "Posted Certified Amount", width: 120, visible:false, enableCellEdit: false, enableFiltering: false ,
			            	 cellClass: 'text-right', cellFilter: 'number:2'},

			            { field: 'quantity', width: 100, enableCellEdit: false, enableFiltering: false ,
			            		 cellClass: 'text-right', cellFilter: 'number:2'},
	            		 {field: 'toBeApprovedQuantity', width: 100, enableCellEdit: false,visible:false, enableFiltering: false},

	            		 {field: 'costRate', width: 60, enableCellEdit: false, enableFiltering: false},
	            		 {field: 'scRate', width: 60, enableCellEdit: false, enableFiltering: false},
	            		 {field: 'toBeApprovedRate', width: 80, enableCellEdit: false, visible:false, enableFiltering: false},
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
				//checkEditable()
			}
        });
		
		gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			if(newValue !== oldValue){
				$scope.gridApi.rowEdit.setRowsDirty( [rowEntity]);
				$scope.gridDirtyRows = $scope.gridApi.rowEdit.getDirtyRows($scope.gridApi);
				
//				record.set("currentWorkDoneAmt", String.valueOf(CalculationUtil.round(totalAmount*(currWDQty/100),2)));
//				record.set("projProvisionAmt", String.valueOf(CalculationUtil.round(totalAmount*(currWDQty-currCertQty)/100,2)));
//				record.set("provisionAmt", String.valueOf(CalculationUtil.round(totalAmount*(currWDQty-postedCertQty)/100,2)));
//				record.set("ivAmount", String.valueOf(CalculationUtil.round(costRate*currWDQty,2)));
				var cumWorkDoneQuantity = rowEntity.cumWorkDoneQuantity !== undefined ? rowEntity.cumWorkDoneQuantity : 0.0;
				rowEntity.cumWorkDoneQuantity = rowEntity.amountSubcontract / rowEntity.amountCumulativeWD;
				rowEntity.projectedProvision = rowEntity.amountSubcontract * (rowEntity.amountCumulativeWD - rowEntity.amountCumulativeCert) / 100;
				rowEntity.provision = rowEntity.amountSubcontract * (rowEntity.amountCumulativeWD - rowEntity.postedCertifiedQuantity) / 100;
				$scope.gridApiIV.grid.rows.forEach(function(row){
					row.entity.currIVAmount = rowEntity.costRate * rowEntity.cumWorkDoneQuantity;
				})
				
			}
		});

	}
	$scope.gridDirtyRows = [];

	$scope.gridOptionsIV = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			showColumnFooter : true,
			enableCellEditOnFocus : true,
			exporterMenuPdf: false,
			rowEditWaitInterval :-1,


			columnDefs: [
			             { field: 'id', width:50 , visible:false, enableCellEdit: false},
			             { field: 'packageNo', width:70, displayName:"Subcontract No.", visible:false, enableCellEdit: false},
			             { field: 'objectCode', enableCellEdit: false , width:70, pinnedLeft:true},
			             { field: 'subsidiaryCode',width:70, enableCellEdit: false, pinnedLeft:true},
			             { field: 'resourceDescription', width:80, displayName: "Description", enableCellEdit: false },
			             {field: 'currIVAmount', displayName: "Cum. IV Amount", width:130, enableFiltering: false, cellClass: "blue", 
			            	 aggregationType: uiGridConstants.aggregationTypes.sum,
			            	footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>',
		            		cellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;">{{COL_FIELD | number:2}}</div>'},
		            	 /*{field: 'ivMovement', enableFiltering: false, width:100, aggregationType: uiGridConstants.aggregationTypes.sum,
	            		 cellTemplate: '<div class="ui-grid-cell-contents" style="color:blue;text-align:right;">{{COL_FIELD}}</div>',
	            		 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'},*/
	            		 {field: 'postedIVAmount', width:130,  enableCellEdit: false, enableFiltering: false,  
	            			aggregationType: uiGridConstants.aggregationTypes.sum,
			            	footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>',
		            		cellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;">{{COL_FIELD | number:2}}</div>'},
            			 {field: 'amount',width:100,  enableCellEdit: false, enableFiltering: false },
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
			
//			$scope.$apply();
		});

	}

	//Save Function
	$scope.update = function () {
		var cleanRows = [];
		$scope.gridDirtyRows.forEach(function(row){
			updateWDandIV(row.entity);
			cleanRows.push(row.entity);
		});
		$scope.gridApi.rowEdit.setRowsClean(cleanRows);
		$scope.gridDirtyRows = [];
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
	
	function getResourceSummariesByLineType(objectCode, subsidiaryCode ,lineType, resourceNo) {
		resourceSummaryService.getResourceSummariesByLineType($scope.jobNo, $scope.subcontractNo, objectCode, subsidiaryCode, lineType, resourceNo)
	   	 .then(
				 function( data ) {
					 //console.log(data);
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
//						 modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', data);
//						 $state.reload();
						}
//					 else
//						 getResourceSummariesByLineType(scDetail.objectCode, scDetail.subsidiaryCode, scDetail.lineType, scDetail.resourceNo);
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

