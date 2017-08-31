app.controller('ReportPdfController', function ($stateParams, $http, $scope, $filter,localStorageService,$translate) {

    $scope.reportPdfStartDate = $filter('date')(new Date($stateParams.startDate), 'MMM dd yyyy');//$filter(new Date($stateParams.startDate, 'MM/dd/yyyy'));
    $scope.reportPdfEndDate = $filter('date')(new Date($stateParams.endDate), 'MMM dd yyyy'); //$filter(new Date($stateParams.endDate, 'MM/dd/yyyy'));


     console.log(localStorageService.get('lan'));
   
    $scope.agencyLanguage=localStorageService.get('agencyLanguage');
    
    $scope.lan = $stateParams.lan ? $stateParams.lan : $scope.agencyLanguage;
    console.log($scope.lan);
    $stateParams.lan = $scope.lan;
    console.log($stateParams.lan);
    changeLanguage($scope.lan);


   
    function changeLanguage(key) {
//        alert(key)
        if ($scope.lan != 'en') {
            $scope.showLangBtn = 'en';
        } else {
            var getAgencyLan = localStorageService.get('agenLan');
            $scope.showLangBtn = getAgencyLan;
        }
        $translate.use(key);
    }

    $http.get('admin/ui/getAccount/' + $stateParams.accountId).success(function (response) {
        console.log(response)
        response.forEach(function (val, key) {
            $scope.userAccountName = val.accountName;
            $scope.userAccountLogo = val.logo;
        });
    });

    $http.get("admin/report/" + $stateParams.reportId).success(function (response) {
        $scope.reportPdfTitle = response.reportTitle;
        $scope.pdfLogo = response.logo;
    });

    $http.get('admin/report/reportWidget/' + $stateParams.reportId).success(function (response) {
        var widgetItems = response;
        $http.get("admin/ui/getChartColorByUserId").success(function (response) {
            $scope.userChartColors = response;
            var widgetColors;
            if (response.optionValue) {
                widgetColors = response.optionValue.split(',');
            }
            widgetItems.forEach(function (value, key) {
                value.widgetId.chartColors = widgetColors;
            });
            $scope.reportWidgets = widgetItems;
        }).error(function () {
            $scope.reportWidgets = widgetItems;
        });
        setInterval(function () {
            window.status = "done";
        }, 10000)
    });
    $scope.downloadUiPdf = function () {
        window.open("admin/pdf/download?windowStatus=done&url=" + encodeURIComponent(window.location.href));
    }
});