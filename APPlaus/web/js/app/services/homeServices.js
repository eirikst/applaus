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
    
    this.getIdeas = function() {
        var promise = $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=getIdeas",
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    }

    this.getNews = function(skip) {
        var promise = $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=getNews&skip=" + skip,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    }
    this.addNewsAll = function(story) {
        var promise = $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=addNewsAll&title=" + story.title + "&text=" + 
                    story.text,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    }
})