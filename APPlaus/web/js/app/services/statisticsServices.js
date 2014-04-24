var services = angular.module('employeeApp.services');

services.service('StatisticsService', function($http) {

    this.getTopFive = function(period) {
        var promise = $http({
              url: 'APPlausServlet',
              method: "POST",
              data: "action=getTopFive&period=" + period,
              headers: {'Content-Type': 'application/x-www-form-urlencoded'}
          });
        return promise;
    };
    
    this.getTotalPointsSeparated = function() {
        var promise = $http({
              url: 'APPlausServlet',
              method: "POST",
              data: "action=getTotalPointsSeparated",
              headers: {'Content-Type': 'application/x-www-form-urlencoded'}
          });
        return promise;
    };
    
    this.getCommentLikeStats = function() {
        var promise = $http({
              url: 'APPlausServlet',
              method: "POST",
              data: "action=getCommentLikeStats",
              headers: {'Content-Type': 'application/x-www-form-urlencoded'}
          });
        return promise;
    };
    
    this.getSectionStats = function(period) {
        var promise = $http({
              url: 'APPlausServlet',
              method: "POST",
              data: "action=getSectionStats&period=" + period,
              headers: {'Content-Type': 'application/x-www-form-urlencoded'}
          });
        return promise;
    };
})