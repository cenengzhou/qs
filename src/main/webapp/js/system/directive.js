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


/*
 * ng-currency
 * http://alaguirre.com/

 * Version: 0.7.5 - 2014-07-15
 * License: MIT
 */

mainApp.directive('ngCurrency', function ($filter, $locale) {
        return {
            require: 'ngModel',
            scope: {
                min: '=min',
                max: '=max',
                ngRequired: '=ngRequired'
            },
            link: function (scope, element, attrs, ngModel) {

                function decimalRex(dChar) {
                    return RegExp("\\d|\\" + dChar, 'g')
                }

                function clearRex(dChar) {
                    return RegExp("((\\" + dChar + ")|([0-9]{1,}\\" + dChar + "?))&?[0-9]{0,2}", 'g');
                }

                function decimalSepRex(dChar) {
                    return RegExp("\\" + dChar, "g")
                }

                function clearValue(value) {
                    value = String(value);
                    var dSeparator = $locale.NUMBER_FORMATS.DECIMAL_SEP;
                    var clear = null;

                    if (value.match(decimalSepRex(dSeparator))) {
                        clear = value.match(decimalRex(dSeparator))
                            .join("").match(clearRex(dSeparator));
                        clear = clear ? clear[0].replace(dSeparator, ".") : null;
                    }
                    else if (value.match(decimalSepRex("."))) {
                        clear = value.match(decimalRex("."))
                            .join("").match(clearRex("."));
                        clear = clear ? clear[0] : null;
                    }
                    else {
                        clear = value.match(/\d/g);
                        clear = clear ? clear.join("") : null;
                    }

                    return clear;
                }

                ngModel.$parsers.push(function (viewValue) {
                    cVal = clearValue(viewValue);
                    return parseFloat(cVal);
                });

                element.on("blur", function () {
                    element.val($filter('currency')(ngModel.$modelValue));
                });

                ngModel.$formatters.unshift(function (value) {
                    return $filter('currency')(value);
                });

                scope.$watch(function () {
                    return ngModel.$modelValue
                }, function (newValue, oldValue) {
                    runValidations(newValue)
                })

                function runValidations(cVal) {
                    if (!scope.ngRequired && isNaN(cVal)) {
                        return
                    }
                    if (scope.min) {
                        var min = parseFloat(scope.min)
                        ngModel.$setValidity('min', cVal >= min)
                    }
                    if (scope.max) {
                        var max = parseFloat(scope.max)
                        ngModel.$setValidity('max', cVal <= max)
                    }
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

