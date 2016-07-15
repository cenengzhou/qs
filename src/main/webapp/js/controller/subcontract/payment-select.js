mainApp.controller('PaymentCtrl', ['$scope', '$uibModal',  'modalService', '$animate', 'colorCode', 'paymentService', 'GlobalParameter',
                                   function($scope, $uibModal, modalService, $animate, colorCode, paymentService, GlobalParameter) {


	loadPaymentCertList();



	$scope.removeDefaultAnimation = function (){
		$animate.enabled(false);
	};



	function loadPaymentCertList() {
		paymentService.getPaymentCertList($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					$scope.payments = data.scPaymentCertWithGSTWrapperList;
					$scope.totalCertificateAmount = data.totalCertificateAmount;
					$scope.paymentTerms = GlobalParameter.getValueById(GlobalParameter.paymentTerms, data.scPackage.paymentTerms);


					$scope.maxPaymentNo = Math.max.apply(Math,$scope.payments.map(function(item){return item.paymentCertNo;}));

					var obj = $scope.payments.filter(function(item){ return item.paymentCertNo == $scope.maxPaymentNo; });
					//console.log(obj);
					$scope.latestPaymentStatus = obj[0]['paymentStatus'];
					$scope.latestPaymentType = obj[0]['intermFinalPayment'];
					
					prepareCalendar();
				});
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
				return { width: "Under Finance Review" }
			}else if (status.localeCompare('PCS') == 0) {
				return { width: "Waiting For Posting" }
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
			return { width: "50%" }
		}else if (status.localeCompare('PCS') == 0) {
			return { width: "75%" }
		}else if (status.localeCompare('APR') == 0) {
			return "100%";
		}else{
			return "10%";
		}
	}

}]);


