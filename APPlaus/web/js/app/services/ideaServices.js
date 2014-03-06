var services = angular.module('employeeApp.services');

services.service('IdeaService', function($http) {
    
    this.getIdeas = function() {
        var promise = $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=getIdeas",
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    }
    
    this.addIdea = function(title, text) {
        var promise = $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=addIdea&title=" + title + "&text=" + text,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        })
        return promise;
    }
})
