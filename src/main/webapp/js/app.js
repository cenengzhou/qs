var mainApp = angular.module('app', ['ui.router', 'chart.js',  'ngTouch', 'ngAnimate', 'ui.bootstrap', 'ngCookies', 'oc.lazyLoad', 'moment-picker', 'angular.vertilize', 'blockUI', 'ngSanitize',
                                     'ui.grid', 'ui.grid.pagination', 'ui.grid.edit', 'ui.grid.selection', 'ui.grid.cellNav', 'ui.grid.autoResize', 'ui.grid.rowEdit', 'NgSwitchery',
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
		params: {
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
		controller: 'JobDatesCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/job/job-dates.js'
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
                           'js/service/subcontract-service.js'
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
                           'js/service/subcontract-service.js'
                    ] 
                });
            }]
        },
        controller: 'NavMenuCtrl'
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
               	         'js/service/resource-summary-service.js'
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
               	         'js/service/tender-service.js'
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
               	         'js/service/master-list-service.js',
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
               	         'js/service/tender-variance-service.js',
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
               	         'js/service/tender-variance-service.js',
               	         'js/service/tender-service.js',
               	         'js/service/master-list-service.js'
                    ] 
                });
            }]
        },
        controller: 'SubcontractAwardSummaryCtrl'
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
                           'js/service/subcontract-service.js'
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
		templateUrl: "view/subcontract/addendum/addendum-select.html",
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
		url: "/addendum/tab",
		templateUrl: "view/subcontract/addendum/addendum-tab.html",
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
	.state('subcontract.addendumDetails.title', {
		url: "/title",
		templateUrl: "view/subcontract/addendum/addendum-title.html",
	})
	.state('subcontract.addendumDetails.detail', {
		url: "/detail",
		templateUrl: "view/subcontract/addendum/addendum-detail.html",
	})
       
	.state('subcontract.addendumDetails.details', {
		url: "/details",
		templateUrl: "view/subcontract/addendum/addendum-details.html",
	})
       
	.state('subcontract.addendumDetails.attachment', {
		url: "/attachment",
		templateUrl: "view/subcontract/addendum/addendum-attachment.html",
	})
	.state('subcontract.addendumDetails.summary', {
		url: "/summary",
		templateUrl: "view/subcontract/addendum/addendum-summary.html",
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
                           'js/controller/subcontract/payment-select.js',
                           'js/service/payment-service.js',
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
        controller: 'NavMenuCtrl'
	})
	.state('subcontract.payment.certificate', {
		url: "/certificate",
		templateUrl: "view/subcontract/payment/payment-certificate.html",
		"params": {
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
	.state('subcontract.payment.attachment', {
		url: "/attachment",
		templateUrl: "view/subcontract/payment/payment-attachment.html",
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
                    ] 
                });
            }]
        },
        controller: 'PaymentInvoiceCtrl',
	})
	
	
	/*.state('subcontract.paymentdetails', {
		url: "/payment/details",
		templateUrl: "view/subcontract/payment/payment-details.html",
		"params": {
			"payment": null, 
			"paymentTerms": null
		},
		controller: 'PaymentDetailsCtrl',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/subcontract/payment-details.js',
                           'js/service/payment-service.js'
                    ] 
                });
            }]
        }
	})
	.state('subcontract.paymentInvoice', {
		url: "/payment/invoice",
		templateUrl: "view/subcontract/payment/payment-invoice.html",
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
	})*/
	
	
	
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
		params: {
			'repackagingEntryId': null
		},
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/repackaging/repackaging-update.js',
                           'js/controller/repackaging/repackaging-add.js',
                           'js/service/resource-summary-service.js'
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
		'params': {
			'jobNo': null,
			'jobDescription': null
		},
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [
                           'js/controller/transit/transit-dashboard.js',
                           'js/service/subcontract-service.js',
                           'js/service/transit-service.js',
                           'js/service/budgetposting-service.js'
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
               	         	'js/service/resource-summary-service.js'
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
               	 files: [	'js/controller/iv/iv-update.js',
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
	.state('admin', {
		url: '/admin',
		parent: 'navigation',
		templateUrl: 'view/admin/admin-menu.html',
		resolve: {
            service: ['$ocLazyLoad', function($ocLazyLoad) {//lazy
                return $ocLazyLoad.load({
               	 name: 'app',
               	 files: [  'js/service/subcontract-service.js',
               	           'js/service/main-cert-service.js',
               	           'js/service/payment-service.js',
               	           'js/service/transit-service.js',
               	           'js/service/quartz-service.js',
                           'js/controller/admin/admin-session.js',
                           'js/controller/admin/admin-ManualProcedures.js',
                           'js/controller/admin/admin-Revisions.js',
                           'js/controller/admin/admin-Revision-Subcontract.js',
                           'js/controller/admin/admin-Revision-Payment.js',
                           'js/controller/admin/admin-Revision-MainCert.js',
                           'js/controller/admin/admin-TransitUOMMaintenance.js',
                           'js/controller/admin/admin-TransitResourceCodeMaintenance.js',
                           'js/controller/admin/admin-SubcontractStandardTermsMaintenance.js',
                           'js/controller/admin/admin-SubcontractStandardTermsAddModal.js',
                           'js/controller/admin/admin-SchedulerMaintenance.js',
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
		'params': {
			'jobNo': null,
			'jobDescription': null
		},
		controller: "AdminSessionCtrl",
		controllerAs: 'sessionCtrl'
	})
	.state('admin.ManualProcedures',{
		url: '/ManualProcedures',
		templateUrl: 'view/admin/admin-ManualProcedures.html',
		'params': {
			'jobNo': null,
			'jobDescription': null
		},
		controller: 'AdminManualProceduresCtrl',
		controllerAs: 'manualProceduresCtrl'
	})
	.state('admin.Revisions',{
		url: '/Revisions',
		templateUrl: 'view/admin/admin-Revisions.html',
		'params': {
			'jobNo': null,
			'jobDescription': null
		},
		controller: 'AdminRevisionsCtrl',
		controllerAs: 'revisionsCtrl'
	})
	.state('admin.TransitUOMMaintenance',{
		url: '/TransitUOMMaintenance',
		templateUrl: 'view/admin/admin-TransitUOMMaintenance.html',
		'params': {
			'jobNo': null,
			'jobDescription': null
		},
		controller: 'AdminTransitUOMMaintenanceCtrl',
		controllerAs: 'transitUOMMaintenanceCtrl'
	})
	.state('admin.TransitResourceCodeMaintenance',{
		url: '/TransitResourceCodeMaintenance',
		templateUrl: 'view/admin/admin-TransitResourceCodeMaintenance.html',
		'params': {
			'jobNo': null,
			'jobDescription': null
		},
		controller: 'AdminTransitResourceCodeMaintenanceCtrl',
		controllerAs: 'resourceCodeMaintenanceCtrl'
	})
	.state('admin.SubcontractStandardTermsMaintenance',{
		url: '/SubcontractStandardTermsMaintenance',
		templateUrl: 'view/admin/admin-SubcontractStandardTermsMaintenance.html',
		'params': {
			'jobNo': null,
			'jobDescription': null
		},
		controller: 'AdminSubcontractStandardTermsMaintenanceCtrl',
		controllerAs: 'standardTermsMaintenanceCtrl'
	})
	.state('admin.SchedulerMaintenance',{
		url: '/SchedulerMaintenance',
		templateUrl: 'view/admin/admin-SchedulerMaintenance.html',
		'params': {
			'jobNo': null,
			'jobDescription': null
		},
		controller: 'AdminSchedulerMaintenanceCtrl',
		controllerAs: 'schedulerMaintenance'
	});

}])
.filter('jsonDate', ['$filter', function ($filter) {
	return function (input, format) {
		return (input) 
		? $filter('date')(input, format) : '';
	};
}]);

mainApp.config(['$httpProvider', function($httpProvider){
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
						//$window.location.href = 'login.htm?status=' + status;
					}else if (status === 403){
						$window.location.href = '403.html';
					}
					/*else if (status === 404){
						console.log("Status 404");
						$window.location.href = 'logout.html?=404';
					}*/
					deferred.reject(rejection);
					return deferred.promise;
				}
			};
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

mainApp.config(['momentPickerProvider', function(momentPickerProvider){
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
}]);

mainApp.config(function(blockUIConfig, colorCode) {

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
	
	blockUIConfig.template = '<div class="block-ui-message-container"><div class="block-ui-message" ng-if="!state.hideMessage">{{state.message}}</div></div><div class="block-ui block-ui-overlay" style="z-index:900"><i class="fa fa-connectdevelop fa-fw fa-pulse text-grey" style="width:100%; font-size: 1000px;opacity: 0.1" ng-if="!state.hideAnimate"></i></div>';
	
	// Disable automatically blocking of the user interface
	blockUIConfig.autoBlock = false;
	
	// Change the default delay to 100ms before the blocking is visible
	blockUIConfig.delay = 100;
	
	// Disable auto body block
	blockUIConfig.autoInjectBodyBlock = false;
	
	blockUIConfig.requestFilter = function(config) {

		  var message;

		  switch(config.method) {
		    case 'GET':
		      message = 'Getting ...';
		      break;

		    case 'POST':
		      message = 'Posting ...';
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
 * Check authentication and user role if location changed
 */
mainApp.run(['$rootScope', 'SessionHelper', '$window', '$document', '$location', function ($rootScope, SessionHelper, $window, $document, $location) {
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
