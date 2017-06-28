app.controller('FavouritesPdfController', function ($stateParams, $http, $scope, $filter) {
    
    $scope.favPdfStartDate = $filter('date')(new Date($stateParams.startDate), 'MMM dd yyyy');//$filter(new Date($stateParams.startDate, 'MM/dd/yyyy'));
    $scope.favPdfEndDate = $filter('date')(new Date($stateParams.endDate), 'MMM dd yyyy'); //$filter(new Date($stateParams.endDate, 'MM/dd/yyyy'));
    
    $http.get('admin/ui/getAccount/'+$stateParams.accountId).success(function (response) {
        response.forEach(function(val, key){
            $scope.favAccountName = val.accountName;
            $scope.favAccountLogo = val.logo;
        });
    });
    
    $http.get("admin/tag/widgetTag/" + $stateParams.favouriteName).success(function (response) {
        $scope.favPdfWidgets = response;
        setInterval(function () {
            window.status = "done";
        }, 10000)
    });
    
   
    $scope.downloadUiPdf = function () {
        window.open("admin/pdf/download?windowStatus=done&url=" + encodeURIComponent(window.location.href));
    }
});