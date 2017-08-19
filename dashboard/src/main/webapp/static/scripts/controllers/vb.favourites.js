app.controller('FavouritesController', function($http,$scope, $stateParams,$cookies,$translate){
    $scope.accountId = $stateParams.accountId;
    $scope.accountName = $stateParams.accountName;
    $scope.startDate = $stateParams.startDate;
    $scope.endDate = $stateParams.endDate;
    
    $scope.favourites=[];
     $http.get("admin/tag").success(function (response) {
     $scope.favourites=response;
     })    
     
      //Chinese Translation
    
    $scope.agencyLanguage = $cookies.getObject("agencyLanguage");

    console.log($scope.agencyLanguage);

    var lan = $scope.agencyLanguage ? $scope.agencyLanguage : null;
    changeLanguage(lan);

    function changeLanguage(key) {
        $translate.use(key);
    }
})