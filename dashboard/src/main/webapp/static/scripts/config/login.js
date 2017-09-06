var app = angular.module("loginApp", ['ngCookies', 'LocalStorageModule', 'pascalprecht.translate']);

app.controller("LoginController", function ($scope, $http, $window, $cookies, localStorageService, $timeout, $translate) {
    $scope.showErrorMessage = false;
    $scope.agency = null;
    $scope.getAgencyByDomain = function () {
        $http({method: "GET", url: "admin/user/getAgencyByDomain"}).success(function (response) {
            $scope.agency = response;
            console.log($scope.agency);
            $scope.logo = $scope.agency ? ($scope.agency.logo ? $scope.agency.logo : 'static/img/logos/deeta-logo.png') : 'static/img/logos/deeta-logo.png';
            var lan = $scope.agency ? $scope.agency.agencyLanguage : null;
            if (lan) {
                changeLanguage(lan);
                localStorageService.set("agencyLanguage", response.agencyLanguage);
                localStorageService.set("agenLan", response.agencyLanguage);
            } else {
                var defaultLan = 'en';
                changeLanguage(defaultLan);
                localStorageService.set("agencyLanguage", defaultLan);
                //localStorageService.set("agenLan", defaultLan);
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
//                Auth.setUser(response);
                $cookies.putObject("userId", response.id);
                $cookies.putObject("fullname", response.username);
                $cookies.putObject("username", response.username);
                localStorageService.set("permission", response.permission);
                localStorageService.set("userId", response.id);
                localStorageService.set("agencyId", response.agencyId);
                $cookies.putObject("isAdmin", response.isAdmin);
                $cookies.putObject("agencyId", response.agencyId);
//                $location.path('index.dashboard');
                $window.location.href = 'index.html'
            }
        });
        $scope.login = "";
    };

    function changeLanguage(key) {
        $translate.use(key);
    }
})
//        .factory('Auth', function () {
//            var user;
//alert("fac")
//            return{
//                setUser: function (aUser) { alert(090990)
//                    user = aUser;
//                },
//                isLoggedIn: function () {
//                    return(user) ? user : false;
//                }
//            }
//        })
//app.run(['$rootScope', '$location', 'Auth', '$state', function ($rootScope, $location, Auth, $state) {
//        $rootScope.$on('$routeChangeStart', function (event) {
//            if (!Auth.isLoggedIn()) {alert("DENY")
//                console.log('DENY');
//                event.preventDefault();
//                $location.path('/login');
//            } else {alert("Allow")
//                console.log('ALLOW');
//                $state.go('index.dashboard');
//            }
//        });
//    }]);
