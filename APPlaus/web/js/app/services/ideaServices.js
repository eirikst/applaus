var services = angular.module('employeeApp.services');

services.service('IdeaService', function($http) {
    
    this.getIdeas = function(skip) {
        var promise = $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=getIdeas&skip=" + skip,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    this.addIdea = function(title, text) {
        var promise = $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=addIdea&title=" + title + "&text=" + text,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    }
})
