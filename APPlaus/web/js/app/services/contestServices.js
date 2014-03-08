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
        });
        return promise;
    };

    this.dontParticipate =  function(contest) {
        var promise = $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=dontParticipate&contestId=" + contest._id.$oid,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };

    this.deleteContest =  function(contest) {
        var promise = $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=deleteContest&contestId=" + contest._id.$oid,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };

    this.createContest =  function(contest) {
        var promise = $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=createContest&title=" + contest.title + "&desc=" + 
                    contest.desc + "&prize=" + contest.prize + "&dateEnd=" + 
                    contest.date_end + "&points=" + contest.points,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    this.editContest =  function(contest) {
        var promise = $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=editContest&contestId=" + contest._id.$oid + "&title="
                    + contest.title + "&desc=" + contest.desc + "&prize=" + 
                    contest.prize + "&dateEnd=" + contest.date_end + "&points=" 
                    + contest.points,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
});