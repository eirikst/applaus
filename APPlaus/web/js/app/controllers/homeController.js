var controllers = angular.module('employeeApp.controllers');

controllers.controller('HomeCtrl', function($scope, $location, $http, $q, $route, HomeService, IdeaService) {    
    
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


    
    
    
    
    
    
    
    
    //News related
    //news
    $scope.news = new Array();
    var skip = 0;
    
    $scope.getNews = function() {
        HomeService.getNews(skip)
            .success(function(data, status, headers, config) {
                skip += 7;
                for(var i = 0; i < data.length; i++) {
                    $scope.news.push(data[i]);//sets news table with info from DB
                }
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=getNews");
        });
    }

    $scope.addNewsAll = function(story) {
        HomeService.addNewsAll(story)
            .success(function(data, status, headers, config) {
            $route.reload();
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=addNewsAll");
        });
    }
    
    //idea related
    $scope.addIdea = function(title, text) {
        IdeaService.addIdea(title, text)
            .success(function(data, status, headers, config) {
                $scope.bank = data;  
            }).error(function(data, status, headers, config) {
                console.log("Failed http action=addIdea");
        });
    };



    $scope.changeView = function(view) {
        $location.path(view); // path not hash
    };
    
    $scope.returnValue = "";
    $scope.getNews(skip);
})
