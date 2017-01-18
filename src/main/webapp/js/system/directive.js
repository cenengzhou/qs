//Customized directive for iCheck Plugin
mainApp.directive('ngIcheck', ['$timeout',  function ($timeout) {
    return {
    	 restrict : 'A', // only activate on element attribute
         require : 'ngModel', 
       
        link: function($scope, element, $attrs, ngModel) {
        	return $timeout(function () {
                var value = $attrs.value;
                var $element = $(element);

                // Instantiate the iCheck control.                            
                $element.iCheck({
                    checkboxClass: 'icheckbox_flat-blue', //square/flat/minimal
                    radioClass: 'iradio_flat-blue'
                    //increaseArea: '20%'
                });

                // If the model changes, update the iCheck control.
                $scope.$watch($attrs.ngModel, function (newValue) {
                    $element.iCheck('update');
                });

                // If the iCheck control changes, update the model.
                $element.on('ifChanged', function (event) {
                    if ($element.attr('type') === 'radio' && $attrs.ngModel) {
                        $scope.$apply(function () {
                        	//console.log('radio value: ', value);
                            ngModel.$setViewValue(value);
                        });
                    }
                    if ($element.attr('type') === 'checkbox' && $attrs.ngModel) {
                        $scope.$apply(function () {
                        	 //console.log('checkbox value:', event.target.checked);
                        	 ngModel.$setViewValue(event.target.checked);
                        });
                    }
                });
                
            });
        }
    
    };
}]);


//Customized directive for Bootstrap Toggle Plugin
mainApp.directive('iosToggle', ['$timeout', function($timeout){
	return {
	    restrict: 'A',
	    transclude: true,
	    replace: false,
	    require: 'ngModel',
	    link: function ($scope, $element, $attr, ngModel) {
	        // update model from Element
	        var updateModelFromElement = function() {
	            // If modified
	            var checked = $element.prop('checked');
	            if (checked !== ngModel.$viewValue) {
	                // Update ngModel
	                ngModel.$setViewValue(checked);
	                $scope.$apply();
	            }
	        };

	        // Update input from Model
	        var updateElementFromModel = function() {
	            $element.trigger('change');
	        };

	        // Observe: Element changes affect Model
	        $element.on('change', function() {
	            updateModelFromElement();
	        });

	        // Observe: ngModel for changes
	        $scope.$watch(function() {
	                return ngModel.$viewValue;
	            }, function() {
	            updateElementFromModel();
	        });

	        // Initialise BootstrapToggle
	        $timeout(function() {
	            $element.bootstrapToggle();
	        });
	    }
	};
}]);



//Customized directive for Knob Plugin
mainApp.directive('ngKnob', function() {
    return {
        require: 'ngModel',
        scope: { model: '=ngModel' },
        controller: function($scope, $element, $timeout) {
            var el = $($element);
            $scope.$watch('model', function(v) {
                var el = $($element);
                el.val(v).trigger('change');
            });
        },

        link: function($scope, $element, $attrs,$ngModel) {
            var el = $($element);
            el.val($scope.value).knob(
                {
                    'change' : function (v) {
                        $scope.$apply(function () {
                          $ngModel.$setViewValue(v);
                    });
                }
                }
            );
        }
    }

});



//Window confirmation popup 
mainApp.directive('ngConfirmClick', [
 function(){
     return {
         link: function ($scope, $element, $attr) {
             var msg = $attr.ngConfirmClick || "Are you sure to close the window?";
             var clickAction = $attr.confirmedClick;
             $element.bind('click',function (event) {
                     if ( window.confirm(msg) ) {
                    	 $scope.$eval(clickAction)
                     }
                 });
             }
         };
 }]);
                             

mainApp.directive('dropzone', function() {
    return {
        restrict: 'C',
        link: function($scope, $element, $attrs) {

            var config = {
                url: 'http://localhost:8080/QSrevamp2/upload',
                maxFilesize: 10,
                paramName: "uploadfile",
                maxThumbnailFilesize: 10,
                parallelUploads: 10,
                autoProcessQueue: false
            };

            var eventHandlers = {
                'addedfile': function(file) {
                    $scope.file = file;
                    if (this.files[1]!=null) {
                        this.removeFile(this.files[0]);
                    }
                    $scope.$apply(function() {
                        $scope.fileAdded = true;
                    });
                },

                'success': function (file, response) {
                }

            };

            dropzone = new Dropzone($element[0], config);

            angular.forEach(eventHandlers, function(handler, event) {
                dropzone.on(event, handler);
            });

            $scope.processDropzone = function() {
                dropzone.processQueue();
            };

            $scope.resetDropzone = function() {
                dropzone.removeAllFiles();
            }
        }
    }
});


