var directives = angular.module('employeeApp.directives');

directives.directive('formatedDate', function($parse) {
  return {
      restrict:'A',
        link: function linkFn(scope, lElement, attrs) {
              console.log('linkingFn(', scope, lElement, attrs, ')');
              scope.$watch(attrs.formatedDate, function(name) {
                
                var td = new Date();
                var today = td.getFullYear() + "/";
                if(td.getMonth() < 9) {
                    today += "0" + (td.getMonth() + 1) + "/";
                }
                else {
                    today +=(td.getMonth() + 1) + "/";
                }
                if(td.getDate()< 10) {
                    today += "0" + td.getDate();
                }
                else {
                    today += td.getDate();
                }
                lElement.text(today);
                
              });
              /*lElement.bind('click', function() {
                  console.log('click');
                scope.name = "abc";
                scope.$apply(function() {
                    $parse(attrs.demoGreet).assign(scope, 'abc');
                });
              })*/
        }
    }
});
