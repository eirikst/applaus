var controllers = angular.module('employeeApp.controllers');

controllers.controller('IdeaCtrl', function($scope, $route, $location, IdeaService) {

    //fetch ideas from server
    $scope.getIdeas = function(skip) {
        IdeaService.getIdeas(skip)
                .success(function(data, status, headers, config) {
                    $scope.skip += 7;
                    for (var i = 0; i < data.length; i++) {
                        $scope.bank.push(data[i]);//sets assignment table with info from DB
                    }
                    console.log("getIdeas success");
                })
                .error(function(data, status, headers, config) {
                    console.log("Failed http action=getIdeas");
                    $scope.getIdeasErrMsg = "En feil oppsto. Vennligst prøv igjen";
                });
    };

    //add idea to server
    $scope.addIdea = function(idea) {
        IdeaService.addIdea(idea)
                .success(function(data, status, headers, config) {
                    idea._id = data._id;
                    idea.date = data.date;
                    idea.username = data.username;
                    $scope.bank.unshift(idea);
                    $scope.skip ++;
                    $scope.createMsg = "Ide registrert!";
                    console.log("addIdea success");
                })
                .error(function(data, status, headers, config) {
                    console.log("Failed http action=addIdea");
                    $scope.createErrMsg = "En feil oppsto. Vennligst prøv igjen";
                });
    };

    //add idea to server
    $scope.addComment = function(idea, comment) {
        IdeaService.addComment(idea._id.$oid, comment)
                .success(function(data, status, headers, config) {
                    console.log("Success on http action=addIdeaComment");
                    idea.comments.push(data);
                })
                .error(function(data, status, headers, config) {
                    console.log("Failed http action=addIdeaComment");
                });
    };
    
    //Delete contest
    $scope.deleteIdea = function(idea) {
        IdeaService.deleteIdea(idea)
                .success(function(data, status, headers, config) {
                    
                    $scope.createMsg = "Ide slettet";
                    console.log("deleteIdea success");

            console.log("deleteIdea success");
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=deleteIdea");
            $scope.activeErrMsg = "En feil oppsto. Vennligst prøv igjen";
        });
    };
    
    
    //init
    $scope.skip = 0;
    $scope.bank = new Array();

    //init function calls
    $scope.getIdeas($scope.skip);


    ////////////////////////////////////////////////////////////////////////////


    //BOORT?
    $scope.changeView = function(view) {
        $location.path(view); // path not hash
    };

});