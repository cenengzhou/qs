mainApp.controller('TourCtrl', ['$rootScope', '$scope', '$timeout', '$interval', '$templateCache', '$location', '$compile', '$cookies', 'blockUI', 'modalService',
						function ($rootScope, $scope, $timeout, $interval, $templateCache, $location, $compile, $cookies, blockUI, modalService){
	window.scope = $scope;
	window.rootscope = $rootScope;

	$scope.parentScope = $rootScope;
	$rootScope.showTour = function (currentTour){
		if(angular.isDefined(showTourinterval)) return;
		closeOtherTour(currentTour);
		showTourinterval = $interval(function(){
			var key = currentTour;
			if(!blockUI.isBlocking() && $rootScope.routedToDefaultJob) {
				if(angular.element('#' + $rootScope.tourArray[key].startElement).length > 0) {
					$rootScope.tourArray[key]['tourShow'] = true;
				} else {
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', $rootScope.tourArray[key].startError);
				}
				var canInterval = $interval.cancel(showTourinterval);
				showTourinterval = undefined;
			}
		},150);
	}
	
	$rootScope.toruAutoStartCheck = function(){
		switch($location.path()){
		case '/subcontract/addendum/tab/title':
			if($rootScope.createAddendumStepTourShow === 1){
				$rootScope.showTour('createAddendumStep');
			}
			if($rootScope.createAddendumStepTourShow === 3){
				$rootScope.showTour('createAddendumStepTabBar');
			}
			break;
		case '/subcontract/addendum-select':
			if($rootScope.createAddendumStepTourShow === 2){
				$rootScope.showTour('createAddendumStepViewDetails');
			}
			break;
		case '/subcontract/addendum/tab/details':
			if($rootScope.createAddendumStepTourShow === 'addendumStep2'){
				$rootScope.showTour('createAddendumStep2');
			}
			break;
		case '/subcontract/payment/tab/certificate':
			if($rootScope.createPaymentStepTourShow === 1){
				$rootScope.showTour('createPaymentStep');
			}
			break;
		default:
			for(var key in $rootScope.tourArray){
				if(
					( (localStorage[key + 'TourShow'] != -1 && $rootScope.tourArray[key].autoStart) ) && $rootScope.routedToDefaultJob){
					if( $location.path().indexOf($rootScope.tourArray[key].startLink) >= 0){
						$rootScope.showTour(key);
					}
				}
			}
		break;
		}
	}
	
	$scope.$on('$stateChangeSuccess', function(){
		if($location.path().indexOf('job-select') < 0) $rootScope.routedToDefaultJob = true;
		$timeout(function(){
			$rootScope.toruAutoStartCheck();
		},500);
	});
	
	$scope.$on('$stateChangeStart', function(){
		for(var key in $rootScope.tourArray){
			if($location.path().indexOf('addendum/tab') < 0){
				$rootScope.tourArray[key]['tourShow'] = false;
			}
		};
	});
	
    $rootScope.onTourFinish = function(t){
    	if(typeof(Storage) != undefined){
    		localStorage[t + 'TourShow'] = -1;
    	}
    	$rootScope.moveHeaderDown();
//    	$rootScope.triggerUserMenu();
    }
	
    $rootScope.triggerSideBar = function(){
    	$rootScope.moveHeaderDown();
    	var sidebar = angular.element('#sidebar-right')[0];
    	if(sidebar.offsetLeft + sidebar.offsetWidth + 25 >= window.outerWidth)
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
    
    $rootScope.goToTour = function(tour){
    	var msg = [];
    	msg['jobNo'] = 'Please select job first';
    	msg['subcontractNo'] = 'Please select subcontract first';
    	var okToGo = true;
		if($rootScope.tourArray[tour].cookieDependences){
			angular.forEach($rootScope.tourArray[tour].cookieDependences, function(dependence){
				if(!$cookies.get(dependence) && okToGo){
					okToGo = false;
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', msg[dependence]);
				}
			})
		} 
		if(okToGo) {
			$location.path($rootScope.tourArray[tour].startLink);
			$timeout(function(){
				$rootScope.showTour(tour);
			}, 500);
		}
    }
    
	var showTourinterval;
	function closeOtherTour(currentTour){
		for(var key in $rootScope.tourArray){
			if(key !== currentTour){
				$rootScope.tourArray[key]['tourShow'] = false;
			}
		};

	}
	
	function TourGuide(tourName, preview, autoStart, cookieDependences, startLink, startElement, startError, heading, description){
		this.tourName = tourName;
		this.autoStart = autoStart;
		this.cookieDependences = cookieDependences;
		this.startLink = startLink;
		this.startElement = startElement;
		this.startError = startError;
		this.heading = heading;
		this.description = description;
		this.preview = preview;
		this.initTourGuide();
	}
	
	TourGuide.prototype.initTourGuide = function (){
		var tourDiv = document.createElement('div');
		tourDiv.setAttribute('id', this.tourName + 'TourDiv')
		tourDiv.setAttribute('class','hide');
		tourDiv.setAttribute('ng-joy-ride', 'parentScope.tourArray["' + this.tourName + '"].tourShow');
		tourDiv.setAttribute('config', 'parentScope.tourArray["' + this.tourName + '"].tourConfig');
		tourDiv.setAttribute('on-finish', "parentScope.onTourFinish('" + this.tourName + "')");
		tourDiv.setAttribute('on-skip', "parentScope.onTourFinish('" + this.tourName + "')"); 
		this.div = tourDiv;
		this.tourConfig = [
			{
			type: 'location_change',
			path: this.autoStartLink
			}
		];
		if(this.heading && this.description){
			var title = {
				type: 'title',
				heading: this.heading,
				text: '<div class="row"><div id="title-text" class="col-md-12">\
				<span class="main-text">' + this.description + '</span><br/><br/>\
				</div></div>'
			};
			this.tourConfig.push(title);
		}
		$rootScope.tourArray[this.tourName] = this;
	}
	
	function initAllTour(){
		if(!$rootScope.tourArray){
			$rootScope.tourArray = {};
			new TourGuide(
					'selectJob', //tourName
					'image\\tour\\selectJob_preview.png', //preview
					false, //autoStart
					null, //cookieDependences
					'/job-select', //startLink
					'searchJobHereDiv', //startElement
					null, //startError
					'Select Job Tour Guide', // heading 
					'Welcome to <strong>QS 2.0</strong><br>This tour guide is design to help you easier adapt to the new QS 2.0.' //description
					);
			new TourGuide(
					'menuBar', 
					'image\\tour\\menuBar_preview.png', //preview
					false, 
					['jobNo'], 
					'/job/dashboard', 
					'jobMenuDiv', 
					'Please select job first', 
					'Menu Bar Tour Guide', 
					'This guide will show you the <strong>Menu</strong> location'
					);
			new TourGuide(
					'createPayment', 
					'image\\tour\\createPayment_preview.png', //preview
					false, 
					['jobNo', 'subcontractNo'], 
					'/subcontract/payment-select', 
					'createPaymentDiv', 
					'There have payment in progress, please finish the progress and start this tour again',
					'Create Payment',
					'This tour show how to create payment'
					);
			new TourGuide(
					'createPaymentStep',
					null,
					false,
					['jobNo', 'subcontractNo'], 
					'/subcontract/payment/tab/certificate',
					'Div'
					);
			new TourGuide(
					'createAddendum', 
					'image\\tour\\createAddendum_preview.png', //preview
					false, 
					['jobNo', 'subcontractNo'], 
					'/subcontract/addendum-select', 
					'createAddendumDiv', 
					'There have addendum in progress, please finish the progress and start this tour again',
					'Create addendum title',
					'This tour show how to create addendum title'
					);
			new TourGuide(
					'createAddendumStep',
					null,
					false,
					['jobNo', 'subcontractNo'], 
					'/subcontract/addendum/tab/title',
					'addendumStep1'
					);
			new TourGuide(
					'createAddendumStepViewDetails',
					null,
					false,
					['jobNo', 'subcontractNo'], 
					'/subcontract/addendum-select',
					'pendingAddendumViewDetails'
					);
			new TourGuide(
					'createAddendumStepTabBar',
					null,
					false,
					['jobNo', 'subcontractNo', 'addendumNo'], 
					'/subcontract/addendum/tab/title',
					'addendumStep1'
					);
			var tourRootDiv = angular.element('#tourRootDiv');
			for(var key in $rootScope.tourArray){
				tourRootDiv.append($rootScope.tourArray[key].div);
				angular.element(document).injector().invoke(
						function($compile) {
						    var scope = angular.element($rootScope.tourArray[key].div).scope();
						    $compile($rootScope.tourArray[key].div)(scope);
						    scope.$digest();
				});
			};
			$templateCache.put('tourModalTemplate.html','\
			<div class="modal-header bg-primary">\
				<div class="row">\
					<div class="col-md-11">\
					</div>\
					<div class="col-md-1">\
						<button class="btn btn-white pull-right" type="button"\
							ng-click="cancel()">\
							<span class="fa fa-remove"></span>\
						</button>\
					</div>\
				</div>\
			</div>\
			<div class="modal-body" style="height: 600px; overflow:auto">\
				<div class="row" vertilize-container>\
					<div vertilize class="col-md-4" ng-repeat="(key, value) in parentScope.listTourArray()">\
			            <div class="thumbnail">\
			                <img ng-src="{{value[\'preview\']}}" style="width:320px;height:150px">\
			                <div class="caption">\
			                    <h3 class="m-t-10 m-b-5">{{value[\'heading\']}}</h3>\
			                    <p ng-bind-html="value[\'description\']"></p>\
			                    <p class="m-b-0">\
			                        <a ng-click="parentScope.goToTour(key);cancel();parentScope.triggerSideBar()" class="btn btn-primary">View</a>\
			                        <!-- a href="#" class="btn btn-default">Button</a -->\
			                    </p>\
			                </div>\
			            </div>\
			        </div>\
				</div>\
			</div>\
			');
		}
	} initAllTour();
	
    function elementTourNoNextButtonTemplate(content, isEnd){
        return '<div class=\"row\"><div id=\"pop-over-text\" class=\"col-md-12\">' + content + '</div></div><hr><div class=\"row\"><div class=\"col-md-4 skip-class\"><a class=\"skipBtn pull-left\" type=\"button\">Skip</a></div><div class=\"col-md-8\"><div class=\"pull-right\"><button class=\"prevBtn btn\" type=\"button\"><i class=\"glyphicon glyphicon-chevron-left\"></i>&nbsp;Previous</button> </div></div></div>';
      }
	
    function elementTourNoPrevButtonTemplate(content, isEnd){
        return '<div class=\"row\"><div id=\"pop-over-text\" class=\"col-md-12\">' + content + '</div></div><hr><div class=\"row\"><div class=\"col-md-4 skip-class\"><a class=\"skipBtn pull-left\" type=\"button\">Skip</a></div><div class=\"col-md-8\"><div class=\"pull-right\"><button id=\"nextTitleBtn\" class=\"nextBtn btn btn-primary\" type=\"button\">Next&nbsp;<i class=\"glyphicon glyphicon-chevron-right\"></i></button> </div></div></div>';
      }
	
    function elementTourNoPrevNextButtonTemplate(content, isEnd){
        return '<div class=\"row\"><div id=\"pop-over-text\" class=\"col-md-12\">' + content + '</div></div><hr><div class=\"row\"><div class=\"col-md-4 skip-class\"><a class=\"skipBtn pull-left\" type=\"button\">Skip</a></div><div class=\"col-md-8\"><div class=\"pull-right\"></div></div></div>';
      }
	
    function pushArray (a,b) {
    	a.push.apply(a,b);
    };
    
    function tourBack(id){
		angular.element('#' + id + 'TourDiv').trigger('joyride:goToBackFn');
    }
    
    $rootScope.checkSearchJob = function(next){
    	if(next && !angular.element('#searchJobField').val()){
    		tourBack('selectJob');
    	}
    }
    
    $rootScope.listTourArray = function(){
    	var tourList = {};
    	for(key in $rootScope.tourArray){
    		if($rootScope.tourArray[key].preview != null){
    			tourList[key] = $rootScope.tourArray[key];
    		}
    	}
    	return tourList;
    }
    
	pushArray($rootScope.tourArray['selectJob'].tourConfig, [
		{
			type: 'element',
			selector: '#searchJobHereDiv',
			heading: 'Step 1',
			text: 'Enter <em>Job</em> related information',
			placement: 'bottom',
			scroll: true,
			attachToBody: true,
			shouldNotStopEvent: true,
//			advanceOn: {element:'#searchJobField', event:'change'}
		},
//		{
//			type: 'function',
//			fn: $rootScope.checkSearchJob
//		},
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
			text: 'Select job and start use QS 2.0',
			placement: 'top',
			scroll: true,
			advanceOn: {element: '#joblabelDiv', event: 'click'}
		}
	]);

	pushArray($rootScope.tourArray['menuBar'].tourConfig, [
		{
			type: 'function',
			fn: $rootScope.moveHeaderUp
		},
		{
			type: 'element',
			selector: '#header',
			heading: 'Step 1',
			text: 'This is main menu bar divided by different catalogue which contain most feature for QS 2.0',
			placement: 'bottom',
			attachToBody: true,
			scroll: true,
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
//			' </span><br/><span class="small"><em>hope you enjoe QS 2.0</em></span></div></div>',
//		}
	]);
	pushArray($rootScope.tourArray['createPayment'].tourConfig, [
		{
			type: 'element',
			selector: '#createPaymentDiv',
			heading: 'Create Payment',
			text: 'Click this button to create payment',
			placement: 'bottom',
			attachToBody: true,
			scroll: true,
			elementTemplate: elementTourNoNextButtonTemplate,
			advanceOn: {element:'#createPaymentDiv', event:'click'}
		},
		{
			type: 'function',
			fn: function(next){if(next)$rootScope.createPaymentStepTourShow = 1;}
		}
	]);
	
	$rootScope.processToAddendumStep = function(next){
		if(next){
			$rootScope.createAddendumStepTourShow += 1;
		}
	}
	pushArray($rootScope.tourArray['createAddendum'].tourConfig, [
		{ // $rootScope.createAddendumStepTourShow = 0
			type: 'element',
			selector: '#createAddendumDiv',
			heading: 'Create Addendum',
			text: 'Click this button to create addendum',
			placement: 'bottom',
			attachToBody: true,
			scroll: true,
			elementTemplate: elementTourNoNextButtonTemplate,
			advanceOn: {element:'#createAddendumDiv', event:'click'}
		},
		{
			type: 'function',
			fn: function(next){if(next)$rootScope.createAddendumStepTourShow = 1;}
		}
	]);

	pushArray($rootScope.tourArray['createAddendumStep'].tourConfig, [
		{//$rootScope.createAddendumStepTourShow = 1
			type: 'element',
			selector: '#addendumTitleStep1',
			heading: 'Addendum Title',
			text: 'Enter addendum title',
			placement: 'top',
			attachToBody: true,
			scroll: true
		},
		{
			type: 'element',
			selector: '#addendumRemarkStep1',
			heading: 'Addendum Remark',
			text: 'Enter addendum remark',
			placement: 'top',
			attachToBody: true,
			scroll: true
		},
		{
			type: 'element',
			selector: '#addendumSaveStep1',
			heading: 'Save addendum',
			text: 'Click Save',
			placement: 'top',
			attachToBody: true,
			scroll: true,
			elementTemplate: elementTourNoNextButtonTemplate,
			advanceOn: {element:'#addendumSaveStep1', event:'click'}
		},
		{
			type: 'function',
			fn: $rootScope.processToAddendumStep
		}
	]);
	pushArray($rootScope.tourArray['createAddendumStepViewDetails'].tourConfig, [
		{//$rootScope.createAddendumStepTourShow = 2
			type: 'element',
			selector: '#pendingAddendumViewDetails',
			heading: 'Update addendum',
			text: 'Click view details to update addendum',
			placement: 'left',
			advanceOn: {element:'#pendingAddendumViewDetails', event:'click'},
			elementTemplate: elementTourNoPrevNextButtonTemplate,
			attachToBody: true,
			scroll: true
		},
		{
			type: 'function',
			fn: $rootScope.processToAddendumStep
		}
	]);
	pushArray($rootScope.tourArray['createAddendumStepTabBar'].tourConfig, [
		{//$rootScope.createAddendumStepTourShow = 3
			type: 'element',
			selector: '#addendumTabBar',
			heading: 'Step bar',
			text: 'Click on the step number to update information',
			placement: 'bottom',
			advanceOn: {element: '#addendumStep2', event:'click'},
			attachToBody: true,
			scroll: true
		},
		{
			type: 'location_change',
			path: '/subcontract/addendum/tab/details'
		},
		{
			type: 'element',
			selector: '#addendumStep2',
			heading: 'Step two',
			text: 'This step is for add addendum',
			placement: 'left',
			advanceOn: {element: '#addendumStep3', event:'click'},
			attachToBody: true,
			scroll: true
		},
		{
			type: 'location_change',
			path: '/subcontract/addendum/tab/detail-list'
		},
		{
			type: 'element',
			selector: '#addendumStep3',
			heading: 'Step two',
			text: 'This step is for addendum review',
			placement: 'left',
			advanceOn: {element: '#addendumStep4', event:'click'},
			attachToBody: true,
			scroll: true
		},
		{
			type: 'location_change',
			path: '/subcontract/addendum/tab/attachment'
		},
		{
			type: 'element',
			selector: '#addendumStep4',
			heading: 'Step two',
			text: 'This step is for add attachment',
			placement: 'left',
			advanceOn: {element: '#addendumStep5', event:'click'},
			attachToBody: true,
			scroll: true
		},
		{
			type: 'location_change',
			path: '/subcontract/addendum/tab/summary'
		},
		{
			type: 'element',
			selector: '#addendumStep5',
			heading: 'Step two',
			text: 'This step is the summary',
			placement: 'left',
			advanceOn: {element: '#addendumStep6', event:'click'},
			attachToBody: true,
			scroll: true
		},
		{
			type: 'location_change',
			path: '/subcontract/addendum/tab/form2'
		},
		{
			type: 'element',
			selector: '#printBtn',
			heading: 'Print PDS/08/Form 2',
			text: 'Click the print button to print the Form2',
			placement: 'left',
			attachToBody: true,
			scroll: false
		},
		{
			type: 'function',
			fn: function(next){if(next)$rootScope.createAddendumStepTourShow = 0;}
		},
		{
			type: 'element',
			selector: '#submitBtn',
			heading: "Submit for approvel",
			text: 'Submit Form2 to Approval system',
			placement: 'top',
			attachToBody: true,
			scroll: true,
			advanceOn: {element: '#submitBtn', event: 'click'}
		}
	]);
	

}]);

mainApp.controller('TourModalCtrl',['$scope', '$rootScope', 'modalStatus', 'modalParam', '$uibModalInstance', 
							function($scope, $rootScope, modalStatus, modalParam, $uibModalInstance){
	$scope.parentScope = $rootScope;
	$scope.cancel = function () {
		$uibModalInstance.close();
	};
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

if(!window.injectedTour){
	if (window.attachEvent) {
	    window.attachEvent('onload', loadTour);
	} else {
	    window.addEventListener('load', loadTour, false);
	}
	window.injectedTour = true;
}