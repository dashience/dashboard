app.controller('AccountController', function ($scope, $http, $state, $stateParams, localStorageService,$translate,$cookies) {
    $scope.permission = localStorageService.get("permission");
    $scope.$state = $state;
    $scope.accounts = [];
    $scope.properties = [];
    $scope.accountUsers = [];
//Tabs
    $scope.tab = 1;

    $scope.setTab = function (newTab) {
        $scope.tab = newTab;
    };

    $scope.isSet = function (tabNum) {
        return $scope.tab === tabNum;
    };
    
    $scope.agencyLanguage = $stateParams.lan;//$cookies.getObject("agencyLanguage");
    
    var lan = $scope.agencyLanguage;
    changeLanguage(lan);

    function changeLanguage(key) {
        $translate.use(key);
    }

    $scope.account = {logo: "static/img/logos/deeta-logo.png"}; //Logo Upload
    $scope.imageUpload = function (event) {
        var files = event.target.files;
        for (var i = 0; i < files.length; i++) {
            var file = files[i];
            var reader = new FileReader();
            reader.onload = $scope.imageIsLoaded;
            reader.readAsDataURL(file);
        }
    };
    $scope.imageIsLoaded = function (e) {
        $scope.$apply(function () {
            $scope.account.logo = e.target.result;
        });
    };

    function getAccountProperty(account) {
        $http.get('admin/user/property/' + account.id).success(function (response) {
            $scope.properties = response;
            console.log(response)
        });

        $http.get('admin/user/userAccount/' + account.id).success(function (response) {
            $scope.accountUsers = response;
            console.log(response)
        });
    }
    $http.get('admin/ui/user').success(function (response) {
        $scope.users = response;
    });

    $scope.addAccount = function () {
        $scope.account = "";
    };

    function getAccount() {
        $http.get('admin/user/account').success(function (response) {
            $scope.accounts = response;
//            $scope.editAccount($scope.accounts[0], 0);
        });
    }
    getAccount();

    $scope.saveAccount = function (account) {
        if ($scope.account.logo === "static/img/logos/deeta-logo.png") {
            $scope.account.logo = "";
        }
        console.log(account)
        account.logo = $scope.account.logo;
        $http({method: account.id ? 'PUT' : 'POST', url: 'admin/user/account', data: account}).success(function (response) {
            getAccount();
            if (response.status == true) {
                $scope.account = {logo: "static/img/logos/deeta-logo.png"};
            } else {
                if (!response.message) {
                    $scope.account = "";
                    $scope.account = {logo: "static/img/logos/deeta-logo.png"};
                    $scope.selectedRow = null;
                    return;
                }
                var dialog = bootbox.dialog({
                    title: 'Alert',
                    message: response.message
                });
                dialog.init(function () {
                    setTimeout(function () {
                        dialog.modal('hide');
                    }, 2000);
                });
            }
        });

    };

    $scope.selectedRow = null;
    $scope.editAccount = function (account, index) {
        getAccountProperty(account)
        $scope.accountUserId = account;
        var data = {
            id: account.id,
            accountName: account.accountName,
            geoLocation: account.geoLocation,
            description: account.description,
            agencyId: account.agencyId,
            logo: account.logo ? account.logo : "static/img/logos/deeta-logo.png"
        };
        $scope.account = data;
        $scope.selectedRow = index;
        $scope.showEditPage = false;
    };

    $scope.clearAccount = function () {
        $scope.account = {logo: "static/img/logos/deeta-logo.png"};
    };

    $scope.deleteAccount = function (account, index) {
        $http({method: 'DELETE', url: 'admin/user/account/' + account.id}).success(function (response) {
            $scope.accounts.splice(index, 1);
        });
    };

    $scope.addProperty = function () {
        $scope.property = "";
        $scope.showEditPage = true;
    };

    $scope.saveProperty = function (property) {
        console.log("************** ACCOUNT PROPERTY ****************");
        console.log(property);
        var accountId = $scope.accountUserId;
        console.log($scope.accountUserId);
        console.log(accountId);
        if (!accountId) {
            var dialog = bootbox.dialog({
                title: 'Alert',
                message: '<p>Select Account</p>'
            });
            dialog.init(function () {
                setTimeout(function () {
                    dialog.modal('hide');
                }, 2000);
            });
        } else {
            var data = {
                id: property.id,
                propertyName: property.propertyName,
                accountId: accountId.id, //property.accountId,
                propertyValue: property.propertyValue,
                propertyRemark: property.propertyRemark
            };

            $http({method: property.id ? 'PUT' : 'POST', url: 'admin/user/property', data: data}).success(function (response) {
                getAccountProperty(accountId);
            });

            $scope.property = "";
            $scope.showEditPage = false;
        }
    };

    $scope.editProperty = function (property) {
        var data = {
            id: property.id,
            propertyName: property.propertyName,
            accountId: property.accountId,
            propertyValue: property.propertyValue,
            propertyRemark: property.propertyRemark
        };
        $scope.property = data;
        $scope.showEditPage = true;
    };

    $scope.selectedRows = null;
    $scope.setPropertyRow = function (index) {
        $scope.selectedRows = index;
    };

    $scope.clearProperty = function (property) {
        $scope.property = "";
        $scope.showEditPage = false;
    };

    $scope.deleteProperty = function (property, index) {
        $http({method: 'DELETE', url: 'admin/user/property/' + property.id}).success(function (response) {
            $scope.properties.splice(index, 1);
        });
    };

    $scope.addAccountUser = function () {
        $scope.accountUsers.push({isEdit: true});
    };

    $scope.saveStatus = function (accountUser) {
        var account = $scope.accountUserId;
        var data = {
            id: accountUser.id,
            accountId: account.id,
            userId: accountUser.userId,
            status: accountUser.status
        };
        if (accountUser.id) {
            $http({method: 'PUT', url: 'admin/user/userAccount', data: data}).success(function (response) {
            });
        }
    };

    $scope.saveAccountUser = function (accountUser) {
        var account = $scope.accountUserId;
        if (!account) {
            var dialog = bootbox.dialog({
                title: 'Alert',
                message: '<p>Select Account</p>'
            });
            dialog.init(function () {
                setTimeout(function () {
                    dialog.modal('hide');
                }, 2000);
            });
        } else {
            var data = {
                id: accountUser.id,
                accountId: account.id, //accountUser.accountId,
                userId: accountUser.userId,
                status: accountUser.status
            };
            $http({method: accountUser.id ? 'PUT' : 'POST', url: 'admin/user/userAccount', data: data}).success(function (response) {
                getAccountProperty(account);
            });
            $scope.accountUser = "";
            accountUser.isEdit = false;
        }
    };

    $scope.editAccountUser = function (accountUser) {
        var data = {
            id: accountUser.id,
            accountId: accountUser.accountId,
            userId: accountUser.userId,
            status: accountUser.status
        };
        $scope.accountUser = data;
    };

    $scope.selectedAccountUser = null;
    $scope.setAccountUserRow = function (index) {
        $scope.selectedAccountUser = index;
    };

    $scope.deleteAccountUser = function (accountUser, index) {
        $http({method: 'DELETE', url: 'admin/user/userAccount/' + accountUser.id}).success(function (response) {
            $scope.accountUsers.splice(index, 1);
        });
    };

    $scope.removeAccountUser = function (index) {
        $scope.accountUsers.splice(index, 1);
    };

    $scope.clearAccountUser = function (accountUser) {
        $scope.accountUser = "";
        $scope.showAccountUserEditPage = false;
    };
});
