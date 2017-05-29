i = 1;
app.controller('PdfController', function ($stateParams, $http, $scope) {
    $scope.userAccountName = $stateParams.accountName;
    $scope.userProductName = $stateParams.productName;
    $scope.reportStartDate = new Date($stateParams.startDate);
    $scope.reportEndDate = new Date($stateParams.endDate);
    $scope.pdfWidget = [];
    $http.get("admin/ui/dbWidget/" + $stateParams.tabId).success(function (response) {
        $scope.pdfWidgets = response;
        i++;
        // window.status = "done" + i
        setInterval(function () {
            window.status = "done";
        }, 10000)
    });

    $scope.downloadUiPdf = function () {
        window.open("admin/pdf/download?windowStatus=done&url=" + encodeURIComponent(window.location.href));
    }
});