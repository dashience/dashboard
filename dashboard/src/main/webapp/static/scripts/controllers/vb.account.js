app.controller('AccountController', function ($scope, $http, $state, $stateParams) {
    $scope.$state = $state;
    $scope.location = $stateParams.locationId;
    $scope.startDate = $stateParams.startDate;
    $scope.endDate = $stateParams.endDate;
    $scope.accountId = $stateParams.accountId;

//Tabs
    $scope.tab = 1;

    $scope.setTab = function (newTab) {
        $scope.tab = newTab;
    };

    $scope.isSet = function (tabNum) {
        return $scope.tab === tabNum;
    };

    $scope.setAccountId = function (account) {
        $stateParams.accountId = account.id;
    };
    
    function getAccountProperty(account){
        $http.get('admin/user/property/' + account.id).success(function (response) {
            $scope.properties = response;
        });
        $http.get('admin/user/accountUser/' + account.id).success(function (response) {
            $scope.accountUsers = response;
        });
        $http.get('admin/ui/user').success(function (response) {
            $scope.users = response;
        });
    }

    $scope.accounts = [];

    function getAccount() {
        $http.get('admin/user/account').success(function (response) {
            $scope.accounts = response;
            $scope.editAccount($scope.accounts[0], 0);
        });
    }
    getAccount();

    $scope.saveAccount = function (account) {
        $http({method: account.id ? 'PUT' : 'POST', url: 'admin/user/account', data: account}).success(function (response) {
            getAccount();
        });

        $scope.account = "";
    };

    $scope.selectedRow = null;
    $scope.editAccount = function (account, index) {
        getAccountProperty(account)
        $scope.accountUserId = account;
        var data = {
            id: account.id,
            accountName: account.accountName,
            geoLocation: account.geoLocation,
            description: account.description
        };
        $scope.account = data;        
        $scope.selectedRow = index;
    };

    $scope.clearAccount = function () {
        $scope.account = "";
    };

    $scope.deleteAccount = function (account, index) {
        $http({method: 'DELETE', url: 'admin/user/account/' + account.id}).success(function (response) {
            $scope.accounts.splice(index, 1);
        });
    };


    $scope.properties = [];
    $scope.addProperty = function(){
         $scope.property = "";
        $scope.showEditPage = true;
    };
    
    $scope.saveProperty = function (property) {
       var accountId = $scope.accountUserId;
        $http({method: property.id ? 'PUT' : 'POST', url: 'admin/user/property', data: property}).success(function (response) {
            getAccountProperty(accountId);
        });

        $scope.property = "";
        $scope.showEditPage = false;
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
    
    
    $scope.addAccountUser = function(){
        $scope.showAccountUserEditPage = true;
        $scope.accountUser = "";
    }

    $scope.saveAccountUser = function (accountUser) {
        console.log(accountUser)
        console.log($scope.accountUserId)
        var account = $scope.accountUserId;
        $http({method: accountUser.id ? 'PUT' : 'POST', url: 'admin/user/accountUser', data: accountUser}).success(function (response) {
            getAccountProperty(account);
        });

        $scope.accountUser = "";
        $scope.showAccountUserEditPage = false;
    };

    $scope.editAccountUser = function (accountUser) {
        var data = {
            id: accountUser.id,
            accountId: accountUser.accountId,
            userId: accountUser.userId,
            status: accountUser.status
        };
        $scope.accountUser = data;
        $scope.showAccountUserEditPage = true;
    };

    $scope.selectedAccountUser = null;
    $scope.setAccountUserRow = function (index) {
        $scope.selectedAccountUser = index;
    };

    $scope.deleteAccountUser = function (accountUser, index) {
        $http({method: 'DELETE', url: 'admin/user/accountUser/' + accountUser.id}).success(function (response) {
            $scope.accountUsers.splice(index, 1);
        });
    };

    $scope.clearAccountUser = function (accountUser) {
        $scope.accountUser = "";
        $scope.showAccountUserEditPage = false;
    };
});