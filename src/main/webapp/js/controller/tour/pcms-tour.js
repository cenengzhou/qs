mainApp.controller('TourCtrl', ['$rootScope', '$scope', '$timeout', '$interval', 'blockUI', '$location', '$compile',
						function ($rootScope, $scope, $timeout, $interval, blockUI, $location, $compile){
	var tourArray = [];
	
	addTour('selectJob', '/job-select');
	addTour('menuBar', '/job/dashboard');
	
	function addTour(tourName, autoStartLink){
		var tourDiv = document.createElement('div');
		tourDiv.setAttribute('id', tourName + 'Tour')
		tourDiv.setAttribute('class','hide');
		tourDiv.setAttribute('ng-joy-ride',tourName + 'TourShow');
		tourDiv.setAttribute('config', tourName + 'TourConfig');
		tourDiv.setAttribute('on-finish', "onTourFinish('" + tourName + "TourShow')");
		tourDiv.setAttribute('on-skip', "onTourFinish('" + tourName + "TourShow')");
		tourArray[tourName] = tourDiv;
		tourArray[tourName].autoStartLink = autoStartLink;
	}
	
	var tourRootDiv = angular.element('#tourRootDiv');
	for(var key in tourArray){
		tourRootDiv.append(tourArray[key]);
		angular.element(document).injector().invoke(
				function($compile) {
				    var scope = angular.element(tourArray[key]).scope();
				    $compile(tourArray[key])(scope);
				    scope.$digest();
		});
	};
	
	$scope.$on('$stateChangeSuccess', function(){
		for(var key in tourArray){
			$rootScope[key + 'TourShow'] = false;
			if(typeof(Storage) != undefined){
				if($location.path() === tourArray[key].autoStartLink && localStorage[key + 'TourShow'] != -1){
					var tourKey = key;
					var showTourinterval = $interval(function(){
						if(!blockUI.isBlocking()) {
							$rootScope[tourKey + 'TourShow'] = true
							$interval.cancel(showTourinterval);
						}
					},1000);
				} 
			}
		}
	});
	
    $rootScope.onTourFinish = function(t){
    	if(typeof(Storage) != undefined){
    		localStorage[t] = -1;
    	}
    	$rootScope.moveHeaderDown();
    }
	
    $rootScope.triggerSideBar = function(){
    	$rootScope.moveHeaderDown();
    	angular.element('[data-click=right-sidebar-toggled]').click();
    }
    
    $rootScope.triggerUserMenu = function(){
    	angular.element('[data-click=userMenuCaret]').dropdown('toggle');
    }

    $rootScope.moveHeaderUp = function(){
    	angular.element('#header').css('z-index', '19999');
    }
    
    $rootScope.moveHeaderDown = function(){
    	angular.element('#header').css('z-index', '1040');
    }
    
	$rootScope.selectJobTourConfig = [
//		{
//			type: 'location_change',
//			path: '/'
//		},
		{
			type: 'title',
			heading: 'Tour guide',
			text: '<div class="row"><div id="title-text" class="col-md-12"><span class="main-text">\
			Welcome to <strong>PCMS</strong></span><br>\
			<span>This tour guide is design to help you easier adapt to the new PCMS.</span><br/><br/>\
			</div></div>',
		},
		{
			type: 'title',
			heading: 'Tour guide',
			text: '<div class="row"><div id="title-text" class="col-md-12"><span class="main-text">Let us start with select job tour' +
			' </span><br/><span class="small"><em>You can search job with job number, name or description</em></span></div></div>',
		},
		{
			type: 'element',
			selector: '#searchJobHereDiv',
			heading: 'Step 1',
			text: 'Enter <em>Job</em> related information',
			placement: 'bottom',
			scroll: true,
			attachToBody: true,
			advanceOn: {element:'#searchJobField', event:'change'}
		},
		{
			type: 'element',
			selector: '#divisionDiv',
			heading: 'Step 2',
			text: 'Choose <em>Division</em> to filter job',
			placement: 'right',
			scroll: true,
			attachToBody: true,
			advanceOn: {element: '#divisionDiv', event: 'click'}
		},
		{
			type: 'element',
			selector: '#joblabelDiv',
			heading: 'Step 3',
			attachToBody: true,
			text: 'Select job and start use PCMS',
			placement: 'top',
			scroll: true,
			advanceOn: {element: '#joblabelDiv', event: 'click'}
		}
	];

	$rootScope.menuBarTourConfig = [
		{
			type: 'title',
			heading: 'Menu Tour',
			text: '<div class="row"><div id="title-text" class="col-md-12">\
			<span class="main-text">This guide will show you the <strong>Menu</strong> location</span></div></div>',
		},
		{
			type: 'function',
			fn: $rootScope.moveHeaderUp
		},
		{
			type: 'element',
			selector: '#header',
			heading: 'Step 1',
			text: 'This is main menu bar divided by different catalogue which contain most feature for PCMS',
			placement: 'bottom',
			attachToBody: true,
			scroll: true
		},
		{
			type: 'function',
			fn: $rootScope.moveHeaderDown
		},
		{
			type: 'element',
			selector: '#sidebar',
			heading: 'Step 2',
			text: 'This is sub-menu bar of current selected catalogue'
		},
		{
			type: 'function',
			fn: $rootScope.triggerSideBar
		},
		{
			type: 'element',
			selector: '#sidebar-right',
			heading: 'Step 3',
			text: 'This is the sidebar contain some helpful link',
			placement: 'left',
			scroll:true
		},
		{
			type: 'function',
			fn: $rootScope.triggerSideBar
		},
	    {
		   	type: 'function',
		   	fn: $rootScope.triggerUserMenu
	    },
		{
			type: 'function',
			fn: $rootScope.moveHeaderUp
		},
		{
			type: 'element',
			selector: '#userDropPanel',
			heading: 'Step 4',
			text: 'This is the user preference panel',
			placement: 'left',
			attachToBody: true,
			scroll:true
		},
		
//		{
//			type: 'title',
//			heading: 'End of tour',
//			text: '<div class="row"><div id="title-text" class="col-md-12'><span class="main-text">The tour is end' +
//			' </span><br/><span class="small"><em>hope you enjoe PCMS</em></span></div></div>',
//		}
	]
}]);

function loadTour(){
	var tourRootDiv = document.createElement('div');
	tourRootDiv.setAttribute('ng-controller', 'TourCtrl');
	tourRootDiv.setAttribute('id', 'tourRootDiv');
	
	angular.element('body').append(tourRootDiv);
	angular.element(document).injector().invoke(
		function($compile) {
		    var scope = angular.element(tourRootDiv).scope();
		    $compile(tourRootDiv)(scope);
		    scope.$digest();
	});
}

if (window.attachEvent) {
    window.attachEvent('onload', loadTour);
} else {
    window.addEventListener('load', loadTour, false);
}