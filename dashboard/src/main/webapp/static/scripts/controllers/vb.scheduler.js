app.controller("SchedulerController", function ($scope, $http, localStorageService, $stateParams) {
    $scope.permission = localStorageService.get("permission");
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
//    $scope.schedularHistoryData = false;
    $scope.showSchedulerHistory = function (scheduler) {
        $scope.schedularHistoryDetails = [];
        $http({method: 'GET', url: 'admin/scheduler/schedulerHistory/' + scheduler.id}).success(function (response) {
//            if (!response) {
//                $scope.schedularData = "History Not Found";
//                $scope.schedularHistoryData = true;
//            } else {
            $scope.schedularHistoryDetails = response;
//            }
        });
    };

    $scope.saveSchedulerStatus = function (scheduler) {
        console.log(scheduler)
        $http({method: scheduler.id ? 'PUT' : 'POST', url: 'admin/scheduler/schedulerStatus/enableOrDisable', data: scheduler}).success(function (response) {
            console.log(response)
        });
    }

    $scope.tableRowExpanded = false;
    $scope.tableRowIndexExpandedCurr = "";
    $scope.tableRowIndexExpandedPrev = "";
    $scope.storeIdExpanded = "";

    $scope.dayDataCollapseFn = function () {
        $scope.dayDataCollapse = [];
        for (var i = 0; i < $scope.schedularHistoryDetails.length; i += 1) {
            $scope.dayDataCollapse.push(false);
        }
    };

    $scope.selectTableRow = function (index, storeId) {
        if (typeof $scope.dayDataCollapse === 'undefined') {
            $scope.dayDataCollapseFn();
        }

        if ($scope.tableRowExpanded === false && $scope.tableRowIndexExpandedCurr === "" && $scope.storeIdExpanded === "") {
            $scope.tableRowIndexExpandedPrev = "";
            $scope.tableRowExpanded = true;
            $scope.tableRowIndexExpandedCurr = index;
            $scope.storeIdExpanded = storeId;
            $scope.dayDataCollapse[index] = true;
        } else if ($scope.tableRowExpanded === true) {
            if ($scope.tableRowIndexExpandedCurr === index && $scope.storeIdExpanded === storeId) {
                $scope.tableRowExpanded = false;
                $scope.tableRowIndexExpandedCurr = "";
                $scope.storeIdExpanded = "";
                $scope.dayDataCollapse[index] = false;
            } else {
                $scope.tableRowIndexExpandedPrev = $scope.tableRowIndexExpandedCurr;
                $scope.tableRowIndexExpandedCurr = index;
                $scope.storeIdExpanded = storeId;
                $scope.dayDataCollapse[$scope.tableRowIndexExpandedPrev] = false;
                $scope.dayDataCollapse[$scope.tableRowIndexExpandedCurr] = true;
            }
        }

    };
});