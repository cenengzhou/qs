mainApp.controller('EnquiryInfoCtrl', [ '$scope', '$rootScope', '$http', 'modalService', '$sce',
		function($scope, $rootScope, $http, modalService, $sce) {

			$scope.jobBtn = {
					'title': 'JOB',
					'name': 'jobBtn',
					'show': false,
					'description': $sce.trustAsHtml('<b>The report contain all job related information for enquiry and export to csv.</b><br>\
							Report include: Job Information, Job Cost, Internal Valuation  History'),
					'items':[
					         {
					        	 'title': 'Job Information',
					        	 'description': 'The report contain all job related information for enquiry',
					        	 'href': '#/enquiry/jobInfo'
					         },
					         {
					        	 'title': 'Job Cost',
					        	 'description': 'The report contain job cost related information for enquiry',
					        	 'href': '#/enquiry/jobCost'
					         },
					         {
					        	 'title': 'Internal Valuation History',
					        	 'description': 'The report contain internal valuation history related information for enquiry',
					        	 'href': '#/enquiry/ivHistory'
					         }
					         ]
			};
			$scope.subcontractBtn = {
					'title': 'SUBCONTRACT',
					'name': 'subcontractBtn',
					'show': false,
					'description': '<b>The report contain all Subcontract related information for enquiry and export to csv.</b><br>\
							Report include: Subcontract, Subcontract Detail, Payment Information, Provision History and Work Scope',
					'items':[
					         {
					        	 'title': 'Subcontract',
					        	 'description': 'The report contain subcontract related information for enquiry',
					        	 'href': '#/enquiry/subcontract'
					         },
					         {
					        	 'title': 'Subcontract Detail',
					        	 'description': 'The report contain subcontract detail information for enquiry',
					        	 'href': '#/enquiry/subcontractDetail'
					         },
					         {
					        	 'title': 'Payment',
					        	 'description': 'The report contain payment information for enquiry',
					        	 'href': '#/enquiry/payment'
					         },
					         {
					        	 'title': 'Provision History',
					        	 'description': 'The report contain provision history for enquiry',
					        	 'href': '#/enquiry/provisionHistory'
					         },
					         {
					        	 'title': 'Work Scope',
					        	 'description': 'The report contain work scope for enquiry',
					        	 'href': '#/enquiry/workScope'
					         }
					         ]
			};
			$scope.addressBookBtn = {
					'title': 'ADDRESS BOOK',
					'name': 'addressBookBtn',
					'show': false,
					'description': '<b>The report contain all Address Book related information for enquiry and export to csv.</b><br>\
							Report include: Subcontractor, Client',
					'items': [
						         {
						        	 'title': 'Subcontractor',
						        	 'description': 'The report contain all sub-contractor related information for enquiry',
						        	 'href': '#/enquiry/subcontractor'
						         },
						         {
						        	 'title': 'Client',
						        	 'description': 'The report contain all client related information for enquiry',
						        	 'href': '#/enquiry/client'
						         }
					         ]
			};
			$scope.purchaseOrderBtn = {
					'title': 'PURCHASE ORDER',
					'name': 'purchaseOrderBtn',
					'show': false,
					'description': '<b>The report contain all job related information for enquiry and export to csv.</b><br>\
							Report include: Purchase Order (Under Construction), Purchase Order Detail (Under Construction)',
					'items': [
						         {
						        	 'title': 'Purchase Order',
						        	 'description': 'The report contain all PO related information for enquiry',
						        	 'href': '#/enquiry/purchaseOrder'
						         },
						         {
						        	 'title': 'Purchase Order Detail (Under Construction)',
						        	 'description': 'The report contain all PO details information for enquiry',
						        	 'href': '#/enquiry/purchaseOrderDetail'
						         }
					         ]
			};
			$scope.jdeBtn = {
					'title': 'JDE',
					'name': 'jdeBtn',
					'show': false,
					'description': '<b>The report contain all JDE related information for enquiry and export to csv.</b><br>\
						Report include: Account Ledger, Customer Ledger (Under Construction), Supplier Ledger (Under Construction), Performance Appralisal (Under Construction)',
					'items':[
					         {
					        	 'title': 'Account Ledger',
					        	 'description': 'The report contain account ledger related information for enquiry',
					        	 'href': '#/enquiry/accountLedger'
					         },
					         {
					        	 'title': 'Customer Ledger',
					        	 'description': 'The report contain customer ledger related information for enquiry',
					        	 'href': '#/enquiry/customerLedger'
					         },
					         {
					        	 'title': 'Supplier Ledger',
					        	 'description': 'The report contain supplier ledger related information for enquiry',
					        	 'href': '#/enquiry/supplierLedger'
					         },
					         {
					        	 'title': 'Performance Appraisal',
					        	 'description': 'The report contain internal valuation history for enquiry',
					        	 'href': '#/enquiry/performanceAppraisal'
					         }
					         ]
			};

			$scope.btns = {
				'jobBtn' : $scope.jobBtn,
				'subcontractBtn' : $scope.subcontractBtn,
				'addressBookBtn' : $scope.addressBookBtn,
				'purchaseOrderBtn' : $scope.purchaseOrderBtn,
				'jdeBtn' : $scope.jdeBtn
			};

			$scope.popoverMenuHeader = '<div class="box box-default" style="width:350px">';
			$scope.popoverMenuFooter = '</div>';
			$scope.popoverItemHeader = '<table class="table table-hover" style="width:350px;cursor:pointer;margin-bottom:0px !important">\
											<tr><td>\
												<a href="@href@"><table class="col-md-12">\
											<tr>';
			$scope.popoverItemColumn = [
			                            '<td style="width: 70px; height:70px;vertical-align:top">\
											<table class="table text-primary" style="margin-bottom:0px !important">\
												<tr>\
													<td style="width: 70px; height:70px;vertical-align: middle;text-align: center; border: 3px solid #1e77c5 !important;">\
														 <i class="fa fa-search fa-3x"></i>\
													</td>\
												</tr>\
											</table>\
										</td>',
			                            '<td style="vertical-align: top;padding:0px 0px 10px 10px;color:black">\
											<b>@title@</b><br>@description@\
										</td>'
			                            ];
			$scope.popoverItemFooter = '				</tr>\
													</table>\
												</a></td>\
											</tr>\
										</table>';

			$scope.genMenuHtml = function(m){
				var menu = $scope.popoverMenuHeader;
				var iconRight = false;
				angular.forEach($scope.btns[m].items, function(item){
					menu += $scope.popoverItemHeader.replace('@href@', item.href);
					menu += $scope.getColumn(item, iconRight);
					iconRight = !iconRight;
					menu += $scope.popoverItemFooter;
				});
				menu += $scope.popoverMenuFooter;
				return menu;
			}
			$scope.getColumn = function(item, iconRight){
				var result = "";
				if(iconRight){
					result += $scope.popoverItemColumn[1].replace('@title@', item.title).replace('@description@', item.description);
					result += $scope.popoverItemColumn[0];
				} else {
					result += $scope.popoverItemColumn[0];
					result += $scope.popoverItemColumn[1].replace('@title@', item.title).replace('@description@', item.description);
				}
				return result;
			}
			
			$scope.jobPopover = $sce.trustAsHtml($scope.genMenuHtml('jobBtn'));
			$scope.jobPopover = $sce.trustAsHtml($scope.genMenuHtml('jobBtn'));
			$scope.jobPopover = $sce.trustAsHtml($scope.genMenuHtml('jobBtn'));
			$scope.jobPopover = $sce.trustAsHtml($scope.genMenuHtml('jobBtn'));
			$scope.jobPopover = $sce.trustAsHtml($scope.genMenuHtml('jobBtn'));
			
			$scope.showPopover = function(p) {
				angular.forEach($scope.btns, function(value, key) {
					$scope.btns[key].popoverHtml = $sce.trustAsHtml($scope.genMenuHtml(p))
					if (key !== p) {
						$scope.btns[key].show = false;
					} else {
						$scope.btns[key].show = true;
					}
				})

			}

		} ]);