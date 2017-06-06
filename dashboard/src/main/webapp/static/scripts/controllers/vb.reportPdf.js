app.controller('ReportPdfController', function ($stateParams, $http, $scope, $filter) {
    
    $scope.reportPdfStartDate = $filter('date')(new Date($stateParams.startDate), 'MMM dd yyyy');//$filter(new Date($stateParams.startDate, 'MM/dd/yyyy'));
    $scope.reportPdfEndDate = $filter('date')(new Date($stateParams.endDate), 'MMM dd yyyy'); //$filter(new Date($stateParams.endDate, 'MM/dd/yyyy'));
    
    $http.get('admin/ui/getAccount/'+$stateParams.accountId).success(function (response) {
        console.log(response)
        response.forEach(function(val, key){
            $scope.userAccountName = val.accountName;
            $scope.userAccountLogo = val.logo;
        });
    });

    $http.get("admin/report/" + $stateParams.reportId).success(function (response) {
        $scope.reportPdfTitle = response.reportTitle;
        $scope.pdfLogo = response.logo;        
    });
    
    $http.get('admin/report/reportWidget/' + $stateParams.reportId).success(function (response) {
        $scope.reportWidgets = response;
        setInterval(function () {
            window.status = "done";
        }, 10000)
    });
    $scope.downloadUiPdf = function () {
        window.open("admin/pdf/download?windowStatus=done&url=" + encodeURIComponent(window.location.href));
    }
});