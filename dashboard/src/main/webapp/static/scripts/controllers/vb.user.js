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
            $scope.userAgencyDetails.push(val.agencyId.agencyName)
            $scope.userAgency = unique($scope.userAgencyDetails);
            console.log($scope.userAgency)
        });
            //$scope.editUser(response[0], 0);
        });

        $http.get('admin/user/account').success(function (response) {
            $scope.accounts = response;
        });
    }

    getUser();
    $scope.searchuserDetails=function(agencyUserName){
        $scope.agencyListName=agencyUserName;
    }
    $scope.saveUser = function (user) {
        var userData = {
            id: user.id,
            firstName: user.firstName,
            lastName: user.lastName,
            userName: user.userName,
            email: user.email,
            password: user.password,
            primaryPhone: user.primaryPhone,
            secondaryPhone: user.secondaryPhone
        };
        $http({method: user.id ? 'PUT' : 'POST', url: 'admin/ui/user', data: userData}).success(function (response) {
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
        userAccount(user);
        $scope.userId = user;
        var data = {
            id: user.id,
            firstName: user.firstName,
            lastName: user.lastName,
            userName: user.userName,
            email: user.email,
            password: user.password,
            primaryPhone: user.primaryPhone,
            secondaryPhone: user.secondaryPhone
        };
        $scope.user = data;
        $scope.selectedUser = index;
    };

    $scope.deleteUser = function (user, index) {
        $http({method: 'DELETE', url: 'admin/ui/user/' + user.id}).success(function (response) {
            $scope.users.splice(index, 1);
        });
    };

    function userAccount(user) {
        $http.get('admin/ui/userAccount/' + user.id).success(function (response) {
            $scope.userAccounts = response;
        });
        $http.get('admin/ui/userPermission/' + user.id).success(function (response) {
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
//            userAccount(currentUserId);
        });
    };

    $scope.deleteUserAccount = function (userAccount, index) {
        if (userAccount.id) {
            $http({method: 'DELETE', url: 'admin/ui/userAccount/' + userAccount.id}).success(function (response) {
                $scope.userAccounts.splice(index, 1);
//                $('.modal-backdrop').remove();
            });
        } else {
            $scope.userAccounts.splice(index, 1);
//            $('.modal-backdrop').remove();
        }
    };

    $scope.testClick = function (permission) {
        $scope.saveUserPermission(permission);
    };
    
    $scope.saveUserPermission = function (permission) {
        var userPermissionId = $scope.hasData(permission.permissionName).id;

        var currentUserId = $scope.userId;
        var data = {
            id: $scope.hasData(permission.permissionName).id,
            permissionId: permission.id,
            userId: currentUserId.id,
            status: permission.status ? 1 : 0
        };
        $http({method: userPermissionId ? 'PUT' : 'POST', url: 'admin/ui/userPermission', data: data}).success(function (response) {
            userAccount(currentUserId);
        });
    };
});