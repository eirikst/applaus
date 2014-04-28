var controllers = angular.module('employeeApp.controllers');

controllers.controller('IdeaCtrl', function($scope, $route, $location, $cookies, IdeaService) {

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
        IdeaService.addComment(idea._id.$oid, comment, idea.username)
                .success(function(data, status, headers, config) {
                    console.log("Success on http action=addIdeaComment");
                    if(idea.comments == undefined) idea.comments = new Array();
                    idea.comments.push(data);
                })
                .error(function(data, status, headers, config) {
                    console.log("Failed http action=addIdeaComment");
                });
    };
    
    //Delete idea
    $scope.deleteIdea = function(idea) {
        IdeaService.deleteIdea(idea)
                .success(function(data, status, headers, config) {
            for(var i = 0; i < $scope.bank.length; i++) {
                if(idea._id.$oid === $scope.bank[i]._id.$oid) {
                    $scope.bank.splice(i, 1);
                }
            }
            console.log("deleteIdea success");
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=deleteIdea");
            $scope.activeErrMsg = "En feil oppsto. Vennligst prøv igjen";
        });
    };

    //Like idea
    $scope.likeIdea = function(idea, like) {
        IdeaService.likeIdea(idea, like, idea.username)
                .success(function(data, status, headers, config) {
                    if(idea.likes === undefined) idea.likes = new Array();
                    if(like == 1) {
                        idea.likes.push($scope.usernameCookie);
                    }
                    else if(like == 0) {
                        for(var i = 0; i < idea.likes.length; i++) {
                            if(idea.likes[i] ==$scope.usernameCookie) {
                                idea.likes.splice(i);
                            }
                        }
                    }
            console.log("likeIdea success");
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=likeIdea");
        });
    };
    
    //Like comment
    $scope.likeComment = function(comment, like) {
        IdeaService.likeComment(comment, like)
                .success(function(data, status, headers, config) {
                    if(comment.likes === undefined) comment.likes = new Array();
                    if(like == 1) {
                        comment.likes.push($scope.usernameCookie);
                    }
                    else if(like == 0) {
                        for(var i = 0; i < comment.likes.length; i++) {
                            if(comment.likes[i] ==$scope.usernameCookie) {
                                comment.likes.splice(i);
                            }
                        }
                    }
            console.log("likeComment success");
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=likeComment");
        });
    };
    
    //Delete comment
    $scope.deleteComment = function(idea, comment) {
        IdeaService.deleteComment(idea, comment)
                .success(function(data, status, headers, config) {
                    for(var i = 0; i < idea.comments.length; i++) {
                        if(idea.comments[i].text == comment.text) {
                            idea.comments.splice(i, 1);
                        }
                    }
            console.log("deleteComment success");
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=deleteComment");
        });
    };
    
    //checking if an element is in an array
    $scope.isInArray = function(array, element) {
        if(!angular.isArray(array)) return false;
        for(var i = 0; i < array.length; i++) {
            if(array[i] == element) return true;
        }
    }
    
    //init
    $scope.skip = 0;
    $scope.bank = new Array();
    $scope.usernameCookie = $cookies.username;//role cookie
    console.log($scope.usernameCookie);

    //init function calls
    $scope.getIdeas($scope.skip);


    ////////////////////////////////////////////////////////////////////////////


    //BOORT?
    $scope.changeView = function(view) {
        if(navigator.userAgent.match(/Android|BlackBerry|iPhone|iPad|iPod|Opera Mini|IEMobile/i)) {
            $location.path(view); // path not hash
        }
    };

});