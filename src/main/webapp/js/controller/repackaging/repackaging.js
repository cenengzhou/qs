mainApp.controller('RepackagingCtrl', ['$scope', '$location', '$cookieStore', 'repackagingService', 'modalService',
                                       function($scope, $location, $cookieStore, repackagingService, modalService) {

	$scope.jobNo = $cookieStore.get("jobNo");
	$scope.jobDescription = $cookieStore.get("jobDescription");
	
	$scope.repackaging = "";
	loadRepacakgingData();
    
	$scope.click = function(view) {
		if(view=="unlock"){
			addRepackagingEntry();
		}else if (view=="reset"){
			deleteRepackagingEntry();
		}else if (view=="snapshot"){
			generateSnapshot();
		}else if (view=="confirm"){
		//	$location.path("/subcontract/dashboard");
		}
	};

	$scope.updateRemarks = function(){
		updateRepackagingEntry();
	}
	
    
    function loadRepacakgingData() {
   	 repackagingService.getLatestRepackagingEntry($scope.jobNo)
   	 .then(
			 function( data ) {
				 console.log(data);
				 $scope.repackaging = data;
			 });
    }
    
    function addRepackagingEntry() {
      	 repackagingService.addRepackagingEntry($scope.jobNo)
      	 .then(
   			 function( data ) {
   				loadRepacakgingData();
   				if(data.length!=0){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
				}else{
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Repackaging has been unlocked.");
				}
   			 });
       }
    
    function updateRepackagingEntry() {
     	 repackagingService.updateRepackagingEntry($scope.repackaging)
     	 .then(
  			 function( data ) {
  				loadRepacakgingData();
  				if(data.length!=0){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
				}else{
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Remarks has been updated.");
				}
  			 });
      }
    
    
	
    function deleteRepackagingEntry() {
     	 repackagingService.deleteRepackagingEntry($scope.repackaging.id)
     	 .then(
  			 function( data ) {
  				loadRepacakgingData();
  				if(data.length!=0){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
				}else{
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Repackaging has been reset.");
				}
  			 });
      }
    
    function generateSnapshot() {
    	 repackagingService.generateSnapshot($scope.repackaging.id, $scope.jobNo)
    	 .then(
 			 function( data ) {
 				loadRepacakgingData();
 				if(data.length!=0){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
				}else{
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Snapshot has been generated.");
				}
 			 });
     }
    
    //Attachment
	$scope.partialDownloadLink = 'http://localhost:8080/QSrevamp2/download?filename=';
    $scope.filename = '';

    $scope.uploadFile = function() {
    	console.log("Upload file");
        $scope.processDropzone();
    };

    $scope.reset = function() {
    	console.log("Reset file");
        $scope.resetDropzone();
    };
	
	
	
	
}]);