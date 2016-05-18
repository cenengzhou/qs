var mainApp = angular.module('app', ['ui.router', 'chart.js',  'ngTouch', 'ngAnimate', 'ui.bootstrap',
                                     'ui.grid', 'ui.grid.pagination', 'ui.grid.edit', 'ui.grid.selection', 'ui.grid.cellNav',
									 'ui.grid.resizeColumns', 'ui.grid.pinning', 'ui.grid.moveColumns', 'ui.grid.exporter', 'ui.grid.importer', 'ui.grid.grouping']);  

/*mainApp.config(function ($httpProvider){  
	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
	var interceptor = ['$rootScope', '$q', function (scope, $q) {  
	    function success(response) { return response; } 
	    function error(response) { 
	        var status = response.status; 
	        if (status === 401) {
	        	console.log("HEY 401");
	            var deferred = $q.defer(); 
	            var req = { 
	                config: response.config, deferred: deferred 
	        }; 
	        //scope.requests401.push(req); 
	        scope.$broadcast('event:auth-loginRequired'); 
	        return deferred.promise; 
	    } 
	    // otherwise return $q.reject(response); 
	    } 
	    return function (promise) { 
	            return promise.then(success, error); 
	        } 
	    }]; $httpProvider.responseInterceptors.push(interceptor); 
	});*/

// configure our routes    
mainApp.config(['$stateProvider', '$urlRouterProvider', '$httpProvider', function($stateProvider, $urlRouterProvider, $httpProvider/*, modalStateProvider*/) {
	
	// For any unmatched url, redirect to /state1
	$urlRouterProvider.otherwise("/job/dashboard");  

	
	$stateProvider
	.state('navigation', {
	    templateUrl: 'navigation-menu.html',
	    abstract: true,
	  })
	  
	
	.state('logout', {
		url: "/logout",
		templateUrl: "logout.html",
		controller: 'NavMenuCtrl'
	})
	
	.state('select-job', {
		url: "/select-job",
		templateUrl: "view/select-job.html"
	}) 
	  
	
	.state('announcement', {
		url: "/announcement",
		templateUrl: "view/job/job-menu.html"
	})
	
	
	//Job
	.state('job', {
		url: "/job",
		parent: "navigation",
		templateUrl: "view/job/job-menu.html"
	})
	.state('job.dashboard', {
		url: "/dashboard",
		templateUrl: "view/job/job-dashboard.html",
		controller: 'JobCtrl'  
	})
	.state('job.info', {
		url: "/info",
		templateUrl: "view/job/job-info.html",
		controller: 'JobInfoCtrl'  
	})
	.state('job.dates', {
		url: "/dates",
		templateUrl: "view/job/job-dates.html",
		controller: 'JobInfoCtrl'  
	})
	
	
	//Subcontract
	.state('subcontract-select', {
		url: "/subcontract-select",
		parent: "navigation",
		templateUrl: "view/subcontract/subcontract-select.html",
		controller: 'SubcontractSelectCtrl'
	})
	
	.state('subcontract-flow', {
		url: "/subcontract-flow",
		parent: "navigation",
		templateUrl: "view/subcontract/subcontract-flow.html",
		controller: 'SubcontractFlowCtrl'
	})
	
	.state('subcontract', {
		url: "/subcontract",
		parent: "navigation",
		templateUrl: "view/subcontract/subcontract-menu.html"
	})
	.state('subcontract.dashboard', {
		url: "/dashboard",
		templateUrl: "view/subcontract/subcontract-dashboard.html",
		"params": {
			"packageno": null
		},
		controller: 'SubcontractCtrl'
		
	})
	.state('subcontract.header', {
		url: "/header",
		templateUrl: "view/subcontract/subcontract-header.html",
		controller: 'SubcontractHeaderCtrl'
	})
	.state('subcontract.details', {
		url: "/details",
		templateUrl: "view/subcontract/subcontract-details.html",
		controller: 'SubcontractDetailsCtrl'
	})
	.state('subcontract.dates', {
		url: "/dates",
		templateUrl: "view/subcontract/subcontract-dates.html",
		controller: 'SubcontractDatesCtrl'
	})
	.state('subcontract.taDetails', {
		url: "/taDetails",
		templateUrl: "view/subcontract/subcontract-ta-details.html",
		controller: 'SubcontractTaDetailsCtrl'
	})
	.state('subcontract.attachment', {
		url: "/attachment",
		templateUrl: "view/attachment.html",
		controller: 'AttachmentCtrl'
	})
	.state('subcontract.workdone', {
		url: "/workdone",
		templateUrl: "view/subcontract/subcontract-workdone.html",
		controller: 'SubcontractWorkdoneCtrl'
	})
	.state('subcontract.payment', {
		url: "/payment",
		templateUrl: "view/subcontract/payment-select.html",
		controller: 'SubcontractPaymentCtrl'
	})
	.state('subcontract.paymentdetails', {
		url: "/paymentdetails",
		templateUrl: "view/subcontract/payment-details.html",
		controller: 'SubcontractPaymentDetailsCtrl'
	})
	.state('subcontract.split', {
		url: "/split",
		templateUrl: "view/subcontract/subcontract-split.html",
		controller: 'SubcontractSplitCtrl'
	})
	.state('subcontract.terminate', {
		url: "/terminate",
		templateUrl: "view/subcontract/subcontract-terminate.html",
		controller: 'SubcontractSplitCtrl'
	})
	
	//Main Cert
	.state('cert-dashboard', {
		url: "/cert-dashboard",
		parent: "navigation",
		templateUrl: "view/main-cert/cert-dashboard.html",
		controller: 'CertCtrl'
	})
	.state('cert-all-details', {
		url: "/cert-all-details",
		parent: "navigation",
		templateUrl: "view/main-cert/cert-all-details.html",
		controller: 'CertAllDetailsCtrl'
	})
	.state('cert', {
		url: "/cert",
		parent: "navigation",
		templateUrl: "view/main-cert/cert-details-menu.html",
		controller: 'CertDetailsCtrl'
	})
	.state('cert.details', {
		url: "/details",
		templateUrl: "view/main-cert/cert-details.html",
		controller: 'CertDetailsCtrl'
	})
	
	//Repackaging
	.state('repackaging', {
		url: "/repackaging",
		parent: "navigation",
		templateUrl: "view/repackaging/repackaging.html",
		controller: 'RepackagingCtrl'
	})
	.state('repackaging-update', {
		url: "/repackaging-update",
		parent: "navigation",
		templateUrl: "view/repackaging/repackaging-update.html",
		controller: 'RepackagingUpdateCtrl'
	})
	
	//Transit
	.state("transit", {
		url: "/transit",
		parent: "navigation",
		templateUrl: "view/transit/transit-dashboard.html",
		controller: "TransitCtrl"
	})
	
	//IV
	.state("iv", {
		url: "/iv",
		parent: "navigation",
		templateUrl: "view/iv/iv-menu.html"
	})
	.state("iv.update", {
		url: "/update",
		templateUrl: "view/iv/iv-update.html",
		controller: "IVUpdateCtrl"
	})
	.state("iv.post", {
		url: "/post",
		templateUrl: "view/iv/iv-post.html",
		controller: "IVPostCtrl"
	})

	//Enquiry
	.state("enquiry", {
		url: "/enquiry",
		parent: "navigation",
		templateUrl: "view/enquiry/enquiry.html",
		controller: "EnquiryCtrl"
	})
	

	/**The custom “X-Requested-With” is a conventional header sent by browser clients, and it used to be the default in Angular but they took it out in 1.3.0. 
	 * Spring Security responds to it by not sending a “WWW-Authenticate” header in a 401 response, and thus the browser will not pop up an authentication dialog**/
	 $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
	
	
	
}]);
/*.factory('authHttpResponseInterceptor',['$q','$location',function($q,$location){
    return {
        response: function(response){
            if (response.status === 401) {
                console.log("Response 401");
            }
            return response || $q.when(response);
        },
        responseError: function(rejection) {
            if (rejection.status === 401) {
                console.log("Response Error 401",rejection);
                $location.path('/login').search('returnTo', $location.path());
            }
            return $q.reject(rejection);
        }
    }
}])
.config(['$httpProvider',function($httpProvider) {
    //Http Intercpetor to check auth failures for xhr requests
    $httpProvider.interceptors.push('authHttpResponseInterceptor');
}]);*/


