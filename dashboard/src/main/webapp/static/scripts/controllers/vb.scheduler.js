app.controller("SchedulerController", function ($scope, $http, $stateParams) {
    $scope.accountId = $stateParams.accountId;
    $scope.accountName = $stateParams.accountName;
    $scope.startDate = $stateParams.startDate;
    $scope.endDate = $stateParams.endDate;
    $http.get("admin/scheduler/scheduler").success(function (response) {
        $scope.schedulers = response;
    });
    $scope.deleteScheduler = function (scheduler, index) {
        $http({method: 'DELETE', url: 'admin/scheduler/scheduler/' + scheduler.id}).success(function (response) {
            $scope.schedulers.splice(index, 1);
        });
    };
    $scope.showSchedulerHistory = function (scheduler) {
        console.log(scheduler);
        $http({method: 'GET', url: 'admin/scheduler/schedulerHistory/' + scheduler.id}).success(function (response) {
            $scope.schedularHistoryDetails = response;
            if (!response) {
                $scope.schedulerData = "Data Not Found";
                //$scope.showTableRow[scheduler.id] = true;
            }
        });
    };
    //    $scope.schedulers = [
//        {name: "Test 1", startDate: "22-7-2016", endDate: "22-7-2016"},
//        {name: "Test 2", startDate: "22-7-2016", endDate: "22-7-2016"},
//        {name: "Test 3", startDate: "22-7-2016", endDate: "22-7-2016"},
//        {name: "Test 4", startDate: "22-7-2016", endDate: "22-7-2016"},
//        {name: "Test 5", startDate: "22-7-2016", endDate: "22-7-2016"},
//        {name: "Test 6", startDate: "22-7-2016", endDate: "22-7-2016"}];    
});