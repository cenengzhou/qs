mainApp.controller('SubcontractCtrl', ['$scope', '$stateParams', 'colorCode', function($scope , $stateParams, colorCode) {
    $scope.subcontractSum = 53895000;
    $scope.paymentAmount = 100256;
    
    $scope.packageNo = $stateParams.packageno;
    
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
    	        responsive : false,
    	        maintainAspectRatio : true,
    	        pointDot : true,
    	        bezierCurve : true,
    	        datasetFill : false,
    	        animation : true,
    	        scaleLabel: " <%= Number(value / 1000) + ' K'%>"

    	     }
    
    };
    
    //Chart.defaults.global.colours = [colorCode.blue, colorCode.red, colorCode.green, colorCode.pink, colorCode.purple];
    
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


    barChartJson.bqData.push(
    		['33796000'],['280000'], ['300000']
    );
    	
    barChartJson.voData.push(
    		[2099000],[80000], [100000]
    ); 	
    	
    $scope.barChartParameters = barChartJson;
   
    
    var subcontractJson = {
    		"data": ["33796000", "2099000"],
    		"labels": ["Original Subcontract Sum", "Addendum"],
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
    $scope.subcontract = subcontractJson;
    
   

    
    
    
    
      
}]);