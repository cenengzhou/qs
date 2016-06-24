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


//CurrencyFilter
mainApp.directive('realTimeCurrency', function ($filter, $locale) {
    var decimalSep = $locale.NUMBER_FORMATS.DECIMAL_SEP;
    var toNumberRegex = new RegExp('[^0-9\\' + decimalSep + ']', 'g');
    var trailingZerosRegex = new RegExp('\\' + decimalSep + '0+$');
    var filterFunc = function (value) {
        return $filter('currency')(value);
    };

    function getCaretPosition(input){
        if (!input) return 0;
        if (input.selectionStart !== undefined) {
            return input.selectionStart;
        } else if (document.selection) {
            // Curse you IE
            input.focus();
            var selection = document.selection.createRange();
            selection.moveStart('character', input.value ? -input.value.length : 0);
            return selection.text.length;
        }
        return 0;
    }

    function setCaretPosition(input, pos){
        if (!input) return 0;
        if (input.offsetWidth === 0 || input.offsetHeight === 0) {
            return; // Input's hidden
        }
        if (input.setSelectionRange) {
            input.focus();
            input.setSelectionRange(pos, pos);
        }
        else if (input.createTextRange) {
            // Curse you IE
            var range = input.createTextRange();
            range.collapse(true);
            range.moveEnd('character', pos);
            range.moveStart('character', pos);
            range.select();
        }
    }
    
    function toNumber(currencyStr) {
        return parseFloat(currencyStr.replace(toNumberRegex, ''), 10);
    }

    return {
        restrict: 'A',
        require: 'ngModel',
        link: function postLink(scope, elem, attrs, modelCtrl) {    
            modelCtrl.$formatters.push(filterFunc);
            modelCtrl.$parsers.push(function (newViewValue) {
                var oldModelValue = modelCtrl.$modelValue;
                var newModelValue = toNumber(newViewValue);
                modelCtrl.$viewValue = filterFunc(newModelValue);
                var pos = getCaretPosition(elem[0]);
                elem.val(modelCtrl.$viewValue);
                var newPos = pos + modelCtrl.$viewValue.length -
                                   newViewValue.length;
                if ((oldModelValue === undefined) || isNaN(oldModelValue)) {
                    newPos -= 3;
                }
                setCaretPosition(elem[0], newPos);
                return newModelValue;
            });
        }
    };
});


//Loading
mainApp.directive('loading', function () {
    return {
      restrict: 'E',
      replace:true,
      template: '<div class="loading"><img src="http://www.nasa.gov/multimedia/videogallery/ajax-loader.gif" width="20" height="20" />LOADING...</div>',
      link: function (scope, element, attr) {
            scope.$watch('loading', function (val) {
                if (val)
                    $(element).show();
                else
                    $(element).hide();
            });
      }
    }
});


/*app.directive('myDatepicker', function() {
    return {
        restrict: 'A',
        require : 'ngModel',
        link : function (scope, element, attrs, ngModelCtrl) {
            $(function(){
                element.datepicker({
                    dateFormat:'dd/mm/yy',
                    onSelect:function (date) {
                        scope.$apply(function () {
                            ngModelCtrl.$setViewValue(date);
                        });
                    }
                });
            });
        }
    }
});*/
mainApp.directive('underConstruction', function(){
	return {
		restrict: 'E',
		templateUrl: 'view/admin/admin-mask.html'
	};
});