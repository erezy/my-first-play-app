var myApp = angular.module('cesium', ['socketModule','mapModule']);
myApp.controller('cesiumCtrl',['$scope','Socket','Scene','$interval','$timeout', function($scope,Socket,Scene,$interval,$timeout) {
        var stop;
       $scope.planes = {};
       function onMessage(plane) {
           Scene.handelPlane(plane);
           $scope.planes[plane.id] = plane;
       }

      $scope.openWebSocket = function(){
            Scene.clear();
            Socket.open(onMessage);
            stop = $interval(function(){
                                if(!$scope.$$phase)$scope.$digest();
                             },500);
      }
      $scope.endWebSocket = function(){
            Socket.close();
            $interval.cancel(stop);

      }
      $scope.mark = function(){
            Scene.mark($scope.myPlane);
            var plane = $scope.planes[$scope.myPlane];

      }
}])