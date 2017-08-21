app.controller('FooterController', function ($scope, $cookies, $translate, $stateParams) {
    var d = new Date();
    $scope.year = d.getFullYear();

    $scope.agencyLanguage = $stateParams.lan;//$cookies.getObject("agencyLanguage");

    var lan = $scope.agencyLanguage;
    changeLanguage(lan);

    function changeLanguage(key) {
        $translate.use(key);
    }

});