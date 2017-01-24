mainApp.controller('ContraChargeModalCtrl', ['$scope',  'modalService', 'jobService',  'mainCertService', '$cookies', 'uiGridConstants', '$uibModalInstance', 'roundUtil', '$state', '$timeout',
                                                 function($scope, modalService, jobService, mainCertService, $cookies, uiGridConstants, $uibModalInstance, roundUtil, $state, $timeout ) {

	$scope.jobNo = $cookies.get("jobNo");
	$scope.mainCertNo = $cookies.get("mainCertNo");

	$scope.totalPostedCCAmount = 0;
	$scope.totalCumCCAmount = 0;
	
	loadData();

	$scope.gridOptions = {
			enableFiltering: false,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			multiSelect: false,
			enableCellEditOnFocus : true,
			showColumnFooter : true,
			showGridFooter : false,
			//showColumnFooter : true,
			exporterMenuPdf: false,

			rowEditWaitInterval :-1,

			columnDefs: [
			             { field: 'id', visible: false},
			             { field: 'objectCode'},
			             { field: 'subsidiary', displayName: "Subsidiary Code"},
		            	 { field: 'postAmount',  displayName: "Posted Amount", enableCellEdit: false,
		            		 cellClass: 'text-right', cellFilter: 'number:2', 
		            		 aggregationType: uiGridConstants.aggregationTypes.sum,
		            		 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'},
	            		 { field: 'currentAmount', displayName: "Cumulative Amount", 
			            	 cellClass: 'text-right', cellFilter: 'number:2', 
			            	 aggregationType: uiGridConstants.aggregationTypes.sum,
			            	 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'}
	            		 ]
	};

	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;
		
		
		gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			if(colDef.name == 'objectCode' && rowEntity.id !=0){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Object code cannot be modified.");
				return;
			}
			else if(colDef.name == "subsidiary" && rowEntity.id != 0){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Subsidiary code cannot be modified.");
				return;
			}
			
		});
		
		
		gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			if(colDef.name == 'objectCode' && rowEntity.objectCode.length!=6){
				rowEntity.objectCode = oldValue;
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Object code must be 6 digits in length.");
				return;
			}
			else if(colDef.name == "subsidiary" && rowEntity.subsidiary.length!=8){
				rowEntity.subsidiary = oldValue;
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Subsidiary code must be 8 digits in length.");
				return;
			}
			
			if(colDef.name == "currentAmount"){
				if(newValue.length ==0)
					rowEntity.amount = oldValue;
				else{
					rowEntity.currentAmount = roundUtil.round(newValue, 2);
				}
			}
				
		});
		
		gridApi.selection.on.rowSelectionChanged($scope,function(row){
			$scope.removeRowIndex = $scope.gridOptions.data.indexOf(row.entity);
		});
	}


	$scope.addData = function() {
		$scope.gridOptions.data.push({
			"id":0,
			"objectCode": "",
			"subsidiary": "",
			"postAmount": 0,
			"currentAmount": 0
		});
	};

	$scope.deleteData = function() {
		var selectedRow = $scope.gridApi.selection.getSelectedRows();
		if(selectedRow.length == 0){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select row to delete.");
			return;
		}else{
			if(selectedRow[0]['id'] == 0){
				if($scope.removeRowIndex != null){
					if($scope.removeRowIndex == 0)
						$scope.gridOptions.data.splice(0,1);
					else
						$scope.gridOptions.data.splice($scope.removeRowIndex, 1);
				}
			}else{
				if(selectedRow[0]['postAmount'] != 0){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Only Contra charge with zero posted amount can be deleted.");
					return;			
				}
				deleteMainCertContraCharge(selectedRow[0]);
			}
		}

	};

	$scope.save = function() {
		//var gridRows = $scope.gridApi.rowEdit.getDirtyRows();
		//var dataRows = gridRows.map( function( gridRow ) { return gridRow.entity; });
		$scope.disableButtons = true;
		var dataRows = $scope.gridOptions.data;

		if(dataRows.length==0){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "No record has been modified");
			$scope.disableButtons = false;
			return;
		}

		updateMainCertContraChargeList(dataRows);
	};

	function loadData(){
		getMainCertContraChargeList();
		
	}

	
	function getMainCertContraChargeList(){
		mainCertService.getMainCertContraChargeList($scope.jobNo, $scope.mainCertNo)
		.then(
				function( data ) {
					$scope.gridOptions.data = data;
					
					$timeout(function () {
						$scope.totalPostedCCAmount = $scope.gridApi.grid.columns[4].getAggregationValue();
						$scope.totalCumCCAmount = $scope.gridApi.grid.columns[5].getAggregationValue();
					}, 100);
				});
	}

	
	function updateMainCertContraChargeList(contraChargeList){
		mainCertService.updateMainCertContraChargeList($scope.jobNo, $scope.mainCertNo, contraChargeList)
		.then(
				function( data ) {
					if(data != null && data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
						$scope.disableButtons = false;
						return;
					}else{
						$state.reload();
						loadData();
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', 'Contra Charge List has been updated.');
					}
				});
	}
	
	function deleteMainCertContraCharge(contraChargeToDelete){
		mainCertService.deleteMainCertContraCharge(contraChargeToDelete)
		.then(
				function( data ) {
					if(data != null && data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', data);
						return;
					}else{
						$state.reload();
						loadData();
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', 'Contra Charge has been deleted.');
					}
				});
	}
	
	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
	};

	//Listen for location changes and call the callback
	$scope.$on('$locationChangeStart', function(event){
		$uibModalInstance.close();
	});

}]);