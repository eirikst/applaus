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
    }
})