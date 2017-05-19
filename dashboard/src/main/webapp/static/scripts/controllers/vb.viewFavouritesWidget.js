app.controller('ViewFavouritesWidgetController', function ($http, $scope, $stateParams) {
    $scope.accountId = $stateParams.accountId;
    $scope.accountName = $stateParams.accountName;
    $scope.productId = $stateParams.productId;
    $scope.startDate = $stateParams.startDate;
    $scope.endDate = $stateParams.endDate;
    $scope.favouriteId = $stateParams.favouriteId;
    $scope.favouritesWidgets = [];
    $http.get("admin/tag/widgetTag/" + $stateParams.favouriteName).success(function (response) {

        $scope.favouritesWidgets = response;
        console.log(response)

    })
})