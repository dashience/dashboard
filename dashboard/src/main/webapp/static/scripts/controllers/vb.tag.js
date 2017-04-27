app.controller('TagController', function ($scope, $http, $stateParams, $filter, $timeout) {
    $scope.tags = [];
    $scope.tagData = function () {
        $http.get('admin/tag').success(function (response) {
            $scope.tags = response;
        });
    };

    $scope.saveTag = function (tag) {
        var data = {
            id: tag.id,
            tagName: tag.tagName,
            description: tag.description,
            status: tag.status,
        };
        $http({method: tag.id ? "PUT" : "POST", url: 'admin/tag', data: data}).success(function (response) {
            $scope.tagData();
        });
        $scope.tag = "";
    };

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
    };
    $scope.deleteTag=function(tag,index){
         $http({method: "DELETE", url: 'admin/tag/' + tag.id}).success(function (response) {
             $scope.tags.splice(index, 1);
            $scope.tagData();
        });
    };
});
