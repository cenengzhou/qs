mainApp.controller('SubcontractCtrl', ['$scope', 'colorCode', 'subcontractService', 'GlobalParameter',
                                       function($scope, colorCode, subcontractService, GlobalParameter) {
	$scope.paymentAmount = 100256;

	//Config Line chart color
	Chart.defaults.global.colours = [colorCode.red, colorCode.blue];

	loadData();

	$scope.getDashboardDataByYear = function (year){
		$scope.selectedYear = year;
		getSubcontractDashboardData(year);
	}

	function loadData(){
		getSubcontract();
		var year =  new Date().getFullYear();
		$scope.selectedYear = "Latest";
		$scope.yearList = ["Latest", year, year-1];

		getSubcontractDashboardData("Latest");
		getSubcontractDetailsDashboardData();
	}

	function getSubcontract(){
		subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					$scope.subcontract = data;
					$scope.subcontractStatus = GlobalParameter.getValueById(GlobalParameter.subcontractStatus, data.scStatus);
					
					$scope.revisedSCSum = data.remeasuredSubcontractSum + data.approvedVOAmount;

					//Chart: Subcontract Distribution
					var subcontractJson = {
							"data": [$scope.subcontract.remeasuredSubcontractSum, $scope.subcontract.approvedVOAmount],
							"labels": ["Remeasured Subcontract Sum", "Addendum"],
							"colours": [{
								"strokeColor": colorCode.black,
								"pointHighlightStroke": colorCode.grey
							}, 
							{
								"strokeColor": colorCode.info,
								"pointHighlightStroke": colorCode.lightInfo
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

					$scope.subcontractChart = subcontractJson;
				});
	}

	function getSubcontractDashboardData(year){
		subcontractService.getSubcontractDashboardData($scope.jobNo, $scope.subcontractNo, year)
		.then(
				function( data ) {
					if(data.length > 0){
						
						var certData = null;
						var wdData = null;
						
						angular.forEach(data, function(value, key){
							if(value.category =="CERT"){
								certData = value;
								$scope.startYear = certData.startYear;
								$scope.endYear = certData.endYear;
								$scope.startMonth = certData.monthList[0];
								$scope.endMonth = certData.monthList[certData.monthList.length-1];
								
							}else if(value.category =="WD"){
								wdData = value;
							}
						});
						
						$scope.lineChartParameters = {
								labels : certData.monthList,
								series : ['Work Done', 'Payment'],
								data : [
								        wdData.detailList,
								        certData.detailList,
								        ],
								        options : {
								        	showScale : true,
								        	showTooltips : true,
								        	responsive : true,
								        	maintainAspectRatio : true,
								        	pointDot : true,
								        	bezierCurve : true,
								        	datasetFill : false,
								        	animation : true,
								        	scaleLabel: " <%= Number(value / 1000).toFixed(2) + ' K'%>"
								        }
						};
					}

				});
	}

	function getSubcontractDetailsDashboardData(){
		subcontractService.getSubcontractDetailsDashboardData($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					//Chart: Subcontract Details Distribution
					var barChartJson = {
							labels : [''],
							series : ['Total', 'Certified', 'Work Done'],
							bqData : [],
							voData : [],
							colours: [
							{
								strokeColor: colorCode.green,
								fillColor: colorCode.green,
								highlightFill: colorCode.green,
								highlightStroke: colorCode.green
							}, 
							{
								strokeColor: colorCode.blue,
								fillColor: colorCode.blue,
								highlightFill: colorCode.blue,
								highlightStroke: colorCode.blue
							},
							{
								strokeColor: colorCode.red,
								fillColor: colorCode.red,
								highlightFill: colorCode.red,
								highlightStroke: colorCode.red
							}
							],
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
								scaleLabel: " <%= Number(value / 1000).toFixed(2) + ' K'%>",
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
									[value.amountSubcontract],[value.amountPostedCert], [value.amountPostedWD]
							);
						}else if(value.lineType == "VO"){
							if(value.amountSubcontract == 0)
								value.amountSubcontract = 0.0001
								if(value.amountPostedCert == 0)
									value.amountPostedCert = 0.0001
									if(value.amountPostedWD == 0)
										value.amountPostedWD = 0.0001
										barChartJson.voData.push(
												[value.amountSubcontract],[value.amountPostedCert], [value.amountPostedWD]
										);	
						}
					});


					$scope.barChartParameters = barChartJson;

				});
	}


}]);