mainApp.directive('underConstruction', function(){
	return {
		restrict: 'E',
		templateUrl: 'view/admin/admin-mask.html'
	};
});

mainApp.directive('timestampToString', function($filter) {
	  return {
		    restrict: 'A',
		    require: 'ngModel',
		    link: function (scope, element, attrs, ngModel) {
		      ngModel.$formatters.push(function (value) {
		    	  if(value != undefined) {
		    		  return $filter('date')(new Date(value), attrs.timestampToString);
		    	  }
		    	  return value;
		      });
		    }
		  };
});



mainApp.directive('resize', function ($window) {
    return function (scope, element, attr) {

        var w = angular.element($window);
        scope.$watch(function () {
            return {
                'h': window.innerHeight, 
                'w': window.innerWidth
            };
        }, function (newValue, oldValue) {
            scope.innerHeight = newValue.h;
            scope.innerWidth = newValue.w;

            scope.resizeWithOffset = function (offsetH) {
                scope.$eval(attr.notifier);
                return { 
                    'height': (newValue.h - offsetH) + 'px',
                    'overflow': 'auto'
                };
            };
            scope.resizeMenuBarWithWOffset = function (offsetW, minWidth) {
                scope.$eval(attr.notifier);
                var width = newValue.w - offsetW;
                width = width > minWidth ? width : minWidth;
                scope.menuBarWidth = width;
                document.getElementById("menubar").scrollLeft = scope.menuScroll;
//                scope.menuWidth = document.getElementById('innerMenu').clientWidth;
                return { 
                    'width': width + 'px',
                    'overflow': 'hidden'
                };
            };

        }, true);

        w.bind('resize', function () {
            scope.$apply();
        });
    };
});

mainApp.filter('dropdownFilter', function (GlobalParameter) {
    return function (input, arrName, idPlusValue) {
    	arrName = arrName.replace(/"/g, '');
    	var arr = GlobalParameter[arrName];
    	var msg = '';
    	if(idPlusValue) msg += input + ' - ' ;
    	return msg + GlobalParameter.getValueById(arr , input);
      };
});

mainApp.directive('imageonload', function() {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            element.bind('load', function() {

            });
            element.bind('error', function(){
//            	console.log('Cannot load image:' + element[0].src)
                element[0].src = attrs.rollBackImage;
//            	console.log('Roll back to:' + element[0].src)
            });
        }
    };
});

//configure new 'compile' directive by passing a directive
// factory function. The factory function injects the '$compile'
mainApp.directive('compile', function($compile) {
  // directive factory creates a link function
  return function(scope, element, attrs) {
    scope.$watch(
      function(scope) {
         // watch the 'compile' expression for changes
        return scope.$eval(attrs.compile);
      },
      function(value) {
        // when the 'compile' expression changes
        // assign it into the current DOM
         element.html(value);
    	  
		// In case value is a TrustedValueHolderType, sometimes it
		// needs to be explicitly called into a string in order to
		// get the HTML string.
        // element.html(value && value.toString());
		
        // compile the new DOM and link it to the current scope.
        // NOTE: we only compile .childNodes so that
        // we don't get into infinite loop compiling ourselves
        $compile(element.contents())(scope);
      }
    );
  };
});

mainApp.directive('onFinishNgRepeat', function($timeout) {
	return {
        restrict: 'A',
        scope: {
            callFn: '&'
        },
		link: function(scope, element, attrs) {
			if (scope.$parent.$last){
				if(typeof scope.callFn === 'function') {
					$timeout(function(){
						scope.callFn();
					},100);
				}
			}
		}
	};
});

mainApp.directive('selectBox', function(){
	return {
		restrict: 'AE',
		replace: true,
		template: '\
			<select  class="form-control" ng-options="item.value as item.display for item in items" ng-model="selectedItem">\
				<option></option>\
			</select>',
		scope:{
			selectedItem: '=',
			itemType: '@'
		},
		controller: function($scope, rootscopeService, GlobalParameter){
			switch($scope.itemType){
			case 'company':
				rootscopeService.gettingCompanies()
				.then(function(response){
					$scope.items = response.companies;
				})
				break;
			case 'division':
				rootscopeService.gettingDivisions()
				.then(function(response){
					$scope.items = response.divisions;
				})
				break;
			case 'subcontractDetailLineType':
				$scope.items = GlobalParameter.SubcontractDetailLineType;
				break;
			case 'subcontractDetailStatus':
				$scope.items = GlobalParameter.SubcontractDetailStatus;
				break;
			case 'paymentTerms':
				$scope.items = GlobalParameter.changeKeyValue(GlobalParameter.getIdPlusValue(GlobalParameter.paymentTerms), 'id', 'value', 'value', 'display');
				break;
			case 'paymentStatus':
				$scope.items = GlobalParameter.changeKeyValue(GlobalParameter.getIdPlusValue(GlobalParameter.paymentStatus), 'id', 'value', 'value', 'display');
				break;
			default:
				$scope.items = [{value:'default', display:'default'}];
				break;
			}
			
		},
		link: function(scope, element, attrs){
			
		}
	}
})

