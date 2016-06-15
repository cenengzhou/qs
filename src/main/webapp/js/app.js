var mainApp = angular.module('app', ['ui.router', 'chart.js',  'ngTouch', 'ngAnimate', 'ui.bootstrap', 'ngCookies', 'oc.lazyLoad',
                                     'ui.grid', 'ui.grid.pagination', 'ui.grid.edit', 'ui.grid.selection', 'ui.grid.cellNav',
									 'ui.grid.resizeColumns', 'ui.grid.pinning', 'ui.grid.moveColumns', 'ui.grid.exporter', 'ui.grid.importer', 'ui.grid.grouping']);  


// configure our routes    
mainApp.config(['$stateProvider', '$urlRouterProvider', '$httpProvider', function($stateProvider, $urlRouterProvider, $httpProvider/*, modalStateProvider*/) {
	
	// For any unmatched url, redirect to /state1
	$urlRouterProvider.otherwise("/job-select");  

	
	$stateProvider
	.state('navigation', {
	    templateUrl: 'navigation-menu.html',
	    abstract: true,
	  })
	  
	
	.state('logout', {
		url: "/logout",
		templateUrl: "logout.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/logout.js'
                    ] 
                });
            }]
        },
		controller: 'LogoutCtrl'
	})
	
	.state('job-select', {
		url: "/job-select",
		//parent: "navigation",
		templateUrl: "view/job-select.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/job/job-select.js',
                           'js/service/job-service.js'
                    ] 
                });
            }]
        },
		controller: 'JobSelectCtrl',
		
	}) 
	  
	
	.state('announcement', {
		url: "/announcement",
		templateUrl: "view/job/job-menu.html"
	})
	
	
	//Job
	.state('job', {
		url: "/job",
		parent: "navigation",
		templateUrl: "view/job/job-menu.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/service/job-service.js'
                    ] 
                });
            }]
        },
        controller: 'NavMenuCtrl'
	})
	.state('job.dashboard', {
		url: "/dashboard",
		templateUrl: "view/job/job-dashboard.html",
		"params": {
			"jobNo": null,
			"jobDescription": null
		},
		controller: 'JobDashboardCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/job/job-dashboard.js'
                    ] 
                });
            }]
        }
	})
	.state('job.info', {
		url: "/info",
		templateUrl: "view/job/job-info.html",
		controller: 'JobInfoCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/job/job-info.js'
                    ] 
                });
            }]
        }
	})
	.state('job.dates', {
		url: "/dates",
		templateUrl: "view/job/job-dates.html",
		controller: 'JobInfoCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/job/job-info.js'
                    ] 
                });
            }]
        }
	})
	
	
	//Subcontract
	.state('subcontract-select', {
		url: "/subcontract-select",
		parent: "navigation",
		templateUrl: "view/subcontract/subcontract-select.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/subcontract-select.js',
                           'js/controller/subcontract/subcontract-create.js',
                           'js/service/subcontact-service.js'
                    ] 
                });
            }]
        },
		controller: 'SubcontractSelectCtrl'
	})
	
	.state('subcontract-flow', {
		url: "/subcontract-flow",
		parent: "navigation",
		templateUrl: "view/subcontract/subcontract-flow.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/subcontract-flow.js',
                           'js/controller/subcontract/subcontract-create.js',
                           'js/controller/subcontract/subcontract-ta.js',
                           'js/controller/subcontract/subcontract-ta-details.js',
                           'js/controller/subcontract/subcontract-vendor.js',
                           'js/controller/subcontract/subcontract-vendor-feedback.js',
                           'js/controller/subcontract/subcontract-vendor-compare.js',
                           'js/controller/subcontract/subcontract-award.js',
                           'js/service/subcontact-service.js'
                    ] 
                });
            }]
        },
		controller: 'SubcontractFlowCtrl'
	})
	
	.state('subcontract', {
		url: "/subcontract",
		parent: "navigation",
		templateUrl: "view/subcontract/subcontract-menu.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/service/subcontact-service.js'
                    ] 
                });
            }]
        },
        controller: 'NavMenuCtrl'
	})
	.state('subcontract.dashboard', {
		url: "/dashboard",
		templateUrl: "view/subcontract/subcontract-dashboard.html",
		controller: 'SubcontractCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/subcontract-dashboard.js'
                    ] 
                });
            }]
        }
		
	})
	.state('subcontract.header', {
		url: "/header",
		templateUrl: "view/subcontract/subcontract-header.html",
		controller: 'SubcontractHeaderCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/subcontract-header.js'
                    ] 
                });
            }]
        }
	})
	.state('subcontract.details', {
		url: "/details",
		templateUrl: "view/subcontract/subcontract-details.html",
		controller: 'SubcontractDetailsCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/subcontract-details.js'
                    ] 
                });
            }]
        }
	})
	.state('subcontract.dates', {
		url: "/dates",
		templateUrl: "view/subcontract/subcontract-dates.html",
		controller: 'SubcontractDatesCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/subcontract-dates.js'
                    ] 
                });
            }]
        }
	})
	.state('subcontract.taDetails', {
		url: "/taDetails",
		templateUrl: "view/subcontract/subcontract-ta-details.html",
		controller: 'SubcontractTaDetailsCtrl'
	})
	.state('subcontract.attachment', {
		url: "/attachment",
		templateUrl: "view/attachment.html",
		controller: 'AttachmentCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/attachment.js'
                    ] 
                });
            }]
        }
	})
	.state('subcontract.workdone', {
		url: "/workdone",
		templateUrl: "view/subcontract/subcontract-workdone.html",
		controller: 'SubcontractWorkdoneCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/subcontract-workdone.js'
                    ] 
                });
            }]
        }
	})
	.state('subcontract.addendum', {
		url: "/addendum",
		templateUrl: "view/subcontract/addendum-select.html",
		controller: 'AddendumCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/addendum-select.js'
                    ] 
                });
            }]
        }
	})
	.state('subcontract.addendumDetails', {
		url: "/addendum/details",
		templateUrl: "view/subcontract/addendum-details.html",
		controller: 'AddendumDetailsCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/addendum-details.js',
                           
                           
                    ] 
                });
            }]
        }
	})
	.state('subcontract.payment', {
		url: "/payment",
		templateUrl: "view/subcontract/payment-select.html",
		controller: 'PaymentCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/payment-select.js',
                           'js/service/payment-service.js',
                    ] 
                });
            }]
        }
	})
	.state('subcontract.paymentdetails', {
		url: "/payment/details",
		templateUrl: "view/subcontract/payment-details.html",
		controller: 'PaymentDetailsCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/payment-details.js'
                    ] 
                });
            }]
        }
	})
	.state('subcontract.paymentInvoice', {
		url: "/payment/invoice",
		templateUrl: "view/subcontract/payment-invoice.html",
		controller: 'PaymentDetailsCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/payment-details.js'
                    ] 
                });
            }]
        }
	})
	.state('subcontract.split', {
		url: "/split",
		templateUrl: "view/subcontract/subcontract-split.html",
		controller: 'SubcontractSplitCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/subcontract-split.js'
                    ] 
                });
            }]
        }
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
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/main-cert/cert-dashboard.js',
                           'js/service/main-cert-service.js'
                    ] 
                });
            }]
        },
		controller: 'CertCtrl'
	})
	.state('cert-all-details', {
		url: "/cert-all-details",
		parent: "navigation",
		templateUrl: "view/main-cert/cert-all-details.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/main-cert/cert-all-details.js',
                           'js/service/main-cert-service.js'
                    ] 
                });
            }]
        },
		controller: 'CertAllDetailsCtrl'
	})
	.state('cert', {
		url: "/cert",
		parent: "navigation",
		templateUrl: "view/main-cert/cert-details-menu.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/main-cert/cert-details.js',
                           'js/service/main-cert-service.js'
                    ] 
                });
            }]
        },
        controller: 'NavMenuCtrl'
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
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/repackaging/repackaging.js',
                           'js/service/repackaging-service.js'
                    ] 
                });
            }]
        },
		controller: 'RepackagingCtrl'
	})
	.state('repackaging-update', {
		url: "/repackaging-update",
		parent: "navigation",
		templateUrl: "view/repackaging/repackaging-update.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/repackaging/repackaging-update.js',
                           'js/service/repackaging-service.js'
                    ] 
                });
            }]
        },
		controller: 'RepackagingUpdateCtrl'
	})
	
	//Transit
	.state("transit", {
		url: "/transit",
		parent: "navigation",
		templateUrl: "view/transit/transit-dashboard.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/transit/transit-dashboard.js'
                    ] 
                });
            }]
        },
		controller: "TransitCtrl"
	})
	
	//IV
	.state("iv", {
		url: "/iv",
		parent: "navigation",
		templateUrl: "view/iv/iv-menu.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [	
               	         	'js/service/iv-service.js'
                    ] 
                });
            }]
        },
        controller: 'NavMenuCtrl'
	})
	.state("iv.update", {
		url: "/update",
		templateUrl: "view/iv/iv-update.html",
		controller: "IVUpdateCtrl",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [	'js/controller/iv/iv-update.js'
                    ] 
                });
            }]
        }
	})
	.state("iv.post", {
		url: "/post",
		templateUrl: "view/iv/iv-post.html",
		controller: "IVPostCtrl",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [	
               	         	'js/controller/iv/iv-post.js'
                    ] 
                });
            }]
        }
	})

	//Enquiry
	.state("enquiry", {
		url: "/enquiry",
		parent: "navigation",
		templateUrl: "view/enquiry/enquiry.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [	'js/controller/enquiry/enquiry.js'
                    ] 
                });
            }]
        },
		controller: "EnquiryCtrl"
	})
	
	//Admin
	.state("admin", {
		url: "/admin",
		parent: "navigation",
		templateUrl: "view/admin/admin-menu.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/admin/admin-session.js'
                    ] 
                });
            }]
        },
        controller: 'NavMenuCtrl'
	})
	.state("admin.session",{
		url: "/session",
		templateUrl: "view/admin/admin-session.html",
		"params": {
			"jobNo": null,
			"jobDescription": null
		},
		controller: "AdminSessionCtrl"
	})
	
}])
.filter('jsonDate', ['$filter', function ($filter) {
	return function (input, format) {
		return (input) 
		? $filter('date')(input, format) : '';
	};
}]);

