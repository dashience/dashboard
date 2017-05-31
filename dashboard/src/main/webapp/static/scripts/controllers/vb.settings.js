app.controller('SettingsController', function ($scope, $cookies, $http, $filter, $stateParams, $state, $location, $rootScope) {
    $scope.ftps = [{userName: "admin", password: "password", url: "google.com", portNo: "8084"}]

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
    
    $scope.getGroupName = function(name, list){
        $scope.fileLists = []
        console.log(name)
        console.log(list)
        var groupList = list;
        groupList.forEach(function(value, key){
            if(value.groupName === name){
                value.isEdit = false;
                $scope.fileLists.push(value);
            }
        })
    }
    
    
    $scope.save = function (fileList) {
        console.log(fileList)

        var data = {
            id: fileList.id,
            propertyName: fileList.propertyName,
            propertyValue: fileList.propertyValue,
            propertyType: fileList.propertyType,
            groupName: fileList.groupName,
            remarks: fileList.remarks
        };


        $http({method: fileList.id ? 'PUT' : 'POST', url: "admin/settings", data: data})


//        $http({method: 'POST', url: "../../admin/ui", data: ftp}).success(function (response) {
////        $scope.file = response.data;
//        });
    };
});