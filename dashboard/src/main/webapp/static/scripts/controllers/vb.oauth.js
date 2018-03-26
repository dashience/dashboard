//var myApp = angular.module('apiService', []);

app.controller('socialController', function ($window, $scope, $http, $stateParams, $cookies) {
    $scope.oAuth2Details = false;
    $scope.showDataSets = false;
    $scope.oAuthData = {};
    $scope.scheduler = {};
    $scope.oAuthData.accountId = $stateParams.accountId;
    console.log("stateParams----->", $scope.oAuthData.accountId);
    $scope.oAuthData.userId = $cookies.getObject("userId");
    $scope.oAuthData.domainName = location.host;
    console.log("var x--->", $scope.oAuthData.domainName);
    $scope.onload = function () {
        $http.get('admin/ui/dataSource').success(function (response) {
            $scope.dataSources = response;
            console.log("response--------->", $scope.dataSources);
        });
    };
    $scope.onload();
    $scope.getOAuthToken = function (index) {
        if ($scope.dataSources[index].oauthStatus == true) {
            $scope.oAuth2Details = false;
            $scope.oAuthData.source = $scope.dataSources[index].dataSourceType;
            $http({method: "GET", url: "admin/ui/dataSet"}).success(function (response) {
                console.log("response---------->", response);
                $scope.dataSets = response.filter(a => a.dataSourceId.dataSourceType == $scope.oAuthData.source);
                $scope.showDataSets = true;
                console.log("dataSets------------------>", $scope.dataSets);
            });
        } else {
            $scope.showDataSets = false;
            $scope.oAuth2Details = true;
            $scope.oAuthData.source = $scope.dataSources[index].dataSourceType;
            $scope.oAuthData.index = index;
        }
    };
    $scope.onSubmit = function () {
        login("admin/social/signin?apiKey=" + $scope.oAuthData.clientId + "&apiSecret=" + $scope.oAuthData.clientSecret + "&apiSource=" + $scope.oAuthData.source + "&accountId=" + $scope.oAuthData.accountId + "&domainName=" + $scope.oAuthData.domainName);
//        $window.open("admin/social/signin?apiKey=" + $scope.oAuthData.clientId + "&apiSecret=" + $scope.oAuthData.clientSecret + "&apiSource=" + $scope.oAuthData.source);
    };
    $scope.success = function () {
        console.log("called-------->", $scope.oAuth2Details);
        $scope.dataSources[$scope.oAuthData.index].oauthStatus = true;
        $scope.oAuth2Details = false;
        $http({method: "PUT", url: "admin/social/oauthStatus", data: $scope.dataSources[$scope.oAuthData.index]}).success(function (response) {
            console.log("response-------->", response);
        });
        console.log("oAuth2Details-------->", $scope.oAuth2Details);
    };
    $scope.setDataSet = function (dataSet) {
        console.log(dataSet);
        $scope.scheduler.dataSet = dataSet;
        $scope.scheduler.accountId = $scope.oAuthData.accountId;
    };
    $scope.scheduleData = function (scheduleData) {
        console.log("scheduleData-------->", scheduleData);
        $http({method: "POST", url: "admin/schedule", data: scheduleData}).success(function (response) {
            console.log("response------>", response);
        });
    };
    function login(url) {
        console.log("url----->", url);
        var win = $window.open(url);

        var pollTimer = $window.setInterval(function () {
            console.log("win--->", win.document.URL);
            try {
//                console.log(win.document.URL);
                if (win.document.URL.indexOf("success") != -1) {
                    $window.clearInterval(pollTimer);
                    var url = win.document.URL;
                    win.top.close();
                    $scope.success();
                }
            } catch (e) {
            }
        }, 500);
    }
});