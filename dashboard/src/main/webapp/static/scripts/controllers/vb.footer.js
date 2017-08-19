app.controller('FooterController', function ($scope,$cookies,$translate) {
    var d = new Date();
    $scope.year = d.getFullYear();
    
    //Chinese Translation
    
    $scope.agencyLanguage = $cookies.getObject("agencyLanguage");

    console.log($scope.agencyLanguage);

    var lan = $scope.agencyLanguage ? $scope.agencyLanguage : null;
    changeLanguage(lan);

    function changeLanguage(key) {
        $translate.use(key);
    }

});