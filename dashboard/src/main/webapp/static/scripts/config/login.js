var app = angular.module("loginApp", ['ngCookies', 'LocalStorageModule']);
app.controller("LoginController", function ($scope, $http, $window, $cookies, localStorageService, $timeout) {
    $scope.showErrorMessage = false;
    $scope.authenticate = function (login) {
        if (!$scope.adminForm.$valid) {
            return;
        }
        $http({method: "POST", url: "admin/user/login", data: login}).success(function (response) {
            console.log(response);
            if (!response.authenticated) {
                $scope.showErrorMessage = true;
                $scope.errorMessage = response.errorMessage;
            } else {
                $cookies.putObject("userId",response.id);
                $cookies.putObject("fullname", response.username);
                $cookies.putObject("username", response.username);
                localStorageService.set("permission", response.permission);
                $cookies.putObject("isAdmin", response.isAdmin);
                $cookies.putObject("agencyId", response.agencyId);
                $window.location.href = 'index.html'
            }
        });
        $scope.login = "";
    };
});


