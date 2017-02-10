app.controller("SchedulerController", function ($scope, $http, $stateParams) {
    $scope.locationId = $stateParams.locationId;
    $scope.startDate = $stateParams.startDate;
    $scope.endDate = $stateParams.endDate;
    $scope.schedulers = [{name: "Test", startDate: "22-7-2016", endDate: "22-7-2016"}];
});