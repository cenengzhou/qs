mainApp.controller('JobDashboardCtrl', ['$scope', 'colorCode', 'jobService', '$cookies',
                               function($scope, colorCode, jobService, $cookies) {
	$scope.loading = true;
	
	//Initialize panel setting
	App.initComponent();
	
	
	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");

    loadJobData();
    
    function loadJobData() {
    	$scope.loading = true;
    	jobService.obtainJobInfo()
    	.then(
    			function( data ) {
    				$scope.job=data;
    				$scope.loading = false;
    			});
    	jobService.obtainJobDashboardData()
    	.then(
    			function( result ) {
    				$scope.chartParameters = {
    						labels : result.labels,
    						series : ['Internal Value', 'Actual Value', 'Total Budget', 'Tender Budget'],
    						data : result.data,
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
    							scaleLabel: " <%= Number(value / 1000000) + ' M'%>"
    						}
    				};

    			});
    }
	 
    
}]);