var services = angular.module('employeeApp.services');

services.service('ContestService', function($http) {

    this.getActiveContests = function() {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=getActiveContests",
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    this.getInactiveContests = function(skip) {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=getInactiveContests&skip=" + skip,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    this.participate =  function(contest) {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=participate&contestId=" + contest._id.$oid,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };

    this.dontParticipate =  function(contest) {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=dontParticipate&contestId=" + contest._id.$oid,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };

    this.deleteContest =  function(contest) {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=deleteContest&contestId=" + contest._id.$oid,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };

    this.createContest =  function(contest) {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=createContest&title=" + contest.title + "&desc=" + 
                    contest.desc + "&prize=" + contest.prize + "&dateEnd=" + 
                    contest.dateSec + "&points=" + contest.points,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    this.editContest =  function(contest) {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=editContest&contestId=" + contest._id.$oid + "&title="
                    + contest.title + "&desc=" + contest.desc + "&prize=" + 
                    contest.prize + "&dateEnd=" + contest.dateSec + "&points=" 
                    + contest.points,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    this.participantList =  function(contest) {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=listParticipants&contestId=" + contest._id.$oid,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    this.declareWinner =  function(contest, username) {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=declareWinner&contestId=" + contest._id.$oid + 
                    "&username=" + username,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
});