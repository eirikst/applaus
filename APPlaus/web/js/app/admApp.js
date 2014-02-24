var adminApp = angular.module('adminApp', 
['employeeApp.controllers',
'adminApp.controllers',
'ngRoute',
'ngTouch',
'ngAnimate',
'ui.bootstrap'
])

        //The route provider manages routing of views
adminApp.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/registerAssignment', {templateUrl: 'partials/registerAssignment.html', controller: 'AssignCtrl'});
    $routeProvider.when('/contests', {templateUrl: 'partials/contests.html', controller: 'ContCtrl'});
    $routeProvider.when('/home', {templateUrl: 'partials/home.html', controller: 'HomeCtrl'});
    $routeProvider.otherwise({redirectTo: '/home'});
}]);