mainApp.controller('AddendumDetailsCtrl', ['$scope' , 'modalService', 'addendumService', 'subcontractService', '$cookies', '$state', 'uiGridConstants','$location',
                                           function($scope ,modalService, addendumService, subcontractService, $cookies, $state, uiGridConstants, $location) {

	$scope.addendumNo = $cookies.get('addendumNo');
	var addendumDetailHeaderRef = $cookies.get('addendumDetailHeaderRef');
	
	loadData();

	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			//enableFullRowSelection: true,
			multiSelect: true,
			showGridFooter : false,
			showColumnFooter : false,
			//fastWatch : true,

			exporterMenuPdf: false,
			columnDefs: [
			             { field: 'id', width:80, visible: false},
			             { field: 'typeVo', displayName:"Type", width:50},
		            	 { field: 'bpi',  displayName:"BPI",  width:90 },
		            	 {field: 'description' ,  width:100 },
		            	 { field: 'quantity' , displayName:"Quantity", width:100, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:4' },
		            	 { field: 'rateBudget', displayName:"Budget Rate", width:100, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:4' },
			             { field: 'amtBudget' , displayName:"Budget Amount", width:100, enableFiltering: false, 
			            	 cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            			var c = 'text-right';
			            			if (row.entity.amtBudget < 0) {
			            				c += ' red';
			            			}
			            			return c;
			            		},
			            		cellFilter : 'number:2',
			            		
		            	},
			             
			             { field: 'rateAddendum' , displayName:"SC Rate", width:100, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:4' },
			             { field: 'amtAddendum' , displayName:"Amount",  width:100, enableFiltering: false, 
		            	 cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
		            			var c = 'text-right';
		            			if (row.entity.amtAddendum < 0) {
		            				c += ' red';
		            			}
		            			return c;
		            		},
		            		cellFilter : 'number:2',
			             },
						 {field: 'typeRecoverable', displayName:"Recoverable", width:120,
							 cellTemplate: "<div class='ui-grid-cell-contents'>{{row.entity.typeRecoverable ? row.entity.typeRecoverable == 'R' ? 'Recoverable' : 'Non-Recoverable' : null}}</div>"},
		            	 {field: 'codeObject',  displayName:"Object Code",  width:80 },
		            	 {field: 'codeSubsidiary', displayName:"Subsidiary Code", width:80 },
		            	 {field: 'noSubcontractChargedRef', displayName:"Corr. Subcontract No.", width:80 },
		            	 {field: 'codeObjectForDaywork', displayName:"Alt. Object Code", width:80 },
		            	 {field: 'unit' ,  width:50 },
		            	 {field: 'remarks' ,  width:100 },
		            	 {field: 'typeAction', displayName:"Action", width:100, visible: true},
		            	 {field: 'idHeaderRef', width:100, visible: false},
		            	 {field: 'idResourceSummary', width:100, visible: false}
		            	 ],
   			rowTemplate: '<div ng-class="{\'red\':row.entity.typeAction==\'DELETE\', \'blue\':row.entity.typeAction==\'UPDATE\' }"><div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" ui-grid-cell></div></div>'
	};

	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;
	}



	$scope.saveDetailHeader = function(){

		if(addendumDetailHeaderRef!=null){
			$scope.disableButtons = true;
			if($scope.detailHeader!=null && $scope.detailHeader.description!=null && $scope.detailHeader.description.trim().length >0)
				updateAddendumDetailHeader(addendumDetailHeaderRef);
			else{
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please input header.");
				$scope.disableButtons = false;
			}
		}else
			updateAddendumDetailHeader('');
		
	}

	$scope.deleteDetailHeader = function(){
		if(addendumDetailHeaderRef!=null && addendumDetailHeaderRef.trim().length >0){
			addendumService.deleteAddendumDetailHeader(addendumDetailHeaderRef)
			.then(
					function( data ) {
						if(data.length != 0){
							modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
							$scope.disableButtons = false;
						}else{
							modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Addendum Detail Header has been deleted.");
							$cookies.put('addendumDetailHeaderRef', '');
							$state.reload();
						}
					});
		}else
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select a header to delete.");
	}
	
	$scope.deleteDetail = function(addendumDetail){
		var dataRows = $scope.gridApi.selection.getSelectedRows();
		if(dataRows.length == 0){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select rows to delete addendum.");
			return;
		}
		deleteAddendumDetail(dataRows);
	}

	$scope.updateDetail = function () {
		var dataRows = $scope.gridApi.selection.getSelectedRows();
		if(dataRows.length == 0 || dataRows.length > 1){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select 1 row to update addendum.");
			return;
		}

		if(dataRows[0]['typeAction'] == 'DELETE'){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Resource deleted from approved Subcontract Detail cannot be updated here.");
			return;
		}else{
			modalService.open('lg', 'view/subcontract/addendum/addendum-detail-add-modal.html', 'AddendumDetailsAddCtrl', 'UPDATE', dataRows[0]);
		}


	};

	$scope.open = function(view){
		if(view == 'noBudget')
			modalService.open('lg', 'view/subcontract/addendum/addendum-detail-add-modal.html', 'AddendumDetailsAddCtrl', 'ADD'); // Add V1/V2 (No Budget)
		else if(view == 'withBudget')
			modalService.open('lg', 'view/subcontract/addendum/addendum-detail-add-v3-modal.html', 'AddendumDetailV3Ctrl'); //Add V1/V3 (With Budget)
		else if(view == 'update')
			modalService.open('lg', 'view/subcontract/addendum/addendum-detail-update-modal.html', 'AddendumDetailUpdateCtrl'); // Update Approved Addendum

	}

	
	function loadData(){
		if($scope.addendumNo != null && $scope.addendumNo.length != 0){
			getSubcontract();
			if(addendumDetailHeaderRef!=null && addendumDetailHeaderRef.length !=0){
				if(addendumDetailHeaderRef!='Empty'){
					getAddendumDetailHeader(addendumDetailHeaderRef);
					getAddendumDetailsByHeaderRef(addendumDetailHeaderRef);
				}else
					getAddendumDetailsWithoutHeaderRef();
			}
		}else{
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Addendum does not exist. Please create addendum title first.");
			$scope.disableButtons = true;
			return;
		}
	}
	
	
	function getAddendum(){
		addendumService.getAddendum($scope.jobNo, $scope.subcontractNo, $scope.addendumNo)
		.then(
				function( data ) {
					$scope.addendum = data;
					if($scope.addendum.length==0 || $scope.addendum.status == "PENDING")
						$scope.disableButtons = false;
					else
						$scope.disableButtons = true;

				});
	}
	
	function getSubcontract(){
		subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					if(data.scStatus < 500 || data.paymentStatus == 'F' || data.splitTerminateStatus ==1  || data.splitTerminateStatus ==2 || data.splitTerminateStatus ==4|| data.submittedAddendum ==1)
						$scope.disableButtons = true;
					else 
						$scope.disableButtons = false;
					
					getAddendum();
				});
	}
	
	function getAddendumDetailHeader(headerRef){
		addendumService.getAddendumDetailHeader(headerRef)
		.then(
				function( data ) {
					$scope.detailHeader = data;
				});
	}

	function getAddendumDetailsByHeaderRef(headerRef){
		addendumService.getAddendumDetailsByHeaderRef(headerRef)
		.then(
				function( data ) {
					$scope.gridOptions.data = data;
				});
	}
	
	function getAddendumDetailsWithoutHeaderRef(){
		addendumService.getAddendumDetailsWithoutHeaderRef($scope.jobNo, $scope.subcontractNo, $scope.addendumNo)
		.then(
				function( data ) {
					$scope.gridOptions.data = data;
				});
	}
	
	
	function updateAddendumDetailHeader(headerRef){
		addendumService.updateAddendumDetailHeader($scope.jobNo, $scope.subcontractNo, $scope.addendumNo, headerRef, $scope.detailHeader.description)
		.then(
				function( data ) {
					if(data.length != 0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
						$scope.disableButtons = false;
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Addendum has been updated.");
						$location.path("/subcontract/addendum/tab/detail-list");
						//$state.reload();
					}
				});
	}
	
	function deleteAddendumDetail(addendumDetailList){
		addendumService.deleteAddendumDetail($scope.jobNo, $scope.subcontractNo, $scope.addendumNo, addendumDetailList)
		.then(
				function( data ) {
					if(data.length != 0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
						$scope.disableButtons = false;
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Addendum Detail has been deleted.");
						$state.reload();
					}
				});
	}

	$scope.$on('$stateChangeStart',function(event,next) {
		if(next.url != '/details'){
			$cookies.put('addendumDetailHeaderRef', '');
		}
	});

}]);