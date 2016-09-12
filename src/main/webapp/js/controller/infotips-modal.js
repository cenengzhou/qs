mainApp.controller('InfoTipsCtrl', 
			['$scope', 'modalStatus', 'modalParam', '$uibModalInstance', 'GlobalParameter', '$sce', '$rootScope',
	function($scope, modalStatus, modalParam, $uibModalInstance, GlobalParameter, $sce, $rootScope){
	$scope.GlobalParameter = GlobalParameter;
	$scope.status = modalStatus;
	$scope.parentScope = modalParam;
	$scope.cancel = function () {
		$uibModalInstance.close();
	};
	$scope.selectedTips = $rootScope.selectedTips;
	$scope.trustAsHtml = $sce.trustAsHtml;
	$scope.tips = [
	               {
        			   name: 'paymentStatus',
        			   header: 'Payment Status Info Tips',
        			   columnNames: {id:'Status', value:'Description'},
            		   flowHtmlString: '<table class="text-primary" style="text-align:center ;"><tr>\
            			   				<td><label class="icon-btn" style="padding: 5px 10px 5px 10px;color:#007D00;background-color:#83D8FD"> PND </label></td><td><span class="fa fa-2x fa-long-arrow-right" style="padding: 0px 5px 0px 5px "></span></td>\
            			   				<td><label class="icon-btn" style="padding: 5px 10px 5px 10px;color:#E68550;background-color:#83D8FD"> SBM </label></td><td><span class="fa fa-2x fa-long-arrow-right" style="padding: 0px 5px 0px 5px "></span></td>\
            			   				<td><label class="icon-btn" style="padding: 5px 10px 5px 10px;color:#E68550;background-color:#83D8FD"> PCS </label></td><td><span class="fa fa-2x fa-long-arrow-right" style="padding: 0px 5px 0px 5px "></span></td>\
            			   				<td><label class="icon-btn" style="padding: 5px 10px 5px 10px;color:#E68550;background-color:#83D8FD"> UFR </label></td><td><span class="fa fa-2x fa-long-arrow-right" style="padding: 0px 5px 0px 5px "></span></td>\
            			   				<td><label class="icon-btn" style="padding: 5px 10px 5px 10px;color:#707070;background-color:#83D8FD"> APR </label></td>\
            			   				</tr></table>',
        			   items:GlobalParameter.paymentStatus,
        			   visible: true,
        			   group:'Payment',
        			   order:1
    			   },
	               {
					   name: 'mainContractCertificateStatus',
					   header: 'Main Contract Certificate Status Info Tips',
            		   flowHtmlString: '<table class="text-primary" style="text-align:center ;"><tr>\
			   				<td><label class="icon-btn text-white" style="padding: 5px 10px 5px 10px;background-color:#83D8FD"> 100 </label></td><td><span class="fa fa-2x fa-long-arrow-right" style="padding: 0px 5px 0px 5px "></span></td>\
			   				<td><label class="icon-btn text-white" style="padding: 5px 10px 5px 10px;background-color:#83D8FD"> 120 </label></td><td><span class="fa fa-2x fa-long-arrow-right" style="padding: 0px 5px 0px 5px "></span></td>\
			   				<td><label class="icon-btn text-white" style="padding: 5px 10px 5px 10px;background-color:#83D8FD"> 150 </label></td><td><span class="fa fa-2x fa-long-arrow-right" style="padding: 0px 5px 0px 5px "></span></td>\
			   				<td><label class="icon-btn text-white" style="padding: 5px 10px 5px 10px;background-color:#83D8FD"> 300 </label></td><td><span class="fa fa-2x fa-long-arrow-right" style="padding: 0px 5px 0px 5px "></span></td>\
			   				<td><label class="icon-btn text-white" style="padding: 5px 10px 5px 10px;background-color:#CCCCCC"> 400 </label></td>\
			   				</tr><tr>\
            			   <td></td><td></td>\
			   				<td></td><td></td>\
			   				<td><span class="fa fa-2x fa-level-up fa-rotate-90" style="padding: 0px 5px 0px 5px;color:#fdaa70"></span></td><td><label class="icon-btn text-white" style="padding: 5px 10px 5px 10px;background-color:#fdaa70"> 200 </label></td>\
			   				<td><span class="fa fa-2x fa-level-up" style="padding: 0px 5px 0px 5px;color:#fdaa70;top:-10px"></span></td><td></td>\
			   				<td></td>\
            			   </tr></table>',
					   columnNames: {id:'Code', value:'Description'},
					   items:GlobalParameter.mainContractCertificateStatus,
        			   visible: true,
        			   group:'Main Certificate',
        			   order:1
				   },
	               {
					   name: 'subcontractStatus',
					   header: 'Subcontract Status Info Tips',
            		   flowHtmlString: '<table class="text-primary" style="text-align:center ;"><tr>\
			   				<td><label class="icon-btn text-white" style="padding: 5px 10px 5px 10px;background-color:#83D8FD"> 100 </label></td><td><span class="fa fa-2x fa-long-arrow-right" style="padding: 0px 5px 0px 5px "></span></td>\
			   				<td><label class="icon-btn text-white" style="padding: 5px 10px 5px 10px;background-color:#83D8FD"> 160 </label></td><td><span class="fa fa-2x fa-long-arrow-right" style="padding: 0px 5px 0px 5px "></span></td>\
			   				<td><label class="icon-btn text-white" style="padding: 5px 10px 5px 10px;background-color:#83D8FD"> 330 </label></td><td><span class="fa fa-2x fa-long-arrow-right" style="padding: 0px 5px 0px 5px "></span></td>\
			   				<td><label class="icon-btn text-white" style="padding: 5px 10px 5px 10px;background-color:#83D8FD"> 500 </label></td>\
			   				</tr></table>',
					   columnNames: {id:'Code', value:'Description'},
					   items:GlobalParameter.subcontractStatus,
        			   visible: true,
        			   group:'Subcontract',
        			   order:1
				   },
	               {
					   name: 'subcontract_paymentStatus',
					   header: 'Subcontract Payment Status Info Tips',
					   columnNames: {id:'Code', value:'Description'},
					   items:GlobalParameter.subcontract_paymentStatus,
        			   visible: true,
        			   group:'Subcontract',
        			   order:2
				   },
				   {
					   name: 'repackagingStatus',
            		   flowHtmlString: '<table class="text-primary" style="text-align:center ;"><tr>\
			   				<td><label class="icon-btn" style="padding: 5px 10px 5px 10px;background-color:#83D8FD;color:#707070"> 900 </label></td><td><span class="fa fa-2x fa-long-arrow-right" style="padding: 0px 5px 0px 5px "></span></td>\
			   				<td><label class="icon-btn" style="padding: 5px 10px 5px 10px;background-color:#83D8FD;color:#007D00"> 100 </label></td><td><span class="fa fa-2x fa-long-arrow-right" style="padding: 0px 5px 0px 5px "></span></td>\
			   				<td><label class="icon-btn" style="padding: 5px 10px 5px 10px;background-color:#83D8FD;color:#007D00"> 200 </label></td><td><span class="fa fa-2x fa-long-arrow-right" style="padding: 0px 5px 0px 5px "></span></td>\
			   				<td><label class="icon-btn" style="padding: 5px 10px 5px 10px;background-color:#83D8FD;color:#E68550"> 300 </label></td><td><span class="fa fa-2x fa-long-arrow-right" style="padding: 0px 5px 0px 5px "></span></td>\
			   				<td><label class="icon-btn" style="padding: 5px 10px 5px 10px;background-color:#83D8FD;color:#707070"> 900 </label></td>\
			   				</tr></table>',
			   	       header: 'Repackaging Status Info Tips',
					   columnNames: {id:'Code', value:'Description'},
					   items:GlobalParameter.repackagingStatus,
        			   visible: true,
        			   group:'Repackaging',
        			   order:1
				   },
	               {
					   name: 'PerformanceAppraisalStatus',
					   header: 'Performance Appraisal Status Info Tips',
					   columnNames: {id:'Code', value:'Description'},
					   items:GlobalParameter.PerformanceAppraisalStatus,
        			   visible: true,
        			   group:'Performance Appraisal',
        			   order:1
				   },
				   {
            		   name: 'paymentTerms',
            		   header: 'Payment Terms Info Tips',
            		   columnNames: {id:'Code', value:'Description'},
            		   items:GlobalParameter.paymentTerms,
        			   visible: true,
        			   group:'Payment',
        			   order:2
        		   },
	               {
    				   name: 'paymentStatusCode',
    				   header: 'Payment Status Code Info Tips',
    				   columnNames: {id:'Code', value:'Description'},
    				   items:GlobalParameter.paymentStatusCode,
        			   visible: true,
        			   group:'Payment',
        			   order:3
				   },
	               {
					   name: 'paymentCurrency',
					   header: 'Payment Currency Info Tips',
					   columnNames: {id:'Code', value:'Description'},
					   items:GlobalParameter.paymentCurrency,
        			   visible: true,
        			   group:'Payment',
        			   order:4
				   },
	               {
					   name: 'approvalType',
					   header: 'Approval Type Info Types',
					   columnNames: {id:'Code', value:'Description', details:'Details'},
					   items:GlobalParameter.approvalType,
        			   visible: true,
        			   group:'Approval',
        			   order:1
				   },
	               {
	            	   name: 'addendumTypes',
	            	   header: 'Addendum Types Info Tips',
	            	   columnNames: {id:'Code', value:'Description'},
	            	   items:GlobalParameter.addendumTypes,
        			   visible: true,
        			   group:'Subcontract',
        			   order:3
            	   },
	               {
					   name: 'PurchaseOrderType',
					   header: 'Purchase Order Type Info Tips',
					   columnNames: {id:'Code', value:'Description'},
					   items:GlobalParameter.PurchaseOrderType,
        			   visible: true,
        			   group:'Purchase Order',
        			   order:1
				   }
   ];
	
	$scope.fixColumnWidth = function(str, len){
		if(len>0){
			var strLen = str.length;
			var nbspLen = len - strLen;
			var nbsp = '';
			for(var i = 0; i< nbspLen; i++){
				nbsp += '&nbsp;';
			}
			str += nbsp;	
		}
		return str;
	}
	
	$scope.showTip = function(){
		$scope.tips.forEach(function(tip){
			if(GlobalParameter.getAttributeArray($scope.tips, 'name').indexOf($scope.selectedTips) > -1){
				if(tip.name === $scope.selectedTips){
					tip.visible = true;
				} else {
					tip.visible = false;
				}
			} else {
				tip.visible = true;
			}
		})
	}
	
		
	$scope.showTip();
}]);