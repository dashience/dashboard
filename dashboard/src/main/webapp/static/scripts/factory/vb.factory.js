app.factory('loginFactory', function ($window, $http, $rootScope) {
    var returnFactory = {};
    var requestUrl = serviceUrl;
    returnFactory.loginValidation = function (authData) {
        var loginMsg = {};
        var returnRes = authData;
        if (returnRes.authenticated == true) {
            sessionStorage.removeItem('auth');
            sessionStorage.setItem('auth', angular.toJson(returnRes));
            $window.location.href = 'index.html';
            return;
        } else {
            loginMsg.showErrorMessage = true;
            loginMsg.errorMessage = returnRes.errorMessage;
        }
        return loginMsg;
    };

    returnFactory.getAuthObj = function () {
        return angular.fromJson(sessionStorage.getItem('auth'));
    };

    returnFactory.getAgencyLan = function () {
        return sessionStorage.getItem('agencyLanguage');
    };


    //Add Tab
   // var tabs = [];
    returnFactory.addTab = function (tab) {
        //var tabs;
        var data = {
            tabName: 'tab'//tab.tabName
        };

        var addTab = httpService.httpProcess('POST', requestUrl.addTabUrl + $stateParams.productId + "/" + $stateParams.accountId +
                "/" + 1, data);
        return addTab;//.then(function (response) {

//        });
//
//        $http({method: 'POST', url: 'admin/ui/dbTabs/' + $stateParams.productId + "/" + $stateParams.accountId +
//                    "/" + $stateParams.templateId, data: data}).then(function (response) {
            

//            $state.go("index.dashboard.widget", {
//                lan: $stateParams.lan,
//                accountId: $stateParams.accountId,
//                accountName: $stateParams.accountName,
//                templateId: $stateParams.templateId,
//                tabId: $stateParams.tabId,
//                startDate: $stateParams.startDate,
//                endDate: $stateParams.endDate
//            });
      //  });
        //$scope.tab = "";
    };



    return returnFactory;
});


