var services = angular.module('employeeApp.services');

services.service('HomeService', function($http) {
    this.getPoints = function() {
        var promise = $http({
          url: 'MongoConnection',
          method: "POST",
          data: "action=getPointsHome",
          headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        })
        return promise;
    }
    
    this.getGoals = function() {
        var promise = $http({
              url: 'MongoConnection',
              method: "POST",
              data: "action=getWeekGoal",
              headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            })
        return promise;
    }
    
    this.getAdminList = function() {
        var promise = $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=getAdminList",
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    }
})