mainApp.controller('TransitCompleteCtrl', ['$scope', '$rootScope', 'modalService', 'transitService', 'jdeService', '$cookies', '$window', '$timeout', 'rootscopeService', 'confirmService', 'resourceSummaryService', 'GlobalHelper',
                          function($scope, $rootScope, modalService, transitService, jdeService, $cookies, $window, $timeout, rootscopeService, confirmService, resourceSummaryService, GlobalHelper) {
	rootscopeService.setSelectedTips('');
	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");
	
	
    getTransit();
    
    function getTransit(){
    	$scope.disableImportBQ = true;
    	$scope.disableImportResources = true;
    	$scope.disableConfirmResouces = true;
    	$scope.disablePrintReport = true;
    	$scope.disableCompleteTransit = true;
    	
    	transitService.getTransit($scope.jobNo)
    	.then(function(data){
	    	$scope.transit = data;
	    	$rootScope.transitLocked = data.systemStatus == 'LOCKED';
	    	$scope.$emit("UpdateTransitStatus", data.status);
	    	if(data instanceof Object) {
	    		if($scope.transit.status === 'Header Created'){
	    			$scope.disableImportBQ = false;
	    			$scope.disableImportResources = false;
	    		} else if($scope.transit.status === 'BQ Items Imported'){
	    			$scope.disableImportBQ = false;
	    			$scope.disableImportResources = false;
	    		} else if($scope.transit.status === 'Resources Imported' || $scope.transit.status === 'Resources Updated') {
	    			$scope.disableImportBQ = false;
	    			$scope.disableImportResources = false;
	    			$scope.disableConfirmResouces = false;
	    		} else if($scope.transit.status === 'Resources Confirmed') {
	    			$scope.disableImportBQ = false;
	    			$scope.disableImportResources = false;
	    			$scope.disablePrintReport = false;
	    		} else if($scope.transit.status === 'Report Printed') {
	    			$scope.disableImportBQ = false;
	    			$scope.disableImportResources = false;
	    			$scope.disablePrintReport = false;
	    			$scope.disableCompleteTransit = false;
	    		}else if($scope.transit.status === 'Transit Completed') {
	    			$scope.disablePrintReport = false;
	    		}
	    		
	    	}

    	}, function(data){
	    	modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Error:" + data.replace('<br/>', '\n'));
	    })
    }
    
    
	$scope.openPrintReport = function(url){
//    	var wnd = $window.open(url, 'Print Report', '_blank');
		GlobalHelper.downloadFile(url);
    	$timeout(function(){
    		getTransit();
    	}, 3000);
    }
	
	$scope.completeTransitMsg = '';
    $scope.completeTransit = function(){
    	
    	var modalOptions = {
				bodyText: 'No admendment can be made after completing the Transit. Confirm?'
		};


		confirmService.showModal({}, modalOptions).then(function (result) {
			if(result == "Yes"){
				transitService.completeTransit($scope.jobNo)
		    	.then(function(data){
		    		getTransit();
		    		
		    		if(data === ''){
		    			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Transit has been completed successfully.");
		    		} else {
		    			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
		    		}
		    		
		    	});
			
			}
		});
		
    }
    

}]);