mainApp.config(['$httpProvider', function($httpProvider){
		/**The custom “X-Requested-With” is a conventional header sent by browser clients, and it used to be the default in Angular but they took it out in 1.3.0. 
		 * Spring Security responds to it by not sending a “WWW-Authenticate” header in a 401 response, and thus the browser will not pop up an authentication dialog**/
		 //$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
		
		var httpIntercepter = ['$rootScope', '$q', '$log', '$window', function($rootScope, $q, $log, $window) {
			return {
				'request' : function(config) {
					return config;
				},
				'requestError' : function(rejection) {
					return $q.reject(rejection);
				},
				'response' : function(response) {
					return response;
				},
				'responseError' : function(rejection) {
					var status = rejection.status;
					var deferred = $q.defer();
					if(status === 401 || status === 405) {
						$window.location.href = 'login.htm';
					}
					deferred.reject(rejection);
					return deferred.promise;
				}
			}
		}];
		$httpProvider.interceptors.push(httpIntercepter);
}]);


//Config color code for charts
mainApp.config(['ChartJsProvider', 'colorCode', function (ChartJsProvider, colorCode) {
	/*Chart.defaults.global.tooltipTemplate = function (label) {
	    return label.datasetLabel + ': $' + Number(label.value).toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	}; */
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

/**Http intercepter: Convert Json date to javascript date object**/
/*mainApp.config(["$httpProvider", function ($httpProvider) {
    $httpProvider.defaults.transformResponse.push(function(responseData){
       convertDateStringsToDates(responseData);
       return responseData;
   });
}]);*/

var regexIso8601 = /^(\d{4}|\+\d{6})(?:-(\d{2})(?:-(\d{2})(?:T(\d{2}):(\d{2}):(\d{2})\.(\d{1,})(Z|([\-+])(\d{2}):(\d{2}))?)?)?)?$/;

function convertDateStringsToDates(input) {
    // Ignore things that aren't objects.
    if (typeof input !== "object") return input;

    for (var key in input) {
        if (!input.hasOwnProperty(key)) continue;

        var value = input[key];
        var match;
        // Check for string properties which look like dates.
        // TODO: Improve this regex to better match ISO 8601 date strings.
        if (typeof value === "string" && (match = value.match(regexIso8601))) {
            // Assume that Date.parse can parse ISO 8601 strings, or has been shimmed in older browsers to do so.
            var milliseconds = Date.parse(match[0]);
            if (!isNaN(milliseconds)) {
                input[key] = new Date(milliseconds);
            }
        } else if (typeof value === "object") {
            // Recurse into object
            convertDateStringsToDates(value);
        }
    }
}





/**
 * Check authentication and user role if location changed
 */
mainApp.run(['$rootScope', 'SessionHelper', '$window', '$document', '$location', function ($rootScope, SessionHelper, $window, $document, $location) {
  /*$rootScope.$on('$locationChangeStart', function(event, next, current) {

	 // console.log('$location.path(): '+$location.path());
	  var isLogin = $location.path() === "/login";
	  var isLogout = $location.path() === "/logout";
	  
	  if(isLogin || isLogout){
			return; // no need to redirect 
		}
	  
      if ($rootScope.authenticated) {
    	  console.log('ALLOW');
          //$location.path('/job-select');
      }
      else {
    	  console.log('DENY');
          event.preventDefault();
          $location.path('/login');
      }
  });*/

//	window.onbeforeunload = function(authenticate)
//	{
//		$http({
//		    method: "get",
//		    url: "logout",
//		    })
//			.success(function(data) {
//				console.log("logout...");
//			});
//	};
	
	SessionHelper.getCurrentSessionId()
	.then(function(data){
	    $rootScope.sessionId = data;
	});
    
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


	
	
/**Compatibility 
Angular Chart 0.8.8
Dependency:
AngularJS (tested with 1.2.x, 1.3.x and 1.4.x although it probably works with older versions)
Chart.js (requires Chart.js 1.0, tested with version 1.0.1 and 1.0.2).

Angular UI
1. UI Grid
2. UI Router 0.2.17 -- > AngularJS 1.x-1.5.0
3. UI Bootstrap 1.2.1 -- >  AngularJS 1.4.x - 1.5.0 (Same version with Angular-animate, Angular-touch)
   UI Bootstrap 0.14.3 -- >  AngularJS 1.3.x
**/
