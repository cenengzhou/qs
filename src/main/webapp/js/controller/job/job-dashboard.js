mainApp.controller('JobDashboardCtrl', ['$scope', 'colorCode', 'jobService', '$animate', '$cookieStore',
                               function($scope, colorCode, jobService, $animate, $cookieStore) {
	$scope.loading = true;
	
	//Initialize panel setting
	App.initComponent();
	
	
	$scope.jobNo = $cookieStore.get("jobNo");
	$scope.jobDescription = $cookieStore.get("jobDescription");

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