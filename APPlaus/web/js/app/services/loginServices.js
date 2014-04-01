var services = angular.module('loginApp.services');

services.service('LoginService', function($http) {
    
    //http call to check username and password towards the database
    this.login = function(usr, pwd) {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=login&usr=" + usr + "&pwd=" + pwd,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    
    //http call to register a new user to the system
    this.registerUser = function(user) {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=registerUser&usr=" + user.usr + "&pwd=" + user.pwd + 
                    "&pwdRepeat=" +  user.pwdRepeat + "&fname=" + user.fname + 
                    "&lname=" +  user.lname + "&email=" +  user.email + 
                    "&sectionId=" + user.id,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    //http call to register a new user to the system
    this.newPassword = function(email) {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=newPassword&email=" + email,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    //http call to get sections
    this.getSections = function() {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=getSections",
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
});
