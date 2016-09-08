mainApp.controller('NavMenuCtrl', ['$http', '$scope', '$location', '$cookies', 'masterListService', 'modalService', 'adlService', '$state', 'GlobalHelper', '$rootScope', '$interval',
                                   function($http, $scope, $location, $cookies, masterListService, modalService, adlService, $state, GlobalHelper, $rootScope, $interval) {
	
	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");
	$scope.subcontractNo = $cookies.get("subcontractNo");
	$scope.subcontractDescription = $cookies.get("subcontractDescription");
	$scope.userIcon = 'resources/images/profile.png';
	$scope.imageServerAddress = 'http://gammon.gamska.com/PeopleDirectory_Picture/' //<= require login
//	$scope.imageServerAddress = 'http://ipeople/Upload/PeopleDir/UserPics/'
	$scope.GlobalHelper = GlobalHelper;
	
	$scope.objectLoaded = false;
	$scope.subsidiaryLoaded = false;
	$scope.addressLoaded = false;
	$scope.subAccountPanel = 'Object';
	$scope.showCodePanel = function(panel){
		$scope.showObjectCode = false;
		$scope.showSubsidiaryCode=false;
		$scope.showAddressBook=false;
		
		switch(panel){
		case 'Address':
			$scope.AddressBookloadGridData();
			$scope.showAddressBook = true;
			break;
		case 'Object':
			$scope.ObjectCodeloadGridData();
			$scope.showObjectCode = true;
			$scope.subAccountPanel = 'Object';
			break;
		case 'Subsidiary':
			$scope.SubsidiaryCodeloadGridData();
			$scope.showSubsidiaryCode = true;
			$scope.subAccountPanel = 'Subsidiary';
			break;
		case 'Account':
			if($scope.subAccountPanel === 'Object'){
				$scope.ObjectCodeloadGridData();
				$scope.showObjectCode = true;
			}
			if($scope.subAccountPanel === 'Subsidiary'){
				$scope.SubsidiaryCodeloadGridData();
				$scope.showSubsidiaryCode = true;
			}
			break;
		}
	}

	$scope.currentPath = $location.path();

	$scope.getCurrentUser = function(){
		$http.get('service/security/getCurrentUser')
		.then(function(response){
			if(response.data instanceof Object){
				$scope.user = response.data;
				$rootScope.showQSAdmin = GlobalHelper.containRole('ROLE_PCMS_QS_ADMIN', $scope.user.UserRoles);
				$rootScope.showIMSAdmin = GlobalHelper.containRole('ROLE_PCMS_IMS_ADMIN', $scope.user.UserRoles);
				if($rootScope.showQSAdmin || $rootScope.showIMSAdmin){
					$rootScope.showAdminMenu = true;
				} else {
					$rootScope.showAdminMenu = GlobalHelper.containRole('ROLE_PCMS_IMS_ENQ', $scope.user.UserRoles);
				}
				$rootScope.showQSAdmin = true;
				var iconPath = $scope.imageServerAddress+$scope.user.StaffID+'.jpg';
				//As the config of http://gammon/ not allow CORS so cannot check if the image is available or 401
				//check with the authType, if Kerberos change the image, if LDAP keep the default
				console.log('User:' +$scope.user.username + ' Staff id:' + $scope.user.StaffID + ' authType:' + $scope.user.authType);
				console.log('Default icon:' + $scope.userIcon);
				if($scope.user.authType === 'Kerberos'){
					console.log('Change user icon to:' + iconPath);
					$scope.userIcon = iconPath;
				}
			}
		});
	}
	$scope.getCurrentUser();
	$scope.menuScroll = 0;
	$scope.scrollStep = 20;
	$scope.menuWidth = document.getElementById('innerMenu').clientWidth;
	$scope.menuAutoScroll = null;
	$scope.stopScrolLeft = function(){
		if (angular.isDefined($scope.menuAutoScrollLeft)) {
            $interval.cancel($scope.menuAutoScrollLeft);
//            console.log("stop scrollLeft:" + $scope.menuScroll);
        }
	}
	$scope.scrollLeft = function(){
		$scope.menuAutoScrollLeft = $interval(function(){
			if($scope.menuScroll > 0) {
				var menubar = document.getElementById("menubar");
				menubar.scrollLeft -= $scope.scrollStep;
				$scope.menuScroll -= $scope.scrollStep;
//				console.log("scrollLeft:" + $scope.menuScroll);
			}
		}, 100);
	}
	$scope.stopScrollRight = function(){
		if (angular.isDefined($scope.menuAutoScrollRight)) {
            $interval.cancel($scope.menuAutoScrollRight);
//            console.log("stop scrollRight:" + $scope.menuScroll);
        }
	}
	$scope.scrollRight = function(){
		$scope.menuAutoScrollRight = $interval(function(){
			if($scope.menuScroll < ($scope.menuWidth - $scope.menuBarWidth)) {
				var menubar = document.getElementById("menubar");
				menubar.scrollLeft += $scope.scrollStep;
				$scope.menuScroll += $scope.scrollStep;
	//			console.log("scrollRight:" + $scope.menuScroll);
			}
		}, 100);
	}
	
	$scope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
    	$scope.currentPath = $location.path();
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
		}
		else if ($scope.currentPath.indexOf("/iv")==0){
			$scope.activeMenu = 'IV';
	
		}else if ($scope.currentPath.indexOf('/transit')==0){
			$scope.activeMenu = 'Transit';
		}else if ($scope.currentPath.indexOf('/admin')==0){
			if(!$rootScope.showAdminMenu){
				//$state.go('job.dashboard');
			}
			$scope.activeMenu = 'Admin';
			$scope.activeAdminSideMenu = $scope.currentPath;
		}else if($scope.currentPath.indexOf('/enquiry')==0){
			$scope.activeMenu= 'Enquiry';
			$scope.activeEnquirySideMenu = $scope.currentPath;
		}else if($scope.currentPath.indexOf('/reports')==0){
			$scope.activeMenu= 'Reports'
		}
    });
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
			enableGridMenu : true,
			exporterMenuPdf: false,
			enableRowSelection: false,
			enableSelectAll: false,
			enableFullRowSelection: false,
			multiSelect: false,
			showGridFooter : false,
			enableCellEditOnFocus : false,
			allowCellFocus: false,
			enableCellSelection: false,
			columnDefs: [
			             { field: 'objectCode', displayName: "Code", width: '80', enableCellEdit: false },
			             { field: 'description', displayName: "Description", width: '170', enableCellEdit: false }
            			 ]
	};
	
	$scope.ObjectCodeGridOptions.onRegisterApi = function (gridApi) {
		  $scope.ObjectCodeGridApi = gridApi;
		  $scope.ObjectCodeGridApi.grid.refresh();
	}
	
	$scope.ObjectCodeloadGridData = function(){
		if($scope.objectLoaded === false){
			masterListService.searchObjectList('*')
		    .then(function(data) {
				if(angular.isArray(data)){
					$scope.ObjectCodeGridOptions.data = data;
					$scope.objectLoaded = true;
				}
		}, function(data){
//			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data ); 
		});
		}

	}
	
	$scope.SubsidiaryCodeGridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			exporterMenuPdf: false,
			enableRowSelection: false,
			enableSelectAll: false,
			enableFullRowSelection: false,
			multiSelect: false,
			showGridFooter : false,
			enableCellEditOnFocus : false,
			allowCellFocus: false,
			enableCellSelection: false,
			columnDefs: [
			              { field: 'subsidiaryCode', displayName: "Code", width: '80', enableCellEdit: false },
			             { field: 'description', displayName: "Description", width: '170', enableCellEdit: false }
            			 ]
	};
	
	$scope.SubsidiaryCodeGridOptions.onRegisterApi = function (gridApi) {
		  $scope.SubsidiaryCodeGridApi = gridApi;
		  $scope.SubsidiaryCodeGridApi.grid.refresh();
	}
	
	$scope.SubsidiaryCodeloadGridData = function(){
		if($scope.subsidiaryLoaded === false){
			masterListService.searchSubsidiaryList('*')
		    .then(function(data) {
				if(angular.isArray(data)){
					$scope.SubsidiaryCodeGridOptions.data = data;
					$scope.subsidiaryLoaded = true;
				}
		}, function(data){
//			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data ); 
		});
		}
	}
	
	$scope.AddressBookGridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			exporterMenuPdf: false,
			enableRowSelection: false,
			enableSelectAll: false,
			enableFullRowSelection: false,
			multiSelect: false,
			showGridFooter : false,
			enableCellEditOnFocus : false,
			allowCellFocus: false,
			enableCellSelection: false,
			columnDefs: [
			             { field: 'addressBookNumber', displayName: "No.", width: '50', enableCellEdit: false },
			             { field: 'addressBookName', displayName: "Name", width: '100', enableCellEdit: false },
			             { field: 'supplierApproval', displayName: "Approved", width: '100', enableCellEdit: false }
            			 ]
	};
	
	$scope.AddressBookGridOptions.onRegisterApi = function (gridApi) {
		  $scope.ObjectCodeGridApi = gridApi;
		  $scope.ObjectCodeGridApi.grid.refresh();
	}
	
	$scope.AddressBookloadGridData = function(){
		if($scope.addressLoaded === false){
			adlService.getAddressBookListOfSubcontractorAndClient()
		    .then(function(data) {
				if(angular.isArray(data)){
					$scope.AddressBookGridOptions.data = data;
					$scope.addressLoaded = true;
				}
		}, function(data){
//			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data ); 
		});
		}
	}

	//	$scope.filter = function() {
//		$scope.ObjectCodeGridApi.grid.refresh();
//		$scope.SubsidiaryCodeGridApi.grid.refresh();
//	};
	
//	$scope.ObjectCodeloadGridData();
//	$scope.SubsidiaryCodeloadGridData();
//	$scope.AddressBookloadGridData();
	
}]);