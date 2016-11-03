mainApp.controller('NavMenuCtrl', ['$http', '$scope', '$location', '$cookies', 'blockUI', 'masterListService', 'modalService', 'adlService', '$state', 'GlobalHelper', '$interval', '$timeout', 'GlobalParameter', 'userpreferenceService', 'rootscopeService', 'uiGridConstants',  
                                   function($http, $scope, $location, $cookies, blockUI, masterListService, modalService, adlService, $state, GlobalHelper, $interval, $timeout, GlobalParameter, userpreferenceService, rootscopeService, uiGridConstants) {
	
	rootscopeService.setEnv();
	$scope.tab = 'profile';
	$scope.selectTab = function(setTab){
		$scope.tab = setTab;
		if(setTab === 'defaultJob'){
			defaultJobListFocus();
			if($scope.resetDefaultJobNo){
				$timeout(function(){
					$scope.defaultJobNo = $scope.resetDefaultJobNo;
					var defaultOption = angular.element('#defaultJobList').find('[value="string:' + $scope.resetDefaultJobNo + '"]');
					var optionTop = defaultOption.length > 0 ? defaultOption.offset().top : 0;
					var selectTop = angular.element('#defaultJobList').offset().top || 0;
					var baseTop = angular.element('#defaultJobList').scrollTop();
					var scrollTo = optionTop > 0 ? baseTop + optionTop - selectTop : baseTop;
					angular.element('#defaultJobList').scrollTop(scrollTo);
				}, 100);			
			}
			if(!$scope.jobs){
				blockUI.start('Loading...');
				$timeout(function(){
				rootscopeService.gettingJobList('ONGOING_JOB_LIST')
				.then(function( response ) {
					$scope.jobs = response.jobs;
						$timeout(function(){
						rootscopeService.gettingJobList('COMPLETED_JOB_LIST')
						.then(function( response ) {
							$scope.jobs = $scope.jobs.concat(response.jobs);
							$scope.jobs.sort(function(a,b){return a.jobNo - b.jobNo;});
							blockUI.stop();
						});
						}, 150);
				});
				}, 150);
			}
		}
	};
	$scope.isSelected = function(checkTab){
		return $scope.tab === checkTab;
	};

	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");
	$scope.subcontractNo = $cookies.get("subcontractNo");
	$scope.subcontractDescription = $cookies.get("subcontractDescription");
	$scope.userIcon = 'resources/images/profile.png';
	$scope.imageServerAddress = GlobalParameter.imageServerAddress //<= require login
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
		rootscopeService.gettingUser()
		.then(function(response){
			if(response.user instanceof Object){
				$scope.user = response.user;
				var iconPath = GlobalParameter.imageServerAddress+$scope.user.StaffID+'.jpg';
				//As the config of http://gammon/ not allow CORS so cannot check if the image is available or 401
				//check with the authType, if Kerberos change the image, if LDAP keep the default
				//console.log('User:' +$scope.user.username + ' Staff id:' + $scope.user.StaffID + ' authType:' + $scope.user.authType);
				//console.log('Default icon:' + $scope.userIcon);
				if($scope.user.authType === 'Kerberos'){
//					console.log('Change user icon to:' + iconPath);
					$scope.userIcon = iconPath;
				}
			}
			
			userpreferenceService.gettingUserPreference()
			.then(function(response){
				updateDefaultJobNo(response.userPreference.DEFAULT_JOB_NO);
			});			
		});
	}
	
	function defaultJobListFocus(){
		$timeout(function(){
			angular.element('#defaultJobList').focus();
		}, 100);
	}
	
	function updateDefaultJobNo(defaultJobNo){
		if(defaultJobNo){
			$scope.defaultJobNo = defaultJobNo;
			$scope.resetDefaultJobNo = angular.copy($scope.defaultJobNo);
		}
	}
	
	$scope.settingDefaultJobNo = function(){
		userpreferenceService.settingDefaultJobNo($scope.defaultJobNo)
		.then(function(response){
			showUpdateDefaultJobStatus();
			updateDefaultJobNo(response.DEFAULT_JOB_NO);
			defaultJobListFocus();
		})
	}
	
	function showUpdateDefaultJobStatus(){
		$scope.showDefaultJobStatus = true;
		$timeout(function(){
			$scope.showDefaultJobStatus = false;
		}, 2000);
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
		rootscopeService.setPreviousStatus(fromState);
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
			if(!rootscopeService.getShowAdminMenu()){
				$state.go('job.dashboard');
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
	var approveOptions = [
        {label: 'Approved', value:"Approved"},
        {label: 'Not Approved', value:"Not Approved"}
        ];
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
//			rowTemplate: GlobalHelper.addressBookRowTemplate('addressBookName', 'addressBookNumber'),
			columnDefs: [
			             { field: 'addressBookNumber', displayName: "No.", width: '60', enableCellEdit: false,
			            	filter:{condition: uiGridConstants.filter.EXACT} 
			             },
			             { field: 'addressBookName', displayName: "Name", width: '100', enableCellEdit: false },
			             { field: 'supplierApproval', displayName: "Approved", width: '80', enableCellEdit: false, headerCellClass:'gridHeaderText',
			            	 filter: { selectOptions: approveOptions, type: uiGridConstants.filter.SELECT, condition: uiGridConstants.filter.STARTS_WITH}, 
			             },
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
	
	$scope.openTips = function(section){
		modalService.open('lg', 'view/infotips-modal.html', 'InfoTipsCtrl', 'Success', $scope ); 
	}

	$scope.openForms = function(){
		modalService.open('lg', 'view/forms-modal.html', 'FormsCtrl', 'Success', $scope ); 
	}

	$scope.openTour = function(){
		modalService.open('lg', 'tourModalTemplate.html', 'TourModalCtrl', 'Success', $scope ); 
	}

	$scope.showRole = function(isShow){
		if(isShow){
			angular.element('#roleMenuCaret').addClass('open');
		} else {
			angular.element('#roleMenuCaret').removeClass('open');
		}
	}
	
	$scope.loadProperties = function(){
		rootscopeService.gettingProperties()
		.then(function(response){
    		$scope.properties = response.properties;
    	}, function(error){
    		console.dir(error);
    	});
	}
	
	angular.element('#userDropPanel').click(function(event){
	     event.stopPropagation();
	 });
	
	angular.element('#roleDropPanel').click(function(event){
	     event.stopPropagation();
	 });
	
	$scope.loadProperties();

	//	$scope.filter = function() {
//		$scope.ObjectCodeGridApi.grid.refresh();
//		$scope.SubsidiaryCodeGridApi.grid.refresh();
//	};
	
//	$scope.ObjectCodeloadGridData();
//	$scope.SubsidiaryCodeloadGridData();
//	$scope.AddressBookloadGridData();
	
}]);