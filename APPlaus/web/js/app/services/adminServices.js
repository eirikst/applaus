var services = angular.module('employeeApp.services');

services.service('AdminService', function($http) {

    this.getAdminList = function() {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=getAdminList",
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    }
    
    this.setRole = function(username, role_id) {
        var promise = $http({
              url: 'APPlausServlet',
              method: "POST",
              data: "action=setRole&username=" + username + "&role=" + role_id,
              headers: {'Content-Type': 'application/x-www-form-urlencoded'}
          });
        return promise;
    }
})