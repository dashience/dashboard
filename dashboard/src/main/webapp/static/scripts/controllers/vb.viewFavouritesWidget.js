app.controller('ViewFavouritesWidgetController', function ($http, $scope, $stateParams) {
    $scope.favouriteId = $stateParams.favouriteId;
    $scope.favouritesWidgets = [];
    $http.get("admin/tag/widgetTag/" + $stateParams.favouriteName).success(function (response) {

        $scope.favouritesWidgets = response;
        console.log(response)
        
    })
})