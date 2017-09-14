mainApp.controller("RepackagingEmailCtrl", ['$scope', '$q', '$state', '$http', '$timeout', '$mdConstant', '$cookies', 'rootscopeService', 'GlobalParameter', 'GlobalMessage', 'repackagingService', 'modalService',
                                      function ($scope, $q, $state, $http, $timeout, $mdConstant, $cookies, rootscopeService, GlobalParameter, GlobalMessage, repackagingService, modalService) {


	//Listen for location changes and call the callback
	$scope.$on('$locationChangeStart', function(event){

	});	

	var self = this;
    var pendingSearch, cancelSearch = angular.noop;
    var cachedQuery, lastSearch;
    this.customKeys = [$mdConstant.KEY_CODE.ENTER, $mdConstant.KEY_CODE.COMMA, 186 /*semicolon*/];
    self.jobNo = $cookies.get('jobNo');
    self.allContacts = loadContacts();
//    self.contacts = [self.allContacts[0]];
    self.contacts = [];
    self.contactsCc = [];
    self.filterSelected = true;
    self.mailData = {};
    self.emailContext = GlobalMessage.repackagingConfirmEmailContent.replace('%JOBNO%', self.jobNo);
    rootscopeService.gettingUser()
    .then(function(response){
    	self.emailSubject = GlobalMessage.repackagingConfirmEmailSubject
    	.replace('%JOBNO%', self.jobNo).replace('%USERNAME%', response.user.fullname);
    });
    self.querySearch = querySearch;
    self.querySearchCc = querySearchCc;
    self.filterToCc = filterToCc;
    self.sendEmail = sendEmail;
    
    $scope.clickContact = function(index){
    	if(self.focusElement.placeholder == 'Cc'){
    		self.contactsCc.push(self.allContacts[index]);
    	} else {
    		self.contacts.push(self.allContacts[index]);
    	}
    	self.focusElement.focus();
    }
    
    $scope.checkFocusElement = function(){
    	self.focusElement = angular.element(window.document.activeElement)[0];
    }
    
    function sendEmail(){
    	self.mailData.contacts = getEmailAddress(self.contacts);
    	self.mailData.contactsCc = getEmailAddress(self.contactsCc);
    	self.mailData.emailSubject = self.emailSubject;
    	self.mailData.emailContext = self.emailContext;
    	repackagingService.sendEmailToReviewer(self.mailData)
    	.then(function(data){
    		if(data){
    			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', 'Email sent to reviewer');
    			self.contacts = [];
    			self.contactsCc = [];
    			self.mailData = {};
//    			self.emailSubject = null;
//    			self.emailContext = null;
    		} else{
    			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'Fail to send mail');
    		}
    	});

    }
    
    function getEmailAddress(contactList){
    	var emailAddress = '';
    	contactList.forEach(function(contact){
    		var email = '';
    		if(contact.email){
    			email = contact.email;
    		} else {
    			email = contact;
    		}
    		emailAddress += email + ';';
    	})
    	return emailAddress;
    }
    /**
     * Search for contacts; use a random delay to simulate a remote call
     */
    function querySearch (criteria) {
      cachedQuery = cachedQuery || criteria;
//      return cachedQuery ? self.allContacts.filter(createFilterFor(cachedQuery)) : [];
      return self.allContacts.filter(createFilterFor(criteria)); 
    }
    function querySearchCc (criteria) {
        cachedQuery = cachedQuery || criteria;
//        return cachedQuery ? self.allContacts.filter(createFilterFor(cachedQuery)) : [];
        return self.allContacts.filter(createFilterFor(criteria)); 
      }

    function filterToCc(){
    	var toCc = angular.extend($scope.contacts, $scope.contactsCc);
    	return toCc;
    }
    /**
     * Create filter function for a query string
     */
    function createFilterFor(query) {
      var lowercaseQuery = angular.lowercase(query);

      return function filterFn(contact) {
//    	console.log(contact._lowername + "indexof " + lowercaseQuery + ' : ' + contact._lowername.indexOf(lowercaseQuery) + ' : ' + (contact._lowername.indexOf(lowercaseQuery) != -1));
        return (contact._lowername.indexOf(lowercaseQuery) != -1 || contact.email.indexOf(lowercaseQuery) != -1);;
      };

    }
    
    function loadContacts() {
//    	var contacts = [];
//    	repackagingService.getReviewerList()
//    	.then(function(data){
//    		contacts = JSON.parse(data)['contactList'];
//    		contacts.forEach(function(contact){
//    			contact.name = contact.username;
//    			contact.email = contact.emailaddress;
//    			contact.image = GlobalParameter.imageServerAddress + contact.staffid + '.jpg'
//    			contact._lowername = contact.name.toLowerCase();
//    		});
//    		self.allContacts = contacts; 
//    	});
//		Get reveiewer email list from GSF
    	repackagingService.getReviewerListFromGSF(self.jobNo)
    	.then(function(data){
    		if(data.length>0)
    		data.forEach(function(contact){
    			contact.name = contact.UserName;
    			contact.email = contact.UserMail;
    			contact.image = GlobalParameter.imageServerAddress + contact.StaffID + '.jpg'
    			contact._lowername = contact.name.toLowerCase();
    		});
    		self.allContacts = data; 
    	})
    }
	
	$scope.tinymceOptions = {
		    plugins: [
		              'advlist autolink lists link textcolor colorpicker charmap autoresize', // code
		              'print preview hr searchreplace wordcount insertdatetime ',
		              'nonbreaking save table contextmenu directionality paste textpattern '
		              ],
              removed_menuitems:'newdocument visualaid ',
              toolbar: 'print preview | undo redo | bold italic | forecolor backcolor | alignleft aligncenter alignright | bullist numlist outdent indent | link ', 
              save_enablewhendirty: false,
              statusbar: true,
              autoresize_min_height: 400,
              autoresize_max_height: 800,
              skin: 'tinymce_charcoal'
};
	
}]);