//reuqire ui-grid and ui-grid-save-state directive
//Grid stateKey mapping can be found at userPreference.properties => GridNameMap
//<div save-and-restore-grid-state="stateKey" ui-grid-save-state ui-grid="gridOptions"></div>
mainApp.directive('saveAndRestoreGridState', function($compile, $timeout, userpreferenceService){
	return {
		restrict: 'A',
		require: '^uiGrid',
		link: function(scope, element, attrs, uiGridCtrl){			
			var byPassSaving = ['RESTORE_DB', 'RESTORE_DEFAULT'];
			var isSaving = false;
			var currentState = '';
			var stateKey = attrs['saveAndRestoreGridState'];
			var gridApi = uiGridCtrl.grid.api;
			var defaultState = {};
		    var clearPrefMenuItem = {
		            title: 'Clear grid state',
		            icon: 'fa fa-trash-o',
		            action: function($event) {
		              clearState();
		            }
		    };
			if(gridApi){
				if(!gridApi.grid.options.gridMenuCustomItems) gridApi.grid.options.gridMenuCustomItems = [];
				gridApi.grid.options.saveWidths = true;
				gridApi.grid.options.saveOrder = true;
				gridApi.grid.options.saveScroll = true;
				gridApi.grid.options.saveFocus = true;
				gridApi.grid.options.saveVisible = true;
				gridApi.grid.options.saveSort = true;
				gridApi.grid.options.saveFilter = true;
				gridApi.grid.options.savePinning = true;
				gridApi.grid.options.saveGrouping = true;
				gridApi.grid.options.saveGroupingExpandedStates = true;
				gridApi.grid.options.saveTreeView = true;
				gridApi.grid.options.saveSelection = true;
				if(gridApi.colMovable) {
			    	gridApi.colMovable.on.columnPositionChanged(scope, saveState);
				}
				if(gridApi.colResizable){
					gridApi.colResizable.on.columnSizeChanged(scope, saveState);
				}
			    if(gridApi.grouping) {
				    gridApi.grouping.on.aggregationChanged(scope, saveState);
				    gridApi.grouping.on.groupingChanged(scope, saveState);
			    }
			    if(gridApi.core){
				    gridApi.core.on.columnVisibilityChanged(scope, saveState);
				    gridApi.core.on.filterChanged(scope, saveState);
				    gridApi.core.on.sortChanged(scope, saveState);
			    }
			    $timeout(function(){
			    	defaultState = gridApi.saveState.save();
			    	restoreGridState();	
			    });
			}
			
			function saveState() {
				if(byPassSaving.indexOf(currentState) >= 0) {
					return;
				}
				if(!isSaving) {
					isSaving = true;
					$timeout(function(){
						var state = gridApi.saveState.save();
						userpreferenceService.savingGridPreference(stateKey, state)
						.then(function(response){
							isSaving = false;
							addMenuItem();
						});
					}, 1000);
				} 
			}
			
			function restoreGridState(){
				var state;
				userpreferenceService.gettingGridPreference()
				.then(function(response) {
					state = response.gridPreference[(response.gridPrefix + stateKey)];
				    if (state) {
				    	addMenuItem();
				    	currentState = 'RESTORE_DB';
				    	gridApi.saveState.restore(scope, state);
				    	currentState = '';
				    }
				});
			}
			
			function clearState(){
				userpreferenceService.clearGridPreference(stateKey)
				.then(function(response){
					currentState = 'RESTORE_DEFAULT';
					gridApi.saveState.restore(scope, defaultState);
					removeMenuItem();
					gridApi.core.refresh();
					currentState = '';
				});
			}
			
			function addMenuItem(){
				removeMenuItem();
				gridApi.grid.options.gridMenuCustomItems.push(clearPrefMenuItem);
			}
			
			function removeMenuItem(){
				var menuItemPosition =  gridApi.grid.options.gridMenuCustomItems.map(function(i){ return i.title = clearPrefMenuItem.title;}).indexOf(clearPrefMenuItem.title);
				gridApi.grid.options.gridMenuCustomItems.splice(menuItemPosition, 1);
			}
		}
	}
});