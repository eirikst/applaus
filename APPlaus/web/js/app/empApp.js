angular.module('employeeApp.services', []);
angular.module('employeeApp.controllers', []);
angular.module('employeeApp.directives', []);
angular.module('employeeApp.filters', []);


var employeeApp = angular.module('employeeApp', 
['employeeApp.services',
'employeeApp.controllers',
'employeeApp.directives',
'employeeApp.filters',
'ngRoute',
'ngTouch',
'ngAnimate',
'ngCookies',
'ngResource',
'ui.bootstrap'
]);

//The route provider manages routing of views
employeeApp.config(['$routeProvider', function($routeProvider) {
    $routeProvider
            .when('/assignments', 
    {templateUrl: 'partials/assignments.html', controller: 'AssignCtrl', 
        depth:2}
            );
    $routeProvider
            .when('/contests', 
    {templateUrl: 'partials/contests.html', controller: 'ContCtrl', depth:4}
            );
    $routeProvider
            .when('/home', 
    {templateUrl: 'partials/home.html', controller: 'HomeCtrl', depth:3}
            );
    $routeProvider
            .when('/adminList', 
    {templateUrl: 'partials/adminList.html', controller: 'AdminCtrl', depth:0}
            );
    $routeProvider
            .when('/ideabank', 
    {templateUrl: 'partials/ideabank.html', controller: 'IdeaCtrl', depth:5}
            );
    $routeProvider
            .when('/assignmentTypeList', 
    {templateUrl: 'partials/assignmentTypeList.html', controller: 'AdminCtrl'}
            );
    $routeProvider
            .when('/stats', 
    {templateUrl: 'partials/stats.html', controller: 'StatisticsCtrl', depth:1}
            );
    $routeProvider
            .otherwise({redirectTo: '/home', depth:3}
            );
}]);


employeeApp.run(function($rootScope, $window) {
  // publish current transition direction on rootScope
  $rootScope.direction = 'ltr';
  // listen change start events
  $rootScope.$on('$routeChangeStart', function(event, next, current) {
    $rootScope.direction = 'rtl';
   // console.log(arguments);
    if(current === undefined || next.depth === 0 || current.depth === 0) {
        $rootScope.direction = 'ltr'; 
    }
    else if (current && next && (current.depth > next.depth)) {
      $rootScope.direction = 'ltr';  
    }
    // back
    $rootScope.back = function() {
      $window.history.back();
    }
  });
});