mainApp.controller('AddendumDetailsCtrl', ['$scope' , 'modalService', 'addendumService', 'subcontractService', '$cookies', '$state', 
                                           function($scope ,modalService, addendumService, subcontractService, $cookies, $state) {

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
			//showGridFooter : true,
			//showColumnFooter : true,
			//fastWatch : true,

			exporterMenuPdf: false,
			columnDefs: [
			             { field: 'id', width:80, visible: false},
			             { field: 'typeVo', displayName:"Type", width:80},
		            	 { field: 'bpi',  displayName:"BPI",  width:100 },
		            	 {field: 'description' ,  width:100 },
		            	 { field: 'rateBudgetTba',  width:100 },
			             { field: 'rateAddendumTba' ,  width:100 },
			             { field: 'quantityTba' ,  width:100 },
			             { field: 'amtBudgetTba' ,  width:100 },
			             { field: 'amtAddendumTba' ,  width:100 },
		            	 {field: 'codeObject',  displayName:"Object Code",  width:100 },
		            	 {field: 'codeSubsidiary', displayName:"Subsidiary Code", width:100 },
		            	 {field: 'unit' ,  width:100 },
		            	 {field: 'remarks' ,  width:100 },
		            	 {field: 'idHeaderRef', visible: false}
		            	 ]
	};

	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;
	}



	$scope.saveDetailHeader = function(){
		$scope.disableButtons = true;

		if(addendumDetailHeaderRef!=null)
			updateAddendumDetailHeader(addendumDetailHeaderRef);
		else
			updateAddendumDetailHeader('');
	}

	$scope.deleteDetailHeader = function(){
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
	}
	
	$scope.deleteDetail = function(addendumDetail){
		var dataRows = $scope.gridApi.selection.getSelectedRows();
		if(dataRows.length == 0){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select rows to add addendum.");
			return;
		}
		deleteAddendumDetail(dataRows);
	}

	$scope.updateDetail = function () {
		var dataRows = $scope.gridApi.selection.getSelectedRows();
		if(dataRows.length == 0){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select 1 row to update addendum.");
			return;
		}else if(dataRows.length > 1){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please only select 1 row to update addendum.");
			return;
		}

		console.log(dataRows[0]);
		if(dataRows[0]['typeAction'] == 'ADD'){
			if(dataRows[0]['rateBudgetTba'] == 0){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Budgetd resource cannot be updated here. Only Add or Remove is allowed here.");
				return;
			}else{
				
			}
		}else if(dataRows[0]['typeAction'] == 'UPDATE'){
			
		}else if(dataRows[0]['typeAction'] == 'DELETE'){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Resource deleted from approved Subcontract Detail cannot be updated here.");
			return;
		}


	};

	$scope.open = function(view){
		if(view == 'noBudget')
			modalService.open('lg', 'view/subcontract/addendum/addendum-detail-add-modal.html', 'AddendumDetailsAddCtrl');
		else if(view == 'withBudget')
			modalService.open('lg', 'view/subcontract/addendum/addendum-detail-add-v3-modal.html', 'AddendumDetailV3Ctrl');
		else if(view == 'update')
			modalService.open('lg', 'view/subcontract/addendum/addendum-detail-update-modal.html', 'AddendumDetailUpdateCtrl');

	}

	
	function loadData(){
		if(addendumDetailHeaderRef!=null && addendumDetailHeaderRef.length !=0){
			if(addendumDetailHeaderRef!='Empty'){
				getAddendumDetailHeader(addendumDetailHeaderRef);
				getAddendumDetailsByHeaderRef(addendumDetailHeaderRef);
			}else
				getAddendumDetailsWithoutHeaderRef();
		}
	}
	
	function getSubcontract(){
		subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					console.log("Subcontract");
					console.log(data);
					var subcontract = data;
					if(subcontract.subcontractStatus < 500){
						//Subcontract not awarded
						$scope.disableButtons = true;
					}
					if(subcontract.submittedAddendum == '1' || subcontract.splitTerminateStatus =='1' || subcontract.splitTerminateStatus =='2'){
						//Addendum/Split/Terminate submitted
						$scope.disableButtons = true;
					}
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
						$cookies.put('addendumDetailHeaderRef', headerRef);
						$state.reload();
					}
				});
	}
	
	function deleteAddendumDetail(addendumDetailList){
		addendumService.deleteAddendumDetail($scope.jobNo, $scope.subcontractNo, addendumDetailList)
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