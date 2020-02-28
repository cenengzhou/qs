var mainApp = angular.module('app', ['ui.router', 'chart.js',  'ngAnimate', 'ui.bootstrap', 'ngCookies', 'oc.lazyLoad', 'moment-picker', 'angular.vertilize', 'blockUI', 'ngSanitize', 'ngMaterial', 'ngJoyRide',
                                     'ui.grid', 'ui.grid.pagination', 'ui.grid.edit', 'ui.grid.selection', 'ui.grid.cellNav', 'ui.grid.autoResize', 'ui.grid.rowEdit', 'NgSwitchery', 'ui.tinymce',
									 'ui.grid.resizeColumns', 'ui.grid.pinning', 'ui.grid.moveColumns', 'ui.grid.exporter', 'ui.grid.importer', 'ui.grid.grouping', 'ui.grid.validate', 'ui.grid.saveState', 'angular-js-xlsx',
									 'ng-currency', 'angular.filter']);  

// configure our routes    
mainApp.config(['$stateProvider', '$urlRouterProvider', '$httpProvider','GlobalParameter', 
        function($stateProvider, $urlRouterProvider, $httpProvider, GlobalParameter/*, modalStateProvider*/) {
	
	// For any unmatched url, redirect to /state1
	$urlRouterProvider.otherwise("/job-select");  

	
	$stateProvider
	.state('navigation', {
	    templateUrl: 'navigation-menu.html?@PROJECT_VERSION@',
	    abstract: true,
	    resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/service/jde-service.js?@PROJECT_VERSION@',
                           'js/service/adl-service.js?@PROJECT_VERSION@',
                           'js/service/hr-service.js?@PROJECT_VERSION@',
                           'js/service/subcontractor-service.js?@PROJECT_VERSION@',
                           'js/service/userpreference-service.js?@PROJECT_VERSION@',
                           'js/service/job-service.js?@PROJECT_VERSION@',
                           'js/service/system-service.js?@PROJECT_VERSION@',
                           'js/service/ap-service.js?@PROJECT_VERSION@',
                           'js/service/userpreference-service.js?@PROJECT_VERSION@',
                           'js/service/transit-service.js?@PROJECT_VERSION@',
                           'js/system/rootscope-service.js?@PROJECT_VERSION@',
                           'js/controller/message-modal.js?@PROJECT_VERSION@',
                           'js/controller/excelupload-modal.js?@PROJECT_VERSION@',
                           'js/controller/infotips-modal.js?@PROJECT_VERSION@',
                           'js/controller/forms-modal.js?@PROJECT_VERSION@',
                           'js/service/attachment-service.js?@PROJECT_VERSION@',
                           'js/controller/attachment/attachment-text-editor.js?@PROJECT_VERSION@',
                           'js/controller/addressbook-details-modal.js?@PROJECT_VERSION@',
                           'js/controller/navigation-menu.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        },
		 controller: 'NavigationCtrl'
	  })
	 .state('profile',{
		 url: '/profile',
		 parent: 'navigation',
		 templateUrl: 'view/profile/profile.html?@PROJECT_VERSION@',
		 resolve: {
			 service: ['$ocLazyLoad', function($ocLazyLoad){
				 return $ocLazyLoad.load({
					 name: 'app',
					 files: [
						 'js/controller/profile/profile-controller.js?@PROJECT_VERSION@'
					 ]
				 })
			 }]
		 },
		 controller: 'ProfileCtrl'
	 })
	
	.state('logout', {
		url: "/logout",
		templateUrl: "logout.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/logout.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        },
		controller: 'LogoutCtrl'
	})
	.state('job-select', {
		url: "/job-select",
		parent: "navigation",
		templateUrl: "view/job-select.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/job/job-select.js?@PROJECT_VERSION@',
                           'js/service/job-service.js?@PROJECT_VERSION@',
                           'js/service/transit-service.js?@PROJECT_VERSION@',
                           'js/controller/transit/transit-header.js?@PROJECT_VERSION@',
                           'js/controller/transit/transit-enquiry.js?@PROJECT_VERSION@',
                    ] 
                });
            }]
        },
		controller: 'JobSelectCtrl',
		
	}) 
	  
	
	
	
	//Job
	.state('job', {
		url: "/job",
		parent: "navigation",
		templateUrl: "view/job/job-menu.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/service/job-service.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        },
        controller: 'NavMenuCtrl'
	})
	.state('job.dashboard', {
		url: "/dashboard",
		templateUrl: "view/job/job-dashboard.html?@PROJECT_VERSION@",
		controller: 'JobDashboardCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/job/job-dashboard.js?@PROJECT_VERSION@',
                           'js/service/adl-service.js?@PROJECT_VERSION@',
                           'js/service/resource-summary-service.js?@PROJECT_VERSION@',
                           'js/service/repackaging-service.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        }
	})
	.state('job.info', {
		url: "/info",
		templateUrl: "view/job/job-info.html?@PROJECT_VERSION@",
		controller: 'JobInfoCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/job/job-info.js?@PROJECT_VERSION@',
                           'js/service/subcontract-service.js?@PROJECT_VERSION@',
                    ] 
                });
            }]
        }
	})
	.state('job.kpi', {
		url: "/kpi",
		templateUrl: "view/job/job-variation-kpi.html?@PROJECT_VERSION@",
		controller: 'JobVariationKpiCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
               		 		'js/controller/job/job-variation-kpi.js?@PROJECT_VERSION@',
                           'js/service/variation-kpi-service.js?@PROJECT_VERSION@',
                    ] 
                });
            }]
        }
	})
	.state('job.attachment', {
		templateUrl: "view/job/job-attachment.html?@PROJECT_VERSION@",
	})
	.state('job.attachment.first', {
		url: "/attachment",
		templateUrl: "view/attachment/attachment-main.html?@PROJECT_VERSION@",
		controller: 'AttachmentMainCtrl',
		params:{
			'nameObject': GlobalParameter['AbstractAttachment'].JobInfoNameObject,
			'offsetTop':280
		},
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/attachment/attachment-main.js?@PROJECT_VERSION@',
                           'js/controller/attachment/attachment-text-editor.js?@PROJECT_VERSION@',
                           'js/service/attachment-service.js?@PROJECT_VERSION@',
                           'js/service/main-cert-service.js?@PROJECT_VERSION@',
                           'js/service/payment-service.js?@PROJECT_VERSION@',
                           'js/service/addendum-service.js?@PROJECT_VERSION@',
                           'js/service/subcontract-service.js?@PROJECT_VERSION@',
                    ] 
                });
            }]
        }
	})
	.state('job.accountMaster', {
		url: "/accountMaster",
		templateUrl: "view/job/job-account-master.html?@PROJECT_VERSION@",
		controller: 'JobAccountMasterCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
               	           'js/service/jde-service.js?@PROJECT_VERSION@',
                           'js/controller/job/job-account-master.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        }
	})
	.state('job.personnel', {
		url: "/personnel",
		templateUrl: "view/job/job-personnel2.html?@PROJECT_VERSION@",
		controller: "JobPersonnel2Ctrl as ctrl",
		resolve: {
			service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
               		 		'js/service/job-service.js?@PROJECT_VERSION@',
               		 		'js/service/personnel-service.js?@PROJECT_VERSION@',
               		 		'js/controller/job/job-personnel2.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
		}
	})
	
	//Subcontract
	.state('subcontract-select', {
		url: "/subcontract-select",
		parent: "navigation",
		templateUrl: "view/subcontract/subcontract-select.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/subcontract-select.js?@PROJECT_VERSION@',
                           'js/controller/subcontract/subcontract-create.js?@PROJECT_VERSION@',
                           'js/service/subcontract-service.js?@PROJECT_VERSION@',
                           'js/service/repackaging-service.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        },
		controller: 'SubcontractSelectCtrl'
	})
	
	.state('subcontract-award', {
		url: "/subcontract-award/tab",
		parent: "navigation",
		templateUrl: "view/subcontract/subcontract-award-tab.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/service/subcontract-service.js?@PROJECT_VERSION@',
                           'js/service/payment-service.js?@PROJECT_VERSION@',
                           'js/controller/subcontract/subcontract-menu.js?@PROJECT_VERSION@',
                           'js/service/addendum-service.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        },
        controller: 'SubcontractMenuCtrl'
	})
	.state('subcontract-award.hearder', {
		url: "/header",
		templateUrl: "view/subcontract/subcontract-award-header.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/subcontract-create.js?@PROJECT_VERSION@',
                           'js/service/job-service.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        },
        controller: 'SubcontractCreateCtrl'
	})
	.state('subcontract-award.assign', {
		url: "/assign",
		templateUrl: "view/subcontract/subcontract-award-assign.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
               	         'js/controller/subcontract/subcontract-award-assign.js?@PROJECT_VERSION@',
               	         'js/controller/repackaging/repackaging-split.js?@PROJECT_VERSION@',
               	         'js/controller/repackaging/repackaging-add.js?@PROJECT_VERSION@',
               	         'js/service/resource-summary-service.js?@PROJECT_VERSION@',
               	         'js/service/jde-service.js?@PROJECT_VERSION@',
               	         'js/service/system-service.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        },
        controller: 'RepackagingAssignResourcesCtrl'
	})
	.state('subcontract-award.ta', {
		url: "/ta",
		templateUrl: "view/subcontract/subcontract-award-ta.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
               	         'js/controller/subcontract/subcontract-award-ta.js?@PROJECT_VERSION@',
               	         'js/service/resource-summary-service.js?@PROJECT_VERSION@',
               	         'js/service/tender-service.js?@PROJECT_VERSION@',
               	         'js/service/jde-service.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        },
        controller: 'SubcontractTACtrl'
	})
	.state('subcontract-award.vendor', {
		url: "/vendor",
		templateUrl: "view/subcontract/subcontract-award-vendor.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
               	         'js/controller/subcontract/subcontract-vendor.js?@PROJECT_VERSION@',
               	         'js/controller/subcontract/subcontract-vendor-feedback.js?@PROJECT_VERSION@',
               	         'js/service/jde-service.js?@PROJECT_VERSION@',
               	         'js/service/tender-service.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        },
        controller: 'SubcontractorCtrl'
	})
	.state('subcontract-award.variance', {
		url: "/variance",
		templateUrl: "view/subcontract/subcontract-award-variance.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
               	         'js/controller/subcontract/subcontract-award-variance.js?@PROJECT_VERSION@',
               	         'js/service/tender-service.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        },
        controller: 'TenderVarianceCtrl'
	})
	.state('subcontract-award.dates', {
		url: "/dates",
		templateUrl: "view/subcontract/subcontract-award-dates.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
               	         'js/controller/subcontract/subcontract-dates.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        },
        controller: 'SubcontractDatesCtrl'
	})
	.state('subcontract-award.summary', {
		url: "/summary",
		templateUrl: "view/subcontract/subcontract-award-summary.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
               	         'js/controller/subcontract/subcontract-award-summary.js?@PROJECT_VERSION@',
               	         'js/service/tender-service.js?@PROJECT_VERSION@',
               	         'js/service/jde-service.js?@PROJECT_VERSION@',
               	         'js/service/html-service.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        },
        controller: 'SubcontractAwardSummaryCtrl'
	})
	.state('subcontract-award.attachment', {
		url: "/attachment",
		templateUrl: "view/attachment/attachment-main.html?@PROJECT_VERSION@",
		controller: 'AttachmentMainCtrl',
		params:{
			'nameObject': GlobalParameter['AbstractAttachment'].SCPackageNameObject,
			'offsetTop':450
		},
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/attachment/attachment-main.js?@PROJECT_VERSION@',
                           'js/controller/attachment/attachment-text-editor.js?@PROJECT_VERSION@',
                           'js/service/attachment-service.js?@PROJECT_VERSION@',
                           'js/service/main-cert-service.js?@PROJECT_VERSION@',
                    ] 
                });
            }]
        }
	})
	.state('subcontract', {
		url: "/subcontract",
		parent: "navigation",
		templateUrl: "view/subcontract/subcontract-menu.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/service/subcontract-service.js?@PROJECT_VERSION@',
             		 	   'js/service/subcontract-date-service.js?@PROJECT_VERSION@',
                           'js/service/payment-service.js?@PROJECT_VERSION@',
                           'js/service/comment-service.js?@PROJECT_VERSION@',
                           'js/controller/subcontract/subcontract-menu.js?@PROJECT_VERSION@',
                           'js/service/addendum-service.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        },
        controller: 'SubcontractMenuCtrl'
	})
	.state('subcontract.dashboard', {
		url: "/dashboard",
		templateUrl: "view/subcontract/subcontract-dashboard.html?@PROJECT_VERSION@",
		controller: 'SubcontractCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/subcontract-dashboard.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        }
		
	})
	.state('subcontract.header', {
		url: "/header",
		templateUrl: "view/subcontract/subcontract-header.html?@PROJECT_VERSION@",
		controller: 'SubcontractHeaderCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/subcontract-header.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        }
	})
	.state('subcontract.details', {
		url: "/details",
		templateUrl: "view/subcontract/subcontract-details.html?@PROJECT_VERSION@",
		controller: 'SubcontractDetailsCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/subcontract-details.js?@PROJECT_VERSION@',
                           'js/controller/subcontract/subcontract-retention-details.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        }
	})
	.state('subcontract.dates', {
		url: "/dates",
		templateUrl: "view/subcontract/subcontract-dates.html?@PROJECT_VERSION@",
		controller: 'SubcontractDatesCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/subcontract-dates.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        }
	})
	.state('subcontract.sti', {
		url: '/sti',
		templateUrl: "view/subcontract/subcontract-tracking-info.html?@PROJECT_VERSION@",
		controller: 'SubcontractTrackingInfoCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/subcontract-tracking-info.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        }	
	})
	.state('subcontract.attachment', {
		templateUrl: "view/subcontract/subcontract-attachment.html?@PROJECT_VERSION@"
	})
	.state('subcontract.attachment.first', {
		url: "/attachment",
		templateUrl: "view/attachment/attachment-main.html?@PROJECT_VERSION@",
		controller: 'AttachmentMainCtrl',
		params:{
			'nameObject': GlobalParameter['AbstractAttachment'].SCPackageNameObject,
			'offsetTop':280
		},
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
               		 	   'js/controller/attachment/modal/attachment-subcontract-select-modal.js?@PROJECT_VERSION@',
                           'js/controller/attachment/attachment-main.js?@PROJECT_VERSION@',
                           'js/controller/attachment/attachment-text-editor.js?@PROJECT_VERSION@',
                           'js/service/attachment-service.js?@PROJECT_VERSION@',
                           'js/service/main-cert-service.js?@PROJECT_VERSION@',
                    ] 
                });
            }]
        }
	})
	.state('subcontract.workdone', {
		url: "/workdone",
		templateUrl: "view/subcontract/subcontract-workdone.html?@PROJECT_VERSION@",
		controller: 'SubcontractWorkdoneCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/subcontract-workdone.js?@PROJECT_VERSION@',
                           'js/service/resource-summary-service.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        }
	})
	.state('subcontract.addendum-select', {
		url: "/addendum-select",
		templateUrl: "view/subcontract/addendum/addendum-select.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/addendum/addendum-select.js?@PROJECT_VERSION@',
                           'js/service/addendum-service.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        },
        controller: 'AddendumSelectCtrl'
	})

	.state('subcontract.addendum', {
		url: "/addendum/tab",
		templateUrl: "view/subcontract/addendum/addendum-tab.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/service/addendum-service.js?@PROJECT_VERSION@' 
                    ] 
                });
            }]
        }
	})
	.state('subcontract.addendum.title', {
		url: "/title",
		templateUrl: "view/subcontract/addendum/addendum-title.html?@PROJECT_VERSION@",
		params: {
			"addendumNo": null 
		},
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/addendum/addendum-title.js?@PROJECT_VERSION@',
                    ] 
                });
            }]
        },
        controller: 'AddendumTitleCtrl'
		
	})
	.state('subcontract.addendum.details', {
		url: "/details",
		templateUrl: "view/subcontract/addendum/addendum-details.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
               	         	'js/service/jde-service.js?@PROJECT_VERSION@',
               	         	'js/service/resource-summary-service.js?@PROJECT_VERSION@',
               	         	'js/service/repackaging-service.js?@PROJECT_VERSION@',
               	         	'js/controller/subcontract/addendum/addendum-details.js?@PROJECT_VERSION@',
               	         	'js/controller/subcontract/addendum/addendum-detail-add.js?@PROJECT_VERSION@',
               	         	'js/controller/subcontract/addendum/addendum-detail-add-v3.js?@PROJECT_VERSION@',
               	         	'js/controller/subcontract/addendum/addendum-detail-update.js?@PROJECT_VERSION@',
                    ] 
                });
            }]
        },
		controller: 'AddendumDetailsCtrl'
	})
       
	.state('subcontract.addendum.detail-list', {
		url: "/detail-list",
		templateUrl: "view/subcontract/addendum/addendum-detail-list.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/addendum/addendum-detail-list.js?@PROJECT_VERSION@',
                    ] 
                });
            }]
        },
		controller: 'AddendumDetailListCtrl'
	})
       
	.state('subcontract.addendum.attachment', {
		url: "/attachment",
		templateUrl: "view/attachment/attachment-main.html?@PROJECT_VERSION@",
		params: {
			'nameObject': GlobalParameter['AbstractAttachment'].AddendumNameObject,
			'offsetTop':440
		},
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                     'js/controller/attachment/attachment-main.js?@PROJECT_VERSION@',
                     'js/controller/attachment/attachment-text-editor.js?@PROJECT_VERSION@',
                         'js/service/attachment-service.js?@PROJECT_VERSION@',
                         'js/service/addendum-service.js?@PROJECT_VERSION@',
                         'js/service/main-cert-service.js?@PROJECT_VERSION@',
                    ] 
                });
            }]
        },
		controller: 'AttachmentMainCtrl'
	})
	.state('subcontract.addendum.summary', {
		url: "/summary",
		templateUrl: "view/subcontract/addendum/addendum-summary.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/addendum/addendum-summary.js?@PROJECT_VERSION@',
                    ] 
                });
            }]
        },
		controller: 'AddendumSummaryCtrl'
	})
	.state('subcontract.addendum.form2', {
		url: "/form2",
		templateUrl: "view/subcontract/addendum/addendum-form2.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/addendum/addendum-form2.js?@PROJECT_VERSION@',
                           'js/service/html-service.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        },
		controller: 'AddendumForm2Ctrl'
	})
	
	
	.state('subcontract.otherAddendum', {
		url: "/otherAddendum/tab",
		templateUrl: "view/subcontract/addendum/other-addendum-tab.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/service/addendum-service.js?@PROJECT_VERSION@' 
                    ] 
                });
            }]
        }
	})
	.state('subcontract.otherAddendum.detail', {
		url: "/detail",
		templateUrl: "view/subcontract/addendum/other-addendum-detail.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/addendum/other-addendum-detail.js?@PROJECT_VERSION@',
                           'js/service/jde-service.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        },
        controller: 'OtherAddendumDetailCtrl'
	})
	.state('subcontract.otherAddendum.list', {
		url: "/list",
		templateUrl: "view/subcontract/addendum/other-addendum-list.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/addendum/other-addendum-list.js?@PROJECT_VERSION@',
                    ] 
                });
            }]
        },
        controller: 'OtherAddendumListCtrl'
	})
	
	.state('subcontract.payment-select', {
		url: "/payment-select",
		templateUrl: "view/subcontract/payment/payment-select.html?@PROJECT_VERSION@",
		controller: 'PaymentCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/payment/payment-select.js?@PROJECT_VERSION@',
                           'js/service/payment-service.js?@PROJECT_VERSION@',
                           'js/controller/enquiry/modal/enquiry-supplierledgerdetails.js?@PROJECT_VERSION@',
                           'js/service/jde-service.js?@PROJECT_VERSION@',
                    ] 
                });
            }]
        }
	})
	.state('subcontract.payment', {
		url: "/payment/tab",
		templateUrl: "view/subcontract/payment/payment-tab.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/service/payment-service.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        },
//        controller: 'NavMenuCtrl'
	})
	.state('subcontract.payment.certificate', {
		url: "/certificate",
		templateUrl: "view/subcontract/payment/payment-certificate.html?@PROJECT_VERSION@",
		params: {
			"paymentCertNo": null, 
			"paymentTermsDesc": null
		},
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/payment/payment-certificate.js?@PROJECT_VERSION@',
                           'js/service/main-cert-service.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        },
        controller: 'PaymentCertCtrl'
	})
	.state('subcontract.payment.details', {
		url: "/details",
		templateUrl: "view/subcontract/payment/payment-details.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/payment/payment-details.js?@PROJECT_VERSION@',
                    ] 
                });
            }]
        },
        controller: 'PaymentDetailsCtrl',
	})
	.state('subcontract.payment.summary', {
		url: "/summary",
		templateUrl: "view/subcontract/payment/payment-summary.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
               	      'js/controller/subcontract/payment/payment-summary.js?@PROJECT_VERSION@',
                         ] 
                });
            }]
        },
		controller: 'PaymentSummaryCtrl'
	})
	.state('subcontract.payment.attachment', {
		url: "/attachment",
		templateUrl: "view/attachment/attachment-main.html?@PROJECT_VERSION@",
		params: {
			'nameObject': GlobalParameter['AbstractAttachment'].SCPaymentNameObject,
			'offsetTop':440
		},
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                         'js/controller/attachment/attachment-main.js?@PROJECT_VERSION@',
                         'js/controller/attachment/attachment-text-editor.js?@PROJECT_VERSION@',
                         'js/service/attachment-service.js?@PROJECT_VERSION@',
                         'js/service/main-cert-service.js?@PROJECT_VERSION@',
                         ] 
                });
            }]
        },
		controller: 'AttachmentMainCtrl'
	})
	.state('subcontract.payment.invoice', {
		url: "/invoice",
		templateUrl: "view/subcontract/payment/payment-invoice.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/payment/payment-invoice.js?@PROJECT_VERSION@',
                           'js/service/html-service.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        },
        controller: 'PaymentInvoiceCtrl',
	})
	
	
	.state('subcontract.split', {
		url: "/split",
		templateUrl: "view/subcontract/subcontract-split-terminate.html?@PROJECT_VERSION@",
		params: {
			'action': 'Split',
			'nameObject': GlobalParameter['AbstractAttachment'].SplitNameObject,
		},
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                         'js/controller/subcontract/subcontract-split-terminate.js?@PROJECT_VERSION@',
	                     'js/controller/attachment/attachment-main.js?@PROJECT_VERSION@',
	                     'js/controller/attachment/attachment-text-editor.js?@PROJECT_VERSION@',
	                     'js/service/attachment-service.js?@PROJECT_VERSION@',
                         'js/service/addendum-service.js?@PROJECT_VERSION@',
                         'js/service/subcontract-service.js?@PROJECT_VERSION@',
                         'js/service/main-cert-service.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        },
        controller: 'SubcontractSplitTerminateCtrl'
	})
	
	.state('subcontract.terminate', {
		url: "/terminate",
		templateUrl: "view/subcontract/subcontract-split-terminate.html?@PROJECT_VERSION@",
		params: {
			'action': 'Terminate',
			'nameObject': GlobalParameter['AbstractAttachment'].TerminateNameObject,
		},
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/subcontract-split-terminate.js?@PROJECT_VERSION@',
                           'js/controller/attachment/attachment-main.js?@PROJECT_VERSION@',
  	                       'js/controller/attachment/attachment-text-editor.js?@PROJECT_VERSION@',
                           'js/service/attachment-service.js?@PROJECT_VERSION@',
                           'js/service/addendum-service.js?@PROJECT_VERSION@',
                           'js/service/subcontract-service.js?@PROJECT_VERSION@',
                           'js/service/main-cert-service.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        },
		controller: 'SubcontractSplitTerminateCtrl'
	})
	
	
	//Main Cert
	.state('cert-dashboard', {
		url: "/cert-dashboard",
		parent: "navigation",
		templateUrl: "view/main-cert/cert-dashboard.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/main-cert/cert-dashboard.js?@PROJECT_VERSION@',
                           'js/controller/main-cert/retention-release-schedule.js?@PROJECT_VERSION@',
                           'js/service/main-cert-service.js?@PROJECT_VERSION@',
                           'js/service/job-service.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        },
		controller: 'CertCtrl'
	})
	.state('main-cert-select', {
		url: "/main-cert-select",
		parent: "navigation",
		templateUrl: "view/main-cert/main-cert-select.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/main-cert/main-cert-select.js?@PROJECT_VERSION@',
                           'js/service/main-cert-service.js?@PROJECT_VERSION@',
                           'js/controller/enquiry/modal/enquiry-customerledgerdetails.js?@PROJECT_VERSION@',
                           'js/service/jde-service.js?@PROJECT_VERSION@',
                           'js/service/subcontract-service.js?@PROJECT_VERSION@',
                    ] 
                });
            }]
        },
		controller: 'MainCertListCtrl'
	})
	
	.state('mainCert', {
		url: "/mainCert",
		parent: "navigation",
		templateUrl: "view/main-cert/main-cert-tab.html?@PROJECT_VERSION@",
		params: {
			'mainCertNo': null
		},
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [  'js/service/main-cert-service.js?@PROJECT_VERSION@',
               		 	   'js/controller/main-cert/main-cert.js?@PROJECT_VERSION@',
               		 	   'js/controller/main-cert/main-cert-ipa.js?@PROJECT_VERSION@',
               		 	   'js/controller/main-cert/main-cert-ipc.js?@PROJECT_VERSION@',
                           'js/controller/main-cert/retention-release-schedule.js?@PROJECT_VERSION@',
                           'js/controller/main-cert/contra-charge-modal.js?@PROJECT_VERSION@',
                           'js/controller/attachment/attachment-main.js?@PROJECT_VERSION@',
                           'js/controller/attachment/attachment-text-editor.js?@PROJECT_VERSION@',
                           'js/service/job-service.js?@PROJECT_VERSION@',
                           'js/service/main-cert-service.js?@PROJECT_VERSION@',
                           'js/service/attachment-service.js?@PROJECT_VERSION@',
                           'js/service/subcontract-service.js?@PROJECT_VERSION@',
                           'js/service/payment-service.js?@PROJECT_VERSION@',
                           'js/service/addendum-service.js?@PROJECT_VERSION@',
                    ] 
                });
            }]
        },
		controller: 'MainCertCtrl'
	})
	.state('mainCert.ipa', {
		url: "/ipa",
		templateUrl: "view/main-cert/main-cert-ipa.html?@PROJECT_VERSION@",
		params: {
			'mainCertNo': null
		},
		controller: 'IPACtrl'
	})
	.state('mainCert.sendIPA', {
		url: "/sendIPA",
		templateUrl: "view/main-cert/main-cert-send-ipa.html?@PROJECT_VERSION@",
		params: {
			'mainCertNo': null
		},
		controller: 'IPACtrl'
	})
	.state('mainCert.ipc', {
		url: "/ipc",
		templateUrl: "view/main-cert/main-cert-ipc.html?@PROJECT_VERSION@",
		controller: 'IPCCtrl'
	})
	.state('mainCert.rr', {
		url: "/rr",
		templateUrl: "view/main-cert/retention-release-schedule.html?@PROJECT_VERSION@",
		controller: 'RetentionReleaseScheduleCtrl'
	})
	.state('mainCert.confirmIPC', {
		url: "/confirmIPC",
		templateUrl: "view/main-cert/main-cert-confirm-reset-ipc.html?@PROJECT_VERSION@",
		controller: 'IPCCtrl'
	})
	.state('mainCert.attachment', {
		url: "/attachment",
		templateUrl: "view/attachment/attachment-main.html?@PROJECT_VERSION@",
		params: {
			'nameObject': GlobalParameter['AbstractAttachment'].MainCertNameObject,
			'offsetTop':440,
			'mainCertStatus':null
		},
		controller: 'AttachmentMainCtrl'
	})
	.state('mainCert.postIPC', {
		url: "/postIPC",
		templateUrl: "view/main-cert/main-cert-post-ipc.html?@PROJECT_VERSION@",
		controller: 'IPCCtrl'
	})
	
	
	//Repackaging
	.state('repackaging', {
		url: "/repackaging",
		parent: "navigation",
		templateUrl: "view/repackaging/repackaging.html?@PROJECT_VERSION@",
		params: {
			'version': null
		},
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
               	         'js/service/repackaging-service.js?@PROJECT_VERSION@',
               	         'js/service/resource-summary-service.js?@PROJECT_VERSION@',
               	         'js/controller/repackaging/repackaging.js?@PROJECT_VERSION@',
               	         'js/controller/repackaging/repackaging-history.js?@PROJECT_VERSION@',
               	         'js/controller/repackaging/repackaging-confirm.js?@PROJECT_VERSION@',
               	         'js/service/main-cert-service.js?@PROJECT_VERSION@'


                    ] 
                });
            }]
        },
		controller: 'RepackagingCtrl'
	})
	.state('repackaging.wizard', {
		url: "/wizard",
		views:{
			"attachment":{
				templateUrl: "view/attachment/attachment-main.html?@PROJECT_VERSION@",
				controller: 'AttachmentMainCtrl'
			}
		},
		params:{
			'nameObject': GlobalParameter['AbstractAttachment'].RepackagingNameObject,
		},
		resolve: {
			service: ['$ocLazyLoad', function($ocLazyLoad){
				return $ocLazyLoad.load({
					name: 'app',
					files: [
              	        'js/service/attachment-service.js?@PROJECT_VERSION@',
                        'js/controller/attachment/attachment-main.js?@PROJECT_VERSION@',
                        'js/controller/attachment/attachment-text-editor.js?@PROJECT_VERSION@',
                        'js/service/subcontract-service.js?@PROJECT_VERSION@',
                        'js/service/payment-service.js?@PROJECT_VERSION@',
                        'js/service/addendum-service.js?@PROJECT_VERSION@'
					]
				})
			}]
		}
	})
	.state('repackaging-update', {
		url: "/repackaging-update",
		parent: "navigation",
		templateUrl: "view/repackaging/repackaging-update.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/repackaging/repackaging-update.js?@PROJECT_VERSION@',
                           'js/controller/repackaging/repackaging-add.js?@PROJECT_VERSION@',
                           'js/controller/repackaging/repackaging-split.js?@PROJECT_VERSION@',
                           'js/service/resource-summary-service.js?@PROJECT_VERSION@',
                           'js/service/system-service.js?@PROJECT_VERSION@',
                           'js/service/jde-service.js?@PROJECT_VERSION@',
                           'js/service/subcontract-service.js?@PROJECT_VERSION@',
                    ] 
                });
            }]
        },
		controller: 'RepackagingUpdateCtrl'
	})
	.state('repackaging-email', {
		url: "/repackaging-email",
		parent: "navigation",
		templateUrl: "view/repackaging/repackaging-email.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/repackaging/repackaging-email.js?@PROJECT_VERSION@',
							'js/service/repackaging-service.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        },
		controller: 'RepackagingEmailCtrl as ctrl',
	})
	
	//Transit
	.state('transit', {
		url: "/transit",
		parent: "navigation",
		templateUrl: "view/transit/transit-tab.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
               		 	'js/controller/transit/modal/CodeMatchEnquiryModal.js?@PROJECT_VERSION@',
               		 	'js/controller/transit/transit.js?@PROJECT_VERSION@',
	               		'js/controller/transit/transit-import.js?@PROJECT_VERSION@',
	               		'js/controller/transit/transit-confirm.js?@PROJECT_VERSION@',
	               		'js/controller/transit/transit-complete.js?@PROJECT_VERSION@',
	               		//'js/controller/admin/admin-TransitUOMMaintenance.js?@PROJECT_VERSION@',
                        //'js/controller/admin/admin-TransitResourceCodeMaintenance.js?@PROJECT_VERSION@',
                        'js/service/resource-summary-service.js?@PROJECT_VERSION@',
	               		'js/service/subcontract-service.js?@PROJECT_VERSION@',
	               		'js/service/transit-service.js?@PROJECT_VERSION@',
	               		'js/service/jde-service.js?@PROJECT_VERSION@',
                        'js/controller/transit/transit-header.js?@PROJECT_VERSION@',
                    ] 
                });
            }]
        },
        controller: 'TransitCtrl'
	})
	.state('transit.userGuide', {
		url: "/userGuide",
		templateUrl: "view/transit/transit-user-guide.html?@PROJECT_VERSION@",
        controller: 'TransitCtrl'
	})
	.state('transit.bq', {
		url: "/BQ",
		templateUrl: "view/transit/transit-bq.html?@PROJECT_VERSION@",
        controller: 'TransitImportCtrl'
	})
	.state('transit.resources', {
		url: "/resources",
		templateUrl: "view/transit/transit-resources.html?@PROJECT_VERSION@",
        controller: 'TransitImportCtrl'
	})
	.state('transit.confirm', {
		url: "/confirm",
		templateUrl: "view/transit/transit-confirm.html?@PROJECT_VERSION@",
        controller: 'TransitConfirmCtrl'
	})
	.state('transit.report', {
		url: "/report",
		templateUrl: "view/transit/transit-report.html?@PROJECT_VERSION@",
        controller: 'TransitCompleteCtrl'
	})
	.state('transit.attachment', {
		url: "/attachment",
		templateUrl: "view/attachment/attachment-main.html?@PROJECT_VERSION@",
		params: {
			'nameObject': GlobalParameter['AbstractAttachment'].TransitNameObject,
			'offsetTop':440
		},
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	files: [
                    'js/controller/attachment/attachment-main.js?@PROJECT_VERSION@',
                    'js/controller/attachment/attachment-text-editor.js?@PROJECT_VERSION@',
                    'js/service/attachment-service.js?@PROJECT_VERSION@',
                    'js/service/addendum-service.js?@PROJECT_VERSION@',
                    'js/service/payment-service.js?@PROJECT_VERSION@',
                    'js/service/main-cert-service.js?@PROJECT_VERSION@',
               ] 
                });
            }]
        },
		controller: 'AttachmentMainCtrl'
	})
	.state('transit.complete', {
		url: "/complete",
		templateUrl: "view/transit/transit-complete.html?@PROJECT_VERSION@",
        controller: 'TransitCompleteCtrl'
	})
	
	.state("iv", {
		url: "/iv",
		parent: "navigation",
		templateUrl: "view/iv/iv-tab.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [	
               	         	'js/service/resource-summary-service.js?@PROJECT_VERSION@',
               	         	'js/service/subcontract-service.js?@PROJECT_VERSION@',
               	         	'js/service/repackaging-service.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        },
		controller: "NavMenuCtrl",
	})
	.state("iv.update", {
		url: "/update",
		templateUrl: "view/iv/iv-update.html?@PROJECT_VERSION@",
		controller: "IVUpdateCtrl",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [	'js/controller/iv/iv-update.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        }
	})
	.state("iv.post", {
		url: "/post",
		templateUrl: "view/iv/iv-post.html?@PROJECT_VERSION@",
		controller: "IVPostCtrl",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [	
               	         	'js/controller/iv/iv-post.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        }
	})
	.state("iv.final", {
		url: "/final",
		templateUrl: "view/iv/iv-post.html?@PROJECT_VERSION@",
		controller: "IVPostCtrl",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [	
               	         	'js/controller/iv/iv-post.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        }
	})

	//Enquiry
	.state("enquiry", {
		url: "/enquiry",
		parent: "navigation",
		templateUrl: "view/enquiry/enquiry-menu.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [	
							'js/controller/enquiry/enquiry-jobinfo.js?@PROJECT_VERSION@',
							'js/controller/enquiry/enquiry-jobcost.js?@PROJECT_VERSION@',
							'js/controller/enquiry/enquiry-jobcost-adl.js?@PROJECT_VERSION@',
							'js/controller/enquiry/enquiry-jobcost-jde.js?@PROJECT_VERSION@',
							'js/controller/enquiry/enquiry-ivhistory.js?@PROJECT_VERSION@',
							'js/controller/enquiry/enquiry-main-cert.js?@PROJECT_VERSION@',
							
							'js/controller/enquiry/enquiry-subcontract.js?@PROJECT_VERSION@',
							'js/controller/enquiry/enquiry-subcontractdetail.js?@PROJECT_VERSION@',
							'js/controller/enquiry/enquiry-payment.js?@PROJECT_VERSION@',
							'js/controller/enquiry/enquiry-provisionhistory.js?@PROJECT_VERSION@',
							'js/controller/enquiry/enquiry-workscope.js?@PROJECT_VERSION@',
							
							'js/controller/enquiry/enquiry-subcontractor.js?@PROJECT_VERSION@',
							'js/controller/enquiry/enquiry-client.js?@PROJECT_VERSION@',
							
							'js/controller/enquiry/enquiry-purchaseorder.js?@PROJECT_VERSION@',
							'js/controller/enquiry/enquiry-purchaseorderdetail.js?@PROJECT_VERSION@',
							
							'js/controller/enquiry/enquiry-accountledger.js?@PROJECT_VERSION@',
							'js/controller/enquiry/enquiry-customerledger.js?@PROJECT_VERSION@',
							'js/controller/enquiry/enquiry-supplierledger.js?@PROJECT_VERSION@',
							'js/controller/enquiry/enquiry-performanceappraisal.js?@PROJECT_VERSION@',
							'js/controller/enquiry/modal/enquiry-jobcostdetails-adl.js?@PROJECT_VERSION@',
							'js/controller/enquiry/modal/enquiry-customerledgerdetails.js?@PROJECT_VERSION@',
							'js/controller/enquiry/modal/enquiry-supplierledgerdetails.js?@PROJECT_VERSION@',

               	         	'js/service/main-cert-service.js?@PROJECT_VERSION@',
               	         	'js/service/payment-service.js?@PROJECT_VERSION@',
               	         	'js/service/subcontract-service.js?@PROJECT_VERSION@',
               	         	'js/service/adl-service.js?@PROJECT_VERSION@',
               	         	'js/service/system-service.js?@PROJECT_VERSION@',
               	         	'js/service/subcontractor-service.js?@PROJECT_VERSION@',
               	         	'js/service/resource-summary-service.js?@PROJECT_VERSION@',
               	         	'js/service/job-service.js?@PROJECT_VERSION@',
               	         	'js/controller/nav-menu.js?@PROJECT_VERSION@',
               	         	'js/service/jde-service.js?@PROJECT_VERSION@',
               	         	'js/service/main-cert-service.js?@PROJECT_VERSION@'
                    ] 
                });
            }]
        },
        controller: 'NavMenuCtrl',
        controllerAs: 'navMenuCtrl'
	})
	.state('enquiry.info', {
		url: '/info',
		templateUrl: 'view/enquiry/enquiry-info.html?@PROJECT_VERSION@',
		params: {
			'jobNo': null,
			'jobDescription': null,
		}
	})
	.state('enquiry.jobInfo', {
		url: '/jobInfo',
		templateUrl: 'view/enquiry/enquiry-jobinfo.html?@PROJECT_VERSION@',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquiryJobInfoCtrl'
		})
	.state('enquiry.jobCost', {
		url: '/jobCost',
		templateUrl: 'view/enquiry/enquiry-jobcost.html?@PROJECT_VERSION@',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquiryJobCostCtrl'
		})
	.state('enquiry.jobCost.adl', {
		url: '/adl',
		templateUrl: 'view/enquiry/enquiry-jobcost-adl.html?@PROJECT_VERSION@',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquiryJobCostAdlCtrl'
		})
	.state('enquiry.jobCost.jde', {
		url: '/jde',
		templateUrl: 'view/enquiry/enquiry-jobcost-jde.html?@PROJECT_VERSION@',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquiryJobCostJdeCtrl'
		})	
	.state('enquiry.ivHistory', {
		url: '/ivHistory',
		templateUrl: 'view/enquiry/enquiry-ivhistory.html?@PROJECT_VERSION@',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquiryIvHistoryCtrl'
	})
	.state('enquiry.subcontract', {
		url: '/subcontract',
		templateUrl: 'view/enquiry/enquiry-subcontract.html?@PROJECT_VERSION@',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquirySubcontractCtrl'
		})
	.state('enquiry.subcontractDetail', {
		url: '/subcontractDetail',
		templateUrl: 'view/enquiry/enquiry-subcontractdetail.html?@PROJECT_VERSION@',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquirySubcontractDetailCtrl'
		})
	.state('enquiry.payment', {
		url: '/payment',
		templateUrl: 'view/enquiry/enquiry-payment.html?@PROJECT_VERSION@',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquiryPaymentCtrl'
		})
	.state('enquiry.provisionHistory', {
		url: '/provisionHistory',
		templateUrl: 'view/enquiry/enquiry-provisionhistory.html?@PROJECT_VERSION@',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquiryProvisionHistoryCtrl'
		})
	.state('enquiry.workScope', {
		url: '/workScope',
		templateUrl: 'view/enquiry/enquiry-workscope.html?@PROJECT_VERSION@',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquiryWorkScopeCtrl'
		})
	.state('enquiry.subcontractor', {
		url: '/subcontractor',
		templateUrl: 'view/enquiry/enquiry-subcontractor.html?@PROJECT_VERSION@',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquirySubcontractorCtrl'
		})
	.state('enquiry.client', {
		url: '/client',
		templateUrl: 'view/enquiry/enquiry-client.html?@PROJECT_VERSION@',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquiryClientCtrl'
		})
	.state('enquiry.purchaseOrder', {
		url: '/purchaseOrder',
		templateUrl: 'view/enquiry/enquiry-purchaseorder.html?@PROJECT_VERSION@',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquiryPurchaseOrderCtrl'
		})
	.state('enquiry.purchaseOrderDetail', {
		url: '/purchaseOrderDetail',
		templateUrl: 'view/enquiry/enquiry-purchaseorderdetail.html?@PROJECT_VERSION@',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquiryPurchaseOrderDetailCtrl'
		})
	.state('enquiry.accountLedger', {
		url: '/accountLedger',
		templateUrl: 'view/enquiry/enquiry-accountledger.html?@PROJECT_VERSION@',
		params: {
			'jobNo': null,
			'jobDescription' : null,
			'searchObject' : null,
		},
		controller: 'EnquiryAccountLedgerCtrl'
		})
	.state('enquiry.customerLedger', {
		url: '/customerLedger',
		templateUrl: 'view/enquiry/enquiry-customerledger.html?@PROJECT_VERSION@',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquiryCustomerLedgerCtrl'
		})
	.state('enquiry.supplierLedger', {
		url: '/supplierLedger',
		templateUrl: 'view/enquiry/enquiry-supplierledger.html?@PROJECT_VERSION@',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquirySupplierLedgerCtrl'
		})
	.state('enquiry.performanceAppraisal', {
		url: '/performanceAppraisal',
		templateUrl: 'view/enquiry/enquiry-performanceappraisal.html?@PROJECT_VERSION@',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquiryPerformanceAppraisalCtrl'
		})
	.state('enquiry.main-cert', {
		url: '/main-cert',
		templateUrl: 'view/enquiry/enquiry-main-cert.html?@PROJECT_VERSION@',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquiryMainCertCtrl'
		})
		
	//Reports
	.state("reports", {
		url: "/reports",
		parent: "navigation",
		templateUrl: "view/reports/reports-main.html?@PROJECT_VERSION@",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [	
							'js/controller/reports/reports-main.js?@PROJECT_VERSION@',
							'js/service/job-service.js?@PROJECT_VERSION@',
							'js/service/adl-service.js?@PROJECT_VERSION@',
                    ] 
                });
            }]
        },
        controller: 'ReportMainCtrl'
	})
	//Admin
	.state('admin', {
		url: '/admin',
		parent: 'navigation',
		templateUrl: 'view/admin/admin-menu.html?@PROJECT_VERSION@',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [  'js/service/job-service.js?@PROJECT_VERSION@',  
               		 	   'js/service/subcontract-service.js?@PROJECT_VERSION@',
               	           'js/service/main-cert-service.js?@PROJECT_VERSION@',
               	           'js/service/payment-service.js?@PROJECT_VERSION@',
               	           'js/service/transit-service.js?@PROJECT_VERSION@',
               	           'js/service/addendum-service.js?@PROJECT_VERSION@',
               	           'js/service/resource-summary-service.js?@PROJECT_VERSION@',
               	           'js/service/system-service.js?@PROJECT_VERSION@',
               	           'js/service/repackaging-service.js?@PROJECT_VERSION@',
                           'js/controller/admin/admin-session.js?@PROJECT_VERSION@',
                           'js/controller/admin/admin-ManualProcedures.js?@PROJECT_VERSION@',
                           'js/controller/admin/admin-Revisions.js?@PROJECT_VERSION@',
                           'js/controller/admin/admin-Revisions-Subcontract.js?@PROJECT_VERSION@',
                           'js/controller/admin/admin-Revisions-SubcontractDetail.js?@PROJECT_VERSION@',
                           'js/controller/admin/admin-Revisions-Payment.js?@PROJECT_VERSION@',
                           'js/controller/admin/admin-Revisions-Addendum.js?@PROJECT_VERSION@',
                           'js/controller/admin/admin-Revisions-AddendumDetail.js?@PROJECT_VERSION@',
                           'js/controller/admin/admin-Revisions-MainCert.js?@PROJECT_VERSION@',
                           'js/controller/admin/admin-Revisions-Repackaging.js?@PROJECT_VERSION@',
                           'js/controller/admin/admin-Revisions-Approval.js?@PROJECT_VERSION@',
                           'js/controller/admin/admin-Revisions-Attachment.js?@PROJECT_VERSION@',
                           'js/controller/admin/admin-TransitUOMMaintenance.js?@PROJECT_VERSION@',
                           'js/controller/admin/admin-TransitResourceCodeMaintenance.js?@PROJECT_VERSION@',
                           'js/controller/admin/admin-SubcontractStandardTermsMaintenance.js?@PROJECT_VERSION@',
                           'js/controller/admin/admin-SubcontractStandardTermsAddModal.js?@PROJECT_VERSION@',
                           'js/controller/admin/admin-SchedulerMaintenance.js?@PROJECT_VERSION@',
                           'js/controller/admin/admin-SystemInformation.js?@PROJECT_VERSION@',
                           'js/controller/admin/admin-AnnouncementSetting.js?@PROJECT_VERSION@',
                           'view/admin/admin-Revisions-Subcontract.html?@PROJECT_VERSION@',
                           'view/admin/admin-Revisions-Payment.html?@PROJECT_VERSION@',
                           'view/admin/admin-Revisions-Addendum.html?@PROJECT_VERSION@',
                           'view/admin/admin-Revisions-MainCert.html?@PROJECT_VERSION@'
                    ] 
                });
            }]
        },
        controller: 'NavMenuCtrl',
        controllerAs: 'navMenuCtrl'
	})
	.state('admin.session',{
		url: '/session',
		templateUrl: 'view/admin/admin-session.html?@PROJECT_VERSION@',
		controller: "AdminSessionCtrl",
		controllerAs: 'sessionCtrl'
	})
	.state('admin.ManualProcedures',{
		url: '/ManualProcedures',
		templateUrl: 'view/admin/admin-ManualProcedures.html?@PROJECT_VERSION@',
		controller: 'AdminManualProceduresCtrl',
		controllerAs: 'manualProceduresCtrl'
	})
	.state('admin.Revisions',{
		url: '/Revisions',
		templateUrl: 'view/admin/admin-Revisions.html?@PROJECT_VERSION@',
		params: {
			'activeTab': null
		},
		controller: 'AdminRevisionsCtrl',
		controllerAs: 'revisionsCtrl'
	})
	.state('admin.Revisions.Subcontract',{
		url: '/Subcontract',
		templateUrl: 'view/admin/admin-Revisions-Subcontract.html?@PROJECT_VERSION@',
		controller: 'AdminRevisionsSubcontractCtrl'
	})
	.state('admin.Revisions.SubcontractDetail',{
		url: '/SubcontractDetail',
		templateUrl: 'view/admin/admin-Revisions-SubcontractDetail.html?@PROJECT_VERSION@',
		controller: 'AdminRevisionsSubcontractDetailCtrl'
	})	
	.state('admin.Revisions.Payment',{
		url: '/Payment',
		templateUrl: 'view/admin/admin-Revisions-Payment.html?@PROJECT_VERSION@',
		controller: 'AdminRevisionsPaymentCtrl'
	})
	.state('admin.Revisions.Addendum',{
		url: '/Addendum',
		templateUrl: 'view/admin/admin-Revisions-Addendum.html?@PROJECT_VERSION@',
		controller: 'AdminRevisionsAddendumCtrl'
	})
	.state('admin.Revisions.AddendumDetail',{
		url: '/AddendumDetail',
		templateUrl: 'view/admin/admin-Revisions-AddendumDetail.html?@PROJECT_VERSION@',
		controller: 'AdminRevisionsAddendumDetailCtrl'
	})
	.state('admin.Revisions.MainCert',{
		url: '/MainCert',
		templateUrl: 'view/admin/admin-Revisions-MainCert.html?@PROJECT_VERSION@',
		controller: 'AdminRevisionsMainCertCtrl'
	})
	.state('admin.Revisions.Repackaging',{
		url: '/Repackaging',
		templateUrl: 'view/admin/admin-Revisions-Repackaging.html?@PROJECT_VERSION@',
		controller: 'AdminRevisionsRepackagingCtrl'
	})
	.state('admin.Revisions.Approval',{
		url: '/Approval',
		templateUrl: 'view/admin/admin-Revisions-Approval.html?@PROJECT_VERSION@',
		controller: 'AdminRevisionsApprovalCtrl'
	})
	.state('admin.Revisions.Attachment',{
		url: '/Attachment',
		templateUrl: 'view/admin/admin-Revisions-Attachment.html?@PROJECT_VERSION@',
		controller: 'AdminRevisionsAttachmentCtrl'
	})
	.state('admin.TransitUOMMaintenance',{
		url: '/TransitUOMMaintenance',
		templateUrl: 'view/admin/admin-TransitUOMMaintenance.html?@PROJECT_VERSION@',
		controller: 'AdminTransitUOMMaintenanceCtrl',
		controllerAs: 'transitUOMMaintenanceCtrl'
	})
	.state('admin.TransitResourceCodeMaintenance',{
		url: '/TransitResourceCodeMaintenance',
		templateUrl: 'view/admin/admin-TransitResourceCodeMaintenance.html?@PROJECT_VERSION@',
		controller: 'AdminTransitResourceCodeMaintenanceCtrl',
		controllerAs: 'resourceCodeMaintenanceCtrl'
	})
	.state('admin.SubcontractStandardTermsMaintenance',{
		url: '/SubcontractStandardTermsMaintenance',
		templateUrl: 'view/admin/admin-SubcontractStandardTermsMaintenance.html?@PROJECT_VERSION@',
		controller: 'AdminSubcontractStandardTermsMaintenanceCtrl',
		controllerAs: 'standardTermsMaintenanceCtrl'
	})
	.state('admin.SchedulerMaintenance',{
		url: '/SchedulerMaintenance',
		templateUrl: 'view/admin/admin-SchedulerMaintenance.html?@PROJECT_VERSION@',
		controller: 'AdminSchedulerMaintenanceCtrl',
		controllerAs: 'schedulerMaintenance'
	})
	.state('admin.SystemInformation',{
		url: '/SystemInformation',
		templateUrl: 'view/admin/admin-SystemInformation.html?@PROJECT_VERSION@'
	})
	.state('admin.HealthCheck',{
		url: '/HealthCheck',
		templateUrl: 'view/admin/admin-HealthCheck.html?@PROJECT_VERSION@'
	})
	.state('admin.announcementSetting',{
		url: '/AnnouncementSetting',
		templateUrl: 'view/admin/admin-announcementSetting.html?@PROJECT_VERSION@',
		controller: 'AnnouncementSettingCtrl'
	});

}])
.filter('jsonDate', ['$filter', function ($filter) {
	return function (input, format) {
		return (input) 
		? $filter('date')(input, format) : '';
	};
}]);

