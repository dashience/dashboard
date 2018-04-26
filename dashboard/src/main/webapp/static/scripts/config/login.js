app.controller("LoginController", function ($scope, $translate, httpService, loginFactory) {
    var loginObj = {showErrorMessage: false};
//    $scope.showErrorMessage = false;
    loginObj.agency = null;
    var requestURL;
    var serviceUrl = '';

    $scope.getAgencyByDomain = function () {
        requestURL = serviceUrl + 'admin/user/getAgencyByDomain/' + '';
        var agency = httpService.httpProcess('GET', requestURL);
        agency.then(function (response) {
            loginObj.agency = response;
            loginObj.logo = loginObj.agency ? (loginObj.agency.logo ? loginObj.agency.logo : 'static/img/logos/deeta-logo.png') : 'static/img/logos/deeta-logo.png';
            var lan = loginObj.agency ? loginObj.agency.agencyLanguage : null;
            if (lan) {
                changeLanguage(lan);
                sessionStorage.setItem('agencyLanguage', response.agencyLanguage);
//                localStorageService.set("agencyLanguage", response.agencyLanguage);
//                localStorageService.set("agenLan", response.agencyLanguage);
            } else {
                var defaultLan = 'en';
                changeLanguage(defaultLan);
                sessionStorage.setItem('agencyLanguage', defaultLan);

//                localStorageService.set("agencyLanguage", defaultLan);
                //localStorageService.set("agenLan", defaultLan);
            }

        });

//        $http({method: "GET", url: "admin/user/getAgencyByDomain"}).then(function onSuccess(response) {
//            $scope.agency = response.data;
//            console.log(response.data);
//            $scope.logo = $scope.agency ? ($scope.agency.logo ? $scope.agency.logo : 'static/img/logos/deeta-logo.png') : 'static/img/logos/deeta-logo.png';
//            var lan = $scope.agency ? $scope.agency.agencyLanguage : null;
//            if (lan) {
//                changeLanguage(lan);
//                localStorageService.set("agencyLanguage", response.agencyLanguage);
//                localStorageService.set("agenLan", response.agencyLanguage);
//            } else {
//                var defaultLan = 'en';
//                changeLanguage(defaultLan);
//                localStorageService.set("agencyLanguage", defaultLan);
//                //localStorageService.set("agenLan", defaultLan);
//            }
//        });
    };
    $scope.getAgencyByDomain();

    $scope.authenticate = function (login) {
        if (!$scope.adminForm.$valid) {
            return;
        }
        requestURL = serviceUrl + 'admin/user/login';
        var login = httpService.httpProcess('POST', requestURL, login);
        login.then(function (response) {
            var returnMsg = loginFactory.loginValidation(response, loginObj);
            if (returnMsg && returnMsg.showErrorMessage == true) {
                loginObj.showErrorMessage = true;
                loginObj.errorMessage = returnMsg.errorMessage;
            }

        });
        $scope.login = "";

//        $http({method: "POST", url: "admin/user/login", data: login}).then(function onSuccess(response) {
//            console.log(response)
//            if (!response.data.authenticated) {
//                $scope.showErrorMessage = true;
//                $scope.errorMessage = response.errorMessage;
//            } else {
////                Auth.setUser(response);
//                $cookies.putObject("userId", response.id);
//                $cookies.putObject("fullname", response.username);
//                $cookies.putObject("username", response.username);
//                localStorageService.set("permission", response.permission);
//                localStorageService.set("userId", response.id);
//                localStorageService.set("agencyId", response.agencyId);
//                $cookies.putObject("isAdmin", response.isAdmin);
//                $cookies.putObject("agencyId", response.agencyId);
////                $location.path('index.dashboard');
//                $window.location.href = 'index.html';
//            }
//        });
//        $scope.login = "";
    };

    function changeLanguage(key) {
        $translate.use(key);
    }

    $scope.loginObj = loginObj;
});

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
