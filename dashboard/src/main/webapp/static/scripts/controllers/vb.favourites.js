app.controller('FavouritesController', function ($http, $scope, $stateParams, $cookies, $translate) {
    $scope.accountId = $stateParams.accountId;
    $scope.accountName = $stateParams.accountName;
    $scope.startDate = $stateParams.startDate;
    $scope.endDate = $stateParams.endDate;

    $scope.favourites = [];
    $http.get("admin/tag").success(function (response) {
        $scope.favourites = response;
    })

    $scope.agencyLanguage = $stateParams.lan;//$cookies.getObject("agencyLanguage");

    var lan = $scope.agencyLanguage;
    changeLanguage(lan);

    function changeLanguage(key) {
        $translate.use(key);
    }
})
