app.controller("SchedulerController", function ($scope, $http, $stateParams) {
    $scope.locationId = $stateParams.locationId;
    $scope.startDate = $stateParams.startDate;
    $scope.endDate = $stateParams.endDate;
    $scope.schedulers = [
        {name: "Test 1", startDate: "22-7-2016", endDate: "22-7-2016"},
        {name: "Test 2", startDate: "22-7-2016", endDate: "22-7-2016"},
        {name: "Test 3", startDate: "22-7-2016", endDate: "22-7-2016"},
        {name: "Test 4", startDate: "22-7-2016", endDate: "22-7-2016"},
        {name: "Test 5", startDate: "22-7-2016", endDate: "22-7-2016"},
        {name: "Test 6", startDate: "22-7-2016", endDate: "22-7-2016"}];    
});