//Config color code for charts
mainApp.config(['ChartJsProvider', 'colorCode', function (ChartJsProvider, colorCode) {
	Chart.defaults.global.tooltipTemplate = function (label) {
	    return label.datasetLabel + ': $' + Number(label.value).toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	}; 
	Chart.defaults.global.multiTooltipTemplate = function (label) {
	    return label.datasetLabel + ': $' + Number(label.value).toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	}; 
    // Configure all charts 
    ChartJsProvider.setOptions({
    	colours: [colorCode.blue, colorCode.red, colorCode.green, colorCode.yellow, colorCode.purple],
      	responsive: true
    });
    // Configure all line charts 
    ChartJsProvider.setOptions('Line', {
      datasetFill: false
    });
  }]);



/**
 * Check authentication and user role if location changed
 */
mainApp.run(['$rootScope', '$location', function ($rootScope, $location) {
  /*$rootScope.$on('$locationChangeStart', function(event, next, current) {

	 // console.log('$location.path(): '+$location.path());
	  var isLogin = $location.path() === "/login";
	  var isLogout = $location.path() === "/logout";
	  
	  if(isLogin || isLogout){
			return; // no need to redirect 
		}
	  
      if ($rootScope.authenticated) {
    	  console.log('ALLOW');
          //$location.path('/select-job');
      }
      else {
    	  console.log('DENY');
          event.preventDefault();
          $location.path('/login');
      }
  });*/
	
	

}]);


/**
 * Event-Listner for Back-Button
 */
mainApp.run(function($rootScope,$location, $uibModalStack){
	  /*$rootScope.$on('$stateChangeStart',function(event,next)
	  {
	    if(next.url==='/state1')
	    {
	      event.preventDefault();
	      $location.url('/state2',true);
	      console.info('$rootScope.$$phase: ',$rootScope.$$phase);
	      if(!$rootScope.$$phase)
	      {
	        $rootScope.$apply();
	      }
	    }
	  });*/
	
	/*$rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams){ 
	      event.preventDefault();
	      window.history.forward();
	});*/
		
	})
	


/*Notes
 * 
 * $log.info('name: ' + $scope.value);
 * console.log('name', $scope.value);
 * window.alert('Hello');
 * 
 * 
 * */

