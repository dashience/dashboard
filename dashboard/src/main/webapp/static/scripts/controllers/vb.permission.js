app.controller('PermissionController', function ($scope, $http) {
    $scope.permissions = [];
    function getPermission() {
        $http.get('admin/user/permission').success(function (response) {
            $scope.permissions = response;
        });
         $http.get('admin/user/account').success(function (response) {
            $scope.accounts = response;
        });
        $http.get('admin/user/property').success(function (response) {
            $scope.properties = response;
        });
    }
    getPermission();

    $scope.savePermission = function (permission) {
        $http({method: permission.id ? 'PUT' : 'POST', url: 'admin/user/permission', data: permission}).success(function (response) {
            getPermission();
        });

        $scope.permission = "";
    };

    $scope.editPermission = function (permission) {
        var data = {
            id: permission.id,
            accountId: permission.accountId,
            propertyId: permission.propertyId,
            status: permission.status
        };
        $scope.permission = data;
    };

    $scope.selectedPermission = null;
    $scope.setPermissionRow = function (index) {
        $scope.selectedPermission = index;
    };

    $scope.deletePermission = function (permission, index) {
        alert("Test")
        $http({method: 'DELETE', url: 'admin/user/permission/' + permission.id}).success(function (response) {
            $scope.permissions.splice(index, 1);
        });
    };

    $scope.clearPermission = function (permission) {
        $scope.permission = "";
    };
})