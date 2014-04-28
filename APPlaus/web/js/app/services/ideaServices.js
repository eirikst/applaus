var services = angular.module('employeeApp.services');

services.service('IdeaService', function($http) {
    
    //http call to get ideas from db, skip=number of ideas to skip
    this.getIdeas = function(skip) {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=getIdeas&skip=" + skip,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    //http call to add idea to db
    this.addIdea = function(idea) {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=addIdea&title=" + idea.title + "&text=" + idea.text,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    this.deleteIdea =  function(idea) {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=deleteIdea&ideaId=" + idea._id.$oid,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    
    //http call to add a comment to an idea
    this.addComment = function(id, comment, writer) {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=addIdeaComment&ideaId=" + id + "&text=" + comment + "&writer=" + writer,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    //http call to like an idea
    this.likeIdea = function(idea, like, writer) {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=likeIdea&ideaId=" + idea._id.$oid + "&like=" + like + "&writer=" + writer,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    //http call to like a comment
    this.likeComment = function(comment, like) {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=likeComment&commentId=" + comment.comment_id.$oid + 
                    "&like=" + like,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
    //http call to delete a comment
    this.deleteComment = function(idea, comment) {
        var promise = $http({
            url: 'APPlausServlet',
            method: "POST",
            data: "action=deleteComment&ideaId=" + idea._id.$oid + "&commentId="
                    + comment.comment_id.$oid,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        return promise;
    };
    
});
