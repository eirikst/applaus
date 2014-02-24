var services = angular.module('employeeApp.services');

services.service('ContestService', function($http) {

    this.getActiveContests = function() {
        var promise = $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=getActiveContests&skip=",
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    this.getInactiveContests = function(skip) {
        var promise = $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=getInactiveContests&skip=" + skip,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };

    this.getUsersActiveContests = function() {
        var promise = $http({
              url: 'MongoConnection',
              method: "POST",
              data: "action=userActiveContList",
              headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    this.participate =  function(contest) {
        var promise = $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=participate&contestId=" + contest._id.$oid,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        })
        return promise;
    }

    this.dontParticipate =  function(contest) {
        var promise = $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=dontParticipate&contestId=" + contest._id.$oid,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        })
        return promise;
    }
});

/*services.factory('activeContService', function($http) {
    var activeContests = {
    async: function() {
      // $http returns a promise, which has a then function, which also returns a promise
      promise = $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=getActiveContests",
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        })
        .then(function (response) {
            // The then function here is an opportunity to modify the response
            console.log(response);
            // The return value gets picked up by the then in the controller.
            return response.data;
        });
        // Return the promise to the controller
        return promise;
    }
  };
  return activeContests;
})*/