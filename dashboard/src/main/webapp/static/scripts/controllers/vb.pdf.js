app.controller('PdfController', function ($stateParams, $http, $scope, $filter, $cookies, $translate) {
    $scope.reportStartDate = $filter('date')(new Date($stateParams.startDate), 'MMM dd yyyy');//$filter(new Date($stateParams.startDate, 'MM/dd/yyyy'));
    $scope.reportEndDate = $filter('date')(new Date($stateParams.endDate), 'MMM dd yyyy'); //$filter(new Date($stateParams.endDate, 'MM/dd/yyyy'));
    $scope.pdfWidget = [];

    $scope.agencyLanguage = $stateParams.lan//$cookies.getObject("agencyLanguage");

    var lan = $scope.agencyLanguage;
    changeLanguage(lan);

    function changeLanguage(key) {
        $translate.use(key);
    }

    $http.get('admin/ui/getAccount/' + $stateParams.accountId).success(function (response) {
        response.forEach(function (val, key) {
            $scope.userAccountName = val.accountName;
            $scope.userAccountLogo = val.logo;
        });
    });

    $http.get('admin/ui/dashboardTemplate/' + $stateParams.productId).success(function (response) {
        $scope.templates = response;
        var template = $filter('filter')(response, {id: $stateParams.templateId})[0];
        $scope.templateName = template ? template.templateName : null;
    });

    $http.get("admin/ui/dbWidget/" + $stateParams.tabId + "/" + $stateParams.accountId).success(function (response) {
        var pdfProductName = response[0].tabId.agencyProductId ? response[0].tabId.agencyProductId.productName : null;
        $scope.userProductName = pdfProductName;
        var pdfWidgetItems = [];
        pdfWidgetItems = response;
        $http.get("admin/ui/getChartColorByUserId").success(function (response) {
            var widgetColors;
            if (response.optionValue) {
                widgetColors = response.optionValue.split(',');
            }
            pdfWidgetItems.forEach(function (value, key) {
                value.chartColors = widgetColors;
            });
            $scope.pdfWidgets = pdfWidgetItems;
        }).error(function (response) {
            $scope.pdfWidgets = pdfWidgetItems;
        });
        setInterval(function () {
            window.status = "done";
        }, 10000);
    });

    $scope.downloadUiPdf = function () {
        window.open("admin/pdf/download?windowStatus=done&url=" + encodeURIComponent(window.location.href));
    };
});