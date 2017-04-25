/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

app.controller('tagController', function ($scope, $http, $stateParams, $filter, $timeout) {
    $scope.tags = [];

    $http.get('admin/widgetTag').success(function (response) {
        $scope.tags = response;
    })
    $scope.saveTag = function (tag) {
        var data = {
            id: tag.id,
            tagName: tag.tagName,
            description: tag.description,
            status: tag.status,
        }
        $http({method: tag.id ? "PUT" : "POST", url: 'admin/widgetTag', data: data}).success(function (response) {
            $scope.getFields();
        });
        $scope.tag = ""
    }
})
