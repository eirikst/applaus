var services = angular.module('employeeApp.services');

services.service('HomeService', function($http) {
    
    this.getPoints = function() {
        var promise = $http({
          url: 'APPlausServlet',
          method: "POST",
          data: "action=getPointsHome",
          headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    this.getGoals = function() {
        var promise = $http({
              url: 'APPlausServlet',
              method: "POST",
              data: "action=getWeekGoal",
              headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            });
        return promise;
    };
        

    this.getNews = function(skip) {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=getNews&skip=" + skip,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    this.addNewsAll = function(story) {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=addNewsAll&title=" + story.title + "&text=" + 
                    story.text,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    this.deleteNews =  function(article) {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=deleteNews&newsId=" + article._id.$oid,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    this.setGoal = function(goal) {
        var promise = $http({
              url: 'APPlausServlet',
              method: "POST",
              data: "action=setGoal&points=" + goal,
              headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            });
        return promise;
    };
});