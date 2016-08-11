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
			//enableSelectAll: true,
			//enableFullRowSelection: true,
			multiSelect: false,
			//showGridFooter : true,
			//showColumnFooter : true,
			//fastWatch : true,


			columnDefs: [
			             { field: 'id', width:80, visible: false},
			             { field: 'typeVo', width:80},
		            	 { field: 'bpi',  width:100 },
		            	 {field: 'description' ,  width:100 },
		            	 { field: 'rateBudget',  width:100 },
		            	 { field: 'rateAddendum' ,  width:100 },
		            	 { field: 'quantity' ,  width:100 },
		            	 { field: 'amtBudget' ,  width:100 },
		            	 { field: 'amtAddendum' ,  width:100 },
		            	 {field: 'codeObject' ,  width:100 },
		            	 {field: 'codeSubsidiary' ,  width:100 },
		            	 {field: 'unit' ,  width:100 },
		            	 {field: 'remarks' ,  width:100 },
		            	 {field: 'idHeaderRef' ,  width:100 }
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


	function loadData(){
		if(addendumDetailHeaderRef!=null && addendumDetailHeaderRef.length !=0){
			getAddendumDetailHeader(addendumDetailHeaderRef);
			getAddendumDetailsByHeaderRef(addendumDetailHeaderRef);
		}
		else
			console.log("NULL");
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

	$scope.open = function(view){
		if(view == 'noBudget')
			modalService.open('lg', 'view/subcontract/addendum/addendum-detail-add-modal.html', 'AddendumDetailsAddCtrl');
		else if(view == 'withBudget')
			modalService.open('lg', 'view/subcontract/addendum/addendum-detail-add-v3-modal.html', 'AddendumDetailV3Ctrl');
		else if(view == 'update')
			modalService.open('lg', 'view/subcontract/addendum/addendum-detail-update-modal.html', 'AddendumDetailUpdateCtrl');

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
					console.log("getAddendumDetailHeader");
					console.log(data);
					$scope.detailHeader = data;
				});
	}

	function getAddendumDetailsByHeaderRef(headerRef){
		addendumService.getAddendumDetailsByHeaderRef(headerRef)
		.then(
				function( data ) {
					console.log("getAddendumDetailsByHeaderRef");
					console.log(data);
					$scope.gridOptions.data = data;
					/*if($scope.addendum.status == "PENDING")
						$scope.disableButtons = false;
					else
						$scope.disableButtons = true;*/
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