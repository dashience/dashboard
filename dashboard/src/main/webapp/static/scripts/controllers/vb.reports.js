app.controller("ReportController", function ($scope, $http, $stateParams, $state, localStorageService, $window, $cookies, $translate) {
    $scope.permission = localStorageService.get("permission");
    console.log($scope.permission)
    $scope.startDate = $stateParams.startDate;
    $scope.endDate = $stateParams.endDate;
    $scope.reportId = $stateParams.reportId;
    $scope.accountId = $stateParams.accountId;
    $scope.accountName = $stateParams.accountName;
    $scope.reportWidgets = [];
    if ($scope.permission.scheduleReport === true) {
        $scope.showSchedulerReport = true;
    } else {
        $scope.showSchedulerReport = false;
    }

   
    $scope.agencyLanguage = $stateParams.lan;//$cookies.getObject("agencyLanguage");
    
    var lan = $scope.agencyLanguage;
    changeLanguage(lan);

    function changeLanguage(key) {
        $translate.use(key);
    }

    $scope.schedulerRepeats = ["Now", "Once", "Daily", "Weekly", "Monthly"];
//    $scope.schedulerRepeats = ["Now", "Once", "Daily", "Weekly", "Monthly", "Yearly", "Year Of Week"];
    $scope.weeks = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];

    function getWeeks(d) {
        var first = new Date(d.getFullYear(), 0, 1);
        var dayms = 1000 * 60 * 60 * 24;
        var numday = ((d - first) / dayms);
        var weeks = Math.ceil((numday + first.getDay() + 1) / 7);
        return weeks;
    }
    function getLastDayOfYear(datex)
    {
        var year = datex.getFullYear();
        var month = 12;
        var day = 31;
        return month + "/" + day + "/" + year;
    }
    var endOfYearDate = getLastDayOfYear(new Date());
    var endOfYear = getWeeks(new Date(endOfYearDate))
    $scope.totalYearOfWeeks = [];
    for (var i = 1; i <= endOfYear; i++) {
        $scope.totalYearOfWeeks.push(i);
    }

    $http.get("admin/report/getReport").success(function (response) {
        $scope.reports = response;
    });

    $scope.addReportToScheduler = function (scheduler, report) {
        if (scheduler.schedulerRepeatType === "Now") {
            scheduler.schedulerNow = new Date();
        }
        scheduler.reportId = report.id;
        $http({method: scheduler.id ? 'PUT' : 'POST', url: 'admin/scheduler/scheduler', data: scheduler}).success(function (response) {
        });
        // $scope.scheduler = "";
    };

    $scope.deleteReport = function (report, index) {
        $http({method: 'DELETE', url: 'admin/report/' + report.id}).success(function (response) {
            $scope.reports.splice(index, 1);
        });
    }

    $scope.downloadReportPdf = function (report) {
        var url = "admin/proxy/downloadReport/" + report.id + "?dealerId=" + $stateParams.accountId + "&location=" + $stateParams.accountId + "&accountId=" + $stateParams.accountId + "&startDate=" + $stateParams.startDate + "&endDate=" + $stateParams.endDate + "&exportType=pdf";
        $window.open(url);
    }

    $scope.goScheduler = function () {
        $state.go("index.schedulerIndex.scheduler", {
            accountId: $stateParams.accountId,
            accountName: $stateParams.accountName,
            startDate: $stateParams.startDate,
            endDate: $stateParams.endDate
        });
    };
});
