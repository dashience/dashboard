app.controller('SettingsController', function ($scope, $cookies, $http, $filter, $stateParams, $state, $location, $rootScope) {
    $scope.ftps = [{userName: "admin", password: "password", url: "google.com", portNo: "8084"}]

    $scope.showSettings = true;

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

        });
    });

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


  
});