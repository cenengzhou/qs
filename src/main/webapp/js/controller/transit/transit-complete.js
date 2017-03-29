mainApp.controller('TransitCompleteCtrl', ['$scope', 'modalService', 'transitService', 'jdeService', '$cookies', '$window', '$timeout', 'rootscopeService', 'confirmService', 'resourceSummaryService', 'GlobalHelper',
                          function($scope, modalService, transitService, jdeService, $cookies, $window, $timeout, rootscopeService, confirmService, resourceSummaryService, GlobalHelper) {
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
    $scope.completeTransitErr = false;
    $scope.completeTransit = function(){
    	transitService.completeTransit($scope.jobNo)
    	.then(function(data){
    		getTransit();
    		$scope.completeTransitMsg = '';
    		$scope.completeTransitErr = false;
    		
    		jdeService.createAccountMasterByGroup($scope.jobNo, true, false, false, false)
    		.then(
    				function( result ) {
    					if(data === ''){
    		    			$scope.completeTransitMsg += 'Complete Transit: Success<br/>';
    		    			jdeService.postBudget($scope.jobNo)
    		    			.then(function(data){
    		    				if(data === ''){
    		    					$scope.completeTransitMsg += 'Budget posting: Success<br/>';
    		    					generateResourceSummaries();
    		    				} else {
    		    					$scope.completeTransitMsg += 'Budget posting:' + data + '<br/>';
    		    					$scope.completeTransitErr = true;
    		    					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', $scope.completeTransitErr ? 'Fail' : 'Success', data.replace('<br/>', '\n') );
    		    				}
    		    			}, function(data){
    		    				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', msg.replace('<br/>', '\n') + data.replace('<br/>', '\n'));
    		    			});
    		    		} else {
    		    			$scope.completeTransitMsg +=  'Complete Transit:' + data + '<br/>';
    		    			$scope.completeTransitErr = true;
    		    			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', $scope.completeTransitMsg.replace('<br/>', '\n') + data.replace('<br/>', '\n'));
    		    		}

    				});
    		
    	}, function(data){
    		modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data.replace('<br/>', '\n') );
    	});
   	
    }
    

    function generateResourceSummaries() {
		resourceSummaryService.generateResourceSummaries($scope.jobNo)
		.then(
				function( data ) {
					if(data.length!=0){
						/*$scope.completeTransitMsg += 'Generate Resource Summary:' + data + '<br/>';
    					$scope.completeTransitErr = true;
    					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', $scope.completeTransitErr ? 'Fail' : 'Success', data.replace('<br/>', '\n') );*/
					}else{
						$scope.completeTransitMsg += 'Generate Resource Summary: Success<br/>';
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', $scope.completeTransitErr ? 'Fail' : 'Success', $scope.completeTransitMsg );
					}
				});
	}
    
}]);