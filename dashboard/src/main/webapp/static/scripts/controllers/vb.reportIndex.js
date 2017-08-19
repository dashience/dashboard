app.controller('ReportIndexController', function ($scope, $stateParams, $state,$cookies,$translate) {    
    $scope.$state = $state;//Find Active Tabs
    $scope.productId = $stateParams.productId ? $stateParams.productId : 0;
    $scope.reportId = $stateParams.reportId ? $stateParams.reportId : 0;
    $scope.startDate = $stateParams.startDate;
    $scope.endDate = $stateParams.endDate;
    $scope.locationId = $stateParams.locationId;
    
    //Chinese Translation
    
    $scope.agencyLanguage = $cookies.getObject("agencyLanguage");

    console.log($scope.agencyLanguage);

    var lan = $scope.agencyLanguage ? $scope.agencyLanguage : null;
    changeLanguage(lan);

    function changeLanguage(key) {
        $translate.use(key);
    }
});