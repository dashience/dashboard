app.controller('ReportPdfController', function ($stateParams, $http, $scope, $filter, $cookies, $translate, localStorageService) {

    $scope.reportPdfStartDate = $filter('date')(new Date($stateParams.startDate), 'MMM dd yyyy');//$filter(new Date($stateParams.startDate, 'MM/dd/yyyy'));
    $scope.reportPdfEndDate = $filter('date')(new Date($stateParams.endDate), 'MMM dd yyyy'); //$filter(new Date($stateParams.endDate, 'MM/dd/yyyy'));
    console.log("userId>>>>>>>>" + $stateParams.userId);

    $scope.agencyLanguage = $stateParams.lan;//$cookies.getObject("agencyLanguage");

    var lan = $scope.agencyLanguage;
    changeLanguage(lan);

    function changeLanguage(key) {
        $translate.use(key);
    }
//    $scope.getTableType = localStorageService.get("selectedTableType");
    $scope.getTableType = $stateParams.getTableType;
//    var compareStartDate = localStorageService.get("comparisonStartDate");
//    var compareEndDate = localStorageService.get("comparisonEndDate");
    var compareStartDate = $stateParams.compareStartDate;
    var compareEndDate = $stateParams.compareEndDate;
    $scope.compareDateRange = {
        startDate: compareStartDate,
        endDate: compareEndDate
    };
    $http.get('admin/ui/getAccount/' + $stateParams.accountId).success(function (response) {
        console.log(response);
        response.forEach(function (val, key) {
            $scope.userAccountName = val.accountName;
            $scope.userAccountLogo = val.logo;
        });
    });

    $http.get("admin/report/" + $stateParams.reportId).success(function (response) {

        $scope.reportPdfTitle = response.reportTitle;
        $scope.reportPdfDescription = response.description
        $scope.pdfLogo = response.logo;
    });

    $http.get('admin/report/reportWidget/' + $stateParams.reportId).success(function (response) {
        var widgetItems = response;
        $http.get("admin/ui/getChartColorByUserId/" + $stateParams.userId).success(function (response) {
            $scope.userChartColors = response;
            var widgetColors;
            if (response.optionValue) {
                widgetColors = response.optionValue.split(',');
            }
            widgetItems.forEach(function (value, key) {
                value.widgetId.chartColors = widgetColors;
                if (value.widgetId.chartType == 'horizontalBar') {
                    value.widgetId.isHorizontalBar = true;
                } else {
                    value.widgetId.isHorizontalBar = false;
                }
                angular.forEach(value.widgetId.columns, function (value, key) {
                    value.expand = true;
                });
            });
            $scope.reportWidgets = widgetItems;
        }).error(function () {
            $scope.reportWidgets = widgetItems;
        });
        setInterval(function () {
            window.status = "done";
        }, 15000);
    });
    $scope.downloadUiPdf = function () {
        console.log("PDF Encode URL -->" + encodeURIComponent(window.location.href));
        console.log("URL -->" + window.location.href);
        window.open("admin/pdf/download?windowStatus=done&url=" + encodeURIComponent(window.location.href));
    };
});