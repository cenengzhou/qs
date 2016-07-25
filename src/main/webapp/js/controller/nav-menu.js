mainApp.controller('NavMenuCtrl', ['$http', '$scope', '$location', '$rootScope', '$cookieStore', '$stateParams', 'masterListService', 'modalService',
                                   function($http, $scope, $location, $rootScope, $cookieStore, $stateParams, masterListService, modalService) {
	if($stateParams.jobNo){
		$cookieStore.put('jobNo', $stateParams.jobNo);
		$cookieStore.put('jobDescription', $stateParams.jobDescription);
	}

	$scope.jobNo = $cookieStore.get("jobNo");
	$scope.jobDescription = $cookieStore.get("jobDescription");
	$scope.subcontractNo = $cookieStore.get("subcontractNo");
	$scope.subcontractDescription = $cookieStore.get("subcontractDescription");
	
	$scope.userIcon = 'resources/images/profile.png';
	$scope.imageServerAddress = 'http://gammon/PeopleDirectory_Picture/' //<= require login
//	$scope.imageServerAddress = 'http://ipeople/Upload/PeopleDir/UserPics/'
	$scope.user = {name:'No one'};
	

	$scope.currentPath = $location.path();

	$scope.getCurrentUser = function(){
		$http.get('service/security/getCurrentUser')
		.then(function(response){
			if(response.data instanceof Object){
				$scope.user = response.data;
				//As the config of http://gammon/ not allow CORS so cannot check if the image is available or 401
				//check with the authType, if Kerberos change the image, if LDAP keep the default
				if($scope.user.authType === 'Kerberos'){
					$scope.userIcon = $scope.imageServerAddress+$scope.user.StaffID+'.jpg';
				}
			}
		});
	}
	$scope.getCurrentUser();

	if($scope.currentPath.indexOf("/job")==0){	
		$scope.activeMenu = '';
	}
	else if ($scope.currentPath.indexOf("/cert")==0){
		$scope.activeMenu = 'Certificate';

	}
	else if ($scope.currentPath.indexOf("/repackaging")==0){
		$scope.activeMenu = 'Repackaging';

	}
	else if ($scope.currentPath.indexOf("/subcontract")==0){
		$scope.activeMenu = 'Subcontract';
		$scope.activeSubcontractSideMenu = $scope.currentPath;
	}
	else if ($scope.currentPath.indexOf("/iv")==0){
		$scope.activeMenu = 'IV';

	}else if ($scope.currentPath.indexOf('/transit')==0){
		$scope.activeMenu = 'Transit';
	}else if ($scope.currentPath.indexOf('/admin')==0){
		$scope.activeMenu = 'Admin';
		$scope.activeAdminSideMenu = $scope.currentPath;
	}else if($scope.currentPath.indexOf('/enquiry')==0){
		$scope.activeMenu= 'Enquiry';
		$scope.activeEnquirySideMenu = $scope.currentPath;
	}else if($scope.currentPath.indexOf('/reports')==0){
		$scope.activeMenu= 'Reports'
	}

//	function convertImgToBase64URL(url, callback, outputFormat){
//	    var img = new Image();
//	    img.crossOrigin = 'Anonymous';
//	    img.onload = function(){
//	        var canvas = document.createElement('CANVAS'),
//	        ctx = canvas.getContext('2d'), dataURL;
//	        canvas.height = img.height;
//	        canvas.width = img.width;
//	        ctx.drawImage(img, 0, 0);
//	        dataURL = canvas.toDataURL(outputFormat);
//	        callback(dataURL);
//	        canvas = null; 
//	    };
//	    img.src = url;
//	}
//	//same origin access policy need set at desction server
//	convertImgToBase64URL($scope.imageServerAddress + $scope.user.StaffID + '.jpg', 
//			function(base64Img){
//			      $('#userImage').find('img').attr('src', base64Img);  
//			   
//			});
	
	$scope.ObjectCodeGridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : false,
			enableRowSelection: false,
			enableSelectAll: false,
			enableFullRowSelection: true,
			multiSelect: false,
			showGridFooter : false,
			enableCellEditOnFocus : false,
			allowCellFocus: false,
			enableCellSelection: false,
			columnDefs: [
			             { field: 'objectCode', displayName: "Code", width: '80', enableCellEdit: false },
			             { field: 'description', displayName: "Description", width: '120', enableCellEdit: false }
            			 ]
	};
	
	$scope.ObjectCodeGridOptions.onRegisterApi = function (gridApi) {
		  $scope.ObjectCodeGridApi = gridApi;
		  $scope.ObjectCodeGridApi.grid.refresh();
	}
	
	$scope.ObjectCodeloadGridData = function(){
//		$scope.blockEnquiryClient.start('Loading...')
			masterListService.searchObjectList('*')
		    .then(function(data) {
				if(angular.isArray(data)){
					$scope.ObjectCodeGridOptions.data = data;
//					$scope.blockEnquiryClient.stop();
				}
		}, function(data){
//			$scope.blockEnquiryClient.stop();
//			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data ); 
		})

	}
	
	$scope.SubsidiaryCodeGridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : false,
			enableRowSelection: false,
			enableSelectAll: false,
			enableFullRowSelection: true,
			multiSelect: false,
			showGridFooter : false,
			enableCellEditOnFocus : false,
			allowCellFocus: false,
			enableCellSelection: false,
			columnDefs: [
			              { field: 'subsidiaryCode', displayName: "Code", width: '80', enableCellEdit: false },
			             { field: 'description', displayName: "Description", width: '120', enableCellEdit: false }
            			 ]
	};
	
	$scope.SubsidiaryCodeGridOptions.onRegisterApi = function (gridApi) {
		  $scope.SubsidiaryCodeGridApi = gridApi;
		  $scope.SubsidiaryCodeGridApi.grid.refresh();
	}
	
	$scope.SubsidiaryCodeloadGridData = function(){
//		$scope.blockEnquiryClient.start('Loading...')
			masterListService.searchSubsidiaryList('*')
		    .then(function(data) {
				if(angular.isArray(data)){
					$scope.SubsidiaryCodeGridOptions.data = data;
//					$scope.blockEnquiryClient.stop();
				}
		}, function(data){
//			$scope.blockEnquiryClient.stop();
//			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data ); 
		})
	}
	
	$scope.filter = function() {
		$scope.ObjectCodeGridApi.grid.refresh();
		$scope.SubsidiaryCodeGridApi.grid.refresh();
	};
	
	$scope.ObjectCodeloadGridData();
	$scope.SubsidiaryCodeloadGridData();
}]);