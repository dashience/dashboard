app.controller('UserController', function ($scope, $http) {
    $scope.users = [];
    function getUser() {
        $http.get('admin/ui/user').success(function (response) {
            $scope.users = response;
        });
    }
    getUser();
    $scope.saveUser = function (user) {
        var userData = {
            id: user.id,
            firstName: user.firstName,
            lastName: user.lastName,
            userName: user.userName,
            email: user.email,
            password: user.password,
            primaryPhone: user.primaryPhone,
            secondaryPhone: user.secondaryPhone
        };
        $http({method: user.id ? 'PUT' : 'POST', url: 'admin/ui/user', data: userData}).success(function (response) {
            getUser();
        });
        $scope.user = "";
    };

    $scope.clearUser = function () {
        $scope.user = "";
    };

    $scope.editUser = function (user) {
        var data = {
            id: user.id,
            firstName: user.firstName,
            lastName: user.lastName,
            userName: user.userName,
            email: user.email,
            password: user.password,
            primaryPhone: user.primaryPhone,
            secondaryPhone: user.secondaryPhone
        };
        $scope.user = data;
    };

    $scope.deleteUser = function (user, index) {
        $http({method: 'DELETE', url: 'admin/ui/user/' + user.id}).success(function (response) {
            $scope.users.splice(index, 1);
        });
    };
});