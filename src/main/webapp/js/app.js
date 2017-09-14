var mainApp = angular.module('app', ['ui.router', 'chart.js',  'ngAnimate', 'ui.bootstrap', 'ngCookies', 'oc.lazyLoad', 'moment-picker', 'angular.vertilize', 'blockUI', 'ngSanitize', 'ngMaterial', 'ngJoyRide',
                                     'ui.grid', 'ui.grid.pagination', 'ui.grid.edit', 'ui.grid.selection', 'ui.grid.cellNav', 'ui.grid.autoResize', 'ui.grid.rowEdit', 'NgSwitchery', 'ui.tinymce',
									 'ui.grid.resizeColumns', 'ui.grid.pinning', 'ui.grid.moveColumns', 'ui.grid.exporter', 'ui.grid.importer', 'ui.grid.grouping', 'ui.grid.validate', 'ui.grid.saveState', 'angular-js-xlsx',
									 'ng-currency']);  


// configure our routes    
mainApp.config(['$stateProvider', '$urlRouterProvider', '$httpProvider','GlobalParameter', 
        function($stateProvider, $urlRouterProvider, $httpProvider, GlobalParameter/*, modalStateProvider*/) {
	
	// For any unmatched url, redirect to /state1
	$urlRouterProvider.otherwise("/job-select");  

	
	$stateProvider
	.state('navigation', {
	    templateUrl: 'navigation-menu.html',
	    abstract: true,
	    resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/service/jde-service.js',
                           'js/service/adl-service.js',
                           'js/service/subcontractor-service.js',
                           'js/service/userpreference-service.js',
                           'js/service/job-service.js',
                           'js/service/system-service.js',
                           'js/service/ap-service.js',
                           'js/service/userpreference-service.js',
                           'js/service/transit-service.js',
                           'js/system/rootscope-service.js',
                           'js/controller/message-modal.js',
                           'js/controller/excelupload-modal.js',
                           'js/controller/infotips-modal.js',
                           'js/controller/forms-modal.js',
                           'js/service/attachment-service.js',
                           'js/controller/attachment/attachment-text-editor.js',
                           'js/controller/addressbook-details-modal.js',
                           'js/controller/navigation-menu.js'
                    ] 
                });
            }]
        },
		 controller: 'NavigationCtrl'
	  })
	 .state('profile',{
		 url: '/profile',
		 parent: 'navigation',
		 templateUrl: 'view/profile/profile.html',
		 resolve: {
			 service: ['$ocLazyLoad', function($ocLazyLoad){
				 return $ocLazyLoad.load({
					 name: 'app',
					 files: [
						 'js/controller/profile/profile-controller.js'
					 ]
				 })
			 }]
		 },
		 controller: 'ProfileCtrl'
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
		parent: "navigation",
		templateUrl: "view/job-select.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/job/job-select.js',
                           'js/service/job-service.js',
                           'js/service/transit-service.js',
                           'js/controller/transit/transit-header.js',
                           'js/controller/transit/transit-enquiry.js',
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
		controller: 'JobDashboardCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/job/job-dashboard.js',
                           'js/service/adl-service.js',
                           'js/service/resource-summary-service.js',
                           'js/service/repackaging-service.js'
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
	.state('job.accountMaster', {
		url: "/accountMaster",
		templateUrl: "view/job/job-account-master.html",
		controller: 'JobAccountMasterCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
               	           'js/service/jde-service.js',
                           'js/controller/job/job-account-master.js'
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
                           'js/service/subcontract-service.js',
                           'js/service/repackaging-service.js'
                    ] 
                });
            }]
        },
		controller: 'SubcontractSelectCtrl'
	})
	
	.state('subcontract-award', {
		url: "/subcontract-award/tab",
		parent: "navigation",
		templateUrl: "view/subcontract/subcontract-award-tab.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/service/subcontract-service.js',
                           'js/service/payment-service.js',
                           'js/controller/subcontract/subcontract-menu.js',
                           'js/service/addendum-service.js'
                    ] 
                });
            }]
        },
        controller: 'SubcontractMenuCtrl'
	})
	.state('subcontract-award.hearder', {
		url: "/header",
		templateUrl: "view/subcontract/subcontract-award-header.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/subcontract-create.js',
                           'js/service/job-service.js'
                    ] 
                });
            }]
        },
        controller: 'SubcontractCreateCtrl'
	})
	.state('subcontract-award.assign', {
		url: "/assign",
		templateUrl: "view/subcontract/subcontract-award-assign.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
               	         'js/controller/subcontract/subcontract-award-assign.js',
               	         'js/controller/repackaging/repackaging-split.js',
               	         'js/controller/repackaging/repackaging-add.js',
               	         'js/service/resource-summary-service.js',
               	         'js/service/jde-service.js',
               	         'js/service/system-service.js'
                    ] 
                });
            }]
        },
        controller: 'RepackagingAssignResourcesCtrl'
	})
	.state('subcontract-award.ta', {
		url: "/ta",
		templateUrl: "view/subcontract/subcontract-award-ta.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
               	         'js/controller/subcontract/subcontract-award-ta.js',
               	         'js/service/resource-summary-service.js',
               	         'js/service/tender-service.js',
               	         'js/service/jde-service.js'
                    ] 
                });
            }]
        },
        controller: 'SubcontractTACtrl'
	})
	.state('subcontract-award.vendor', {
		url: "/vendor",
		templateUrl: "view/subcontract/subcontract-award-vendor.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
               	         'js/controller/subcontract/subcontract-vendor.js',
               	         'js/controller/subcontract/subcontract-vendor-feedback.js',
               	         'js/service/jde-service.js',
               	         'js/service/tender-service.js'
                    ] 
                });
            }]
        },
        controller: 'SubcontractorCtrl'
	})
	.state('subcontract-award.variance', {
		url: "/variance",
		templateUrl: "view/subcontract/subcontract-award-variance.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
               	         'js/controller/subcontract/subcontract-award-variance.js',
               	         'js/service/tender-service.js'
                    ] 
                });
            }]
        },
        controller: 'TenderVarianceCtrl'
	})
	.state('subcontract-award.dates', {
		url: "/dates",
		templateUrl: "view/subcontract/subcontract-award-dates.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
               	         'js/controller/subcontract/subcontract-dates.js'
                    ] 
                });
            }]
        },
        controller: 'SubcontractDatesCtrl'
	})
	.state('subcontract-award.summary', {
		url: "/summary",
		templateUrl: "view/subcontract/subcontract-award-summary.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
               	         'js/controller/subcontract/subcontract-award-summary.js',
               	         'js/service/tender-service.js',
               	         'js/service/jde-service.js',
               	         'js/service/html-service.js'
                    ] 
                });
            }]
        },
        controller: 'SubcontractAwardSummaryCtrl'
	})
	.state('subcontract-award.attachment', {
		url: "/attachment",
		templateUrl: "view/attachment/attachment-main.html",
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
                           'js/controller/attachment/attachment-main.js',
                           'js/controller/attachment/attachment-text-editor.js',
                           'js/service/attachment-service.js',
                           'js/service/main-cert-service.js',
                    ] 
                });
            }]
        }
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
                           'js/service/subcontract-service.js',
                           'js/service/payment-service.js',
                           'js/controller/subcontract/subcontract-menu.js',
                           'js/service/addendum-service.js'
                    ] 
                });
            }]
        },
        controller: 'SubcontractMenuCtrl'
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
                           'js/controller/subcontract/subcontract-details.js',
                           'js/controller/subcontract/subcontract-retention-details.js'
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
	
	.state('subcontract.attachment', {
		templateUrl: "view/subcontract/subcontract-attachment.html",
	})
	.state('subcontract.attachment.first', {
		url: "/attachment",
		templateUrl: "view/attachment/attachment-main.html",
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
                           'js/controller/attachment/attachment-main.js',
                           'js/controller/attachment/attachment-text-editor.js',
                           'js/service/attachment-service.js',
                           'js/service/main-cert-service.js',
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
                           'js/controller/subcontract/subcontract-workdone.js',
                           'js/service/resource-summary-service.js'
                    ] 
                });
            }]
        }
	})
	.state('subcontract.addendum-select', {
		url: "/addendum-select",
		templateUrl: "view/subcontract/addendum/addendum-select.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/addendum/addendum-select.js',
                           'js/service/addendum-service.js'
                    ] 
                });
            }]
        },
        controller: 'AddendumSelectCtrl'
	})

	.state('subcontract.addendum', {
		url: "/addendum/tab",
		templateUrl: "view/subcontract/addendum/addendum-tab.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/service/addendum-service.js' 
                    ] 
                });
            }]
        }
	})
	.state('subcontract.addendum.title', {
		url: "/title",
		templateUrl: "view/subcontract/addendum/addendum-title.html",
		params: {
			"addendumNo": null 
		},
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/addendum/addendum-title.js',
                    ] 
                });
            }]
        },
        controller: 'AddendumTitleCtrl'
		
	})
	.state('subcontract.addendum.details', {
		url: "/details",
		templateUrl: "view/subcontract/addendum/addendum-details.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
               	         	'js/service/jde-service.js',
               	         	'js/service/resource-summary-service.js',
               	         	'js/service/repackaging-service.js',
               	         	'js/controller/subcontract/addendum/addendum-details.js',
               	         	'js/controller/subcontract/addendum/addendum-detail-add.js',
               	         	'js/controller/subcontract/addendum/addendum-detail-add-v3.js',
               	         'js/controller/subcontract/addendum/addendum-detail-update.js',
                    ] 
                });
            }]
        },
		controller: 'AddendumDetailsCtrl'
	})
       
	.state('subcontract.addendum.detail-list', {
		url: "/detail-list",
		templateUrl: "view/subcontract/addendum/addendum-detail-list.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/addendum/addendum-detail-list.js',
                    ] 
                });
            }]
        },
		controller: 'AddendumDetailListCtrl'
	})
       
	.state('subcontract.addendum.attachment', {
		url: "/attachment",
		templateUrl: "view/attachment/attachment-main.html",
		params: {
			'nameObject': GlobalParameter['AbstractAttachment'].AddendumNameObject,
			'offsetTop':440
		},
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                     'js/controller/attachment/attachment-main.js',
                     'js/controller/attachment/attachment-text-editor.js',
                         'js/service/attachment-service.js',
                         'js/service/addendum-service.js',
                         'js/service/main-cert-service.js',
                    ] 
                });
            }]
        },
		controller: 'AttachmentMainCtrl'
	})
	.state('subcontract.addendum.summary', {
		url: "/summary",
		templateUrl: "view/subcontract/addendum/addendum-summary.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/addendum/addendum-summary.js',
                    ] 
                });
            }]
        },
		controller: 'AddendumSummaryCtrl'
	})
	.state('subcontract.addendum.form2', {
		url: "/form2",
		templateUrl: "view/subcontract/addendum/addendum-form2.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/addendum/addendum-form2.js',
                           'js/service/html-service.js'
                    ] 
                });
            }]
        },
		controller: 'AddendumForm2Ctrl'
	})
	
	
	.state('subcontract.otherAddendum', {
		url: "/otherAddendum/tab",
		templateUrl: "view/subcontract/addendum/other-addendum-tab.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/service/addendum-service.js' 
                    ] 
                });
            }]
        }
	})
	.state('subcontract.otherAddendum.detail', {
		url: "/detail",
		templateUrl: "view/subcontract/addendum/other-addendum-detail.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/addendum/other-addendum-detail.js',
                           'js/service/jde-service.js'
                    ] 
                });
            }]
        },
        controller: 'OtherAddendumDetailCtrl'
	})
	.state('subcontract.otherAddendum.list', {
		url: "/list",
		templateUrl: "view/subcontract/addendum/other-addendum-list.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/addendum/other-addendum-list.js',
                    ] 
                });
            }]
        },
        controller: 'OtherAddendumListCtrl'
	})
	
	.state('subcontract.payment-select', {
		url: "/payment-select",
		templateUrl: "view/subcontract/payment/payment-select.html",
		controller: 'PaymentCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/payment/payment-select.js',
                           'js/service/payment-service.js',
                           'js/controller/enquiry/modal/enquiry-supplierledgerdetails.js',
                           'js/service/jde-service.js',
                    ] 
                });
            }]
        }
	})
	.state('subcontract.payment', {
		url: "/payment/tab",
		templateUrl: "view/subcontract/payment/payment-tab.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/service/payment-service.js'
                    ] 
                });
            }]
        },
