mainApp.controller('JobPersonnelCtrl', [
		'$scope', '$rootScope', '$q', '$log', '$timeout', '$cookies', 'rootscopeService', 'jobService', 'modalService',
		function($scope, $rootScope, $q, $log, $timeout, $cookies, rootscopeService, jobService, modalService) {

			function Person(id){
				this.id=id;
			    this.disabled=true;
			    this.showClearButton=true;
			    this.selectedItem=null;
			    this.searchText="";
			    this.placeholder=this.id;
			    this.sort=0;
			    return this;
			}
			Person.prototype.setId = function(id){;
				return this.setId(id);
			}
			Person.prototype.setId = function(id){
				this.id = id;
				return this;
			}
			Person.prototype.setDisabled = function(disabled){
				this.disabled = disabled;
				return this;
			}
			Person.prototype.setShowClearButton = function(showClearButton){
				this.showClearButton = showClearButton;
				return this;
			}
			Person.prototype.setSelectedItem = function(selectedItem){
				this.selectedItem = selectedItem;
				if(this.selectedItem && this.selectedItem.fullName) this.searchText = selectedItem.fullName;
				return this;
			}
			Person.prototype.setSearchText = function(searchText){
				if(this.selectedItem) {
					this.searchText = this.selectedItem.fullName;
				} else {
					this.searchText = searchText;
				}
				return this;
			}
			Person.prototype.setPlaceHolder = function(placeholder){
				this.placeholder = placeholder;
				return this;
			}
			Person.prototype.setSort = function(sort){
				this.sort = sort;
				return this;
			}
			
			var self = this;
			self.site = {};
			self.site['Management'] = {}
			self.site['Management'].sort = 1;
			self.site['Management'].clazz = 'panel-danger';
			self.site['Supervision'] = {}
			self.site['Supervision'].sort = 2;
			self.site['Supervision'].clazz = 'panel-warning';
			self.site['Site Management (Optional)'] = {}
			self.site['Site Management (Optional)'].sort = 3;
			self.site['Site Management (Optional)'].clazz = 'panel-warning';
			self.site['Site Supervision (Optional)'] = {}
			self.site['Site Supervision (Optional)'].sort = 4;
			self.site['Site Supervision (Optional)'].clazz = 'panel-warning';
			self.querySearch = querySearch;
			self.selectedItemChange = selectedItemChange;
			self.searchTextChange = searchTextChange;
			self.saveJobInfo = saveJobInfo;
			self.checkDisable = checkDisable;
			self.jobNo = $cookies.get("jobNo");
			
			rootscopeService.gettingAllUser()
			.then(function(data){
				self.repos = data;
				jobService.getJob(self.jobNo)
				.then(function(data){
					self.job = data;
					
					self.personData = [
						{id:'nameExecutiveDirector', description:'Executive Director', isDisabled:checkDisable('dloa'), store:'Management', sort:11},
						{id:'nameDirector', description:'Director', isDisabled:checkDisable('dloa'), store:'Management', sort:12},
						{id:'nameDirectSupervisorPic', description:'Direct supervisor of PIC', isDisabled:checkDisable('dloa'), store:'Management', sort:13},
						{id:'nameProjectInCharge', description:'Project in Charge', isDisabled:checkDisable('dloa'), store:'Management', sort:14},
						{id:'nameCommercialInCharge', description:'Commercial in Charge', isDisabled:checkDisable('dloa'), store:'Management', sort:15},
						{id:'nameTempWorkController', description:'Temp Works Controller', isDisabled:checkDisable('site'), store:'Supervision', sort:21},
						{id:'nameSafetyEnvRep', description:'Project Safety & Environmental Representative', isDisabled:checkDisable('site'), store:'Supervision', sort:22},
						{id:'nameAuthorizedPerson', description:'Authorized Person', isDisabled:checkDisable('site'), store:'Supervision', sort:23},
						{id:'nameSiteAdmin1', description:'Site Admin 1', isDisabled:checkDisable('site'), store:'Supervision', sort:24},
						{id:'nameSiteAdmin2', description:'Site Admin 2 (Optional)', isDisabled:checkDisable('site'), store:'Supervision', sort:25},
						{id:'nameSiteManagement1', description:'Site Management 1 (Optional)', isDisabled:checkDisable('site'), store:'Site Management (Optional)', sort:31},
						{id:'nameSiteManagement2', description:'Site Management 2 (Optional)', isDisabled:checkDisable('site'), store:'Site Management (Optional)', sort:32},
						{id:'nameSiteManagement3', description:'Site Management 3 (Optional)', isDisabled:checkDisable('site'), store:'Site Management (Optional)', sort:33},
						{id:'nameSiteManagement4', description:'Site Management 4 (Optional)', isDisabled:checkDisable('site'), store:'Site Management (Optional)', sort:34},
						{id:'nameSiteSupervision1', description:'Site Supervision 1 (Optional)', isDisabled:checkDisable('site'), store:'Site Supervision (Optional)', sort:41},
						{id:'nameSiteSupervision2', description:'Site Supervision 2 (Optional)', isDisabled:checkDisable('site'), store:'Site Supervision (Optional)', sort:42},
						{id:'nameSiteSupervision3', description:'Site Supervision 3 (Optional)', isDisabled:checkDisable('site'), store:'Site Supervision (Optional)', sort:43},
						{id:'nameSiteSupervision4', description:'Site Supervision 4 (Optional)', isDisabled:checkDisable('site'), store:'Site Supervision (Optional)', sort:44}
					];
					
					self.personData.forEach(function(p){
						var person = new Person(p.id).setPlaceHolder(p.description).setSort(p.sort);
						querySearch(self.job[p.id])
						.then(function(data){
							if(data && data.length == 1 && createFilterFor(self.job[p.id])(data[0])) {
								if(self.job[p.id] == data[0].username)
								person.setSelectedItem(data[0]);
							}
							person.setDisabled(p.isDisabled);
							person.setSearchText(self.job[p.id]);
							if(!self.site[p.store]) {
								self.site[p.store] = {};
								self.site[p.store].sort = p.sort;
							}
							if(!self.site[p.store].store) self.site[p.store].store = [];
							self.site[p.store].store.push(person);
						});
					});
				});

			});			

			
			function checkDisable(type){
				switch(type){
				case 'dloa':
					return !$rootScope.roles['has_QS_DLOA'];
					break;
				case 'site':
					return !($rootScope.roles['has_QS_SITE_ADM'] || $rootScope.roles['has_QS_DLOA']);
					break;
				default:
					return true;
				}
			}
			function querySearch(query) {
				var results = query ? self.repos.filter(createFilterFor(query)) : self.repos, deferred;
					deferred = $q.defer();
					$timeout(function() {
						deferred.resolve(results);
					}, Math.random() * 1000, false);
					return deferred.promise;
			}

			function searchTextChange(text, id) {
//				$log.info('Text of ' + id + ' changed to ' + text);
				self.job[id] = text; 
			}

			function selectedItemChange(item, id) {
//				$log.info('Item of ' + id + ' changed to ' + JSON.stringify(item));
				self.job[id] = item.username;
			}

			function createFilterFor(query) {
				var lowercaseQuery = query.toLowerCase();

				return function filterFn(item) {
					var fullNameRegExp = new RegExp(lowercaseQuery.split(' ').join('.*'));
					var fullNameReverseRegExp = new RegExp(lowercaseQuery.split(' ').reverse().join('.*'));
					return (
							(item.username && item.username.indexOf(lowercaseQuery) === 0) || 
							(item.employeeId && item.employeeId.indexOf(lowercaseQuery) === 0) ||
							(item.fullName && (fullNameRegExp.test(item.fullName.toLowerCase()) || fullNameReverseRegExp.test(item.fullName.toLowerCase()))) ||
							(item.email && item.email.toLowerCase().indexOf(lowercaseQuery) === 0)
							);
				};

			}
			
			function saveJobInfo(){
				jobService.updateJobInfo(self.job)
				.then(function( data ) {
					if(data.length>0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
				    	modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Updated successfully.");
				    	$state.reload();
					}
				});
			}
} ]);
