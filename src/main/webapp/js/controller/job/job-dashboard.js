mainApp.controller('JobCtrl', ['$scope', '$rootScope', 'colorCode', 'jobService', 
                               function($scope, $rootScope, colorCode, jobService) {
	$scope.loading = true;
	
    loadJobData();
    
    function loadJobData() {
    	 $scope.loading = true;
    	jobService.obtainJobInfo()
   	 	.then(
			 function( data ) {
				$scope.job=data;
				$rootScope.jobNo = data.jobNo;
				$rootScope.jobDescription = data.jobDescription;
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
			    	        /*scaleStartValue': 725000000,*/
			    	        animation : true,
			    	        scaleLabel: " <%= Number(value / 1000000) + ' M'%>"
			    	     }
			    
			    };
			 
			 });
    }
    
}]);