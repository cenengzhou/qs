mainApp.controller('TransitCtrl', ['$scope', 'colorCode', function($scope, colorCode) {

    Chart.defaults.global.colours = ['#00c0ef', '#FF5252'];
    
    var sellingJson = {
    		"data": [],
    		"labels": ["Labour", "Plant", "Material", "Subcontract", "Others"],
    		"colours": [{
    			"strokeColor": colorCode.red,
    			"pointHighlightStroke": colorCode.lightRed
    		}, 
    		{
    			"strokeColor": colorCode.blue,
    			"pointHighlightStroke": colorCode.lightBlue
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
    		}],
    };
    
    sellingJson.data.push(
    		30000000,
    		400000000,
    		500000000,
    		2000000000,
    		70000000
    );
    $scope.selling = sellingJson;
    
   

    var barChartJson = {
    		'labels' : [''],
    		'series' : ['Selling Amount', 'ECA Amount'],
    		'data' : [],
    		"colours": [{
    			'strokeColor': colorCode.red,
    			'fillColor': colorCode.red,
    			'highlightFill': colorCode.red,
    			'highlightStroke': colorCode.red
    		}, 
    		{
    			"strokeColor": colorCode.blue,
    			'fillColor': colorCode.blue,
    			'highlightFill': colorCode.blue,
    			'highlightStroke': colorCode.blue
    		}],
    		 'options' : {
     	       'showTooltips' : true,
     	        'responsive' : false,
     	        'maintainAspectRatio' : true,
     	       'scaleLabel': " <%= Number(value / 1000000) + ' M'%>"
     	     }
     
    };
    
    barChartJson.data.push(
    		[3000000000], [2800000000]
    );
    
    $scope.barChartParameters = barChartJson;
    
      
}]);