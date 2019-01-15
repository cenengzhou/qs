mainApp.controller('JobPersonnel2Ctrl', [
		'$scope', '$rootScope', '$q', '$log', '$timeout', '$sce', '$cookies', '$filter', '$mdDialog', 'rootscopeService', 'jobService', 'modalService', 'personnelService',
		function($scope, $rootScope, $q, $log, $timeout, $sce, $cookies, $filter, $mdDialog, rootscopeService, jobService, modalService, personnelService) {

			var self = this;
			self.modified = 0;
			self.requiredApproval = 0;
			self.add = add;
			self.modifyIsApprover = modifyIsApprover;
			self.save = saveList;
			self.getActivePersonnel = getActivePersonnel;
			self.submitForApproval = submitForApproval;
			self.cancelApproval = cancelApproval;
			self.showApproval = showApproval;
			self.querySearch = querySearch;
			self.selectedItemChange = selectedItemChange;
			self.searchTextChange = searchTextChange;
			self.jobNo = $cookies.get("jobNo");
			
			if (!Array.prototype.includes) {
				  Object.defineProperty(Array.prototype, "includes", {
				    enumerable: false,
				    value: function(obj) {
				        var newArr = this.filter(function(el) {
				          return el == obj;
				        });
				        return newArr.length > 0;
				      }
				  });
				}
			
			function init() {
				var deferred = $q.defer();
				getAllUser()
				.then(getAllPersonnelMap)
				.then(getActivePersonnel)
				.then(function() {
					deferred.resolve();
				});
				return deferred.promise;
			} init();
			
			function getAllUser() {
				var deferred = $q.defer();
				rootscopeService.gettingAllUser()
				.then(function(data){
					self.repos = data;
					jobService.getJob(self.jobNo)
					.then(function(data){
						self.job = data;
						deferred.resolve();
					});
				});
				return deferred.promise;
			}
			
			function getAllPersonnelMap() {
				var deferred = $q.defer();
				personnelService.getAllPersonnelMap()
				.then(function(data){
					self.AllMap = data;
					var mapList = {};
					angular.forEach(data, function(value, key) {
						if(mapList[value.userGroup] == null) mapList[value.userGroup] = [];
						mapList[value.userGroup].push(value);
					});

					var index = 0
					self.personnelMapList = [];
					angular.forEach(mapList, function(value, key){
						self.personnelMapList[index] = value;
						index++;
					})
					deferred.resolve(self.personnelMapList);
				});
				return deferred.promise;
			}
			
			function getActivePersonnel() {
				var deferred = $q.defer();
				self.site = [];
				personnelService.getActivePersonnel(self.jobNo)
				.then(function(data){
					self.modified = 0;
					self.requiredApproval = 0;
					self.personnelList = data;
					self.personnelList.forEach(function(personnel){
						personnel.currentUser = self.repos.filter(createFilterFor(personnel.userAd))[0];
						personnel.toBeApprovalUser = self.repos.filter(createFilterFor(personnel.userAdToBeApproved))[0];
						if(personnel.personnelMap.requiredApproval == 'Y'){
//							querySearch(((personnel || {}).toBeApprovalUser || {}).username)
//							.then(function(user){
//								if(user.length == 1)
//								personnel.selectedItem = user[0]; 
//							})
							personnel.selectedItem = personnel.action != 'DELETE' ? personnel.toBeApprovalUser || personnel.currentUser : null;
							personnel.disabled = ['SUBMITTED'].includes(self.job.statusApproval);
						} else {
//							querySearch(((personnel || {}).currentUser || {}).username)
//							.then(function(user){
//								if(user.length == 1)
//								personnel.selectedItem = user[0]; 
//							})
							personnel.selectedItem = personnel.toBeApprovalUser || personnel.currentUser;
						}
						if(personnel.personnelMap.requiredApproval == 'Y' && personnel.action != 'NONE') {
							self.requiredApproval++;
						}
						var userSequence = personnel.personnelMap.userSequence;
						if(self.site[userSequence] == null) self.site[userSequence] = [];
						personnel.modified = 'LOAD';
						self.site[userSequence].push(personnel);
					});
					self.personnelMapList.forEach(function(c) {
						c.forEach(function(m) {
							if(self.site[m.userSequence] == null) {
								var tempPerson = {
										action: "NONE",
										isApprover: "N",
										personnelMap: m,
										userAd: null,
										userAdPrevious: null,
										userAdToBeApproved: null,
										modified: 'TEMP'
									}
								self.site[m.userSequence] = [];
								self.site[m.userSequence].push(tempPerson);
								if(m.requiredApproval == 'Y')
									tempPerson.disabled = ['SUBMITTED'].includes(self.job.statusApproval);
							}	
						});
					});
					self.siteBack = JSON.parse(JSON.stringify(self.site));
					deferred.resolve(self.site);
				})
				return deferred.promise;
			}
			
			function add(item) {
				var person = {
						noJob: self.jobNo,
						userAdToBeApproved: item.selectedItem.username,
						user: item.selectedItem,
						isApprover: item.ISAPPROVER,
						action: 'ADD',
						personnelMap: $filter('filter')(self.AllMap, {id: item.TITLE})[0],
						modified: 'ADD',
						selectedItem: item.selectedItem
				}
				if(person.personnelMap.requiredApproval=='Y') self.requiredApproval++;
				self.modified++;
				self.site[person.personnelMap.id].push(person);
			}
			
			function cancelAdd(person) {
				if(person.modified =='LOAD' && person.id && person.action == 'ADD' && person.personnelMap.requiredApproval == 'Y'){
					personnelService.deletePersonnel(person.id)
					.then(function(data){
						getActivePersonnel();
					});
				} else {
					var idx = self.site[person.personnelMap.id].indexOf(person);
					if(idx >= 0) self.site[person.personnelMap.id].splice(idx, 1);
//					if(person.personnelMap.requiredApproval=='Y') self.requiredApproval--;
//					self.modified--;					
				}
			}
			
			function modifyIsApprover(person) {
				person.modified = 'ISAPPROVER';
				person.modifiedIsApprover = !person.modifiedIsApprover;
				if(person.modifiedIsApprover){
					//person.action = 'UPDATE';
//					if(person.personnelMap.requiredApproval=='Y') self.requiredApproval++;
					self.modified++;
				} else {
//					person.action = 'NONE';
//					person.modified = 'UPDATE';
//					if(person.personnelMap.requiredApproval=='Y') self.requiredApproval--;
					self.modified--;
				}
			}
			
			function saveList() {
				var deferred = $q.defer();
				var modifiedList = [];
				self.site.forEach(function(c) {
					var mc = c.filter(function(p){		
						return (['ADD', 'DELETE', 'UPDATE'].indexOf(p.action) > -1 && p.modified) || p.modified == 'ISAPPROVER';
					});
					modifiedList = modifiedList.concat(mc);
				});
				if(modifiedList.length>0){
					personnelService.saveList(self.jobNo, modifiedList)
					.then(function(data){
						getActivePersonnel();
						console.debug("saveList:" + data);
						deferred.resolve();
					}, function(error){
						console.error("saveList:" + error);
					})
				} else {
					deferred.resolve();
				}
				return deferred.promise;
			}
			
			function submitForApproval() {
				saveList()
				.then(function(data){
					personnelService.submitForApproval(self.jobNo)
					.then(function(data){
						init()
						.then(function(){
							self.personnelMapList[0].mode = 'W';
							modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Approval request submitted. Reference:" + data);
						});
					});
				});
			}
			
			function cancelApproval() {
				personnelService.cancelApproval(self.job.noReference)
				.then(function(data){
					init();
				});
			}
			
			function showApproval(id, ev) {
				var parentEl = angular.element(document.body);
				personnelService.returnDecisionMapHtml(self.job.noReference)
				.then(function(data){
					$scope.returnDecisionMapHtml = data;
				    $mdDialog.show({
		    	        parent:parentEl,
		    	        clickOutsideToClose: true,
		    	        templateUrl: 'show-approval-route.html',
	    	            locals: {
	    	            	trustAsHtml: $scope.trustAsHtml,
	    	                decisionMap: $scope.returnDecisionMapHtml,
	    	                job: self.job
	    	            },
		    	        targetEvent: ev,
		    	        controller: approvalRouteCtrl
		    		});
				});
			}
			
			$scope.trustAsHtml = function(string) {
			    return $sce.trustAsHtml(string);
			};
			
			function approvalRouteCtrl ($scope, $mdDialog, trustAsHtml, decisionMap, job) {
		        $scope.closeDialog = function() {
		            $mdDialog.hide();
		          }
		        $scope.$on('$stateChangeStart', $scope.closeDialog);
		        $scope.trustAsHtml = trustAsHtml;
		        $scope.decisionMap = decisionMap;
		        $scope.job = job;
				$timeout(function() {
    	        	var el = $('md-dialog');
    	        	el.css('position', 'fixed');
    	        	el.css('left', "280px");
    	        	el.css('top', '100px');
    	        	el.css('width', '1080px');
    	        	el.css('max-height', '600px');
				}, 300);
			}
			
			function searchTextChange(text, person) {
				person.user = null;
				if(!text) {
					person.action = 'DELETE';
					person.modified = 'DELETE';
				}
				if((person.personnelMap || {}).requiredApproval=='Y') self.requiredApproval++;
				self.modified++;
			}

			function selectedItemChange(item, person) {
				person.userAdToBeApproved = (item || {}).username;
				if(person.modified != 'LOAD' && item) {
					if(person.userAd) {
						person.action = 'UPDATE';
						person.modified = 'UPDATE';
					} else {
						person.action = 'ADD';
						person.modified = 'ADD';
					}
				}
				person.user = item;
			}
			
			function querySearch(query) {
				var results = query ? self.repos.filter(createFilterFor(query)) : self.repos, deferred;
					deferred = $q.defer();
					$timeout(function() {
						deferred.resolve(results);
					}, 300, false);
					return deferred.promise;
			}

			function createFilterFor(query) {
				var lowercaseQuery = (query || "").toLowerCase();
				return function filterFn(item) {
					var fullNameRegExp = new RegExp(lowercaseQuery.split(' ').join('.*'));
					var fullNameReverseRegExp = new RegExp(lowercaseQuery.split(' ').reverse().join('.*'));
					return lowercaseQuery.length > 3 && (
							(item.username && item.username.indexOf(lowercaseQuery) === 0) || 
							(item.employeeId && item.employeeId.indexOf(lowercaseQuery) === 0) ||
							(item.fullName && (fullNameRegExp.test(item.fullName.toLowerCase()) || fullNameReverseRegExp.test(item.fullName.toLowerCase()))) ||
							(item.email && item.email.toLowerCase().indexOf(lowercaseQuery) === 0)
							);
				};

			}

} ]);
