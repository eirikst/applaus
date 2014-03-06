var controllers = angular.module('employeeApp.controllers');

controllers.controller('IdeaCtrl', function($scope, $route, IdeaService) {    
    $scope.skip = 0;
    $scope.bank = new Array();  

    $scope.getIdeas = function(skip) {
        IdeaService.getIdeas(skip)
                .success(function(data, status, headers, config) {
            $scope.skip += 7;
            for(var i = 0; i < data.length; i++) {
                $scope.bank.push(data[i]);//sets assignment table with info from DB
            }
        })
                .error(function(data, status, headers, config) {
            console.log("Failed http action=getIdeas");
            $scope.getIdeasErrMsg = "En feil oppsto. Vennligst prøv igjen";
        });
        
    }
        
    
    $scope.addIdea = function(title, text) {
    IdeaService.addIdea(title, text)
        .success(function(data, status, headers, config) {
            $route.reload(); 
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=addIdea");
            $scope.createErrMsg = "En feil oppsto. Vennligst prøv igjen";
        });
    }
    
    $scope.getIdeas($scope.skip);
})