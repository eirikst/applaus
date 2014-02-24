angular.module('employeeApp.services', []);
angular.module('employeeApp.controllers', []);


var employeeApp = angular.module('employeeApp', 
['employeeApp.services',
'employeeApp.controllers', 
'ngRoute',
'ngTouch',
'ngAnimate',
'ui.bootstrap'
])

        //The route provider manages routing of views
employeeApp.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/registerAssignment', {templateUrl: 'partials/registerAssignment.html', controller: 'AssignCtrl'});
    $routeProvider.when('/contests', {templateUrl: 'partials/contests.html', controller: 'ContCtrl'});
    $routeProvider.when('/home', {templateUrl: 'partials/home.html', controller: 'HomeCtrl'});
    $routeProvider.otherwise({redirectTo: '/home'});
}]);