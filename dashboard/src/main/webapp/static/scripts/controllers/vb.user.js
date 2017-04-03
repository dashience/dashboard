app.controller('UserController', function ($scope, $http, localStorageService) {
    $scope.permission = localStorageService.get("permission");
    $scope.users = [];

    //Tabs
    $scope.tab = 1;

    $scope.setTabUser = function (newTab) {
        $scope.tab = newTab;
    };

    $scope.isSetUser = function (tabNum) {
        return $scope.tab === tabNum;
    };

    var unique = function (origArr) {
        var newArr = [],
                origLen = origArr.length,
                found, x, y;

        for (x = 0; x < origLen; x++) {
            found = undefined;
            for (y = 0; y < newArr.length; y++) {
                if (origArr[x] === newArr[y]) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                newArr.push(origArr[x]);
            }
        }
        return newArr;
    }
    function getUser() {
        $http.get('admin/ui/user').success(function (response) {
            $scope.userAgencyDetails = [];
            $scope.users = response;
            angular.forEach($scope.users, function (val, key) {
                console.log(val)
                if(!val.agencyId){
                    return;
                }
                $scope.userAgencyDetails.push(val.agencyId.agencyName)
                $scope.userAgency = unique($scope.userAgencyDetails);
            });
        });

        $http.get('admin/user/account').success(function (response) {
            $scope.accounts = response;
        });
    }

    getUser();
    $scope.searchuserDetails = function (agencyUserName) {
        $scope.agencyListName = agencyUserName;
    }
    $scope.saveUser = function (user) {
//        var userData = {
//            id: user.id,
//            firstName: user.firstName,
//            lastName: user.lastName,
//            userName: user.userName,
//            email: user.email,
//            password: user.password,
//            primaryPhone: user.primaryPhone,
//            secondaryPhone: user.secondaryPhone,
//            agencyId: user.agencyId.id?user.agencyId.id:''
//        };
        $http({method: user.id ? 'PUT' : 'POST', url: 'admin/ui/user', data: user}).success(function (response) {
            getUser();
        });
        $scope.user = "";
    };

    $scope.clearUser = function () {
        $scope.user = "";
    };

    $scope.addUser = function () {
        $scope.user = '';
    };

    $scope.selectedUser = null;
    $scope.editUser = function (user, index) {
        getUserAccount(user);
        $scope.userId = user;
        var data = {
            id: user.id,
            firstName: user.firstName,
            lastName: user.lastName,
            userName: user.userName,
            email: user.email,
            password: user.password,
            primaryPhone: user.primaryPhone,
            secondaryPhone: user.secondaryPhone,
            agencyId: user.agencyId
        };
        $scope.user = data;
        $scope.selectedUser = index;
    };

    $scope.deleteUser = function (user, index) {
        $http({method: 'DELETE', url: 'admin/ui/user/' + user.id}).success(function (response) {
            $scope.users.splice(index, 1);
        });
    };

    function getUserAccount(user) {
        $http.get('admin/ui/userAccount/' + user.id).success(function (response) {
            $scope.userAccounts = response;
        });
        $http.get('admin/ui/userPermission/' + user.id).success(function (response) {
            console.log(response)
            $scope.userPermissions = response;
            $http.get('admin/ui/permission').success(function (response1) {
                $scope.permissions = response1;
                angular.forEach($scope.permissions, function (permission) {
                    $scope.hasPermission(permission);
                });
            });
        });
    }

    $scope.hasPermission = function (permission) {
        for (var i = 0; i < $scope.userPermissions.length; i++) {
            if ($scope.userPermissions[i].permissionId.permissionName === permission.permissionName) {
                permission.status = ($scope.userPermissions[i].status === 1 || $scope.userPermissions[i].status === true) ? 1 : 0;
                return ($scope.userPermissions[i].status === 1 || $scope.userPermissions[i].status === true) ? true : false;
            }
        }
        permission.status = 0;
        return false;
    };

    $scope.hasData = function (permissionName) {
        for (var i = 0; i < $scope.userPermissions.length; i++) {
            if ($scope.userPermissions[i].permissionId.permissionName === permissionName) {
                var data = {
                    status: true,
                    id: $scope.userPermissions[i].id
                };
                return data;
            }
        }
        return false;
    };

    $scope.userAccounts = [];
    $scope.addUserAccount = function () {
        $scope.userAccounts.push({isEdit: true});
    };

    $scope.saveUserAccount = function (userAccount) {
        var currentUserId = $scope.userId;
        var data = {
            id: userAccount.id,
            accountId: userAccount.accountId.id,
            userId: currentUserId.id
        };
        $http({method: userAccount.id ? 'PUT' : 'POST', url: 'admin/ui/userAccount', data: data}).success(function (response) {
            getUserAccount(currentUserId)
           //$scope.userAccounts = response.id;
//            userAccount(currentUserId);
        });
    };

    $scope.deleteUserAccount = function (userAccount, index) {
        //if (userAccount.id) {
            $http({method: 'DELETE', url: 'admin/ui/userAccount/' + userAccount.id}).success(function (response) {
                
                $scope.userAccounts.splice(index, 1);
                //$scope.userAccounts = response;
            });
       // } else {
        //$scope.userAccounts.splice(index, 1);
        //}
    };
    
    $scope.removeUserAccount = function(index){
        $scope.userAccounts.splice(index, 1);
    }

    $scope.setUserPermission = function (permission) {
        $scope.saveUserPermission(permission);
    };

    $scope.saveUserPermission = function (permission) {
        var userPermissionId = $scope.hasData(permission.permissionName).id;

        var currentUserId = $scope.userId;
        var data = {
            id: $scope.hasData(permission.permissionName).id,
            permissionId: permission.id,
            userId: currentUserId.id,
            status: permission.status
        };
        console.log(data)
        $http({method: userPermissionId ? 'PUT' : 'POST', url: 'admin/ui/userPermission', data: data}).success(function (response) {
            getUserAccount(currentUserId);
        });
    };
});
app.directive("showPassword", function () {
    return {
        restrict: "EA",
        link: function (scope, element, attrs) {
            element.on('click', function () {
                var inputType = angular.element(angular.element(element[0]).parent().siblings()[0]).attr("type");
                if (inputType != null && inputType.toLowerCase() == "password") {
                    angular.element(angular.element(element[0]).parent().siblings()[0]).attr("type", "text");
                } else {
                    angular.element(angular.element(element[0]).parent().siblings()[0]).attr("type", "password");
                }
            })
        }
    }
});