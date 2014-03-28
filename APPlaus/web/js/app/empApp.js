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
    {templateUrl: 'partials/assignments.html', controller: 'AssignCtrl'}
            );
    $routeProvider
            .when('/contests', 
    {templateUrl: 'partials/contests.html', controller: 'ContCtrl'}
            );
    $routeProvider
            .when('/home', 
    {templateUrl: 'partials/home.html', controller: 'HomeCtrl'}
            );
    $routeProvider
            .when('/adminList', 
    {templateUrl: 'partials/adminList.html', controller: 'AdminCtrl'}
            );
    $routeProvider
            .when('/ideabank', 
    {templateUrl: 'partials/ideabank.html', controller: 'IdeaCtrl'}
            );
    $routeProvider
            .otherwise({redirectTo: '/home'}
            );
}]);