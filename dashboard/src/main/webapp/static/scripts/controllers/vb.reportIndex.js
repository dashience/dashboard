app.controller('ReportIndexController', function ($scope, $stateParams, $state) {    
    $scope.$state = $state;//Find Active Tabs
    $scope.productId = $stateParams.productId ? $stateParams.productId : 0;
    $scope.reportId = $stateParams.reportId ? $stateParams.reportId : 0;
    $scope.startDate = $stateParams.startDate;
    $scope.endDate = $stateParams.endDate;
    $scope.locationId = $stateParams.locationId;
    $scope.getTableType = $stateParams.compareStatus ? $stateParams.compareStatus : "compareOff";
    $scope.compareDateRange = {
        startDate: $stateParams.compareStartDate,
        endDate: $stateParams.compareEndDate
    }
    $scope.compareStartDate = $scope.compareDateRange.startDate;
    $scope.compareEndDate = $scope.compareDateRange.endDate;
});