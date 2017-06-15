app.controller('PdfController', function ($stateParams, $http, $scope, $filter) {
    $scope.reportStartDate = $filter('date')(new Date($stateParams.startDate), 'MMM dd yyyy');//$filter(new Date($stateParams.startDate, 'MM/dd/yyyy'));
    $scope.reportEndDate = $filter('date')(new Date($stateParams.endDate), 'MMM dd yyyy'); //$filter(new Date($stateParams.endDate, 'MM/dd/yyyy'));
    $scope.pdfWidget = [];
    
    $http.get('admin/ui/getAccount/'+$stateParams.accountId).success(function (response) {
        console.log(response)
        response.forEach(function(val, key){
            $scope.userAccountName = val.accountName;
            $scope.userAccountLogo = val.logo;
        });
    });
    $http.get("admin/ui/dbWidget/" + $stateParams.tabId).success(function (response) {
        $scope.userProductName = response[0].tabId.agencyProductId.productName;
        $scope.pdfWidgets = response;
        setInterval(function () {
            window.status = "done";
        }, 10000)
    });

    $scope.downloadUiPdf = function () {
        window.open("admin/pdf/download?windowStatus=done&url=" + encodeURIComponent(window.location.href));
    }
});