app.controller('SettingsController', function ($scope, $cookies, $http, $filter, $stateParams, $state, $location, $rootScope) {
    $scope.userId = $cookies.getObject("userId");
    console.log($scope.userId);
    $scope.tab = 1;
    $scope.setTab = function (newTab) {
        $scope.tab = newTab;
    };
    $scope.isSet = function (tabNum) {
        return $scope.tab === tabNum;
    };
    $scope.ftps = [{userName: "admin", password: "password", url: "google.com", portNo: "8084"}]

    $scope.showSettings = true;
    function getSettings() {
        $http.get("admin/settings/getSettings").success(function (response) {
            $scope.files = response;
            console.log(response);
            $scope.productArray = [];
            angular.forEach($scope.files, function (value, key) {
                var index = $scope.productArray.indexOf(value.groupName);
                if (index === -1)
                {
                    $scope.productArray.push(value.groupName);
                }
                if (value.groupName === null) {
                    $scope.chartColor = value;
                    console.log($scope.chartColor);
                    if ($scope.chartColor.defaultChartColor) {
                        var userChatColor = $scope.chartColor.defaultChartColor.split(",");
                        $scope.color = userChatColor[userChatColor.length - 1];
                    } else {
                        $scope.color = "#000000";
                    }
                }

            });
        });
    }
    getSettings()
    $scope.getGroupName = function (name, list) {
        $scope.fileLists = []
        console.log(name)
        console.log(list)
        var groupList = list;
        groupList.forEach(function (value, key) {
            if (value.groupName === name) {
                value.isEdit = false;
                $scope.fileLists.push(value);
            }
        })
    };

//    $scope.addSettings = function () {
//        $scope.showSettings = false;
//    };

    $scope.editSettings = function (settings) {
        console.log("Edit Settings");
        $scope.showSettings = false;
        console.log(settings);
        var editSettingsObject = {
            id: settings.id,
            groupName: settings.groupName,
            propertyName: settings.propertyName,
            propertyValue: settings.propertyValue,
            propertyType: settings.propertyType,
            remarks: settings.remarks
        };
        $scope.settings = editSettingsObject;
    };
    $scope.saveSettings = function (settings) {
        console.log(settings);
        var SettingsObject = {
            id: settings.id,
            groupName: settings.groupName,
            propertyName: settings.propertyName,
            propertyValue: settings.propertyValue,
            propertyType: settings.propertyType,
            remarks: settings.remarks
        };
        $http({
            method: settings.id ? 'PUT' : 'POST',
            url: 'admin/settings',
            data: JSON.stringify(SettingsObject)
        }).success(function (response) {
            console.log(response);
            $scope.showSettings = true;
        }).error(function (response) {
            console.log("error");
            console.log(response);
        });
    };
    $scope.clearSettings = function () {
        $scope.showSettings = true;
    };
    $scope.selectChartColor = function (color, chartColor) {

        console.log($scope.chartColor);
        console.log(chartColor);
        console.log(color);
        var chartColorOptions=$scope.chartColor ? $scope.chartColor.defaultChartColor : "";
        console.log(chartColorOptions);
        if (chartColorOptions) {alert("if")
            $scope.chartColor.defaultChartColor = $scope.chartColor.defaultChartColor + "," + color;
        } else {alert("else")
            $scope.chartColor = {
                id: chartColor ? chartColor.id : null,
                defaultChartColor: color
            };
        }
    };

    $scope.saveChartColor = function (chartColor) {
        console.log(chartColor)
        var data = {
            id: chartColor.id,
            defaultChartColor: chartColor.defaultChartColor
        };
        console.log(data)
        $http({method: chartColor.id ? 'PUT' : 'POST', url: 'admin/settings', data: data}).success(function (response) {
            getSettings();
//                        $rootScope.getWidgetItem();
//            $scope.loadNewUrl();
        });
    };
});