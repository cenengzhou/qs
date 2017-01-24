mainApp.controller('PaymentCtrl', ['$scope', '$q', '$uibModal',  'modalService', '$cookies', '$animate', 'colorCode', 'paymentService', 'subcontractService', 'GlobalParameter', '$q', 'rootscopeService', 'jdeService', '$state',
                                   function($scope, $q, $uibModal, modalService, $cookies, $animate, colorCode, paymentService, subcontractService, GlobalParameter, $q, rootscopeService, jdeService, $state) {

	$scope.maxPaymentNo = 0;
	$scope.latestPaymentStatus = '';
	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");
	
	loadData();
	
	$scope.removeDefaultAnimation = function (){
		$animate.enabled(false);
	};

	function loadData(){
		getSubcontract();
		getPaymentCertList();
		getTotalPostedCertAmount();
		getPaymentResourceDistribution();
		rootscopeService.gettingJob($scope.jobNo)
		.then(function(response){
			$scope.job = response.job;
		});
	}
	
	$scope.disableAllButtons = function(){
		$scope.disableButton = true;
	}
	
	$scope.showPaymentHistory = function(payment){
		$scope.searchPaymentCert = payment;
		$scope.documentType = 'PS';
		modalService.open('md', 'view/enquiry/modal/enquiry-supplierledgerdetails.html', 'EnquirySupplierLedgerDetailsCtrl', 'Success', $scope);
	};
	
	/*$scope.searchPaymentStatus = function(){
		var deferral = $q.defer();
		var paymentNo = '0000' + payment.paymentCertNo;
		$scope.searchJobNo = $scope.jobNo;
		$scope.searchSubcontractNo = $scope.subcontractNo;
		$scope.searchDocumentType = 'PS'; console.log('@select');
		$scope.searchInvoiceNo = $scope.jobNo + '/' + $scope.subcontractNo + '/' + paymentNo.substring(paymentNo.length - 4);
		jdeService.obtainAPRecordList(
				$scope.searchJobNo, 
				$scope.searchInvoiceNo, 
				$scope.searchSupplierNo, 
				$scope.searchDocumentNo, 
				$scope.searchDocumentType, 
				$scope.searchSubcontractNo, 
				null)
				.then(function(data){
					if(angular.isObject(data)){

						deferral.resolve({
							paymentStatus : data[0]
						});
					}
				});
		return deferral.promise;
	}*/

	function getSubcontract(){
		subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					if(data.paymentStatus == 'F' || data.splitTerminateStatus ==1  || data.splitTerminateStatus ==2 || data.submittedAddendum ==1){
						$scope.disableButton = true;
					}
					else 
						$scope.disableButton = false;
				});
	}
	
	function getPaymentCertList() {
		paymentService.getPaymentCertList($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					if(data != null && data.length > 0){
						$scope.payments = data;
	
						$scope.maxPaymentNo = Math.max.apply(Math,$scope.payments.map(function(item){return item.paymentCertNo;}));
	
						var obj = $scope.payments.filter(function(item){ return item.paymentCertNo == $scope.maxPaymentNo; });
						$scope.latestPaymentStatus = obj[0]['paymentStatus'];
						$scope.latestPaymentType = obj[0]['intermFinalPayment'];
						
					}
					getSubcontract();
					prepareCalendar();
				});
	}

	function getTotalPostedCertAmount() {
		paymentService.getTotalPostedCertAmount($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					$scope.totalCertificateAmount = data;
				});
	}
	
	function getSubcontract(){
		subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					$scope.paymentTerms = data.paymentTerms + " - " + GlobalParameter.getValueById(GlobalParameter.paymentTerms, data.paymentTerms);
				});
	}

	
	function getPaymentResourceDistribution(){
		var bqCum = paymentService.getPaymentResourceDistribution($scope.jobNo, $scope.subcontractNo, 'BQ', 'Cumulative');
    	var voCum = paymentService.getPaymentResourceDistribution($scope.jobNo, $scope.subcontractNo, 'VO', 'Cumulative');
    	var ccCum = paymentService.getPaymentResourceDistribution($scope.jobNo, $scope.subcontractNo, 'CC', 'Cumulative');
    	var retentionCum = paymentService.getPaymentResourceDistribution($scope.jobNo, $scope.subcontractNo, 'RT', 'Cumulative');
    	var advancedCum = paymentService.getPaymentResourceDistribution($scope.jobNo, $scope.subcontractNo, 'Advanced', 'Cumulative');
    	var othersCum = paymentService.getPaymentResourceDistribution($scope.jobNo, $scope.subcontractNo, 'Others', 'Cumulative');
    	
		
    	$q.all([bqCum, voCum, ccCum, retentionCum, advancedCum, othersCum])
    		.then(function (data){
    			setDashboardData(data[0], data[1], data[2], data[3], data[4], data[5]);
    	});
	}

	function setDashboardData(bqCum, voCum, ccCum, retentionCum, advancedCum, othersCum){
		var paymentResourceDistributionJson = {
				"data": [bqCum, voCum, ccCum, retentionCum, advancedCum, othersCum],
				"labels": ["BQ", "Addendum", "Contra Charge", "Retention", "Advanced", "Others"],
				"colours": [
				{
					"strokeColor": colorCode.blue,
					"pointHighlightStroke": colorCode.lightBlue
				},
				{
					"strokeColor": colorCode.red,
					"pointHighlightStroke": colorCode.lightRed
				},
				{
					"strokeColor": colorCode.yellow,
					"pointHighlightStroke": colorCode.lightYellow
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
				}
				],
				'options' : {
					showTooltips: true,
				}
		};

		$scope.paymentResourceDistributionChart = paymentResourceDistributionJson;
	}
	
	
	function prepareCalendar(){
		var myEvent = [];

		for (i in $scope.payments) {
			curDate = (new Date($scope.payments[i].certIssueDate)).yyyymmdd();

			myEvent.push({
				title:"Cert. No."+$scope.payments[i].paymentCertNo,
				allDay: true,
				start:  curDate,
				tooltip: "Posted to Finance on "+ curDate
			});

			/*var myEvent = {
    	    	    	title:"Cert. No."+$scope.payments[payment].paymentCertNo,
    	    	    	allDay: true,
    	    	    	start:  curDate.yyyymmdd(),
    	    	    	tooltip: "Posted to Finance on"
    	    	    }

    	    	 myCalendar.fullCalendar( 'renderEvent', myEvent);*/
		}

		var myCalendar = $('#calendar').fullCalendar({
			header: {
				left: 'month,agendaWeek,agendaDay',
				center: 'title',
				right: 'prev,today,next '
			},
			selectable: true,
			selectHelper: true,
			eventLimit: true, // allow "more" link when too many events
			events:  myEvent,
			eventColor: colorCode.primary,
			eventTextColor: colorCode.white,
			eventRender: function(event, element) {
				element.attr('title', event.tooltip);
			},
			eventClick: function(calEvent, jsEvent, view) {
				var selectedCertNo = calEvent.title.substring(9);
				$state.go('subcontract.payment.certificate', {paymentCertNo: selectedCertNo, paymentTermsDesc: $scope.paymentTerms});
			}
		});
	}

	Date.prototype.yyyymmdd = function() {
		var yyyy = this.getFullYear().toString();
		var mm = (this.getMonth()+1).toString(); // getMonth() is zero-based
		var dd  = this.getDate().toString();

		return  yyyy+"-"+(mm.length===2?mm:"0"+mm) +"-"+ (dd.length===2?dd:"0"+dd); // padding
	};

	$scope.convertPaymentStatus = function(status){
		if(status!=null){
			if (status.localeCompare('PND') == 0) {
				return "Pending";
			}else if (status.localeCompare('SBM') == 0) {
				return "Submitted";
			}else if (status.localeCompare('UFR') == 0) {
				return "Under Finance Review";
			}else if (status.localeCompare('PCS') == 0) {
				return "Waiting For Posting";
			}else if (status.localeCompare('APR') == 0) {
				return "Posted To Finance";
			}
		}
	}

	$scope.progress_bar = function(status){
		if (status.localeCompare('PND') == 0) {
			return "25%";
		}else if (status.localeCompare('SBM') == 0) {
			return "50%";
		}else if (status.localeCompare('UFR') == 0) {
			return "50%";
		}else if (status.localeCompare('PCS') == 0) {
			return "75%";
		}else if (status.localeCompare('APR') == 0) {
			return "100%";
		}else{
			return "10%";
		}
	}

}]);


