app.controller('IndexController', function ($scope, $http, $state, $filter, $stateParams) {
    $http.get('admin/user/account').success(function (response) {
        console.log(response)
        $stateParams.accountId = $stateParams.accountId ? $stateParams.accountId : response[0].id;
        console.log($stateParams.accountId)
        $scope.name = $filter('filter')(response, {id: $stateParams.accountId})[0];
        $http.get('admin/user/agencyProduct/' + $scope.name.agencyId.id).success(function (response) {
            console.log(response)
            $scope.products = response;
            if (!$stateParams.productId) {
                $stateParams.productId = response[0].id;
            }
            console.log($stateParams.productId)
            //$state.go("index.dashboard", {productId: $stateParams.productId});
        });
    });
//    $http.get('admin/ui/product').success(function (response) {
//        if (!response) {
//            return;
//        }
//        $scope.products = response;
//        var allowedProduct = [];
//        angular.forEach($scope.products, function (value, key) {
//            if (value.showProduct === true) {
//                allowedProduct.push(value);
//            }
//        });
//        var findProductId = allowedProduct[0].id;
//        if (!$stateParams.productId) {
//            alert("IndexController");
//            alert($stateParams.productId)
//            $state.go("index.dashboard", {productId: findProductId});
//        }
//    });

});