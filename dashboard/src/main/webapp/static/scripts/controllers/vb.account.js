app.controller('AccountController', function ($scope, $http) {

    $scope.accounts = [];

    function getAccount() {
        $http.get('admin/user/account').success(function (response) {
            $scope.accounts = response;
        });
    }
    getAccount();
    $scope.saveAccount = function (account) {
        $http({method: account.id ? 'PUT' : 'POST', url: 'admin/user/account', data: account}).success(function (response) {
            getAccount();
        });

        $scope.account = "";
    };

    $scope.editAccount = function (account) {
        var data = {
            id: account.id,
            accountName: account.accountName,
            geoLocation: account.geoLocation,
            description: account.description
        };
        $scope.account = data;
    };

    $scope.selectedRow = null;
    $scope.setClickedRow = function (index) {
        $scope.selectedRow = index;
    };

    $scope.clearAccount = function () {
        $scope.account = "";
    };

    $scope.deleteAccount = function (account, index) {
        $http({method: 'DELETE', url: 'admin/user/account/' + account.id}).success(function (response) {
            $scope.accounts.splice(index, 1)
        })
    };
});