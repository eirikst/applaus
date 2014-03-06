var controllers = angular.module('employeeApp.controllers');

controllers.controller('IdeaCtrl', function($scope, $route, IdeaService) {    

    $scope.bank = new Array();  

    IdeaService.getIdeas().success(function(data, status, headers, config) {
            $scope.bank = data;            
        });   
        
    
    $scope.addIdea = function(title, text) {
    IdeaService.addIdea(title, text)
        .success(function(data, status, headers, config) {
            $route.reload(); 
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=addIdea");
            $scope.createErrMsg = "En feil oppsto. Vennligst prøv igjen";
        });
    }
})