app.factory('loginFactory', function ($window) {
    var returnFactory = {};
    returnFactory.loginValidation = function (authData) {
        var loginMsg = {};
        var returnRes = authData;
//        var storage = window.localStorage;
        if (returnRes.authenticated == true) {
            sessionStorage.removeItem('auth');
//            storage.removeItem('auth');
//            storage.setItem('auth', objToString(returnRes));
            sessionStorage.setItem('auth', angular.toJson(returnRes));
            $window.location.href = 'index.html';
            return;
        } else {
            loginMsg.showErrorMessage = true;
            loginMsg.errorMessage = returnRes.errorMessage;
        }
        return loginMsg;
    };
    return returnFactory;
});


