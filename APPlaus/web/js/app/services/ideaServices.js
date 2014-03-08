var services = angular.module('employeeApp.services');

services.service('IdeaService', function($http) {
    
    //http call to get ideas from db, skip=number of ideas to skip
    this.getIdeas = function(skip) {
        var promise = $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=getIdeas&skip=" + skip,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    //http call to add idea to db
    this.addIdea = function(title, text) {
        var promise = $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=addIdea&title=" + title + "&text=" + text,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
});
