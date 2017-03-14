mainApp.controller('JobDashboardCtrl', ['$scope', 'colorCode', 'jobService', 'adlService', 'resourceSummaryService', '$cookies', '$q', 'repackagingService', 
                               function($scope, colorCode, jobService, adlService, resourceSummaryService, $cookies, $q, repackagingService) {
	$scope.loading = true;
	
	//Initialize panel setting
	App.initComponent();
	
	Chart.defaults.global.colours = [colorCode.black, colorCode.green, colorCode.purple,  colorCode.blue];
	
	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");

	var year =  new Date().getFullYear();
	$scope.selectedYear = year;
	$scope.yearList = [year];
	
	for(var i=0; i < 20; i++){
		var yearToAdd = year - i;		
		if(i>0 && yearToAdd > 2002){
			$scope.yearList.push(yearToAdd);
		}
	} 
	
    loadJobData();
    
    $scope.getJobDashboardData = function (year){
    	$scope.selectedYear = year;
    	getJobData(year.toString().substring(2, 4));
    }
    
    function loadJobData() {
    	getJobInfo();
    	getRepackaging();
    	getJobData($scope.selectedYear.toString().substring(2, 4));
    	getResourceSummariesGroupByObjectCode();
    }

    function getRepackaging(){
    	repackagingService.getRepackaging($scope.jobNo, 1)
		.then(
				function( data ) {
					if(data)
						$scope.originalBudget = data.totalResourceAllowance;
				});
		
    }
    
    function getJobInfo(){
    	jobService.getJob($scope.jobNo)
		.then(
				function( data ) {
					$scope.job = data;
				});
		jobService.getJobDates($scope.jobNo)
		.then(
				function( data ) {
					$scope.jobDates = data;
				});
    }
    
    function getJobData(year) {
    	var contractReceivableList = adlService.getJobDashboardData($scope.jobNo, 'ContractReceivable', year);
    	var turnoverList = adlService.getJobDashboardData($scope.jobNo,  'Turnover', year);
    	var totalBudgetList = adlService.getJobDashboardData($scope.jobNo, 'TotalBudget', year);
    	var actualValueList = adlService.getJobDashboardData($scope.jobNo, 'ActualValue', year);
    	

    	$q.all([contractReceivableList, turnoverList, totalBudgetList, actualValueList])
    		.then(function (data){
    			setDashboardData(data[0], data[1], data[2], data[3]);
    	});
    }
 
    
    function setDashboardData(contractReceivableList, turnoverList, totalBudgetList, actualValueList) {
    	$scope.contractReceivable = contractReceivableList[11];
    	$scope.turnover = turnoverList[11];
    	//$scope.originalBudget = originalBudgetData;
    	$scope.totalBudget = totalBudgetList[11];
    	$scope.actualValue = actualValueList[11];
    	
    	$scope.chartParameters = {
    			labels : ['Jan', 'Fev', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
    			series : ['Total Budget', 'Internal Value', 'Contract Receivable', 'Actual Value'],//  'Tender Budget'
    			//data : data.data,
    			data: [totalBudgetList, turnoverList, contractReceivableList, actualValueList],
    			options : {
    				showScale : true,
    				showTooltips : true,
    				responsive : true,
    				maintainAspectRatio : true,
    				pointDot : true,
    				bezierCurve : true,
    				datasetFill : false,
    				//scaleStartValue: 725000000,
    				animation : true,
    				//scaleLabel: " <%= Number(value / 1000000).toFixed(2) + ' M'%>"
    				scaleLabel: " <%= Number(value / 1000000) + ' M'%>"
    			}
    	};
    }
    
    function getResourceSummariesGroupByObjectCode(){
    	resourceSummaryService.getResourceSummariesGroupByObjectCode($scope.jobNo)
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
					var resourceSummaryJson = {
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

					$scope.resourceSummaryChart = resourceSummaryJson;

    	});

    }
    
}]);