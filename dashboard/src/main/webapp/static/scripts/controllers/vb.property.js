app.controller('PropertyController', function($scope,$http){
    $scope.properties = [];

    function getProperties() {
        $http.get('admin/user/property').success(function (response) {
            $scope.properties = response;
        });
    }
    getProperties();
    
    $scope.saveProperty = function (property) {
        $http({method: property.id ? 'PUT' : 'POST', url: 'admin/user/property', data: property}).success(function (response) {
            getProperties();
        });

        $scope.property = "";
    };

    $scope.editProperty = function (property) {
        var data = {
            id: property.id,
            propertyName: property.propertyName,
            accountId: property.accountId,
            propertyValue: property.propertyValue,
            propertyRemark: property.propertyRemark
        };
        $scope.property = data;
    };

    $scope.selectedRows = null;
    $scope.setPropertyRow = function (index) {
        $scope.selectedRows = index;
    };

    $scope.clearProperty = function (property) {
        $scope.property = "";
    };

    $scope.deleteProperty = function (property, index) {
        $http({method: 'DELETE', url: 'admin/user/property/' + property.id}).success(function (response) {
            $scope.properties.splice(index, 1);
        });
    };    
})