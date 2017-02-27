app.controller('AccountIndexController', function ($scope, $state, $stateParams) {
    $scope.$state = $state;
    $scope.startDate = $stateParams.startDate;
    $scope.endDate = $stateParams.endDate;
    $scope.locationId = $stateParams.locationId;
});