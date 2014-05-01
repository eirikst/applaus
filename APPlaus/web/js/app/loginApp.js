angular.module('loginApp.services', []);
angular.module('loginApp.controllers', []);
angular.module('loginApp.directives', []);


var loginApp = angular.module('loginApp', 
['loginApp.services',
'loginApp.controllers',
'loginApp.directives',
'ngRoute',
'ngAnimate',
]);

//The route provider manages routing of views
loginApp.config(['$routeProvider', function($routeProvider) {
    $routeProvider
            .when('/login', 
    {templateUrl: 'loginPartials/login.html', controller: 'LoginCtrl'}
    );
    $routeProvider
            .when('/regUser', 
    {templateUrl: 'loginPartials/regUser.html', controller: 'LoginCtrl'}
    );
    $routeProvider
            .when('/newPwd', 
    {templateUrl: 'loginPartials/newPwd.html', controller: 'LoginCtrl'}
    );
    $routeProvider
            .when('/regUserFb', 
    {templateUrl: 'loginPartials/regUserFb.html', controller: 'LoginCtrl'}
    );
    $routeProvider
            .otherwise({redirectTo: '/login'}
    );
}]);