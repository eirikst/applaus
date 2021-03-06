var services = angular.module('employeeApp.services');

services.service('AssignService', function($http) {
    
    this.createAssignment = function(assignment) {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=createAssignment&title=" + assignment.title + "&points=" + assignment.points
            + "&desc=" + assignment.desc,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    this.registerAssignment = function(id ,assignment) {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=registerAssignment&id=" + id + "&date_done=" + assignment.time
            + "&comment=" + assignment.comment,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    this.getAllAssignments = function(skip) {
        var promise = $http({
          url: 'APPlausServlet',
          method: "POST",
          data: "action=getAllAssignmentsUserSorted&skip=" + skip,
          headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    this.getAssignmentTypes = function() {
        var promise = $http({
          url: 'APPlausServlet',
          method: "POST",
          data: "action=getAssignmentsTypes",
          headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    this.editAssignment =  function(assignment) {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=editAssignment&assignId=" + assignment.thisId.$oid + "&comment=" + 
                    assignment.comment + "&date_done=" + assignment.dateSec,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    this.deleteAssignment =  function(assignment) {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=deleteAssignment&assignId=" + assignment.thisId.$oid,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
});