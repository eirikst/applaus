var services = angular.module('loginApp.services');

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
    
    
    //http call to register a new user to the system
    this.registerUser = function(user) {
        var promise = $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=registerUser&usr=" + user.usr + "&pwd=" + user.pwd + "&pwdRepeat=" +  user.pwdRepeat
                                       + "&fname=" + user.fname + "&lname=" +  user.lname + "&email=" +  user.email,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        })
        return promise;
    };
    
    //http call to register a new user to the system
    this.newPassword = function(userInfo) {
        var promise = $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=newPassword&email=" + userInfo.email + "&pwd=" + userInfo.pwd + "&pwdRepeat=" +  userInfo.pwdRepeat,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
});
