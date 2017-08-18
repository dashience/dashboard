var app = angular.module("loginApp", ['ngCookies', 'LocalStorageModule', 'pascalprecht.translate']);
app.controller("LoginController", function ($scope, $http, $window, $cookies, localStorageService, $timeout, $translate) {
    $scope.showErrorMessage = false;
    $scope.agency = null;
    $scope.getAgencyByDomain = function () {
        $http({method: "GET", url: "admin/user/getAgencyByDomain"}).success(function (response) {
            $scope.agency = response;
            $scope.logo = $scope.agency?($scope.agency.logo?$scope.agency.logo:'static/img/logos/deeta-logo.png'):'static/img/logos/deeta-logo.png';
            var lan = $scope.agency ? $scope.agency.agencyLanguage : null;
            if (lan) {
                changeLanguage(lan);
            } else {
                changeLanguage('en');
            }
        });
    };
    $scope.getAgencyByDomain();
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
                $cookies.putObject("userId", response.id);
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

    function changeLanguage(key) {
        $translate.use(key);
    }
});


