var controllers = angular.module('employeeApp.controllers');

controllers.controller('HomeCtrl', function($scope, $location, $http, $q, HomeService) {    
    
    $scope.adminList = new Array();
    $scope.bank = new Array();
    
    $scope.slideInterval = 5000;
    var slides = $scope.slides = [];
    slides.push({image:'http://placehold.it/800x320', text:'Antall poeng samlet denne uka', points:$scope.week});
    slides.push({image:'http://placehold.it/800x320', text:'Antall poeng samlet denne måned', points:$scope.month});
    slides.push({image:'http://placehold.it/800x320', text:'Antall poeng samlet i år', points:$scope.year});
    
    //Goal and points related
    
    /*$q.all([HomeService.getPoints(), HomeService.getGoals()]).then(function(values) {
            $scope.week = values[0][0];//sets assignment table with info from DB
            $scope.lastWeek = values[0][1];//sets assignment table with info from DB
            $scope.month = values[0][2];//sets assignment table with info from DB
            $scope.year = values[0][3];//sets assignment table with info from DB
            
            $scope.goal = values[1][0];//sets assignment table with info from DB
            $scope.goalLast = values[1][1];//sets assignment table with info from DB
            $scope.progress = ($scope.week / $scope.goal)*100;
    });*/
    HomeService.getPoints()
            .success(function(data, status, headers, config) {
            $scope.week = data[0];//sets assignment table with info from DB
            $scope.lastWeek = data[1];//sets assignment table with info from DB
            $scope.month = data[2];//sets assignment table with info from DB
            $scope.year = data[3];//sets assignment table with info from DB
            HomeService.getGoals()
                    .success(function(data, status, headers, config) {
            $scope.goal = data[0];//sets assignment table with info from DB
            $scope.goalLast = data[1];//sets assignment table with info from DB
            $scope.progress = ($scope.week / $scope.goal)*100;
                            });
                    });
                    
    HomeService.getAdminList().success(function(data, status, headers, config) {
            $scope.adminList = data;            
        });
        
     HomeService.getIdeas().success(function(data, status, headers, config) {
            $scope.bank = data;            
        });   
        
        
    $scope.setRole = function(username, role_id) {
            $http({
              url: 'MongoConnection',
              method: "POST",
              data: "action=setRole&username=" + username + "&role=" + role_id,
              headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).success(function(data, status, headers, config) {
              $scope.adminList = data;  
            }).error(function(data, status, headers, config) {
                //error
            });
        }
    
    
    $scope.addIdea = function() {
        $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=addIdea&title=" + $scope.title + "&text=" + $scope.text,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(data, status, headers, config) {
            $scope.bank = data;  
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=addIdea");
        });
    }

        $scope.setGoal = function() {
            console.log("inne");
            $http({
              url: 'MongoConnection',
              method: "POST",
              data: "action=setGoal&points=" + $scope.goalToSet,
              headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).success(function(data, status, headers, config) {
                
            }).error(function(data, status, headers, config) {
                //error
            });
        }


    
    
    
    
    
    
    
    //Ideabank related
    
    
    
    
    //News related
    $scope.news = [{title:'Konkurranse ferdig!', text:'Konkurransen "APPlaus 2.0" er ferdig. Dette er dummytekst laget av Eirik.'},
    {title:'Hvordan går det?', text:'Bruker du APPlaus nok? Snart kommer det en ny konkurranse som vil motivere til bruk av APPlaus blablabla.'},
    {title:'Heia', text:'Dette er dummytekst laget av Eirik.'},
    {title:'Registrer mål for uka!', text:'Dette er dummytekst laget av Eirik.'}];


    $scope.changeView = function(view) {
        $scope.weekPoints = 599;
        $location.path(view); // path not hash
    };
    
    $scope.returnValue = "";
    
})
