mainApp.controller('PaymentSummaryCtrl', ['$scope', 'modalService', 'colorCode', 'paymentService','$q', '$cookies',
                                   function($scope, modalService, colorCode, paymentService, $q, $cookies) {

	$scope.paymentCertNo = $cookies.get('paymentCertNo');


	getPaymentResourceDistribution();

	
	function getPaymentResourceDistribution(){
		var bqCum = paymentService.getPaymentResourceDistribution($scope.jobNo, $scope.subcontractNo, 'BQ', 'Cumulative', $scope.paymentCertNo);
    	var voCum = paymentService.getPaymentResourceDistribution($scope.jobNo, $scope.subcontractNo, 'VO', 'Cumulative', $scope.paymentCertNo);
    	var ccCum = paymentService.getPaymentResourceDistribution($scope.jobNo, $scope.subcontractNo, 'CC', 'Cumulative', $scope.paymentCertNo);
    	var retentionCum = paymentService.getPaymentResourceDistribution($scope.jobNo, $scope.subcontractNo, 'RT', 'Cumulative', $scope.paymentCertNo);
    	var advancedCum = paymentService.getPaymentResourceDistribution($scope.jobNo, $scope.subcontractNo, 'Advanced', 'Cumulative', $scope.paymentCertNo);
    	var othersCum = paymentService.getPaymentResourceDistribution($scope.jobNo, $scope.subcontractNo, 'Others', 'Cumulative', $scope.paymentCertNo);
    	
    	var bqMovement = paymentService.getPaymentResourceDistribution($scope.jobNo, $scope.subcontractNo, 'BQ', 'Movement', $scope.paymentCertNo);
    	var voMovement = paymentService.getPaymentResourceDistribution($scope.jobNo, $scope.subcontractNo, 'VO', 'Movement', $scope.paymentCertNo);
    	var ccMovement = paymentService.getPaymentResourceDistribution($scope.jobNo, $scope.subcontractNo, 'CC', 'Movement', $scope.paymentCertNo);
    	var retentionMovement = paymentService.getPaymentResourceDistribution($scope.jobNo, $scope.subcontractNo, 'RT', 'Movement', $scope.paymentCertNo);
    	var advancedMovement = paymentService.getPaymentResourceDistribution($scope.jobNo, $scope.subcontractNo, 'Advanced', 'Movement', $scope.paymentCertNo);
    	var othersMovement = paymentService.getPaymentResourceDistribution($scope.jobNo, $scope.subcontractNo, 'Others', 'Movement', $scope.paymentCertNo);
		
    	$q.all([bqCum, voCum, ccCum, retentionCum, advancedCum, othersCum, bqMovement, voMovement, ccMovement, retentionMovement, advancedMovement, othersMovement])
    		.then(function (data){
    			console.log(data);
    			setDashboardData(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9], data[10], data[11]);
    	});
	}

	function setDashboardData(bqCum, voCum, ccCum, retentionCum, advancedCum, othersCum, bqMovement, voMovement, ccMovement, retentionMovement, advancedMovement, othersMovement){
		$scope.cumPaymentResourceDistributionChart = {
				"data": [bqCum, voCum, ccCum, retentionCum, advancedCum, othersCum],
				"labels": ["BQ", "Addendum", "Contra Charge", "Retention", "Advanced", "Others"],
				"colours": [
				{
					"strokeColor": colorCode.blue,
					"pointHighlightStroke": colorCode.lightBlue
				},
				{
					"strokeColor": colorCode.red,
					"pointHighlightStroke": colorCode.lightRed
				},
				{
					"strokeColor": colorCode.yellow,
					"pointHighlightStroke": colorCode.lightYellow
				}, 
				{
					"strokeColor": colorCode.green,
					"pointHighlightStroke": colorCode.lightGreen
				}, 
				{
					"strokeColor": colorCode.purple,
					"pointHighlightStroke": colorCode.lightPurple
				},
				{
					"strokeColor": colorCode.grey,
					"pointHighlightStroke": colorCode.lightGrey
				}
				],
				'options' : {
					showTooltips: true,
				}
		};

		var totalMovement = bqMovement + voMovement + ccMovement - retentionMovement + advancedMovement + othersMovement;
		var totalPosted = bqCum + voCum + ccCum + retentionCum + advancedCum + othersCum + bqMovement - totalMovement;
		$scope.overallChart = {
				"data": [totalPosted, totalMovement],
				"labels": ["Posted", "Movement"],
				"colours": [
				{
					"strokeColor": colorCode.blue,
					"pointHighlightStroke": colorCode.lightBlue
				},
				{
					"strokeColor": colorCode.black,
					"pointHighlightStroke": colorCode.black
				}
				],
				'options' : {
					showTooltips: true,
				}
		};
		
		$scope.bqChart = {
				"data": [bqCum-bqMovement, bqMovement],
				"labels": ["Posted", "Movement"],
				"colours": [
				{
					"strokeColor": colorCode.blue,
					"pointHighlightStroke": colorCode.lightBlue
				},
				{
					"strokeColor": colorCode.black,
					"pointHighlightStroke": colorCode.black
				}
				],
				'options' : {
					showTooltips: true,
				}
		};
		
		$scope.voChart = {
				"data": [voCum-voMovement, voMovement],
				"labels": ["Posted", "Movement"],
				"colours": [
				{
					"strokeColor": colorCode.red,
					"pointHighlightStroke": colorCode.lightRed
				},
				{
					"strokeColor": colorCode.black,
					"pointHighlightStroke": colorCode.black
				}
				],
				'options' : {
					showTooltips: true,
				}
		};
		
		$scope.othersChart = {
				"data": [othersCum-othersMovement, othersMovement],
				"labels": ["Posted", "Movement"],
				"colours": [
				{
					"strokeColor": colorCode.grey,
					"pointHighlightStroke": colorCode.lightGrey
				},
				{
					"strokeColor": colorCode.black,
					"pointHighlightStroke": colorCode.black
				}
				],
				'options' : {
					showTooltips: true,
				}
		};
		
		$scope.advancedChart = {
				"data": [advancedCum-advancedMovement, advancedMovement],
				"labels": ["Posted", "Movement"],
				"colours": [
				{
					"strokeColor": colorCode.purple,
					"pointHighlightStroke": colorCode.lightPurple
				},
				{
					"strokeColor": colorCode.black,
					"pointHighlightStroke": colorCode.black
				}
				],
				'options' : {
					showTooltips: true,
				}
		};
		
		$scope.retentionChart = {
				"data": [retentionCum-retentionMovement, retentionMovement],
				"labels": ["Posted", "Movement"],
				"colours": [
				{
					"strokeColor": colorCode.green,
					"pointHighlightStroke": colorCode.lightGreen
				},
				{
					"strokeColor": colorCode.black,
					"pointHighlightStroke": colorCode.black
				}
				],
				'options' : {
					showTooltips: true,
				}
		};
		
		$scope.ccChart = {
				"data": [ccCum-ccMovement, ccMovement],
				"labels": ["Posted", "Movement"],
				"colours": [
				{
					"strokeColor": colorCode.yellow,
					"pointHighlightStroke": colorCode.lightYellow
				},
				{
					"strokeColor": colorCode.black,
					"pointHighlightStroke": colorCode.black
				}
				],
				'options' : {
					showTooltips: true,
				}
		};
		
		
	}
	
	


}]);


