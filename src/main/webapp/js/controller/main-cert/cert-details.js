mainApp.controller('CertDetailsCtrl', ['$scope', '$http', '$location',  function ($scope, $http, $location) {

	$scope.certNo = "10";
	$scope.clientCertNo = "10";
	$scope.certStatus = "300";
	

	
	$(".date").datepicker({
		 	format: "dd-mm-yyyy",
		    todayHighlight: true,
		    autoclose: true,
		    todayBtn: "linked"
	});
	
	$scope.ipaSubmissionDate = "";
	$scope.asAtDate = "";
	$scope.certIssueDate = "";
	$scope.certDueDate = "";
   
   $scope.cert = {
   	    'mainCertAmount' : "167,341,485",
   	    'mainCertRetentionReleased' : "7,443,000",
   	    'mainCertRetention' : "7,443,000"
   };
   
   
   
 //Save Function
	$scope.save = function () {
		console.log("ipaSubmissionDate: " + $scope.ipaSubmissionDate);

	};
	
}]);