mainApp.config(['$httpProvider', function($httpProvider){
		var httpIntercepter = ['$q', '$log', '$window', '$rootScope', function($q, $log, $window, $rootScope) {
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
					switch(status){
					case 401:
					case 405:
						$window.location.href = 'login.htm?status=' + status;
						break;
					case 403:
						var maintenance = false;
						if($rootScope.user){
							angular.forEach($rootScope.user.UserRoles, function(r){
								if(r.RoleName === 'ROLE_MAINTENANCE'){
									maintenance = true;	
								}
							})
						} 
						if(maintenance){
							$window.location.href = '503.html';
						} else {
							$window.location.href = '403.html';	
						}
						break;
					case 503:
						$window.location.href = '503.html';
						break;
					}
					deferred.reject(rejection);
					return deferred.promise;
				}
			};
		}];
		$httpProvider.interceptors.push(httpIntercepter);
		
}]);


//Config color code for charts
mainApp.config(['ChartJsProvider', 'colorCode', function (ChartJsProvider, colorCode) {
	Chart.defaults.global.tooltipTemplate = function (label) {
	    return label.label + ': $' + Number(label.value).toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	}; 
	Chart.defaults.global.multiTooltipTemplate = function (label) {
	    return label.datasetLabel + ': $' + Number(label.value).toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	}; 
    // Configure all charts 
    /*ChartJsProvider.setOptions({
    	colours: [colorCode.blue, colorCode.red, colorCode.green, colorCode.yellow, colorCode.purple],
      	responsive: true
    });*/
    // Configure all line charts 
    ChartJsProvider.setOptions('Line', {
      datasetFill: false
    });
  }]);

