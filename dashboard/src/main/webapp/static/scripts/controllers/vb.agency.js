app.controller('AgencyController', function ($scope, $http) {
//Tabs
    $scope.tab = 1;
    $scope.setTabAgency = function (newTab) {
        $scope.tab = newTab;
    };
    $scope.isSetAgency = function (tabNum) {
        return $scope.tab === tabNum;
    };
    $scope.addAgency = function () {
        $scope.agency = {logo: "static/img/logos/deeta-logo.png"};
    };
    $scope.agencies = [];
    function getAgency() {
        $http.get('admin/user/agency').success(function (response) {
            $scope.agencies = response;
            $scope.editAgency(response[0], 0);
        });
    }
    getAgency();
    $scope.agency = {logo: "static/img/logos/deeta-logo.png"}; //Logo Upload
    $scope.imageUpload = function (event) {
        var files = event.target.files;
        for (var i = 0; i < files.length; i++) {
            var file = files[i];
            var reader = new FileReader();
            reader.onload = $scope.imageIsLoaded;
            reader.readAsDataURL(file);
        }
    };
    $scope.imageIsLoaded = function (e) {
        $scope.$apply(function () {
            $scope.agency.logo = e.target.result;
        });
    };
    $scope.saveAgency = function (agency) {
        if ($scope.agency.logo === "static/img/logos/deeta-logo.png") {
            $scope.agency.logo = "";
        }
        var data = {
            id: agency.id,
            agencyName: agency.agencyName,
            email: agency.email,
            description: agency.description,
            status: agency.status,
            logo: $scope.agency.logo
        };
        $http({method: agency.id ? 'PUT' : 'POST', url: 'admin/user/agency', data: data}).success(function (response) {
            //getAgency();
        });
        $scope.agency = {logo: "static/img/logos/deeta-logo.png"};
    };
    $scope.selectedRow = null;
    $scope.editAgency = function (agency, index) {
        getAgencyLicence(agency)
        $scope.agencyLicenceId = agency;
        var data = {
            id: agency.id,
            agencyName: agency.agencyName,
            email: agency.email,
            description: agency.description,
            status: agency.status,
            logo: agency.logo ? agency.logo : "static/img/logos/deeta-logo.png"
        };
        $scope.agency = data;
        $scope.selectedRow = index;
    };
    $scope.clearAgency = function () {
        $scope.agency = {logo: "static/img/logos/deeta-logo.png"};
    };
    $scope.deleteAgency = function (agency, index) {
        $http({method: 'DELETE', url: 'admin/user/agency/' + agency.id}).success(function (response) {
            $scope.agencies.splice(index, 1);
        });
    };
    function getAgencyLicence(agency) {
        $http.get('admin/user/agencyLicence/' + agency.id).success(function (response) {
            $scope.agencyLicence = {}
            $scope.agencyLicences = response;
            console.log($scope.agencyLicences)
            angular.forEach(response, function (value, key) {
                $scope.agencyLicence = {id: value.id, maxNoTab: value.maxNoTab, maxNoUser: value.maxNoUser, maxNoClient: value.maxNoClient, maxNoAccount: value.maxNoAccount, expiryDate: value.expiryDate, maxNoWidgetPerTab: value.maxNoWidgetPerTab}
                console.log($scope.agencyLicence)
            })
        });
    }

    $scope.saveAgencyLicence = function (agencyLicence) {
        console.log($scope.agencyLicenceId)
        var data = {
            id: agencyLicence.id,
            agencyId: $scope.agencyLicenceId.id,
            maxNoTab: agencyLicence.maxNoTab,
            maxNoUser: agencyLicence.maxNoUser,
            maxNoClient: agencyLicence.maxNoClient,
            maxNoAccount: agencyLicence.maxNoAccount,
            expiryDate: new Date(agencyLicence.expiryDate),
            maxNoWidgetPerTab: agencyLicence.maxNoWidgetPerTab
        };
        $http({method: agencyLicence.id ? 'PUT' : 'POST', url: 'admin/user/agencyLicence', data: data}).success(function (response) {
            $scope.agencyLicences = response;
        });
    };
});
app.directive("datePicker", function () {
    return {
        restrict: "A",
        link: function (scope, el, attr) {
            el.datepicker({
                dateFormat: 'yy-mm-dd'
            });
        }
    };
});