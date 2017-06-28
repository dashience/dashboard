var app = angular.module("loginApp", ['ngCookies', 'LocalStorageModule']);
app.controller("LoginController", function ($scope, $http, $window, $cookies, localStorageService, $timeout) {
    $scope.showErrorMessage = false;
    $scope.authenticate = function (login) {
        if (!$scope.adminForm.$valid) {
            return;
        }
        $http({method: "POST", url: "admin/user/login", data: login}).success(function (response) {
            
            if (!response.authenticated) {
               
                $scope.showErrorMessage = true;
                $scope.errorMessage = response.errorMessage;
            } else {
                
                console.log("CALLING CHECK EXPIRY");
                checkForLicenseExpiryDate();
                $cookies.putObject("fullname", response.username);
                $cookies.putObject("username", response.username);
                localStorageService.set("permission", response.permission);
                $cookies.putObject("isAdmin", response.isAdmin);
                $cookies.putObject("agencyId", response.agencyId);
                // checkForLicenseExpiryDate();                
            }
        });
        $scope.login = "";
    };


    function checkForLicenseExpiryDate() {
        console.log("IS AGENCY LICENCE EXPIRED");
        $http.get("admin/user/checkexpirydate").success(function (response) {

            if (response.code === 2) {
                console.log("there is no data....");

                bootbox.alert({

                    title: "<img  src='static/img/1497916017_Error.png'>&nbsp;&nbsp;&nbsp;&nbsp; Agency Licence Error!",
                    message: response.message

                });


            } else {
                $window.location.href = 'index.html';
            }

        });
    }

});