mainApp.config(['momentPickerProvider', '$provide', function(momentPickerProvider, $provide){
	  momentPickerProvider.options({
          /* Picker properties */
          locale:        'en',
          format:        'L LTS',
          minView:       'decade',
          maxView:       'minute',
          startView:     'year',
          today:         false,
          
          /* Extra: Views properties */
          leftArrow:     '&larr;',
          rightArrow:    '&rarr;',
          yearsFormat:   'YYYY',
          monthsFormat:  'MMM',
          daysFormat:    'D',
          hoursFormat:   'HH:[00]',
          minutesFormat: moment.localeData().longDateFormat('LT').replace(/[aA]/, ''),
          secondsFormat: 'ss',
          minutesStep:   1,
          secondsStep:   1
      });
	  
	  $provide.decorator('uiGridExporterService', function($delegate, $filter){
		    $delegate.formatFieldAsCsv = function (field) {
		      if (field.value == null) {
		    	  return '';
		      }
		      if (typeof(field.value) === 'number') {
		    	  return field.value;
		      }
		      if (typeof(field.value) === 'boolean') {
		    	  return (field.value ? 'TRUE' : 'FALSE') ;
		      }
		      if (typeof(field.value) === 'string') {
		    	  if(field.value === 'text-warning fa-thumbs-up') field.value = 'Yes';
		    	  if(field.value === 'text-danger fa-thumbs-down') field.value = 'No';
		    	  return '"' + field.value.replace(/\n/g,'').replace(/"/g, '""') + '"';
		      }
		      if ( field.value instanceof Date){
		    	  return $filter('date')(field.value, 'yyyy-MM-dd');
		      }

		      return JSON.stringify(field.value);
		    };

		    return $delegate
		  });
}]);

mainApp.config(function(blockUIConfig) {

	// Change the default overlay message
	// blockUIConfig.message = '';
	
	// Apply these classes to al block-ui elements
	// blockUIConfig.cssClass = 'block-ui my-custom-class'; 

	// Enable browser navigation blocking
	// blockUIConfig.blockBrowserNavigation = true;
	
	// Disable clearing block whenever an exception has occurred
	// blockUIConfig.resetOnException = false;
	
	// Start a block with custom property values
	// blockUI.start({ myProperty: 'My value' });

	// Display the property value in the custom template.
	// blockUIConfig.template = '<div>{{ state.myProperty }}</div>';
	
	blockUIConfig.template = '<div class="block-ui-message-container"><div class="block-ui-message" ng-if="!state.hideMessage">{{state.message}}</div></div><div class="block-ui block-ui-overlay" style="z-index:100"><i class="fa fa-spinner fa-pulse fa-fw text-grey" style="width:100%; font-size: 600px;opacity: 0.1" ng-if="!state.hideAnimate"></i></div>';
	
	// Disable automatically blocking of the user interface( start a block whenever the Angular $http service has an pending request)
	blockUIConfig.autoBlock = true;
	
	// Change the default delay to 100ms before the blocking is visible
	blockUIConfig.delay = 100;
	
	// Disable auto body block
	blockUIConfig.autoInjectBodyBlock = true;
	
	blockUIConfig.requestFilter = function(config) {
//		if(config.url.match(/^service\/adl($|\/).*/)) {
//			return false; // ... don't block it.
//		}
		var message;
		var bypassBlockUI = [
			'service/mainCert/getCertificateDashboardData', 
			'service/adl/getJobDashboardData',
			'service/job/getJobDates',
			'service/subcontract/getSubcontractDashboardData',
			'service/subcontract/getSubcontractDetailsDashboardData',
			'service/payment/getPaymentResourceDistribution'
			//'service/payment/getPaymentCertSummary'
			];
		bypassBlockUI.forEach(function(addr){
			if(config.url == addr) {
				message = 'false';
			}
		});
		
		if(message == 'false'){
			return false;
		}
			

		  switch(config.method) {
		    case 'GET':
		      message = 'Loading ...';
		      break;

		    case 'POST':
		      message = 'Loading ...';
		      break;

		    case 'DELETE':
		      message = 'Deleting ...';
		      break;

		    case 'PUT':
		      message = 'Putting ...';
		      break;
		  };

		  return message;

		};

});


/**
 * Event-Listner for Location change
 */
mainApp.run(function($rootScope, $location, $cookies){
	$rootScope.$on('$stateChangeStart',function(event,next) {
		if(next.url==='/job-select'){
			$rootScope.hideNavMenu = true;
		}else{
			//console.log("jobno: "+$cookies.get("jobNo"));
			if($cookies.get("jobNo") == null){
				$location.path('/job-select');
			}
			$rootScope.hideNavMenu = false;
		}
	});
	
	/*$rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams){ 
	    event.preventDefault();
	    window.history.forward();
	});*/

});
	
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
