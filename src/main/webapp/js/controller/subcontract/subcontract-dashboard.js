mainApp.controller('SubcontractCtrl', ['$scope', 'colorCode', 'subcontractService',
                                       function($scope, colorCode, subcontractService) {
	$scope.paymentAmount = 100256;

	//Chart.defaults.global.colours = [colorCode.blue, colorCode.red, colorCode.green, colorCode.pink, colorCode.purple];

	loadData();

	$scope.getDashboardDataByYear = function (year){
		$scope.selectedYear = year;
		getSubcontractDashboardData(year);
	}

	function loadData(){
		getSubcontract();
		var year =  new Date().getFullYear();
		$scope.selectedYear = year;
		$scope.yearList = [year, year-1, year-2];
		console.log($scope.yearList);
		getSubcontractDashboardData(year);
		getSubcontractDetailsDashboardData();
	}

	function getSubcontract(){
		subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					console.log(data);
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

	function getSubcontractDashboardData(year){
		subcontractService.getSubcontractDashboardData($scope.jobNo, $scope.subcontractNo, year)
		.then(
				function( data ) {
					console.log(data);
					if(data.length > 0){
						var certData = null;
						var wdData = null;
						var ccData = null;
						var mosData = null
						var retData = null;
						
						angular.forEach(data, function(value, key){
							if(value.category =="CERT"){
								certData = value.subcontractDashboardDetailWrapper;
							}else if(value.category =="WD"){
								wdData = value.subcontractDashboardDetailWrapper;
							}else if(value.category =="CC"){
								ccData = value.subcontractDashboardDetailWrapper;
							}else if(value.category =="MOS"){
								mosData = value.subcontractDashboardDetailWrapper;
							}else if(value.category =="RET"){
								retData = value.subcontractDashboardDetailWrapper;
							}
						});

						console.log(certData);
						console.log(wdData);
						console.log(ccData);
						console.log(retData);

						$scope.lineChartParameters = {
								labels : ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
								series : ['Work Done', 'Payment', 'Contra Charge',  'Material On Site', 'Retention Balance'],
								data : [
								        [wdData.jan, wdData.feb, wdData.mar, wdData.apr, wdData.may, wdData.jun, wdData.jul, wdData.aug, wdData.sep, wdData.oct, wdData.nov, wdData.dec],
								        [certData.jan, certData.feb, certData.mar, certData.apr, certData.may, certData.jun, certData.jul, certData.aug, certData.sep, certData.oct, certData.nov, certData.dec],
								        [ccData.jan, ccData.feb, ccData.mar, ccData.apr, ccData.may, ccData.jun, ccData.jul, ccData.aug, ccData.sep, ccData.oct, ccData.nov, ccData.dec],
								        [mosData.jan, mosData.feb, mosData.mar, mosData.apr, mosData.may, mosData.jun, mosData.jul, mosData.aug, mosData.sep, mosData.oct, mosData.nov, mosData.dec],
								        [retData.jan, retData.feb, retData.mar, retData.apr, retData.may, retData.jun, retData.jul, retData.aug, retData.sep, retData.oct, retData.nov, retData.dec],
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
					}

				});
	}

	function getSubcontractDetailsDashboardData(){
		subcontractService.getSubcontractDetailsDashboardData($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					//console.log(data);

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