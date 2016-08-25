mainApp.controller('JobDashboardCtrl', ['$scope', 'colorCode', 'jobService', 'adlService', 'resourceSummaryService', '$cookies', '$q', '$http',
                               function($scope, colorCode, jobService, adlService, resourceSummaryService, $cookies, $q, $http) {
	$scope.loading = true;
	
	//Initialize panel setting
	App.initComponent();
	
	Chart.defaults.global.colours = [colorCode.black, colorCode.green, colorCode.purple];
	
	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");

	var year =  new Date().getFullYear();
	$scope.selectedYear = year;
	$scope.yearList = [year, year-1, year-2];
	
    loadJobData();
    
    $scope.getJobDashboardData = function (year){
    	$scope.selectedYear = year;
    	getJobData(year.toString().substring(2, 4));
    }
    
    function loadJobData() {
    	getJobData($scope.selectedYear.toString().substring(2, 4));
    	getResourceSummariesGroupByObjectCode();
    }

    function getResourceSummariesGroupByObjectCode(){
    	resourceSummaryService.getResourceSummariesGroupByObjectCode($scope.jobNo)
    	.then(
    			function( data ) {
    				console.log(data);


    	});

    }

    function getJobData(year) {
    	var contractReceivableList = adlService.getJobDashboardData($scope.jobNo, 'ContractReceivable', year);
    	var turnoverList = adlService.getJobDashboardData($scope.jobNo,  'Turnover', year);
    	var totalBudgetList = adlService.getJobDashboardData($scope.jobNo, 'TotalBudget', year);
    	

    	$q.all([turnoverList, contractReceivableList, totalBudgetList])
    		.then(function (data){
    			setDashboardData(data[0].dataList, data[1].dataList, data[2].dataList);
    	});
    }
 
    
    function setDashboardData(contractReceivableList, turnoverList, totalBudgetList) {
    	$scope.contractReceivable = contractReceivableList[11];
    	$scope.turnover = turnoverList[11];
    	//$scope.originalBudget = originalBudgetData;
    	$scope.totalBudget = totalBudgetList[11];

    	$scope.chartParameters = {
    			labels : ['Jan', 'Fev', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
    			series : ['Total Budget', 'Internal Value', 'Contract Receivable'],//  'Actual Value' ,'Tender Budget'
    			//data : data.data,
    			data: [totalBudgetList, turnoverList, contractReceivableList],
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
    				scaleLabel: " <%= Number(value / 1000000).toFixed(2) + ' M'%>"
    					//scaleLabel: " <%= Number(value / 1000000) + ' M'%>"
    			}
    	};
    }
    
}]);