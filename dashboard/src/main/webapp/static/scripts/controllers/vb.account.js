app.controller('AccountController', function ($scope, $http) {

    $scope.accounts = [];

    function getAccount() {
        $http.get('admin/user/account').success(function (response) {
            $scope.accounts = response;
        });
    }
    
    $scope.properties = [];

    function getProperties() {
        $http.get('admin/user/property').success(function (response) {
            $scope.properties = response;
        });
    }
    getProperties();
    
    $scope.permissions = [];
    function getPermission() {
        $http.get('admin/user/permission').success(function (response) {
            $scope.permissions = response;
        });
    }
    getPermission();

    getAccount();
    $scope.saveAccount = function (account) {
        $http({method: account.id ? 'PUT' : 'POST', url: 'admin/user/account', data: account}).success(function (response) {
            getAccount();
        });

        $scope.account = "";
    };

    $scope.editAccount = function (account) {
        var data = {
            id: account.id,
            accountName: account.accountName,
            geoLocation: account.geoLocation,
            description: account.description
        };
        $scope.account = data;
    };

    $scope.selectedRow = null;
    $scope.setClickedRow = function (index) {
        $scope.selectedRow = index;
    };

    $scope.clearAccount = function () {
        $scope.account = "";
    };

    $scope.deleteAccount = function (account, index) {
        $http({method: 'DELETE', url: 'admin/user/account/' + account.id}).success(function (response) {
            $scope.accounts.splice(index, 1)
        })
    };    

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
    
    $scope.deletePermission = function (permission, index) {alert("Test")
        $http({method: 'DELETE', url: 'admin/user/permission/' + permission.id}).success(function (response) {
            $scope.permissions.splice(index, 1);
        });
    };
    
    $scope.clearPermission = function (permission) {
        $scope.permission = "";
    };


});