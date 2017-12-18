app.controller('FavouritesController', function($http,$scope, $stateParams){
    $scope.accountId = $stateParams.accountId;
    $scope.accountName = $stateParams.accountName;
    $scope.startDate = $stateParams.startDate;
    $scope.endDate = $stateParams.endDate;
    $scope.getTableType = $stateParams.compareStatus ? $stateParams.compareStatus : "compareOff";
    $scope.compareDateRange = {
        startDate: $stateParams.compareStartDate,
        endDate: $stateParams.compareEndDate
    }
    $scope.compareStartDate = $scope.compareDateRange.startDate;
    $scope.compareEndDate = $scope.compareDateRange.endDate;
    $scope.favourites=[];
     $http.get("admin/tag").success(function (response) {
     $scope.favourites=response;
     })    
})