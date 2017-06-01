//i = 1;
app.controller('PdfController', function ($stateParams, $http, $scope, $filter) {
//    $scope.userAccountName = $stateParams.accountName;
    $scope.reportStartDate = $filter('date')(new Date($stateParams.startDate), 'MMM dd yyyy');//$filter(new Date($stateParams.startDate, 'MM/dd/yyyy'));
    $scope.reportEndDate = $filter('date')(new Date($stateParams.endDate), 'MMM dd yyyy'); //$filter(new Date($stateParams.endDate, 'MM/dd/yyyy'));
    $scope.pdfWidget = [];
    
//    $http.get('admin/user/agencyProduct/' + $stateParams.productId).success(function (response) {
//        console.log(response)
//    })
    $http.get('admin/ui/getAccount/'+$stateParams.accountId).success(function (response) {
        console.log(response)
        response.forEach(function(val, key){
            $scope.userAccountName = val.accountName;
            $scope.userAccountLogo = val.logo;
//            console.log(val.accountId.id)
//            console.log($stateParams.accountId)
//            console.log(val)
//            if(val.accountId.id == $stateParams.accountId){
//                $scope.userAccountName = val.accountId.accountName;
//                $scope.userAccountLogo = val.accountId.logo
//                console.log(val)
//            }
        });
    });
    $http.get("admin/ui/dbWidget/" + $stateParams.tabId).success(function (response) {
//        console.log(response[0])
        $scope.userProductName = response[0].tabId.agencyProductId.productName;
//        response.forEach(function(val, k){
//            console.log(val.tabId.agencyProductId.productName)
//        })
        $scope.pdfWidgets = response;
//        i++;
        // window.status = "done" + i
        setInterval(function () {
            window.status = "done";
        }, 10000)
    });

    $scope.downloadUiPdf = function () {
        window.open("admin/pdf/download?windowStatus=done&url=" + encodeURIComponent(window.location.href));
    }
});