//        controller: 'NavMenuCtrl'
	})
	.state('subcontract.payment.certificate', {
		url: "/certificate",
		templateUrl: "view/subcontract/payment/payment-certificate.html",
		params: {
			"paymentCertNo": null, 
			"paymentTermsDesc": null
		},
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/payment/payment-certificate.js',
                           'js/service/main-cert-service.js'
                    ] 
                });
            }]
        },
        controller: 'PaymentCertCtrl'
	})
	.state('subcontract.payment.details', {
		url: "/details",
		templateUrl: "view/subcontract/payment/payment-details.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/payment/payment-details.js',
                    ] 
                });
            }]
        },
        controller: 'PaymentDetailsCtrl',
	})
	.state('subcontract.payment.summary', {
		url: "/summary",
		templateUrl: "view/subcontract/payment/payment-summary.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
               	      'js/controller/subcontract/payment/payment-summary.js',
                         ] 
                });
            }]
        },
		controller: 'PaymentSummaryCtrl'
	})
	.state('subcontract.payment.attachment', {
		url: "/attachment",
		templateUrl: "view/attachment/attachment-main.html",
		params: {
			'nameObject': GlobalParameter['AbstractAttachment'].SCPaymentNameObject,
			'offsetTop':440
		},
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                         'js/controller/attachment/attachment-main.js',
                         'js/controller/attachment/attachment-text-editor.js',
                         'js/service/attachment-service.js',
                         'js/service/main-cert-service.js',
                         ] 
                });
            }]
        },
		controller: 'AttachmentMainCtrl'
	})
	.state('subcontract.payment.invoice', {
		url: "/invoice",
		templateUrl: "view/subcontract/payment/payment-invoice.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/payment/payment-invoice.js',
                           'js/service/html-service.js'
                    ] 
                });
            }]
        },
        controller: 'PaymentInvoiceCtrl',
	})
	
	
	.state('subcontract.split', {
		url: "/split",
		templateUrl: "view/subcontract/subcontract-split-terminate.html",
		params: {
			'action': 'Split',
			'nameObject': GlobalParameter['AbstractAttachment'].SplitNameObject,
		},
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                         'js/controller/subcontract/subcontract-split-terminate.js',
	                     'js/controller/attachment/attachment-main.js',
	                     'js/controller/attachment/attachment-text-editor.js',
	                     'js/service/attachment-service.js',
                         'js/service/addendum-service.js',
                         'js/service/subcontract-service.js',
                         'js/service/main-cert-service.js'
                    ] 
                });
            }]
        },
        controller: 'SubcontractSplitTerminateCtrl'
	})
	
	.state('subcontract.terminate', {
		url: "/terminate",
		templateUrl: "view/subcontract/subcontract-split-terminate.html",
		params: {
			'action': 'Terminate',
			'nameObject': GlobalParameter['AbstractAttachment'].TerminateNameObject,
		},
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/subcontract-split-terminate.js',
                           'js/controller/attachment/attachment-main.js',
  	                       'js/controller/attachment/attachment-text-editor.js',
                           'js/service/attachment-service.js',
                           'js/service/addendum-service.js',
                           'js/service/subcontract-service.js',
                           'js/service/main-cert-service.js'
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
		templateUrl: "view/main-cert/cert-dashboard.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/main-cert/cert-dashboard.js',
                           'js/controller/main-cert/retention-release-schedule.js',
                           'js/service/main-cert-service.js',
                           'js/service/job-service.js'
                    ] 
                });
            }]
        },
		controller: 'CertCtrl'
	})
	.state('main-cert-select', {
		url: "/main-cert-select",
		parent: "navigation",
		templateUrl: "view/main-cert/main-cert-select.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/main-cert/main-cert-select.js',
                           'js/service/main-cert-service.js',
                           'js/controller/enquiry/modal/enquiry-customerledgerdetails.js',
                           'js/service/jde-service.js',
                    ] 
                });
            }]
        },
		controller: 'MainCertListCtrl'
	})
	
	.state('mainCert', {
		url: "/mainCert",
		parent: "navigation",
		templateUrl: "view/main-cert/main-cert-tab.html",
		params: {
			'mainCertNo': null
		},
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [  'js/service/main-cert-service.js',
               		 	   'js/controller/main-cert/main-cert.js',
               		 	   'js/controller/main-cert/main-cert-ipa.js',
               		 	   'js/controller/main-cert/main-cert-ipc.js',
                           'js/controller/main-cert/retention-release-schedule.js',
                           'js/controller/main-cert/contra-charge-modal.js',
                           'js/controller/attachment/attachment-main.js',
                           'js/controller/attachment/attachment-text-editor.js',
                           'js/service/job-service.js',
                           'js/service/main-cert-service.js',
                           'js/service/attachment-service.js',
                           'js/service/subcontract-service.js',
                           'js/service/payment-service.js',
                           'js/service/addendum-service.js'
                    ] 
                });
            }]
        },
		controller: 'MainCertCtrl'
	})
	.state('mainCert.ipa', {
		url: "/ipa",
		templateUrl: "view/main-cert/main-cert-ipa.html",
		params: {
			'mainCertNo': null
		},
		controller: 'IPACtrl'
	})
	.state('mainCert.sendIPA', {
		url: "/sendIPA",
		templateUrl: "view/main-cert/main-cert-send-ipa.html",
		params: {
			'mainCertNo': null
		},
		controller: 'IPACtrl'
	})
	.state('mainCert.ipc', {
		url: "/ipc",
		templateUrl: "view/main-cert/main-cert-ipc.html",
		controller: 'IPCCtrl'
	})
	.state('mainCert.rr', {
		url: "/rr",
		templateUrl: "view/main-cert/retention-release-schedule.html",
		controller: 'RetentionReleaseScheduleCtrl'
	})
	.state('mainCert.confirmIPC', {
		url: "/confirmIPC",
		templateUrl: "view/main-cert/main-cert-confirm-reset-ipc.html",
		controller: 'IPCCtrl'
	})
	.state('mainCert.attachment', {
		url: "/attachment",
		templateUrl: "view/attachment/attachment-main.html",
		params: {
			'nameObject': GlobalParameter['AbstractAttachment'].MainCertNameObject,
			'offsetTop':440,
			'mainCertStatus':null
		},
		controller: 'AttachmentMainCtrl'
	})
	.state('mainCert.postIPC', {
		url: "/postIPC",
		templateUrl: "view/main-cert/main-cert-post-ipc.html",
		controller: 'IPCCtrl'
	})
	
	
	//Repackaging
	.state('repackaging', {
		url: "/repackaging",
		parent: "navigation",
		templateUrl: "view/repackaging/repackaging.html",
		params: {
			'version': null
		},
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
               	         'js/service/repackaging-service.js',
               	         'js/service/resource-summary-service.js',
               	         'js/controller/repackaging/repackaging.js',
               	         'js/controller/repackaging/repackaging-history.js',
               	         'js/controller/repackaging/repackaging-confirm.js',


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
				templateUrl: "view/attachment/attachment-main.html",
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
              	        'js/service/attachment-service.js',
                        'js/controller/attachment/attachment-main.js',
                        'js/controller/attachment/attachment-text-editor.js',
                        'js/service/subcontract-service.js',
                        'js/service/payment-service.js',
                        'js/service/addendum-service.js'
					]
				})
			}]
		}
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
                           'js/controller/repackaging/repackaging-add.js',
                           'js/controller/repackaging/repackaging-split.js',
                           'js/service/resource-summary-service.js',
                           'js/service/system-service.js',
                           'js/service/jde-service.js',
                           'js/service/subcontract-service.js',
                    ] 
                });
            }]
        },
		controller: 'RepackagingUpdateCtrl'
	})
	.state('repackaging-email', {
		url: "/repackaging-email",
		parent: "navigation",
		templateUrl: "view/repackaging/repackaging-email.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/repackaging/repackaging-email.js',
							'js/service/repackaging-service.js'
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
		templateUrl: "view/transit/transit-tab.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
               		 	'js/controller/transit/modal/CodeMatchEnquiryModal.js',
               		 	'js/controller/transit/transit.js',
	               		'js/controller/transit/transit-import.js',
	               		'js/controller/transit/transit-confirm.js',
	               		'js/controller/transit/transit-complete.js',
	               		//'js/controller/admin/admin-TransitUOMMaintenance.js',
                        //'js/controller/admin/admin-TransitResourceCodeMaintenance.js',
                        'js/service/resource-summary-service.js',
	               		'js/service/subcontract-service.js',
	               		'js/service/transit-service.js',
	               		'js/service/jde-service.js'
                    ] 
                });
            }]
        },
        controller: 'TransitCtrl'
	})
	.state('transit.userGuide', {
		url: "/userGuide",
		templateUrl: "view/transit/transit-user-guide.html",
        controller: 'TransitCtrl'
	})
	.state('transit.bq', {
		url: "/BQ",
		templateUrl: "view/transit/transit-bq.html",
        controller: 'TransitImportCtrl'
	})
	.state('transit.resources', {
		url: "/resources",
		templateUrl: "view/transit/transit-resources.html",
        controller: 'TransitImportCtrl'
	})
	.state('transit.confirm', {
		url: "/confirm",
		templateUrl: "view/transit/transit-confirm.html",
        controller: 'TransitConfirmCtrl'
	})
	.state('transit.report', {
		url: "/report",
		templateUrl: "view/transit/transit-report.html",
        controller: 'TransitCompleteCtrl'
	})
	.state('transit.attachment', {
		url: "/attachment",
		templateUrl: "view/attachment/attachment-main.html",
		params: {
			'nameObject': GlobalParameter['AbstractAttachment'].TransitNameObject,
			'offsetTop':440
		},
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	files: [
                    'js/controller/attachment/attachment-main.js',
                    'js/controller/attachment/attachment-text-editor.js',
                    'js/service/attachment-service.js',
                    'js/service/addendum-service.js',
                    'js/service/payment-service.js',
                    'js/service/main-cert-service.js',
               ] 
                });
            }]
        },
		controller: 'AttachmentMainCtrl'
	})
	.state('transit.complete', {
		url: "/complete",
		templateUrl: "view/transit/transit-complete.html",
        controller: 'TransitCompleteCtrl'
	})
	
	.state("iv", {
		url: "/iv",
		parent: "navigation",
		templateUrl: "view/iv/iv-tab.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [	
               	         	'js/service/resource-summary-service.js',
               	         	'js/service/subcontract-service.js',
               	         	'js/service/repackaging-service.js'
                    ] 
                });
            }]
        },
		controller: "NavMenuCtrl",
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
	.state("iv.final", {
		url: "/final",
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
		templateUrl: "view/enquiry/enquiry-menu.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [	
							'js/controller/enquiry/enquiry-jobinfo.js',
							'js/controller/enquiry/enquiry-jobcost.js',
							'js/controller/enquiry/enquiry-jobcost-adl.js',
							'js/controller/enquiry/enquiry-jobcost-jde.js',
							'js/controller/enquiry/enquiry-ivhistory.js',
							'js/controller/enquiry/enquiry-main-cert.js',
							
							'js/controller/enquiry/enquiry-subcontract.js',
							'js/controller/enquiry/enquiry-subcontractdetail.js',
							'js/controller/enquiry/enquiry-payment.js',
							'js/controller/enquiry/enquiry-provisionhistory.js',
							'js/controller/enquiry/enquiry-workscope.js',
							
							'js/controller/enquiry/enquiry-subcontractor.js',
							'js/controller/enquiry/enquiry-client.js',
							
							'js/controller/enquiry/enquiry-purchaseorder.js',
							'js/controller/enquiry/enquiry-purchaseorderdetail.js',
							
							'js/controller/enquiry/enquiry-accountledger.js',
							'js/controller/enquiry/enquiry-customerledger.js',
							'js/controller/enquiry/enquiry-supplierledger.js',
							'js/controller/enquiry/enquiry-performanceappraisal.js',
							'js/controller/enquiry/modal/enquiry-jobcostdetails-adl.js',
							'js/controller/enquiry/modal/enquiry-customerledgerdetails.js',
							'js/controller/enquiry/modal/enquiry-supplierledgerdetails.js',

               	         	'js/service/main-cert-service.js',
               	         	'js/service/payment-service.js',
               	         	'js/service/subcontract-service.js',
               	         	'js/service/adl-service.js',
               	         	'js/service/system-service.js',
               	         	'js/service/subcontractor-service.js',
               	         	'js/service/resource-summary-service.js',
               	         	'js/service/job-service.js',
               	         	'js/controller/nav-menu.js',
               	         	'js/service/jde-service.js',
               	         	'js/service/main-cert-service.js'
                    ] 
                });
            }]
        },
        controller: 'NavMenuCtrl',
        controllerAs: 'navMenuCtrl'
	})
	.state('enquiry.info', {
		url: '/info',
		templateUrl: 'view/enquiry/enquiry-info.html',
		params: {
			'jobNo': null,
			'jobDescription': null,
		}
	})
	.state('enquiry.jobInfo', {
		url: '/jobInfo',
		templateUrl: 'view/enquiry/enquiry-jobinfo.html',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquiryJobInfoCtrl'
		})
	.state('enquiry.jobCost', {
		url: '/jobCost',
		templateUrl: 'view/enquiry/enquiry-jobcost.html',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquiryJobCostCtrl'
		})
	.state('enquiry.jobCost.adl', {
		url: '/adl',
		templateUrl: 'view/enquiry/enquiry-jobcost-adl.html',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquiryJobCostAdlCtrl'
		})
	.state('enquiry.jobCost.jde', {
		url: '/jde',
		templateUrl: 'view/enquiry/enquiry-jobcost-jde.html',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquiryJobCostJdeCtrl'
		})	
	.state('enquiry.ivHistory', {
		url: '/ivHistory',
		templateUrl: 'view/enquiry/enquiry-ivhistory.html',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquiryIvHistoryCtrl'
	})
	.state('enquiry.subcontract', {
		url: '/subcontract',
		templateUrl: 'view/enquiry/enquiry-subcontract.html',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquirySubcontractCtrl'
		})
	.state('enquiry.subcontractDetail', {
		url: '/subcontractDetail',
		templateUrl: 'view/enquiry/enquiry-subcontractdetail.html',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquirySubcontractDetailCtrl'
		})
	.state('enquiry.payment', {
		url: '/payment',
		templateUrl: 'view/enquiry/enquiry-payment.html',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquiryPaymentCtrl'
		})
	.state('enquiry.provisionHistory', {
		url: '/provisionHistory',
		templateUrl: 'view/enquiry/enquiry-provisionhistory.html',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquiryProvisionHistoryCtrl'
		})
	.state('enquiry.workScope', {
		url: '/workScope',
		templateUrl: 'view/enquiry/enquiry-workscope.html',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquiryWorkScopeCtrl'
		})
	.state('enquiry.subcontractor', {
		url: '/subcontractor',
		templateUrl: 'view/enquiry/enquiry-subcontractor.html',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquirySubcontractorCtrl'
		})
	.state('enquiry.client', {
		url: '/client',
		templateUrl: 'view/enquiry/enquiry-client.html',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquiryClientCtrl'
		})
	.state('enquiry.purchaseOrder', {
		url: '/purchaseOrder',
		templateUrl: 'view/enquiry/enquiry-purchaseorder.html',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquiryPurchaseOrderCtrl'
		})
	.state('enquiry.purchaseOrderDetail', {
		url: '/purchaseOrderDetail',
		templateUrl: 'view/enquiry/enquiry-purchaseorderdetail.html',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquiryPurchaseOrderDetailCtrl'
		})
	.state('enquiry.accountLedger', {
		url: '/accountLedger',
		templateUrl: 'view/enquiry/enquiry-accountledger.html',
		params: {
			'jobNo': null,
			'jobDescription' : null,
			'searchObject' : null,
		},
		controller: 'EnquiryAccountLedgerCtrl'
		})
	.state('enquiry.customerLedger', {
		url: '/customerLedger',
		templateUrl: 'view/enquiry/enquiry-customerledger.html',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquiryCustomerLedgerCtrl'
		})
	.state('enquiry.supplierLedger', {
		url: '/supplierLedger',
		templateUrl: 'view/enquiry/enquiry-supplierledger.html',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquirySupplierLedgerCtrl'
		})
	.state('enquiry.performanceAppraisal', {
		url: '/performanceAppraisal',
		templateUrl: 'view/enquiry/enquiry-performanceappraisal.html',
		params: {
			'jobNo': null,
			'jobDescription' : null,
		},
		controller: 'EnquiryPerformanceAppraisalCtrl'
		})
	.state('enquiry.main-cert', {
		url: '/main-cert',
		templateUrl: 'view/enquiry/enquiry-main-cert.html',
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
		templateUrl: "view/reports/reports-main.html",
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [	
							'js/controller/reports/reports-main.js',
							'js/service/job-service.js',
							'js/service/adl-service.js',
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
		templateUrl: 'view/admin/admin-menu.html',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [  'js/service/job-service.js',  
               		 	   'js/service/subcontract-service.js',
               	           'js/service/main-cert-service.js',
               	           'js/service/payment-service.js',
               	           'js/service/transit-service.js',
               	           'js/service/addendum-service.js',
               	           'js/service/resource-summary-service.js',
               	           'js/service/system-service.js',
               	           'js/service/repackaging-service.js',
                           'js/controller/admin/admin-session.js',
                           'js/controller/admin/admin-ManualProcedures.js',
                           'js/controller/admin/admin-Revisions.js',
                           'js/controller/admin/admin-Revisions-Subcontract.js',
                           'js/controller/admin/admin-Revisions-Payment.js',
                           'js/controller/admin/admin-Revisions-Addendum.js',
                           'js/controller/admin/admin-Revisions-MainCert.js',
                           'js/controller/admin/admin-Revisions-Repackaging.js',
                           'js/controller/admin/admin-Revisions-Approval.js',
                           'js/controller/admin/admin-Revisions-Attachment.js',
                           'js/controller/admin/admin-TransitUOMMaintenance.js',
                           'js/controller/admin/admin-TransitResourceCodeMaintenance.js',
                           'js/controller/admin/admin-SubcontractStandardTermsMaintenance.js',
                           'js/controller/admin/admin-SubcontractStandardTermsAddModal.js',
                           'js/controller/admin/admin-SchedulerMaintenance.js',
                           'js/controller/admin/admin-SystemInformation.js',
                           'js/controller/admin/admin-AnnouncementSetting.js',
                           'view/admin/admin-Revisions-Subcontract.html',
                           'view/admin/admin-Revisions-Payment.html',
                           'view/admin/admin-Revisions-Addendum.html',
                           'view/admin/admin-Revisions-MainCert.html'
                    ] 
                });
            }]
        },
        controller: 'NavMenuCtrl',
        controllerAs: 'navMenuCtrl'
	})
	.state('admin.session',{
		url: '/session',
		templateUrl: 'view/admin/admin-session.html',
		controller: "AdminSessionCtrl",
		controllerAs: 'sessionCtrl'
	})
	.state('admin.ManualProcedures',{
		url: '/ManualProcedures',
		templateUrl: 'view/admin/admin-ManualProcedures.html',
		controller: 'AdminManualProceduresCtrl',
		controllerAs: 'manualProceduresCtrl'
	})
	.state('admin.Revisions',{
		url: '/Revisions',
		templateUrl: 'view/admin/admin-Revisions.html',
		params: {
			'activeTab': null
		},
		controller: 'AdminRevisionsCtrl',
		controllerAs: 'revisionsCtrl'
	})
	.state('admin.Revisions.Subcontract',{
		url: '/Subcontract',
		templateUrl: 'view/admin/admin-Revisions-Subcontract.html',
		controller: 'AdminRevisionsSubcontractCtrl'
	})
	.state('admin.Revisions.Payment',{
		url: '/Payment',
		templateUrl: 'view/admin/admin-Revisions-Payment.html',
		controller: 'AdminRevisionsPaymentCtrl'
	})
	.state('admin.Revisions.Addendum',{
		url: '/Addendum',
		templateUrl: 'view/admin/admin-Revisions-Addendum.html',
		controller: 'AdminRevisionsAddendumCtrl'
	})
	.state('admin.Revisions.MainCert',{
		url: '/MainCert',
		templateUrl: 'view/admin/admin-Revisions-MainCert.html',
		controller: 'AdminRevisionsMainCertCtrl'
	})
	.state('admin.Revisions.Repackaging',{
		url: '/Repackaging',
		templateUrl: 'view/admin/admin-Revisions-Repackaging.html',
		controller: 'AdminRevisionsRepackagingCtrl'
	})
	.state('admin.Revisions.Approval',{
		url: '/Approval',
		templateUrl: 'view/admin/admin-Revisions-Approval.html',
		controller: 'AdminRevisionsApprovalCtrl'
	})
	.state('admin.Revisions.Attachment',{
		url: '/Attachment',
		templateUrl: 'view/admin/admin-Revisions-Attachment.html',
		controller: 'AdminRevisionsAttachmentCtrl'
	})
	.state('admin.TransitUOMMaintenance',{
		url: '/TransitUOMMaintenance',
		templateUrl: 'view/admin/admin-TransitUOMMaintenance.html',
		controller: 'AdminTransitUOMMaintenanceCtrl',
		controllerAs: 'transitUOMMaintenanceCtrl'
	})
	.state('admin.TransitResourceCodeMaintenance',{
		url: '/TransitResourceCodeMaintenance',
		templateUrl: 'view/admin/admin-TransitResourceCodeMaintenance.html',
		controller: 'AdminTransitResourceCodeMaintenanceCtrl',
		controllerAs: 'resourceCodeMaintenanceCtrl'
	})
	.state('admin.SubcontractStandardTermsMaintenance',{
		url: '/SubcontractStandardTermsMaintenance',
		templateUrl: 'view/admin/admin-SubcontractStandardTermsMaintenance.html',
		controller: 'AdminSubcontractStandardTermsMaintenanceCtrl',
		controllerAs: 'standardTermsMaintenanceCtrl'
	})
	.state('admin.SchedulerMaintenance',{
		url: '/SchedulerMaintenance',
		templateUrl: 'view/admin/admin-SchedulerMaintenance.html',
		controller: 'AdminSchedulerMaintenanceCtrl',
		controllerAs: 'schedulerMaintenance'
	})
	.state('admin.SystemInformation',{
		url: '/SystemInformation',
		templateUrl: 'view/admin/admin-SystemInformation.html'
	})
	.state('admin.HealthCheck',{
		url: '/HealthCheck',
		templateUrl: 'view/admin/admin-HealthCheck.html'
	})
	.state('admin.announcementSetting',{
		url: '/AnnouncementSetting',
		templateUrl: 'view/admin/admin-announcementSetting.html',
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

		  var message;

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
