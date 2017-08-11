app.controller('FilterConfigController', function ($scope, $http) {


    $http.get('admin/ui/dataSource').success(function (response) {
        $scope.dataSources = response;
        console.log($scope.dataSources);
    });


    $scope.filters = [];
    $scope.addFilter = function () {
        $scope.filters.push({isEdit: true});
    };


    $scope.editFilter = function (filter) {
        
    };
    
    $scope.deleteFieldFilter = function (index) {
         $scope.filters.splice(index, 1);
    };
    
    $scope.saveFilter = function (filter) {

    };
    
    $scope.deleteFilter = function (index) {
        $scope.filters.splice(index, 1);
    };    
});        