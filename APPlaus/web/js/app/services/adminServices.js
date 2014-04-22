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
            data: "action=editAssignmentType&assignId=" + assignment._id.$oid + "&title=" + 
                    assignment.title + "&points=" + assignment.points + "&desc=" + 
                    assignment.desc + "&active=" + assignment.active,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    this.deleteAssignment =  function(assignment) {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=deleteAssignmentType&assignId=" + assignment._id.$oid,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
})