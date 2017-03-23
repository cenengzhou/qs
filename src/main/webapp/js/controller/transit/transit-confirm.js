mainApp.controller('TransitConfirmCtrl', ['$scope', 'modalService', 'transitService', 'jdeService', '$cookies', '$window', '$timeout', 'rootscopeService', 'confirmService', '$q', 'colorCode',
                          function($scope, modalService, transitService, jdeService, $cookies, $window, $timeout, rootscopeService, confirmService, $q, colorCode) {
	rootscopeService.setSelectedTips('');
	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");
	
	
    getTransit();
    getTransitTotalAmount();
    getECADistributionByObjectCode();
    
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
    
    
    function getTransitTotalAmount() {
    	var totalSellingAmount = transitService.getTransitTotalAmount($scope.jobNo, 'Selling');
    	var totalECAAmount = transitService.getTransitTotalAmount($scope.jobNo,  'ECA');

    	$q.all([totalSellingAmount, totalECAAmount])
    		.then(function (data){
    			$scope.totalSellingAmount = data[0];
    			$scope.totalECAAmount = data[1];
    			setDashboardData(data[0], data[1]);
    	});
    }
    
    function setDashboardData(totalSellingAmount, totalECAAmount) {
    	 $scope.barChartParameters = {
        		labels : [''],
        		series: ['Selling Amount', 'ECA Amount'],
        		data : [[totalSellingAmount], [totalECAAmount]],
        		colours: [
	        		{
	        			'strokeColor': colorCode.red,
	        			'fillColor': colorCode.red,
	        			'highlightFill': colorCode.red,
	        			'highlightStroke': colorCode.red
	        		}, 
	        		{
	        			"strokeColor": colorCode.blue,
	        			'fillColor': colorCode.blue,
	        			'highlightFill': colorCode.blue,
	        			'highlightStroke': colorCode.blue
	        		}
        		],
        		options: {
         	       'showTooltips' : true,
         	        'responsive' : false,
         	        'maintainAspectRatio' : true,
         	       'scaleLabel': " <%= Number(value / 1000000).toFixed(2) + ' M'%>"
         	     }
        };
        
        /*barChartJson.data.push(
        		[3000000000], [2800000000]
        );*/
        
       //$scope.barChartParameters = barChartJson;
    }
    
    
    function getECADistributionByObjectCode(){
    	transitService.getBQResourceGroupByObjectCode($scope.jobNo)
    	.then(
    			function( data ) {
    				$scope.resourceSummary = data;
    				var objectCodeList = [];
    				var amountBudgetList = [0,0,0,0,0];
    				
    				angular.forEach(data, function(value, key){
    					if(value.objectCode != '19'){
    						if(value.objectCode == '11')
    							amountBudgetList[0] = value.amountBudget;
    						else if(value.objectCode == '12')
    							amountBudgetList[1] =value.amountBudget;
    						else if(value.objectCode == '13')
    							amountBudgetList[2] = value.amountBudget;
    						else if(value.objectCode == '14')
    							amountBudgetList[3]=value.amountBudget;
    						else if(value.objectCode == '15')
    							amountBudgetList[4]=value.amountBudget;
    					}
    					else
    						$scope.genuineMarkup = value.amountBudget;
					});
    				
    				$scope.eca = {
							"data": amountBudgetList,
							"labels": ["Labour & Staff", "Plant", "Material", "Subcontract", "Others"],
							"colours": [
							{
								"strokeColor": colorCode.yellow,
								"pointHighlightStroke": colorCode.lightYellow
							}, 
							{
								"strokeColor": colorCode.purple,
								"pointHighlightStroke": colorCode.lightPurple
							},
							{
								"strokeColor": colorCode.green,
								"pointHighlightStroke": colorCode.lightGreen
							}, 
							{
								"strokeColor": colorCode.blue,
								"pointHighlightStroke": colorCode.lightBlue
							},
							{
								"strokeColor": colorCode.grey,
								"pointHighlightStroke": colorCode.lightGrey
							}
							],
							'options' : {
								showTooltips: true,

								//SHow Tooltip by default
								/*onAnimationComplete: function () {
				    				this.showTooltip(this.segments, true);
				    			}*/
							}
					};

    	});

    }
    
    $scope.confirmResources = function(){
    	var modalOptions = {
				bodyText: 'Do you want system to group resources into Packages?'
		};


		confirmService.showModal({}, modalOptions).then(function (result) {
			if(result == "Yes"){
				confirmResourcesAndCreatePackages(true);
			}else
				confirmResourcesAndCreatePackages(false);
		});
		
		
    	
    }
	
	function confirmResourcesAndCreatePackages(createPackage){
		transitService.confirmResourcesAndCreatePackages($scope.jobNo, createPackage)
    	.then(function(data){
    		getTransit();
    		if(data === ''){
    			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', 'Resources confirmed' );
    		}else{
    			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', data.replace('<br/>', '\n') );
    		}
    	}, function(data){
    		modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data.replace('<br/>', '\n') );
    	});
	}
    

}]);