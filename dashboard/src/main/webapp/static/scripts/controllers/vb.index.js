app.controller('IndexController', function ($scope, $http, $state, $stateParams) {
    $http.get('admin/ui/product').success(function (response) {
        if (!response) {
            return;
        }
        $scope.products = response;
        var allowedProduct = [];
        angular.forEach($scope.products, function (value, key) {
            if (value.showProduct === true) {
                allowedProduct.push(value);
            }
        });
        var findProductId = allowedProduct[0].id;
        if (!$stateParams.productId) {
            alert("IndexController");
            alert($stateParams.productId)
            $state.go("index.dashboard", {productId: findProductId});
        }
    });
});