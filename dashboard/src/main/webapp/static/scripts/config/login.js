app.controller("LoginController", function ($scope, $translate, httpService, loginFactory) {
    var loginObj = this;
    var requestURL = serviceUrl;
    loginObj.showErrorMessage = false;
    loginObj.agency = null;

    loginObj.getAgencyByDomain = function () {
        var agency = httpService.httpProcess('GET', requestURL.agencyUrl);
        agency.then(function (response) {
            loginObj.agency = response;
            loginObj.logo = loginObj.agency ? (loginObj.agency.logo ? loginObj.agency.logo : 'static/img/logos/deeta-logo.png') : 'static/img/logos/deeta-logo.png';
            var lan = loginObj.agency ? loginObj.agency.agencyLanguage : null;
            if (lan) {
                changeLanguage(lan);
                sessionStorage.setItem('agencyLanguage', response.agencyLanguage);
            } else {
                var defaultLan = 'en';
                changeLanguage(defaultLan);
                sessionStorage.setItem('agencyLanguage', defaultLan);
            }
        });

    };
    loginObj.getAgencyByDomain();

    loginObj.authenticate = function (userLogin) {
        if (!$scope.adminForm.$valid) {
            return;
        }
        var login = httpService.httpProcess('POST', requestURL.loginUrl, userLogin);
        login.then(function (response) {
            var returnMsg = loginFactory.loginValidation(response, loginObj);
            if (returnMsg && returnMsg.showErrorMessage == true) {
                loginObj.showErrorMessage = true;
                loginObj.errorMessage = returnMsg.errorMessage;
            }

        });
        loginObj.login = "";

    };

    function changeLanguage(key) {
        $translate.use(key);
    }

//    $scope.loginObj = loginObj;
});


