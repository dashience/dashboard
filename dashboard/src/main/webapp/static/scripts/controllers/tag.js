/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

app.controller('tagController', function ($scope, $http, $stateParams, $filter, $timeout) {
    $scope.tags = [];
    $scope.tagData = function () {
        $http.get('admin/tag').success(function (response) {
            $scope.tags = response;
        })
    }

    $scope.saveTag = function (tag) {
        alert()
        var data = {
            id: tag.id,
            tagName: tag.tagName,
            description: tag.description,
            status: tag.status,
        }
        $http({method: tag.id ? "PUT" : "POST", url: 'admin/tag', data: data}).success(function (response) {
            $scope.tagData();
            console.log(response)
        });
        $scope.tag = ""
    }

    $http.get('admin/tag').success(function (response) {

        $scope.tags = response;
    })
    $scope.editTag = function (tag, index) {
        $scope.tagData();
        $scope.id = tag;
        var data = {
            id: tag.id,
            tagName:tag.tagName,
            description: tag.description,
            status:tag.status
        }
        $scope.tag=data;
    }
    $scope.deleteTag=function(tag,index){
         $http({method: "DELETE", url: 'admin/tag/' + tag.id}).success(function (response) {
             $scope.tags.splice(index, 1);
            $scope.tagData();
        });
    }
})
