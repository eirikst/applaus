var services = angular.module('employeeApp.services');

services.service('LoginService', function($http) {
    
    //http call to check username and password towards the database
    this.login = function(usr, pwd) {
        var promise = $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=login&usr=" + usr + "&pwd=" + pwd,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
})
