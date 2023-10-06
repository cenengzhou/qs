mainApp.controller('JobDashboardCtrl', ['$scope', 'colorCode', 'jobService', 'adlService', 'resourceSummaryService', '$cookies', '$q', 'repackagingService', 'dashboardHelper',
                               function($scope, colorCode, jobService, adlService, resourceSummaryService, $cookies, $q, repackagingService, dashboardHelper) {
	$scope.loading = true;
	
	//Initialize panel setting
	App.initComponent();
	
	Chart.defaults.global.colours = [colorCode.black, colorCode.green, colorCode.purple,  colorCode.blue];
	
	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");

	$scope.yearList = dashboardHelper.getDashboardDropdown();
	$scope.selectedYear = $scope.yearList[0];

    loadJobData();
    
    $scope.getJobDashboardData = function (year){
    	$scope.selectedYear = year;
    	getJobData(year);
    }
    
    function loadJobData() {
    	getJobInfo();
    	getRepackaging();
    	getJobData($scope.selectedYear);
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
    	var getJobDashboardData = adlService.getJobDashboardData($scope.jobNo, year);

    	$q.all([getJobDashboardData]).then(function (data){setDashboardData(data[0]);});

    }
 
    
    function setDashboardData(data) {
    	var crData = data.find(function(x) {return (x.category == 'CR')}).detailList;
    	var ivData = data.find(function(x) {return (x.category == 'IV')}).detailList;
    	var avData = data.find(function(x) {return (x.category == 'AV')}).detailList;
    	var tbData = data.find(function(x) {return (x.category == 'TB')}).detailList;
    	var chartLabels = data[0].monthList

		$scope.contractReceivable = crData[crData.length-1];
		$scope.turnover = ivData[ivData.length-1];
		//$scope.originalBudget = originalBudgetData;
		$scope.totalBudget = tbData[tbData.length-1];
		$scope.actualValue = avData[avData.length-1];

		$scope.startYear = data[0].startYear;
		$scope.endYear = data[0].endYear;
		$scope.startMonth = chartLabels[0];
		$scope.endMonth = chartLabels[chartLabels.length-1];

    	$scope.chartParameters = {
    			labels : chartLabels,
    			series : ['Internal Value', 'Main Contract Cert. Amount', 'Actual Cost'],//  'Tender Budget'
    			data: [ivData, crData, avData],
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
    			},
			    colours : ['#17B6A4', '#9b59b6', '#2184DA']
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