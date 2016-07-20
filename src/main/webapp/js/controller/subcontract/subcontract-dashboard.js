mainApp.controller('SubcontractCtrl', ['$scope', 'colorCode', 'subcontractService',
                                       function($scope, colorCode, subcontractService) {
	$scope.paymentAmount = 100256;


	$scope.lineChartParameters = {
			labels : ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
			series : ['Work Done', 'Payment', 'Contra Charge',  'Material On Site', 'Retention Balance'],
			data : [
			        [50000, 80000, 110000, 120000, 180000, 230000, 250000, 278000, 298000, 360000, 380000, 400000],
			        [30000, 60000, 90000, 120000, 150000, 200000, 220000, 228000, 228000, 260000, 280000, 280000],
			        [0, 0, 0, 0, 0, 0, 50000, 80000, 100000, 100000, 100000, 100000],
			        [15000, 18000, 21000, 31000, 41000, 48000, 58000, 60000, 60000, 60000, 60000, 60000],
			        [5000, 8000, 11000, 12000, 18000, 23000, 25000, 25000, 25000, 25000, 25000, 25000]
			        ],
			        options : {
			        	showScale : true,
			        	showTooltips : true,
			        	responsive : true,
			        	/*maintainAspectRatio : true,*/
			        	pointDot : true,
			        	bezierCurve : true,
			        	datasetFill : false,
			        	animation : true,
			        	scaleLabel: " <%= Number(value / 1000) + ' K'%>"

			        }

	};

	//Chart.defaults.global.colours = [colorCode.blue, colorCode.red, colorCode.green, colorCode.pink, colorCode.purple];

	loadData();
	
	function loadData(){
		getSubcontract();
		getSubcontractDetailsDashboardData();
	}

	function getSubcontract(){
		subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					//console.log(data);
					$scope.subcontract = data;
					$scope.revisedSCSum = data.remeasuredSubcontractSum + data.approvedVOAmount;

					//Chart: Subcontract Distribution
					var subcontractJson = {
							"data": [$scope.subcontract.remeasuredSubcontractSum, $scope.subcontract.approvedVOAmount],
							"labels": ["Remeasured Subcontract Sum", "Addendum"],
							"colours": [{
								"strokeColor": colorCode.blue,
								"pointHighlightStroke": colorCode.lightBlue
							}, 
							{
								"strokeColor": colorCode.green,
								"pointHighlightStroke": colorCode.lightGreen
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

					$scope.subcontractPieChart = subcontractJson;
				});
	}

	function getSubcontractDetailsDashboardData(){
		subcontractService.getSubcontractDetailsDashboardData($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					console.log(data);
					
					//Chart: Subcontract Details Distribution
					var barChartJson = {
							labels : [''],
							series : ['Total', 'Certified', 'Work Done'],
							bqData : [],
							voData : [],
							colours: [{
								strokeColor: colorCode.red,
								fillColor: colorCode.red,
								highlightFill: colorCode.red,
								highlightStroke: colorCode.red
							}, 
							{
								strokeColor: colorCode.blue,
								fillColor: colorCode.blue,
								highlightFill: colorCode.blue,
								highlightStroke: colorCode.blue
							},
							{
								strokeColor: colorCode.green,
								fillColor: colorCode.green,
								highlightFill: colorCode.green,
								highlightStroke: colorCode.green
							}],
							options : {
								/*'onAnimationComplete': function () {
				     	        	 //this.showTooltip(this.segments, true);

				     	            //Show tooltips in bar chart (issue: multiple datasets doesnt work http://jsfiddle.net/5gyfykka/14/)
				     	            this.showTooltip(this.datasets[0].bars, true);

				     	            //Show tooltips in line chart (issue: multiple datasets doesnt work http://jsfiddle.net/5gyfykka/14/)
				     	            //this.showTooltip(this.datasets[0].points, true);  
				     	        },*/
								showTooltips : true,
								responsive : true,
								maintainAspectRatio : true,
								animation : true,
								scaleLabel: " <%= Number(value / 1000) + ' K'%>",
								//'scaleLabel' : "<%= Number(value).toFixed(2).replace('.', ',') + ' $'%>"
								/*'scaleOverride':true,
				     	        'scaleSteps':2,
				     	        'scaleStartValue':100000,
				     	        'scaleStepWidth':1000000*/
							}

					};
					
					angular.forEach(data, function(value, key){
						if(value.lineType == "BQ"){
							barChartJson.bqData.push(
									[value.amountBudget],[value.amountPostedCert], [value.amountPostedWD]
							);
						}else if(value.lineType == "VO"){
							console.log(value.amountBudget, value.amountPostedCert, value.amountPostedWD);
							if(value.amountBudget == 0)
								value.amountBudget = 0.0001
							if(value.amountPostedCert == 0)
								value.amountPostedCert = 0.0001
							if(value.amountPostedWD == 0)
									value.amountPostedWD = 0.0001
							barChartJson.voData.push(
									[value.amountBudget],[value.amountPostedCert], [value.amountPostedWD]
							);	
						}
					});
					

					$scope.barChartParameters = barChartJson;
						
				});
	}
	
	
}]);